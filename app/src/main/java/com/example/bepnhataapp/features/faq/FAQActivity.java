package com.example.bepnhataapp.features.faq;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bepnhataapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Hiển thị danh sách FAQ đơn giản, giao diện được xây dựng thuần LinearLayout.
 * Tiêu đề + thanh danh mục cố định, danh sách câu hỏi đặt trong ScrollView có thể cuộn.
 */
public class FAQActivity extends AppCompatActivity {

    private final String[] categories = {"Tất cả", "Tài khoản", "Đặt hàng", "Thanh toán","Sản phẩm", "Vận chuyển", "Hoàn trả" };

    private static class Faq {
        String category;
        String question;
        String answer;
        Faq(String category, String question, String answer) {
            this.category = category;
            this.question = question;
            this.answer = answer;
        }
    }

    private final List<Faq> faqList = new ArrayList<>();

    private String currentCategory = "Tất cả";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        // Back button action
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        buildCategoryTabs();
        seedSampleData();
        renderQuestionList();
    }

    /**
     * Tạo các tab danh mục bằng TextView động
     */
    private void buildCategoryTabs() {
        LinearLayout llCategory = findViewById(R.id.llCategory);
        
        for (int i = 0; i < categories.length; i++) {
            final TextView tv = new TextView(this);
            tv.setText(categories[i]);
            tv.setTextSize(16);
            tv.setPadding(24, 16, 24, 16);
            tv.setBackgroundResource(R.drawable.bg_tab_unselected);
            tv.setTextColor(Color.BLACK);

            if (i == 0) {
                tv.setSelected(true);
                tv.setBackgroundResource(R.drawable.bg_tab_selected);
                tv.setTextColor(Color.WHITE);
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_checked, 0, 0, 0);
                tv.setCompoundDrawablePadding(8);
                tv.setTypeface(Typeface.DEFAULT_BOLD);
            }

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 16, 0);
            tv.setLayoutParams(lp);

            int index = i;
            tv.setOnClickListener(v -> {
                for (int j = 0; j < llCategory.getChildCount(); j++) {
                    TextView child = (TextView) llCategory.getChildAt(j);
                    boolean selected = j == index;
                    child.setSelected(selected);
                    child.setBackgroundResource(selected ? R.drawable.bg_tab_selected : R.drawable.bg_tab_unselected);
                    child.setTextColor(selected ? Color.WHITE : Color.BLACK);
                    child.setTypeface(selected ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
                    if (selected) {
                        child.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_checked, 0, 0, 0);
                        child.setCompoundDrawablePadding(8);
                    } else {
                        child.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                }
                currentCategory = categories[index];
                renderQuestionList();
            });

            llCategory.addView(tv);
        }
    }

    private void seedSampleData() {
        // Tài khoản
        faqList.add(new Faq("Tài khoản", "Tôi quên mật khẩu, phải làm sao?", "Chọn \"Quên mật khẩu\" tại màn đăng nhập và làm theo hướng dẫn để đặt lại mật khẩu."));
        faqList.add(new Faq("Tài khoản", "Làm sao thay đổi thông tin cá nhân?", "Vào mục Tài khoản > Thông tin cá nhân để chỉnh sửa tên, số điện thoại, địa chỉ ..."));

        // Đặt hàng
        faqList.add(new Faq("Đặt hàng", "Có thể hủy đơn sau khi đặt không?", "Bạn có thể hủy miễn phí trong vòng 30 phút sau khi đặt hàng."));

        // Thanh toán
        faqList.add(new Faq("Thanh toán", "Những phương thức thanh toán nào được hỗ trợ?", "Chúng tôi hỗ trợ thẻ tín dụng, chuyển khoản và COD."));
        faqList.add(new Faq("Thanh toán", "Tôi có thể xuất hoá đơn VAT không?", "Có, vui lòng điền thông tin công ty ở bước thanh toán."));

        // Sản phẩm
        faqList.add(new Faq("Sản phẩm", "Nguồn gốc nguyên liệu?", "Nguyên liệu được nhập trực tiếp từ các nhà cung cấp đạt chuẩn VietGAP."));

        // Vận chuyển
        faqList.add(new Faq("Vận chuyển", "Phí vận chuyển được tính như thế nào?", "Phí phụ thuộc vào trọng lượng và địa chỉ giao hàng, sẽ hiển thị ở bước thanh toán."));

        // Hoàn trả
        faqList.add(new Faq("Hoàn trả", "Thời gian hoàn tiền bao lâu?", "Hoàn tiền về tài khoản ngân hàng trong 3-7 ngày làm việc."));
    }

    private void renderQuestionList() {
        LinearLayout llQuestions = findViewById(R.id.llQuestions);
        llQuestions.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Faq faq : faqList) {
            if (!currentCategory.equals("Tất cả") && !faq.category.equals(currentCategory)) continue;

            View item = inflater.inflate(R.layout.item_faq, llQuestions, false);
            TextView tvQuestion = item.findViewById(R.id.tvQuestion);
            TextView tvAnswer = item.findViewById(R.id.tvAnswer);
            ImageView ivArrow = item.findViewById(R.id.ivArrow);

            tvQuestion.setText(faq.question);
            tvAnswer.setText(faq.answer);

            item.setOnClickListener(v -> {
                boolean expanded = tvAnswer.getVisibility() == View.VISIBLE;
                tvAnswer.setVisibility(expanded ? View.GONE : View.VISIBLE);
                ivArrow.setRotation(expanded ? 0f : 90f);
            });

            llQuestions.addView(item);
        }
    }
} 