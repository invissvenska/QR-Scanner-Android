package nl.invissvenska.qrscanner.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import nl.invissvenska.qrscanner.database.table.History;

@Dao
public interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(History history);

    @Query("SELECT * FROM history ORDER BY date DESC")
    LiveData<List<History>> getAllHistory();
}
