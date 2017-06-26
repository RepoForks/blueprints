package com.shellmonger.notes.data;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.UUID;

/**
 * The Note model
 */
public class Note {
    private String noteId;
    private String title;
    private String content;
    private DateTime created;
    private DateTime updated;

    /**
     * Create a new blank note
     */
    public Note() {
        setNoteId(UUID.randomUUID().toString());
        setTitle("");
        setContent("");
        setCreated(DateTime.now(DateTimeZone.UTC));
    }

    /**
     * Create a new note with a specific title and content
     * @param title the title
     * @param content the content
     */
    public Note(String title, String content) {
        setNoteId(UUID.randomUUID().toString());
        setTitle(title);
        setContent(content);
        setCreated(DateTime.now(DateTimeZone.UTC));
    }

    /**
     * Returns the noteId
     * @return the note ID
     */
    public String getNoteId() { return noteId; }

    /**
     * Sets the noteId
     * @param noteId the new note ID
     */
    void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    /**
     * Returns the title
     * @return the title
     */
    public String getTitle() { return title; }

    /**
     * Sets the title
     * @param title the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the note content
     * @return the note content
     */
    public String getContent() { return content; }

    /**
     * Sets the note content
     * @param content the note content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Returns the created date
     * @return the created date
     */
    public DateTime getCreated() { return created; }

    /**
     * Sets the created date
     * @param created the created date
     */
    void setCreated(DateTime created) {
        this.created = created;
    }

    /**
     * Returns the last updated date
     * @return the last updated date
     */
    public DateTime getUpdated() { return updated; }

    /**
     * Sets the last updated date
     * @param updatedDate the new last updated date
     */
    public void setUpdated(DateTime updatedDate) {
        this.updated = updatedDate;
    }

    /**
     * Updates the note
     * @param title the new title
     * @param content the new content
     */
    public void updateNote(String title, String content) {
        setTitle(title);
        setContent(content);
        setUpdated(DateTime.now(DateTimeZone.UTC));
    }

    /**
     * The string version of the class
     * @return the class unique descriptor
     */
    @Override
    public String toString() {
        return String.format("[note#%s] %s", noteId, title);
    }
}
