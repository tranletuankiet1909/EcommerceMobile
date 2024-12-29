package com.example.ecommerce.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ecommerce.database.DatabaseHelper;
import com.example.ecommerce.entity.Favorite;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context context;

    public FavoriteDAO(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public List<Favorite> getWishListByUserId(int userId) {
        List<Favorite> wishlist = new ArrayList<>();
        String[] columns = {
                DatabaseHelper.COLUMN_FAV_ID,
                DatabaseHelper.COLUMN_FAV_BUYER_ID,
                DatabaseHelper.COLUMN_FAV_PRODUCT_ID
        };
        String selection = DatabaseHelper.COLUMN_FAV_BUYER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_FAVORITE,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FAV_ID));
                int productId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FAV_PRODUCT_ID));
                UserDAO userDAO = new UserDAO(this.context);
                userDAO.open();
                User buyer = userDAO.getUserById(userId);
                userDAO.close();

                ProductDAO productDAO = new ProductDAO(this.context);
                productDAO.open();
                Product product = productDAO.getProductById(productId);
                productDAO.close();

                Favorite favorite = new Favorite(id, buyer, product);
                wishlist.add(favorite);
            }
            cursor.close();
        }
        this.close();
        return wishlist;
    }
    public boolean isFavorite(int productId, int buyerId){
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_FAVORITE + " WHERE " +
                DatabaseHelper.COLUMN_FAV_PRODUCT_ID + " = ? AND " + DatabaseHelper.COLUMN_FAV_BUYER_ID + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(productId), String.valueOf(buyerId)});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean insertProductToWishlist(int productId, int buyerId) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FAV_BUYER_ID, buyerId);
        values.put(DatabaseHelper.COLUMN_FAV_PRODUCT_ID, productId);
        long result = database.insert(DatabaseHelper.TABLE_FAVORITE, null, values);
        return result != -1;
    }

    public boolean deleteProductFromWishList(int buyerId, int productId) {
        String selection = DatabaseHelper.COLUMN_FAV_BUYER_ID + " = ? AND " + DatabaseHelper.COLUMN_FAV_PRODUCT_ID + " = ?";

        String[] selectionArgs = {String.valueOf(buyerId), String.valueOf(productId)};

        int result = database.delete(DatabaseHelper.TABLE_FAVORITE, selection, selectionArgs);

        return result > 0;
    }

    public Favorite getFavoriteById(int id) {
        String[] columns = {
                DatabaseHelper.COLUMN_FAV_ID,
                DatabaseHelper.COLUMN_FAV_BUYER_ID,
                DatabaseHelper.COLUMN_FAV_PRODUCT_ID
        };
        String selection = DatabaseHelper.COLUMN_FAV_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_FAVORITE,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            UserDAO userDAO = new UserDAO(this.context);
            ProductDAO productDAO = new ProductDAO(this.context);
            userDAO.open();
            productDAO.open();

            Favorite favorite = new Favorite(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FAV_ID)),
                    userDAO.getUserById(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FAV_BUYER_ID))),
                    productDAO.getProductById(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FAV_PRODUCT_ID)))
            );
            userDAO.close();
            productDAO.close();
            cursor.close();
            return favorite;
        } else {
            assert cursor != null;
            cursor.close();
            return null;
        }
    }
    public void open() {
        if (dbHelper == null) {
            dbHelper = new DatabaseHelper(context);
        }
        database = dbHelper.getWritableDatabase();
    }
    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
