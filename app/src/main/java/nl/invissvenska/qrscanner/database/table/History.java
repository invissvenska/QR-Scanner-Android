package nl.invissvenska.qrscanner.database.table;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.joda.time.LocalDate;


@Entity
public class History {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    @ColumnInfo(name = "date", index = true)
    @TypeConverters({LocalDateConverter.class})
    LocalDate date;
    @NonNull
    @ColumnInfo(name = "value")
    String value;

    public History(LocalDate date, String value) {
        this.date = date;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public LocalDate getDate() {
        return date;
    }

    @NonNull
    public String getValue() {
        return value;
    }

}
