package com.example.my_application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RssDatabase extends SQLiteOpenHelper {

    public RssDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }

    public void addItem(String tableName,String item){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put("iterator",2);
        values.put("item",item);
        try {
            database.insertOrThrow(tableName, null, values);
        }catch (SQLException e){
            Log.d("Error",e.getMessage());
        }
    }

    public void removeItem(String tableName, int id){
        SQLiteDatabase database = getWritableDatabase();
        String[] args = {""+id};
        database.delete(tableName,"iterator=?",args);
    }

    public Cursor getAll(String tableName){
        String[] columns = {"iterator","item"};
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(tableName,columns,null,null,null,null,null);
        return cursor;
    }

    public void createTable(SQLiteDatabase db, String tableName){
        db.execSQL(
                "create table if not exists "+tableName+"("+
                        "iterator integer primary key autoincrement,"+
                        "item text);"
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "drop table if exists words"
        );
        db.execSQL(
                "drop table if exists rss"
        );
        createTable(db,"rss");
        createTable(db,"words");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
