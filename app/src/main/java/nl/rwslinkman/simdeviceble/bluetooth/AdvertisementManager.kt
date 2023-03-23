package nl.rwslinkman.simdeviceble.bluetooth

import android.bluetooth.*
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.os.ParcelUuid
import android.util.Log
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.device.model.Device
import java.util.*

class AdvertisementManager(
    private val context: Context,
    bluetoothAdapter: BluetoothAdapter,
    var listener: Listener? = null
) {
    interface Listener {
        fun updateDataContainer(
            characteristic: Characteristic,
            data: ByteArray,
            isInitialValue: Boolean
        )

        fun setIsAdvertising(isAdvertising: Boolean, advertisedDevice: String? = null)
        fun onDeviceConnected(deviceAddress: String)
        fun onDeviceDisconnected(deviceAddress: String)
    }

    // Overall used variables
    private val bluetoothManager: BluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val advertiser = bluetoothAdapter.bluetoothLeAdvertiser
    private var gattServer: BluetoothGattServer? = null
    private val connectedDevices: MutableMap<String, BluetoothDevice> = mutableMapOf()

    // Only used when advertising
    private lateinit var addServiceQueue: Queue<BluetoothGattService>
    private lateinit var advertiseCommand: AdvertiseCommand

    private val gattCallback = object : BluetoothGattServerCallback() {
        override fun onConnectionStateChange(device: BluetoothDevice?, status: Int, newState: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    device?.let {
                        Log.i(
                            TAG,
                            "onConnectionStateChange: connected to ${it.name} [${it.address}]"
                        )
                        onDeviceConnected(it)
                    }
                } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    device?.let {
                        Log.i(
                            TAG,
                            "onConnectionStateChange: disconnected from ${device.name} [${device.address}]"
                        )
                        onDeviceDisconnected(device)
                    }
                }
            } else {
                device?.let {
                    Log.i(
                        TAG,
                        "onConnectionStateChange: Connect error while connecting ${device.name} [${device.address}]"
                    )
                    onDeviceDisconnected(device)
                }
            }
        }

        override fun onCharacteristicReadRequest(
            device: BluetoothDevice?,
            requestId: Int,
            offset: Int,
            characteristic: BluetoothGattCharacteristic?
        ) {
            Log.i(
                TAG,
                "onCharacteristicReadRequest: Device tried to read characteristic: " + characteristic?.uuid
            )
            Log.i(TAG, "onCharacteristicReadRequest: current value ${characteristic?.value}")
            if (offset != 0) {
                gattServer?.sendResponse(
                    device,
                    requestId,
                    BluetoothGatt.GATT_INVALID_OFFSET,
                    offset,
                    null
                )
                return
            }
            gattServer?.sendResponse(
                device,
                requestId,
                BluetoothGatt.GATT_SUCCESS,
                offset,
                characteristic?.value
            )
        }

        override fun onCharacteristicWriteRequest(
            device: BluetoothDevice?,
            requestId: Int,
            characteristic: BluetoothGattCharacteristic?,
            preparedWrite: Boolean,
            responseNeeded: Boolean,
            offset: Int,
            value: ByteArray?
        ) {
            Log.i(TAG, "onCharacteristicWriteRequest: Characteristic Write request")

            var status: Int = BluetoothGatt.GATT_SUCCESS
            val characteristicModel =
                advertiseCommand.device.getCharacteristic(characteristic?.uuid)
            characteristicModel?.let { charModel ->
                // Let characteristic decide if write value is OK
                status = characteristicModel.validateWrite(offset, value)

                value?.let {
                    characteristic?.value = value
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        listener?.updateDataContainer(charModel, it, false)
                    }
                }
            }

            if (responseNeeded) {
                gattServer?.sendResponse(device, requestId, status, 0, null)
            }
        }

        override fun onDescriptorReadRequest(
            device: BluetoothDevice?,
            requestId: Int,
            offset: Int,
            descriptor: BluetoothGattDescriptor?
        ) {
            Log.i(
                TAG,
                "onDescriptorReadRequest: Device tried to read descriptor: " + descriptor?.uuid
            )
            if (offset != 0) {
                gattServer?.sendResponse(
                    device,
                    requestId,
                    BluetoothGatt.GATT_INVALID_OFFSET,
                    offset,
                    null
                )
                return
            }
            gattServer?.sendResponse(
                device,
                requestId,
                BluetoothGatt.GATT_SUCCESS,
                offset,
                descriptor?.value
            )
        }

        override fun onDescriptorWriteRequest(
            device: BluetoothDevice?,
            requestId: Int,
            descriptor: BluetoothGattDescriptor?,
            preparedWrite: Boolean,
            responseNeeded: Boolean,
            offset: Int,
            value: ByteArray?
        ) {
            Log.i(TAG, "Descriptor Write Request " + descriptor?.uuid)
            val status: Int
            if (descriptor?.uuid == CLIENT_CHARACTERISTIC_CONFIGURATION_UUID) {
                val characteristic = descriptor?.characteristic
                val supportsNotifications =
                    (characteristic?.properties != null && BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0)
                val supportsIndications =
                    (characteristic?.properties != null && BluetoothGattCharacteristic.PROPERTY_INDICATE != 0)
                if (!(supportsNotifications || supportsIndications)) {
                    status = BluetoothGatt.GATT_REQUEST_NOT_SUPPORTED
                } else if (value?.size != 2) {
                    status = BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH
                } else if (Arrays.equals(
                        value,
                        BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
                    )
                ) {
                    status = BluetoothGatt.GATT_SUCCESS
                    descriptor?.value = value
                } else if (supportsNotifications && Arrays.equals(
                        value,
                        BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    )
                ) {
                    status = BluetoothGatt.GATT_SUCCESS
                    descriptor?.value = value
                } else if (supportsIndications && Arrays.equals(
                        value,
                        BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
                    )
                ) {
                    status = BluetoothGatt.GATT_SUCCESS
                    descriptor?.value = value
                } else {
                    status = BluetoothGatt.GATT_REQUEST_NOT_SUPPORTED
                }
            } else {
                status = BluetoothGatt.GATT_SUCCESS
                descriptor?.value = value
            }
            if (responseNeeded) {
                gattServer?.sendResponse(device, requestId, status, 0, null)
            }
        }

        override fun onServiceAdded(status: Int, service: BluetoothGattService?) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // Services must be added sequentially
                val nextItem = addServiceQueue.poll()
                if (nextItem == null) {
                    startAdvertisement()
                } else {
                    gattServer?.addService(nextItem)
                }
            }
        }
    }
    private val scanCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            super.onStartSuccess(settingsInEffect)
            Log.d(TAG, "onStartSuccess: ")
            listener?.setIsAdvertising(true, advertiseCommand.device.name)
        }

        override fun onStartFailure(errorCode: Int) {
            super.onStartFailure(errorCode)
            Log.d(TAG, "onStartFailure: ")
        }
    }

    //region Public methods
    fun advertise(command: AdvertiseCommand) {
        gattServer = bluetoothManager.openGattServer(context, gattCallback)
        if (gattServer == null) {
            Log.e(TAG, "advertise: Unable to open GATT server")
            return
        }

        advertiseCommand = command // Store for later

        val gattServices = createServices(command.device)
        addServiceQueue = LinkedList(gattServices)

        if (addServiceQueue.isEmpty()) {
            startAdvertisement()
        } else {
            gattServer?.addService(addServiceQueue.poll())
        }
    }

    fun stop() {
        gattServer?.clearServices()
        gattServer?.close()
        gattServer = null

        advertiser?.stopAdvertising(scanCallback)
        listener?.setIsAdvertising(false)
    }

    fun updateAdvertisedCharacteristics(characteristicData: MutableMap<UUID, ByteArray>) {
        getAdvertisedGattCharacteristics()?.forEach {
            // Gather all advertised characteristics to check if update can be set
            val updateValue: ByteArray? = characteristicData[it.uuid]
            updateValue?.let { upVal ->
                it.value = upVal
            }
        }
    }

    fun sendNotificationToConnectedDevices(uuid: UUID) {
        val characteristic = advertiseCommand.device.getCharacteristic(uuid)
        characteristic?.let {
            sendNotificationToConnectedDevices(it)
        }
    }


    fun sendNotificationToConnectedDevices(characteristic: Characteristic) {
        val indicate = characteristic.isIndicate
        val notifyMe: BluetoothGattCharacteristic? = getAdvertisedGattCharacteristics()?.first {
            it.uuid == characteristic.uuid
        }

        notifyMe?.let {
            for (device in connectedDevices.values) {
                gattServer?.notifyCharacteristicChanged(device, it, indicate)
            }
        }
    }

    fun getAdvertisedCharacteristicValues(): Map<Characteristic, ByteArray> {
        val device = advertiseCommand.device
        val result = mutableMapOf<Characteristic, ByteArray>()
        getAdvertisedGattCharacteristics()?.forEach { btChar ->
            val char = device.getCharacteristic(btChar.uuid)
            char?.let {
                var btValue = byteArrayOf()
                if (btChar.value !== null) {
                    btValue = btChar.value
                }
                result[char] = btValue
            }
        }
        return result
    }
    //endregion

    //region private methods
    private fun createServices(device: Device): List<BluetoothGattService> {
        return device.services.map { service ->
            val gattService =
                BluetoothGattService(service.uuid, BluetoothGattService.SERVICE_TYPE_PRIMARY)

            service.characteristics
                .map {
                    // Map to GATT Characteristic
                    val gattCharacteristic = createGattCharacteristic(it)

                    if (it.isNotify || it.isIndicate) {
                        val cccDescriptor = createClientCharacteristicConfigurationDescriptor()
                        gattCharacteristic.addDescriptor(cccDescriptor)
                    }

                    it.description?.let { descr ->
                        val descriptor = createUserDescriptionDescriptor(descr)
                        gattCharacteristic.addDescriptor(descriptor)
                    }

                    val initialValue: ByteArray? = it.initialValue
                    initialValue?.let { initVal ->
                        gattCharacteristic.value = initialValue
                        listener?.updateDataContainer(it, initVal, true)
                    }

                    gattCharacteristic
                }
                .forEach {
                    gattService.addCharacteristic(it)
                }
            gattService
        }
    }

    private fun createGattCharacteristic(characteristic: Characteristic): BluetoothGattCharacteristic {
        var properties = 0
        var permissions = 0
        if (characteristic.isRead) {
            properties = properties.or(BluetoothGattCharacteristic.PROPERTY_READ)
            permissions = permissions.or(BluetoothGattCharacteristic.PERMISSION_READ)
        }
        if (characteristic.isWrite) {
            properties = properties.or(BluetoothGattCharacteristic.PROPERTY_WRITE)
            permissions = permissions.or(BluetoothGattCharacteristic.PERMISSION_WRITE)
        }
        if (characteristic.isNotify) {
            properties = properties.or(BluetoothGattCharacteristic.PROPERTY_NOTIFY)
        }
        if (characteristic.isIndicate) {
            properties = properties.or(BluetoothGattCharacteristic.PROPERTY_INDICATE)
        }
        return BluetoothGattCharacteristic(characteristic.uuid, properties, permissions)
    }

    private fun createClientCharacteristicConfigurationDescriptor(): BluetoothGattDescriptor {
        val descriptor = BluetoothGattDescriptor(
            CLIENT_CHARACTERISTIC_CONFIGURATION_UUID,
            BluetoothGattDescriptor.PERMISSION_READ or BluetoothGattDescriptor.PERMISSION_WRITE
        )
        descriptor.value = byteArrayOf(0, 0)
        return descriptor
    }

    private fun createUserDescriptionDescriptor(description: String): BluetoothGattDescriptor {
        val descriptor = BluetoothGattDescriptor(
            USER_DESCRIPTION_CHARACTERISTIC_UUID,
            BluetoothGattDescriptor.PERMISSION_READ.or(BluetoothGattDescriptor.PERMISSION_WRITE)
        )
        descriptor.value = description.toByteArray()
        return descriptor
    }

    private fun startAdvertisement() {
        val mAdvSettings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
            .setConnectable(advertiseCommand.isConnectable)
            .build()
        val mAdvData = AdvertiseData.Builder()
            .setIncludeTxPowerLevel(true)
            .addServiceUuid(ParcelUuid(advertiseCommand.device.primaryServiceUuid))
            .build()

        val mAdvScanResponse = AdvertiseData.Builder()
            .setIncludeDeviceName(advertiseCommand.includeDeviceName)
            .build()
        advertiser.startAdvertising(mAdvSettings, mAdvData, mAdvScanResponse, scanCallback)
    }

    private fun onDeviceConnected(device: BluetoothDevice) {
        connectedDevices[device.address] = device
        listener?.onDeviceConnected(device.address)
    }

    private fun onDeviceDisconnected(device: BluetoothDevice) {
        connectedDevices.remove(device.address)
        listener?.onDeviceDisconnected(device.address)
    }

    private fun getAdvertisedGattCharacteristics(): List<BluetoothGattCharacteristic>? {
        return gattServer?.services?.flatMap { it.characteristics }
    }
    // endregion

    companion object {
        const val TAG = "AdvertisementManager"
        private val CLIENT_CHARACTERISTIC_CONFIGURATION_UUID =
            UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
        private val USER_DESCRIPTION_CHARACTERISTIC_UUID =
            UUID.fromString("00002901-0000-1000-8000-00805f9b34fb")
    }
}