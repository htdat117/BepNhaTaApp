package com.example.bepnhataapp.common.utils;

import android.content.Context;

import com.example.bepnhataapp.common.dao.CartDao;
import com.example.bepnhataapp.common.dao.CartDetailDao;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.dao.ProductDao;
import com.example.bepnhataapp.common.model.Cart;
import com.example.bepnhataapp.common.model.CartDetail;
import com.example.bepnhataapp.common.model.Customer;
import com.example.bepnhataapp.common.model.Product;
import com.example.bepnhataapp.common.model.CartItem;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Utility thao tác giỏ hàng (thêm sản phẩm, lấy danh sách) dựa trên các DAO sẵn có.
 */
public final class CartHelper {
    private CartHelper() {}

    /** Thêm sản phẩm vào giỏ; servingFactor=1 (2 người) mặc định */
    public static void addProduct(Context ctx, Product p){
        addProduct(ctx,p,1);
    }

    /**
     * Thêm sản phẩm với khẩu phần (1=2 người, 2=4 người).
     * @param quantity Số lượng muốn thêm (>=1)
     */
    public static void addProduct(Context ctx, Product p, int servingFactor, int quantity){
        if(p==null || quantity<=0) return;

        long customerId = getCurrentCustomerId(ctx);
        CartDao cartDao = new CartDao(ctx);
        Cart cart = cartDao.getByCustomer(customerId).stream().findFirst().orElse(null);
        if(cart==null){
            cart = new Cart();
            cart.setCustomerID(customerId);
            cart.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
            cart.setCartID(cartDao.insert(cart));
        }

        CartDetailDao detailDao = new CartDetailDao(ctx);
        CartDetail existing = null;
        for(CartDetail cd: detailDao.getByCart(cart.getCartID())){
            if(cd.getProductID()==p.getProductID() && cd.getServingFactor()==servingFactor){
                existing = cd;
                break;
            }
        }
        if(existing==null){
            detailDao.insert(new CartDetail(cart.getCartID(), p.getProductID(), servingFactor, quantity));
        }else{
            existing.setQuantity(existing.getQuantity()+quantity);
            detailDao.update(existing);
        }

        ctx.sendBroadcast(new android.content.Intent("com.bepnhata.CART_CHANGED"));
    }

    /** Giữ lại hàm cũ để tương thích - thêm 1 sản phẩm cùng servingFactor */
    public static void addProduct(Context ctx, Product p, int servingFactor){
        addProduct(ctx, p, servingFactor, 1);
    }

    /** Trả về danh sách CartItem để hiển thị UI. */
    public static List<CartItem> loadItems(Context ctx) {
        long customerId = getCurrentCustomerId(ctx);
        CartDao cartDao = new CartDao(ctx);
        Cart cart = cartDao.getByCustomer(customerId).stream().findFirst().orElse(null);
        if (cart == null) return new ArrayList<>();

        CartDetailDao detailDao = new CartDetailDao(ctx);
        ProductDao productDao = new ProductDao(ctx);

        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        List<CartItem> list = new ArrayList<>();
        for (CartDetail cd : detailDao.getByCart(cart.getCartID())) {
            Product pr = productDao.getById(cd.getProductID());
            if(pr==null) continue;
            int price = pr.getProductPrice() * (100 - pr.getSalePercent()) / 100;
            CartItem ci = new CartItem(pr.getProductID(), pr.getProductName(), price, pr.getProductPrice(), cd.getQuantity(), pr.getProductThumb());
            ci.setServing(cd.getServingFactor()==2?"4 người":"2 người");
            // Thêm vào đầu danh sách để sản phẩm mới nhất hiển thị trước
            list.add(0, ci);
        }
        return list;
    }

    /** Xoá 1 sản phẩm variant ra khỏi giỏ hàng */
    public static void removeProduct(Context ctx, long productId, int servingFactor){
        long customerId = getCurrentCustomerId(ctx);
        CartDao cartDao = new CartDao(ctx);
        Cart cart = cartDao.getByCustomer(customerId).stream().findFirst().orElse(null);
        if(cart==null) return;
        new CartDetailDao(ctx).delete(cart.getCartID(), productId, servingFactor);
        ctx.sendBroadcast(new android.content.Intent("com.bepnhata.CART_CHANGED"));
    }

    /** Xoá danh sách sản phẩm theo variant */
    public static void removeProducts(Context ctx, List<CartItem> items){
        if(items==null || items.isEmpty()) return;
        for(CartItem ci: items){
            // Xoá cả hai khả năng khẩu phần để đảm bảo biến mất dù user đã đổi variant nhưng chưa lưu DB
            removeProduct(ctx, ci.getProductId(), 1);
            removeProduct(ctx, ci.getProductId(), 2);
        }
        ctx.sendBroadcast(new android.content.Intent("com.bepnhata.CART_CHANGED"));
    }

    private static long getCurrentCustomerId(Context ctx) {
        if (!SessionManager.isLoggedIn(ctx)) return 0;
        String phone = SessionManager.getPhone(ctx);
        if (phone == null) return 0;
        CustomerDao dao = new CustomerDao(ctx);
        Customer c = dao.findByPhone(phone);
        return c != null ? c.getCustomerID() : 0;
    }

    private static com.example.bepnhataapp.common.databases.DBHelper helper(Context ctx){
        return new com.example.bepnhataapp.common.databases.DBHelper(ctx);
    }

    /** Cập nhật số lượng cụ thể cho một sản phẩm + variant trong giỏ hàng. */
    public static void setQuantity(Context ctx, long productId, int servingFactor, int quantity){
        if(quantity<=0){
            removeProduct(ctx, productId, servingFactor);
            return;
        }

        long customerId = getCurrentCustomerId(ctx);
        CartDao cartDao = new CartDao(ctx);
        Cart cart = cartDao.getByCustomer(customerId).stream().findFirst().orElse(null);
        if(cart==null){
            // Nếu chưa có cart thì tạo mới và insert chi tiết
            cart = new Cart();
            cart.setCustomerID(customerId);
            cart.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
            cart.setCartID(cartDao.insert(cart));
        }

        CartDetailDao detailDao = new CartDetailDao(ctx);
        CartDetail target = null;
        for(CartDetail cd: detailDao.getByCart(cart.getCartID())){
            if(cd.getProductID()==productId && cd.getServingFactor()==servingFactor){
                target = cd;
                break;
            }
        }

        if(target==null){
            // Chưa tồn tại -> insert mới
            try{
                helper(ctx).getWritableDatabase().execSQL("ALTER TABLE "+ com.example.bepnhataapp.common.databases.DBHelper.TBL_CART_DETAILS +" ADD COLUMN servingFactor INTEGER DEFAULT 1");
            }catch(Exception ignored){}
            detailDao = new CartDetailDao(ctx);
            detailDao.insert(new CartDetail(cart.getCartID(), productId, servingFactor, quantity));
        }else{
            target.setQuantity(quantity);
            detailDao.update(target);
        }

        ctx.sendBroadcast(new android.content.Intent("com.bepnhata.CART_CHANGED"));
    }

    /** Thay đổi khẩu phần (servingFactor) của 1 sản phẩm variant, giữ nguyên quantity */
    public static void changeServing(Context ctx, long productId, int oldServingFactor, int newServingFactor, int quantity){
        if(oldServingFactor == newServingFactor) return;
        // Xoá variant cũ
        removeProduct(ctx, productId, oldServingFactor);
        // Thêm variant mới với số lượng hiện tại
        setQuantity(ctx, productId, newServingFactor, quantity);
    }
} 
