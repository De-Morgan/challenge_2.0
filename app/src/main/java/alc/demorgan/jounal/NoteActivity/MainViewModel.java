package alc.demorgan.jounal.NoteActivity;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;


import java.util.List;

import alc.demorgan.jounal.database.AppDatabase;
import alc.demorgan.jounal.database.JournalEntry;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<JournalEntry>> tasks;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        tasks = database.journalDao().loadAllJournals();
    }

    public LiveData<List<JournalEntry>> getJournals() {
        return tasks;
    }
}
