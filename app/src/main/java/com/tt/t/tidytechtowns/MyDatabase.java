package com.tt.t.tidytechtowns;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MyDatabase extends SQLiteAssetHelper //SQLiteOpenHelper??
{

    private static final String DATABASE_NAME = "tidytechtowndb.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void writeDatabase(Double lat,Double lon, String type) {
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("Lat", lat);
        values.put("Long", lon);
        values.put("Type", type);

        db.insert("mapMarkers", null, values);
        db.close();
    }

    public Cursor getBins()
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"_id", "Lat", "Long", "Type"};
        String sqlTables = "mapMarkers";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        c.moveToFirst();
        return c;
    }

    public Cursor getPloggingInfo()
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"_id", "Lat", "Long", "Type"};
        String sqlTables = "mapMarkers";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        c.moveToFirst();
        return c;
    }

    public Cursor getCenters()
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"id", "lat", "lon","Name"};
        String sqlTables = "recyclingCenters";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        c.moveToFirst();
        return c;
    }

    public void saveLogIn(String code) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        db.execSQL(" CREATE TABLE IF NOT EXISTS LOGIN_DETAILS " +
                "(code text PRIMARY KEY, community text)");

        ContentValues values = new ContentValues();

        values.put("code", code);
        values.put("community", "Dublin");

        db.insert("LOGIN_DETAILS", null, values);
        db.close();
    }

    public boolean loggedIn(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        boolean verify = false;
        Cursor cursor = db.rawQuery("SELECT count(*) FROM sqlite_master WHERE type = 'table' AND name = 'LOGIN_DETAILS'", null);
        cursor.moveToFirst();
        if ( cursor.getInt(0) >0 ) {
                verify = true;
        }
        db.close();
        return verify;
    }

    public Cursor getCommunities()
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        Cursor resultSet = db.rawQuery("Select * from communities",null);
        resultSet.moveToFirst();



        return resultSet;
    }

    public Cursor getIndScores()
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();


        Cursor resultSet = db.rawQuery("Select * from individual",null);
        resultSet.moveToFirst();

        return resultSet;

    }

    public Cursor getEvents()
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        Cursor resultSet = db.rawQuery("Select * from events",null);
        //resultSet.moveToFirst();


        return resultSet;
    }


    public Cursor getCommunityTotals()
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        Cursor resultSet = db.rawQuery("SELECT  community, SUM(personal_score) personal_score FROM individual GROUP BY community",null);
        resultSet.moveToFirst();
        db.close();

        return resultSet;
    }

    public Cursor getCarbonScore()
    {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(" CREATE TABLE IF NOT EXISTS CarbonScores " +
                "(id integer PRIMARY KEY DEFAULT 1, home NUMBER DEFAULT 0, travel NUMBER DEFAULT 0, total NUMBER DEFAULT 0)");

        Cursor x = db.rawQuery(" SELECT * FROM CarbonScores", null);
        x.moveToFirst();
        if(x.getCount()==0) {
            ContentValues values = new ContentValues();
            values.put("home", 0);
            values.put("travel", 0);
            values.put("total", 9);
            db.insert("CarbonScores", null, values);
        }

        Cursor resultSet = db.rawQuery("SELECT total FROM CarbonScores",null);
        resultSet.moveToFirst();
        db.close();

        return resultSet;
    }

    public void writeHomeCarbon(double home_score){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        double travel;
        Cursor cursor = db.rawQuery("SELECT travel FROM CarbonScores",null);
        cursor.moveToFirst();
        if (cursor.getCount()>0){
            travel = cursor.getDouble(0) ;
        }
        else{  travel = 0; }
        double total = home_score+travel;
        values.put("id", 1);
        values.put("home", home_score);
        values.put("travel", travel);
        values.put("total", total);

        //db.rawQuery("REPLACE INTO CarbonScore ()",null);
        db.replace("CarbonScores", null, values);
        db.close();
    }

    public void writeTravelCarbon(double travel_score){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        double home;
        Cursor cursor = db.rawQuery("SELECT  home FROM CarbonScores",null);
        cursor.moveToFirst();
        if (cursor.getCount()>0){
            home = cursor.getDouble(0) ;
        }
        else{  home = 0; }
        double total = home+travel_score;
        values.put("home", home);
        values.put("travel", travel_score);
        values.put("total", total);

        values.put("id", 1);
        db.replace("CarbonScores", null, values);
        db.close();
    }
}
