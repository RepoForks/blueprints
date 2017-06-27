package com.shellmonger.notes;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shellmonger.notes.data.Note;
import com.shellmonger.notes.data.NoteViewHolder;
import com.shellmonger.notes.data.NotesContentContract;

/**
 * An activity representing a list of Notes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link NoteDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class NoteListActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>
{
    /**
     * The unique identifier for the loader
     */
    private static final int NOTES_LOADER = 10;

    /**
     * The listview
     */
    RecyclerView notesList;

    /**
     * The Add New Note button
     */
    FloatingActionButton addNoteButton;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    /**
     * Activity lifecycle event handler - called when the activity is first created.
     * @param savedInstanceState the saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        // Install the application crash handler.  This is only done on the first activity.
        ApplicationCrashHandler.installHandler();

        // Work out if we are in 2-pane (tablet) mode or not
        mTwoPane = (findViewById(R.id.note_detail_container) != null);

        // Initialize the Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Initialize the floating action button
        addNoteButton = (FloatingActionButton) findViewById(R.id.addNoteButton);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTwoPane) {
                    NoteDetailFragment fragment = new NoteDetailFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.note_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, NoteDetailActivity.class);
                    context.startActivity(intent);
                }
            }
        });

        // Update the ListView with the CursorAdapter
        notesList = (RecyclerView) findViewById(R.id.note_list);
        NotesAdapter adapter = new NotesAdapter(this, null);
        notesList.setAdapter(adapter);

        // Kick of the data loader for the RecyclerView
        getLoaderManager().initLoader(NOTES_LOADER, null, this);
    }

    /**
     * Event handler callback for the loader manager.  Called when creating the loader
     * manager.
     *
     * @param id The ID - should always be NOTES_LOADER in this edition
     * @param args any arguments - should always be null in this edition
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                NotesContentContract.Notes.CONTENT_URI,
                NotesContentContract.Notes.PROJECTION_ALL,
                null,
                null,
                NotesContentContract.Notes.SORT_ORDER_DEFAULT);
    }

    /**
     * Event handler callback for the loader manager.  Called when data has finished loading.
     * @param loader the loader that finished loading
     * @param data a cursor to the data that was loaded
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ((NotesAdapter) notesList.getAdapter()).swapCursor(data);
    }

    /**
     * Event handler callback for the loader manager.  Called when the loader is reset.
     * @param loader the loader that was reset
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((NotesAdapter) notesList.getAdapter()).swapCursor(null);
    }

    /**
     * The NotesAdapter is a data provider for linking the notes content provider to the UI.
     */
    public class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {
        Cursor dataCursor;
        Context context;

        public NotesAdapter(Context mContext, Cursor cursor) {
            dataCursor = cursor;
            context = mContext;
        }

        @Override
        public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.note_list_content, parent, false);
            return new NoteViewHolder(view);
        }

        /**
         * The main part of the NotesAdapter - this is called once for each element in the
         * list of data that is returned.
         * @param holder the ViewHolder (which is a NoteViewHolder) for the record
         * @param position the position in the list.
         */
        @Override
        public void onBindViewHolder(final NoteViewHolder holder, int position) {
            dataCursor.moveToPosition(position);
            Note note = Note.fromCursor(dataCursor);
            holder.setNote(note);

            // Install a click-handler for clicking on the row
            holder.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putLong(NoteDetailFragment.ARG_ITEM_ID, holder.getNote().getId());
                if (mTwoPane) {
                    NoteDetailFragment fragment = new NoteDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.note_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, NoteDetailActivity.class);
                    intent.putExtras(arguments);
                    context.startActivity(intent);
                }
                }
            });
        }

        @Override
        public int getItemCount() {
            return (dataCursor == null) ? 0 : dataCursor.getCount();
        }

        /**
         * Used to support the loader framework for loading data
         * @param cursor the new cursor
         * @return the old cursor
         */
        public Cursor swapCursor(Cursor cursor) {
            if (dataCursor == cursor) {
                return null;
            }
            Cursor oldCursor = dataCursor;
            this.dataCursor = cursor;
            if (cursor != null) {
                this.notifyDataSetChanged();
            }
            return oldCursor;
        }
    }
}
