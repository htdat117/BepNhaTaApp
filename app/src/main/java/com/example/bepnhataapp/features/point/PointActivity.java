package com.example.bepnhataapp.features.point;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.model.Customer;
import com.example.bepnhataapp.common.utils.SessionManager;

public class PointActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish(); // Đóng Activity hiện tại
                }
            });
        }

        TextView tvHistory = findViewById(R.id.tvHistory);
        if (tvHistory != null) {
            tvHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PointActivity.this, PointHistoryActivity.class);
                    startActivity(intent);
                }
            });
        }

        AppCompatButton btnExchange = findViewById(R.id.btnExchange);
        if (btnExchange != null) {
            btnExchange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PointActivity.this, com.example.bepnhataapp.features.voucher.VoucherActivity.class);
                    startActivity(intent);
                }
            });
        }

        // Hiển thị điểm thành viên thực tế
        TextView tvRankPoint = findViewById(R.id.tvRankPoint);
        TextView tvTotalPoint = findViewById(R.id.tvTotalPoint);
        int point = 0;
        String phone = SessionManager.getPhone(this);
        if (phone != null) {
            CustomerDao dao = new CustomerDao(this);
            Customer customer = dao.findByPhone(phone);
            if (customer != null) {
                point = customer.getLoyaltyPoint();
            }
        }
        if (tvRankPoint != null) {
            tvRankPoint.setText(point + "/499");
        }
        if (tvTotalPoint != null) {
            tvTotalPoint.setText("Tổng điểm có thể dùng: " + point);
        }

        // Hiển thị layout động viên nếu chưa có điểm
        View layoutNoPoint = findViewById(R.id.layoutNoPoint);
        if (layoutNoPoint != null) {
            if (point == 0) {
                layoutNoPoint.setVisibility(View.VISIBLE);
            } else {
                layoutNoPoint.setVisibility(View.GONE);
            }
        }

        TextView tvRankName = findViewById(R.id.tvRankName);
        TextView tvRankNote = findViewById(R.id.tvRankNote);
        ProgressBar progressRank = findViewById(R.id.progressRank);
        int maxPoint = 499;
        String rankName = "Tân thủ";
        String rankNote = "Cần tích lũy thêm " + (500 - point) + " điểm nữa để nâng lên Thực tập bếp";
        int progress = (int) ((point * 100.0f) / 499);
        if (point >= 500 && point < 1500) {
            rankName = "Thực tập bếp";
            maxPoint = 1499;
            rankNote = "Cần tích lũy thêm " + (1500 - point) + " điểm nữa để nâng lên Đầu Bếp Nhà";
            progress = (int) (((point - 500) * 100.0f) / (1499 - 500));
        } else if (point >= 1500 && point < 7501) {
            rankName = "Đầu Bếp Nhà";
            maxPoint = 7500;
            rankNote = "Cần tích lũy thêm " + (7501 - point) + " điểm nữa để nâng lên Bếp Trưởng";
            progress = (int) (((point - 1500) * 100.0f) / (7500 - 1500));
        } else if (point >= 7501 && point < 14000) {
            rankName = "Bếp Trưởng";
            maxPoint = 13999;
            rankNote = "Cần tích lũy thêm " + (14000 - point) + " điểm nữa để nâng lên Đại Sứ Bếp Nhà Ta";
            progress = (int) (((point - 7501) * 100.0f) / (13999 - 7501));
        } else if (point >= 14000) {
            rankName = "Đại Sứ Bếp Nhà Ta";
            maxPoint = point; // Đã max cấp, thanh luôn đầy
            rankNote = "Bạn đã đạt cấp bậc cao nhất!";
            progress = 100;
        }
        if (tvRankName != null) tvRankName.setText(rankName);
        if (tvRankNote != null) tvRankNote.setText(rankNote);
        if (tvRankPoint != null) tvRankPoint.setText(point + "/" + maxPoint);
        if (progressRank != null) progressRank.setProgress(progress);
    }
} 