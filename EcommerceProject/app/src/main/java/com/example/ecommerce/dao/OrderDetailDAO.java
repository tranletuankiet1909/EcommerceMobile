package com.example.ecommerce.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ecommerce.database.DatabaseHelper;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.PurchaseOrder;
import com.example.ecommerce.entity.PurchaseOrderDetail;
import com.example.ecommerce.entity.Store;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context context;

    public OrderDetailDAO(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public boolean insertOrderDetail(PurchaseOrderDetail orderDetail) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ORDER_DETAIL_ORDER_ID, orderDetail.getPurchaseOrder().getId());
        values.put(DatabaseHelper.COLUMN_ORDER_DETAIL_PRODUCT_ID, orderDetail.getProduct().getId());
        values.put(DatabaseHelper.COLUMN_ORDER_DETAIL_QUANTITY, orderDetail.getQuantity());
        values.put(DatabaseHelper.COLUMN_ORDER_DETAIL_PRICE, orderDetail.getPrice());
        open();
        long result = database.insert(DatabaseHelper.TABLE_ORDER_DETAIL, null, values);
        close();
        return result != -1;
    }

    public List<PurchaseOrderDetail> getOrderDetailsByOrderId(int orderId) {
        List<PurchaseOrderDetail> orderDetails = new ArrayList<>();

        String[] columns = {
                DatabaseHelper.COLUMN_ORDER_DETAIL_ID,
                DatabaseHelper.COLUMN_ORDER_DETAIL_ORDER_ID,
                DatabaseHelper.COLUMN_ORDER_DETAIL_PRODUCT_ID,
                DatabaseHelper.COLUMN_ORDER_DETAIL_QUANTITY,
                DatabaseHelper.COLUMN_ORDER_DETAIL_PRICE,
        };

        String selection = DatabaseHelper.COLUMN_ORDER_DETAIL_ORDER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(orderId)};

        this.open();
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_ORDER_DETAIL,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        OrderDAO orderDAO = new OrderDAO(this.context);
        orderDAO.open();
        PurchaseOrder order = orderDAO.getOrderById(orderId);
        orderDAO.close();

        while (cursor.moveToNext()) {
            int productId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_DETAIL_PRODUCT_ID));

            ProductDAO productDAO = new ProductDAO(this.context);
            productDAO.open();
            Product product = productDAO.getProductById(productId);
            productDAO.close();

            PurchaseOrderDetail detail = new PurchaseOrderDetail(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_DETAIL_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_DETAIL_QUANTITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_DETAIL_PRICE)),
                    product,
                    order
            );
            orderDetails.add(detail);
        }
        cursor.close();
        this.close();
        return orderDetails;
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
