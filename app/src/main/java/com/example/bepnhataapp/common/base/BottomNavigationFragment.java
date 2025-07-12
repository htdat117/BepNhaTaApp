package com.example.bepnhataapp.common.base;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bepnhataapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationFragment extends Fragment {

    private static final String TAG = "BottomNavFragment";
    private BaseActivity.OnNavigationItemReselectedListener mListener;
    private int initialSelectedItemId;

    public BottomNavigationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity.OnNavigationItemReselectedListener) {
            mListener = (BaseActivity.OnNavigationItemReselectedListener) context;
            Log.d(TAG, "onAttach: Listener attached successfully.");
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNavigationItemReselectedListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            initialSelectedItemId = getArguments().getInt("selectedItemId", R.id.nav_home);
            Log.d(TAG, "onCreate: Initial selected item ID: " + initialSelectedItemId);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: Inflating layout.");
        View view = inflater.inflate(R.layout.layout_bottom_navigation, container, false);
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);

        if (bottomNavigationView == null) {
            Log.e(TAG, "onCreateView: BottomNavigationView not found with ID R.id.bottom_navigation");
            return view;
        }

        // Set the initial selected item without triggering callback
        bottomNavigationView.setSelectedItemId(initialSelectedItemId);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == initialSelectedItemId && getParentFragmentManager().isStateSaved()) {
                // Ignore first automatic selection
                return true;
            }
            if (mListener != null) {
                mListener.onNavigationItemReselected(item.getItemId());
            }
            return true;
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.d(TAG, "onDetach: Listener detached.");
    }
} 
