package com.example.bepnhataapp.features.manage_order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.dao.OrderDao;
import com.example.bepnhataapp.common.dao.OrderLineDao;
import com.example.bepnhataapp.common.dao.ProductDao;
import com.example.bepnhataapp.common.model.Order;
import com.example.bepnhataapp.common.model.OrderStatus;
import com.example.bepnhataapp.common.model.OrderLine;
import com.example.bepnhataapp.common.model.Product;
import java.util.List;
import android.widget.LinearLayout;
import android.widget.Button;
import androidx.core.content.ContextCompat;

public class OrderListFragment extends Fragment {
    private static final String ARG_STATUS = "order_status";
    OrderStatus status;
    private RecyclerView rv;
    private TextView tvEmpty;

    public static OrderListFragment newInstance(OrderStatus status) {
        OrderListFragment f = new OrderListFragment();
        Bundle b = new Bundle();
        b.putString(ARG_STATUS, status.name());
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        rv = view.findViewById(R.id.rvOrders);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        if (getArguments() != null) {
            status = OrderStatus.valueOf(getArguments().getString(ARG_STATUS, OrderStatus.WAIT_CONFIRM.name()));
        } else {
            status = OrderStatus.WAIT_CONFIRM;
        }
        reloadOrders();
        return view;
    }

    public void reloadOrders() {
        List<Order> orders = getOrdersByStatus(status);
        if (orders == null || orders.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            rv.setAdapter(new OrderAdapter(orders, status, this));
        }
    }

    private List<Order> getOrdersByStatus(OrderStatus status) {
        // Lấy customerID hiện tại
        long customerId = com.example.bepnhataapp.common.utils.OrderHelper.getCurrentCustomerId(getContext());
        List<Order> allOrders = new OrderDao(getContext()).getByCustomer(customerId);
        java.util.List<Order> filtered = new java.util.ArrayList<>();
        for (Order o : allOrders) {
            if (o.getStatus() != null && o.getStatus().equals(status.name())) {
                filtered.add(o);
            }
        }
        return filtered;
    }
}

