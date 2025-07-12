package com.example.bepnhataapp.features.voucher;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bepnhataapp.R;

public class VoucherDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_detail);

        // Xử lý nút quay lại
        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Nhận dữ liệu từ Intent, có giá trị mặc định nếu null
        String code = getIntent().getStringExtra("code");
        String desc = getIntent().getStringExtra("desc");
        // String point = getIntent().getStringExtra("point"); // Không còn sử dụng
        String expire = getIntent().getStringExtra("expire"); // Thay thế cho 'time' nếu cần
        String time = "Từ ngày 01/04/2025 đến 30/04/2025. Áp dụng trong khung giờ 10:00 – 22:00 mỗi ngày."; // Dữ liệu mẫu
        String benefit = "Giảm ngay 20% tổng giá trị đơn hàng, tối đa 60.000đ cho mỗi đơn hàng."; // Dữ liệu mẫu
        String product = "Tất cả các món ăn có mặt trên ứng dụng Bếp Nhà Ta, Không áp dụng cho combo \"Đặc biệt trong tuần\" hoặc món đã giảm giá."; // Dữ liệu mẫu
        String payment = "Áp dụng khi thanh toán qua:\n• Ví Momo, ZaloPay, ShopeePay\n• Thẻ ngân hàng (ATM, Visa/Master)\n• Không áp dụng với thanh toán khi nhận hàng (COD)"; // Dữ liệu mẫu
        String shipping = "Áp dụng với mọi đơn vị vận chuyển đối tác trong khu vực hỗ trợ của Bếp Nhà Ta (GrabExpress, AhaMove, Giao Hàng Nhanh).\nKhông áp dụng cho các đơn tự đến lấy."; // Dữ liệu mẫu
        String device = "Chỉ áp dụng khi đặt hàng thông qua app Bếp Nhà Ta trên điện thoại (iOS/Android). Không áp dụng trên website."; // Dữ liệu mẫu
        String condition = "• Mỗi tài khoản chỉ được sử dụng 1 lần.\n• Không áp dụng đồng thời với các mã giảm giá khác.\n• Mã có thể hết lượt sử dụng sớm nếu đủ số lượng.\n• Bếp Nhà Ta có quyền từ chối đơn hàng nếu phát hiện hành vi gian lận hoặc sử dụng mã không đúng mục đích."; // Dữ liệu mẫu

        // Gán dữ liệu cho view, kiểm tra null trước khi setText
        TextView tvCode = findViewById(R.id.tvVoucherCode);
        if (tvCode != null) tvCode.setText(code != null ? code : "");
        TextView tvDesc = findViewById(R.id.tvVoucherDesc);
        if (tvDesc != null) tvDesc.setText(desc != null ? desc : "");
        // TextView tvPoint = findViewById(R.id.tvVoucherPoint); // Không còn sử dụng
        // if (tvPoint != null) tvPoint.setText(point != null ? point : ""); // Không còn sử dụng

        TextView tvTime = findViewById(R.id.tvVoucherTime);
        if (tvTime != null) tvTime.setText(time != null ? time : "");
        TextView tvBenefit = findViewById(R.id.tvVoucherBenefit);
        if (tvBenefit != null) tvBenefit.setText(benefit != null ? benefit : "");
        TextView tvProduct = findViewById(R.id.tvVoucherProduct);
        if (tvProduct != null) tvProduct.setText(product != null ? product : "");
        TextView tvPayment = findViewById(R.id.tvVoucherPayment);
        if (tvPayment != null) tvPayment.setText(payment != null ? payment : "");
        TextView tvShipping = findViewById(R.id.tvVoucherShipping);
        if (tvShipping != null) tvShipping.setText(shipping != null ? shipping : "");
        TextView tvDevice = findViewById(R.id.tvVoucherDevice);
        if (tvDevice != null) tvDevice.setText(device != null ? device : "");
        TextView tvCondition = findViewById(R.id.tvVoucherCondition);
        if (tvCondition != null) tvCondition.setText(condition != null ? condition : "");

        Button btnUseVoucher = findViewById(R.id.btnUseVoucher);
        if (btnUseVoucher != null) {
            btnUseVoucher.setOnClickListener(v ->
                Toast.makeText(this, "Bạn đã nhấp vào Sử dụng voucher!", Toast.LENGTH_SHORT).show()
            );
        }
    }
} 
