package nl.rwslinkman.simdeviceble.device.model

import android.text.Editable
import java.util.*

interface Characteristic {

    enum class Type {
        number,
        decimal,
        text
    }

    val name: String
    val uuid: UUID
//    val type: Type // TODO:

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
    fun convertToBytes(value: Editable): ByteArray
}