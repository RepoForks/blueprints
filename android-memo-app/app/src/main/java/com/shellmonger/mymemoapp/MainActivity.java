package com.shellmonger.mymemoapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;

import com.shellmonger.mymemoapp.db.DatabaseAccess;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private Button btnAdd;
    private List<Document> memos;
    private DateFormat formatter = new SimpleDateFormat("EEEEE MMMMM yyyy HH:mm:ss.SSSZ");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        }

        this.listView = (ListView) findViewById(R.id.listView);
        this.btnAdd = (Button) findViewById(R.id.btnAdd);

        this.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClicked();
            }
        });

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Document memo = memos.get(position);
            TextView txtMemo = (TextView) view.findViewById(R.id.txtMemo);
            txtMemo.setText(asShortString(memo.get("content").asString()));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetAllItemsAsyncTask task = new GetAllItemsAsyncTask();
        task.execute();
    }

    public void onAddClicked() {
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }

    public void onDeleteClicked(Document memo) {
        DeleteItemAsyncTask task = new DeleteItemAsyncTask();
        task.execute(new Document[] { memo });

    }

    public void onEditClicked(Document memo) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("MEMO", memo.get("noteId").asString());
        startActivity(intent);
    }

    public String asShortString(String text) {
        String temp = text.replaceAll("\n", " ");
        if (temp.length() > 25) {
            return temp.substring(0, 25) + "...";
        } else {
            return temp;
        }
    }

    public String asDateString(long milliseconds) {
        Date date = new Date(milliseconds);
        return formatter.format(date);
    }

    public void populateListAdapter(List<Document> memos) {
        this.memos = memos;
        MemoAdapter adapter = new MemoAdapter(this, memos);
        listView.setAdapter(adapter);
    }

    public void removeDocumentsFromAdapter(List<Document> memos) {
        MemoAdapter adapter = (MemoAdapter) listView.getAdapter();
        for (Iterator<Document> i = memos.iterator(); i.hasNext(); ) {
            Document memo = i.next();
            adapter.remove(memo);
        }
        adapter.notifyDataSetChanged();
    }

    private class GetAllItemsAsyncTask extends AsyncTask<Void, Void, List<Document>> {
        @Override
        protected List<Document> doInBackground(Void... params) {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity.this);
            return databaseAccess.getAllMemos();
        }

        @Override
        protected void onPostExecute(List<Document> documents) {
            if (documents != null) {
                populateListAdapter(documents);
            }
        }
    }

    private class DeleteItemAsyncTask extends AsyncTask<Document, Void, List<Document>> {
        @Override
        protected List<Document> doInBackground(Document... documents) {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity.this);
            ArrayList<Document> deletedDocuments = new ArrayList<Document>();

            for (int i = 0 ; i < documents.length ; i++) {
                databaseAccess.delete(documents[i]);
                deletedDocuments.add(documents[i]);
            }
            return deletedDocuments;
        }

        @Override
        protected void onPostExecute(List<Document> documents) {
            removeDocumentsFromAdapter(documents);
        }
    }

    private class MemoAdapter extends ArrayAdapter<Document> {
        public MemoAdapter(Context context, List<Document> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.layout_list_item, parent, false);
            }

            ImageView btnEdit = (ImageView) convertView.findViewById(R.id.btnEdit);
            ImageView btnDelete = (ImageView) convertView.findViewById(R.id.btnDelete);
            TextView txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            TextView txtMemo = (TextView) convertView.findViewById(R.id.txtMemo);

            final Document memo = memos.get(position);
            txtDate.setText(asDateString(memo.get("creationDate").asLong()));
            txtMemo.setText(asShortString(memo.get("content").asString()));
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditClicked(memo);
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClicked(memo);
                }
            });
            return convertView;
        }
    }
}
