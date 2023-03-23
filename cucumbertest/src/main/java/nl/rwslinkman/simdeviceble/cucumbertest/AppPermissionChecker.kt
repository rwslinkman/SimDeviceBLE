package nl.rwslinkman.simdeviceble.cucumbertest

import android.content.pm.PackageManager
import android.os.Build

class AppPermissionChecker(private val activity: CucumberTestActivity) {

    fun hasPermissionsBluetoothOld(): Boolean {
        val oldBluetoothPermissions = listOf(
            android.Manifest.permission.BLUETOOTH,
            android.Manifest.permission.BLUETOOTH_ADMIN,
        )
        return hasPermissions(oldBluetoothPermissions)
    }

    fun hasPermissionsBluetoothNew() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val newBluetoothPermissions = listOf(
            android.Manifest.permission.BLUETOOTH_SCAN,
            android.Manifest.permission.BLUETOOTH_CONNECT,
        )
        hasPermissions(newBluetoothPermissions)
    } else {
        false
    }

    fun hasPermissionsLocation(): Boolean {
        val locationPermissions = listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
        return hasPermissions(locationPermissions)
    }

    fun hasPermissionsOther(): Boolean {
        val requiredPermissions = listOf(
            android.Manifest.permission.INTERNET,
        )
        return hasPermissions(requiredPermissions)
    }

    private fun hasPermissions(requiredPermissions: List<String>): Boolean =
        requiredPermissions.map {
            activity.checkSelfPermission(it)
        }.all {
            it == PackageManager.PERMISSION_GRANTED
        }
}