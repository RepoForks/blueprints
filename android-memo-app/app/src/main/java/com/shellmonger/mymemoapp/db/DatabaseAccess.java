package com.shellmonger.mymemoapp.db;

import android.content.Context;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Expression;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Search;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive;

import java.util.List;

public class DatabaseAccess {
    private Context context;
    private CognitoCachingCredentialsProvider credentialsProvider;
    private AmazonDynamoDBClient dbClient;
    private Table dbTable;

    /**
     * The Pool ID for the Cognito User Pool
     */
    private final String COGNITO_POOL_ID = "us-east-1:f9d582af-51f9-4db3-8e36-7bdf25f4ee07";

    /**
     * The region that the Cognito User Pool is in
     */
    private final Regions COGNITO_REGION = Regions.US_EAST_1;

    /**
     * The table name that will be used to store data
     */
    private final String DYNAMODB_TABLE = "androidmemoapp-mobilehub-1932532734-Notes";

    /**
     * Instance Storage for the Singleton package
     */
    private static volatile DatabaseAccess instance;

    /**
     * Create a new DatabaseAccess instance.  This is part of the Singleton pattern
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.context = context;

        // Create a new credentials provider
        credentialsProvider = new CognitoCachingCredentialsProvider(
                context, COGNITO_POOL_ID, COGNITO_REGION);

        // Create a connection to Dynamo Db
        dbClient = new AmazonDynamoDBClient(credentialsProvider);

        // Create a table reference
        dbTable = Table.loadTable(dbClient, DYNAMODB_TABLE);
    }

    public static synchronized DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Save a new record to the database.
     * @param memo the record to save
     */
    public void save(Document memo) {
        // The userId must exist
        if (!memo.containsKey("userId")) {
            memo.put("userId", "default");
        }
        dbTable.putItem(memo);
    }

    /**
     * Update an existing record in the database
     * @param memo the record to save
     */
    public void update(Document memo) {
        dbTable.updateItem(memo, null);
    }

    /**
     * Delete an existing record in the database
     * @param memo the record to delete
     */
    public void delete(Document memo) {
        dbTable.deleteItem(memo.get("userId").asPrimitive(), memo.get("noteId").asPrimitive());
    }

    /**
     * Get a list of all the memos in the database
     * @return a list of memos
     */
    public List<Document> getAllMemos() {
        return dbTable.query(new Primitive("default"), new Expression()).getAllResults();
    }

    /**
     * Get a memo by the specified ID
     * @param id the ID of the memo
     * @return the related document
     */
    public Document getById(String id) {
        Expression expression = new Expression();
        expression.setExpressionStatement("noteId = :id");
        expression.withExpressionAttibuteValues(":id", new Primitive(id));
        Search results = dbTable.query(new Primitive("default"), expression);
        return results.getAllResults().get(0);
    }
}
