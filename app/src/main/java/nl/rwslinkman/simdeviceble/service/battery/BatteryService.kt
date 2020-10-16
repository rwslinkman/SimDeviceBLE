package nl.rwslinkman.simdeviceble.service.battery

import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Service
import java.util.*

class BatteryService: Service {

//    mBatteryLevelCharacteristic =
//    new BluetoothGattCharacteristic(BATTERY_LEVEL_UUID,
//    BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
//    BluetoothGattCharacteristic.PERMISSION_READ);
//
//    mBatteryLevelCharacteristic.addDescriptor(
//    Peripheral.getClientCharacteristicConfigurationDescriptor());
//
//    mBatteryLevelCharacteristic.addDescriptor(
//    Peripheral.getCharacteristicUserDescriptionDescriptor(BATTERY_LEVEL_DESCRIPTION));
//
//    mBatteryService = new BluetoothGattService(BATTERY_SERVICE_UUID,
//    BluetoothGattService.SERVICE_TYPE_PRIMARY);
//    mBatteryService.addCharacteristic(mBatteryLevelCharacteristic);

    override val uuid: UUID
        get() = SERVICE_UUID

    override val characteristics: List<Characteristic>
        get() = listOf(
            BatteryLevelCharacteristic()
        )

    companion object {
        private val SERVICE_UUID = UUID.fromString("0000180F-0000-1000-8000-00805f9b34fb")

        private const val INITIAL_BATTERY_LEVEL = 50
        private const val BATTERY_LEVEL_MAX = 100
    }
}