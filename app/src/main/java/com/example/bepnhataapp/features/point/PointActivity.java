package com.example.bepnhataapp.features.point;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.model.Customer;
import com.example.bepnhataapp.common.utils.SessionManager;
import com.example.bepnhataapp.features.voucher.VoucherDisplayAdapter;
import com.example.bepnhataapp.features.voucher.VoucherItem;
import com.example.bepnhataapp.common.dao.CouponDao;
import com.example.bepnhataapp.common.model.Coupon;
import com.example.bepnhataapp.common.dao.PointLogDao;
import com.example.bepnhataapp.common.model.PointLog;
import java.util.ArrayList;
import java.util.List;

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
        ImageView imgRank = findViewById(R.id.imgRank); // Thêm dòng này để lấy ImageView icon bậc
        int maxPoint = 499;
        String rankName = "Tân thủ";
        String rankNote = "Cần tích lũy thêm " + (500 - point) + " điểm nữa để nâng lên Thực tập bếp";
        int progress = (int) ((point * 100.0f) / 499);
        int iconRes = R.drawable.ic_rank_newbie; // Mặc định icon bậc Tân thủ
        if (point >= 500 && point < 1500) {
            rankName = "Thực tập bếp";
            maxPoint = 1499;
            rankNote = "Cần tích lũy thêm " + (1500 - point) + " điểm nữa để nâng lên Đầu Bếp Nhà";
            progress = (int) (((point - 500) * 100.0f) / (1499 - 500));
            iconRes = R.drawable.ic_rank_trainee;
        } else if (point >= 1500 && point < 7501) {
            rankName = "Đầu Bếp Nhà";
            maxPoint = 7500;
            rankNote = "Cần tích lũy thêm " + (7501 - point) + " điểm nữa để nâng lên Bếp Trưởng";
            progress = (int) (((point - 1500) * 100.0f) / (7500 - 1500));
            iconRes = R.drawable.ic_rank_chef;
        } else if (point >= 7501 && point < 14000) {
            rankName = "Bếp Trưởng";
            maxPoint = 13999;
            rankNote = "Cần tích lũy thêm " + (14000 - point) + " điểm nữa để nâng lên Đại Sứ Bếp Nhà Ta";
            progress = (int) (((point - 7501) * 100.0f) / (13999 - 7501));
            iconRes = R.drawable.ic_rank_masterchef;
        } else if (point >= 14000) {
            rankName = "Đại Sứ Bếp Nhà Ta";
            maxPoint = point; // Đã max cấp, thanh luôn đầy
            rankNote = "Bạn đã đạt cấp bậc cao nhất!";
            progress = 100;
            iconRes = R.drawable.ic_rank_ambassador;
        }
        if (tvRankName != null) tvRankName.setText(rankName);
        if (tvRankNote != null) tvRankNote.setText(rankNote);
        if (tvRankPoint != null) tvRankPoint.setText(point + "/" + maxPoint);
        if (progressRank != null) progressRank.setProgress(progress);
        if (imgRank != null) imgRank.setImageResource(iconRes);

        // Hiển thị quà tặng hấp dẫn (voucher)
        RecyclerView rvGifts = findViewById(R.id.rvGifts);
        if (rvGifts != null) {
            rvGifts.setLayoutManager(new LinearLayoutManager(this));
            java.util.List<VoucherItem> voucherList = new java.util.ArrayList<>();
            String phoneGift = SessionManager.getPhone(this);
            long customerIdGift = -1;
            int userPointGift = 0;
            if (phoneGift != null) {
                CustomerDao customerDao = new CustomerDao(this);
                Customer customer = customerDao.findByPhone(phoneGift);
                if (customer != null) {
                    customerIdGift = customer.getCustomerID();
                    userPointGift = customer.getLoyaltyPoint();
                }
            }
            CouponDao couponDao = new CouponDao(this);
            java.util.List<Coupon> coupons = couponDao.getAvailableForCustomer(customerIdGift, 0);
            for (Coupon c : coupons) {
                voucherList.add(new VoucherItem(
                    c.getCouponTitle(),
                    (c.getCouponValue()<=100? ("Giảm "+c.getCouponValue()+"%") : ("Giảm "+java.text.NumberFormat.getInstance().format(c.getCouponValue())+"đ")) +
                            (c.getMaxDiscount()!=null? (" tối đa "+java.text.NumberFormat.getInstance().format(c.getMaxDiscount())+"đ") : "") +
                            ", đơn tối thiểu "+java.text.NumberFormat.getInstance().format(c.getMinPrice())+"đ",
                    c.getExpireDate(),
                    true,
                    36 // điểm cần để đổi voucher, bạn có thể sửa lại cho phù hợp
                ));
            }
            VoucherDisplayAdapter adapter = new VoucherDisplayAdapter(voucherList, this, userPointGift);
            rvGifts.setAdapter(adapter);
            rvGifts.setVisibility(voucherList.isEmpty() ? View.GONE : View.VISIBLE);
        }

        // Hiển thị lịch sử điểm
        RecyclerView rvPointHistory = findViewById(R.id.rvPointHistory);
        if (rvPointHistory != null) {
            rvPointHistory.setLayoutManager(new LinearLayoutManager(this));
            List<PointLog> logs = new ArrayList<>();
            long customerId = -1;
            if (phone != null) {
                CustomerDao dao = new CustomerDao(this);
                Customer customer = dao.findByPhone(phone);
                if (customer != null) {
                    customerId = customer.getCustomerID();
                }
            }
            if (customerId > 0) {
                PointLogDao logDao = new PointLogDao(this);
                logs = logDao.getByCustomer(customerId);
            }
            List<PointHistoryAdapter.PointHistoryItem> items = new ArrayList<>();
            for (PointLog log : logs) {
                String title = log.getAction();
                String desc = log.getDescription();
                int logPoint = log.getPoint();
                items.add(new PointHistoryAdapter.PointHistoryItem(title, desc, logPoint));
            }
            PointHistoryAdapter adapter = new PointHistoryAdapter(items);
            rvPointHistory.setAdapter(adapter);
        }
    }
} 
