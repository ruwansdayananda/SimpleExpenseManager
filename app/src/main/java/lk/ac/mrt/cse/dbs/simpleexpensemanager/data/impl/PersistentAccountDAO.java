/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * This is an In-Memory implementation of the AccountDAO interface. This is not a persistent storage. A HashMap is
 * used to store the account details temporarily in the memory.
 */
public class PersistentAccountDAO implements AccountDAO {
    private DatabaseHelper databaseHelper;

    public PersistentAccountDAO(Context context) {
        this.databaseHelper = new DatabaseHelper(context);

    }

    @Override
    public List<String> getAccountNumbersList() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] projection = {
          AccountContract.AccountEntry.COLUMN_ACCOUNT_NO
        };
        Cursor cursor = db.query(AccountContract.AccountEntry.TABLE_NAME,projection,null,null,null,null,null);
        List account_nos = new ArrayList<>();
        while(cursor.moveToNext()) {
            String itemId = cursor.getString(
                    cursor.getColumnIndexOrThrow(AccountContract.AccountEntry.COLUMN_ACCOUNT_NO));
            account_nos.add(itemId);
        }
        cursor.close();
        return account_nos;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] projection = {
                AccountContract.AccountEntry.COLUMN_ACCOUNT_NO,
                AccountContract.AccountEntry.COLUMN_BANK_NAME,
                AccountContract.AccountEntry.COLUMN_ACCOUNT_HOLDER_NAME,
                AccountContract.AccountEntry.COLUMN_BALANCE,
        };
        Cursor cursor = db.query(AccountContract.AccountEntry.TABLE_NAME,projection,null,null,null,null,null);
        List accounts = new ArrayList<>();
        while(cursor.moveToNext()) {
            String accountNo = cursor.getString(cursor.getColumnIndexOrThrow(AccountContract.AccountEntry.COLUMN_ACCOUNT_NO));
            String bankName = cursor.getString(cursor.getColumnIndexOrThrow(AccountContract.AccountEntry.COLUMN_BANK_NAME));
            String accountHolderName = cursor.getString(cursor.getColumnIndexOrThrow(AccountContract.AccountEntry.COLUMN_ACCOUNT_HOLDER_NAME));
            double balance = cursor.getDouble(cursor.getColumnIndexOrThrow(AccountContract.AccountEntry.COLUMN_BALANCE));
            accounts.add(new Account(accountNo,bankName,accountHolderName,balance));
        }
        cursor.close();
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        if (this.getAccountNumbersList().contains(accountNo)) {
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            String[] projection = {
                    AccountContract.AccountEntry.COLUMN_ACCOUNT_NO,
                    AccountContract.AccountEntry.COLUMN_BANK_NAME,
                    AccountContract.AccountEntry.COLUMN_ACCOUNT_HOLDER_NAME,
                    AccountContract.AccountEntry.COLUMN_BALANCE,
            };
            String selection = AccountContract.AccountEntry.COLUMN_ACCOUNT_NO + " = ?";
            String[] selectionArgs = { accountNo };
            System.out.println(accountNo);
            Cursor cursor = db.query(AccountContract.AccountEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
            List accounts = new ArrayList<>();
            while(cursor.moveToNext()) {
                String accountNumber = cursor.getString(cursor.getColumnIndexOrThrow(AccountContract.AccountEntry.COLUMN_ACCOUNT_NO));
                String bankName = cursor.getString(cursor.getColumnIndexOrThrow(AccountContract.AccountEntry.COLUMN_BANK_NAME));
                String accountHolderName = cursor.getString(cursor.getColumnIndexOrThrow(AccountContract.AccountEntry.COLUMN_ACCOUNT_HOLDER_NAME));
                double balance = cursor.getDouble(cursor.getColumnIndexOrThrow(AccountContract.AccountEntry.COLUMN_BALANCE));
                accounts.add(new Account(accountNumber,bankName,accountHolderName,balance));
            }
            cursor.close();
            return (Account) accounts.get(0);
//            return accounts.get(accountNo);
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AccountContract.AccountEntry.COLUMN_ACCOUNT_NO, account.getAccountNo());
        values.put(AccountContract.AccountEntry.COLUMN_BANK_NAME, account.getBankName());
        values.put(AccountContract.AccountEntry.COLUMN_ACCOUNT_HOLDER_NAME, account.getAccountHolderName());
        values.put(AccountContract.AccountEntry.COLUMN_BALANCE, account.getBalance());
        long newRowId = db.insert(AccountContract.AccountEntry.TABLE_NAME, null, values);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        if (!this.getAccountNumbersList().contains(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String selection = AccountContract.AccountEntry.COLUMN_ACCOUNT_NO + " = ?";
        String[] selectionArgs = { accountNo };
        int deletedRows = db.delete(AccountContract.AccountEntry.TABLE_NAME, selection, selectionArgs);
        //accounts.remove(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        if (!this.getAccountNumbersList().contains(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        Account account = this.getAccount(accountNo);
        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        double updated_balance = account.getBalance();
        ContentValues values = new ContentValues();
        values.put(AccountContract.AccountEntry.COLUMN_BALANCE, updated_balance);


        String selection = AccountContract.AccountEntry.COLUMN_ACCOUNT_NO + "= ?";
        String[] selectionArgs = {account.getAccountNo()};

        int count = db.update(
                AccountContract.AccountEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
}
