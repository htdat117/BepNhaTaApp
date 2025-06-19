package com.example.bepnhataapp.common.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.bepnhataapp.R;

import java.io.ByteArrayOutputStream;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 3;
    private static final String DB_NAME = "BepNhaTa.db";

    public static final String TBL_CUSTOMERS = "CUSTOMERS";
    public static final String TBL_MEAL_PLANS = "MEAL_PLANS";

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

    private final Context ctx;
    private static final String TAG = "DBHelper";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        ctx = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create schema first
        db.execSQL(SQL_CREATE_CUSTOMER);
        db.execSQL(SQL_CREATE_MEAL_PLAN);

        // Seed initial data but do NOT allow any runtime-exception here to abort DB creation
        try {
            seedCustomers(db);
            seedMealPlans(db);
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error seeding initial customers. Database schema is still created.", e);
            // Optionally you could decide to delete all partially inserted rows here.
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_CUSTOMERS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_MEAL_PLANS);
        onCreate(db);
    }

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
} 