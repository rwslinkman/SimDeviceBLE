# SimDeviceBLE
Simulate a BLE device using this Android app

SimDeviceBLE allows developers to simulate Bluetooth devices with multiple GATT Services.   
It allows full configuration of the BLE advertisement data and shows the amount of connected devices.

Feel free to import the `app` module into your Android Studio project to simulate your own BLE peripherals.      
Contributing the proprietary devices, services and characteristics is appreciated but not required.     

## Usage
Make sure Bluetooth is enabled on your phone (use `Enable Bluetooth` button) and advertise a selected device.   
The selected device will advertise the services associated to it.   
All options available in the `Advertise` section will be used in the Advertisment data.   

Characteristic values can be updated using the `Service Data` screen.   
All characteristics will be mapped to a View on the screen to show their current data value.   
Some characteristics will allow to update their value and notify all connected Central devices.   
Developers do not need to write their own View classes.   
Services and characteristics are introspected and Views are created dynamically.   

![Home screen allows for configuration of Advertisment data](docs/image_home_fragment.jpg)
![Service Data screen manipulates data of all advertised characteristics](docs/image_home_fragment.jpg)

## Contributing
Please feel free to add any devices in the package `nl.rwslinkman.simdeviceble.device` package.   
All devices must implement the `Device` interface.  
The list of `services` can contain any of the services defined in the `nl.rwslinkman.simdeviceble.service` package.    
Don't forget to add the new device in the `AppModel.supportedDevices` list.   

When adding a new `Service`, please implement according to the Bluetooth SIG specification as much as possible.
Please keep in mind that the services might be used by multiple `Device` implementations.   

### Notice
BLE peripheral mode was introduced in Android 5.0 Lollipop.  
Due to hardware chipset dependency, some Android phones don't have access to this feature.   
This will be visible in the `Bluetooth` section on the Home screen.   