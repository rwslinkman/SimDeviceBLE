package nl.rwslinkman.simdeviceble.cucumbertest

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import nl.rwslinkman.simdeviceble.cucumbertest.databinding.ActivityCucumberTestBinding

class CucumberTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCucumberTestBinding
    private val permissionChecker = AppPermissionChecker(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCucumberTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAppSettings.setOnClickListener {
            openAppSettingsScreen()
        }
    }

    override fun onResume() {
        super.onResume()

        // Table content
        val hasBlePermission: Boolean = permissionChecker.hasPermissionsBluetoothOld() or permissionChecker.hasPermissionsBluetoothNew()
        binding.imgPermissionBluetoothStatus.displayPermissionStatus(hasBlePermission)
        val hasLocationPermission = permissionChecker.hasPermissionsLocation()
        binding.imgPermissionLocationStatus.displayPermissionStatus(hasLocationPermission)
        val hasOtherPermissions = permissionChecker.hasPermissionsOther()
        binding.imgPermissionOtherStatus.displayPermissionStatus(hasOtherPermissions)
        // Top content
        val hasAllPermissions = hasBlePermission and hasLocationPermission and hasOtherPermissions
        binding.imgPermissionCheck.displayPermissionStatus(hasAllPermissions)
    }

    private fun openAppSettingsScreen() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}