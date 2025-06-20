package com.example.bepnhataapp.common.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.bepnhataapp.R;

import java.io.ByteArrayOutputStream;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "BepNhaTa.db";

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

    private static final String SQL_CREATE_CUSTOMER =
            "CREATE TABLE IF NOT EXISTS " + TBL_CUSTOMERS + " (" +
                    "customerID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "fullName TEXT," +
                    "gender TEXT," +
                    "birthday TEXT," +
                    "email TEXT," +
                    "password TEXT," +
                    "phone TEXT," +
                    "avatar BLOB," +
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
                    "imageThumb BLOB," +
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
                    "imageThumb BLOB," +
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

    private final Context ctx;
    private static final String TAG = "DBHelper";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        ctx = context.getApplicationContext();
    }

    // Add context getter for DAOs
    public Context getContext() {
        return ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create schema first
        db.execSQL(SQL_CREATE_CUSTOMER);
        db.execSQL(SQL_CREATE_MEAL_PLAN);
        db.execSQL(SQL_CREATE_MEAL_DAY);
        db.execSQL(SQL_CREATE_MEAL_TIME);
        db.execSQL(SQL_CREATE_MEAL_RECIPE);
        db.execSQL(SQL_CREATE_CUSTOMER_HEALTH);
        db.execSQL(SQL_CREATE_RECIPE);
        db.execSQL(SQL_CREATE_RECIPE_DETAIL);
        db.execSQL(SQL_CREATE_INSTRUCTION_RECIPE);
        db.execSQL(SQL_CREATE_RECIPE_INGREDIENT);
        db.execSQL(SQL_CREATE_RECIPE_DOWNLOAD);
        db.execSQL(SQL_CREATE_FAVOURITE_RECIPE);
        db.execSQL(SQL_CREATE_CART);
        db.execSQL(SQL_CREATE_CART_DETAIL);
        db.execSQL(SQL_CREATE_ADDRESS);
        db.execSQL(SQL_CREATE_ORDER);
        db.execSQL(SQL_CREATE_ORDER_LINE);
        db.execSQL(SQL_CREATE_PRODUCT_FEEDBACK);
        db.execSQL(SQL_CREATE_BLOG);
        db.execSQL(SQL_CREATE_FAVOURITE_BLOG);
        db.execSQL(SQL_CREATE_BLOG_COMMENT);
        db.execSQL(SQL_CREATE_RECIPE_COMMENT);
        db.execSQL(SQL_CREATE_COUPON);

        // Seed initial data but do NOT allow any runtime-exception here to abort DB creation
        try {
            seedCustomers(db);
            seedMealPlans(db);
            seedMealDays(db);
            seedMealTimes(db);
            seedMealRecipes(db);
            seedCustomerHealth(db);
            seedRecipes(db);
            seedRecipeDetails(db);
            seedInstructionRecipes(db);
            seedRecipeIngredients(db);
            seedRecipeDownloads(db);
            seedFavouriteRecipes(db);
            seedCarts(db);
            seedCartDetails(db);
            seedAddresses(db);
            seedOrders(db);
            seedOrderLines(db);
            seedProductFeedback(db);
            seedBlogs(db);
            seedFavouriteBlogs(db);
            seedBlogComments(db);
            seedRecipeComments(db);
            seedCoupons(db);
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error seeding initial customers. Database schema is still created.", e);
            // Optionally you could decide to delete all partially inserted rows here.
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_CUSTOMERS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_MEAL_PLANS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_MEAL_DAYS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_MEAL_TIMES);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_MEAL_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_CUSTOMER_HEALTH);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_RECIPE_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_INSTRUCTION_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_RECIPE_DOWNLOAD);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_FAVOURITE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_CART_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_CARTS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_ORDER_LINES);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_ADDRESSES);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_PRODUCT_FEEDBACK);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_BLOGS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_FAVOURITE_BLOGS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_BLOG_COMMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_RECIPE_COMMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_COUPONS);
        onCreate(db);
    }

    /* =========================================================
     *  TABLE: CUSTOMERS
     *  Schema & seed data section
     *  --------------------------------------------------------- */
    private void seedCustomers(SQLiteDatabase db) {
        Object[][] rows = {
                {"Trần Bình",       "Nam","1995-08-22","tranbinh@example.com","pass456","0905899988", R.drawable.avarta_1, "Bạc",      800,  "2023-02-15","active"},
                {"Lê Hoàng Cường",  "Nam","1988-03-10","lehoangcuong@example.com","secure789","0916234567", R.drawable.avarta_2,"Kim cương",3000,"2022-11-20","active"},
                {"Phạm Thị Duyên",  "Nữ","1992-12-01","phamthiduyen@example.com","duyen2023","0937345678", R.drawable.avarta_3,"Bạc",      500,  "2023-03-05","inactive"},
                {"Hoàng Minh Đức",  "Nam","1997-07-07","hoangminhduc@example.com","minhduc99","0948456789", R.drawable.avarta_4,"Vàng",     1200,"2023-04-12","active"},
                {"Vũ Hà",           "Nam","1993-09-25","vuha@example.com","ha123456","0929567890", R.drawable.avarta_5,"Kim cương",2500,"2022-12-01","active"},
                {"Đặng Văn Hùng",   "Nam","1991-04-18","dangvanhung@example.com","hungpass","0960678901", R.drawable.avarta_6,"Bạc",       300,  "2023-05-20","inactive"},
                {"Bùi Văn Cường",   "Nam","1996-06-30","buivancuong@example.com","cuong456789","0979988776", R.drawable.avarta_7,"Vàng", 1800,"2023-06-15","active"}
        };

        db.beginTransaction();
        try {
            for (Object[] r : rows) {
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put("fullName",      (String) r[0]);
                cv.put("gender",        (String) r[1]);
                cv.put("birthday",      (String) r[2]);
                cv.put("email",         (String) r[3]);
                cv.put("password",      (String) r[4]);
                cv.put("phone",         (String) r[5]);

                int resId = (Integer) r[6];
                Bitmap bmp = BitmapFactory.decodeResource(ctx.getResources(), resId);
                if (bmp != null) {
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 90, os);
                    cv.put("avatar", os.toByteArray());
                } else {
                    android.util.Log.w(TAG, "Cannot decode avatar resource id=" + resId + ". Skipping avatar blob.");
                }

                cv.put("customerType",  (String) r[7]);
                cv.put("loyaltyPoint",  (Integer) r[8]);
                cv.put("createdAt",     (String) r[9]);
                cv.put("status",        (String) r[10]);

                db.insert(TBL_CUSTOMERS, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /* =========================================================
     *  TABLE: MEAL_PLANS
     *  Schema & seed data section
     *  --------------------------------------------------------- */
    private void seedMealPlans(SQLiteDatabase db) {
        // Demo data inspired by the spec above – numeric averages left 0 for brevity
        Object[][] rows = {
                {null, "Bữa tối nhanh chóng cho người bận rộn", "Thực đơn tối nhanh 1", "2025-06-01", null, 7, 0.0, 0.0, 0.0, 0.0, "2025-06-02", "2025-06-08", "Chứa súp và cơm gạo lứt", "System"},
                {null, "Bữa tối nhanh chóng cho người bận rộn", "Thực đơn tối nhanh 2", "2025-06-11", null, 5, 0.0, 0.0, 0.0, 0.0, "2025-06-12", "2025-06-16", "Thêm gà và rau củ", "System"},
                {null, "Bữa tối nhanh chóng cho người bận rộn", "Thực đơn tối nhanh 3", "2025-06-21", null, 6, 0.0, 0.0, 0.0, 0.0, "2025-06-22", "2025-06-27", "Nhiều rau và cà chua", "System"},
                {null, "Bữa tối nhanh chóng cho người bận rộn", "Thực đơn tối nhanh 4", "2025-06-25", null, 4, 0.0, 0.0, 0.0, 0.0, "2025-06-26", "2025-07-01", "Gồm súp rau và thịt gà", "System"},
                {null, "Bữa tối nhanh chóng cho người bận rộn", "Thực đơn tối nhanh 5", "2025-06-28", null, 3, 0.0, 0.0, 0.0, 0.0, "2025-06-29", "2025-07-02", "Giảm tinh bột, tăng rau xanh", "System"},
                {null, "Ăn chay ngày rằm", "Thực đơn chay ngày rằm 1", "2025-07-01", null, 3, 0.0, 0.0, 0.0, 0.0, "2025-07-15", "2025-07-17", "Rau củ, đậu hũ", "System"},
                {null, "Ăn chay ngày rằm", "Thực đơn chay ngày rằm 2", "2025-07-05", null, 4, 0.0, 0.0, 0.0, 0.0, "2025-07-18", "2025-07-21", "Đậu hũ, nấm", "System"},
                {null, "Ăn chay ngày rằm", "Thực đơn chay ngày rằm 3", "2025-07-10", null, 3, 0.0, 0.0, 0.0, 0.0, "2025-07-19", "2025-07-21", "Rau củ, đậu hũ", "System"},
                {null, "Ăn chay ngày rằm", "Thực đơn chay ngày rằm 4", "2025-07-12", null, 2, 0.0, 0.0, 0.0, 0.0, "2025-07-20", "2025-07-21", "Rau củ kho, đậu hũ", "System"},
                {null, "Ăn chay ngày rằm", "Thực đơn chay ngày rằm 5", "2025-07-15", null, 3, 0.0, 0.0, 0.0, 0.0, "2025-07-22", "2025-07-24", "Rau củ, đậu hũ", "System"},
                {null, "Bữa sáng thơm ngon", "Bữa sáng năng lượng 1", "2025-07-01", null, 5, 0.0, 0.0, 0.0, 0.0, "2025-07-01", "2025-07-05", "Trứng, bánh mì nguyên cám", "System"},
                {null, "Bữa sáng thơm ngon", "Bữa sáng năng lượng 2", "2025-07-06", null, 4, 0.0, 0.0, 0.0, 0.0, "2025-07-07", "2025-07-10", "Sinh tố trái cây, cháo yến mạch", "System"},
                {null, "Bữa sáng thơm ngon", "Bữa sáng năng lượng 3", "2025-07-10", null, 6, 0.0, 0.0, 0.0, 0.0, "2025-07-11", "2025-07-16", "Bánh mì, trứng ốp la", "System"},
                {null, "Bữa sáng thơm ngon", "Bữa sáng năng lượng 4", "2025-07-15", null, 3, 0.0, 0.0, 0.0, 0.0, "2025-07-16", "2025-07-18", "Ngũ cốc, sữa chua", "System"},
                {null, "Bữa sáng thơm ngon", "Bữa sáng năng lượng 5", "2025-07-20", null, 5, 0.0, 0.0, 0.0, 0.0, "2025-07-21", "2025-07-25", "Trứng hấp, trái cây tươi", "System"},
                {null, "Thực đơn giảm cân", "Giảm cân cấp tốc 1", "2025-07-01", null, 7, 0.0, 0.0, 0.0, 0.0, "2025-07-02", "2025-07-09", "Giảm lượng tinh bột", "System"},
                {null, "Thực đơn giảm cân", "Giảm cân cấp tốc 2", "2025-07-10", null, 7, 0.0, 0.0, 0.0, 0.0, "2025-07-11", "2025-07-17", "Thêm nhiều rau và protein", "System"},
                {null, "Thực đơn giảm cân", "Giảm cân cấp tốc 3", "2025-07-20", null, 6, 0.0, 0.0, 0.0, 0.0, "2025-07-21", "2025-07-26", "Chế độ ăn ít tinh bột", "System"},
                {null, "Thực đơn giảm cân", "Giảm cân cấp tốc 4", "2025-07-25", null, 5, 0.0, 0.0, 0.0, 0.0, "2025-07-26", "2025-07-30", "Chế độ ăn tiết kiệm calo", "System"},
                {null, "Thực đơn giảm cân", "Giảm cân cấp tốc 5", "2025-07-30", null, 4, 0.0, 0.0, 0.0, 0.0, "2025-07-31", "2025-08-03", "Tăng cường vận động và chế độ ăn", "System"},
                {1, null, "Thực đơn cá ngày ngày 1", "2025-07-30", null, 1, 0.0, 0.0, 0.0, 0.0, "2025-07-30", "2025-07-30", "", "Download"},
                {1, null, "Thực đơn cá nhân tuần 1", "2025-07-30", null, 4, 0.0, 0.0, 0.0, 0.0, "2025-07-31", "2025-08-03", "", "Download"},
                {1, null, "Thực đơn ăn kiêng cá nhân tuần 2", "2025-07-30", null, 7, 0.0, 0.0, 0.0, 0.0, "2025-07-31", "2025-09-03", "", "Download"}
        };

        db.beginTransaction();
        try {
            for (Object[] r : rows) {
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put("customerID", (Integer) r[0] == null ? null : (Integer) r[0]);
                cv.put("mealCategory", (String) r[1]);
                cv.put("title", (String) r[2]);
                cv.put("createdAt", (String) r[3]);
                cv.putNull("imageThumb");
                cv.put("totalDays", (Integer) r[5]);
                cv.put("avgCalories", (Double) r[6]);
                cv.put("avgCarbs", (Double) r[7]);
                cv.put("avgProtein", (Double) r[8]);
                cv.put("avgFat", (Double) r[9]);
                cv.put("startDate", (String) r[10]);
                cv.put("endDate", (String) r[11]);
                cv.put("note", (String) r[12]);
                cv.put("type", (String) r[13]);

                db.insert(TBL_MEAL_PLANS, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /* =========================================================
     *  TABLE: MEAL_DAYS
     *  Schema & seed data section
     *  --------------------------------------------------------- */
    private void seedMealDays(SQLiteDatabase db) {
        Object[][] rows = {
                {1, "2025-06-02", "Thực đơn tối nhanh 1 - Ngày 1"},
                {1, "2025-06-03", "Thực đơn tối nhanh 1 - Ngày 2"},
                {1, "2025-06-04", "Thực đơn tối nhanh 1 - Ngày 3"},
                {1, "2025-06-05", "Thực đơn tối nhanh 1 - Ngày 4"},
                {1, "2025-06-06", "Thực đơn tối nhanh 1 - Ngày 5"},
                {1, "2025-06-07", "Thực đơn tối nhanh 1 - Ngày 6"},
                {1, "2025-06-08", "Thực đơn tối nhanh 1 - Ngày 7"},
                {2, "2025-06-12", "Thực đơn tối nhanh 2 - Ngày 1"},
                {2, "2025-06-13", "Ngày 2"},
                {2, "2025-06-14", "Ngày 3"},
                {2, "2025-06-15", "Ngày 4"},
                {2, "2025-06-16", "Ngày 5"},
                {3, "2025-06-22", "Thực đơn tối nhanh 3 - Ngày 1"},
                {3, "2025-06-23", "Ngày 2"},
                {3, "2025-06-24", "Ngày 3"},
                {3, "2025-06-25", "Ngày 4"},
                {3, "2025-06-26", "Ngày 5"},
                {3, "2025-06-27", "Ngày 6"},
                {4, "2025-06-26", "Thực đơn tối nhanh 4 - Ngày 1"},
                {4, "2025-06-27", "Ngày 2"},
                {4, "2025-06-28", "Ngày 3"},
                {4, "2025-06-29", "Ngày 4"},
                {5, "2025-06-29", "Thực đơn tối nhanh 5 - Ngày 1"},
                {5, "2025-06-30", "Ngày 2"},
                {5, "2025-07-01", "Ngày 3"},
                {6, "2025-07-15", "Thực đơn chay ngày rằm 1 - Ngày 1"},
                {6, "2025-07-16", "Ngày 2"},
                {6, "2025-07-17", "Ngày 3"},
                {7, "2025-07-18", "Thực đơn chay ngày rằm 2 - Ngày 1"},
                {7, "2025-07-19", "Ngày 2"},
                {7, "2025-07-20", "Ngày 3"},
                {7, "2025-07-21", "Ngày 4"},
                {8, "2025-07-19", "Thực đơn chay ngày rằm 3 - Ngày 1"},
                {8, "2025-07-20", "Ngày 2"},
                {8, "2025-07-21", "Ngày 3"},
                {9, "2025-07-20", "Thực đơn chay ngày rằm 4 - Ngày 1"},
                {9, "2025-07-21", "Ngày 2"},
                {10, "2025-07-22", "Thực đơn chay ngày rằm 5 - Ngày 1"},
                {10, "2025-07-23", "Ngày 2"},
                {10, "2025-07-24", "Ngày 3"},
                {11, "2025-07-01", "Bữa sáng năng lượng 1 - Ngày 1"},
                {11, "2025-07-02", "Ngày 2"},
                {11, "2025-07-03", "Ngày 3"},
                {11, "2025-07-04", "Ngày 4"},
                {11, "2025-07-05", "Ngày 5"},
                {12, "2025-07-07", "Bữa sáng năng lượng 2 - Ngày 1"},
                {12, "2025-07-08", "Ngày 2"},
                {12, "2025-07-09", "Ngày 3"},
                {13, "2025-07-11", "Bữa sáng năng lượng 3 - Ngày 1"},
                {13, "2025-07-12", "Ngày 2"},
                {13, "2025-07-13", "Ngày 3"},
                {13, "2025-07-14", "Ngày 4"},
                {13, "2025-07-15", "Ngày 5"},
                {14, "2025-07-16", "Bữa sáng năng lượng 4 - Ngày 1"},
                {14, "2025-07-17", "Ngày 2"},
                {14, "2025-07-18", "Ngày 3"},
                {15, "2025-07-21", "Bữa sáng năng lượng 5 - Ngày 1"},
                {15, "2025-07-22", "Ngày 2"},
                {15, "2025-07-23", "Ngày 3"},
                {15, "2025-07-24", "Ngày 4"},
                {15, "2025-07-25", "Ngày 5"},
                {16, "2025-07-02", "Giảm cân cấp tốc 1 - Ngày 1"},
                {16, "2025-07-03", "Ngày 2"},
                {16, "2025-07-04", "Ngày 3"},
                {16, "2025-07-05", "Ngày 4"},
                {16, "2025-07-06", "Ngày 5"},
                {16, "2025-07-07", "Ngày 6"},
                {16, "2025-07-08", "Ngày 7"},
                {17, "2025-07-10", "Giảm cân cấp tốc 2 - Ngày 1"},
                {17, "2025-07-11", "Ngày 2"},
                {17, "2025-07-12", "Ngày 3"},
                {17, "2025-07-13", "Ngày 4"},
                {17, "2025-07-14", "Ngày 5"},
                {17, "2025-07-15", "Ngày 6"},
                {17, "2025-07-16", "Ngày 7"},
                {18, "2025-07-20", "Giảm cân cấp tốc 3 - Ngày 1"},
                {18, "2025-07-21", "Ngày 2"},
                {18, "2025-07-22", "Ngày 3"},
                {18, "2025-07-23", "Ngày 4"},
                {18, "2025-07-24", "Ngày 5"},
                {18, "2025-07-25", "Ngày 6"},
                {19, "2025-07-25", "Giảm cân cấp tốc 4 - Ngày 1"},
                {19, "2025-07-26", "Ngày 2"},
                {19, "2025-07-27", "Ngày 3"},
                {19, "2025-07-28", "Ngày 4"},
                {19, "2025-07-29", "Ngày 5"},
                {20, "2025-07-30", "Giảm cân cấp tốc 5 - Ngày 1"},
                {20, "2025-07-31", "Ngày 2"},
                {20, "2025-08-01", "Ngày 3"},
                {20, "2025-08-02", "Ngày 4"},
                {21, "2025-07-30", "Thực đơn cá nhân ngày"},
                {22, "2025-07-31", "Thực đơn cá nhân tuần ngày 1"},
                {22, "2025-08-01", "Ngày 2"},
                {22, "2025-08-02", "Ngày 3"},
                {22, "2025-08-03", "Ngày 4"},
                {23, "2025-07-30", "Thực đơn ăn kiêng cá nhân ngày 1"},
                {23, "2025-07-31", "Ngày 2"},
                {23, "2025-08-01", "Ngày 3"},
                {23, "2025-08-02", "Ngày 4"},
                {23, "2025-08-03", "Ngày 5"},
                {23, "2025-08-04", "Ngày 6"},
                {23, "2025-08-05", "Ngày 7"}
        };

        db.beginTransaction();
        try {
            for (Object[] r : rows) {
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put("mealPlanID", (Integer) r[0]);
                cv.put("date", (String) r[1]);
                cv.put("note", (String) r[2]);

                db.insert(TBL_MEAL_DAYS, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /* =========================================================
     *  TABLE: MEAL_TIMES
     *  Seed demo rows (shortened – extend as needed)
     *  --------------------------------------------------------- */
    private void seedMealTimes(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            // Build 3 meals (Sáng/Trưa/Tối) for mỗi MealDay có trong bảng MEAL_DAYS
            android.database.Cursor c = db.rawQuery("SELECT mealDayID, note FROM " + TBL_MEAL_DAYS, null);
            if (c.moveToFirst()) {
                do {
                    long mealDayID = c.getLong(0);
                    String note = c.getString(1);
                    for (String mealType : new String[]{"Sáng", "Trưa", "Tối"}) {
                        android.content.ContentValues cv = new android.content.ContentValues();
                        cv.put("mealDayID", mealDayID);
                        cv.put("mealType", mealType);
                        cv.put("note", note);
                        db.insert(TBL_MEAL_TIMES, null, cv);
                    }
                } while (c.moveToNext());
            }
            c.close();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /* =========================================================
     *  TABLE: MEAL_RECIPES
     *  Seed join rows (MealTimeID ↔ RecipeID)
     *  --------------------------------------------------------- */
    private void seedMealRecipes(SQLiteDatabase db) {
        int[][] rows = {
            {1,13},{2,14},{3,11},{4,19},{5,17},{6,6},{7,9},{8,5},{9,11},{10,5},
            {11,18},{12,7},{13,6},{14,17},{15,4},{16,7},{17,2},{18,14},{19,16},{20,13},
            {21,7},{22,1},{23,12},{24,15},{25,4},{26,5},{27,4},{28,19},{29,12},{30,15},
            {31,1},{32,6},{33,1},{34,11},{35,18},{36,7},{37,4},{38,15},{39,7},{40,15},
            {41,10},{42,7},{43,15},{44,7},{45,11},{46,13},{47,2},{48,13},{49,6},{50,7},
            {51,11},{52,7},{53,14},{54,6},{55,17},{56,18},{57,12},{58,4},{59,3},{60,19},
            {61,20},{62,7},{63,2},{64,2},{65,17},{66,10},{67,10},{68,9},{69,1},{70,11},
            {71,17},{72,13},{73,10},{74,9},{75,10},{76,1},{77,18},{78,8},{79,13},{80,17},
            {81,13},{82,13},{83,14},{84,20},{85,18},{86,12},{87,9},{88,11},{89,19},{90,9},
            {91,17},{92,5},{93,20},{94,14},{95,2},{96,4},{97,12},{98,11},{99,13},{100,17},
            {101,18},{102,13},{103,1},{104,17},{105,20},{106,15},{107,6},{108,20},{109,10},{110,6},
            {111,8},{112,16},{113,12},{114,13},{115,20},{116,2},{117,15},{118,19},{119,18},{120,6},
            {121,2},{122,19},{123,10},{124,4},{125,7},{126,17},{127,3},{128,3},{129,20},{130,14},
            {131,5},{132,7},{133,3},{134,12},{135,9},{136,14},{137,10},{138,16},{139,4},{140,1},
            {141,2},{142,7},{143,5},{144,1},{145,11},{146,16},{147,1},{148,10},{149,1},{150,14},
            {151,7},{152,11},{153,4},{154,4},{155,3},{156,17},{157,17},{158,14},{159,19},{160,4},
            {161,10},{162,7},{163,2},{164,12},{165,3},{166,10},{167,18},{168,12},{169,16},{170,18},
            {171,18},{172,5},{173,17},{174,13},{175,14},{176,13},{177,12},{178,1},{179,5},{180,1},
            {181,13},{182,13},{183,16},{184,16},{185,11},{186,9},{187,20},{188,12},{189,5},{190,16},
            {191,2},{192,4},{193,18},{194,11},{195,5},{196,18},{197,16},{198,9},{199,18},{200,1},
            {201,15},{202,10},{203,13},{204,6},{205,5},{206,16},{207,13},{208,19},{209,11},{210,12},
            {211,7},{212,1},{213,15},{214,3},{215,11},{216,18},{217,13},{218,4},{219,9},{220,9},
            {221,1},{222,12},{223,7},{224,10},{225,11},{226,20},{227,9},{228,3},{229,3},{230,15},
            {231,20},{232,18},{233,1},{234,17},{235,8},{236,19},{237,8},{238,14},{239,11},{240,11},
            {241,5},{242,5},{243,4},{244,10},{245,15},{246,20},{247,20},{248,8},{249,2},{250,13},
            {251,17},{252,10},{253,5},{254,2},{255,14},{256,15},{257,15},{258,6},{259,5},{260,4},
            {261,16},{262,13},{263,15},{264,16},{265,4},{266,18},{267,7},{268,10},{269,19},{270,14},
            {271,5},{272,4},{273,10},{274,15},{275,20},{276,20},{277,8},{278,2},{279,13},{280,17},
            {281,10},{282,5},{283,2},{284,14},{285,15},{286,15},{287,6},{288,5},{289,4},{290,15},
            {291,3},{292,11},{293,18},{294,13},{295,4},{296,9},{297,9},{298,1},{299,12},{300,7},
            {301,10},{302,11},{303,20},{304,11},{305,18}
        };

        db.beginTransaction();
        try {
            for (int[] r : rows) {
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put("mealTimeID", r[0]);
                cv.put("recipeID", r[1]);
                db.insert(TBL_MEAL_RECIPES, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /* =========================================================
     *  TABLE: CUSTOMER_HEALTH
     *  Seed sample health data
     *  --------------------------------------------------------- */
    private void seedCustomerHealth(SQLiteDatabase db) {
        Object[][] rows = {
                {1, "Male", 30, 70.0, 175.0, "Mesomorph", "None", "Muscle Gain", 75.0, 0.5, "Moderate"},
                {2, "Female", 25, 60.0, 165.0, "Ectomorph", "Pollen", "Weight Loss", 55.0, 0.3, "High"},
                {3, "Male", 40, 80.0, 180.0, "Endomorph", "Nuts", "Improve Endurance", 78.0, 0.4, "Low"}
        };

        db.beginTransaction();
        try {
            for (Object[] r : rows) {
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put("customerID", (Integer) r[0]);
                cv.put("gender", (String) r[1]);
                cv.put("age", (Integer) r[2]);
                cv.put("weight", (Double) r[3]);
                cv.put("height", (Double) r[4]);
                cv.put("bodyType", (String) r[5]);
                cv.put("allergy", (String) r[6]);
                cv.put("commonGoal", (String) r[7]);
                cv.put("targetWeight", (Double) r[8]);
                cv.put("weightChangeRate", (Double) r[9]);
                cv.put("physicalActivityLevel", (String) r[10]);
                db.insert(TBL_CUSTOMER_HEALTH, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /* =========================================================
     *  TABLE: RECIPES
     *  Seed sample 20 recipes
     *  --------------------------------------------------------- */
    private void seedRecipes(SQLiteDatabase db) {
        Object[][] rows = {
                {"Gà kho gừng", "Món truyền thống thơm nức vị gừng", "gà, gừng, truyền thống", "2025-06-17", R.drawable.recipe1, "Món mặn", 120, 10, 4},
                {"Salad trái cây", "Món ăn nhẹ, giàu vitamin", "salad, trái cây, healthy", "2025-06-18", R.drawable.recipe2, "Món khai vị", 95, 5, 4},
                {"Bò xào cay", "Thơm nồng vị tiêu, thích hợp ngày lạnh", "bò, tiêu, cay", "2025-06-19", R.drawable.recipe3, "Món mặn", 100, 8, 6},
                {"Đậu hũ xào nấm", "Món chay thanh đạm cho ngày rằm", "đậu hũ, nấm, chay", "2025-06-20", R.drawable.recipe4, "Món chay", 78, 4, 5},
                {"Cá hồi áp chảo", "Món ăn giàu Omega-3 tốt cho sức khỏe", "cá hồi, healthy", "2025-06-21", R.drawable.recipe5, "Món chính", 130, 9, 5},
                {"Canh bí đỏ nấu tôm", "Canh ngọt mát, dễ tiêu hóa", "bí đỏ, tôm, canh", "2025-06-22", R.drawable.recipe6, "Món canh", 86, 3, 5},
                {"Bún bò Huế", "Đậm đà hương vị miền Trung", "bún, bò, Huế, cay", "2025-06-23", R.drawable.recipe7, "Món nước", 140, 12, 5},
                {"Mì Ý sốt cà chua", "Món Tây dễ làm, hợp khẩu vị", "mì, Ý, sốt, cà chua", "2025-06-24", R.drawable.recipe8, "Món chính", 110, 6, 5},
                {"Súp cua", "Món khai vị bổ dưỡng từ cua và nấm", "súp, cua, khai vị", "2025-06-25", R.drawable.recipe9, "Món khai vị", 75, 4, 5},
                {"Cơm chiên dương châu", "Món cơm quen thuộc, dễ ăn, giàu màu sắc", "cơm, chiên, dương châu", "2025-06-26", R.drawable.recipe10, "Món chính", 102, 7, 5},
                {"Gỏi cuốn tôm thịt", "Món cuốn thanh mát, ít dầu mỡ", "gỏi cuốn, tôm, thịt", "2025-06-27", R.drawable.recipe11, "Món ăn nhẹ", 90, 6, 5},
                {"Trứng hấp kiểu Nhật", "Trứng mềm mịn, ăn kèm nước tương nhẹ", "trứng, hấp, Nhật", "2025-06-28", R.drawable.recipe12, "Món phụ", 88, 3, 5},
                {"Nem rán", "Món ăn truyền thống, vỏ giòn nhân thơm", "nem, rán, Việt Nam", "2025-06-29", R.drawable.recipe13, "Món mặn", 112, 9, 5},
                {"Bánh mì bơ tỏi", "Món phụ giòn thơm, dễ làm", "bánh mì, bơ, tỏi", "2025-06-30", R.drawable.recipe14, "Món phụ", 70, 2, 5},
                {"Sườn ram mặn", "Sườn mềm thấm vị, ăn cùng cơm nóng", "sườn, ram, mặn", "2025-07-01", R.drawable.recipe15, "Món chính", 124, 11, 5},
                {"Canh chua cá", "Vị chua dịu thanh mát, miền Tây", "canh, chua, cá, miền Tây", "2025-07-02", R.drawable.recipe16, "Món canh", 98, 6, 5},
                {"Cháo gà hành tím", "Món cháo bổ dưỡng cho người ốm", "cháo, gà, hành tím", "2025-07-03", R.drawable.recipe17, "Món nhẹ", 85, 4, 5},
                {"Bánh flan", "Món tráng miệng mềm mịn, ngọt dịu", "bánh, flan, tráng miệng", "2025-07-04", R.drawable.recipe18, "Tráng miệng", 92, 5, 5},
                {"Trà đào cam sả", "Thức uống giải nhiệt, thơm mát", "trà, đào, cam, sả", "2025-07-05", R.drawable.recipe19, "Thức uống", 77, 3, 5},
                {"Sữa chua dẻo", "Món ăn vặt tốt cho tiêu hóa", "sữa chua, dẻo, healthy", "2025-07-06", R.drawable.recipe20, "Tráng miệng", 84, 6, 5}
        };

        db.beginTransaction();
        try {
            for (Object[] r : rows) {
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put("recipeName", (String) r[0]);
                cv.put("description", (String) r[1]);
                cv.put("tag", (String) r[2]);
                cv.put("createdAt", (String) r[3]);

                int resId = (Integer) r[4];
                Bitmap bmp = BitmapFactory.decodeResource(ctx.getResources(), resId);
                if (bmp != null) {
                    java.io.ByteArrayOutputStream osImg = new java.io.ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 90, osImg);
                    cv.put("imageThumb", osImg.toByteArray());
                }

                cv.put("category", (String) r[5]);
                cv.put("commentAmount", (Integer) r[6]);
                cv.put("likeAmount", (Integer) r[7]);
                cv.put("sectionAmount", (Integer) r[8]);
                db.insert(TBL_RECIPES, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /* =========================================================
     *  TABLE: RECIPE_DETAIL
     *  Nutritional & meta info per recipe
     *  --------------------------------------------------------- */
    private void seedRecipeDetails(SQLiteDatabase db) {
        Object[][] rows = {
            {1,650,45,12,30,"gà, gừng","Việt Nam",60,"Mặn, cay","Ấm bụng, truyền thống"},
            {2,180,3,28,6,"salad, trái cây","Âu",15,"Ngọt","Giàu vitamin, tốt cho tiêu hóa"},
            {3,580,35,20,40,"bò, tiêu, cay","Việt Nam",25,"Cay","Tăng nhiệt, ngon cơm"},
            {4,300,12,18,14,"đậu hũ, nấm, chay","Chay",20,"Thanh đạm","Ăn nhẹ, lành mạnh"},
            {5,420,32,15,20,"cá hồi, healthy","Nhật",30,"Mặn, béo","Omega-3, tốt cho tim mạch"},
            {6,310,18,25,10,"bí đỏ, tôm, canh","Việt Nam",25,"Ngọt, thanh","Dễ tiêu hóa, mát"},
            {7,500,30,45,15,"bún, bò, Huế, cay","Trung Bộ",45,"Đậm đà, cay","Đậm vị, no lâu"},
            {8,430,25,40,12,"mì, Ý, sốt, cà chua","Ý",35,"Chua nhẹ","Nhanh gọn, dễ ăn"},
            {9,320,22,18,8,"súp, cua, khai vị","Việt Nam",20,"Ngọt, nhẹ","Bổ dưỡng, khai vị"},
            {10,550,20,60,15,"cơm, chiên, dương châu","Trung Quốc",35,"Đậm đà","Nhiều màu sắc, đủ chất"},
            {11,260,18,22,8,"gỏi cuốn, tôm, thịt","Việt Nam",20,"Thanh nhẹ","Dễ tiêu, mát"},
            {12,180,15,8,6,"trứng, hấp, Nhật","Nhật",15,"Mềm nhẹ","Bổ sung đạm dễ hấp thụ"},
            {13,420,22,28,20,"nem, rán, Việt Nam","Việt Nam",25,"Giòn, béo","Truyền thống, ngon miệng"},
            {14,500,28,40,18,"bánh mì, bơ, tỏi","Âu",20,"Béo, thơm","Nhanh gọn, no lâu"},
            {15,530,35,25,22,"sườn, ram, mặn","Việt Nam",40,"Mặn, đậm","Đưa cơm, đậm đà"},
            {16,300,20,30,10,"canh, chua, cá","Miền Tây",25,"Chua nhẹ","Dễ ăn, mát"},
            {17,250,18,20,8,"cháo, gà, hành tím","Việt Nam",25,"Nhẹ, thơm","Bồi dưỡng người ốm"},
            {18,320,8,35,12,"bánh, flan, tráng miệng","Âu",60,"Ngọt, béo","Tráng miệng nhẹ nhàng"},
            {19,110,0,25,0,"trà, đào, cam, sả","Việt Nam",10,"Chua nhẹ","Giải nhiệt, detox"},
            {20,180,5,15,7,"sữa chua, dẻo","Âu",5,"Ngọt nhẹ","Tốt tiêu hóa, ăn nhẹ"}
        };

        db.beginTransaction();
        try {
            for(Object[] r: rows){
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put("recipeID", (Integer) r[0]);
                cv.put("calo", ((Number) r[1]).doubleValue());
                cv.put("protein", ((Number) r[2]).doubleValue());
                cv.put("carbs", ((Number) r[3]).doubleValue());
                cv.put("fat", ((Number) r[4]).doubleValue());
                cv.put("foodTag", (String) r[5]);
                cv.put("cuisine", (String) r[6]);
                cv.put("cookingTimeMinutes", (Integer) r[7]);
                cv.put("flavor", (String) r[8]);
                cv.put("benefit", (String) r[9]);
                db.insert(TBL_RECIPE_DETAILS, null, cv);
            }
            db.setTransactionSuccessful();
        } finally { db.endTransaction(); }
    }

    /* =========================================================
     *  TABLE: INSTRUCTION_RECIPES
     *  Seed sample recipe instructions (shortened example)
     *  --------------------------------------------------------- */
    private void seedInstructionRecipes(SQLiteDatabase db) {
        // NOTE: Only a subset of sample data is inserted to keep APK size small.
        Object[][] rows = {
                {1, 1, 1, "Sơ chế nguyên liệu", "Thịt gà đem chà xát với một ít muối rồi rửa lại vài lần với nước cho sạch, sau đó cắt thành từng miếng vừa ăn. Gừng rửa sạch, gọt vỏ rồi cắt thành từng sợi nhỏ. Tỏi và hành tím lột bỏ vỏ, rửa lại với nước rồi băm nhỏ. Ớt rửa sạch, cắt lát nhỏ. Rau mùi rửa sạch, thái nhỏ.", "ga_kho_gung_step1"},
                {2, 1, 2, "Ướp thịt gà", "Cho thịt gà vào trong một cái tô cùng với một ít bột ngọt, hạt nêm, muối, nước mắm, đường, tương ớt, nước màu. Sau đó trộn đều rồi cho thêm một ít gừng, hành tím vào để thịt gà thơm hơn, rồi ướp trong khoảng 30 phút cho thấm gia vị.", ""},
                {3, 1, 3, "Kho gà", "Bắc một cái nồi lên bếp, rồi cho một ít dầu ăn, gừng và tỏi vào, phi lên cho vàng thơm. Tiếp đó, cho thịt gà vào, đảo đều cho thịt gà săn lại, rồi cho hết phần nước ướp vào. Đun trong khoảng 3 phút cho thịt gà thấm gia vị, rồi cho nước lọc vào, hạ nhỏ lửa và kho thịt gà trong 20 phút.", ""},
                {4, 1, 4, "Thành phẩm", "Cho gà kho gừng ra đĩa rồi thêm ít rau mùi cho đẹp mắt và thưởng thức.", ""},
                {5, 2, 1, "Sơ chế nguyên liệu", "Thái các loại quả thành những miếng vuông nhỏ vừa ăn. Cà chua bi thái đôi. Các loại quả như táo, lê nên ngâm qua nước muối loãng để không bị thâm.", "salad_trai_cay_step1"}
        };

        db.beginTransaction();
        try {
            for (Object[] r : rows) {
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put("instructionID", (Integer) r[0]);
                cv.put("recipeID", (Integer) r[1]);
                cv.put("numberSection", (Integer) r[2]);
                cv.put("title", (String) r[3]);
                cv.put("content", (String) r[4]);
                cv.put("image", (String) r[5]);
                db.insert(TBL_INSTRUCTION_RECIPES, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /* =========================================================
     *  TABLE: RECIPE_INGREDIENTS
     *  Seed sample recipe ingredients
     *  --------------------------------------------------------- */
    private void seedRecipeIngredients(SQLiteDatabase db) {
        Object[][] rows = {
        {1,1,500.0,"Gà ta"},
        {2,1,30.0,"Gừng"},
        {3,1,3.0,"Hành tím"},
        {4,1,2.0,"Nước mắm"},
        {5,1,1.0,"Đường"},
        {6,1,0.5,"Tiêu"},
        {7,2,300.0,"Trái cây hỗn hợp"},
        {8,2,2.0,"Sốt mayonnaise"},
        {9,2,1.0,"Mật ong"},
        {10,2,50.0,"Rau xà lách"},
        {11,3,200.0,"Thịt bò"},
        {12,3,1.0,"Ớt chuông"},
        {13,3,1.0,"Hành tây"},
        {14,3,2.0,"Tỏi"},
        {15,3,1.0,"Dầu hào"},
        {16,4,2.0,"Đậu hũ"},
        {17,4,100.0,"Nấm đùi gà"},
        {18,4,0.5,"Cà rốt"},
        {19,4,1.0,"Hành lá"},
        {20,5,200.0,"Cá hồi"},
        {21,5,0.5,"Tiêu đen"},
        {22,5,0.25,"Muối"},
        {23,5,1.0,"Chanh"},
        {24,5,1.0,"Bơ"},
        {25,6,200.0,"Bí đỏ"},
        {26,6,100.0,"Tôm tươi"},
        {27,6,1.0,"Hành lá"},
        {28,6,1.0,"Nước mắm"},
        {29,7,300.0,"Thịt bò"},
        {30,7,100.0,"Chả Huế"},
        {31,7,200.0,"Bún tươi"},
        {32,7,2.0,"Sả"},
        {33,7,1.0,"Ớt bột"},
        {34,8,150.0,"Mì Ý"},
        {35,8,100.0,"Thịt xay"},
        {36,8,100.0,"Sốt cà chua"},
        {37,8,20.0,"Phô mai"},
        {38,9,100.0,"Thịt cua"},
        {39,9,50.0,"Nấm tuyết"},
        {40,9,100.0,"Ngô ngọt"},
        {41,9,1.0,"Trứng gà"},
        {42,10,1.0,"Cơm nguội"},
        {43,10,1.0,"Xúc xích"},
        {44,10,1.0,"Trứng gà"},
        {45,10,30.0,"Đậu hà lan"},
        {46,11,100.0,"Tôm luộc"},
        {47,11,100.0,"Thịt ba chỉ"},
        {48,11,10.0,"Bánh tráng"},
        {49,11,100.0,"Bún tươi"},
        {50,11,50.0,"Rau sống"},
        {51,12,2.0,"Trứng gà"},
        {52,12,200.0,"Nước dùng dashi"},
        {53,12,30.0,"Nấm đông cô"},
        {54,12,50.0,"Tôm bóc vỏ"},
        {55,13,200.0,"Thịt heo xay"},
        {56,13,20.0,"Miến"},
        {57,13,10.0,"Mộc nhĩ"},
        {58,13,0.25,"Cà rốt"},
        {59,13,10.0,"Bánh đa nem"},
        {60,14,1.0,"Bánh mì"},
        {61,14,2.0,"Bơ"},
        {62,14,1.0,"Tỏi băm"},
        {63,14,1.0,"Ngò"},
        {64,15,300.0,"Sườn non"},
        {65,15,2.0,"Nước mắm"},
        {66,15,1.0,"Đường"},
        {67,15,0.5,"Tiêu"},
        {68,16,200.0,"Cá lóc"},
        {69,16,1.0,"Cà chua"},
        {70,16,1.0,"Me"},
        {71,16,50.0,"Giá đỗ"},
        {72,17,0.5,"Gạo tẻ"},
        {73,17,100.0,"Thịt gà"},
        {74,17,2.0,"Hành tím"},
        {75,17,1.0,"Gừng"},
        {76,18,3.0,"Trứng gà"},
        {77,18,100.0,"Sữa đặc"},
        {78,18,250.0,"Sữa tươi"},
        {79,18,3.0,"Đường"},
        {80,19,1.0,"Trà đen"},
        {81,19,2.0,"Đào ngâm"},
        {82,19,1.0,"Cam tươi"},
        {83,19,1.0,"Sả cây"},
        {84,20,2.0,"Sữa chua"},
        {85,20,5.0,"Gelatin"},
        {86,20,1.0,"Đường"},
        {87,20,50.0,"Sữa đặc"}
        };

        db.beginTransaction();
        try {
            for(Object[] r: rows) {
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put("ingredientID", (Integer) r[0]);
                cv.put("recipeID", (Integer) r[1]);
                cv.put("quantity", ((Number) r[2]).doubleValue());
                cv.put("nameIngredient", (String) r[3]);
                db.insert(TBL_RECIPE_INGREDIENTS, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void seedRecipeDownloads(SQLiteDatabase db) {
        Object[][] rows = {
                {1, 1, "2025-06-10"},
                {2, 2, "2025-06-10"},
                {3, 3, "2025-06-11"}
        };

        db.beginTransaction();
        try {
            for (Object[] r : rows) {
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put("customerID", (Integer) r[0]);
                cv.put("recipeID", (Integer) r[1]);
                cv.put("downloadedAt", (String) r[2]);
                db.insert(TBL_RECIPE_DOWNLOAD, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void seedFavouriteRecipes(SQLiteDatabase db) {
        Object[][] rows = {
                {1, 4, "2025-06-16"},
                {2, 5, "2025-06-17"},
                {3, 7, "2025-06-18"}
        };

        db.beginTransaction();
        try {
            for (Object[] r : rows) {
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put("recipeID", (Integer) r[0]);
                cv.put("customerID", (Integer) r[1]);
                cv.put("createdAt", (String) r[2]);
                db.insert(TBL_FAVOURITE_RECIPES, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /* =========================================================
     *  TABLE: CARTS
     *  Seed sample cart data
     *  --------------------------------------------------------- */
    private void seedCarts(SQLiteDatabase db) {
        Object[][] rows = {
                {1, 1, "2023-01-15 08:25:41"},
                {2, 1, "2023-02-07 10:12:09"},
                {3, 1, "2023-03-18 14:30:55"},
                {4, 1, "2023-04-25 16:45:12"},
                {5, 2, "2023-05-10 09:05:38"},
                {6, 2, "2023-06-03 11:20:27"},
                {7, 3, "2023-07-21 17:40:03"},
                {8, 3, "2023-08-13 13:15:49"},
                {9, 2, "2023-09-05 08:00:00"},
                {10, 2, "2023-10-27 19:30:18"},
                {11, 4, "2023-11-14 12:50:00"},
                {12, 4, "2023-12-31 23:59:59"},
                {13, 4, "2024-02-01 06:45:30"},
                {14, 4, "2024-03-22 15:10:15"}
        };

        db.beginTransaction();
        try {
            for (Object[] r : rows) {
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put("cartID", (Integer) r[0]);
                cv.put("customerID", (Integer) r[1]);
                cv.put("createdAt", (String) r[2]);
                db.insert(TBL_CARTS, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /* =========================================================
     *  TABLE: CART_DETAILS
     *  Seed sample cart detail data
     *  --------------------------------------------------------- */
    private void seedCartDetails(SQLiteDatabase db) {
        Object[][] rows = {
                {1, 1, 2},
                {1, 3, 1},
                {2, 4, 1},
                {3, 2, 2},
                {3, 5, 1},
                {4, 6, 3},
                {5, 1, 1},
                {6, 7, 2},
                {7, 8, 1},
                {8, 3, 1},
                {9, 9, 4},
                {10, 5, 2},
                {11, 2, 1},
                {12, 10, 1}
        };

        db.beginTransaction();
        try {
            for (Object[] r : rows) {
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put("cartID", (Integer) r[0]);
                cv.put("productID", (Integer) r[1]);
                cv.put("quantity", (Integer) r[2]);
                db.insert(TBL_CART_DETAILS, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /* =========================================================
     *  TABLE: ADDRESSES
     *  Seed sample address data
     *  --------------------------------------------------------- */
    private void seedAddresses(SQLiteDatabase db) {
        Object[][] rows = {
                {1, 1, "Trần Văn Bảo", "0905123456", "123 Lê Lợi", "Quận 1", "TP. Hồ Chí Minh", 1, "Giao giờ hành chính"},
                {2, 1, "Nguyễn Thị Thanh", "0905899988", "456 Nguyễn Trãi", "Quận 3", "TP. Hồ Chí Minh", 0, "Giao sau 18h"},
                {3, 1, "Phạm Văn Long", "0916234567", "789 Trần Hưng Đạo", "Quận 5", "TP. Hồ Chí Minh", 0, "Có người lớn nhận hàng"},
                {4, 2, "Nguyễn Hồng Phúc", "0937345678", "12 Trường Chinh", "Hoàn Kiếm", "Hà Nội", 0, "Giao sáng sớm"},
                {5, 2, "Trần Minh Quân", "0948456789", "34 Nguyễn Văn Cừ", "Ninh Kiều", "Cần Thơ", 1, "Giao cuối tuần"},
                {6, 4, "Lê Thị Thảo", "0929567890", "23 Cách Mạng Tháng 8", "Thanh Xuân", "Hà Nội", 1, "Giao giờ hành chính"},
                {7, 4, "Nguyễn Hoàng Anh", "0960678901", "89 Nguyễn Du", "Ngô Quyền", "Hải Phòng", 0, "Gọi trước khi giao hàng"},
                {8, 4, "Mai Văn Hiếu", "0979988776", "99 Hai Bà Trưng", "Lê Chân", "Hải Phòng", 0, "Nhận hàng buổi tối"},
                {9, 5, "Phan Thị Hòa", "0971789012", "12 Hoàng Hoa Thám", "Bình Thạnh", "TP. Hồ Chí Minh", 1, null},
                {10, 5, "Nguyễn Trọng Tín", "0982890123", "90 Võ Văn Tần", "Hải Châu", "Đà Nẵng", 0, null},
                {11, 6, "Lâm Quỳnh Như", "0993901234", "21 Trần Phú", "Quận 10", "TP. Hồ Chí Minh", 0, "Giao ngay trong ngày"},
                {12, 6, "Trương Hải Yến", "0904111222", "111 Trường Chinh", "Ba Đình", "Hà Nội", 1, null},
                {13, 7, "Lê Hồng Nhung", "0936225566", "45 Nguyễn Bỉnh Khiêm", "Quận 2", "TP. Hồ Chí Minh", 1, "Giao đúng giờ"}
        };

        db.beginTransaction();
        try {
            for (Object[] r : rows) {
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put("addressID", (Integer) r[0]);
                cv.put("customerID", (Integer) r[1]);
                cv.put("receiverName", (String) r[2]);
                cv.put("phone", (String) r[3]);
                cv.put("addressLine", (String) r[4]);
                cv.put("district", (String) r[5]);
                cv.put("province", (String) r[6]);
                cv.put("isDefault", (Integer) r[7]);
                if (r[8] != null) cv.put("note", (String) r[8]); else cv.putNull("note");
                db.insert(TBL_ADDRESSES, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /* =========================================================
     *  TABLE: ORDERS
     *  Seed sample order data
     *  --------------------------------------------------------- */
    private void seedOrders(SQLiteDatabase db) {
        Object[][] rows = {
                {1, 1, 1, null, "2023-03-10", 220.0, "completed", "Credit Card", "Giao hàng nhanh"},
                {2, 3, 1, 1, "2023-04-01", 150.0, "completed", "COD", null},
                {3, 4, 2, null, "2023-04-20", 300.0, "cancelled", "Credit Card", null},
                {4, 5, 4, 2, "2023-05-15", 410.0, "pending", "Credit Card", null},
                {5, 6, 5, null, "2023-06-01", 100.0, "completed", "COD", "Yêu cầu gói kỹ"},
                {6, 7, 5, 3, "2023-06-18", 90.0, "completed", "Credit Card", null}
        };

        db.beginTransaction();
        try {
            for (Object[] r : rows) {
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put("orderID", (Integer) r[0]);
                cv.put("cartID", (Integer) r[1]);
                cv.put("addressID", (Integer) r[2]);
                if (r[3] != null) cv.put("couponID", (Integer) r[3]); else cv.putNull("couponID");
                cv.put("orderDate", (String) r[4]);
                cv.put("totalPrice", ((Number) r[5]).doubleValue());
                cv.put("status", (String) r[6]);
                cv.put("paymentMethod", (String) r[7]);
                if (r[8] != null) cv.put("note", (String) r[8]); else cv.putNull("note");
                db.insert(TBL_ORDERS, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /* =========================================================
     *  TABLE: ORDER_LINES
     *  Seed sample order line data
     *  --------------------------------------------------------- */
    private void seedOrderLines(SQLiteDatabase db) {
        Object[][] rows = {
                {1, 1, 1, 2, 200.0},
                {2, 1, 4, 1, 150.0},
                {3, 1, 2, 2, 300.0},
                {4, 2, 6, 3, 410.0},
                {5, 2, 1, 1, 100.0},
                {6, 2, 7, 2, 90.0},
                {7, 3, 8, 1, 560.0},
                {8, 3, 3, 1, 210.0},
                {9, 3, 9, 4, 305.0},
                {10, 4, 5, 2, 85.0},
                {11, 4, 2, 1, 190.0},
                {12, 5, 1, 1, 275.0},
                {13, 5, 3, 1, 120.0},
                {14, 6, 6, 3, 400.0}
        };

        db.beginTransaction();
        try {
            for (Object[] r : rows) {
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put("orderLineID", (Integer) r[0]);
                cv.put("orderID", (Integer) r[1]);
                cv.put("productID", (Integer) r[2]);
                cv.put("quantity", (Integer) r[3]);
                cv.put("totalPrice", ((Number) r[4]).doubleValue());
                db.insert(TBL_ORDER_LINES, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /* =========================================================
     *  TABLE: PRODUCT_FEEDBACK
     *  Seed sample feedback data
     *  --------------------------------------------------------- */
    private void seedProductFeedback(SQLiteDatabase db) {
        Object[][] rows = {
            {1, 1, "Sản phẩm đúng như mô tả, đóng gói chắc chắn, giao hàng nhanh.", "link", 5, "2023-01-15 08:25:41"},
            {2, 2, "Hàng nhận bị móp méo, chất lượng không như kỳ vọng, khá thất vọng.", "link", 2, "2023-02-07 10:12:09"},
            {3, 3, "Sản phẩm ổn trong tầm giá, không có gì đặc biệt nhưng cũng không tệ.", null, 4, "2023-03-18 14:30:55"},
            {4, 5, "Rất ưng ý, mùi hương dễ chịu, màu sắc đẹp, chắc chắn sẽ mua lại lần sau.", "link", 5, "2023-04-25 16:45:12"},
            {5, 6, "Hàng đẹp như hình nhưng giao hơi chậm, mong lần sau cải thiện hơn.", null, 4, "2023-05-10 09:05:38"},
            {6, 8, "Đóng gói tỉ mỉ, chất lượng vượt mong đợi, rất hài lòng.", null, 5, "2023-06-03 11:20:27"},
            {7, 9, "Sản phẩm lỗi nhẹ, tuy nhân viên hỗ trợ nhanh nhưng vẫn chưa hài lòng.", "link", 1, "2023-07-21 17:40:03"},
            {8, 10, "Mua dùng thử thấy ổn, hy vọng dùng lâu dài vẫn giữ chất lượng.", null, 3, "2023-08-13 13:15:49"},
            {9, 11, "Hàng mới, đầy đủ phụ kiện, giao đúng hẹn, sẽ giới thiệu bạn bè.", "link", 5, "2023-09-05 08:00:00"},
            {10, 12, "Đã mua nhiều lần, lần này vẫn hài lòng, shop giữ phong độ rất tốt.", null, 4, "2023-10-27 19:30:18"},
            {11, 13, "Nhận sai màu, shop phản hồi chậm, hơi thất vọng với cách xử lý.", null, 2, "2023-11-14 12:50:00"}
        };

        db.beginTransaction();
        try {
            for (Object[] r : rows) {
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put("proFeedbackID", (Integer) r[0]);
                cv.put("orderLineID", (Integer) r[1]);
                cv.put("content", (String) r[2]);
                if (r[3] != null) cv.put("image", (String) r[3]); else cv.putNull("image");
                cv.put("rating", (Integer) r[4]);
                cv.put("createdAt", (String) r[5]);
                db.insert(TBL_PRODUCT_FEEDBACK, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /* =========================================================
     *  TABLE: BLOGS
     *  Seed sample blog data
     *  --------------------------------------------------------- */
    private void seedBlogs(SQLiteDatabase db) {
        Object[][] rows = {
            {1, "Cách Làm Canh Chua Cá Lóc Chuẩn Vị Miền Tây", "Blog content", "Bếp Nhà Ta", "2025-06-18", "https://i-giadinh.vnecdn.net/2023/04/25/Thanh-pham-1-1-7239-1682395675.jpg", "Đã đăng", "Món Chính"},
            {2, "Bí Quyết Làm Nước Mắm Chua Ngọt Cho Mọi Món Ăn", "Blog content", "Bếp Nhà Ta", "2025-06-18", "https://khaihoanphuquoc.com.vn/wp-content/uploads/2023/08/cach-pha-nuoc-mam-sanh-dac-de-duoc-lau.jpg", "Đã đăng", "Sốt & Gia Vị"},
            {3, "Cách Làm Bánh Flan Không Cần Lò Nướng", "Blog content", "Bếp Nhà Ta", "2025-06-18", "https://file.hstatic.net/1000396324/file/banh-flan-socola-2_71a4b9be8ddc459c9be9a7226ae74476.jpg", "Chưa xuất bản", "Món Ăn Vặt & Tráng Miệng"},
            {4, "5 Mẹo Giữ Rau Tươi Lâu Trong Tủ Lạnh", "Blog content", "Bếp Nhà Ta", "2025-06-18", "https://cdn.tgdd.vn/2021/12/CookDish/cach-lam-rau-cau-sua-tuoi-gion-ngon-hap-dan-chi-voi-cac-avt-1200x676.jpg", "Đã đăng", "Mẹo Bếp & Kỹ Thuật"},
            {5, "Gợi Ý Thực Đơn Mùa Hè Thanh Mát Cho Gia Đình", "Blog content", "Bếp Nhà Ta", "2025-06-18", "https://cdn.tgdd.vn/2021/06/CookProductThumb/thumb2-620x620-1.jpg", "Đã đăng", "Món Chính"},
            {6, "Cách Làm Sữa Hạt Hạnh Nhân Tại Nhà", "Blog content", "Bếp Nhà Ta", "2025-06-18", "https://cdn.tgdd.vn/2023/03/CookDish/huong-dan-lam-sua-hanh-nhan-bang-may-lam-sua-hat-tai-nha-don-avt-1200x676.jpg", "Chưa xuất bản", "Món Ăn Vặt & Tráng Miệng"},
            {7, "Làm Gỏi Cuốn Tươi Ngon Đơn Giản Tại Nhà", "Blog content", "Bếp Nhà Ta", "2025-06-18", "https://khaihoanphuquoc.com.vn/wp-content/uploads/2023/11/nu%CC%9Bo%CC%9B%CC%81c-ma%CC%86%CC%81m-cha%CC%82%CC%81m-go%CC%89i-cuo%CC%82%CC%81n-1200x923.png", "Chưa xuất bản", "Món Chính"},
            {8, "Cách Chọn Cá Tươi Ngon Ngoài Chợ", "Blog content", "Bếp Nhà Ta", "2025-06-18", "https://chocayenso.com/wp-content/uploads/2024/09/cach-chon-ca-tuoi-ngon.jpg", "Đã đăng", "Mẹo Bếp & Kỹ Thuật"},
            {9, "Tự Làm Sinh Tố Mùa Hè Giải Nhiệt", "Blog content", "Bếp Nhà Ta", "2025-06-18", "https://images2.thanhnien.vn/Uploaded/yennh/2022_04_25/sinh-to-dd-8235.jpg", "Đã đăng", "Món Ăn Vặt & Tráng Miệng"},
            {10, "Thực Đơn Chay Thanh Đạm Ngày Rằm", "Blog content", "Bếp Nhà Ta", "2025-06-18", "https://thuanchay.vn/wp-content/uploads/2024/09/thuc-don-chay-30-ngay-3.webp", "Đã đăng", "Món Chính"},
            {11, "Cách Làm Chè Bắp Ngọt Bùi Tại Nhà", "Blog content", "Bếp Nhà Ta", "2025-06-18", "https://hc.com.vn/i/ecommerce/media/ck3263552-cach-nau-che-bap-8.jpg", "Đã đăng", "Món Ăn Vặt & Tráng Miệng"},
            {12, "Hướng Dẫn Làm Kim Chi Cải Thảo Chuẩn Hàn Quốc", "Blog content", "Bếp Nhà Ta", "2025-06-18", "https://cdn2.fptshop.com.vn/unsafe/1920x0/filters:format(webp):quality(75)/2023_11_3_638346373170840215_cach-lam-kim-chi.jpg", "Chưa xuất bản", "Sốt & Gia Vị"},
            {13, "Cách Làm Gà Chiên Nước Mắm Giòn Ngon", "Blog content", "Bếp Nhà Ta", "2025-06-18", "https://www.vinmec.com/static/uploads/20210602_020034_162242_cach_lam_thit_ga_ch_max_1800x1800_jpg_a2edd266e4.jpg", "Đã đăng", "Món Chính"},
            {14, "Cách Làm Nước Sâm Mát Lạnh Giải Nhiệt", "Blog content", "Bếp Nhà Ta", "2025-06-18", "https://static.gia-hanoi.com/uploads/2024/05/nau-nuoc-sam-bi-dao.jpg", "Đã đăng", "Món Ăn Vặt & Tráng Miệng"},
            {15, "Làm Bánh Xèo Vàng Giòn Nhân Đầy Đặn", "Blog content", "Bếp Nhà Ta", "2025-06-18", "https://i.ytimg.com/vi/hxI-i5jAeB8/hq720.jpg?sqp=-oaymwEhCK4FEIIDSFryq4qpAxMIARUAAAAAGAElAADIQj0AgKJD&rs=AOn4CLBojnmiQAk3Xll7SREnHLzCPhVJ7w", "Chưa xuất bản", "Món Chính"}
        };

        db.beginTransaction();
        try {
            for (Object[] r : rows) {
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put("blogID", (Integer) r[0]);
                cv.put("title", (String) r[1]);
                cv.put("content", (String) r[2]);
                cv.put("authorName", (String) r[3]);
                cv.put("createdAt", (String) r[4]);
                if (r[5] != null) cv.put("imageThumb", (String) r[5]); else cv.putNull("imageThumb");
                cv.put("status", (String) r[6]);
                cv.put("tag", (String) r[7]);
                db.insert(TBL_BLOGS, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /* =========================================================
     *  TABLE: FAVOURITE_BLOGS
     *  Seed sample favourite blogs data (updated)
     *  --------------------------------------------------------- */
    private void seedFavouriteBlogs(SQLiteDatabase db) {
        Object[][] rows = {
            {3, 2, "2025-06-18"},
            {8, 2, "2025-06-18"},
            {4, 2, "2025-06-18"},
            {1, 5, "2025-04-15"},
            {7, 1, "2025-04-15"},
            {3, 3, "2025-03-10"},
            {10, 3, "2025-06-10"},
            {15, 4, "2025-06-18"}
        };

        db.beginTransaction();
        try {
            for (Object[] r : rows) {
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put("blogID", (Integer) r[0]);
                cv.put("customerID", (Integer) r[1]);
                cv.put("createdAt", (String) r[2]);
                db.insert(TBL_FAVOURITE_BLOGS, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /* =========================================================
     *  TABLE: BLOG_COMMENTS
     *  Seed sample comment data
     *  --------------------------------------------------------- */
    private void seedBlogComments(SQLiteDatabase db) {
        Object[][] rows = {
                {1, 3, 3, "Món ăn này ngon quá, cả nhà mình ai cũng khen ngon", "2025-06-18", null, 25},
                {2, 6, 2, "Cảm ơn Bếp vì đã chia sẻ", "2025-06-18", null, 10},
                {3, 2, 5, "Rất ngon Bếp nhé", "2025-06-18", null, 3},
                {4, 6, 1, "Mình rất thích công thức này", "2025-06-18", 2, 8},
                {5, 2, 3, "Rất mong Bếp chia sẻ nhiều thông tin xịn như này nhá", "2025-06-18", 3, 50}
        };

        db.beginTransaction();
        try {
            for (Object[] r : rows) {
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put("blogCommentID", (Integer) r[0]);
                cv.put("blogID", (Integer) r[1]);
                cv.put("customerID", (Integer) r[2]);
                cv.put("content", (String) r[3]);
                cv.put("createdAt", (String) r[4]);
                if (r[5] != null) cv.put("parentCommentID", (Integer) r[5]); else cv.putNull("parentCommentID");
                cv.put("usefulness", (Integer) r[6]);
                db.insert(TBL_BLOG_COMMENTS, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /* =========================================================
     *  TABLE: RECIPE_COMMENTS
     *  Seed sample comment data
     *  --------------------------------------------------------- */
    private void seedRecipeComments(SQLiteDatabase db) {
        Object[][] rows = {
                {1, 6, 2, "Món ăn này ngon quá, cả nhà mình ai cũng khen ngon", "2025-04-24", null, 25},
                {2, 10, 3, "Cảm ơn Bếp vì đã chia sẻ", "2025-03-15", null, 10},
                {3, 12, 4, "Rất ngon Bếp nhé", "2025-03-19", null, 3},
                {4, 1, 7, "Mình rất thích công thức này", "2025-05-08", null, 8},
                {5, 3, 1, "Rất mong Bếp chia sẻ nhiều thông tin xịn như này nhá", "2025-05-10", null, 50}
        };

        db.beginTransaction();
        try {
            for (Object[] r : rows) {
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put("recipeCommentID", (Integer) r[0]);
                cv.put("recipeID", (Integer) r[1]);
                cv.put("customerID", (Integer) r[2]);
                cv.put("content", (String) r[3]);
                cv.put("createdAt", (String) r[4]);
                if (r[5] != null) cv.put("parentCommentID", (Integer) r[5]); else cv.putNull("parentCommentID");
                cv.put("usefulness", (Integer) r[6]);
                db.insert(TBL_RECIPE_COMMENTS, null, cv);
            }
            db.setTransactionSuccessful();
        } finally { db.endTransaction(); }
    }

    /* =========================================================
     *  TABLE: COUPONS
     *  Seed sample coupon data
     *  --------------------------------------------------------- */
    private void seedCoupons(SQLiteDatabase db) {
        Object[][] rows = {
                {1, null, "Giảm 20% cho đơn hàng đầu tiên", 100000, "2025-06-18", "2025-06-30", 50000, 20, 1, null},
                {2, 2, "Mã giảm 50K cho khách VIP", 200000, "2025-06-20", "2025-07-10", null, 50000, 0, 100},
                {3, null, "Flash Sale giảm 100K", 300000, "2025-06-25", "2025-06-28", null, 100000, 1, null},
                {4, 5, "Mã cá nhân giảm 15% đơn hàng", 150000, "2025-07-01", "2025-07-15", 30000, 15, 0, 50},
                {5, null, "Mã chung giảm 30K cho mọi đơn", 50000, "2025-06-18", "2025-06-25", null, 30000, 1, null},
                {6, 3, "Ưu đãi đổi điểm giảm 200K", 500000, "2025-07-01", "2025-07-31", null, 200000, 0, 200}
        };

        db.beginTransaction();
        try {
            for (Object[] r : rows) {
                android.content.ContentValues cv = new android.content.ContentValues();
                cv.put("couponID", (Integer) r[0]);
                if (r[1] != null) cv.put("customerID", (Integer) r[1]); else cv.putNull("customerID");
                cv.put("couponTitle", (String) r[2]);
                cv.put("minPrice", (Integer) r[3]);
                cv.put("validDate", (String) r[4]);
                cv.put("expireDate", (String) r[5]);
                if (r[6] != null) cv.put("maxDiscount", (Integer) r[6]); else cv.putNull("maxDiscount");
                cv.put("couponValue", (Integer) r[7]);
                cv.put("isGeneral", (Integer) r[8]);
                if (r[9] != null) cv.put("exchangePoints", (Integer) r[9]); else cv.putNull("exchangePoints");
                db.insert(TBL_COUPONS, null, cv);
            }
            db.setTransactionSuccessful();
        } finally { db.endTransaction(); }
    }
} 