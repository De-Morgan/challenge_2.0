package alc.demorgan.jounal.NoteActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import alc.demorgan.jounal.MainActivity;
import alc.demorgan.jounal.R;
import alc.demorgan.jounal.database.AppDatabase;
import alc.demorgan.jounal.database.JournalEntry;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;


public class NotepadActivity extends AppCompatActivity implements NoteAdapter.ItemClickListener {

    FirebaseUser user;

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private NoteAdapter mAdapter;
    private  TextView welcome;
    private AppDatabase mDb;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);
        user = FirebaseAuth.getInstance().getCurrentUser();


        mRecyclerView = findViewById(R.id.recyclerViewJournals);

      mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new NoteAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {

                Snackbar.make(viewHolder.itemView, "Delete Journal Entry ?", Snackbar.LENGTH_LONG)
                        .setAction("Delect", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Toast.makeText(getBaseContext(),"Journal Entry Deleted",Toast.LENGTH_SHORT).show();


                                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        int position = viewHolder.getAdapterPosition();
                                        List<JournalEntry> journals = mAdapter.getJournals();
                                        mDb.journalDao().deleteJournal(journals.get(position));



                                    }
                                });

                            }
                        })
                        .show();

            }
        }).attachToRecyclerView(mRecyclerView);


        FloatingActionButton fabButton = findViewById(R.id.fab);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addJournalIntent = new Intent(NotepadActivity.this, AddNoteActivity.class);
                startActivity(addJournalIntent);
            }
        });

        mDb = AppDatabase.getInstance(getApplicationContext());
        setupViewModel();





    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getJournals().observe(this, new Observer<List<JournalEntry>>() {
            @Override
            public void onChanged(@Nullable List<JournalEntry> journalEntries) {
                Log.d(TAG, "Updating list of journal from LiveData in ViewModel");
                mAdapter.setJournals(journalEntries);
            }
        });
    }

    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(NotepadActivity.this, AddNoteActivity.class);
        intent.putExtra(AddNoteActivity.EXTRA_JOURNAL_ID, itemId);
        startActivity(intent);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            if(item.getItemId() == R.id.sign_out){

                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            }


            return false;
    }
}