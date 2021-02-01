package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.provider.BaseColumns;

/**TransactionContract contains the Table schema of the 'trans' table and Main SQL methods**/
public final class TransactionContract {
    /**To avoid instantiating through constructor**/
    private TransactionContract(){}
    /**AccountEntry contains the Table schema of the 'trans' table**/
    public static class TransactionEntry implements BaseColumns{
        public static final String TABLE_NAME = "trans";      //can not be named as transaction
        public static final String COLUMN_TRANSACTION_ID="transaction_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_ACCOUNT_NO = "accountNo";
        public static final String COLUMN_EXPENSE_TYPE = "expenseType";
        public static final String COLUMN_AMOUNT= "amount";


    }
    /**Query for creating 'trans' table **/
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TransactionContract.TransactionEntry.TABLE_NAME + " ( " +
                    TransactionContract.TransactionEntry.COLUMN_TRANSACTION_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TransactionContract.TransactionEntry.COLUMN_DATE+ " TEXT, " +
                    TransactionContract.TransactionEntry.COLUMN_ACCOUNT_NO + " TEXT, " +
                    TransactionContract.TransactionEntry.COLUMN_EXPENSE_TYPE+ " TEXT, "+
                    TransactionContract.TransactionEntry.COLUMN_AMOUNT + " DOUBLE ) ";

    /**Query for droping 'trans' table **/
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TransactionContract.TransactionEntry.TABLE_NAME;
}
