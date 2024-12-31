package com.example.ecommerce.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ecommerce.database.DatabaseHelper;
import com.example.ecommerce.entity.Invoice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InvoiceDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context context;

    public InvoiceDAO(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public Invoice getInvoiceOfOrder(int orderId) {
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_INVOICE + " WHERE " +
                DatabaseHelper.COLUMN_INVOICE_ORDER_ID + " = ?";
        open();
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(orderId)});
        OrderDAO orderDAO = new OrderDAO(this.context);
        orderDAO.open();
        Invoice invoice = null;
        if (cursor.moveToFirst()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date createdDate = null;
            try {
                String createdDateString = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INVOICE_CREATED_DATE));
                createdDate = dateFormat.parse(createdDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            invoice = new Invoice(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INVOICE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INVOICE_PAYMENT_METHOD)),
                    createdDate,
                    orderDAO.getOrderById(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INVOICE_ORDER_ID)))
            );
        }
        orderDAO.close();
        cursor.close();
        close();
        return invoice;
    }
    public boolean insertInvoice(Invoice invoice) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_INVOICE_ORDER_ID, invoice.getPurchaseOrder().getId());
        values.put(DatabaseHelper.COLUMN_INVOICE_CREATED_DATE, invoice.getCreatedDate().toString());
        values.put(DatabaseHelper.COLUMN_INVOICE_PAYMENT_METHOD, invoice.getPayMethod());
        long result = database.insert(DatabaseHelper.TABLE_INVOICE, null, values);
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
