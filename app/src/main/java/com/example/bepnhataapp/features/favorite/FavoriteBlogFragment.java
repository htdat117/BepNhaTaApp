package com.example.bepnhataapp.features.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.adapter.BlogAdapter;
import com.example.bepnhataapp.common.dao.BlogDao;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.dao.FavouriteBlogDao;
import com.example.bepnhataapp.common.model.BlogEntity;
import com.example.bepnhataapp.common.model.Customer;
import com.example.bepnhataapp.common.model.FavouriteBlog;
import com.example.bepnhataapp.common.model.Blog;
import com.example.bepnhataapp.common.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class FavoriteBlogFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);
        RecyclerView rv = view.findViewById(R.id.recyclerViewFavorites);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        loadDataAsync(rv);
        return view;
    }

    private void loadDataAsync(RecyclerView rv) {
        new Thread(() -> {
            List<Blog> list = new ArrayList<>();
            if (getContext() != null && SessionManager.isLoggedIn(getContext())) {
                String phone = SessionManager.getPhone(getContext());
                Customer c = new CustomerDao(getContext()).findByPhone(phone);
                if (c != null) {
                    long customerId = c.getCustomerID();
                    FavouriteBlogDao favDao = new FavouriteBlogDao(getContext());
                    List<FavouriteBlog> favs = favDao.getByCustomer(customerId);
                    BlogDao bDao = new BlogDao(getContext());
                    for (FavouriteBlog fb : favs) {
                        BlogEntity be = bDao.get(fb.getBlogID());
                        if (be != null) {
                            list.add(new Blog(be.getBlogID(), be.getTitle(), be.getContent(), be.getTag(), be.getImageThumb(), true, be.getCreatedAt(), 0, 0));
                        }
                    }
                }
            }
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> rv.setAdapter(new BlogAdapter(list)));
            }
        }).start();
    }
} 
