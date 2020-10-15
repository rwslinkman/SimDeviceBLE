package nl.rwslinkman.simdeviceble.ui.devices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Show a list of all supported devices with details about services and characteristics"
    }
    val text: LiveData<String> = _text
}