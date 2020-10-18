package nl.rwslinkman.simdeviceble.device.model

import android.util.Log
import java.util.*

abstract class Device {

    abstract val name: String
    abstract val primaryServiceUuid: UUID
    abstract val services: List<Service>

    fun getCharacteristic(uuid: UUID?): Characteristic? {
        val result = services.flatMap {
            it.characteristics
        }.first { uuid == it.uuid }
        Log.d(TAG, "getCharacteristic: uuid found $result")
        return result
    }

    companion object {
        const val TAG = "Device"
    }
}