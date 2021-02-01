package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private DatabaseHelper databaseHelper;

    public PersistentTransactionDAO(Context context) {
        this.databaseHelper = new DatabaseHelper(context);

    }
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(date);
        ContentValues values = new ContentValues();
        values.put(TransactionContract.TransactionEntry.COLUMN_DATE, strDate);
        values.put(TransactionContract.TransactionEntry.COLUMN_ACCOUNT_NO, accountNo);
        values.put(TransactionContract.TransactionEntry.COLUMN_EXPENSE_TYPE, String.valueOf(expenseType));
        values.put(TransactionContract.TransactionEntry.COLUMN_AMOUNT, Double.valueOf(amount));
        long newRowId = db.insert(TransactionContract.TransactionEntry.TABLE_NAME, null, values);

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] projection = {
                TransactionContract.TransactionEntry.COLUMN_DATE,
                TransactionContract.TransactionEntry.COLUMN_ACCOUNT_NO,
                TransactionContract.TransactionEntry.COLUMN_EXPENSE_TYPE,
                TransactionContract.TransactionEntry.COLUMN_AMOUNT,
        };
        Cursor cursor = db.query(TransactionContract.TransactionEntry.TABLE_NAME,projection,null,null,null,null,null);
        List transactions = new ArrayList<>();
        while(cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndexOrThrow(TransactionContract.TransactionEntry.COLUMN_DATE));
            String accountNo = cursor.getString(cursor.getColumnIndexOrThrow(TransactionContract.TransactionEntry.COLUMN_ACCOUNT_NO));
            String expenseType = cursor.getString(cursor.getColumnIndexOrThrow(TransactionContract.TransactionEntry.COLUMN_EXPENSE_TYPE));
            double amount = cursor.getLong(cursor.getColumnIndexOrThrow(TransactionContract.TransactionEntry.COLUMN_AMOUNT));
            try {
                transactions.add(new Transaction(new SimpleDateFormat("dd-MM-yyyy").parse(date),accountNo,ExpenseType.valueOf(expenseType),amount));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] projection = {
                TransactionContract.TransactionEntry.COLUMN_DATE,
                TransactionContract.TransactionEntry.COLUMN_ACCOUNT_NO,
                TransactionContract.TransactionEntry.COLUMN_EXPENSE_TYPE,
                TransactionContract.TransactionEntry.COLUMN_AMOUNT,
        };
        Cursor cursor = db.query(TransactionContract.TransactionEntry.TABLE_NAME,projection,null,null,null,null,null);
        List transactions = new ArrayList<>();
        int i=0;
        while(cursor.moveToNext() && (i<limit)) {
            String date = cursor.getString(cursor.getColumnIndexOrThrow(TransactionContract.TransactionEntry.COLUMN_DATE));
            String accountNo = cursor.getString(cursor.getColumnIndexOrThrow(TransactionContract.TransactionEntry.COLUMN_ACCOUNT_NO));
            String expenseType = cursor.getString(cursor.getColumnIndexOrThrow(TransactionContract.TransactionEntry.COLUMN_EXPENSE_TYPE));
            double amount = cursor.getLong(cursor.getColumnIndexOrThrow(TransactionContract.TransactionEntry.COLUMN_AMOUNT));
            try {
                transactions.add(new Transaction(new SimpleDateFormat("dd-MM-yyyy").parse(date),accountNo,ExpenseType.valueOf(expenseType),amount));
                i++;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return transactions;

    }
}
