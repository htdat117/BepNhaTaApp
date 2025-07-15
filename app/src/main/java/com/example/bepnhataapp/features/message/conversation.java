package com.example.bepnhataapp.features.message;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.content.ContextCompat;

import com.example.bepnhataapp.R;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import com.example.bepnhataapp.common.utils.SessionManager;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.model.Customer;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Intent;
import android.provider.MediaStore;
import androidx.appcompat.app.AlertDialog;
import android.widget.Toast;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class conversation extends AppCompatActivity {

    private LinearLayout llMessages;
    private EditText edtInput;
    private final Handler handler = new Handler();

    private final Map<String,String> faqAnswers = new HashMap<>();

    // Added: default welcome message shown when opening the screen
    private static final String WELCOME_MSG = "Chào mừng bạn đến với Bếp Nhà Ta. Bạn có vấn đề gì cần hỗ trợ cứ nhắn cho chúng mình nhé.";

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_IMAGE_PICK = 102;

    private static final int PERM_CAMERA = 201;
    private static final int PERM_READ_IMG = 202;
    private static final int PERM_READ_MEDIA_IMG = 203; // Android 13+

    // Preview image that the user just selected/captured before sending
    private ImageView ivPreview;
    // Holds the bitmap of the image waiting to be sent
    private Bitmap pendingImage;

    private LinearLayout llSuggestions; // to hide after first user message

    private void initFaq(){
        faqAnswers.put("giao nội thành", "Bếp Nhà Ta giao nội thành trong 2-3 ngày, phí ship 20k, đơn trên 300k miễn phí ship ạ!");
        faqAnswers.put("giao ngoại thành", "Ngoại thành (VD: Bình Chánh) giao 3-4 ngày, phí ship 30k, đơn trên 500k miễn phí ship nha anh/chị!");
        faqAnswers.put("đổi trả", "Nếu nguyên liệu hư hỏng hay thiếu, anh/chị báo trong 24h, Bếp Nhà Ta sẽ đổi mới hoặc hoàn tiền 100%.");
        faqAnswers.put("quên mật khẩu", "Nhấn 'Quên mật khẩu' ở trang đăng nhập, sau đó làm theo hướng dẫn để đặt lại mật khẩu.");
        faqAnswers.put("cập nhật địa chỉ", "Bạn vui lòng liên hệ fanpage của Bếp Nhà Ta hoặc hotline 0909868686 để được hỗ trợ.");
        faqAnswers.put("huỷ đơn hàng", "Bạn có thể huỷ đơn hàng nếu đơn chưa được đóng gói. Vui lòng liên hệ sớm với Bếp Nhà Ta qua hotline hoặc fanpage để được hỗ trợ nhé.");
        faqAnswers.put("phí vận chuyển", "Phí vận chuyển được tính dựa trên địa chỉ giao hàng và trọng lượng đơn hàng.");
        faqAnswers.put("thanh toán", "Bếp Nhà Ta hỗ trợ chuyển khoản ngân hàng, ví điện tử (Momo, ZaloPay), và tiền mặt khi nhận hàng (COD).");
        faqAnswers.put("nguyên liệu", "Bếp Nhà Ta sử dụng nguyên liệu chất lượng cao, đảm bảo an toàn và ngon miệng.");
        faqAnswers.put("đặt thành công?", "Bạn sẽ nhận email xác nhận và có thể kiểm tra trong mục 'Đơn hàng của tôi'.");
        // Thêm các câu hỏi khác tại đây
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Chỉ cho phép sử dụng khi đã đăng nhập
        if(!SessionManager.isLoggedIn(this)){
            Toast.makeText(this, "Vui lòng đăng nhập để sử dụng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        setContentView(R.layout.activity_conversation);

        // Initialize views
        llMessages = findViewById(R.id.llMessages);
        edtInput = findViewById(R.id.message_input);
        ImageView btnSend = findViewById(R.id.btn_send);
        ImageView ivBack = findViewById(R.id.btnBack);
        ImageView btnCamera = findViewById(R.id.btn_camera);
        ivPreview = findViewById(R.id.iv_image_preview);

        initFaq();

        // Set header title and hide edit button if layout_back_header is used
        TextView txtHeader = findViewById(R.id.txtContent);
        if(txtHeader!=null) txtHeader.setText("Chat cùng bếp nhà ta");
        View txtChange = findViewById(R.id.txtChange);
        if(txtChange!=null) txtChange.setVisibility(View.INVISIBLE);

        // Add some suggestion chips
        llSuggestions = findViewById(R.id.llSuggestions);
        if(llSuggestions!=null){
            String[] suggArr = new String[]{
                    "Nguyên liệu có đảm bảo chất lượng không?",
                    "Làm sao để biết đơn hàng đã được đặt thành công?",
                    "Nếu hàng có vấn đề tôi cần  đổi trả thế nào?"};
            for(String s: suggArr){
                android.widget.Button b = new android.widget.Button(this, null, androidx.appcompat.R.attr.buttonStyle);
                b.setText(s);
                b.setAllCaps(false);
                b.setBackgroundResource(R.drawable.bg_button_outline_orange);
                b.setTextColor(ContextCompat.getColor(this, R.color.dark1));
                android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.topMargin = 12;
                b.setLayoutParams(lp);
                b.setOnClickListener(v -> {
                    edtInput.setText(s);
                    sendMessage();
                    llSuggestions.setVisibility(View.GONE);
                });
                llSuggestions.addView(b);
            }
        }

        // Quick action chips horizontally scrollable
        LinearLayout llQuick = findViewById(R.id.llQuickActions);
        if(llQuick!=null){
            String[] quickArr = new String[]{"Chat với nhân viên Bếp","Quên mật khẩu","Cập nhật địa chỉ","Huỷ đơn hàng","Chính sách đổi trả","Phí vận chuyển","Hình thức thanh toán"};
            for(String q: quickArr){
                android.widget.Button chip = new android.widget.Button(this, null, androidx.appcompat.R.attr.buttonStyle);
                chip.setText(q);
                chip.setAllCaps(false);
                chip.setBackgroundResource(R.drawable.bg_button_primary); // light gray pill
                chip.setTextColor(ContextCompat.getColor(this, R.color.white));
                android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.rightMargin = 12;
                chip.setLayoutParams(lp);
                chip.setMinWidth(0);
                chip.setPadding(32,16,32,16);
                chip.setOnClickListener(v->{
                    edtInput.setText(q);
                    sendMessage();
                });
                llQuick.addView(chip);
            }
        }

        // Preload user avatar if logged-in
        loadUserAvatar();

        if(btnSend!=null){
            btnSend.setOnClickListener(v->sendMessage());
        }

        if(btnCamera!=null){
            btnCamera.setOnClickListener(v-> showImagePickerDialog());
        }

        if(ivBack!=null){
            ivBack.setOnClickListener(v->{
                android.content.Intent res = new android.content.Intent();
                res.putExtra("from_chat", true);
                setResult(RESULT_OK, res);
                finish();
            });
        }

        // Show the first welcome message from Bếp Nhà Ta
        addMessage(WELCOME_MSG, false);
    }

    // Utility: convert dp to pixel
    private int dpToPx(int dp){
        float density = getResources().getDisplayMetrics().density;
        return (int)(dp * density + 0.5f);
    }

    private void sendMessage(){
        String msg = edtInput!=null? edtInput.getText().toString().trim():"";
        // If there is neither text nor image, do nothing
        if(TextUtils.isEmpty(msg) && pendingImage==null) return;

        // Add user message (can be text, image or both)
        addMessage(msg, pendingImage, true);

        // Reset input
        if(edtInput!=null) edtInput.setText("");
        if(ivPreview!=null){
            ivPreview.setVisibility(View.GONE);
            ivPreview.setImageDrawable(null);
        }
        pendingImage = null;

        // Auto-reply only when user actually typed text
        if(!TextUtils.isEmpty(msg)){
            String reply = getAutoReply(msg);
            handler.postDelayed(() -> addMessage(reply, false), 600);

            // Hide suggestions once user has interacted
            if(llSuggestions!=null && llSuggestions.getVisibility()==View.VISIBLE){
                llSuggestions.setVisibility(View.GONE);
            }
        }
    }

    private String getAutoReply(String question){
        String q = question.toLowerCase(Locale.getDefault());
        for(Map.Entry<String,String> e : faqAnswers.entrySet()){
            if(q.contains(e.getKey())) return e.getValue();
        }
        return "Cảm ơn anh/chị đã đặt câu hỏi. Nhân viên của Bếp Nhà Ta sẽ phản hồi đến anh/chị sớm nhất!";
    }

    private String lastDateHeader = ""; // keep track of last shown date

    // Existing helper for text-only messages (kept for compatibility)
    private void addMessage(String text, boolean isUser){
        addMessage(text, null, isUser);
    }

    /**
     * Add a message to the chat view. It can include text, an image or both.
     */
    private void addMessage(String text, Bitmap image, boolean isUser){
        if(llMessages==null) return;

        Date now = new Date();
        String dateStr = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(now);

        // Add date header if new day
        if(!dateStr.equals(lastDateHeader)){
            TextView tvDate = new TextView(this);
            tvDate.setText(dateStr);
            tvDate.setTextSize(12);
            tvDate.setTextColor(android.graphics.Color.DKGRAY);
            tvDate.setTypeface(null, android.graphics.Typeface.BOLD);
            tvDate.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams dateLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dateLp.topMargin = dpToPx(8);
            tvDate.setLayoutParams(dateLp);
            llMessages.addView(tvDate);
            lastDateHeader = dateStr;
        }

        // Container for avatar + bubble
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams cLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cLp.topMargin = dpToPx(8);
        cLp.bottomMargin = dpToPx(4);
        cLp.gravity = isUser ? Gravity.END : Gravity.START;
        container.setLayoutParams(cLp);

        // Avatar
        ImageView avatar = new ImageView(this);
        int avatarSize = dpToPx(36);
        LinearLayout.LayoutParams avatarLp = new LinearLayout.LayoutParams(avatarSize, avatarSize);
        avatarLp.gravity = Gravity.BOTTOM;
        if(isUser){
            avatarLp.setMarginStart(dpToPx(8));
        }else{
            avatarLp.setMarginEnd(dpToPx(8));
        }
        avatar.setLayoutParams(avatarLp);
        avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if(isUser){
            if(userAvatarBmp!=null){
                avatar.setImageBitmap(userAvatarBmp);
            }else{
                avatar.setImageResource(R.drawable.ic_avatar);
            }
        }else{
            avatar.setImageResource(R.drawable.ic_logo);
        }

        // Bubble layout (vertical) containing message and time
        LinearLayout bubble = new LinearLayout(this);
        bubble.setOrientation(LinearLayout.VERTICAL);
        bubble.setPadding(dpToPx(12), dpToPx(8), dpToPx(12), dpToPx(8));

        // message text (only add if not empty)
        TextView tvMsg = null;
        if(!TextUtils.isEmpty(text)){
            tvMsg = new TextView(this);
            tvMsg.setText(text);
            tvMsg.setTextSize(16);
            tvMsg.setMaxWidth(getResources().getDisplayMetrics().widthPixels*3/4);
            tvMsg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        // message image (optional)
        ImageView imgMsg = null;
        if(image!=null){
            imgMsg = new ImageView(this);
            int maxW = getResources().getDisplayMetrics().widthPixels*2/3;
            LinearLayout.LayoutParams imgLp = new LinearLayout.LayoutParams(maxW, LinearLayout.LayoutParams.WRAP_CONTENT);
            imgLp.gravity = Gravity.CENTER;
            imgMsg.setLayoutParams(imgLp);
            imgMsg.setAdjustViewBounds(true);
            imgMsg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgMsg.setImageBitmap(image);
        }

        // time text inside bubble
        TextView tvTime = new TextView(this);
        tvTime.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(now));
        tvTime.setTextSize(12);
        tvTime.setTextColor(android.graphics.Color.BLACK);
        LinearLayout.LayoutParams timeInLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        timeInLp.gravity = Gravity.END;
        tvTime.setLayoutParams(timeInLp);

        if(imgMsg!=null) bubble.addView(imgMsg);
        if(tvMsg!=null) bubble.addView(tvMsg);
        bubble.addView(tvTime);

        // background and text colors based on sender
        if(isUser){
            if(tvMsg!=null) tvMsg.setTextColor(android.graphics.Color.WHITE);
            bubble.setBackgroundResource(R.drawable.bg_button_primary2);
        }else{
            if(tvMsg!=null) tvMsg.setTextColor(getResources().getColor(R.color.dark1));
            bubble.setBackgroundResource(R.drawable.button_rounded_gray);
        }

        // Add views in proper order
        if(isUser){
            container.addView(bubble);
            container.addView(avatar);
        }else{
            container.addView(avatar);
            container.addView(bubble);
        }

        // Add container to message list
        llMessages.addView(container);

        // Scroll to bottom
        llMessages.post(() -> {
            android.widget.ScrollView sv = (android.widget.ScrollView) llMessages.getParent().getParent();
            if(sv!=null) sv.fullScroll(android.view.View.FOCUS_DOWN);
        });
    }

    // Holds logged-in user's avatar for reuse
    private Bitmap userAvatarBmp;

    private void loadUserAvatar(){
        if(!SessionManager.isLoggedIn(this)) return;
        String phone = SessionManager.getPhone(this);
        if(phone==null) return;
        try{
            CustomerDao dao = new CustomerDao(this);
            Customer customer = dao.findByPhone(phone);
            if(customer!=null && customer.getAvatar()!=null && customer.getAvatar().length>0){
                Bitmap bmp = BitmapFactory.decodeByteArray(customer.getAvatar(),0,customer.getAvatar().length);
                if(bmp!=null){
                    userAvatarBmp = getCircularBitmap(bmp);
                }
            }
        }catch(Exception ignored){}
    }

    // Utility: crop bitmap to circle (fallback)
    private Bitmap getCircularBitmap(Bitmap src){
        int size = Math.min(src.getWidth(), src.getHeight());
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        float r = size/2f;
        canvas.drawCircle(r,r,r,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Rect rect = new Rect(0,0,size,size);
        canvas.drawBitmap(src,null,rect,paint);
        return output;
    }

    private void showImagePickerDialog(){
        BottomSheetDialog sheet = new BottomSheetDialog(this);
        View v = getLayoutInflater().inflate(R.layout.bottom_sheet_image_source,null);
        sheet.setContentView(v);
        v.findViewById(R.id.optCamera).setOnClickListener(bt->{
            sheet.dismiss();
            openCamera();
        });
        v.findViewById(R.id.optGallery).setOnClickListener(bt->{
            sheet.dismiss();
            openGallery();
        });
        sheet.show();
    }

    private void openCamera(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, PERM_CAMERA);
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void openGallery(){
        String perm;
        int reqCode;
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU){
            perm = Manifest.permission.READ_MEDIA_IMAGES;
            reqCode = PERM_READ_MEDIA_IMG;
        }else{
            perm = Manifest.permission.READ_EXTERNAL_STORAGE;
            reqCode = PERM_READ_IMG;
        }

        if(ContextCompat.checkSelfPermission(this, perm)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{perm}, reqCode);
            return;
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERM_CAMERA && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            openCamera();
        }else if((requestCode==PERM_READ_IMG || requestCode==PERM_READ_MEDIA_IMG) && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            openGallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK || data==null) return;
        if(requestCode==REQUEST_IMAGE_CAPTURE){
            Bitmap bmp = null;
            if(data.getExtras()!=null){
                bmp = (Bitmap) data.getExtras().get("data"); // thumbnail
            }
            if(bmp!=null){
                pendingImage = bmp;
                if(ivPreview!=null){
                    ivPreview.setImageBitmap(bmp);
                    ivPreview.setVisibility(View.VISIBLE);
                }
            }else{
                Toast.makeText(this,"Không lấy được ảnh",Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode==REQUEST_IMAGE_PICK){
            try{
                android.net.Uri uri = data.getData();
                if(uri!=null){
                    java.io.InputStream is = getContentResolver().openInputStream(uri);
                    Bitmap bmp = BitmapFactory.decodeStream(is);
                    if(is!=null) is.close();
                    if(bmp!=null){
                        pendingImage = bmp;
                        if(ivPreview!=null){
                            ivPreview.setImageBitmap(bmp);
                            ivPreview.setVisibility(View.VISIBLE);
                        }
                    }else{
                        Toast.makeText(this,"Không đọc được ảnh",Toast.LENGTH_SHORT).show();
                    }
                }
            }catch(Exception e){
                Toast.makeText(this,"Lỗi đọc ảnh",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
