package nl.invissvenska.qrscanner.service

import android.content.Intent
import android.service.quicksettings.TileService

class QRTileService : TileService() {
    override fun onClick() {
        super.onClick()
        val intent = Intent("nl.invissvenska.qrscanner.MainActivity")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivityAndCollapse(intent)
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
    }

    override fun onTileAdded() {
        super.onTileAdded()
    }

    override fun onStartListening() {
        super.onStartListening()
    }

    override fun onStopListening() {
        super.onStopListening()
    }
}