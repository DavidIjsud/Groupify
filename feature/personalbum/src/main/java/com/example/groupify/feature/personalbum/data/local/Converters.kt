// feature/personalbum/src/main/.../data/local/Converters.kt
package com.example.groupify.feature.personalbum.data.local

import androidx.room.TypeConverter
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Converters {

    @TypeConverter
    fun floatArrayToByteArray(value: FloatArray): ByteArray {
        val buffer = ByteBuffer.allocate(value.size * 4)
        buffer.order(ByteOrder.nativeOrder())
        buffer.asFloatBuffer().put(value)
        return buffer.array()
    }

    @TypeConverter
    fun byteArrayToFloatArray(value: ByteArray): FloatArray {
        val buffer = ByteBuffer.wrap(value)
        buffer.order(ByteOrder.nativeOrder())
        val floatBuffer = buffer.asFloatBuffer()
        val result = FloatArray(floatBuffer.remaining())
        floatBuffer.get(result)
        return result
    }
}
