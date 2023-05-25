package com.dgqstudio.dropdown.helpers

import android.content.Context
import android.content.res.Configuration

object DeviceUIMode {
    fun getDeviceUIMode(context: Context): DeviceUIModes {
        val nightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return if (nightMode == Configuration.UI_MODE_NIGHT_YES) DeviceUIModes.NIGHT else DeviceUIModes.DAY
    }
}