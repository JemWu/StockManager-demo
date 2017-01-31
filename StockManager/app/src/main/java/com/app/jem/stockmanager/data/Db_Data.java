package com.app.jem.stockmanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jem on 2017/1/20.
 */

public class Db_Data extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    private int flag;
    private String name;
    private String parent_id;



    public Db_Data(Context context, String name,String parent_id,int flag) {
        super(context, name, null, VERSION);
        this.name = name;
        this.parent_id = parent_id;
        this.flag = flag;
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = creatSql();
        db.execSQL(sql);
    }

    private String creatSql() {

        String sql;

        if (flag == 1){
         sql = "CREATE TABLE IF NOT EXISTS "+ name +" (_ID INTEGER PRIMARY KEY AUTOINCREMENT, "+
                  parent_id + " TEXT_TYPE " + ")";
            return sql;
        }else if (flag == 2){
            sql = "CREATE TABLE IF NOT EXISTS "+ name +" (_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "MODEL TEXT_TYPE , PRICE INTEGER , NUMBER INTEGER )";
            return sql;
        }
        return null;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public
    boolean deleteDatabase(Context context) {
        return context.deleteDatabase(name);
    }
}
