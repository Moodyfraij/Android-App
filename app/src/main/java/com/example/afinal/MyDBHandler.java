package com.example.database;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
public class MyDBHandler extends SQLiteOpenHelper {
    //DB-related constants (DB name, version, tables etc)
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "productDB.db";
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "productname";
    public static final String COLUMN_QUANTITY = "quantity";

    //Constructor
    public MyDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    //Create the DB schema (table products)
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_PRODUCTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_PRODUCTNAME + " TEXT," +
                COLUMN_QUANTITY + " INTEGER" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    //Update the DB schema: here I simply drop the table and recreate it
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    //Method to add a product in the DB
    public void addProduct(com.example.database.Product product) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    //Method to find a product in the DB based on its name
    public com.example.database.Product findProduct(String productname) {
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " +
                COLUMN_PRODUCTNAME + " = '" + productname + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        com.example.database.Product product = new com.example.database.Product();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            product.setID(Integer.parseInt(cursor.getString(0)));
            product.setProductName(cursor.getString(1));
            product.setQuantity(Integer.parseInt(cursor.getString(2)));
            cursor.close();
        } else {
            product = null;
        }
        db.close();
        return product;
    }

    //Method to delete a product in the DB based on its name
    public boolean deleteProduct(String productname) {
        boolean result = false;
        com.example.database.Product product = findProduct(productname);
        if (product != null) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(product.getID())});
            result = true;
            db.close();
        }
        return result;
    }

}
