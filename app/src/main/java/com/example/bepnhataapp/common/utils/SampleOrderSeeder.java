package com.example.bepnhataapp.common.utils;

import android.content.Context;

import com.example.bepnhataapp.common.dao.AddressDao;
import com.example.bepnhataapp.common.dao.CartDao;
import com.example.bepnhataapp.common.dao.OrderDao;
import com.example.bepnhataapp.common.dao.OrderLineDao;
import com.example.bepnhataapp.common.dao.ProductDao;
import com.example.bepnhataapp.common.model.Address;
import com.example.bepnhataapp.common.model.Cart;
import com.example.bepnhataapp.common.model.Order;
import com.example.bepnhataapp.common.model.OrderLine;
import com.example.bepnhataapp.common.model.OrderStatus;
import com.example.bepnhataapp.common.model.Product;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Utility class để chèn một số đơn hàng giả phục vụ việc demo quản lý đơn hàng.
 * Hiện tại sẽ chèn 6 đơn tương ứng với tất cả trạng thái trong {@link OrderStatus}
 * cho customerID = 10 nếu chưa tồn tại bất kỳ đơn nào của khách này.
 */
public final class SampleOrderSeeder {

    private SampleOrderSeeder() {}

    /**
     * Gọi hàm này ở bất kỳ đâu (ví dụ trong {@code onCreate} của màn hình quản lý đơn)
     * để chắc chắn dữ liệu demo đã sẵn sàng.
     */
    public static void seedOrdersForCustomer(Context ctx, long customerId) {
        try {
            OrderDao orderDao = new OrderDao(ctx);

            // Nếu customer chưa tồn tại thì tạo nhanh một bản ghi mẫu để tránh lỗi FOREIGN KEY
            com.example.bepnhataapp.common.dao.CustomerDao customerDao = new com.example.bepnhataapp.common.dao.CustomerDao(ctx);
            com.example.bepnhataapp.common.model.Customer existingCustomer = customerDao.findById(customerId);
            if (existingCustomer == null) {
                com.example.bepnhataapp.common.model.Customer c = new com.example.bepnhataapp.common.model.Customer();
                c.setCustomerID(customerId); // insert với ID cụ thể
                c.setFullName("Khách hàng demo");
                c.setGender("Nam");
                c.setBirthday("1990-01-01");
                c.setEmail("demo@example.com");
                c.setPassword("123456");
                c.setPhone("0999999910");
                c.setCustomerType("Normal");
                c.setLoyaltyPoint(0);
                c.setCreatedAt(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date()));
                c.setStatus("ACTIVE");
                // CustomerDao.insert sẽ tạo ID autoincrement, không đảm bảo =10, nên bỏ qua nếu không trùng.
                // Do vậy, chỉ insert khi thiếu và chấp nhận ID mới phát sinh.
                customerId = customerDao.insert(c);
            }

            if (!orderDao.getByCustomer(customerId).isEmpty()) {
                // Đã có đơn, không seed nữa tránh trùng lặp
                return;
            }

            // Lấy hoặc tạo địa chỉ mặc định cho customer
            AddressDao addressDao = new AddressDao(ctx);
            Address address = addressDao.getDefault(customerId);
            if (address == null) {
                address = new Address();
                address.setCustomerID(customerId);
                address.setReceiverName("Nguyễn Văn A");
                address.setPhone("0123456789");
                address.setAddressLine("123 Đường ABC");
                address.setDistrict("Quận 1");
                address.setProvince("HCM");
                address.setNote("Địa chỉ mặc định");
                address.setDefault(true);
                long addrId = addressDao.insert(address);
                address.setAddressID(addrId);
            }

            // Lấy một vài sản phẩm có sẵn để tạo dòng đơn hàng
            ProductDao productDao = new ProductDao(ctx);
            List<Product> productList = productDao.getAll();
            if (productList == null || productList.isEmpty()) {
                return; // Không có sản phẩm thì thôi
            }

            CartDao cartDao = new CartDao(ctx);
            OrderLineDao lineDao = new OrderLineDao(ctx);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            double baseTotal = 120_000; // giá mẫu

            for (OrderStatus st : OrderStatus.values()) {
                // Tạo cart mới
                Cart cart = new Cart();
                cart.setCustomerID(customerId);
                cart.setCreatedAt(sdf.format(new Date()));
                long cartId = cartDao.insert(cart);

                // Tạo order chính
                Order order = new Order();
                order.setCartID(cartId);
                order.setAddressID(address.getAddressID());
                order.setOrderDate(sdf.format(new Date()));
                order.setTotalPrice(baseTotal);
                order.setStatus(st.name());
                order.setPaymentMethod("COD");
                order.setNote("Đơn hàng mẫu trạng thái " + st.getDisplayName());
                long orderId = orderDao.insert(order);

                // Tạo 1-2 dòng sản phẩm
                for (int i = 0; i < Math.min(2, productList.size()); i++) {
                    Product p = productList.get(i);
                    OrderLine ol = new OrderLine();
                    ol.setOrderID(orderId);
                    ol.setProductID(p.getProductID());
                    ol.setQuantity(1);
                    // Lấy giá bán 2 người hoặc chung
                    int price = p.getProductPrice2() > 0 ? p.getProductPrice2() : p.getProductPrice();
                    ol.setTotalPrice(price);
                    lineDao.insert(ol);
                }
            }
        } catch (Exception ignored) {
            // Chặn mọi crash ngoài ý muốn khi seed, chỉ log nếu cần
            android.util.Log.e("SampleOrderSeeder", "Seed error", ignored);
        }
    }
} 