package alc.demorgan.jounal.NoteActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;


import alc.demorgan.jounal.database.AppDatabase;
import alc.demorgan.jounal.database.JournalEntry;

public class AddNoteViewModel extends ViewModel {

    private LiveData<JournalEntry> note;

   public AddNoteViewModel(AppDatabase database, int taskId) {
        note = database.journalDao().loadJournalById(taskId);
    }

    public LiveData<JournalEntry> getTask() {
        return note;
    }
}
