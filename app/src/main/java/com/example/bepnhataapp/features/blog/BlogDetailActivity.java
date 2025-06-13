package com.example.bepnhataapp.features.blog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.models.Blog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import com.example.bepnhataapp.common.adapter.BlogAdapter;
import com.example.bepnhataapp.features.blog.Comment;
import com.example.bepnhataapp.features.blog.CommentAdapter;

public class BlogDetailActivity extends AppCompatActivity {

    public static final String EXTRA_BLOG = "extra_blog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        // Khởi tạo Header (tương tự BlogActivity)
        ImageView btnBack = findViewById(R.id.btnBack);
        TextView tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Blog"); // Đặt tiêu đề cho Toolbar

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BlogDetailActivity.this, BlogActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Nhận dữ liệu Blog từ Intent (có thể không dùng nếu muốn hiển thị dữ liệu mẫu cố định)
        Blog blog = (Blog) getIntent().getSerializableExtra(EXTRA_BLOG);

        // Gán dữ liệu mẫu cố định vào các View
        ImageView imgMain = findViewById(R.id.imgMain);
        TextView tvCategory = findViewById(R.id.tvCategory);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvDate = findViewById(R.id.tvDate);
        TextView tvLike = findViewById(R.id.tvLike);
        TextView tvComment = findViewById(R.id.tvComment);
        TextView tvContent = findViewById(R.id.tvContent);

        imgMain.setImageResource(R.drawable.blog);
        tvCategory.setText("Mẹo hay - Nấu chuẩn");
        tvTitle.setText("Thực đơn 3 món ngon cho bữa tối mùa đông se lạnh");
        tvDate.setText("31/3/2025");
        tvLike.setText("20");
        tvComment.setText("20");
        tvContent.setText("Bữa tối ngày mùa đông trời se se lạnh mà có một đĩa chả cá chiên nóng hổi cùng một tô canh vị chua chua ngọt ngọt thì ngon phải biết luôn đó nha! Bếp Nhà Ta phải rủ Mẹ vào bếp trổ tài nhanh một thực đơn xuất sắc với toàn món ngon mùa đông ngay thôi!\n\nViệc vệ sinh an toàn thực phẩm là một vấn đề mà các chị em nội trợ nào cũng vô cùng quan tâm, vì thực phẩm chính là nơi mà những loại vi khuẩn thường hay trú ngụ nhất khi chúng ta không sơ chế, sử dụng đúng cách. Điểm lại những thói quen khi vào bếp hàng ngày, hãy cùng Bếp Nhà Ta bảo vệ gia đình mình bằng cách tránh những sai lầm này khi chế biến thực phẩm nhé!\n\nNêm bột thô hoặc bột mì làm bánh chưa nấu chín\n\nCũng như trứng không được nấu chín kĩ, các loại bột thô và bột mì làm bánh chưa chế biến chín có thể chứa vi khuẩn E.Coli, Salmonella hoặc các vi khuẩn có hại khác; đặc biệt là các loại bột có nhào với trứng sống. Vì vậy, các chị em nội trợ cần nấu hoặc nướng kỹ bột mì và hỗn hợp bột có chứa trứng. Rửa tay, bề mặt làm việc và đồ dùng kỹ lưỡng sau khi tiếp xúc với bột mì, trứng sống và bột nhào sống. Và không nên ăn những thực phẩm có chứa trứng sống hoặc nấu chưa chín chẳng hạn như: trứng chần, sốt hollandaise và rượu trứng.\n\nThịt, gà, hải sản, trứng không nấu kĩ\n\nNhững loại thực phẩm như thịt gà, hải sản, trứng là những loại cần được nấu chín kĩ trước khi dùng. Theo các chuyên gia, việc những thực phẩm này chưa chín có thể chứa vi trùng, vi khuẩn như Salmonella, E.Coli khiến chúng ta bị bệnh đó nhé!\n\nKhông rửa tay trước khi chế biến\n\nBàn tay chính là nơi có chứa nhiều vi trùng, vi khuẩn nhất từ những hoạt động sinh hoạt hàng ngày. Thế nên, nếu không rửa tay kĩ thì rất có thể sẽ làm vi khuẩn từ tay dính vào thực phẩm và làm cho thực phẩm đó không an toàn. Trước, trong và sau khi nấu ăn hay chế biến, bạn cần chú ý đến rửa tay đúng cách trong ít nhất 20 giây bằng xà phòng và vòi nước chảy nhé!");

        // Thiết lập RecyclerView cho bình luận
        RecyclerView rvComments = findViewById(R.id.rvComments);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        List<Comment> commentList = new ArrayList<>();
        commentList.add(new Comment("Kiên Đoàn", "2 ngày trước", "Bài viết rất hữu ích! Cảm ơn Bếp Nhà Ta đã chia sẻ", 2));
        commentList.add(new Comment("Bếp Nhà Ta", "2 ngày trước", "Cảm ơn bạn đã chia sẻ. Bếp Nhà Ta sẽ tiếp tục chia sẻ những bài viết bổ ích trong thời gian sắp tới!", 0));
        commentList.add(new Comment("Đức Mạnh", "2 giờ trước", "Tôi đã áp dụng và thành công. Món ăn rất ngon, hợp với khẩu vị của gia đình. Con trai tôi ăn 3 chén cơm liền, cháu tôi Hưng Trần ăn rất ngon miệng, ăn xong vẫn thèm. Cảm ơn chia sẻ của Bếp Nhà Ta rất nhiều!!", 2));
        CommentAdapter commentAdapter = new CommentAdapter(commentList);
        rvComments.setAdapter(commentAdapter);

        // Thiết lập RecyclerView cho các bài viết gợi ý
        RecyclerView rvSuggest = findViewById(R.id.rvSuggest);
        rvSuggest.setLayoutManager(new LinearLayoutManager(this));
        List<Blog> suggestBlogList = new ArrayList<>();
        suggestBlogList.add(new Blog("Thực đơn 3 món ngon cho bữa tối mùa đông se lạnh", "", "Mẹo hay - Nấu chuẩn", R.drawable.blog, false, "31/3/2025", 20, 15));
        suggestBlogList.add(new Blog("Thực đơn 3 món ngon cho bữa tối mùa đông se lạnh", "", "Mẹo hay - Nấu chuẩn", R.drawable.blog, false, "31/3/2025", 20, 15));
        suggestBlogList.add(new Blog("Thực đơn 3 món ngon cho bữa tối mùa đông se lạnh", "", "Mẹo hay - Nấu chuẩn", R.drawable.blog, false, "31/3/2025", 20, 15));
        BlogAdapter suggestBlogAdapter = new BlogAdapter(suggestBlogList);
        rvSuggest.setAdapter(suggestBlogAdapter);
    }
} 