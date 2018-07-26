package choo.edeline.foodrng;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Database.db";
    //item table
    private static final String TABLE_ITEM = "item_table";
    private static final String COLUMN_ITEM_ID = "ITEM_ID";
    private static final String COLUMN_ITEM = "item";
    private static final String COLUMN_OWNER = "owner";
    //user table
    private static final String TABLE_USER = "user_table";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createItemTable = "CREATE TABLE " + TABLE_ITEM +
                " (ITEM_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ITEM + " TEXT," + COLUMN_OWNER +" TEXT)";
        db.execSQL(createItemTable);

        String createUserTable = "CREATE TABLE " + TABLE_USER +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT," + COLUMN_PASSWORD +" TEXT)";
        db.execSQL(createUserTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_ITEM);
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_USER);

        onCreate(db);
    }

    /**
     * Adds the item labelled with the owner of the item into the database
     */
    public boolean addData(String item, String owner) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ITEM, item);
        contentValues.put(COLUMN_OWNER, owner);

        long result = db.insert(TABLE_ITEM, null, contentValues);

        //if date is inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns True if the specified owner already has the item in their list
     */
    public boolean checkIfDataExist(String item, String owner) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COLUMN_ITEM + " FROM " + TABLE_ITEM +
                " WHERE " + COLUMN_ITEM + " = '" + item + "' AND " +
                COLUMN_OWNER + " = '" + owner + "'";
        Cursor data = db.rawQuery(query, null);
        if (data.getCount() > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Returns all the data of the specific owner from the databse
     */
    public Cursor getData(String owner){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ITEM +
                " WHERE " + COLUMN_OWNER + " = '" + owner + "'";
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    /**
     * Returns only the ID that matches the item and owner passed in
     */
    public Cursor getItemID(String item, String owner){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COLUMN_ITEM_ID + " FROM " + TABLE_ITEM +
                " WHERE " + COLUMN_ITEM + " = '" + item + "' AND " +
                COLUMN_OWNER + " = '" + owner + "'";
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    /**
     * Returns the all the items belonging to the owner specified
     */
    public Cursor getItemData(String owner){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COLUMN_ITEM + " FROM " + TABLE_ITEM +
                " WHERE " + COLUMN_OWNER + " = '" + owner + "'";
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    /**
     * Updates the item field
     */
    public void updateItem(String newItem, int id, String oldItem, String owner){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_ITEM + " SET " + COLUMN_ITEM +
                " = '" + newItem + "' WHERE " + COLUMN_ITEM_ID + " = '" + id + "'" +
                " AND " + COLUMN_ITEM + " = '" + oldItem + "' AND " + COLUMN_OWNER +
                " = '" + owner + "'";

        db.execSQL(query);
    }

    /**
     * Delete from an item from the database
     */
    public void deleteItem(int id, String item, String owner){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_ITEM + " WHERE "
                + COLUMN_ITEM_ID + " = '" + id + "'" +
                " AND " + COLUMN_ITEM + " = '" + item + "'" +
                " AND " + COLUMN_OWNER + " = '" + owner + "'";

        db.execSQL(query);
    }

    /**
     * Register a new user into the database
     */
    public void addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_PASSWORD, password);

        db.insert(TABLE_USER, null, contentValues);
    }

    /**
     * Returns True if the username entered already exists in the database
     */
    public boolean checkIfUserExist(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COLUMN_USERNAME + " FROM " + TABLE_USER +
                " WHERE " + COLUMN_USERNAME + " = '" + username + "'";
        Cursor data = db.rawQuery(query, null);
        if (data.getCount() > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Returns True if the password entered matches the username
     */
    public boolean validateUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_USER +
                " WHERE " + COLUMN_USERNAME + " = '" + username + "' AND "
                + COLUMN_PASSWORD + " = '" + password + "'";
        Cursor data = db.rawQuery(query, null);
        if (data.getCount() > 0) {
            return true;
        }
        else {
            return false;
        }
    }

}