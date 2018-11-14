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




    public Cursor readDatabase(String argument){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        // Need to explicitly name columns to select here
        String [] sqlSelect = {"", "", "", "", ""};
        // Table name
        String sqlTables = "";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        c.moveToFirst();
        return c;
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

        String [] sqlSelect = {"_id", "Lat", "Long", "Type"};
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

        String [] sqlSelect = {"_id", "Lat", "Long", "Type"};
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

        String [] sqlSelect = {"_id", "Lat", "Lon","Name"};
        String sqlTables = "recyclingCenters";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        c.moveToFirst();
        return c;
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




}
