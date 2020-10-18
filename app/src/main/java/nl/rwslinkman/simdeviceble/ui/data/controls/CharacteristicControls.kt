package nl.rwslinkman.simdeviceble.ui.data.controls

import android.view.View
import nl.rwslinkman.simdeviceble.device.model.Characteristic
import nl.rwslinkman.simdeviceble.ui.data.ServiceDataAdapter

interface CharacteristicControls {
    val controlsLayoutId: Int

    fun setup(controlsView: View)

    fun bind(charItem: Characteristic, listener: ServiceDataAdapter.CharacteristicManipulationListener)
}
