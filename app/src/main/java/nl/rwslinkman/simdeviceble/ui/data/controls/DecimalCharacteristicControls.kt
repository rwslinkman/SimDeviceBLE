package nl.rwslinkman.simdeviceble.ui.data.controls

import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import nl.rwslinkman.simdeviceble.R
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.ui.data.ServiceDataAdapter

class DecimalCharacteristicControls: CharacteristicControls {

    private lateinit var valueControl: EditText
    private lateinit var valueUpdateButton: Button
    private lateinit var notifyButton: Button

    override val controlsLayoutId: Int
        get() = R.layout.characteristic_update_control_decimal

    override fun setup(controlsView: View) {
        valueControl = controlsView.findViewById(R.id.characterstic_control_edittext)
        valueUpdateButton  = controlsView.findViewById(R.id.characteristic_control_set_btn)
        notifyButton = controlsView.findViewById(R.id.characteristic_control_notify_btn)
    }

    override fun bind(charItem: Characteristic, listener: ServiceDataAdapter.CharacteristicManipulationListener) {
        valueUpdateButton.setOnClickListener {
            val fieldValue: Editable = valueControl.text
            listener.setCharacteristicValue(charItem, fieldValue)
        }

        notifyButton.isEnabled = charItem.isNotify
        notifyButton.setOnClickListener {
            listener.notifyCharacteristic(charItem)
        }
    }
}