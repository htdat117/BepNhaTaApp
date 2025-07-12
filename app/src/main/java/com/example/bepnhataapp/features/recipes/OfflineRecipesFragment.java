package com.example.bepnhataapp.features.recipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.utils.NetworkUtils;
import com.example.bepnhataapp.features.recipes.DownloadedRecipesFragment;
import android.util.Log;

public class OfflineRecipesFragment extends Fragment {

    private static final String TAG = "OfflineRecipesFragment";

    private Button btnViewOffline, btnTryAgain;
    private ImageView imgNoInternet;
    private TextView tvTitleOffline, tvDescOffline;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Creating OfflineRecipesFragment view");
        View view = inflater.inflate(R.layout.activity_offline, container, false);

        // Ánh xạ các View
        imgNoInternet = view.findViewById(R.id.imgNoInternet);
        tvTitleOffline = view.findViewById(R.id.tvTitleOffline);
        tvDescOffline = view.findViewById(R.id.tvDescOffline);
        btnViewOffline = view.findViewById(R.id.btnOffline); // ID của nút trong layout activity_offline
        btnTryAgain = view.findViewById(R.id.btnRetry); // ID của nút trong layout activity_offline

        // Kiểm tra xem tất cả các view đã được tìm thấy chưa
        if (imgNoInternet == null || tvTitleOffline == null || tvDescOffline == null ||
            btnViewOffline == null || btnTryAgain == null) {
            Log.e(TAG, "onCreateView: Some views were not found");
            return view;
        }

        Log.d(TAG, "onCreateView: All views found successfully");

        // Thiết lập sự kiện click cho nút "Xem ngoại tuyến"
        btnViewOffline.setOnClickListener(v -> {
            Log.d(TAG, "btnViewOffline clicked");
            // Chuyển sang DownloadedRecipesFragment
            if (getFragmentManager() != null) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_container, new DownloadedRecipesFragment()); // Thay thế Fragment hiện tại
                transaction.addToBackStack(null); // Thêm vào back stack để có thể quay lại
                transaction.commit();
                Log.d(TAG, "Navigating to DownloadedRecipesFragment");
            } else {
                Log.e(TAG, "FragmentManager is null. Cannot navigate.");
            }
        });

        // Thiết lập sự kiện click cho nút "Nhấn để thử lại"
        btnTryAgain.setOnClickListener(v -> {
            Log.d(TAG, "btnTryAgain clicked");
            checkNetworkAndDisplayContent();
        });

        // Kiểm tra mạng khi Fragment được tạo
        checkNetworkAndDisplayContent();

        return view;
    }

    private void checkNetworkAndDisplayContent() {
        if (getContext() != null && NetworkUtils.isNetworkAvailable(getContext())) {
            Log.d(TAG, "Network is available. Loading main recipes content.");
            // Nếu có mạng, chuyển đến DownloadedRecipesFragment (hoặc Fragment chứa nội dung online)
            if (getFragmentManager() != null) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_container, new DownloadedRecipesFragment()); // Giả định khi có mạng sẽ hiển thịDownloadedRecipesFragment
                transaction.commit();
                Log.d(TAG, "Network available, loaded DownloadedRecipesFragment");
            } else {
                Log.e(TAG, "FragmentManager is null. Cannot load content.");
            }
        } else {
            Log.d(TAG, "Network is NOT available. Displaying offline view.");
            // Giữ nguyên giao diện offline
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Kiểm tra lại trạng thái mạng khi Fragment được hiển thị lại (ví dụ: quay lại từ màn hình khác)
        checkNetworkAndDisplayContent();
    }
} 
