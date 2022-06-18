package com.firstapp.fa_mohitkumarbhagi_c0851229_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "mohit";
    public static final String TABLE_NAME = "favplaces";
    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table "+ TABLE_NAME +"(id integer primary key autoincrement, placename text, address text, latitude text, longitude text, visited text, createdon text)"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public boolean insert(String placename, String address, String latitude, String longitude, String visited, String createdon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("placename", placename);
        contentValues.put("address", address);
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
        contentValues.put("visited", visited);
        contentValues.put("createdon", createdon);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public AddressBean getAddress(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { "placename", "address",
                        "latitude", "longitude", "visited", "createdon" }, "id" + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        AddressBean ab = new AddressBean();

        ab.setId(cursor.getInt(0));
        ab.setPlacename(cursor.getString(1));
        ab.setAddress(cursor.getString(2));
        ab.setLatitude(cursor.getString(3));
        ab.setLongitude(cursor.getString(4));
        ab.setVisited(cursor.getString(5));
        ab.setCreatedon(cursor.getString(6));
        return ab;
    }


    public List<AddressBean> getAllFavPlaces() {
        List<AddressBean> addressBeanList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                AddressBean ab = new AddressBean();

                ab.setId(cursor.getInt(0));
                ab.setPlacename(cursor.getString(1));
                ab.setAddress(cursor.getString(2));
                ab.setLatitude(cursor.getString(3));
                ab.setLongitude(cursor.getString(4));
                ab.setVisited(cursor.getString(5));
                ab.setCreatedon(cursor.getString(6));

                addressBeanList.add(ab);
            } while (cursor.moveToNext());
        }

        return addressBeanList;
    }

    public int updateFavPlace(AddressBean ab) {
        Log.e("id...", ""+ab.getId());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("placename", ab.getPlacename());
        contentValues.put("address", ab.getAddress());
        contentValues.put("latitude", ab.getLatitude());
        contentValues.put("longitude", ab.getLongitude());
        contentValues.put("visited", ab.getVisited());
        contentValues.put("createdon", ab.getCreatedon());

        return db.update(TABLE_NAME, contentValues, "id" + " = ?",
                new String[] { String.valueOf(ab.getId()) });
    }

    public void deleteFavPlace(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id" + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }
}