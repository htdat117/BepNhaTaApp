package com.example.bepnhataapp.features.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.adapter.CartAdapter;
import com.example.bepnhataapp.common.model.CartItem;
import androidx.core.content.ContextCompat;
import android.widget.TextView;
import android.widget.ImageView;
import java.text.NumberFormat;
import java.util.Locale;

import java.util.ArrayList;
import java.util.List;
import android.widget.CheckBox;

public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private View emptyView;
    private CartAdapter adapter;
    private Button btnBuyNow;
    private View layoutEditFooter;
    private Button btnDelete, btnFavorite;
    private TextView txtChange;
    private TextView tvTotal,tvDiscount;
    private boolean isEditMode = false;
    private View layoutBottomBar;
    private CheckBox cbSelectAll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewCart);
        emptyView = view.findViewById(R.id.emptyCartView);
        btnBuyNow = view.findViewById(R.id.btnBuyNow);
        layoutEditFooter = view.findViewById(R.id.layoutEditFooter);
        txtChange = view.findViewById(R.id.txtChange);
        btnDelete = view.findViewById(R.id.btnDelete);
        btnFavorite = view.findViewById(R.id.btnFavorite);
        layoutBottomBar = view.findViewById(R.id.layoutBottomBar);
        cbSelectAll = view.findViewById(R.id.checkboxSelectAll);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvDiscount = view.findViewById(R.id.tvDiscount);
        
        // Xử lý nút back
        ImageView btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> requireActivity().finish());
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartAdapter(new ArrayList<>());
        adapter.setCartListener(this::recalculateBottom);
        recyclerView.setAdapter(adapter);

        // Thêm hỗ trợ vuốt trái / phải để Xoá hoặc Lưu vào mục yêu thích
        androidx.recyclerview.widget.ItemTouchHelper swipeHelper = new androidx.recyclerview.widget.ItemTouchHelper(new androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(0,
                androidx.recyclerview.widget.ItemTouchHelper.LEFT | androidx.recyclerview.widget.ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull androidx.recyclerview.widget.RecyclerView rv,
                                   @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder vh,
                                   @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder target) {
                return false; // không hỗ trợ kéo
            }

            @Override
            public void onSwiped(@NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                if (pos == androidx.recyclerview.widget.RecyclerView.NO_POSITION) {
                    return;
                }

                com.example.bepnhataapp.common.model.CartItem item = adapter.getItems().get(pos);
                android.content.Context ctx = requireContext();

                if (direction == androidx.recyclerview.widget.ItemTouchHelper.LEFT) {
                    // Vuốt trái – hiển thị dialog xác nhận xoá
                    new androidx.appcompat.app.AlertDialog.Builder(ctx)
                            .setTitle("Xác nhận xoá")
                            .setMessage("Bạn có chắc muốn xoá sản phẩm này khỏi giỏ hàng?")
                            .setPositiveButton("Xoá", (dialog, which) -> {
                                com.example.bepnhataapp.common.utils.CartHelper.removeProduct(ctx, item.getProductId(), item.getServing().startsWith("4") ? 2 : 1);
                                adapter.getItems().remove(pos);
                                adapter.notifyItemRemoved(pos);
                                recalculateBottom();
                                android.widget.Toast.makeText(ctx, "Đã xoá sản phẩm khỏi giỏ", android.widget.Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Huỷ", (dialog, which) -> {
                                adapter.notifyItemChanged(pos); // khôi phục item nếu huỷ
                            })
                            .setOnCancelListener(dialog -> adapter.notifyItemChanged(pos))
                            .show();
                } else if (direction == androidx.recyclerview.widget.ItemTouchHelper.RIGHT) {
                    // Vuốt phải – lưu vào mục yêu thích
                    if (!com.example.bepnhataapp.common.utils.SessionManager.isLoggedIn(ctx)) {
                        android.widget.Toast.makeText(ctx, "Vui lòng đăng nhập để sử dụng", android.widget.Toast.LENGTH_SHORT).show();
                    } else {
                        String phone = com.example.bepnhataapp.common.utils.SessionManager.getPhone(ctx);
                        com.example.bepnhataapp.common.dao.CustomerDao cusDao = new com.example.bepnhataapp.common.dao.CustomerDao(ctx);
                        com.example.bepnhataapp.common.model.Customer cus = cusDao.findByPhone(phone);
                        if (cus != null) {
                            com.example.bepnhataapp.common.dao.FavouriteProductDao favDao = new com.example.bepnhataapp.common.dao.FavouriteProductDao(ctx);
                            boolean exists = favDao.isFavourite(item.getProductId(), cus.getCustomerID());
                            if (exists) {
                                android.widget.Toast.makeText(ctx, "Sản phẩm đã có trong Yêu thích", android.widget.Toast.LENGTH_SHORT).show();
                            } else {
                                favDao.insert(new com.example.bepnhataapp.common.model.FavouriteProduct(
                                        item.getProductId(),
                                        cus.getCustomerID(),
                                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date())
                                ));
                                android.widget.Toast.makeText(ctx, "Đã lưu sản phẩm vào Yêu thích ", android.widget.Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    // Khôi phục item để tiếp tục hiển thị trong danh sách
                    adapter.notifyItemChanged(pos);
                }
            }

            @Override
            public void onChildDraw(@NonNull android.graphics.Canvas c,
                                     @NonNull androidx.recyclerview.widget.RecyclerView recyclerView,
                                     @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder,
                                     float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                // Vẽ nền màu và text cho thao tác vuốt
                android.view.View itemView = viewHolder.itemView;
                android.graphics.Paint paint = new android.graphics.Paint();
                int height = itemView.getBottom() - itemView.getTop();
                int width = 200; // chiều rộng khung hành động

                if (dX < 0) { // vuốt trái – xoá
                    paint.setColor(androidx.core.content.ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark));
                    android.graphics.RectF background = new android.graphics.RectF(
                            itemView.getRight() + dX, itemView.getTop(),
                            itemView.getRight(), itemView.getBottom());
                    c.drawRect(background, paint);

                    paint.setColor(android.graphics.Color.WHITE);
                    paint.setTextSize(40);
                    paint.setTextAlign(android.graphics.Paint.Align.CENTER);
                    float textX = itemView.getRight() - width / 2f;
                    float textY = itemView.getTop() + height / 2f - ((paint.descent() + paint.ascent()) / 2);
                    c.drawText("Xoá", textX, textY, paint);

                } else if (dX > 0) { // vuốt phải – lưu
                    paint.setColor(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.primary1));
                    android.graphics.RectF background = new android.graphics.RectF(
                            itemView.getLeft(), itemView.getTop(),
                            itemView.getLeft() + dX, itemView.getBottom());
                    c.drawRect(background, paint);

                    paint.setColor(android.graphics.Color.WHITE);
                    paint.setTextSize(40);
                    paint.setTextAlign(android.graphics.Paint.Align.CENTER);
                    float textX = itemView.getLeft() + width / 2f;
                    float textY = itemView.getTop() + height / 2f - ((paint.descent() + paint.ascent()) / 2);
                    c.drawText("Lưu", textX, textY, paint);
                }
            }
        });
        swipeHelper.attachToRecyclerView(recyclerView);

        // Button "Xem sản phẩm" trong layout_empty_cart
        View btnViewProducts = emptyView.findViewById(R.id.btnViewProducts);
        if(btnViewProducts!=null){
            btnViewProducts.setOnClickListener(v->{
                // Mở trang Nguyên liệu (ProductActivity với bottom nav ingredients)
                android.content.Intent it = new android.content.Intent(requireContext(), com.example.bepnhataapp.features.products.ProductActivity.class);
                startActivity(it);
            });
        }

        // empty cart
//        updateCart(new ArrayList<>()); // test empty, replace with real data

        updateCart(com.example.bepnhataapp.common.utils.CartHelper.loadItems(requireContext()));

        if (btnBuyNow != null) {
            btnBuyNow.setOnClickListener(v -> {
                java.util.ArrayList<com.example.bepnhataapp.common.model.CartItem> selected = new java.util.ArrayList<>();
                for (com.example.bepnhataapp.common.model.CartItem ci : adapter.getItems()) {
                    if (ci.isSelected()) selected.add(ci);
                }
                if (selected.isEmpty()) {
                    android.widget.Toast.makeText(requireContext(), "Vui lòng chọn sản phẩm để mua", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }
                android.content.Intent intent = new android.content.Intent(requireContext(), com.example.bepnhataapp.features.checkout.CheckoutActivity.class);
                intent.putExtra("selected_items", selected);
                startActivity(intent);
            });
        }

        txtChange.setOnClickListener(v -> {
            isEditMode = !isEditMode;
            if (isEditMode) {
                if (btnBuyNow != null) btnBuyNow.setVisibility(View.GONE);
                if (layoutBottomBar != null) {
                    // hide entire default footer container
                    View defaultFooter = (View) layoutBottomBar.getParent();
                    if (defaultFooter != null) defaultFooter.setVisibility(View.GONE);
                }
                if (layoutEditFooter != null) layoutEditFooter.setVisibility(View.VISIBLE);
                txtChange.setText("Xong");
            } else {
                if (btnBuyNow != null) btnBuyNow.setVisibility(View.VISIBLE);
                if (layoutBottomBar != null) {
                    View defaultFooter = (View) layoutBottomBar.getParent();
                    if (defaultFooter != null) defaultFooter.setVisibility(View.VISIBLE);
                }
                if (layoutEditFooter != null) layoutEditFooter.setVisibility(View.GONE);
                txtChange.setText("Sửa");
            }
            adapter.setEditMode(isEditMode);
        });

        // Header controls
        TextView tvTitle = view.findViewById(R.id.txtContent);
        if(tvTitle!=null) tvTitle.setText("Giỏ hàng");

        java.util.function.Consumer<Boolean> applySelectAll = checked -> {
            for(CartItem ci:adapter.getItems()) ci.setSelected(checked);
            adapter.notifyDataSetChanged();
            recalculateBottom();
        };

        if(cbSelectAll!=null){
            cbSelectAll.setChecked(false);
            cbSelectAll.setOnCheckedChangeListener((b,checked)-> applySelectAll.accept(checked));
        }
        // checkbox inside edit footer
        CheckBox cbSelectAllEdit = layoutEditFooter.findViewById(R.id.checkboxSelectAll);
        if(cbSelectAllEdit!=null){
            cbSelectAllEdit.setChecked(false);
            cbSelectAllEdit.setOnCheckedChangeListener((b,checked)-> applySelectAll.accept(checked));
        }

        // Xử lý nút Xoá trong footer chỉnh sửa
        if (btnDelete != null) {
            btnDelete.setOnClickListener(v -> {
                java.util.List<CartItem> sel = new java.util.ArrayList<>();
                for (CartItem ci : adapter.getItems()) {
                    if (ci.isSelected()) sel.add(ci);
                }
                if(sel.isEmpty()){
                    android.widget.Toast.makeText(requireContext(), "Vui lòng chọn sản phẩm để xoá", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }

                int count = sel.size();
                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("Xác nhận xoá")
                        .setMessage("Bạn có chắc muốn xoá " + count + " sản phẩm khỏi giỏ hàng?")
                        .setPositiveButton("Xoá", (d, which) -> {
                            com.example.bepnhataapp.common.utils.CartHelper.removeProducts(requireContext(), sel);
                            updateCart(com.example.bepnhataapp.common.utils.CartHelper.loadItems(requireContext()));
                            android.widget.Toast.makeText(requireContext(), "Đã xoá " + count + " sản phẩm", android.widget.Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Huỷ", null)
                        .show();
            });
        }

        // Xử lý nút "Lưu vào yêu thích" trong footer chỉnh sửa
        if (btnFavorite != null) {
            btnFavorite.setOnClickListener(v -> {
                java.util.List<CartItem> sel = new java.util.ArrayList<>();
                for (CartItem ci : adapter.getItems()) {
                    if (ci.isSelected()) sel.add(ci);
                }
                if(sel.isEmpty()){
                    android.widget.Toast.makeText(requireContext(), "Vui lòng chọn sản phẩm để lưu", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra đăng nhập
                if(!com.example.bepnhataapp.common.utils.SessionManager.isLoggedIn(requireContext())){
                    android.widget.Toast.makeText(requireContext(), "Vui lòng đăng nhập để sử dụng", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }

                String phone = com.example.bepnhataapp.common.utils.SessionManager.getPhone(requireContext());
                com.example.bepnhataapp.common.dao.CustomerDao cusDao = new com.example.bepnhataapp.common.dao.CustomerDao(requireContext());
                com.example.bepnhataapp.common.model.Customer cus = cusDao.findByPhone(phone);
                if(cus==null){
                    android.widget.Toast.makeText(requireContext(), "Không tìm thấy thông tin người dùng", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }

                com.example.bepnhataapp.common.dao.FavouriteProductDao favDao = new com.example.bepnhataapp.common.dao.FavouriteProductDao(requireContext());
                int added = 0;
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
                String now = sdf.format(new java.util.Date());
                for(CartItem ci: sel){
                    if(!favDao.isFavourite(ci.getProductId(), cus.getCustomerID())){
                        favDao.insert(new com.example.bepnhataapp.common.model.FavouriteProduct(ci.getProductId(), cus.getCustomerID(), now));
                        added++;
                    }
                }

                if(added>0){
                    android.widget.Toast.makeText(requireContext(), "Đã lưu " + added + " sản phẩm vào Yêu thích", android.widget.Toast.LENGTH_SHORT).show();
                } else {
                    android.widget.Toast.makeText(requireContext(), "Sản phẩm đã có trong Yêu thích", android.widget.Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateCart(com.example.bepnhataapp.common.utils.CartHelper.loadItems(requireContext()));
    }

    private void recalculateBottom(){
        int totalNew=0, save=0;
        for(com.example.bepnhataapp.common.model.CartItem it: adapter.getItems()){
            if(it.isSelected()){
                totalNew+=it.getTotal();
                save+=it.getTotalSave();
            }
        }
        NumberFormat nf=NumberFormat.getInstance(new Locale("vi","VN"));
        if(tvTotal!=null) tvTotal.setText(nf.format(totalNew)+"đ");
        if(tvDiscount!=null) tvDiscount.setText("-"+nf.format(save)+"đ");
        boolean allSelected=true;
        for(CartItem ci:adapter.getItems()) if(!ci.isSelected()){allSelected=false;break;}
        if(cbSelectAll!=null) cbSelectAll.setChecked(allSelected);
        CheckBox cbSelectAllEdit = layoutEditFooter.findViewById(R.id.checkboxSelectAll);
        if(cbSelectAllEdit!=null) cbSelectAllEdit.setChecked(allSelected);
    }

    public void updateCart(List<CartItem> items) {
        adapter.setItems(items);
        recalculateBottom();
        if (items == null || items.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            if (btnBuyNow != null) {
                btnBuyNow.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.primary3));
                btnBuyNow.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.primary1));
                btnBuyNow.setEnabled(false);
            }
            if(txtChange!=null) txtChange.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            if (btnBuyNow != null) {
                btnBuyNow.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.primary1));
                btnBuyNow.setEnabled(true);
            }
            if(txtChange!=null) txtChange.setVisibility(View.VISIBLE);
        }
    }
} 
