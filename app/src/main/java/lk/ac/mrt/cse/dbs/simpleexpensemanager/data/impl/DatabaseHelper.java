package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;           //version of current database
    public static final String DATABASE_NAME = "180109R.db";    //Database name
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**onCreate method is called when the application initiating without pre database.**/
    /**account and trans tables will be created within this method**/
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(AccountContract.SQL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(TransactionContract.SQL_CREATE_ENTRIES);
    }

    /**onUpgrade method is called when the main database schema has been changed**/
    /**account and trans tables will be droped if this is executed.*/
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(AccountContract.SQL_DELETE_ENTRIES);
        sqLiteDatabase.execSQL(TransactionContract.SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);

    }
}
