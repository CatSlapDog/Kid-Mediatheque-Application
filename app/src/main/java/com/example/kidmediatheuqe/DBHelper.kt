package com.example.kidmediatheuqe

import android.content.ContentValues
import android.content.Context //import this to use context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // the constructor takes a context parameter of type Context. This context uses to access the database resources file.
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "UserDatabase"
        private const val TABLE_USERS = "Users"
        private const val KEY_NAME = "name"
        private const val KEY_AGE = "age"
        //the database use uppercase letters, example. UserDatabase here in kotlin refer to DATABASE_NAME in database language
    }

    //create database by override fun, first start with table users
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableSQL = "CREATE TABLE $TABLE_USERS ($KEY_NAME TEXT, $KEY_AGE INTEGER)" //give command to create user table in SQL
        // $ dollar signs uses to denote variables in the SQL language
        // the string in database use TEXT but the number use INTEGER same as kotlin
        db?.execSQL(createTableSQL) //execute to create table in SQL database send
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db) // Whenever the database version is increased,
        // it drops the existing TABLE_USERS (if it exists) and then recreates the entire database schema
        // (including potentially an updated TABLE_USERS) by calling onCreate.
    }

    fun insertUser(name: String, age: Int): Boolean {
        // takes two arguments: name and age The function returns boolean indicating success or failure.

        val db = this.writableDatabase
        // pulls database instance and assigns it to a local variable named

        val values = ContentValues()
        // creates a new ContentValues object named values.
        // used to store key-value pairs representing the data to be inserted into the database table.

        values.put(KEY_NAME, name)
        values.put(KEY_AGE, age)
        // these are add key-value pairs to the ContentValues object.
        // representing the column in database(uppercase letters) for storing the key-value(lower case letters)


        val success = db.insert(TABLE_USERS, null, values)
        // attempts to insert a new record into the TABLE_USERS table using the db.insert method.

        db.close() // closes the connection to the database after the insert operation is complete.

        return (Integer.parseInt("$success") != -1)
        // converts the success result (which might be an integer or a long) to an integer using Integer.parseInt
        // and checks if it's not equal to -1. In SQLite, the number of rows affected by insert operations is typically
        // greater than or equal to 1 (0 indicates no rows inserted).
        // The function returns true if the conversion to integer is successful and the value is not -1
        // (likely indicating successful insertion), otherwise it returns false.
    }
    //  This function takes user data (name and age), creates a content values object with the data,
    //  inserts it into the TABLE_USERS table, closes the database connection,
    //  and returns a boolean indicating success or failure based on the number of rows affected by the insert operation.


    fun getUserName(): String {
        //defines a function getUserName that doesn't take any arguments and returns a string value from database.

        val db = this.readableDatabase
        // pulls database instance and assigns it to a local variable named

        val selectQuery = "SELECT $KEY_NAME FROM $TABLE_USERS LIMIT 1"
        //SELECT statement that pulls value from column named KEY_NAME from the TABLE_USERS table in database.
        //The LIMIT 1 clause ensures only the first record is pulled, as the function aims to get just one username.

        val cursor = db.rawQuery(selectQuery, null)
        //This line executes the selectQuery on the database using db.rawQuery.
        //The result is stored in a Cursor object named cursor.
        //The null argument indicates no selection arguments are needed for this query.

        var userName = ""
        if (cursor.moveToFirst()) {
            //checks if the cursor in database has any results using the moveToFirst method.
            //If there's at least one row of data, the code inside the curly braces is executed.
            // ให้หาว่ามีข้อมูลบันทึกไว้ในดาต้าเบสไหม ถ้ามีข้อมูลอย่างน้อยสักหนึ่ง คือมีชื่อผู้ใช้ โค้ดในวงเล็บปีกกาทำงาน โค้ดบรรทัดล่างนี่ล่ะ ให้ดึงชื่อผู้ใช้มา

            val columnIndex = cursor.getColumnIndex(KEY_NAME)
            //pulls the index of the column named KEY_NAME within the cursor using getColumnIndex.
            //The index is stored in a variable named columnIndex.

            if (columnIndex != -1) { // Check if the column index is valid เช็คว่าค่าว่างไหม ถ้าไม่ว่าง โค้ดในปีกกาทำงาน
                userName = cursor.getString(columnIndex)
            //pulls the value of the column at the columnIndex from the cursor as a string and assigns it to the userName variable.
            //ถ้า columnIndex บรรทัดบนดึงข้อมูลมาได้ ให้ใส่ค่า userName ใน kotlin เป็นชื่อตาม database ที่ดึงมา
            }
        }
        cursor.close()
        //closes the cursor to release resources associated with the query.

        db.close()
        //closes the connection to the database.

        return userName
        //returns the userName string, which might be empty if no user was found.
    }
    //This function establishes a database connection,
    // executes a query to retrieve the username of the first user from a table,
    // extracts the value and stores it in a variable,
    // closes resources, and returns the username (or an empty string if no user is found).

}
