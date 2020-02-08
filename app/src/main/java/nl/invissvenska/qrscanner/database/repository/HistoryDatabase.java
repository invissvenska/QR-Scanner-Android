package nl.invissvenska.qrscanner.database.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import nl.invissvenska.qrscanner.database.dao.HistoryDao;
import nl.invissvenska.qrscanner.database.table.History;

@Database(entities = {History.class}, version = 1, exportSchema = false)
public abstract class HistoryDatabase extends RoomDatabase {

    public abstract HistoryDao dao();

    private static volatile HistoryDatabase INSTANCE;

    static HistoryDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (HistoryDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), HistoryDatabase.class, "history_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
