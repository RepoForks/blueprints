package com.shellmonger.mymemoapp.db;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private Context context;
    private static volatile DatabaseAccess instance;

    /**
     * Create a new DatabaseAccess instance.  This is part of the Singleton pattern
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.context = context;
    }

    public static synchronized DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database, if necessary.
     */
    public void open() {
    }

    /**
     * Close the database, if necessary.
     */
    public void close() {
    }

    /**
     * Save a new record to the database.
     * @param memo the record to save
     */
    public void save(Memo memo) {
    }

    /**
     * Update an existing record in the database
     * @param memo the record to save
     */
    public void update(Memo memo) {
    }

    /**
     * Delete an existing record in the database
     * @param memo the record to delete
     */
    public void delete(Memo memo) {
    }

    /**
     * Get a list of all the memos in the database
     * @return a list of memos
     */
    public List<Memo> getAllMemos() {
        return new ArrayList<Memo>();
    }
}
