package nl.rwslinkman.simdeviceble.device.model

import java.util.*

interface Characteristic {

    enum class Type {
        Number,
        Text
    }

    val name: String
    val uuid: UUID
    val type: Type

    val isRead: Boolean
        get() = false

    val isWrite: Boolean
        get() = false

    val isNotify: Boolean
        get() = false

    val isIndicate: Boolean
        get() = false

    val description: String?
        get() = null

    val initialValue: ByteArray?
        get() = null

    fun validateWrite(offset: Int, value: ByteArray?): Int
    fun convertToPresentable(value: ByteArray): String
    fun convertToBytes(value: String): ByteArray
}