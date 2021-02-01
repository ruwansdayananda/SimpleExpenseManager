package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.provider.BaseColumns;

/**AccountContract contains the Table schema of the 'account' table and Main SQL methods**/

public final class AccountContract {

    /**To avoid instantiating through constructor**/
    private AccountContract(){}

    /**AccountEntry contains the Table schema of the 'account' table**/
    public static class AccountEntry implements BaseColumns{
        public static final String TABLE_NAME = "account";
        public static final String COLUMN_ACCOUNT_NO = "accountNo";
        public static final String COLUMN_BANK_NAME = "bankName";
        public static final String COLUMN_ACCOUNT_HOLDER_NAME = "accountHolderName";
        public static final String COLUMN_BALANCE = "balance";


    }
    /**Query for creating 'account' table **/
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AccountEntry.TABLE_NAME + " (" +
                    AccountEntry.COLUMN_ACCOUNT_NO+ " TEXT PRIMARY KEY," +
                    AccountEntry.COLUMN_BANK_NAME + " TEXT," +
                    AccountEntry.COLUMN_ACCOUNT_HOLDER_NAME + " TEXT,"+
                    AccountEntry.COLUMN_BALANCE + " DOUBLE)";

    /**Query for droping 'account' table **/
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AccountEntry.TABLE_NAME;

}
