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
        faqList.clear();

        // Tài khoản
        faqList.add(new Faq("Tài khoản", "Tôi có cần đăng ký tài khoản để đặt hàng không?", "Không bắt buộc, nhưng đăng ký giúp bạn quản lý đơn hàng và nhận ưu đãi dễ dàng hơn."));
        faqList.add(new Faq("Tài khoản", "Làm sao để đăng ký tài khoản?", "Bạn nhấn “Đăng ký” ở góc phải màn hình và điền thông tin cơ bản như tên, email, mật khẩu."));
        faqList.add(new Faq("Tài khoản", "Tôi quên mật khẩu, phải làm sao?", "Nhấn “Quên mật khẩu” ở trang đăng nhập, sau đó làm theo hướng dẫn để đặt lại mật khẩu."));
        faqList.add(new Faq("Tài khoản", "Tôi có thể thay đổi thông tin cá nhân không?", "Có. Truy cập “Tài khoản của tôi” để chỉnh sửa thông tin bất kỳ lúc nào."));
        faqList.add(new Faq("Tài khoản", "Tài khoản của tôi có bị xóa nếu lâu không dùng?", "Không. Tài khoản sẽ được lưu trữ trừ khi bạn yêu cầu xóa."));
        faqList.add(new Faq("Tài khoản", "Tôi có thể dùng một tài khoản để đặt hàng cho nhiều người không?", "Có thể. Bạn chỉ cần thay đổi địa chỉ nhận hàng khi đặt đơn."));
        faqList.add(new Faq("Tài khoản", "Làm sao để xem lịch sử đơn hàng?", "Truy cập “Đơn hàng của tôi” trong tài khoản để xem toàn bộ đơn đã mua."));
        faqList.add(new Faq("Tài khoản", "Tài khoản có bảo mật không?", "Chúng tôi sử dụng mã hóa và các biện pháp bảo mật để bảo vệ thông tin cá nhân của bạn."));

        // Đặt hàng
        faqList.add(new Faq("Đặt hàng", "Làm sao để đặt hàng?", "Chọn sản phẩm, nhấn “Thêm vào giỏ”, sau đó “Thanh toán” và điền thông tin cần thiết."));
        faqList.add(new Faq("Đặt hàng", "Tôi có thể đặt nhiều món trong một đơn hàng không?", "Có, bạn có thể thêm nhiều món vào giỏ trước khi thanh toán."));
        faqList.add(new Faq("Đặt hàng", "Làm sao để biết đơn hàng đã được đặt thành công?", "Bạn sẽ nhận email xác nhận và có thể kiểm tra trong mục “Đơn hàng của tôi”."));
        faqList.add(new Faq("Đặt hàng", "Tôi có thể chỉnh sửa đơn hàng sau khi đã đặt không?", "Bạn có thể chỉnh sửa trong vòng 30 phút sau khi đặt nếu đơn chưa được xử lý."));
        faqList.add(new Faq("Đặt hàng", "Tôi có thể huỷ đơn hàng không?", "Có, nếu đơn chưa được đóng gói. Vui lòng liên hệ sớm qua hotline hoặc fanpage."));
        faqList.add(new Faq("Đặt hàng", "Có giới hạn số lượng sản phẩm trong một đơn hàng không?", "Không giới hạn, nhưng với số lượng lớn chúng tôi sẽ xác nhận lại với bạn."));
        faqList.add(new Faq("Đặt hàng", "Tôi có thể đặt hàng trước cho ngày hôm sau không?", "Có. Bạn chọn ngày nhận hàng mong muốn khi thanh toán."));
        faqList.add(new Faq("Đặt hàng", "Tôi có thể đặt hàng qua điện thoại hoặc fanpage không?", "Có thể. Inbox fanpage hoặc gọi hotline để được hỗ trợ đặt hàng thủ công."));

        // Thanh toán
        faqList.add(new Faq("Thanh toán", "Có những hình thức thanh toán nào?", "Chúng tôi hỗ trợ chuyển khoản ngân hàng, ví điện tử (Momo, ZaloPay), và tiền mặt khi nhận hàng (COD)."));
        faqList.add(new Faq("Thanh toán", "Tôi có được xác nhận thanh toán không?", "Có. Sau khi thanh toán, bạn sẽ nhận được email hoặc tin nhắn xác nhận."));
        faqList.add(new Faq("Thanh toán", "Thanh toán có an toàn không?", "Có. Mọi giao dịch được mã hóa và bảo mật theo tiêu chuẩn cao."));
        faqList.add(new Faq("Thanh toán", "Tôi có thể xuất hóa đơn không?", "Có, vui lòng ghi chú yêu cầu hóa đơn khi thanh toán."));
        faqList.add(new Faq("Thanh toán", "Chuyển khoản nhưng chưa thấy xác nhận?", "Vui lòng gửi minh chứng thanh toán qua fanpage hoặc hotline để được hỗ trợ."));
        faqList.add(new Faq("Thanh toán", "Có áp dụng mã giảm giá khi thanh toán không?", "Có. Bạn nhập mã khuyến mãi ở bước thanh toán để được giảm giá."));
        faqList.add(new Faq("Thanh toán", "Tôi có thể thanh toán một phần trước, phần còn lại khi nhận không?", "Hiện chưa hỗ trợ hình thức thanh toán chia nhỏ."));
        faqList.add(new Faq("Thanh toán", "Có phí phụ thu nào khác không?", "Không. Tổng số tiền hiển thị là số bạn phải thanh toán."));

        // Sản phẩm
        faqList.add(new Faq("Sản phẩm", "Nguyên liệu có tươi mới không?", "Có. Tất cả nguyên liệu được chuẩn bị trong ngày giao hàng."));
        faqList.add(new Faq("Sản phẩm", "Các món ăn có sẵn công thức không?", "Có. Mỗi phần ăn kèm công thức hướng dẫn chi tiết, dễ hiểu."));
        faqList.add(new Faq("Sản phẩm", "Tôi có thể chọn khẩu phần ăn phù hợp không?", "Có. Bạn có thể chọn phần cho 1–4 người tùy món."));
        faqList.add(new Faq("Sản phẩm", "Có món chay không?", "Có. Mục “Món chay” luôn được cập nhật hàng tuần."));
        faqList.add(new Faq("Sản phẩm", "Thành phần có ghi rõ dị ứng thực phẩm không?", "Có. Các món đều ghi rõ thành phần để bạn kiểm tra trước."));
        faqList.add(new Faq("Sản phẩm", "Tôi có thể yêu cầu thay đổi nguyên liệu không?", "Hiện chưa hỗ trợ tuỳ chỉnh nguyên liệu, nhưng bạn có thể chọn món phù hợp nhất."));
        faqList.add(new Faq("Sản phẩm", "Có combo tiết kiệm không?", "Có. Bạn có thể chọn các combo bữa ăn tiết kiệm theo tuần."));
        faqList.add(new Faq("Sản phẩm", "Sản phẩm có hạn sử dụng bao lâu?", "Nguyên liệu nên dùng trong ngày. Nếu không, bảo quản lạnh và sử dụng trong 24h."));

        // Vận chuyển
        faqList.add(new Faq("Vận chuyển", "Bếp Nhà Ta giao hàng ở đâu?", "Hiện tại chúng tôi giao hàng trong khu vực nội thành và một số quận lân cận."));
        faqList.add(new Faq("Vận chuyển", "Phí giao hàng là bao nhiêu?", "Miễn phí với đơn từ 200.000đ. Dưới mức này, phí từ 15.000–30.000đ tùy khu vực."));
        faqList.add(new Faq("Vận chuyển", "Thời gian giao hàng như thế nào?", "Giao trong khung 8h–19h mỗi ngày, bạn có thể chọn giờ cụ thể khi thanh toán."));
        faqList.add(new Faq("Vận chuyển", "Tôi có thể kiểm tra tình trạng giao hàng không?", "Có. Chúng tôi sẽ cập nhật qua email hoặc tin nhắn khi đơn đang được giao."));
        faqList.add(new Faq("Vận chuyển", "Giao hàng có đúng giờ không?", "Chúng tôi luôn cố gắng giao đúng hẹn, và sẽ thông báo nếu có chậm trễ."));
        faqList.add(new Faq("Vận chuyển", "Tôi không có nhà lúc giao thì sao?", "Bạn có thể nhờ người nhận thay hoặc liên hệ để dời thời gian giao."));
        faqList.add(new Faq("Vận chuyển", "Có thể nhận hàng tại địa chỉ khác không?", "Có. Bạn điền địa chỉ mong muốn khi đặt hàng."));
        faqList.add(new Faq("Vận chuyển", "Có giao hàng vào cuối tuần và ngày lễ không?", "Có. Tuy nhiên, đơn đặt vào dịp lễ nên được đặt sớm để đảm bảo giao đúng hẹn."));

        // Hoàn trả
        faqList.add(new Faq("Hoàn trả", "Tôi có thể trả lại hàng không?", "Chỉ áp dụng cho sản phẩm lỗi hoặc hư hỏng do vận chuyển."));
        faqList.add(new Faq("Hoàn trả", "Làm sao để yêu cầu hoàn tiền hoặc đổi hàng?", "Liên hệ hotline hoặc fanpage trong vòng 12h sau khi nhận hàng kèm ảnh sản phẩm."));
        faqList.add(new Faq("Hoàn trả", "Thời gian xử lý hoàn tiền là bao lâu?", "Trong 3–5 ngày làm việc sau khi xác nhận yêu cầu."));
        faqList.add(new Faq("Hoàn trả", "Tôi có cần trả lại sản phẩm lỗi không?", "Tuỳ trường hợp. Đội ngũ CSKH sẽ hướng dẫn chi tiết khi tiếp nhận yêu cầu."));
        faqList.add(new Faq("Hoàn trả", "Nếu nhận thiếu nguyên liệu thì sao?", "Bạn chụp ảnh đơn hàng và liên hệ ngay để được bổ sung hoặc hoàn tiền."));
        faqList.add(new Faq("Hoàn trả", "Hoàn tiền bằng hình thức nào?", "Chúng tôi hoàn qua tài khoản ngân hàng hoặc mã giảm giá cho đơn sau."));
        faqList.add(new Faq("Hoàn trả", "Có chính sách đổi món khác nếu không vừa ý không?", "Hiện chưa hỗ trợ đổi vì lý do khẩu vị cá nhân."));
        faqList.add(new Faq("Hoàn trả", "Tôi phản hồi sau 24h có được hỗ trợ không?", "Tùy trường hợp. Nhưng khuyến khích bạn phản hồi sớm nhất có thể."));
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
