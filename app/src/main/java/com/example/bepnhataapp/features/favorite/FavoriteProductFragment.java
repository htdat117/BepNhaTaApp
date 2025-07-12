package com.example.bepnhataapp.features.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.adapter.ProductAdapter;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.dao.FavouriteProductDao;
import com.example.bepnhataapp.common.dao.ProductDao;
import com.example.bepnhataapp.common.model.Customer;
import com.example.bepnhataapp.common.model.FavouriteProduct;
import com.example.bepnhataapp.common.model.Product;
import com.example.bepnhataapp.common.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class FavoriteProductFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);
        RecyclerView rv = view.findViewById(R.id.recyclerViewFavorites);
        rv.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getContext()));
        loadDataAsync(rv);
        return view;
    }

    private void loadDataAsync(RecyclerView rv) {
        new Thread(() -> {
            List<Product> list = new ArrayList<>();
            if (getContext() != null && SessionManager.isLoggedIn(getContext())) {
                String phone = SessionManager.getPhone(getContext());
                Customer c = new CustomerDao(getContext()).findByPhone(phone);
                if (c != null) {
                    long customerId = c.getCustomerID();
                    FavouriteProductDao favDao = new FavouriteProductDao(getContext());
                    List<FavouriteProduct> favs = favDao.getByCustomer(customerId);
                    ProductDao pDao = new ProductDao(getContext());
                    for (FavouriteProduct fp : favs) {
                        Product p = pDao.getById(fp.getProductID());
                        if (p != null) list.add(p);
                    }
                }
            }
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> rv.setAdapter(new ProductAdapter(getContext(), list)));
            }
        }).start();
    }
} 
