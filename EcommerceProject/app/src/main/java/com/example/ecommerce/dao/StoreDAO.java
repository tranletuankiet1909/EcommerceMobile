package com.example.ecommerce.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObservable;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import com.example.ecommerce.database.DatabaseHelper;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Store;
import com.example.ecommerce.entity.User;

import java.util.ArrayList;
import java.util.List;

public class StoreDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context context;

    public StoreDAO(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public Cursor getAllStore() {
        String[] columns = {
                DatabaseHelper.COLUMN_STORE_ID,
                DatabaseHelper.COLUMN_STORE_NAME,
                DatabaseHelper.COLUMN_STORE_ADDRESS,
                DatabaseHelper.COLUMN_STORE_IMAGE,
                DatabaseHelper.COLUMN_STORE_OWNER_ID
        };
        return database.query(DatabaseHelper.TABLE_STORE, columns, null, null, null, null, null);
    }
    public List<Store> getStores() {
        List<Store> stores = new ArrayList<>();
        Cursor cursor = this.getAllStore();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STORE_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STORE_NAME));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STORE_ADDRESS));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STORE_IMAGE));
                int ownerId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STORE_OWNER_ID));
                UserDAO userDAO = new UserDAO(this.context);
                userDAO.open();
                User owner = userDAO.getUserById(ownerId);
                Store store = new Store(id, name, address, image, owner);
                stores.add(store);
                userDAO.close();
            }
        }

        cursor.close();
        this.close();
        return stores;
    }
    public Store getStoreById(int id) {
        String[] columns = {
                DatabaseHelper.COLUMN_STORE_ID,
                DatabaseHelper.COLUMN_STORE_NAME,
                DatabaseHelper.COLUMN_STORE_ADDRESS,
                DatabaseHelper.COLUMN_STORE_IMAGE,
                DatabaseHelper.COLUMN_STORE_OWNER_ID
        };
        String selection = DatabaseHelper.COLUMN_STORE_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_STORE,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        UserDAO userDAO = new UserDAO(this.context);
        userDAO.open();
        if (cursor != null && cursor.moveToFirst()) {
            Store store = new Store(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STORE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STORE_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STORE_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STORE_IMAGE)),
                    userDAO.getUserById(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STORE_OWNER_ID))
                    );
            userDAO.close();
            cursor.close();
            return store;
        } else {
            userDAO.close();
            assert cursor != null;
            cursor.close();
            return null;
        }
    }
    public boolean insertStore(Store store) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_STORE_NAME, store.getName());
        values.put(DatabaseHelper.COLUMN_STORE_ADDRESS, store.getAddress());
        values.put(DatabaseHelper.COLUMN_STORE_IMAGE, store.getImage());
        values.put(DatabaseHelper.COLUMN_STORE_OWNER_ID, store.getOwner().getId());
        long result = database.insert(DatabaseHelper.TABLE_STORE, null, values);
        return result != -1;
    }

    public boolean updateStore(Store newStore) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_STORE_NAME, newStore.getName());
        values.put(DatabaseHelper.COLUMN_STORE_ADDRESS, newStore.getAddress());
//        values.put(DatabaseHelper.COLUMN_STORE_IMAGE, newStore.getImage());
//        values.put(DatabaseHelper.COLUMN_STORE_OWNER_ID, newStore.getOwner().getId());

        String selection = DatabaseHelper.COLUMN_STORE_ID + " = ?";
        String[] selectionArgs = { String.valueOf(newStore.getId()) };
        long result = database.update(DatabaseHelper.TABLE_STORE, values, selection, selectionArgs);

        return result != -1;
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
