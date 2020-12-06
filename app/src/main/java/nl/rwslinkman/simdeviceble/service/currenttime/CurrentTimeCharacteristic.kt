package nl.rwslinkman.simdeviceble.service.currenttime

import android.bluetooth.BluetoothGatt
import nl.rwslinkman.simdeviceble.bluetooth.BluetoothBytesParser
import nl.rwslinkman.simdeviceble.bluetooth.BluetoothUUID
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CurrentTimeCharacteristic: Characteristic {
    override val name: String
        get() = "Current Time"
    override val uuid: UUID
        get() = BluetoothUUID.fromSigNumber("2A2B")
    override val type: Characteristic.Type
        get() = Characteristic.Type.Text
    override val isRead: Boolean
        get() = true
    override val isNotify: Boolean
        get() = true
    private val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH)

    override fun validateWrite(offset: Int, value: ByteArray?): Int {
        return BluetoothGatt.GATT_SUCCESS
    }

    override fun convertToPresentable(value: ByteArray): String {
        val parser = BluetoothBytesParser(value)
        val dateTime = parser.dateTime
        return formatter.format(dateTime)
    }

    override fun convertToBytes(value: String): ByteArray {
        val cal = Calendar.getInstance()
        try {
            val parsedDate = formatter.parse(value)
            cal.time = parsedDate!!
        } catch (ignored: ParseException) {
             // ignore and use phone's current datetime from Calendar.getInstance()
        }

        val parser = BluetoothBytesParser()
        parser.setDateTime(cal)
        return parser.value
    }
}