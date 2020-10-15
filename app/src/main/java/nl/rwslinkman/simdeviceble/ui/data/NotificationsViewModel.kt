package nl.rwslinkman.simdeviceble.ui.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Allow user to manipulate data from services currently broadcasted"
    }
    val text: LiveData<String> = _text
}