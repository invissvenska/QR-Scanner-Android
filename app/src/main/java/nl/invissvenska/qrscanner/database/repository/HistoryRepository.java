package nl.invissvenska.qrscanner.database.repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import nl.invissvenska.qrscanner.database.dao.HistoryDao;
import nl.invissvenska.qrscanner.database.table.History;

public class HistoryRepository {

    private HistoryDao dao;

    public HistoryRepository(Application application) {
        HistoryDatabase db = HistoryDatabase.getDatabase(application);
        dao = db.dao();
    }

    public HistoryRepository(Context context) {
        HistoryDatabase db = HistoryDatabase.getDatabase(context);
        dao = db.dao();
    }

    public void insert(History history) {
        new insertAsyncTask(dao).execute(history);
    }

    private static class insertAsyncTask extends AsyncTask<History, Void, Void> {
        private HistoryDao asyncTaskDao;

        insertAsyncTask(HistoryDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final History... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public LiveData<List<History>> getAllHistory() {
        return dao.getAllHistory();
    }
}
