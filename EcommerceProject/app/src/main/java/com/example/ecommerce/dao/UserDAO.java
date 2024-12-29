package com.example.ecommerce.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ecommerce.database.DatabaseHelper;
import com.example.ecommerce.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context context;

    public UserDAO(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        Cursor cursor = this.getAllUsers();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID));
            String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_USERNAME));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PASSWORD));
            String fullname = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_FULLNAME));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL));
            String role = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ROLE));
            String avatar = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_AVATAR));
            User user = new User(id, username, password, fullname, email, role, avatar);
            users.add(user);
        }
        cursor.close();
        this.close();
        return users;
    }

    public Cursor getAllUsers(){
        String[] columns = {
                DatabaseHelper.COLUMN_USER_ID,
                DatabaseHelper.COLUMN_USER_USERNAME,
                DatabaseHelper.COLUMN_USER_PASSWORD,
                DatabaseHelper.COLUMN_USER_FULLNAME,
                DatabaseHelper.COLUMN_USER_EMAIL,
                DatabaseHelper.COLUMN_USER_ROLE,
                DatabaseHelper.COLUMN_USER_AVATAR
        };
        return database.query(DatabaseHelper.TABLE_USER, columns, null, null, null, null, null);
    }

    public User getUserById(int id){
        String[] columns = {
                DatabaseHelper.COLUMN_USER_ID,
                DatabaseHelper.COLUMN_USER_USERNAME,
                DatabaseHelper.COLUMN_USER_PASSWORD,
                DatabaseHelper.COLUMN_USER_FULLNAME,
                DatabaseHelper.COLUMN_USER_EMAIL,
                DatabaseHelper.COLUMN_USER_ROLE,
                DatabaseHelper.COLUMN_USER_AVATAR
        };
        String selection = DatabaseHelper.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_FULLNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ROLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_AVATAR))
            );
            cursor.close();
            return user;
        } else {
            assert cursor != null;
            cursor.close();
            return null;
        }
    };

    public List<User> getUserByRole(String role) {
        List<User> users = new ArrayList<>();
        String[] columns = {
                DatabaseHelper.COLUMN_USER_ID,
                DatabaseHelper.COLUMN_USER_USERNAME,
                DatabaseHelper.COLUMN_USER_PASSWORD,
                DatabaseHelper.COLUMN_USER_FULLNAME,
                DatabaseHelper.COLUMN_USER_EMAIL,
                DatabaseHelper.COLUMN_USER_ROLE,
                DatabaseHelper.COLUMN_USER_AVATAR
        };

        String selection = DatabaseHelper.COLUMN_USER_ROLE + " = ?";
        String[] selectionArgs = { role };

        Cursor cursor = database.query(
               DatabaseHelper.TABLE_USER,
               columns,
               selection,
               selectionArgs,
               null,
               null,
               null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID));
            String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_USERNAME));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PASSWORD));
            String fullname = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_FULLNAME));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL));
            String userRole = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ROLE));
            String avatar = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_AVATAR));
            User user = new User(id, username, password, fullname, email, userRole, avatar);
            users.add(user);
        }
        cursor.close();
        return users;
    }

    public boolean updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_USERNAME, user.getUsername());
        values.put(DatabaseHelper.COLUMN_USER_FULLNAME, user.getFullName());
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, user.getEmail());
        values.put(DatabaseHelper.COLUMN_USER_AVATAR, user.getAvatar());

        String selection = DatabaseHelper.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(user.getId()) };
        long result = database.update(DatabaseHelper.TABLE_USER, values, selection, selectionArgs);
        return result != -1;

    }

    public User getUserByUsername(String username, String password) {
        String[] columns = {
                DatabaseHelper.COLUMN_USER_ID,
                DatabaseHelper.COLUMN_USER_USERNAME,
                DatabaseHelper.COLUMN_USER_PASSWORD,
                DatabaseHelper.COLUMN_USER_FULLNAME,
                DatabaseHelper.COLUMN_USER_EMAIL,
                DatabaseHelper.COLUMN_USER_ROLE,
                DatabaseHelper.COLUMN_USER_AVATAR
        };
        String selection = DatabaseHelper.COLUMN_USER_USERNAME + " = ? AND "
                + DatabaseHelper.COLUMN_USER_PASSWORD + " = ?;";
        String[] selectionArgs = { username, password };

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_FULLNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ROLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_AVATAR))
            );
            cursor.close();
            return user;
        } else {
            cursor.close();
            return null;
        }
    }
    public boolean registerUser(User user) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_USER_USERNAME, user.getUsername());
            values.put(DatabaseHelper.COLUMN_USER_PASSWORD, user.getPassword());
            values.put(DatabaseHelper.COLUMN_USER_FULLNAME, user.getFullName());
            values.put(DatabaseHelper.COLUMN_USER_EMAIL, user.getEmail());
            values.put(DatabaseHelper.COLUMN_USER_ROLE, user.getRole());
            values.put(DatabaseHelper.COLUMN_USER_AVATAR, user.getAvatar());

            long result = database.insert(DatabaseHelper.TABLE_USER, null, values);
            return result != -1;
    }

    public boolean deleteUserByUserName(String username) {
        String selection = DatabaseHelper.COLUMN_USER_USERNAME + " = ?";
        String[] selectionArgs = { username };
        long result = database.delete(DatabaseHelper.TABLE_USER, selection, selectionArgs);
        return result != -1;
    }

    public boolean isUsernameExists(String username){
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_USER + " WHERE " +
                DatabaseHelper.COLUMN_USER_USERNAME + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{username});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
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
