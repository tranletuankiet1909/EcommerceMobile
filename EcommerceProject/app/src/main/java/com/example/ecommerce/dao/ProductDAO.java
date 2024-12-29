package com.example.ecommerce.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObservable;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import com.example.ecommerce.database.DatabaseHelper;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.Store;
import com.example.ecommerce.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context context;

    public ProductDAO(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public Cursor getAllProducts(){
        String[] columns = {
                DatabaseHelper.COLUMN_PRODUCT_ID,
                DatabaseHelper.COLUMN_PRODUCT_NAME,
                DatabaseHelper.COLUMN_PRODUCT_IMAGE,
                DatabaseHelper.COLUMN_PRODUCT_DESCRIPTION,
                DatabaseHelper.COLUMN_PRODUCT_PRICE,
                DatabaseHelper.COLUMN_PRODUCT_QUANTITY,
                DatabaseHelper.COLUMN_PRODUCT_CATEGORY_ID,
                DatabaseHelper.COLUMN_PRODUCT_STORE_ID
        };
        return database.query(DatabaseHelper.TABLE_PRODUCT, columns, null, null, null, null, null);
    }
    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        Cursor cursor = this.getAllProducts();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_NAME));
            String image = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_IMAGE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_DESCRIPTION));
            String price = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_PRICE));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_QUANTITY));
            int cateId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_CATEGORY_ID));
            int storeId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_STORE_ID));

            CategoryDAO categoryDAO = new CategoryDAO(this.context);
            StoreDAO storeDAO = new StoreDAO(this.context);
            categoryDAO.open();
            storeDAO.open();
            Category cate = categoryDAO.getCategoryById(cateId);
            Store store = storeDAO.getStoreById(storeId);
            categoryDAO.close();
            storeDAO.close();
            Product product = new Product(id, name, image, description, price, quantity, cate, store);
            products.add(product);
        }
        cursor.close();
        this.close();
        return products;
    }
    public Product getProductById(int id) {
        String[] columns = {
                DatabaseHelper.COLUMN_PRODUCT_ID,
                DatabaseHelper.COLUMN_PRODUCT_NAME,
                DatabaseHelper.COLUMN_PRODUCT_IMAGE,
                DatabaseHelper.COLUMN_PRODUCT_DESCRIPTION,
                DatabaseHelper.COLUMN_PRODUCT_PRICE,
                DatabaseHelper.COLUMN_PRODUCT_QUANTITY,
                DatabaseHelper.COLUMN_PRODUCT_CATEGORY_ID,
                DatabaseHelper.COLUMN_PRODUCT_STORE_ID
        };
        String selection = DatabaseHelper.COLUMN_PRODUCT_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_PRODUCT,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            CategoryDAO categoryDAO = new CategoryDAO(this.context);
            StoreDAO storeDAO = new StoreDAO(this.context);
            categoryDAO.open();
            storeDAO.open();

            Product product = new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_IMAGE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_PRICE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_QUANTITY)),
                    categoryDAO.getCategoryById(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_CATEGORY_ID))),
                    storeDAO.getStoreById(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_STORE_ID)))
                    );
            categoryDAO.close();
            storeDAO.close();
            cursor.close();
            return product;
        } else {
            assert cursor != null;
            cursor.close();
            return null;
        }
    }
    public boolean insertProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PRODUCT_NAME, product.getName());
        values.put(DatabaseHelper.COLUMN_PRODUCT_IMAGE, product.getImage());
        values.put(DatabaseHelper.COLUMN_PRODUCT_DESCRIPTION, product.getDescription());
        values.put(DatabaseHelper.COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(DatabaseHelper.COLUMN_PRODUCT_QUANTITY, product.getQuantity());
        values.put(DatabaseHelper.COLUMN_PRODUCT_CATEGORY_ID, product.getCategory().getId());
        values.put(DatabaseHelper.COLUMN_PRODUCT_STORE_ID, product.getStore().getId());
        long result = database.insert(DatabaseHelper.TABLE_PRODUCT, null, values);
        return result != -1;
    }

    public boolean updateProduct(Product newProduct) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PRODUCT_NAME, newProduct.getName());
        values.put(DatabaseHelper.COLUMN_PRODUCT_IMAGE, newProduct.getImage());
        values.put(DatabaseHelper.COLUMN_PRODUCT_DESCRIPTION, newProduct.getDescription());
        values.put(DatabaseHelper.COLUMN_PRODUCT_PRICE, newProduct.getPrice());
        values.put(DatabaseHelper.COLUMN_PRODUCT_QUANTITY, newProduct.getQuantity());
        values.put(DatabaseHelper.COLUMN_PRODUCT_CATEGORY_ID, newProduct.getCategory().getId());
        values.put(DatabaseHelper.COLUMN_PRODUCT_STORE_ID, newProduct.getStore().getId());

        String selection = DatabaseHelper.COLUMN_PRODUCT_ID + " = ?";
        String[] selectionArgs = { String.valueOf(newProduct.getId()) };
        long result = database.update(DatabaseHelper.TABLE_PRODUCT, values, selection, selectionArgs);

        return result != -1;
    }

    public List<Product> getProductsByStore(int storeId){
        List<Product> products = new ArrayList<>();

        String[] columns = {
                DatabaseHelper.COLUMN_PRODUCT_ID,
                DatabaseHelper.COLUMN_PRODUCT_NAME,
                DatabaseHelper.COLUMN_PRODUCT_IMAGE,
                DatabaseHelper.COLUMN_PRODUCT_DESCRIPTION,
                DatabaseHelper.COLUMN_PRODUCT_PRICE,
                DatabaseHelper.COLUMN_PRODUCT_QUANTITY,
                DatabaseHelper.COLUMN_PRODUCT_CATEGORY_ID,
                DatabaseHelper.COLUMN_PRODUCT_STORE_ID
        };

        String selection = DatabaseHelper.COLUMN_PRODUCT_STORE_ID + " = ?";
        String[] selectionArgs = { String.valueOf(storeId) };

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_PRODUCT,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_NAME));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_IMAGE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_DESCRIPTION));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_QUANTITY));
                int cateId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_CATEGORY_ID));

                CategoryDAO categoryDAO = new CategoryDAO(this.context);
                categoryDAO.open();
                Category category = categoryDAO.getCategoryById(cateId);
                categoryDAO.close();

                StoreDAO storeDAO = new StoreDAO(this.context);
                storeDAO.open();
                Store store = storeDAO.getStoreById(storeId);
                storeDAO.close();

                Product product = new Product(id, name, image, description, price, quantity, category, store);
                products.add(product);
            }
            cursor.close();
        }
        this.close();
        return products;
    }

    public List<Product> getProductsByCategory(int cateId){
        List<Product> products = new ArrayList<>();

        String[] columns = {
                DatabaseHelper.COLUMN_PRODUCT_ID,
                DatabaseHelper.COLUMN_PRODUCT_NAME,
                DatabaseHelper.COLUMN_PRODUCT_IMAGE,
                DatabaseHelper.COLUMN_PRODUCT_DESCRIPTION,
                DatabaseHelper.COLUMN_PRODUCT_PRICE,
                DatabaseHelper.COLUMN_PRODUCT_QUANTITY,
                DatabaseHelper.COLUMN_PRODUCT_CATEGORY_ID,
                DatabaseHelper.COLUMN_PRODUCT_STORE_ID
        };

        String selection = DatabaseHelper.COLUMN_PRODUCT_CATEGORY_ID + " = ?";
        String[] selectionArgs = { String.valueOf(cateId) };

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_PRODUCT,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_NAME));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_IMAGE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_DESCRIPTION));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_QUANTITY));
                int storeId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_STORE_ID));

                CategoryDAO categoryDAO = new CategoryDAO(this.context);
                categoryDAO.open();
                Category category = categoryDAO.getCategoryById(cateId);
                categoryDAO.close();

                StoreDAO storeDAO = new StoreDAO(this.context);
                storeDAO.open();
                Store store = storeDAO.getStoreById(storeId);
                storeDAO.close();

                Product product = new Product(id, name, image, description, price, quantity, category, store);
                products.add(product);
            }
            cursor.close();
        }
        this.close();
        return products;
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
