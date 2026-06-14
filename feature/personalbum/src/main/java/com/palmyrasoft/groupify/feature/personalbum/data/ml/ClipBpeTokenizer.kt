// feature/personalbum/src/main/.../data/ml/ClipBpeTokenizer.kt
package com.palmyrasoft.groupify.feature.personalbum.data.ml

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Kotlin port of OpenAI CLIP's byte-pair-encoding `SimpleTokenizer`, used to turn a text query into
 * the `input_ids` the MobileCLIP text encoder expects. Faithful to the reference implementation:
 * GPT-2 byte→unicode mapping, the canonical BPE merges (first 48,894 entries of
 * `clip/bpe_simple_vocab_16e6.txt`), and the CLIP regex token pattern. Produces a fixed-length
 * context window (77) padded with 0, bracketed by `<|startoftext|>` / `<|endoftext|>`.
 *
 * @Singleton: the merge table + vocab (~49,408 entries) are parsed once on first use.
 */
@Singleton
class ClipBpeTokenizer @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    // byte (0..255) -> printable unicode char, matching GPT-2/CLIP bytes_to_unicode().
    private val byteEncoder: Map<Int, Char>
    // BPE merge rank: lower rank = merged earlier.
    private val bpeRanks: Map<Pair<String, String>, Int>
    // vocab token string -> id.
    private val encoder: Map<String, Int>
    private val bpeCache = HashMap<String, String>()

    private val startOfText: Int
    private val endOfText: Int

    init {
        // ---- bytes_to_unicode(): build parallel byte/char lists, preserving order ----
        val bs = ArrayList<Int>()
        for (c in '!'.code..'~'.code) bs.add(c)
        for (c in '¡'.code..'¬'.code) bs.add(c)
        for (c in '®'.code..'ÿ'.code) bs.add(c)
        val cs = ArrayList(bs)
        var n = 0
        for (b in 0 until 256) {
            if (b !in bs) {
                bs.add(b)
                cs.add(256 + n)
                n++
            }
        }
        val byteEnc = HashMap<Int, Char>(bs.size)
        val vocabBase = ArrayList<String>(bs.size)
        for (i in bs.indices) {
            val ch = cs[i].toChar()
            byteEnc[bs[i]] = ch
            vocabBase.add(ch.toString())
        }
        byteEncoder = byteEnc

        // ---- BPE merges: drop the header line, take the canonical 48,894 merges ----
        val lines = context.assets.open(VOCAB_ASSET).bufferedReader(Charsets.UTF_8).readLines()
        val mergeLines = lines.subList(1, MERGE_END)
        val ranks = HashMap<Pair<String, String>, Int>(mergeLines.size)
        val merges = ArrayList<String>(mergeLines.size)
        mergeLines.forEachIndexed { index, line ->
            val sep = line.indexOf(' ')
            if (sep > 0) {
                val first = line.substring(0, sep)
                val second = line.substring(sep + 1)
                ranks[first to second] = index
                merges.add(first + second)
            }
        }
        bpeRanks = ranks

        // ---- vocab = 256 base + 256 with </w> + merges + 2 special tokens ----
        val vocab = ArrayList<String>(vocabBase.size * 2 + merges.size + 2)
        vocab.addAll(vocabBase)
        vocab.addAll(vocabBase.map { it + END_WORD })
        vocab.addAll(merges)
        vocab.add(START_TOKEN)
        vocab.add(END_TOKEN)
        val enc = HashMap<String, Int>(vocab.size)
        vocab.forEachIndexed { index, token -> enc[token] = index }
        encoder = enc

        startOfText = enc.getValue(START_TOKEN)
        endOfText = enc.getValue(END_TOKEN)
    }

    /**
     * Tokenizes [text] into a fixed [contextLength] window of token ids (zero-padded), bracketed by
     * the start/end tokens. Truncates over-long inputs, always keeping the end token last.
     */
    fun tokenize(text: String, contextLength: Int = CONTEXT_LENGTH): IntArray {
        val tokens = ArrayList<Int>()
        tokens.add(startOfText)
        tokens.addAll(encode(text))
        tokens.add(endOfText)

        val result = IntArray(contextLength) // zero-padded
        if (tokens.size > contextLength) {
            for (i in 0 until contextLength) result[i] = tokens[i]
            result[contextLength - 1] = endOfText
        } else {
            for (i in tokens.indices) result[i] = tokens[i]
        }
        return result
    }

    private fun encode(text: String): List<Int> {
        val cleaned = text.replace(WHITESPACE, " ").trim().lowercase()
        val ids = ArrayList<Int>()
        val matcher = PATTERN.matcher(cleaned)
        while (matcher.find()) {
            val token = matcher.group()
            // Byte-encode the UTF-8 bytes into the GPT-2 unicode space.
            val sb = StringBuilder()
            for (byte in token.toByteArray(Charsets.UTF_8)) {
                byteEncoder[byte.toInt() and 0xFF]?.let { sb.append(it) }
            }
            for (piece in bpe(sb.toString()).split(' ')) {
                encoder[piece]?.let { ids.add(it) }
            }
        }
        return ids
    }

    private fun bpe(token: String): String {
        if (token.isEmpty()) return token
        bpeCache[token]?.let { return it }

        // word starts as a list of single chars; the last char carries the </w> end marker.
        var word = ArrayList<String>(token.length)
        for (ch in token) word.add(ch.toString())
        word[word.size - 1] = word[word.size - 1] + END_WORD

        var pairs = getPairs(word)
        if (pairs.isEmpty()) {
            val result = token + END_WORD
            bpeCache[token] = result
            return result
        }

        while (true) {
            // Pick the lowest-rank adjacent pair present in the merge table.
            var bigram: Pair<String, String>? = null
            var minRank = Int.MAX_VALUE
            for (pair in pairs) {
                val rank = bpeRanks[pair] ?: continue
                if (rank < minRank) {
                    minRank = rank
                    bigram = pair
                }
            }
            if (bigram == null) break

            val (first, second) = bigram
            val newWord = ArrayList<String>(word.size)
            var i = 0
            while (i < word.size) {
                val j = indexOfFrom(word, first, i)
                if (j == -1) {
                    for (k in i until word.size) newWord.add(word[k])
                    break
                }
                for (k in i until j) newWord.add(word[k])
                i = j
                if (word[i] == first && i < word.size - 1 && word[i + 1] == second) {
                    newWord.add(first + second)
                    i += 2
                } else {
                    newWord.add(word[i])
                    i += 1
                }
            }
            word = newWord
            if (word.size == 1) break
            pairs = getPairs(word)
        }

        val result = word.joinToString(" ")
        bpeCache[token] = result
        return result
    }

    private fun getPairs(word: List<String>): Set<Pair<String, String>> {
        val pairs = LinkedHashSet<Pair<String, String>>()
        for (i in 0 until word.size - 1) pairs.add(word[i] to word[i + 1])
        return pairs
    }

    private fun indexOfFrom(word: List<String>, value: String, from: Int): Int {
        for (i in from until word.size) if (word[i] == value) return i
        return -1
    }

    companion object {
        private const val VOCAB_ASSET = "clip/bpe_simple_vocab_16e6.txt"
        const val CONTEXT_LENGTH = 77

        private const val END_WORD = "</w>"
        private const val START_TOKEN = "<|startoftext|>"
        private const val END_TOKEN = "<|endoftext|>"

        // Canonical CLIP slice: merges = lines[1 : 49152 - 256 - 2 + 1] -> [1, 48895).
        private const val MERGE_END = 49152 - 256 - 2 + 1

        private val WHITESPACE = Regex("\\s+")

        // CLIP token pattern (special tokens, common contractions, letters, digits, symbols).
        private val PATTERN: Pattern = Pattern.compile(
            "<\\|startoftext\\|>|<\\|endoftext\\|>|'s|'t|'re|'ve|'m|'ll|'d|\\p{L}+|\\p{N}|[^\\s\\p{L}\\p{N}]+",
            Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE,
        )
    }
}
