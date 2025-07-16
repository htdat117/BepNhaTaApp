package com.example.bepnhataapp.features.mealplan.steps;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.features.mealplan.MealPlanContentActivity;
import com.example.bepnhataapp.features.mealplan.steps.CustomerHealthViewModel;
import com.example.bepnhataapp.common.dao.CustomerHealthDao;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.utils.SessionManager;
import androidx.lifecycle.ViewModelProvider;
import android.widget.Toast;

public class StepSummaryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mealplan_summary, container, false);

        // Setup show menu button
        Button btnShowMenu = view.findViewById(R.id.btnShowMenu);
        btnShowMenu.setOnClickListener(v -> {
            if(getActivity()==null) return;

            CustomerHealthViewModel vm = new ViewModelProvider(requireActivity()).get(CustomerHealthViewModel.class);
            // get customerID if logged in
            long cid=-1;
            if(SessionManager.isLoggedIn(getContext())){
                String phone=SessionManager.getPhone(getContext());
                com.example.bepnhataapp.common.model.Customer c=new CustomerDao(getContext()).findByPhone(phone);
                if(c!=null) cid=c.getCustomerID();
            }
            vm.draft.setCustomerID(cid);
            CustomerHealthDao dao=new CustomerHealthDao(getContext());
            if(cid!=-1){
                com.example.bepnhataapp.common.model.CustomerHealth existing=dao.findByCustomer(cid);
                if(existing==null) dao.insert(vm.draft); else {vm.draft.setCustomerHealthID(existing.getCustomerHealthID()); dao.update(vm.draft);} }

            Toast.makeText(getContext(),"Đã lưu thông tin sức khỏe",Toast.LENGTH_SHORT).show();

            // Tự động sinh thực đơn cho tuần hiện tại (thứ 2 → CN)
            new Thread(() -> {
                com.example.bepnhataapp.common.repository.LocalMealPlanRepository repo =
                        new com.example.bepnhataapp.common.repository.LocalMealPlanRepository(requireContext());

                java.time.LocalDate monday = java.time.LocalDate.now().with(java.time.DayOfWeek.MONDAY);
                for (int i = 0; i < 7; i++) {
                    repo.generateWeekPlan(monday.plusDays(i));
                }
            }).start();

            // Đánh dấu đã hoàn thành khảo sát
            SharedPreferences prefs = requireContext().getSharedPreferences("MealPlanPrefs", android.content.Context.MODE_PRIVATE);
            prefs.edit().putBoolean("onboarding_completed", true).apply();

            Intent intent = new Intent(getActivity(), MealPlanContentActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        // Setup close button
        ImageButton btnClose = view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        return view;
    }
} 
