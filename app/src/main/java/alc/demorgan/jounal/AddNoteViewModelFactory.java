package alc.demorgan.jounal;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;


import alc.demorgan.jounal.database.AppDatabase;

public class AddNoteViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final int mJournalId;

    public AddNoteViewModelFactory(AppDatabase database, int journalId) {
        mDb = database;
        mJournalId = journalId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T>  modelClass) {
        return (T) new AddNoteViewModel(mDb, mJournalId);
    }
}
