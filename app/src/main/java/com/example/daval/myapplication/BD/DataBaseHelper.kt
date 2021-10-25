package com.example.daval.myapplication.BD

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.daval.myapplication.models.User
import java.util.*
import kotlin.collections.ArrayList


val CUSTOMER_TABLE = "CUSTOMER_TABLE"
val COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME"
val COLUMN_CUSTOMER_PHONE = "CUSTOMER_PHONE"
val COLUMN_CUSTOMER_EMAIL = "CUSTOMER_EMAIL"
val COLUMN_CUSTOMER_ID = "CUSTOMER_ID"
val COLUMN_ID = "ID"
class DataBaseHelper (var context: Context): SQLiteOpenHelper(context, "customer.db", null, 1 ) {

    //This method is called the first time a database is accesed
    //There should be code inside to create a new database
    // We need to create a table inside it
    override fun onCreate(db: SQLiteDatabase) {
        val createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CUSTOMER_NAME + " TEXT, " +
                COLUMN_CUSTOMER_PHONE + " TEXT, " +
                COLUMN_CUSTOMER_EMAIL + " TEXT, " +
                COLUMN_CUSTOMER_ID + "INT)"
        db.execSQL(createTableStatement)
    }

    //this is called if the database version number changes.
    //It prevents previous users apps from breaking when you change the database design
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    // Adding one row of database of object type customer model
    fun addOne(User: User): Boolean {
        //getWrittable for insert rows, update or delete records
        val db = this.writableDatabase
        // Content values stores data in pairs (cv.put("name", value), cv.getString("name")
        // It is similar to Put Extras in an Intent
        val cv = ContentValues()
        cv.put(COLUMN_CUSTOMER_NAME, User.name)
        cv.put(COLUMN_CUSTOMER_PHONE, User.phone)
        cv.put(COLUMN_CUSTOMER_EMAIL, User.email)
        cv.put(COLUMN_CUSTOMER_ID, User.id)
        val insert = db.insert(CUSTOMER_TABLE, null, cv)
        return insert != -1L
    }

    //deleting an item
    fun deleteOne(User: User): Boolean {
        //find User in the database. if it founded, deleted and return true
        // if it is not found, return false
        val db = this.writableDatabase
        val queryString =
            "DELETE FROM " + CUSTOMER_TABLE + " WHERE " + COLUMN_CUSTOMER_ID + " = " + User.id
        val cursor = db.rawQuery(queryString, null)
        return cursor.moveToFirst()
    }

    //adding method for selecting all items from the database
    fun getEveryone(): ArrayList<User>? {
        val returnList: ArrayList<User> = ArrayList<User>()
        // get data from the database
        val queryString = "SELECT * FROM $CUSTOMER_TABLE"
        val db = this.readableDatabase
        //cursor is the result of the query
        val cursor = db.rawQuery(queryString, null)
        // checking if the first row was selected
        if (cursor.moveToFirst()) {
            //loop through the cursor (result set) and create a new customer object and insert it in the return list
            do {
                val customerID = cursor.getInt(4)
                val customerName = cursor.getString(1)
                val customerPhone = cursor.getString(2)
                val customerEmail = cursor.getString(3)
                val newCustomer =
                    User(customerID, customerName, customerPhone, customerEmail)
                returnList.add(newCustomer)
            } while (cursor.moveToNext())
        } else {
            //failure. Do not add anything to the list
        }
        cursor.close()
        db.close()
        return returnList
    }
}