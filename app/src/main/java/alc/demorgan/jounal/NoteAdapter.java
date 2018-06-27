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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import alc.demorgan.jounal.database.JournalEntry;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.JournalViewHolder> {

    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyy";

    final private ItemClickListener mItemClickListener;
    private List<JournalEntry> mJournalEntries;
    private Context mContext;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    public NoteAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    @Override
    public JournalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the note_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.note_layout, parent, false);

        return new JournalViewHolder(view);
    }
    @Override
    public void onBindViewHolder(JournalViewHolder holder, int position) {
        // Determine the values of the wanted data
        JournalEntry journalEntry = mJournalEntries.get(position);
        String description = journalEntry.getDescription();
        String content = journalEntry.getContent();
        String updatedAt = dateFormat.format(journalEntry.getUpdatedAt());

        //Set values
        holder.noteDescriptionView.setText(description);
        holder.updatedAtView.setText(updatedAt);
        holder.noteContent.setText(content);

       // holder.optionMenu.setOnClickListener();
        // Programmatically set the text and color for the priority TextView

        // Get the appropriate background color based on the priority
    }

    /*
    Helper method for selecting the correct priority circle color.
    P1 = red, P2 = orange, P3 = yellow
    */

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mJournalEntries == null) {
            return 0;
        }
        return mJournalEntries.size();
    }

    public List<JournalEntry> getJournals() {
        return mJournalEntries;
    }

    public void setJournals(List<JournalEntry> journalEntries) {
        mJournalEntries = journalEntries;
        notifyDataSetChanged();
    }



    public interface ItemClickListener  {
        void onItemClickListener(int itemId);
    }

    class JournalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView noteDescriptionView;
        TextView noteContent;
        TextView updatedAtView;



        public JournalViewHolder(View itemView) {
            super(itemView);

            noteDescriptionView = itemView.findViewById(R.id.journalDescription);
            updatedAtView = itemView.findViewById(R.id.noteUpdatedAt);
            noteContent = itemView.findViewById(R.id.layout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = mJournalEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);

        }



    }

}