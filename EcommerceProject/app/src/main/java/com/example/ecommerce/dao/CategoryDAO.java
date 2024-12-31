package com.example.ecommerce.dao;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ecommerce.database.DatabaseHelper;
import com.example.ecommerce.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context context;

    public CategoryDAO(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public List<Category> getCategories() {
        List<Category> cates = new ArrayList<>();
        Cursor cursor = this.getAllCategories();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_NAME));
            Category cate = new Category(id, name);
            cates.add(cate);
        }
        cursor.close();
        this.close();
        return cates;
    }

    private Cursor getAllCategories() {
        String[] columns = {
                DatabaseHelper.COLUMN_CATEGORY_ID,
                DatabaseHelper.COLUMN_CATEGORY_NAME
        };
        return database.query(DatabaseHelper.TABLE_CATEGORY, columns, null, null, null, null, null);
    }

    public Category getCategoryById(int id) {
        String[] columns = {
                DatabaseHelper.COLUMN_CATEGORY_ID,
                DatabaseHelper.COLUMN_CATEGORY_NAME
        };
        String selection = DatabaseHelper.COLUMN_CATEGORY_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_CATEGORY,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            Category cate = new Category(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_NAME))
            );
            cursor.close();
            return cate;
        } else {
            assert cursor != null;
            cursor.close();
            return null;
        }
    }
    public boolean insertCategory(Category cate) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CATEGORY_NAME, cate.getName());
        long result = database.insert(DatabaseHelper.TABLE_CATEGORY, null, values);
        return result != -1;
    }

    public boolean updateCategory(Category newCate) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CATEGORY_NAME, newCate.getName());

        String selection = DatabaseHelper.COLUMN_CATEGORY_ID + " = ?";
        String[] selectionArgs = { String.valueOf(newCate.getId()) };
        long result = database.update(DatabaseHelper.TABLE_CATEGORY, values, selection, selectionArgs);
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
