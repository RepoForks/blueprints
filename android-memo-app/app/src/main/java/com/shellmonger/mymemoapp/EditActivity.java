package com.shellmonger.mymemoapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;

import com.shellmonger.mymemoapp.db.DatabaseAccess;

import java.util.UUID;

public class EditActivity extends AppCompatActivity {
    private EditText etText;
    private Button btnSave;
    private Button btnCancel;
    private Document memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        this.etText = (EditText) findViewById(R.id.etText);
        this.btnSave = (Button) findViewById(R.id.btnSave);
        this.btnCancel = (Button) findViewById(R.id.btnCancel);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String memoId = (String) bundle.get("MEMO");
            GetItemAsyncTask task = new GetItemAsyncTask();
            task.execute(new String[] { memoId });
        }

        this.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveClicked();
            }
        });

        this.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClicked();
            }
        });
    }

    public void onSaveClicked() {
        if(memo == null) {
            // Add new memo
            Document temp = new Document();
            temp.put("noteId", UUID.randomUUID().toString());
            temp.put("content", etText.getText().toString());
            temp.put("creationDate", System.currentTimeMillis());
            SaveItemAsyncTask task = new SaveItemAsyncTask();
            task.execute(new Document[] { temp });
        } else {
            // Update the memo
            memo.put("content", etText.getText().toString());
            UpdateItemAsyncTask task = new UpdateItemAsyncTask();
            task.execute(new Document[] { memo });
        }
        this.finish();
    }

    public void onCancelClicked() {
        this.finish();
    }

    private class GetItemAsyncTask extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... documents) {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(EditActivity.this);
            return databaseAccess.getById(documents[0]);
        }

        @Override
        protected void onPostExecute(Document memo) {
            if (memo != null) {
                etText.setText(memo.get("text").asString());
            }
        }
    }

    private class SaveItemAsyncTask extends AsyncTask<Document, Void, Void> {
        @Override
        protected Void doInBackground(Document... documents) {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(EditActivity.this);
            databaseAccess.save(documents[0]);
            return null;
        }
    }

    private class UpdateItemAsyncTask extends AsyncTask<Document, Void, Void> {
        @Override
        protected Void doInBackground(Document... documents) {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(EditActivity.this);
            databaseAccess.update(documents[0]);
            return null;
        }
    }

}
