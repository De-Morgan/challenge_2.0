/*
* Copyright (C) 2016 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package alc.demorgan.jounal;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import alc.demorgan.jounal.database.AppDatabase;
import alc.demorgan.jounal.database.JournalEntry;


public class AddNoteActivity extends AppCompatActivity {

    public static final String EXTRA_JOURNAL_ID = "extraJournalId";
    public static final String INSTANCE_JOURNAL_ID = "instanceJournalId";

    private static final int DEFAULT_JOURNAL_ID = -1;
    private static final String TAG = AddNoteActivity.class.getSimpleName();
    EditText mEditText;
    EditText mEditTextContext;

    private int mJournalId = DEFAULT_JOURNAL_ID;

    private AppDatabase mDb;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        initViews();

        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_JOURNAL_ID)) {
            mJournalId = savedInstanceState.getInt(INSTANCE_JOURNAL_ID, DEFAULT_JOURNAL_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_JOURNAL_ID)) {
            if (mJournalId == DEFAULT_JOURNAL_ID) {
                mJournalId = intent.getIntExtra(EXTRA_JOURNAL_ID, DEFAULT_JOURNAL_ID);

               AddNoteViewModelFactory factory = new AddNoteViewModelFactory(mDb, mJournalId);
              final AddNoteViewModel viewModel
                        = ViewModelProviders.of(this, factory).get(AddNoteViewModel.class);

                viewModel.getTask().observe(this, new Observer<JournalEntry>() {
                    @Override
                    public void onChanged(@Nullable JournalEntry journalEntry) {
                        viewModel.getTask().removeObserver(this);
                        populateUI(journalEntry);
                    }
                });
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_JOURNAL_ID, mJournalId);
        super.onSaveInstanceState(outState);
    }


    private void initViews() {
        mEditText = findViewById(R.id.editTextDescription);
        mEditTextContext = findViewById(R.id.content);

    }


    private void populateUI(JournalEntry task) {
        if (task == null) {
            return;
        }

        mEditText.setText(task.getDescription());
        mEditTextContext.setText(task.getContent());
    }

    public void onSaveButtonClicked() {
        String description = mEditText.getText().toString();
        String content = mEditTextContext.getText().toString();

        Date date = new Date();

        final JournalEntry journal = new JournalEntry(description, content,  date);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mJournalId == DEFAULT_JOURNAL_ID) {
                    mDb.journalDao().insertJournal(journal);
                } else {
                    journal.setId(mJournalId);
                    mDb.journalDao().updateJournal(journal);
                }
                finish();
            }
        });
    }



public void shareText(String textToShare) {

        String mimeType = "text/plain";

        String title = "Share Note ";

        ShareCompat.IntentBuilder
                /* The from method specifies the Context from which this share is coming from */
                .from(this)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(textToShare)
                .startChooser();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_save :
                onSaveButtonClicked();
                Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();

                return true;
            case  R.id.action_share :
                String noteToShare = mEditTextContext.getText().toString();
                shareText(noteToShare);
                return true;
            case R.id.action_clear :

                new AlertDialog.Builder(this)
                        .setMessage("Do you want to clear note ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mEditTextContext.setText("");

                            }
                        })
                        .setNegativeButton("No",null).show();



                return true;


        }

        return false;
    }

    @Override
    public void onBackPressed() {
        onSaveButtonClicked();
        Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }


}
