package com.example.ecommerce.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ecommerce.db";
    private static final int DATABASE_VERSION = 2;

    //Table name
    public static final String TABLE_USER = "User";
    public static final String TABLE_STORE = "Store";
    public static final String TABLE_PRODUCT = "Product";
    public static final String TABLE_CATEGORY = "Category";
    public static final String TABLE_ORDER = "PurchaseOrder";
    public static final String TABLE_ORDER_DETAIL = "PurchaseOrderDetail";
    public static final String TABLE_REVIEW = "Review";
    public static final String TABLE_CART = "Cart";
    public static final String TABLE_FAVORITE = "Favorite";
    public static final String TABLE_INVOICE = "Invoice";

    // User Table Columns
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_USERNAME = "username";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_FULLNAME = "fullname";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_ROLE = "role";
    public static final String COLUMN_USER_AVATAR = "avatar";

    // Store Table Columns
    public static final String COLUMN_STORE_ID = "id";
    public static final String COLUMN_STORE_NAME = "name";
    public static final String COLUMN_STORE_ADDRESS = "address";
    public static final String COLUMN_STORE_IMAGE = "image";
    public static final String COLUMN_STORE_OWNER_ID = "ownerId";

    // Category Table Columns
    public static final String COLUMN_CATEGORY_ID = "id";
    public static final String COLUMN_CATEGORY_NAME = "name";

    // Product Table Columns
    public static final String COLUMN_PRODUCT_ID = "id";
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_IMAGE = "image";
    public static final String COLUMN_PRODUCT_DESCRIPTION = "description";
    public static final String COLUMN_PRODUCT_PRICE = "price";
    public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
    public static final String COLUMN_PRODUCT_CATEGORY_ID = "cateId";
    public static final String COLUMN_PRODUCT_STORE_ID = "storeId";

    // Order Table Columns
    public static final String COLUMN_ORDER_ID = "id";
    public static final String COLUMN_ORDER_STATUS = "status";
    public static final String COLUMN_ORDER_TOTAL_PRICE = "totalPrice";
    public static final String COLUMN_ORDER_BUYER_ID = "buyerId";
    public static final String COLUMN_ORDER_STORE_ID = "storeId";

    // Order Detail Table Columns
    public static final String COLUMN_ORDER_DETAIL_ID = "id";
    public static final String COLUMN_ORDER_DETAIL_ORDER_ID = "orderId";
    public static final String COLUMN_ORDER_DETAIL_PRODUCT_ID = "productId";
    public static final String COLUMN_ORDER_DETAIL_QUANTITY = "quantity";
    public static final String COLUMN_ORDER_DETAIL_PRICE = "price";

    // Review Table Columns
    public static final String COLUMN_REVIEW_ID = "id";
    public static final String COLUMN_REVIEW_ORDER_ID = "orderId";
    public static final String COLUMN_REVIEW_USER_ID = "userId";
    public static final String COLUMN_REVIEW_RATING = "rating";
    public static final String COLUMN_REVIEW_CONTENT = "content";
    public static final String COLUMN_REVIEW_PARENT_ID = "parentId";

    // Cart Table Columns
    public static final String COLUMN_CART_ID = "id";
    public static final String COLUMN_CART_BUYER_ID = "buyerId";
    public static final String COLUMN_CART_PRODUCT_ID = "productId";
    public static final String COLUMN_CART_QUANTITY = "quantity";
    public static final String COLUMN_CART_SELECTED = "selected";

    // Invoice Table Columns
    public static final String COLUMN_INVOICE_ID = "id";
    public static final String COLUMN_INVOICE_ORDER_ID = "orderId";
    public static final String COLUMN_INVOICE_CREATED_DATE = "createdDate";
    public static final String COLUMN_INVOICE_PAYMENT_METHOD = "paymentMethod";

    // Favorite Table Columns
    public static final String COLUMN_FAV_ID = "id";
    public static final String COLUMN_FAV_BUYER_ID = "buyerId";
    public static final String COLUMN_FAV_PRODUCT_ID = "productId";

    // Create User Table
    private static final String CREATE_USER_TABLE =
            "CREATE TABLE " + TABLE_USER + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_USERNAME + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_USER_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_USER_FULLNAME + " TEXT, " +
                    COLUMN_USER_EMAIL + " TEXT, " +
                    COLUMN_USER_AVATAR + " TEXT, " +
                    COLUMN_USER_ROLE + " TEXT);";

    // Create Store Table
    private static final String CREATE_STORE_TABLE =
            "CREATE TABLE " + TABLE_STORE + " (" +
                    COLUMN_STORE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_STORE_NAME + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_STORE_ADDRESS + " TEXT NOT NULL, " +
                    COLUMN_STORE_IMAGE + " TEXT, " +
                    COLUMN_STORE_OWNER_ID + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_STORE_OWNER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "));";

    // Create Category Table
    private static final String CREATE_CATEGORY_TABLE =
            "CREATE TABLE " + TABLE_CATEGORY + " (" +
                    COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CATEGORY_NAME + " TEXT NOT NULL UNIQUE);";

    // Create Product Table
    private static final String CREATE_PRODUCT_TABLE =
            "CREATE TABLE " + TABLE_PRODUCT + " (" +
                    COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                    COLUMN_PRODUCT_IMAGE + " TEXT, " +
                    COLUMN_PRODUCT_DESCRIPTION + " TEXT, " +
                    COLUMN_PRODUCT_PRICE + " TEXT, " +
                    COLUMN_PRODUCT_QUANTITY + " INTEGER, " +
                    COLUMN_PRODUCT_CATEGORY_ID + " INTEGER, " +
                    COLUMN_PRODUCT_STORE_ID + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_PRODUCT_STORE_ID + ") REFERENCES " + TABLE_STORE + "(" + COLUMN_STORE_ID + ") ON DELETE CASCADE, " +
                    "FOREIGN KEY(" + COLUMN_PRODUCT_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + "));";

    // Create Order Table
    private static final String CREATE_ORDER_TABLE =
            "CREATE TABLE " + TABLE_ORDER + " (" +
                    COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ORDER_BUYER_ID + " INTEGER, " +
                    COLUMN_ORDER_STORE_ID + " INTEGER, " +
                    COLUMN_ORDER_STATUS + " TEXT, " +
                    COLUMN_ORDER_TOTAL_PRICE + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_ORDER_BUYER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "), " +
                    "FOREIGN KEY(" + COLUMN_ORDER_STORE_ID + ") REFERENCES " + TABLE_STORE + "(" + COLUMN_STORE_ID + "));";

    //Create Order Detail Table
    private static final String CREATE_ORDER_DETAIL_TABLE =
            "CREATE TABLE " + TABLE_ORDER_DETAIL + " (" +
                    COLUMN_ORDER_DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ORDER_DETAIL_ORDER_ID + " INTEGER, " +
                    COLUMN_ORDER_DETAIL_PRODUCT_ID + " INTEGER, " +
                    COLUMN_ORDER_DETAIL_QUANTITY + " INTEGER NOT NULL, " +
                    COLUMN_ORDER_DETAIL_PRICE + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_ORDER_DETAIL_ORDER_ID + ") REFERENCES " + TABLE_ORDER + "(" + COLUMN_ORDER_ID + ") ON DELETE CASCADE, " +
                    "FOREIGN KEY(" + COLUMN_ORDER_DETAIL_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCT + "(" + COLUMN_PRODUCT_ID + ") ON DELETE CASCADE );";

    //Create Review Table
    private static final String CREATE_REVIEW_TABLE =
            "CREATE TABLE " + TABLE_REVIEW + " (" +
                    COLUMN_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_REVIEW_USER_ID + " INTEGER, " +
                    COLUMN_REVIEW_ORDER_ID + " INTEGER, " +
                    COLUMN_REVIEW_PARENT_ID + " INTEGER, " +
                    COLUMN_REVIEW_RATING + " INTEGER, " +
                    COLUMN_REVIEW_CONTENT + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_REVIEW_PARENT_ID + ") REFERENCES " + TABLE_REVIEW + "(" + COLUMN_REVIEW_ID + "), " +
                    "FOREIGN KEY(" + COLUMN_REVIEW_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + ") ON DELETE CASCADE, " +
                    "FOREIGN KEY(" + COLUMN_REVIEW_ORDER_ID + ") REFERENCES " + TABLE_ORDER + "(" + COLUMN_ORDER_ID + ") ON DELETE CASCADE);";

    // Create Cart Table
    private static final String CREATE_CART_TABLE =
            "CREATE TABLE " + TABLE_CART + " (" +
                    COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CART_BUYER_ID + " INTEGER, " +
                    COLUMN_CART_PRODUCT_ID + " INTEGER, " +
                    COLUMN_CART_QUANTITY + " INTEGER, " +
                    COLUMN_CART_SELECTED + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_CART_BUYER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + ") ON DELETE CASCADE, " +
                    "FOREIGN KEY(" + COLUMN_CART_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCT+ "(" + COLUMN_PRODUCT_ID + ") ON DELETE CASCADE, " +
                    "UNIQUE(" + COLUMN_CART_BUYER_ID + ", " + COLUMN_CART_PRODUCT_ID + "));";

    // Create Invoice Table
    private static final String CREATE_INVOICE_TABLE =
            "CREATE TABLE " + TABLE_INVOICE + " (" +
                    COLUMN_INVOICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_INVOICE_ORDER_ID + " INTEGER, " +
                    COLUMN_INVOICE_CREATED_DATE + " TEXT, " +
                    COLUMN_INVOICE_PAYMENT_METHOD + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_INVOICE_ORDER_ID + ") REFERENCES " + TABLE_ORDER + "(" + COLUMN_ORDER_ID + ") ON DELETE CASCADE);";

    // Create Favorite Table
    private static final String CREATE_FAV_TABLE =
            "CREATE TABLE " + TABLE_FAVORITE + " (" +
                    COLUMN_FAV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FAV_BUYER_ID + " INTEGER, " +
                    COLUMN_FAV_PRODUCT_ID + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_FAV_BUYER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + ") ON DELETE CASCADE, " +
                    "FOREIGN KEY(" + COLUMN_FAV_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCT + "(" + COLUMN_PRODUCT_ID + ") ON DELETE CASCADE, " +
                    "UNIQUE(" + COLUMN_FAV_BUYER_ID + ", " + COLUMN_FAV_PRODUCT_ID + " ));";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_STORE_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);
        db.execSQL(CREATE_ORDER_DETAIL_TABLE);
        db.execSQL(CREATE_REVIEW_TABLE);
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_INVOICE_TABLE);
        db.execSQL(CREATE_FAV_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);
        onCreate(db);
    }
}
