package com.shellmonger.notes.data;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shellmonger.notes.R;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    private final View mView;        // The containing view

    // The components for this view
    private final TextView mIdView;
    private final TextView mContentView;

    // The data it is linked to
    private Note mItem;

    public NoteViewHolder(View view) {
        super(view);
        mView = view;

        // Get the view components
        mIdView = (TextView) view.findViewById(R.id.list_id);
        mContentView = (TextView) view.findViewById(R.id.list_title);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mContentView.getText() + "'";
    }

    public View getView() {
        return mView;
    }

    public Note getNote() {
        return mItem;
    }

    public void setNote(Note note) {
        mItem = note;
        mIdView.setText(note.getNoteId());
        mContentView.setText(note.getTitle());
    }
}