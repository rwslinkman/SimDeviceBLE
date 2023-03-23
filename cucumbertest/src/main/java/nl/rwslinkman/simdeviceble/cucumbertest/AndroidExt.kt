package nl.rwslinkman.simdeviceble.cucumbertest

import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat

fun ImageView.displayPermissionStatus(hasPermission: Boolean) {
    val statusRes: Int = if (hasPermission) R.drawable.ic_success_black_24dp else R.drawable.ic_warning_black_24dp
    val statusColor: Int = if (hasPermission) R.color.success else R.color.warning

    setColorFilter(ContextCompat.getColor(context, statusColor), PorterDuff.Mode.SRC_IN)
    setImageResource(statusRes)
    visibility = View.VISIBLE
}