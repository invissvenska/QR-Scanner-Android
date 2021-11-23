package nl.invissvenska.qrscanner.database.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import nl.invissvenska.qrscanner.database.repository.HistoryRepository;
import nl.invissvenska.qrscanner.database.table.History;

public class HistoryModel extends AndroidViewModel {

    private HistoryRepository repository;

    public HistoryModel(@NonNull Application application) {
        super(application);
        repository = new HistoryRepository(application);
    }

    public void insert(History history) {
        repository.insert(history);
    }

    public LiveData<List<History>> getAllHistory(){
        return repository.getAllHistory();
    }
}