class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderVH> {
    private final List<Order> orders;
    private final OrderStatus status;
    private final OrderListFragment fragment;
    OrderAdapter(List<Order> orders, OrderStatus status, OrderListFragment fragment) { this.orders = orders; this.status = status; this.fragment = fragment; }
    @NonNull
    @Override
    public OrderVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId;
        if (status == OrderStatus.WAIT_CONFIRM) {
            layoutId = R.layout.item_order_list_wait_confirm;
        } else if (status == OrderStatus.CANCELED) {
            layoutId = R.layout.item_order_list_canceled;
        } else {
            layoutId = R.layout.item_order_list;
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new OrderVH(v, status);
    }
    @Override
    public void onBindViewHolder(@NonNull OrderVH h, int pos) {
        Order o = orders.get(pos);
        h.tvCode.setText("Mã đơn hàng: " + o.getOrderID());
        // Hiển thị trạng thái
        String displayStatus;
        try {
            displayStatus = OrderStatus.valueOf(o.getStatus()).getDisplayName();
        } catch (Exception e) {
            displayStatus = o.getStatus();
        }
        // Hiển thị tổng số tiền đã thanh toán (đã gồm ship, voucher nếu có)
        if (h.tvTotal != null) {
            h.tvTotal.setText("Tổng số tiền: " + formatMoney(o.getTotalPrice()) + "đ");
        }
        // Hiển thị danh sách sản phẩm
        h.layoutOrderProducts.removeAllViews();
        List<OrderLine> lines = new OrderLineDao(h.itemView.getContext()).getByOrder(o.getOrderID());
        ProductDao productDao = new ProductDao(h.itemView.getContext());
        LayoutInflater inflater = LayoutInflater.from(h.itemView.getContext());
        for (int i = 0; i < lines.size(); i++) {
            OrderLine line = lines.get(i);
            Product p = productDao.getById(line.getProductID());
            View itemView = inflater.inflate(R.layout.item_order_product, h.layoutOrderProducts, false);
            TextView tvName = itemView.findViewById(R.id.tvProductName);
            TextView tvVariant = itemView.findViewById(R.id.tvVariant);
            TextView tvOldPrice = itemView.findViewById(R.id.tvOldPrice);
            TextView tvPrice = itemView.findViewById(R.id.tvPrice);
            TextView tvQuantity = itemView.findViewById(R.id.tvQuantity);
            ImageView img = itemView.findViewById(R.id.imgProduct);
            if (p != null) {
                if (tvName != null) tvName.setText(p.getProductName());
                if (p.getProductThumb() != null && img != null) {
                    com.bumptech.glide.Glide.with(img.getContext())
                        .load(p.getProductThumb())
                        .placeholder(R.drawable.sample_img)
                        .into(img);
                } else if (img != null) {
                    img.setImageDrawable(null); // Không hiển thị gì nếu không có ảnh
                }
                if (tvVariant != null) tvVariant.setText("Phân loại: " + (line.getQuantity() == 2 ? "4 người" : "2 người"));
                if (tvOldPrice != null) {
                    tvOldPrice.setText(formatMoney(p.getProductPrice()) + "đ");
                    tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
                }
                if (tvPrice != null) tvPrice.setText(formatMoney(line.getTotalPrice()) + "đ");
            }
            if (tvQuantity != null) tvQuantity.setText("x" + line.getQuantity());
            // Thêm margin top cho các sản phẩm từ thứ 2 trở đi
            if (i > 0) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) itemView.getLayoutParams();
                params.topMargin = (int) (itemView.getContext().getResources().getDisplayMetrics().density * 8); // 8dp
                itemView.setLayoutParams(params);
            }
            h.layoutOrderProducts.addView(itemView);
        }
        // Xử lý click vào item để xem chi tiết đơn hàng
        h.itemView.setOnClickListener(v -> {
            android.content.Intent it = new android.content.Intent(v.getContext(), com.example.bepnhataapp.features.manage_order.order_detail.class);
            it.putExtra("orderId", o.getOrderID());
            v.getContext().startActivity(it);
        });
        // Hiển thị nút phù hợp theo trạng thái (đã tách layout nên không cần set visibility nhiều)
        if (status == OrderStatus.WAIT_CONFIRM && h.btnCancelOrder != null) {
            h.btnCancelOrder.setOnClickListener(v -> {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(v.getContext());
                android.view.LayoutInflater inflaterDialog = android.view.LayoutInflater.from(v.getContext());
                android.view.View dialogView = inflaterDialog.inflate(R.layout.popup_cancel_order, null);
                builder.setView(dialogView);
                final android.app.AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialogView.findViewById(R.id.btnQuit).setOnClickListener(view -> dialog.dismiss());
                dialogView.findViewById(R.id.btnAccept).setOnClickListener(view -> {
                    // Cập nhật trạng thái đơn hàng sang CANCELED
                    com.example.bepnhataapp.common.model.Order order = o;
                    order.setStatus(com.example.bepnhataapp.common.model.OrderStatus.CANCELED.name());
                    new com.example.bepnhataapp.common.dao.OrderDao(v.getContext()).update(order);
                    android.widget.Toast.makeText(v.getContext(), "Đã huỷ đơn hàng", android.widget.Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    // Reload lại danh sách của tab hiện tại
                    if (fragment != null) {
                        fragment.reloadOrders();
                    }
                });
                dialog.show();
            });
        }
        if (status == OrderStatus.CANCELED && h.btnReorder != null) {
            h.btnReorder.setOnClickListener(v -> {
                // TODO: Xử lý logic mua lại đơn hàng
                android.widget.Toast.makeText(v.getContext(), "Chức năng MUA LẠI đang phát triển", android.widget.Toast.LENGTH_SHORT).show();
            });
        }
    }
    private int getProductCount(long orderId, OrderVH h) {
        List<OrderLine> lines = new OrderLineDao(h.itemView.getContext()).getByOrder(orderId);
        int count = 0;
        for (OrderLine l : lines) count += l.getQuantity();
        return count;
    }
    private String formatMoney(double money) {
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance(new java.util.Locale("vi","VN"));
        return nf.format(money);
    }
    @Override
    public int getItemCount() { return orders == null ? 0 : orders.size(); }
    static class OrderVH extends RecyclerView.ViewHolder {
        TextView tvCode, tvTotal;
        LinearLayout layoutOrderProducts;
        Button btnReview, btnCancelOrder;
        Button btnReorder, btnReturn;
        OrderVH(View v, OrderStatus status) {
            super(v);
            tvCode = v.findViewById(R.id.tvOrderCode);
            tvTotal = v.findViewById(R.id.tvOrderTotal);
            layoutOrderProducts = v.findViewById(R.id.layoutOrderProducts);
            if (status == OrderStatus.WAIT_CONFIRM) {
                btnCancelOrder = v.findViewById(R.id.btnCancelOrder);
                btnReorder = null;
                btnReview = null;
                btnReturn = null;
            } else if (status == OrderStatus.CANCELED) {
                btnReorder = v.findViewById(R.id.btnReorder);
                btnCancelOrder = null;
                btnReview = null;
                btnReturn = null;
            } else {
                btnReview = v.findViewById(R.id.btnReview);
                btnCancelOrder = v.findViewById(R.id.btnCancelOrder);
                btnReorder = v.findViewById(R.id.btnReorder);
                btnReturn = v.findViewById(R.id.btnReturn);
            }
        }
    }
} 
