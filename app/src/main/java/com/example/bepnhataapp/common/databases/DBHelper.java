package com.example.bepnhataapp.common.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 2; // bumped to match latest asset DB
    private static final String DB_NAME = "BepNhaTa.db";
    private static final String DB_PATH_SUFFIX = "/databases/";
    // Alias for teacher-style constant name
    public static final String DATABASE_NAME = DB_NAME;
    // Holder for opened instance (optional, matches slide)
    public static SQLiteDatabase database = null;

    public static final String TBL_CUSTOMERS = "CUSTOMERS";
    public static final String TBL_MEAL_PLANS = "MEAL_PLANS";
    public static final String TBL_MEAL_DAYS = "MEAL_DAYS";
    public static final String TBL_MEAL_TIMES = "MEAL_TIMES";
    public static final String TBL_MEAL_RECIPES = "MEAL_RECIPES";
    public static final String TBL_CUSTOMER_HEALTH = "CUSTOMER_HEALTH";
    public static final String TBL_RECIPES = "RECIPES";
    public static final String TBL_RECIPE_DETAILS = "RECIPE_DETAIL";
    public static final String TBL_INSTRUCTION_RECIPES = "INSTRUCTION_RECIPES";
    public static final String TBL_RECIPE_INGREDIENTS = "RECIPE_INGREDIENTS";
    public static final String TBL_RECIPE_DOWNLOAD = "RECIPE_DOWNLOAD";
    public static final String TBL_FAVOURITE_RECIPES = "FAVOURITE_RECIPES";
    public static final String TBL_CARTS = "CARTS";
    public static final String TBL_CART_DETAILS = "CART_DETAILS";
    public static final String TBL_ADDRESSES = "ADDRESSES";
    public static final String TBL_ORDERS = "ORDERS";
    public static final String TBL_ORDER_LINES = "ORDER_LINES";
    public static final String TBL_PRODUCT_FEEDBACK = "PRODUCT_FEEDBACK";

    public static final String TBL_BLOGS = "BLOGS";
    public static final String TBL_FAVOURITE_BLOGS = "FAVOURITE_BLOGS";
    public static final String TBL_BLOG_COMMENTS = "BLOG_COMMENTS";
    public static final String TBL_RECIPE_COMMENTS = "RECIPE_COMMENTS";
    public static final String TBL_COUPONS = "COUPONS";
    public static final String TBL_PRODUCTS = "PRODUCTS";
    public static final String TBL_PRODUCT_DETAILS = "PRODUCT_DETAILS";
    public static final String TBL_INGREDIENTS = "INGREDIENTS";
    public static final String TBL_PRODUCT_INGREDIENTS = "PRODUCT_INGREDIENTS";
    public static final String TBL_FAVOURITE_PRODUCTS = "FAVOURITE_PRODUCTS";
    public static final String TBL_FOOD_CALO = "FOOD_CALO";

    private static final String SQL_CREATE_CUSTOMER =
            "CREATE TABLE IF NOT EXISTS " + TBL_CUSTOMERS + " (" +
                    "customerID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "fullName TEXT," +
                    "gender TEXT," +
                    "birthday TEXT," +
                    "email TEXT," +
                    "password TEXT," +
                    "phone TEXT," +
                    "avatar TEXT," +
                    "customerType TEXT," +
                    "loyaltyPoint INTEGER," +
                    "createdAt TEXT," +
                    "status TEXT" +
                    ")";

    private static final String SQL_CREATE_MEAL_PLAN =
            "CREATE TABLE IF NOT EXISTS " + TBL_MEAL_PLANS + " (" +
                    "mealPlanID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "customerID INTEGER," +
                    "mealCategory TEXT," +
                    "title TEXT," +
                    "createdAt TEXT," +
                    "imageThumb TEXT," +
                    "totalDays INTEGER," +
                    "avgCalories REAL," +
                    "avgCarbs REAL," +
                    "avgProtein REAL," +
                    "avgFat REAL," +
                    "startDate TEXT," +
                    "endDate TEXT," +
                    "note TEXT," +
                    "type TEXT" +
                    ")";

    private static final String SQL_CREATE_MEAL_DAY =
            "CREATE TABLE IF NOT EXISTS " + TBL_MEAL_DAYS + " (" +
                    "mealDayID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "mealPlanID INTEGER," +
                    "date TEXT," +
                    "note TEXT," +
                    "FOREIGN KEY(mealPlanID) REFERENCES " + TBL_MEAL_PLANS + "(mealPlanID)" +
                    ")";

    private static final String SQL_CREATE_MEAL_TIME =
            "CREATE TABLE IF NOT EXISTS " + TBL_MEAL_TIMES + " (" +
                    "mealTimeID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "mealDayID INTEGER," +
                    "mealType TEXT," +
                    "note TEXT," +
                    "FOREIGN KEY(mealDayID) REFERENCES " + TBL_MEAL_DAYS + "(mealDayID)" +
                    ")";

    private static final String SQL_CREATE_MEAL_RECIPE =
            "CREATE TABLE IF NOT EXISTS " + TBL_MEAL_RECIPES + " (" +
                    "mealTimeID INTEGER," +
                    "recipeID INTEGER," +
                    "PRIMARY KEY(mealTimeID, recipeID)," +
                    "FOREIGN KEY(mealTimeID) REFERENCES " + TBL_MEAL_TIMES + "(mealTimeID)" +
                    ")";

    private static final String SQL_CREATE_CUSTOMER_HEALTH =
            "CREATE TABLE IF NOT EXISTS " + TBL_CUSTOMER_HEALTH + " (" +
                    "customerHealthID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "customerID INTEGER," +
                    "gender TEXT," +
                    "age INTEGER," +
                    "weight REAL," +
                    "height REAL," +
                    "bodyType TEXT," +
                    "allergy TEXT," +
                    "commonGoal TEXT," +
                    "targetWeight REAL," +
                    "weightChangeRate REAL," +
                    "physicalActivityLevel TEXT," +
                    "FOREIGN KEY(customerID) REFERENCES " + TBL_CUSTOMERS + "(customerID)" +
                    ")";

    private static final String SQL_CREATE_RECIPE =
            "CREATE TABLE IF NOT EXISTS " + TBL_RECIPES + " (" +
                    "recipeID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "recipeName TEXT," +
                    "description TEXT," +
                    "tag TEXT," +
                    "createdAt TEXT," +
                    "imageThumb TEXT," +
                    "category TEXT," +
                    "commentAmount INTEGER," +
                    "likeAmount INTEGER," +
                    "sectionAmount INTEGER" +
                    ")";

    private static final String SQL_CREATE_RECIPE_DETAIL =
            "CREATE TABLE IF NOT EXISTS " + TBL_RECIPE_DETAILS + " (" +
                    "recipeID INTEGER PRIMARY KEY," +
                    "calo REAL," +
                    "protein REAL," +
                    "carbs REAL," +
                    "fat REAL," +
                    "foodTag TEXT," +
                    "cuisine TEXT," +
                    "cookingTimeMinutes INTEGER," +
                    "flavor TEXT," +
                    "benefit TEXT," +
                    "FOREIGN KEY(recipeID) REFERENCES " + TBL_RECIPES + "(recipeID)" +
                    ")";

    private static final String SQL_CREATE_INSTRUCTION_RECIPE =
            "CREATE TABLE IF NOT EXISTS " + TBL_INSTRUCTION_RECIPES + " (" +
                    "instructionID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "recipeID INTEGER," +
                    "numberSection INTEGER," +
                    "title TEXT," +
                    "content TEXT," +
                    "image TEXT," +
                    "FOREIGN KEY(recipeID) REFERENCES " + TBL_RECIPES + "(recipeID)" +
                    ")";

    private static final String SQL_CREATE_RECIPE_INGREDIENT =
            "CREATE TABLE IF NOT EXISTS " + TBL_RECIPE_INGREDIENTS + " (" +
                    "ingredientID INTEGER," +
                    "recipeID INTEGER," +
                    "quantity REAL," +
                    "nameIngredient TEXT," +
                    "PRIMARY KEY(ingredientID, recipeID)" +
                    ")";

    private static final String SQL_CREATE_RECIPE_DOWNLOAD =
            "CREATE TABLE IF NOT EXISTS " + TBL_RECIPE_DOWNLOAD + " (" +
                    "customerID INTEGER," +
                    "recipeID INTEGER," +
                    "downloadedAt TEXT," +
                    "PRIMARY KEY(customerID, recipeID)," +
                    "FOREIGN KEY(customerID) REFERENCES " + TBL_CUSTOMERS + "(customerID)," +
                    "FOREIGN KEY(recipeID) REFERENCES " + TBL_RECIPES + "(recipeID)" +
                    ")";

    private static final String SQL_CREATE_FAVOURITE_RECIPE =
            "CREATE TABLE IF NOT EXISTS " + TBL_FAVOURITE_RECIPES + " (" +
                    "recipeID INTEGER," +
                    "customerID INTEGER," +
                    "createdAt TEXT," +
                    "PRIMARY KEY(recipeID, customerID)," +
                    "FOREIGN KEY(recipeID) REFERENCES " + TBL_RECIPES + "(recipeID)," +
                    "FOREIGN KEY(customerID) REFERENCES " + TBL_CUSTOMERS + "(customerID)" +
                    ")";

    private static final String SQL_CREATE_CART =
            "CREATE TABLE IF NOT EXISTS " + TBL_CARTS + " (" +
                    "cartID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "customerID INTEGER," +
                    "createdAt TEXT," +
                    "FOREIGN KEY(customerID) REFERENCES " + TBL_CUSTOMERS + "(customerID)" +
                    ")";

    private static final String SQL_CREATE_CART_DETAIL =
            "CREATE TABLE IF NOT EXISTS " + TBL_CART_DETAILS + " (" +
                    "cartID INTEGER," +
                    "productID INTEGER," +
                    "quantity INTEGER," +
                    "PRIMARY KEY(cartID, productID)," +
                    "FOREIGN KEY(cartID) REFERENCES " + TBL_CARTS + "(cartID)" +
                    ")";

    private static final String SQL_CREATE_ADDRESS =
            "CREATE TABLE IF NOT EXISTS " + TBL_ADDRESSES + " (" +
                    "addressID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "customerID INTEGER," +
                    "receiverName TEXT," +
                    "phone TEXT," +
                    "addressLine TEXT," +
                    "district TEXT," +
                    "province TEXT," +
                    "isDefault INTEGER," +
                    "note TEXT," +
                    "FOREIGN KEY(customerID) REFERENCES " + TBL_CUSTOMERS + "(customerID)" +
                    ")";

    private static final String SQL_CREATE_ORDER =
            "CREATE TABLE IF NOT EXISTS " + TBL_ORDERS + " (" +
                    "orderID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "cartID INTEGER," +
                    "addressID INTEGER," +
                    "couponID INTEGER," +
                    "orderDate TEXT," +
                    "totalPrice REAL," +
                    "status TEXT," +
                    "paymentMethod TEXT," +
                    "note TEXT," +
                    "FOREIGN KEY(cartID) REFERENCES " + TBL_CARTS + "(cartID)," +
                    "FOREIGN KEY(addressID) REFERENCES " + TBL_ADDRESSES + "(addressID)" +
                    ")";

    private static final String SQL_CREATE_ORDER_LINE =
            "CREATE TABLE IF NOT EXISTS " + TBL_ORDER_LINES + " (" +
                    "orderLineID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "orderID INTEGER," +
                    "productID INTEGER," +
                    "quantity INTEGER," +
                    "totalPrice REAL," +
                    "FOREIGN KEY(orderID) REFERENCES " + TBL_ORDERS + "(orderID)" +
                    ")";

    private static final String SQL_CREATE_PRODUCT_FEEDBACK =
            "CREATE TABLE IF NOT EXISTS " + TBL_PRODUCT_FEEDBACK + " (" +
                    "proFeedbackID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "orderLineID INTEGER," +
                    "content TEXT," +
                    "image TEXT," +
                    "rating INTEGER," +
                    "createdAt TEXT," +
                    "FOREIGN KEY(orderLineID) REFERENCES " + TBL_ORDER_LINES + "(orderLineID)" +
                    ")";

    private static final String SQL_CREATE_BLOG =
            "CREATE TABLE IF NOT EXISTS " + TBL_BLOGS + " (" +
                    "blogID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT," +
                    "content TEXT," +
                    "authorName TEXT," +
                    "createdAt TEXT," +
                    "imageThumb TEXT," +
                    "status TEXT," +
                    "tag TEXT" +
                    ")";

    private static final String SQL_CREATE_FAVOURITE_BLOG =
            "CREATE TABLE IF NOT EXISTS " + TBL_FAVOURITE_BLOGS + " (" +
                    "blogID INTEGER," +
                    "customerID INTEGER," +
                    "createdAt TEXT," +
                    "PRIMARY KEY(blogID, customerID)," +
                    "FOREIGN KEY(blogID) REFERENCES " + TBL_BLOGS + "(blogID)," +
                    "FOREIGN KEY(customerID) REFERENCES " + TBL_CUSTOMERS + "(customerID)" +
                    ")";

    private static final String SQL_CREATE_BLOG_COMMENT =
            "CREATE TABLE IF NOT EXISTS " + TBL_BLOG_COMMENTS + " (" +
                    "blogCommentID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "blogID INTEGER," +
                    "customerID INTEGER," +
                    "content TEXT," +
                    "createdAt TEXT," +
                    "parentCommentID INTEGER," +
                    "usefulness INTEGER," +
                    "FOREIGN KEY(blogID) REFERENCES " + TBL_BLOGS + "(blogID)," +
                    "FOREIGN KEY(customerID) REFERENCES " + TBL_CUSTOMERS + "(customerID)" +
                    ")";

    private static final String SQL_CREATE_RECIPE_COMMENT =
            "CREATE TABLE IF NOT EXISTS " + TBL_RECIPE_COMMENTS + " (" +
                    "recipeCommentID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "recipeID INTEGER," +
                    "customerID INTEGER," +
                    "content TEXT," +
                    "createdAt TEXT," +
                    "parentCommentID INTEGER," +
                    "usefulness INTEGER," +
                    "FOREIGN KEY(recipeID) REFERENCES " + TBL_RECIPES + "(recipeID)," +
                    "FOREIGN KEY(customerID) REFERENCES " + TBL_CUSTOMERS + "(customerID)" +
                    ")";

    private static final String SQL_CREATE_COUPON =
            "CREATE TABLE IF NOT EXISTS " + TBL_COUPONS + " (" +
                    "couponID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "customerID INTEGER," +
                    "couponTitle TEXT," +
                    "minPrice INTEGER," +
                    "validDate TEXT," +
                    "expireDate TEXT," +
                    "maxDiscount INTEGER," +
                    "couponValue INTEGER," +
                    "isGeneral INTEGER," +
                    "exchangePoints INTEGER," +
                    "FOREIGN KEY(customerID) REFERENCES " + TBL_CUSTOMERS + "(customerID)" +
                    ")";

    private static final String SQL_CREATE_PRODUCT =
            "CREATE TABLE IF NOT EXISTS " + TBL_PRODUCTS + " (" +
                    "productID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "productName TEXT," +
                    "productDescription TEXT," +
                    "productPrice INTEGER," +
                    "salePercent INTEGER," +
                    "productThumb TEXT," +
                    "commentAmount INTEGER," +
                    "category TEXT," +
                    "inventory INTEGER," +
                    "soldQuantity INTEGER," +
                    "avgRating REAL," +
                    "status TEXT," +
                    "createdDate TEXT," +
                    "updatedDate TEXT" +
                    ")";

    private static final String SQL_CREATE_PRODUCT_DETAIL =
            "CREATE TABLE IF NOT EXISTS " + TBL_PRODUCT_DETAILS + " (" +
                    "productID INTEGER PRIMARY KEY," +
                    "recipeID INTEGER," +
                    "calo REAL," +
                    "protein REAL," +
                    "carbs REAL," +
                    "fat REAL," +
                    "foodTag TEXT," +
                    "cuisine TEXT," +
                    "cookingTimeMinutes INTEGER," +
                    "storageGuide TEXT," +
                    "expiry TEXT," +
                    "note TEXT," +
                    "FOREIGN KEY(productID) REFERENCES " + TBL_PRODUCTS + "(productID)," +
                    "FOREIGN KEY(recipeID) REFERENCES " + TBL_RECIPES + "(recipeID)" +
                    ")";

    private static final String SQL_CREATE_INGREDIENT =
            "CREATE TABLE IF NOT EXISTS " + TBL_INGREDIENTS + " (" +
                    "ingredientID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ingredientName TEXT," +
                    "unit TEXT," +
                    "image TEXT" +
                    ")";

    private static final String SQL_CREATE_PRODUCT_INGREDIENT =
            "CREATE TABLE IF NOT EXISTS " + TBL_PRODUCT_INGREDIENTS + " (" +
                    "productID INTEGER," +
                    "ingredientID INTEGER," +
                    "quantity INTEGER," +
                    "PRIMARY KEY(productID, ingredientID)," +
                    "FOREIGN KEY(productID) REFERENCES " + TBL_PRODUCTS + "(productID)," +
                    "FOREIGN KEY(ingredientID) REFERENCES " + TBL_INGREDIENTS + "(ingredientID)" +
                    ")";

    private static final String SQL_CREATE_FAVOURITE_PRODUCT =
            "CREATE TABLE IF NOT EXISTS " + TBL_FAVOURITE_PRODUCTS + " (" +
                    "productID INTEGER," +
                    "customerID INTEGER," +
                    "createdAt TEXT," +
                    "PRIMARY KEY(productID, customerID)," +
                    "FOREIGN KEY(productID) REFERENCES " + TBL_PRODUCTS + "(productID)," +
                    "FOREIGN KEY(customerID) REFERENCES " + TBL_CUSTOMERS + "(customerID)" +
                    ")";

    private static final String SQL_CREATE_FOOD_CALO =
            "CREATE TABLE IF NOT EXISTS " + TBL_FOOD_CALO + " (" +
                    "foodCaloID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "foodName TEXT," +
                    "foodThumb TEXT," +
                    "caloPerOneHundredGrams INTEGER" +
                    ")";

    private final Context ctx;
    private static final String TAG = "DBHelper";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        ctx = context.getApplicationContext();

        // Copy the bundled database (once) using the teacher-style method.
        copyDataBaseO();
    }

    /**
     * Teacher-style wrapper that copies the DB from assets to the app's internal
     * storage the first time the app runs.
     */
    private void copyDataBaseO() {
        try {
            java.io.File dbFile = ctx.getDatabasePath(DATABASE_NAME);
            if (!dbFile.exists()) {
                if (CopyDBFromAsset()) {
                    Toast.makeText(ctx, "Copy database successful!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ctx, "Copy database fail!", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }

    /**
     * Actual copy implementation – closely follows the snippet from the slide.
     * @return true if the copy succeeded.
     */
    private boolean CopyDBFromAsset() {
        String dbPath = ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
        try (java.io.InputStream inputStream = ctx.getAssets().open(DATABASE_NAME)) {
            // Ensure parent folder exists
            java.io.File dbDir = new java.io.File(ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!dbDir.exists()) {
                dbDir.mkdir();
            }

            try (java.io.OutputStream outputStream = new java.io.FileOutputStream(dbPath)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
            }
            return true;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Add context getter for DAOs
    public Context getContext() {
        return ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Schema is already included in the pre-populated database copied from assets.
        // No table-creation statements are necessary here.
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // No migration logic for now because we rely on the pre-populated DB.
        // Bump DB_VERSION and implement migrations when you need to change schema.
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Prevent crash when DB version is higher than expected (e.g., user keeps old database).
        // Simply keep existing schema; you could also implement copy-from-asset again.
        // Here we just log and ignore.
        Log.w(TAG, "Downgrading database from version " + oldVersion + " to " + newVersion + ". No changes applied.");
    }
} 