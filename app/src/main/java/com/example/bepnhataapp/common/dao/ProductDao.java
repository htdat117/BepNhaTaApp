package com.example.bepnhataapp.common.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bepnhataapp.common.databases.DBHelper;
import com.example.bepnhataapp.common.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDao {
    private final DBHelper dbHelper;

    public ProductDao(Context ctx) {
        this.dbHelper = new DBHelper(ctx);
    }

    private Product cursorToProduct(Cursor c) {
        Product p = new Product();
        p.setProductID(c.getLong(c.getColumnIndexOrThrow("productID")));
        p.setProductName(c.getString(c.getColumnIndexOrThrow("productName")));
        p.setProductDescription(c.getString(c.getColumnIndexOrThrow("productDescription")));

        // Price 2-person
        int idxP2 = c.getColumnIndex("productprice2");
        if (idxP2 == -1) idxP2 = c.getColumnIndex("productPrice2");
        if (idxP2 == -1) idxP2 = c.getColumnIndex("productPrice");
        if (idxP2 != -1) p.setProductPrice2(c.getInt(idxP2));

        // Price 4-person
        int idxP4 = c.getColumnIndex("productprice4");
        if (idxP4 == -1) idxP4 = c.getColumnIndex("productPrice4");
        if (idxP4 != -1) p.setProductPrice4(c.getInt(idxP4));

        // Sale percent 2 & 4
        int idxSale2 = c.getColumnIndex("salePercent2");
        if (idxSale2 == -1) idxSale2 = c.getColumnIndex("salePercent");
        if (idxSale2 != -1) p.setSalePercent2(c.getInt(idxSale2));
        int idxSale4 = c.getColumnIndex("salePercent4");
        if (idxSale4 != -1) p.setSalePercent4(c.getInt(idxSale4));

        p.setProductThumb(c.getString(c.getColumnIndexOrThrow("productThumb")));
        p.setCommentAmount(c.getInt(c.getColumnIndexOrThrow("commentAmount")));
        p.setCategory(c.getString(c.getColumnIndexOrThrow("category")));

        int idxInv2 = c.getColumnIndex("inventory2");
        if (idxInv2 == -1) idxInv2 = c.getColumnIndex("inventory");
        if (idxInv2 != -1) p.setInventory2(c.getInt(idxInv2));
        int idxInv4 = c.getColumnIndex("inventory4");
        if (idxInv4 != -1) p.setInventory4(c.getInt(idxInv4));

        p.setAvgRating(c.getDouble(c.getColumnIndexOrThrow("avgRating")));
        return p;
    }

    public long insert(Product p) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = toContentValues(p, false);
        return db.insert(DBHelper.TBL_PRODUCTS, null, cv);
    }

    public int update(Product p) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = toContentValues(p, true);
        return db.update(DBHelper.TBL_PRODUCTS, cv, "productID=?", new String[]{String.valueOf(p.getProductID())});
    }

    public int delete(long productID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DBHelper.TBL_PRODUCTS, "productID=?", new String[]{String.valueOf(productID)});
    }

    public Product getById(long productID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DBHelper.TBL_PRODUCTS, null, "productID=?", new String[]{String.valueOf(productID)}, null, null, null);
        if (c != null && c.moveToFirst()) {
            Product p = cursorToProduct(c);
            c.close();
            return p;
        }
        return null;
    }

    public List<Product> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DBHelper.TBL_PRODUCTS, null, null, null, null, null, "createdDate DESC");
        List<Product> list = new ArrayList<>();
        if (c.moveToFirst()) {
            do { list.add(cursorToProduct(c)); } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<Product> getByCategory(String category) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DBHelper.TBL_PRODUCTS, null, "category=?", new String[]{category}, null, null, "createdDate DESC");
        List<Product> list = new ArrayList<>();
        if (c.moveToFirst()) {
            do { list.add(cursorToProduct(c)); } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    /**
     * Trả về danh sách sản phẩm bán chạy nhất (hoặc hot) giới hạn limit.
     */
    public List<Product> getHotProducts(int limit) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DBHelper.TBL_PRODUCTS, null, null, null, null, null, "avgRating DESC", String.valueOf(limit));
        List<Product> list = new ArrayList<>();
        if (c.moveToFirst()) {
            do { list.add(cursorToProduct(c)); } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    private ContentValues toContentValues(Product p, boolean includeId) {
        ContentValues cv = new ContentValues();
        if (includeId) cv.put("productID", p.getProductID());
        cv.put("productName", p.getProductName());
        cv.put("productDescription", p.getProductDescription());
        cv.put("productprice2", p.getProductPrice2());
        cv.put("productprice4", p.getProductPrice4());
        cv.put("salePercent2", p.getSalePercent2());
        cv.put("salePercent4", p.getSalePercent4());
        cv.put("productThumb", p.getProductThumb());
        cv.put("commentAmount", p.getCommentAmount());
        cv.put("category", p.getCategory());
        cv.put("inventory2", p.getInventory2());
        cv.put("inventory4", p.getInventory4());
        cv.put("avgRating", p.getAvgRating());
        return cv;
    }
} 
