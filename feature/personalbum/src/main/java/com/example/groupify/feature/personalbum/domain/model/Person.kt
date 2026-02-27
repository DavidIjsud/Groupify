// feature/personalbum/src/main/.../domain/model/Person.kt
package com.example.groupify.feature.personalbum.domain.model

data class Person(
    val id: String,
    val name: String,
    val referenceEmbedding: FloatArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Person) return false
        return id == other.id &&
            name == other.name &&
            referenceEmbedding.contentEquals(other.referenceEmbedding)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + referenceEmbedding.contentHashCode()
        return result
    }
}
