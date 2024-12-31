package com.example.ecommerce.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ecommerce.database.DatabaseHelper;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;

import java.util.ArrayList;
import java.util.List;

public class CartDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context context;

    public CartDAO(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public List<Cart> getCartByUserId(int userId) {
        List<Cart> carts = new ArrayList<>();

        String[] columns = {
                DatabaseHelper.COLUMN_CART_ID,
                DatabaseHelper.COLUMN_CART_BUYER_ID,
                DatabaseHelper.COLUMN_CART_PRODUCT_ID,
                DatabaseHelper.COLUMN_CART_QUANTITY,
                DatabaseHelper.COLUMN_CART_SELECTED
        };
        String selection = DatabaseHelper.COLUMN_CART_BUYER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId)};
        open();
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_CART,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_ID));
            int buyerId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_BUYER_ID));
            int productId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_PRODUCT_ID));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_QUANTITY));
            int selected  = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_SELECTED));

            UserDAO userDAO = new UserDAO(this.context);
            userDAO.open();
            User buyer = userDAO.getUserById(buyerId);
            userDAO.close();

            ProductDAO productDAO = new ProductDAO(this.context);
            productDAO.open();
            Product product = productDAO.getProductById(productId);
            productDAO.close();

            Cart cart = new Cart(id, quantity, selected==1, buyer, product);
            carts.add(cart);
        }
        cursor.close();
        this.close();
        return carts;
    }
    public List<Cart> getSelectedCartItems(int userId) {
        List<Cart> carts = new ArrayList<>();

        String[] columns = {
                DatabaseHelper.COLUMN_CART_ID,
                DatabaseHelper.COLUMN_CART_BUYER_ID,
                DatabaseHelper.COLUMN_CART_PRODUCT_ID,
                DatabaseHelper.COLUMN_CART_QUANTITY,
                DatabaseHelper.COLUMN_CART_SELECTED
        };
        String selection = DatabaseHelper.COLUMN_CART_BUYER_ID + " = ? AND " + DatabaseHelper.COLUMN_CART_SELECTED + " = 1";
        String[] selectionArgs = { String.valueOf(userId) };
        open();
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_CART,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_ID));
            int buyerId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_BUYER_ID));
            int productId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_PRODUCT_ID));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_QUANTITY));
            int selected  = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_SELECTED));

            UserDAO userDAO = new UserDAO(this.context);
            userDAO.open();
            User buyer = userDAO.getUserById(buyerId);
            userDAO.close();

            ProductDAO productDAO = new ProductDAO(this.context);
            productDAO.open();
            Product product = productDAO.getProductById(productId);
            productDAO.close();

            Cart cart = new Cart(id, quantity, selected==1, buyer, product);
            carts.add(cart);
        }
        cursor.close();
        this.close();
        return carts;
    }

    public boolean isExistProductInCartOfUser(int userId, int productId) {
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_CART + " WHERE " +
                DatabaseHelper.COLUMN_CART_BUYER_ID + " = ? AND " + DatabaseHelper.COLUMN_CART_PRODUCT_ID + " = ?";
        open();
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(productId)});

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        this.close();
        return exists;
    }

    public int sumOfQuantityInCartOfUser(int userId) {
        int totalQuantity = 0;
        String query = "SELECT SUM(" + DatabaseHelper.COLUMN_CART_QUANTITY + ") AS TotalQuantity FROM " +
                DatabaseHelper.TABLE_CART + " WHERE " + DatabaseHelper.COLUMN_CART_BUYER_ID + " = ?";
        open();
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            totalQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("TotalQuantity"));
        }
        cursor.close();
        this.close();
        return totalQuantity;
    }
    public long totalPriceInCartOfUser(int userId) {
        long totalPrice = 0;
        String query = "SELECT SUM(c." + DatabaseHelper.COLUMN_CART_QUANTITY + " * p." + DatabaseHelper.COLUMN_PRODUCT_PRICE + ") AS TotalPrice " +
                "FROM " + DatabaseHelper.TABLE_CART + " c " +
                "JOIN " + DatabaseHelper.TABLE_PRODUCT + " p " +
                "ON c." + DatabaseHelper.COLUMN_CART_PRODUCT_ID + " = p." + DatabaseHelper.COLUMN_PRODUCT_ID + " " +
                "WHERE c." + DatabaseHelper.COLUMN_CART_BUYER_ID + " = ? " +
                "AND c." + DatabaseHelper.COLUMN_CART_SELECTED + " = 1";
        open();
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            totalPrice = cursor.getLong(cursor.getColumnIndexOrThrow("TotalPrice"));
        }
        cursor.close();
        this.close();
        return totalPrice;
    }
    public Cart getCartByUserAndProduct(int userId, int productId) {
        String[] columns = {
                DatabaseHelper.COLUMN_CART_ID,
                DatabaseHelper.COLUMN_CART_BUYER_ID,
                DatabaseHelper.COLUMN_CART_PRODUCT_ID,
                DatabaseHelper.COLUMN_CART_QUANTITY,
                DatabaseHelper.COLUMN_CART_SELECTED
        };
        String selection = DatabaseHelper.COLUMN_CART_BUYER_ID + " = ? AND "
                + DatabaseHelper.COLUMN_CART_PRODUCT_ID + " = ?;";
        String[] selectionArgs = { String.valueOf(userId), String.valueOf(productId) };
        open();
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_CART,
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
            User buyer = userDAO.getUserById(userId);
            Product product = productDAO.getProductById(productId);
            userDAO.close();
            productDAO.close();

            Cart cart = new Cart(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_QUANTITY)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_SELECTED))==1,
                    buyer,
                    product
            );
            cursor.close();
            this.close();
            return cart;
        } else {
            cursor.close();
            this.close();
            return null;
        }
    }

    public boolean insertCart(Cart cart) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CART_BUYER_ID, cart.getBuyer().getId());
        values.put(DatabaseHelper.COLUMN_CART_PRODUCT_ID, cart.getProduct().getId());
        values.put(DatabaseHelper.COLUMN_CART_QUANTITY, cart.getQuantity());
        int isSelected = cart.getSelected()?1:0;
        values.put(DatabaseHelper.COLUMN_CART_SELECTED, isSelected);
        open();
        long result = database.insert(DatabaseHelper.TABLE_CART, null, values);
        close();
        return result != -1;
    }

    public boolean updateCart(Cart newCart) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CART_BUYER_ID, newCart.getBuyer().getId());
        values.put(DatabaseHelper.COLUMN_CART_PRODUCT_ID, newCart.getProduct().getId());
        values.put(DatabaseHelper.COLUMN_CART_QUANTITY, newCart.getQuantity());
        int isSelected = newCart.getSelected()?1:0;
        values.put(DatabaseHelper.COLUMN_CART_SELECTED, isSelected);

        String selection = DatabaseHelper.COLUMN_CART_ID + " = ?";
        String[] selectionArgs = { String.valueOf(newCart.getId()) };
        open();
        long result = database.update(DatabaseHelper.TABLE_CART, values, selection, selectionArgs);
        close();
        return result != -1;
    }

    public boolean removeSelectedItems(int userId) {
        String selection = DatabaseHelper.COLUMN_CART_BUYER_ID + " = ? AND " + DatabaseHelper.COLUMN_CART_SELECTED + " = 1";

        String[] selectionArgs = {String.valueOf(userId)};
        open();
        int result = database.delete(DatabaseHelper.TABLE_CART, selection, selectionArgs);
        close();
        return result > 0;
    }


    public void open() {
        if (database == null || !database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }
    }

    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

}
