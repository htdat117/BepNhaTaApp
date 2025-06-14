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

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private View emptyView;
    private CartAdapter adapter;
    private Button btnBuyNow;
    private View layoutEditFooter;
    private Button btnDelete, btnFavorite;
    private TextView txtChange;
    private boolean isEditMode = false;
    private View layoutBottomBar;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // empty cart
//        updateCart(new ArrayList<>()); // test empty, replace with real data

        // demo data
         List<CartItem> demoItems = new ArrayList<>();
         demoItems.add(new CartItem("Gói nguyên liệu Bò kho", "90.000đ", "2 người"));
         demoItems.add(new CartItem("Gói nguyên liệu Bò kho", "90.000đ", "2 người"));
         demoItems.add(new CartItem("Gói nguyên liệu Bò kho", "90.000đ", "2 người"));
         updateCart(demoItems);

         if (btnBuyNow != null) {
             btnBuyNow.setOnClickListener(v -> {
                 android.content.Intent intent = new android.content.Intent(requireContext(), com.example.bepnhataapp.features.checkout.CheckoutActivity.class);
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
                 adapter.setEditMode(true); // show checkbox
             } else {
                 if (btnBuyNow != null) btnBuyNow.setVisibility(View.VISIBLE);
                 if (layoutBottomBar != null) {
                     View defaultFooter = (View) layoutBottomBar.getParent();
                     if (defaultFooter != null) defaultFooter.setVisibility(View.VISIBLE);
                 }
                 if (layoutEditFooter != null) layoutEditFooter.setVisibility(View.GONE);
                 txtChange.setText("Sửa");
                 adapter.setEditMode(false); // hide checkbox
             }
         });

         return view;
    }

    public void updateCart(List<CartItem> items) {
        if (items == null || items.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            if (btnBuyNow != null) {
                btnBuyNow.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.primary3));
                btnBuyNow.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.primary1));
                btnBuyNow.setEnabled(false);
            }
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            adapter.setItems(items);
            if (btnBuyNow != null) {
                btnBuyNow.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.primary1));
                btnBuyNow.setEnabled(true);
            }
        }
    }
} 