package com.teapink.damselindistress.activities;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.teapink.damselindistress.models.Contact;

import java.util.ArrayList;
import java.util.List;

public class DataBaseAdapter {
    static final String DATABASE_NAME = "wf04.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;
    static final String DATABASE_CREATE = "create table " + "LOGIN" + "( "
            + "ID" + " integer primary key autoincrement,"
            + "NAME text,USERNAME  text,PASSWORD text); ";
    static final String Product_CREATE = "create table " + "PHONETAB" + "( "
            + "ID" + " integer primary key autoincrement,"
            + "C_NAME text,P_NAME text,P_NO text); ";

    static final String Purchase_CREATE = "create table " + "ORDERS" + "( "
            + "ID" + " integer primary key autoincrement,"
            + "C_NAME text,P_ID text,P_NAME text," +
            "P_QTY text,P_PRICE text,STATUS text); ";

    static final String Alert_CREATE = "create table " + "ALERT" + "( "
            + "ID" + " integer primary key autoincrement,"
            + "STATUS integer,COUNT integer); ";


    public SQLiteDatabase db;
    private final Context context;
    private DataBaseHelper dbHelper;

    public DataBaseAdapter(Context _context) {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null,
                DATABASE_VERSION);
    }

    public DataBaseAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public void insertEntry(String userName, String password, String Name) {
        ContentValues newValues = new ContentValues();
        newValues.put("USERNAME", userName);
        newValues.put("PASSWORD", password);
        newValues.put("NAME", Name);
        db.insert("LOGIN", null, newValues);

    }
    public void insertAlert(int userName, int password) {
        ContentValues newValues = new ContentValues();
        newValues.put("STATUS", userName);
        newValues.put("COUNT", password);
        db.insert("ALERT", null, newValues);

    }
    public void insertProduct(String cno,String cat, String sub_cat) {
        ContentValues newValues = new ContentValues();
        newValues.put("C_NAME", cno);
        newValues.put("P_NAME", cat);
        newValues.put("P_NO", sub_cat);

        db.insert("PHONETAB", null, newValues);

    }
    public void insertOrders(String cat, String pid, String pname, String pqty, String pprice, String status) {
        ContentValues newValues = new ContentValues();
        newValues.put("C_NAME", cat);
        newValues.put("P_ID", pid);
        newValues.put("P_NAME", pname);
        newValues.put("P_QTY", pqty);
        newValues.put("P_PRICE", pprice);
        newValues.put("STATUS", status);
        db.insert("ORDERS", null, newValues);
    }
    public int deleteEntry(String UserName, String cName) {

        String where = "C_NAME=? and P_NO=?" ;
        int numberOFEntriesDeleted = db.delete("PHONETAB", where,
                new String[] { UserName,cName });
        return numberOFEntriesDeleted;
    }

    public String getSinlgeEntry(String userName) {
        Cursor cursor = db.query("LOGIN", null, " USERNAME=?",
                new String[] { userName }, null, null, null);
        if (cursor.getCount() < 1) {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
        cursor.close();
        return password;
    }

    public void updateEntry(String userName, String password, String type) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("PASSWORD", password);
        String where = "USERNAME = ? and TYPE=?";
        db.update("LOGIN", updatedValues, where, new String[] { userName,type });
    }
    public void updateAlertEntry(int userName, int password) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("STATUS", userName);
        updatedValues.put("COUNT", password);
        String where="ID=?";
        db.update("ALERT", updatedValues,where,new String[]{"1"});
    }
    public void updateOrdersEntry(String cusname) {
        String status="Paid";
        String Pre_State="Pending";
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("STATUS", status);
        String where="C_NAME=? AND STATUS=?";
        db.update("ORDERS", updatedValues,where,new String[]{cusname,Pre_State});
    }

    public String getID(String email){

        // Select All Query
        String selectQuery = "SELECT COUNT(*) FROM PRODUCT where P_CAT=?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{email});//selectQuery,selectedArguments

        // looping through all rows and adding to list
        cursor.moveToFirst();
        String value=cursor.getString(0);

        cursor.close();
        db.close();

        // returning lables
        return value;
    }

    public String getPrice(String email){

        // Select All Query
        String selectQuery = "SELECT P_PRICE FROM PRODUCT where P_ID=?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{email});//selectQuery,selectedArguments

        // looping through all rows and adding to list
        cursor.moveToFirst();
        String value=cursor.getString(0);

        cursor.close();
        db.close();

        // returning lables
        return value;
    }
    public String getSum(String email){

        // Select All Query
        String status="Pending";
        String selectQuery = "SELECT SUM(P_PRICE) FROM ORDERS where C_NAME=? AND STATUS=?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{email,status});//selectQuery,selectedArguments

        // looping through all rows and adding to list
        cursor.moveToFirst();
        String value=cursor.getString(0);

        cursor.close();
        db.close();

        // returning lables
        return value;
    }
    public int getCount(String email){

        // Select All Query
        String status="Pending";
        String selectQuery = "SELECT COUNT(C_NAME) FROM ORDERS where C_NAME=? AND STATUS=?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{email,status});//selectQuery,selectedArguments

        // looping through all rows and adding to list
        cursor.moveToFirst();
        int value= Integer.parseInt(cursor.getString(0));

        cursor.close();
        db.close();

        // returning lables
        return value;
    }
    public int getlimit(){

        // Select All Query
        String STATUS="1";

        String selectQuery = "SELECT * FROM ALERT WHERE STATUS=?  ";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{STATUS});//selectQuery,selectedArguments

        // looping through all rows and adding to list
        cursor.moveToFirst();
        int value= Integer.parseInt(cursor.getString(2));

        cursor.close();
        db.close();

        // returning lables
        return value;
    }
    public Cursor DisplayPartData(String tableName, String email)
    {
        //Select query

        //return db.query(TABLE_NAME, new String[]{NAME, ROLL,COURSE}, null, null, null, null, null);
        String status = "Pending";
        String selectQuery = "SELECT  * FROM "+tableName+" where C_NAME=? and STATUS=?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,new String[]{email,status});//selectQuery,selectedArguments
        return cursor;
    }
    public Cursor DisplayData(String tableName)
    {
        //Select query

        //return db.query(TABLE_NAME, new String[]{NAME, ROLL,COURSE}, null, null, null, null, null);
        String status = "Pending";
        String selectQuery = "SELECT  * FROM PHONETAB where C_NAME=?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,new String[]{tableName});//selectQuery,selectedArguments
        return cursor;
    }
    public String getQty(String email){

        // Select All Query
        String selectQuery = "SELECT P_QTY FROM PRODUCT where P_ID=?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{email});//selectQuery,selectedArguments

        // looping through all rows and adding to list
        cursor.moveToFirst();
        String value=cursor.getString(0);

        cursor.close();
        db.close();

        // returning lables
        return value;
    }
    // Getting All Contacts
    public ArrayList<Contact> getAllContacts(String cat) {
        ArrayList<Contact> contactList = new ArrayList<Contact>();
    // Select All Query
        String selectQuery = "SELECT * FROM PHONETAB WHERE C_NAME=?";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{cat});
    // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setPhone(cursor.getString(2));
                contact.setName(cursor.getString(3));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
    // close inserting data from database
        db.close();
    // return contact list
        return contactList;

    }
  }