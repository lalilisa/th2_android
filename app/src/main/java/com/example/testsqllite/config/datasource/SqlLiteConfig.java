package com.example.testsqllite.config.datasource;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlLiteConfig extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "schoolManager.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "students";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PHONE_NUMBER = "phone_number";

    public SqlLiteConfig(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_students_table = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT , %s TEXT, %s TEXT, %s TEXT)", TABLE_NAME, KEY_ID, KEY_NAME, KEY_ADDRESS, KEY_PHONE_NUMBER);
        db.execSQL(create_students_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_students_table = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(drop_students_table);
        onCreate(db);
    }
    public  void  open(){
        this.getReadableDatabase();
    }

}
