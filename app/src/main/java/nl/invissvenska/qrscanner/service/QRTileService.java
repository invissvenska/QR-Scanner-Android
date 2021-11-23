package nl.invissvenska.qrscanner.service;

import android.content.Intent;
import android.service.quicksettings.TileService;

public class QRTileService extends TileService {
    @Override
    public void onClick() {
        super.onClick();
        Intent intent = new Intent("nl.invissvenska.qrscanner.MainActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityAndCollapse(intent);
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }
}
