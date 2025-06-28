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
import com.example.bepnhataapp.common.models.CartItem;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartAdapter(new ArrayList<>());
        adapter.setCartListener(this::recalculateBottom);
        recyclerView.setAdapter(adapter);

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
                java.util.ArrayList<com.example.bepnhataapp.common.models.CartItem> selected = new java.util.ArrayList<>();
                for (com.example.bepnhataapp.common.models.CartItem ci : adapter.getItems()) {
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
        ImageView ivBack = view.findViewById(R.id.iv_logo); // back arrow icon in header
        if(ivBack!=null) ivBack.setOnClickListener(v -> requireActivity().finish());

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

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateCart(com.example.bepnhataapp.common.utils.CartHelper.loadItems(requireContext()));
    }

    private void recalculateBottom(){
        int totalNew=0, save=0;
        for(com.example.bepnhataapp.common.models.CartItem it: adapter.getItems()){
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