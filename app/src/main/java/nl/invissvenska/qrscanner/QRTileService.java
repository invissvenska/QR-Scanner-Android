package nl.invissvenska.qrscanner;

import android.content.Intent;
import android.service.quicksettings.TileService;

public class QRTileService extends TileService {
    @Override
    public void onClick() {
        super.onClick();
        Intent calendarIntent = new Intent(Intent.ACTION_EDIT);
        calendarIntent.setType("vnd.android.cursor.item/event");

        startActivityAndCollapse(calendarIntent);
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        //TODO add more functionality: https://android.jlelse.eu/develop-a-custom-tile-with-quick-settings-tile-api-74073e849457
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
