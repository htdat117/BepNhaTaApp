package com.example.bepnhataapp.features.recipes;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.button.MaterialButtonToggleGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import android.graphics.BitmapFactory;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.base.BaseActivity;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.dao.RecipeDao;
import com.example.bepnhataapp.common.dao.RecipeDetailDao;
import com.example.bepnhataapp.common.dao.RecipeDownloadDao;
import com.example.bepnhataapp.common.dao.RecipeIngredientDao;
import com.example.bepnhataapp.common.dao.IngredientDao;
import com.example.bepnhataapp.common.dao.InstructionRecipeDao;
import com.example.bepnhataapp.common.dao.FavouriteRecipeDao;
import com.example.bepnhataapp.common.dao.RecipeCommentDao;
import com.example.bepnhataapp.common.model.Customer;
import com.example.bepnhataapp.common.model.RecipeDetail;
import com.example.bepnhataapp.common.model.RecipeEntity;
import com.example.bepnhataapp.common.model.RecipeDownload;
import com.example.bepnhataapp.common.model.RecipeIngredient;
import com.example.bepnhataapp.common.model.Ingredient;
import com.example.bepnhataapp.common.model.InstructionRecipe;
import com.example.bepnhataapp.common.model.FavouriteRecipe;
import com.example.bepnhataapp.common.model.RecipeComment;
import com.example.bepnhataapp.common.utils.SessionManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.text.Normalizer;

public class RecipeDetailActivity extends BaseActivity implements BaseActivity.OnNavigationItemReselectedListener {

    private int servingFactor = 1; // 1 = 2 người, 2 = 4 người
    private long currentRecipeId; // expose for update methods
    private RecipeDetailPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        currentRecipeId = getIntent().hasExtra("recipeId") ? getIntent().getLongExtra("recipeId", -1) : getIntent().getIntExtra("recipeId", -1);
        if (currentRecipeId == -1) {
            finish();
            return;
        }

        ImageView img = findViewById(R.id.imvRecipe);
        TextView tvName = findViewById(R.id.txtName);
        TextView tvCategory = findViewById(R.id.tvRecipeCategory);
        TextView tvCaloTime = findViewById(R.id.tvRecipeCaloTime);

