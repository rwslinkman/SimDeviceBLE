package nl.rwslinkman.simdeviceble.device.heartrate

import nl.rwslinkman.simdeviceble.device.model.Device
import nl.rwslinkman.simdeviceble.device.model.Service
import nl.rwslinkman.simdeviceble.service.BatteryService
import nl.rwslinkman.simdeviceble.service.HeartRateService
import java.util.*

class HeartRatePeripheral: Device {
//
//    mHeartRateMeasurementCharacteristic =
//    new BluetoothGattCharacteristic(HEART_RATE_MEASUREMENT_UUID,
//    BluetoothGattCharacteristic.PROPERTY_NOTIFY,
//    /* No permissions */ 0);
//
//    mHeartRateMeasurementCharacteristic.addDescriptor(
//    Peripheral.getClientCharacteristicConfigurationDescriptor());
//
//    mHeartRateMeasurementCharacteristic.addDescriptor(
//    Peripheral.getCharacteristicUserDescriptionDescriptor(HEART_RATE_MEASUREMENT_DESCRIPTION));
//
//    mBodySensorLocationCharacteristic =
//    new BluetoothGattCharacteristic(BODY_SENSOR_LOCATION_UUID,
//    BluetoothGattCharacteristic.PROPERTY_READ,
//    BluetoothGattCharacteristic.PERMISSION_READ);
//
//    mHeartRateControlPoint =
//    new BluetoothGattCharacteristic(HEART_RATE_CONTROL_POINT_UUID,
//    BluetoothGattCharacteristic.PROPERTY_WRITE,
//    BluetoothGattCharacteristic.PERMISSION_WRITE);
//
//    mHeartRateService = new BluetoothGattService(HEART_RATE_SERVICE_UUID,
//    BluetoothGattService.SERVICE_TYPE_PRIMARY);
//    mHeartRateService.addCharacteristic(mHeartRateMeasurementCharacteristic);
//    mHeartRateService.addCharacteristic(mBodySensorLocationCharacteristic);
//    mHeartRateService.addCharacteristic(mHeartRateControlPoint);

    companion object {
        val SERVICE_UUID = UUID.fromString("00001809-0000-1000-8000-00805f9b34fb");
    }

    override val name: String
        get() = "Fitness Band (Chest)"
    override val primaryServiceUuid: UUID
        get() = SERVICE_UUID
    override val services: List<Service>
        get() = listOf(
            HeartRateService(),
            BatteryService()
        )
}