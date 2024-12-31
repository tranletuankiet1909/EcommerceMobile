package com.example.ecommerce.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ecommerce.database.DatabaseHelper;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.PurchaseOrder;
import com.example.ecommerce.entity.PurchaseOrderDetail;

import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context context;

    public OrderDAO(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public List<PurchaseOrder> getOrders() {
        List<PurchaseOrder> orders = new ArrayList<>();

        String[] columns = {
                DatabaseHelper.COLUMN_ORDER_ID,
                DatabaseHelper.COLUMN_ORDER_BUYER_ID,
                DatabaseHelper.COLUMN_ORDER_STORE_ID,
                DatabaseHelper.COLUMN_ORDER_STATUS,
                DatabaseHelper.COLUMN_ORDER_TOTAL_PRICE,
        };

        open();
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_ORDER,
                columns,
                null,
                null,
                null,
                null,
                null
        );
        UserDAO userDAO = new UserDAO(this.context);
        StoreDAO storeDAO = new StoreDAO(this.context);
        userDAO.open();
        storeDAO.open();
        while (cursor.moveToNext()) {
            PurchaseOrder order = new PurchaseOrder(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_TOTAL_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_STATUS)),
                    userDAO.getUserById(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_BUYER_ID))),
                    storeDAO.getStoreById(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_STORE_ID)))
            );
            orders.add(order);
        }
        userDAO.close();
        storeDAO.close();
        cursor.close();
        this.close();
        return orders;
    }

    public PurchaseOrder getOrderById(int orderId) {
        String[] columns = {
                DatabaseHelper.COLUMN_ORDER_ID,
                DatabaseHelper.COLUMN_ORDER_BUYER_ID,
                DatabaseHelper.COLUMN_ORDER_STORE_ID,
                DatabaseHelper.COLUMN_ORDER_STATUS,
                DatabaseHelper.COLUMN_ORDER_TOTAL_PRICE,
        };
        String selection = DatabaseHelper.COLUMN_ORDER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(orderId) };
        open();
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_ORDER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            UserDAO userDAO = new UserDAO(this.context);
            StoreDAO storeDAO = new StoreDAO(this.context);
            userDAO.open();
            storeDAO.open();

            PurchaseOrder product = new PurchaseOrder(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_TOTAL_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_STATUS)),
                    userDAO.getUserById(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_BUYER_ID))),
                    storeDAO.getStoreById(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_STORE_ID)))
            );
            userDAO.close();
            storeDAO.close();
            cursor.close();
            return product;
        } else {
            assert cursor != null;
            cursor.close();
            return null;
        }
    }

    public List<PurchaseOrder> getOrderByStatusAndUserId(int userId, String status) {
        List<PurchaseOrder> orders = new ArrayList<>();

        String[] columns = {
                DatabaseHelper.COLUMN_ORDER_ID,
                DatabaseHelper.COLUMN_ORDER_BUYER_ID,
                DatabaseHelper.COLUMN_ORDER_STORE_ID,
                DatabaseHelper.COLUMN_ORDER_STATUS,
                DatabaseHelper.COLUMN_ORDER_TOTAL_PRICE,
        };
        String selection = DatabaseHelper.COLUMN_CART_BUYER_ID + " = ? AND " + DatabaseHelper.COLUMN_ORDER_STATUS + " = ?";
        String[] selectionArgs = { String.valueOf(userId), status };
        open();
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_ORDER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        UserDAO userDAO = new UserDAO(this.context);
        StoreDAO storeDAO = new StoreDAO(this.context);
        userDAO.open();
        storeDAO.open();
        while (cursor.moveToNext()) {
            PurchaseOrder order = new PurchaseOrder(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_TOTAL_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_STATUS)),
                    userDAO.getUserById(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_BUYER_ID))),
                    storeDAO.getStoreById(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_STORE_ID)))
            );
            orders.add(order);
        }
        userDAO.close();
        storeDAO.close();
        cursor.close();
        this.close();
        return orders;
    }

    public long insertOrder(PurchaseOrder order) {
        ContentValues values = new ContentValues();
//        values.put(DatabaseHelper.COLUMN_ORDER_ID, order.getId());
        values.put(DatabaseHelper.COLUMN_ORDER_BUYER_ID, order.getBuyer().getId());
        values.put(DatabaseHelper.COLUMN_ORDER_STORE_ID, order.getStore().getId());
        values.put(DatabaseHelper.COLUMN_ORDER_STATUS, order.getStatus());
        values.put(DatabaseHelper.COLUMN_ORDER_TOTAL_PRICE, order.getTotalPrice());
        open();
        long result = database.insert(DatabaseHelper.TABLE_ORDER, null, values);
        close();
        return result;
    }

    public boolean updateOrder(PurchaseOrder order) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ORDER_BUYER_ID, order.getBuyer().getId());
        values.put(DatabaseHelper.COLUMN_ORDER_STORE_ID, order.getStore().getId());
        values.put(DatabaseHelper.COLUMN_ORDER_STATUS, order.getStatus());
        values.put(DatabaseHelper.COLUMN_ORDER_TOTAL_PRICE, order.getTotalPrice());

        String selection = DatabaseHelper.COLUMN_ORDER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(order.getId()) };
        open();
        long result = database.update(DatabaseHelper.TABLE_ORDER, values, selection, selectionArgs);
        close();
        return result != -1;
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