        // Ẩn hai label màu xám theo yêu cầu
        if(tvCategory!=null) tvCategory.setVisibility(View.GONE);
        if(tvCaloTime!=null) tvCaloTime.setVisibility(View.GONE);
        ImageView imvDownload = findViewById(R.id.imvDowload);
        ImageView ivFavorite = findViewById(R.id.iconOverlay);
        // Handle favorite state for this recipe
        final boolean[] isFavourite = {false};
        if (ivFavorite != null) {
            // Determine initial favourite state if user logged in
            if (SessionManager.isLoggedIn(this)) {
                String phoneFav = SessionManager.getPhone(this);
                CustomerDao cDaoFav = new CustomerDao(this);
                Customer cFav = cDaoFav.findByPhone(phoneFav);
                if (cFav != null) {
                    FavouriteRecipeDao favDao = new FavouriteRecipeDao(this);
                    isFavourite[0] = favDao.get(currentRecipeId, cFav.getCustomerID()) != null;
                }
            }
            // Set initial icon according to state
            ivFavorite.setImageResource(isFavourite[0] ? R.drawable.ic_favorite_checked : R.drawable.ic_favorite_unchecked);

            ivFavorite.setOnClickListener(v -> {
                if (!SessionManager.isLoggedIn(this)) {
                    Toast.makeText(this, "Vui lòng đăng nhập để sử dụng", Toast.LENGTH_SHORT).show();
                    return;
                }
                String phoneToggle = SessionManager.getPhone(this);
                CustomerDao cDaoToggle = new CustomerDao(this);
                Customer cToggle = cDaoToggle.findByPhone(phoneToggle);
                if (cToggle == null) return;
                FavouriteRecipeDao favDaoToggle = new FavouriteRecipeDao(this);
                boolean currentlyFav = favDaoToggle.get(currentRecipeId, cToggle.getCustomerID()) != null;
                if (currentlyFav) {
                    favDaoToggle.delete(currentRecipeId, cToggle.getCustomerID());
                    Toast.makeText(this, "Đã xoá khỏi mục yêu thích", Toast.LENGTH_SHORT).show();
                } else {
                    FavouriteRecipe fr = new FavouriteRecipe();
                    fr.setRecipeID(currentRecipeId);
                    fr.setCustomerID(cToggle.getCustomerID());
                    fr.setCreatedAt(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date()));
                    favDaoToggle.insert(fr);
                    Toast.makeText(this, "Đã thêm vào mục yêu thích", Toast.LENGTH_SHORT).show();
                }
                isFavourite[0] = !currentlyFav;
                ivFavorite.setImageResource(isFavourite[0] ? R.drawable.ic_favorite_checked : R.drawable.ic_favorite_unchecked);
            });
        }
        TextView tvDescription = findViewById(R.id.tvDescription);
        TextView tvTryProduct = findViewById(R.id.tvTryProduct);
        // Additional UI for metrics
        TextView txtLevel = findViewById(R.id.txtLevel);
        TextView txtTaste = findViewById(R.id.txtTaste);
        TextView txtUses = findViewById(R.id.txtUses);
        TextView txtTime = findViewById(R.id.txtTime);
        ImageView imvTaste = findViewById(R.id.imvTaste);
        ImageView imvUses = findViewById(R.id.imvUses);

        // Nutrition section TextViews
        TextView tvCarbs = findViewById(R.id.tvCarbs);
        TextView tvProteinVal = findViewById(R.id.tvProtein);
        TextView tvCaloVal = findViewById(R.id.tvCalo);
        TextView tvFatVal = findViewById(R.id.tvFat);

        // Recommend section
        androidx.recyclerview.widget.RecyclerView rvRecommend = findViewById(R.id.rvRecommend);
        java.util.List<RecipeItem> recommendItems = new java.util.ArrayList<>();

        // Load recipe entity
        RecipeEntity entity = new RecipeDao(this).getAllRecipes().stream()
                .filter(r -> r.getRecipeID() == currentRecipeId)
                .findFirst()
                .orElse(null);
        if (entity != null) {
            tvName.setText(entity.getRecipeName());
            tvCategory.setText(entity.getCategory());
            Object source;
            String imgStr = entity.getImageThumb() != null ? entity.getImageThumb().trim() : "";
            if (imgStr.isEmpty()) {
                source = R.drawable.placeholder_banner_background;
            } else if (imgStr.startsWith("http")) {
                source = imgStr;
            } else {
                int resId = getResources().getIdentifier(imgStr, "drawable", getPackageName());
                source = resId != 0 ? resId : R.drawable.placeholder_banner_background;
            }
            Glide.with(this).load(source).placeholder(R.drawable.placeholder_banner_background).into(img);
            // Set mô tả công thức
            tvDescription.setText(entity.getDescription());
        }

        RecipeDetail detail = new RecipeDetailDao(this).get(currentRecipeId);
        if (detail != null) {
            String str = String.format("%d Kcal • %d Min", (int) detail.getCalo(), detail.getCookingTimeMinutes());
            tvCaloTime.setText(str);
            if(txtTime!=null) txtTime.setText(detail.getCookingTimeMinutes()+" phút");
            if(txtLevel!=null && detail.getLevel()!=null) txtLevel.setText(detail.getLevel());
            String flavorTxt = detail.getFlavor();
            if(flavorTxt != null && flavorTxt.trim().equalsIgnoreCase("Thanh thanh")) {
                flavorTxt = "Thanh nhẹ"; // đổi tên theo yêu cầu
            }
            if(txtTaste!=null && flavorTxt!=null) txtTaste.setText(flavorTxt);

            // Handle benefit text
            String benefitTxt = detail.getBenefit();
            if(txtUses!=null && benefitTxt!=null) txtUses.setText(benefitTxt);

            // Set icon for taste and benefit dynamically using slug
            if(imvTaste != null && flavorTxt != null) {
                String slug = slugify(flavorTxt);
                if(slug.contains("thanh_nhe") || slug.equals("thanh")) slug = "thanh";
                int res = getResources().getIdentifier("ic_"+slug, "drawable", getPackageName());
                if(res == 0) res = R.drawable.ic_taste;
                imvTaste.setImageResource(res);
            }
            if(imvUses != null && benefitTxt!= null) {
                String slug = slugify(benefitTxt);
                int res = getResources().getIdentifier("ic_"+slug, "drawable", getPackageName());
                if(res == 0) {
                    // Synonym mapping for benefit icons
                    if(slug.contains("ngu")) slug = "sleepy";      // Giấc ngủ ngon
                    else if(slug.contains("skin") || slug.contains("da")) slug = "skin";
                    else if(slug.contains("xuong")) slug = "bone"; // Bổ xương
                    else if(slug.contains("bo_mau") || (slug.contains("mau") && !slug.contains("bo_mau"))) slug = "blood"; // Bổ máu
                    else if(slug.contains("giai_doc") || slug.contains("detox") || slug.contains("doc")) slug = "detox"; // Giải độc
                    else if(slug.contains("giam_can") || slug.contains("weight")) slug = "weight"; // Giảm cân
                    else if(slug.contains("tim") || slug.contains("heart")) slug = "tim"; // Tốt tim (icon ic_tim)

                    res = getResources().getIdentifier("ic_"+slug, "drawable", getPackageName());
                    if(res == 0) res = R.drawable.ic_bone; // fallback cuối cùng
                }
                imvUses.setImageResource(res);
            }

            // Populate nutrition values (rounded to int for display)
            if(tvCarbs!=null) tvCarbs.setText((int) detail.getCarbs() + "g carbs");
            if(tvProteinVal!=null) tvProteinVal.setText((int) detail.getProtein() + "g proteins");
            if(tvCaloVal!=null) tvCaloVal.setText((int) detail.getCalo() + " Kcal");
            if(tvFatVal!=null) tvFatVal.setText((int) detail.getFat() + "g fat");
        }

        if(entity!=null){
            java.util.List<RecipeEntity> allRecipes = new RecipeDao(this).getAllRecipes();

            // 1. lấy các công thức cùng danh mục (khác công thức hiện tại)
            java.util.List<RecipeEntity> candidate = new java.util.ArrayList<>();
            for(RecipeEntity r: allRecipes){
                if(r.getRecipeID()==entity.getRecipeID()) continue;
                if(entity.getCategory()!=null && entity.getCategory().equals(r.getCategory())) candidate.add(r);
            }

            // 2. nếu chưa đủ 3 thì lấy ngẫu nhiên các công thức khác
            if(candidate.size()<3){
                java.util.List<RecipeEntity> others = new java.util.ArrayList<>();
                for(RecipeEntity r: allRecipes){
                    if(r.getRecipeID()==entity.getRecipeID()) continue;
                    if(!candidate.contains(r)) others.add(r);
                }
                java.util.Collections.shuffle(others);
                int need = 3 - candidate.size();
                for(int i=0;i<need && i<others.size();i++) candidate.add(others.get(i));
            }

            // 3. xáo trộn danh sách và chỉ lấy 3
            java.util.Collections.shuffle(candidate);
            if(candidate.size()>3) candidate = candidate.subList(0,3);

            final java.util.List<RecipeEntity> recommendList = new java.util.ArrayList<>(candidate);
            RecipeDetailDao dDao = new RecipeDetailDao(this);
            for(RecipeEntity e : recommendList){
                if(e.getRecipeID()==entity.getRecipeID()) continue; // skip current

                String imgStr = e.getImageThumb()!=null?e.getImageThumb().trim():"";
                byte[] imgData=null;
                try{ java.lang.reflect.Method m=e.getClass().getMethod("getImage"); imgData=(byte[])m.invoke(e);}catch(Exception ex){ }
                int resId = R.drawable.placeholder_banner_background;
                if(!imgStr.isEmpty()){ int r=getResources().getIdentifier(imgStr,"drawable",getPackageName()); if(r!=0) resId=r; }

                RecipeDetail det = dDao.get(e.getRecipeID());
                String timeStr = det!=null? det.getCookingTimeMinutes()+" phút" : "";
                String benefit = det!=null? det.getBenefit():"";
                String level = det!=null? det.getLevel():"";

                RecipeItem item = new RecipeItem(resId,imgStr,imgData,e.getRecipeName(),"",level,timeStr,e.getCategory()!=null?e.getCategory():"");
                item.setBenefit(benefit);
                item.setLevel(level);
                item.setTime(timeStr);
                item.setLikeCount(e.getLikeAmount());
                item.setCommentCount(e.getCommentAmount());
                recommendItems.add(item);
            }
            RecommendedRecipeAdapter recAdapter = new RecommendedRecipeAdapter(recommendItems, new RecipeAdapter.OnRecipeActionListener() {
                @Override
                public void onView(RecipeItem recipe) {
                    // open detail again with new id
                    RecipeEntity found=null;
                    for(RecipeEntity r: recommendList){ if(r.getRecipeName().equals(recipe.getName())) {found=r; break;} }
                    if(found!=null){
                        android.content.Intent intent = new android.content.Intent(RecipeDetailActivity.this, RecipeDetailActivity.class);
                        intent.putExtra("recipeId", (long)found.getRecipeID());
                        startActivity(intent);
                    }
                }
                @Override public void onDelete(RecipeItem recipe){ /*not used*/ }
            });
            rvRecommend.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
            rvRecommend.setAdapter(recAdapter);
        }

        imvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy customerID động
                String phone = SessionManager.getPhone(RecipeDetailActivity.this);
                if (phone == null) {
                    Toast.makeText(RecipeDetailActivity.this, "Bạn cần đăng nhập để tải công thức!", Toast.LENGTH_SHORT).show();
                    return;
                }
                CustomerDao customerDao = new CustomerDao(RecipeDetailActivity.this);
                Customer customer = customerDao.findByPhone(phone);
                if (customer == null) {
                    Toast.makeText(RecipeDetailActivity.this, "Không tìm thấy tài khoản!", Toast.LENGTH_SHORT).show();
                    return;
                }
                long customerId = customer.getCustomerID();
                RecipeDownloadDao dao = new RecipeDownloadDao(RecipeDetailActivity.this);
                // Kiểm tra đã tải chưa
                if (dao.get(customerId, currentRecipeId) != null) {
                    Toast.makeText(RecipeDetailActivity.this, "Công thức đã được tải về trước đó!", Toast.LENGTH_SHORT).show();
                    return;
                }
                RecipeDownload rd = new RecipeDownload();
                rd.setCustomerID(customerId);
                rd.setRecipeID(currentRecipeId);
                String now = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date());
                rd.setDownloadedAt(now);
                long result = dao.insert(rd);
                if (result != -1) {
                    Toast.makeText(RecipeDetailActivity.this, "Đã lưu công thức để xem offline!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RecipeDetailActivity.this, "Lỗi khi lưu công thức!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvTryProduct.setOnClickListener(v -> {
            com.example.bepnhataapp.common.dao.ProductDetailDao productDetailDao = new com.example.bepnhataapp.common.dao.ProductDetailDao(this);
            com.example.bepnhataapp.common.model.ProductDetail productDetail = productDetailDao.getByRecipeId(currentRecipeId);
            if (productDetail != null) {
                android.content.Intent intent = new android.content.Intent(this, com.example.bepnhataapp.features.products.ProductDetailActivity.class);
                intent.putExtra("productId", productDetail.getProductID());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Không tìm thấy sản phẩm liên kết!", Toast.LENGTH_SHORT).show();
            }
        });

        // Hiển thị nguyên liệu
        RecyclerView rcStepGuide = findViewById(R.id.rcStepGuide);
        RecyclerView rcComment = findViewById(R.id.rcComment);
        TextView txtCommentCount = findViewById(R.id.txtCommentCount);
        MaterialButtonToggleGroup toggleGroupTab = findViewById(R.id.toggleGroupTab);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new RecipeDetailPagerAdapter(this, currentRecipeId, servingFactor);
        viewPager.setAdapter(pagerAdapter);
        // Đồng bộ tab và page
        toggleGroupTab.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btnTabIngredient) {
                    viewPager.setCurrentItem(0);
                } else if (checkedId == R.id.btnTabGuide) {
                    viewPager.setCurrentItem(1);
                }
            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) toggleGroupTab.check(R.id.btnTabIngredient);
                else toggleGroupTab.check(R.id.btnTabGuide);
            }
        });

        // Load comments for recipe
        java.util.List<com.example.bepnhataapp.common.model.RecipeComment> allComments = new com.example.bepnhataapp.common.dao.RecipeCommentDao(this).getByRecipe(currentRecipeId);
        if(txtCommentCount!=null) txtCommentCount.setText("("+allComments.size()+")");
        // Lấy 2 bình luận hữu ích nhất
        java.util.List<com.example.bepnhataapp.common.model.RecipeComment> topComments = getTopUsefulComments(allComments, 2);
        final RecipeCommentAdapter cmtAdapter = new RecipeCommentAdapter(topComments);
        rcComment.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        rcComment.setAdapter(cmtAdapter);

        // Comment input bar elements
        ImageView imvAvatarCmt = findViewById(R.id.imvAvatarCmt);
        EditText edtComment = findViewById(R.id.edtComment);
        ImageButton btnSendComment = findViewById(R.id.btnSendComment);
        if (!SessionManager.isLoggedIn(this)) {
            // Hide entire comment bar if user not logged in
            if (imvAvatarCmt != null) imvAvatarCmt.setVisibility(View.GONE);
            if (edtComment != null) {
                edtComment.setVisibility(View.GONE);
                View parentBar = (View) edtComment.getParent();
                if (parentBar != null) parentBar.setVisibility(View.GONE);
            }
            if(btnSendComment != null) btnSendComment.setVisibility(View.GONE);
        } else {
            // If logged in, show avatar (if available)
            if (imvAvatarCmt != null) {
                String phoneUser = SessionManager.getPhone(this);
                CustomerDao customerDao = new CustomerDao(this);
                Customer loggedCustomer = customerDao.findByPhone(phoneUser);
                if (loggedCustomer != null && loggedCustomer.getAvatar() != null && loggedCustomer.getAvatar().length > 0) {
                    android.graphics.Bitmap bmp = BitmapFactory.decodeByteArray(
                            loggedCustomer.getAvatar(),
                            0,
                            loggedCustomer.getAvatar().length);
                    if(bmp!=null){
                        imvAvatarCmt.setImageBitmap(bmp);
                    }else{
                        imvAvatarCmt.setImageResource(R.drawable.ic_avatar);
                    }
                } else {
                    imvAvatarCmt.setImageResource(R.drawable.ic_avatar);
                }
            }
        }

        if(btnSendComment != null) {
            btnSendComment.setOnClickListener(v -> {
                if(!SessionManager.isLoggedIn(this)) {
                    Toast.makeText(this, "Vui lòng đăng nhập để bình luận", Toast.LENGTH_SHORT).show();
                    return;
                }
                String content = edtComment != null ? edtComment.getText().toString().trim() : "";
                if(content.isEmpty()) {
                    Toast.makeText(this, "Nội dung không được để trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                String phoneCmt = SessionManager.getPhone(this);
                CustomerDao customerDao1 = new CustomerDao(this);
                Customer cus = customerDao1.findByPhone(phoneCmt);
                if(cus == null) return;

                RecipeComment rc = new RecipeComment();
                rc.setRecipeID(currentRecipeId);
                rc.setCustomerID(cus.getCustomerID());
                rc.setContent(content);
                rc.setCreatedAt(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date()));
                rc.setUsefulness(0);
                new RecipeCommentDao(this).insert(rc);

                // Reload list và hiển thị lại top 2
                java.util.List<com.example.bepnhataapp.common.model.RecipeComment> updatedAll = new RecipeCommentDao(this).getByRecipe(currentRecipeId);
                cmtAdapter.updateData(getTopUsefulComments(updatedAll, 2));
                if(txtCommentCount != null) txtCommentCount.setText("("+updatedAll.size()+")");
                if(edtComment != null) edtComment.setText("");
            });
        }

        ImageView imvCalendarAdd = findViewById(R.id.imvCalendarAdd);
        if (imvCalendarAdd != null) {
            imvCalendarAdd.setOnClickListener(v -> {
                if (!SessionManager.isLoggedIn(this)) {
                    Toast.makeText(this, "Vui lòng đăng nhập để sử dụng chức năng này", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Bước 1: Chọn ngày
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder
                        .datePicker()
                        .setTitleText("Chọn ngày thêm món")
                        .build();
                datePicker.addOnPositiveButtonClickListener(selection -> {
                    LocalDate selectedDate = Instant.ofEpochMilli(selection)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    // Bước 2: Chọn buổi
                    String[] slots = {"Sáng", "Trưa", "Tối", "Snack"};
                    new AlertDialog.Builder(this)
                            .setTitle("Chọn buổi")
                            .setItems(slots, (dialog, which) -> {
                                String chosenSlot = slots[which];
                                addRecipeToMealPlan(selectedDate, chosenSlot);
                            })
                            .show();
                });
                datePicker.show(getSupportFragmentManager(), "datePicker");
            });
        }

        // bottom nav
        setupBottomNavigationFragment(R.id.nav_recipes);

        // Xử lý header tài khoản
        TextView tvLogin = findViewById(R.id.tv_login);
        ImageView ivLogo = findViewById(R.id.iv_logo);
        if(tvLogin != null) {
            if(com.example.bepnhataapp.common.utils.SessionManager.isLoggedIn(this)) {
                String phone = com.example.bepnhataapp.common.utils.SessionManager.getPhone(this);
                com.example.bepnhataapp.common.dao.CustomerDao cDao = new com.example.bepnhataapp.common.dao.CustomerDao(this);
                com.example.bepnhataapp.common.model.Customer c = cDao.findByPhone(phone);
                if(c != null) tvLogin.setText(c.getFullName());
            } else {
                tvLogin.setText("Đăng nhập");
            }
        }
        if(ivLogo != null) {
            ivLogo.setOnClickListener(v -> finish()); // dùng logo làm nút back
        }

        // Xử lý header phụ (back + tiêu đề)
        TextView tvBackHeader = findViewById(R.id.txtContent);
        ImageView btnBack = findViewById(R.id.btnBack);
        if(tvBackHeader != null) tvBackHeader.setText("Chi tiết công thức");
        if(btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Xử lý nút 'Tất cả' để xem toàn bộ bình luận
        TextView tvAllComments = findViewById(R.id.tvSeeAllComments);
        if (tvAllComments != null) {
            tvAllComments.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(this, com.example.bepnhataapp.features.recipes.RecipeCommentActivity.class);
                intent.putExtra("recipeId", currentRecipeId);
                startActivity(intent);
            });
        }
    }

    @Override
    protected int getBottomNavigationContainerId() {
        return R.id.bottom_navigation_container;
    }

    @Override
    public void onNavigationItemReselected(int itemId) {
        handleNavigation(itemId);
    }

    private void addIngredient(LinearLayout parent, Ingredient ingEntity, double quantity) {
        View v = getLayoutInflater().inflate(R.layout.item_ingredient_grid, parent, false);
        ImageView iv = v.findViewById(R.id.ivIngredient);
        TextView tvName = v.findViewById(R.id.tvIngredientName);
        TextView tvQty = v.findViewById(R.id.tvIngredientQuantity);
        if (ingEntity != null) {
            if (ingEntity.getImageLink() != null && !ingEntity.getImageLink().isEmpty()) {
                Glide.with(this).load(ingEntity.getImageLink()).placeholder(R.drawable.food_placeholder).into(iv);
            } else if (ingEntity.getImage() != null && ingEntity.getImage().length > 0) {
                Glide.with(this).load(ingEntity.getImage()).placeholder(R.drawable.food_placeholder).into(iv);
            } else {
                iv.setImageResource(R.drawable.food_placeholder);
            }
            tvName.setText(ingEntity.getIngredientName());
        } else {
            iv.setImageResource(R.drawable.food_placeholder);
            tvName.setText("Không rõ");
        }
        tvQty.setText(String.valueOf(quantity));
        parent.addView(v);
    }

    private String slugify(String input) {
        if(input == null) return "";
        String temp = Normalizer.normalize(input, Normalizer.Form.NFD);
        temp = temp.replaceAll("đ", "d").replaceAll("Đ", "d");
        temp = temp.replaceAll("\\p{M}", ""); // remove diacritics
        temp = temp.toLowerCase(java.util.Locale.ROOT).replaceAll("[^a-z0-9]+", "_");
        if(temp.startsWith("_")) temp = temp.substring(1);
        if(temp.endsWith("_")) temp = temp.substring(0, temp.length()-1);
        return temp;
    }

    /**
     * Update nutrition TextViews when serving factor changes
     * @param factor 1 = 2 người, 2 = 4 người
     */
    public void updateNutritionDisplay(int factor){
        this.servingFactor = factor;
        RecipeDetail detail = new RecipeDetailDao(this).get(currentRecipeId);
        if(detail==null) return;
        double carbs = detail.getCarbs() * factor;
        double protein = detail.getProtein() * factor;
        double calo = detail.getCalo() * factor;
        double fat = detail.getFat() * factor;
        TextView tvCarbs = findViewById(R.id.tvCarbs);
        TextView tvProteinVal = findViewById(R.id.tvProtein);
        TextView tvCaloVal = findViewById(R.id.tvCalo);
        TextView tvFatVal = findViewById(R.id.tvFat);
        if(tvCarbs!=null) tvCarbs.setText((int)carbs + "g carbs");
        if(tvProteinVal!=null) tvProteinVal.setText((int)protein + "g proteins");
        if(tvCaloVal!=null) tvCaloVal.setText((int)calo + " Kcal");
        if(tvFatVal!=null) tvFatVal.setText((int)fat + "g fat");
    }

    // Helper: lấy N bình luận có lượt hữu ích cao nhất
    private java.util.List<com.example.bepnhataapp.common.model.RecipeComment> getTopUsefulComments(java.util.List<com.example.bepnhataapp.common.model.RecipeComment> list, int n){
        java.util.List<com.example.bepnhataapp.common.model.RecipeComment> copy = new java.util.ArrayList<>(list);
        copy.sort((a,b)->{
            int cmp = Integer.compare(b.getUsefulness(), a.getUsefulness());
            if(cmp==0){ // nếu bằng lượt hữu ích, ưu tiên mới hơn
                return b.getCreatedAt().compareTo(a.getCreatedAt());
            }
            return cmp;
        });
        if(copy.size()>n) return new java.util.ArrayList<>(copy.subList(0,n));
        return copy;
    }

    /**
     * Thêm công thức hiện tại vào kế hoạch bữa ăn cá nhân ở ngày và buổi chỉ định.
     */
    private void addRecipeToMealPlan(LocalDate date, String mealType) {
        String phone = SessionManager.getPhone(this);
        CustomerDao customerDao = new CustomerDao(this);
        Customer customer = customerDao.findByPhone(phone);
        if (customer == null) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            return;
        }

        long customerId = customer.getCustomerID();
        com.example.bepnhataapp.common.dao.MealPlanDao mealPlanDao = new com.example.bepnhataapp.common.dao.MealPlanDao(this);
        com.example.bepnhataapp.common.model.MealPlan personalPlan = mealPlanDao.getPersonalPlan(customerId);
        if (personalPlan == null) {
            personalPlan = new com.example.bepnhataapp.common.model.MealPlan();
            personalPlan.setCustomerID(customerId);
            personalPlan.setType("PERSONAL");
            personalPlan.setTitle("Kế hoạch của tôi");
            personalPlan.setCreatedAt(java.time.LocalDateTime.now().toString());
            long newPlanId = mealPlanDao.insert(personalPlan);
            personalPlan.setMealPlanID(newPlanId);
        }

        String dateStr = date.toString();
        com.example.bepnhataapp.common.dao.MealDayDao mealDayDao = new com.example.bepnhataapp.common.dao.MealDayDao(this);
        com.example.bepnhataapp.common.model.MealDay mealDay = null;
        java.util.List<com.example.bepnhataapp.common.model.MealDay> days = mealDayDao.getAllByDate(dateStr);
        for (com.example.bepnhataapp.common.model.MealDay d : days) {
            if (d.getMealPlanID() == personalPlan.getMealPlanID()) {
                mealDay = d;
                break;
            }
        }
        if (mealDay == null) {
            mealDay = new com.example.bepnhataapp.common.model.MealDay();
            mealDay.setMealPlanID(personalPlan.getMealPlanID());
            mealDay.setDate(dateStr);
            long newDayId = mealDayDao.insert(mealDay);
            mealDay.setMealDayID(newDayId);
        }

        // Lấy hoặc tạo meal time
        com.example.bepnhataapp.common.dao.MealTimeDao mealTimeDao = new com.example.bepnhataapp.common.dao.MealTimeDao(this);
        java.util.List<com.example.bepnhataapp.common.model.MealTime> times = mealTimeDao.getByMealDay(mealDay.getMealDayID());
        com.example.bepnhataapp.common.model.MealTime mealTime = null;
        for (com.example.bepnhataapp.common.model.MealTime t : times) {
            if (mealType.equalsIgnoreCase(t.getMealType())) {
                mealTime = t;
                break;
            }
        }
        if (mealTime == null) {
            mealTime = new com.example.bepnhataapp.common.model.MealTime();
            mealTime.setMealDayID(mealDay.getMealDayID());
            mealTime.setMealType(mealType);
            long newTimeId = mealTimeDao.insert(mealTime);
            mealTime.setMealTimeID(newTimeId);
        }

        // Thêm công thức vào meal time
        com.example.bepnhataapp.common.dao.MealRecipeDao mealRecipeDao = new com.example.bepnhataapp.common.dao.MealRecipeDao(this);
        mealRecipeDao.insert(new com.example.bepnhataapp.common.model.MealRecipe(mealTime.getMealTimeID(), currentRecipeId));

        Toast.makeText(this, "Đã thêm công thức vào " + mealType + " ngày " + date + "!", Toast.LENGTH_SHORT).show();
    }
} 
