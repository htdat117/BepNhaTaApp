package com.example.bepnhataapp.common.utils;

import android.content.Context;

import com.example.bepnhataapp.common.dao.CartDao;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.dao.OrderDao;
import com.example.bepnhataapp.common.dao.OrderLineDao;
import com.example.bepnhataapp.common.model.Cart;
import com.example.bepnhataapp.common.model.Customer;
import com.example.bepnhataapp.common.model.Order;
import com.example.bepnhataapp.common.model.OrderLine;
import com.example.bepnhataapp.common.models.CartItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/** Utility tạo đơn hàng và đơn vị sản phẩm khi thanh toán thành công */
public final class OrderHelper {
    private OrderHelper(){}

    /**
     * Lưu đơn hàng mới xuống DB.
     * @param ctx Context
     * @param items danh sách sản phẩm đã mua
     * @param paymentMethod "COD", "VCB", "Momo", ...
     * @param addressId id địa chỉ giao hàng (<=0 nếu khách vãng lai)
     * @param shippingFee phí ship
     * @param discount tổng giảm giá (voucher / sale)
     * @param note ghi chú đơn hàng
     * @return orderID vừa tạo hoặc -1 nếu lỗi
     */
    public static long saveOrder(Context ctx, List<CartItem> items, String paymentMethod, long addressId,
                                 int shippingFee, int discount, String note){
        if(items==null || items.isEmpty()) return -1;

        long customerId = SessionManager.isLoggedIn(ctx) ? getCurrentCustomerId(ctx) : 0;

        // Lấy hoặc tạo cart hiện tại của customer (khách vãng lai thì customerID=0)
        CartDao cartDao = new CartDao(ctx);
        Cart cart = cartDao.getByCustomer(customerId).stream().findFirst().orElse(null);
        if(cart==null){
            cart = new Cart();
            cart.setCustomerID(customerId);
            cart.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
            cart.setCartID(cartDao.insert(cart));
        }

        // Tổng tiền
        double goodsTotal = 0;
        for(CartItem ci: items){
            goodsTotal += ci.getTotal();
        }
        double totalPrice = goodsTotal + shippingFee - discount;

        // Tạo order
        Order order = new Order();
        order.setCartID(cart.getCartID());
        order.setAddressID(addressId>0?addressId:0);
        order.setOrderDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        order.setTotalPrice(totalPrice);
        order.setStatus(com.example.bepnhataapp.common.model.OrderStatus.WAIT_CONFIRM.name());
        order.setPaymentMethod(paymentMethod);
        order.setNote(note);
        long orderId = new OrderDao(ctx).insert(order);

        // Tạo order lines
        OrderLineDao lineDao = new OrderLineDao(ctx);
        for(CartItem ci: items){
            OrderLine ol = new OrderLine();
            ol.setOrderID(orderId);
            ol.setProductID(ci.getProductId());
            ol.setQuantity(ci.getQuantity());
            ol.setTotalPrice(ci.getTotal());
            lineDao.insert(ol);
        }
        return orderId;
    }

    private static long getCurrentCustomerId(Context ctx){
        String phone = SessionManager.getPhone(ctx);
        if(phone==null) return 0;
        CustomerDao dao=new CustomerDao(ctx);
        Customer c=dao.findByPhone(phone);
        return c!=null?c.getCustomerID():0;
    }
} 