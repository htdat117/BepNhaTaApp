package com.example.bepnhataapp.features.calocal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.adapter.FoodItemAdapter;
import com.example.bepnhataapp.common.dao.FoodCaloDao;
import com.example.bepnhataapp.common.model.FoodCalo;
import okhttp3.*;
import org.json.*;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class CaloCalculatorFragment extends Fragment {

    private static final String TAG = "CaloCalculatorFragment";

    private EditText editTextFoodName, editTextWeight;
    private Button btnSave, btnDelete;
    private LinearLayout llHistory;
    private TextView textViewTotalCalories;

    private ArrayList<FoodItem> foodList;
    private FoodItemAdapter adapter;
    private Map<String, FoodData> foodCaloriesMap;
    private double totalCalories = 0;
    private FoodCaloDao foodCaloDao;

    private ImageView ivBarcode, ivMic, ivCamera;
    private static final int REQ_CODE_SPEECH = 1001;
    private static final int REQ_CODE_CAMERA = 1002;
    private Uri photoUri;
    private String currentPhotoPath;

    public static class FoodData {
        double caloriesPer100g;
        int imageResId;
        String foodThumb;

        public FoodData(double caloriesPer100g, int imageResId) {
            this.caloriesPer100g = caloriesPer100g;
            this.imageResId = imageResId;
        }

        public FoodData(double caloriesPer100g, int imageResId, String foodThumb) {
            this.caloriesPer100g = caloriesPer100g;
            this.imageResId = imageResId;
            this.foodThumb = foodThumb;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Creating fragment view");
        View view = inflater.inflate(R.layout.fragment_calo_calculator, container, false);

        editTextFoodName = view.findViewById(R.id.editTextFoodName);
        Log.d(TAG, "editTextFoodName: " + (editTextFoodName != null ? "Found" : "Null"));
        editTextWeight = view.findViewById(R.id.editTextWeight);
        Log.d(TAG, "editTextWeight: " + (editTextWeight != null ? "Found" : "Null"));
        btnSave = view.findViewById(R.id.btnSave);
        Log.d(TAG, "btnSave: " + (btnSave != null ? "Found" : "Null"));
        btnDelete = view.findViewById(R.id.btnDelete);
        Log.d(TAG, "btnDelete: " + (btnDelete != null ? "Found" : "Null"));
        llHistory = view.findViewById(R.id.llHistory);
        Log.d(TAG, "llHistory: " + (llHistory != null ? "Found" : "Null"));
        textViewTotalCalories = view.findViewById(R.id.textViewTotalCalories);
        Log.d(TAG, "textViewTotalCalories: " + (textViewTotalCalories != null ? "Found" : "Null"));

        ivBarcode = view.findViewById(R.id.ivBarcode);
        ivMic = view.findViewById(R.id.ivMic);
        ivCamera = view.findViewById(R.id.ivCamera);

        ivBarcode.setOnClickListener(v -> startBarcodeScan());
        ivMic.setOnClickListener(v -> startVoiceInput());
        ivCamera.setOnClickListener(v -> startCamera());

        foodList = new ArrayList<>();
        foodCaloriesMap = new HashMap<>();
        foodCaloDao = new FoodCaloDao(getContext());
        
        // Debug database
        debugDatabase();
        
        initializeFoodCalories();

        // Bỏ ListView và adapter
        // adapter = new FoodItemAdapter(getContext(), R.layout.item_food_history, foodList);
        // listViewFoods.setAdapter(adapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Save button clicked");
                saveFood();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Delete button clicked");
                deleteSelectedFood();
            }
        });

        // Xóa dòng này vì không còn ListView
        // listViewFoods.setOnItemClickListener((parent, view1, position, id) -> {
        //     FoodItem selectedFood = foodList.get(position);
        //     showFoodDetails(selectedFood);
        // });

        return view;
    }

    private void debugDatabase() {
        try {
            Log.d(TAG, "=== DATABASE DEBUG START ===");
            
            // Kiểm tra bảng có tồn tại không
            boolean tableExists = foodCaloDao.isTableExists();
            Log.d(TAG, "Table FOOD_CALO exists: " + tableExists);
            
            if (tableExists) {
                // Đếm số bản ghi
                int count = foodCaloDao.getCount();
                Log.d(TAG, "Total records in FOOD_CALO: " + count);
                
                // In ra tất cả dữ liệu
                foodCaloDao.debugPrintAllData();
            } else {
                Log.w(TAG, "Table FOOD_CALO does not exist!");
            }
            
            Log.d(TAG, "=== DATABASE DEBUG END ===");
        } catch (Exception e) {
            Log.e(TAG, "Error debugging database", e);
        }
    }

    private void initializeFoodCalories() {
        // Load từ database trước
        loadFoodCaloriesFromDatabase();
        
        // Fallback data nếu database trống
        if (foodCaloriesMap.isEmpty()) {
            foodCaloriesMap.put("Cơm trắng", new FoodData(130.0, R.drawable.food_placeholder));
            foodCaloriesMap.put("Thịt heo", new FoodData(242.0, R.drawable.food_placeholder));
            foodCaloriesMap.put("Thịt bò", new FoodData(250.0, R.drawable.food_placeholder));
            foodCaloriesMap.put("Cá hồi", new FoodData(208.0, R.drawable.food_placeholder));
            foodCaloriesMap.put("Rau xanh", new FoodData(20.0, R.drawable.food_placeholder));
            foodCaloriesMap.put("Trứng gà", new FoodData(155.0, R.drawable.food_placeholder));
            foodCaloriesMap.put("Sữa tươi", new FoodData(42.0, R.drawable.food_placeholder));
            foodCaloriesMap.put("Chuối", new FoodData(89.0, R.drawable.food_placeholder));
            foodCaloriesMap.put("Táo", new FoodData(52.0, R.drawable.food_placeholder));
            foodCaloriesMap.put("Bánh mì", new FoodData(265.0, R.drawable.food_placeholder));
            foodCaloriesMap.put("Cua biển tươi", new FoodData(60.0, R.drawable.food_placeholder));
            foodCaloriesMap.put("Xương heo", new FoodData(120.0, R.drawable.food_placeholder));
            foodCaloriesMap.put("Tôm tươi", new FoodData(99.0, R.drawable.food_placeholder));
            foodCaloriesMap.put("Chả cua", new FoodData(170.0, R.drawable.food_placeholder));
            foodCaloriesMap.put("Bánh canh", new FoodData(110.0, R.drawable.food_placeholder));
            // Bổ sung cần tây đúng chuẩn
            foodCaloriesMap.put("Cần tây", new FoodData(16.0, R.drawable.food_placeholder));
        }
    }

    private void loadFoodCaloriesFromDatabase() {
        try {
            List<FoodCalo> foodCaloList = foodCaloDao.getAll();
            Log.d(TAG, "Database query returned " + foodCaloList.size() + " items");
            
            for (FoodCalo foodCalo : foodCaloList) {
                Log.d(TAG, "Food: " + foodCalo.getFoodName() + 
                          ", Calo: " + foodCalo.getCaloPerOneHundredGrams() + 
                          ", Thumb: " + foodCalo.getFoodThumb());
                
                // Xử lý ảnh từ foodThumb
                int imageResId = R.drawable.food_placeholder;
                if (foodCalo.getFoodThumb() != null && !foodCalo.getFoodThumb().isEmpty()) {
                    // Thử load ảnh từ drawable resource
                    try {
                        String imageName = foodCalo.getFoodThumb().replaceAll("\\.[^.]+$", ""); // Loại bỏ extension
                        imageResId = getResources().getIdentifier(imageName, "drawable", getContext().getPackageName());
                        if (imageResId == 0) {
                            imageResId = R.drawable.food_placeholder; // Fallback
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error loading image: " + foodCalo.getFoodThumb(), e);
                        imageResId = R.drawable.food_placeholder;
                    }
                }
                
                foodCaloriesMap.put(foodCalo.getFoodName(), 
                    new FoodData(foodCalo.getCaloPerOneHundredGrams(), 
                                imageResId, 
                                foodCalo.getFoodThumb()));
            }
            
            if (foodCaloList.isEmpty()) {
                Log.w(TAG, "No food data found in database, using fallback data");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading food calories from database", e);
        }
    }

    private void addSampleFoodItem(String foodName, double weight) {
        FoodData data = foodCaloriesMap.get(foodName);
        if (data != null) {
            double calories = weight * data.caloriesPer100g / 100.0;
            foodList.add(new FoodItem(foodName, weight, calories, data.imageResId));
        }
    }

    private void saveFood() {
        String foodName = editTextFoodName.getText().toString().trim();
        String weightText = editTextWeight.getText().toString().trim();

        if (foodName.isEmpty()) {
            editTextFoodName.setError("Vui lòng nhập tên món ăn");
            editTextFoodName.requestFocus();
            return;
        }

        if (weightText.isEmpty()) {
            editTextWeight.setError("Vui lòng nhập khối lượng");
            editTextWeight.requestFocus();
            return;
        }

        double weight;
        try {
            weight = Double.parseDouble(weightText);
            if (weight <= 0) {
                editTextWeight.setError("Khối lượng phải lớn hơn 0");
                editTextWeight.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            editTextWeight.setError("Khối lượng không hợp lệ");
            editTextWeight.requestFocus();
            return;
        }

        // Tìm kiếm thông tin calo từ database
        FoodCalo foodCalo = findFoodCalo(foodName);
        
        if (foodCalo != null) {
            // Tính calo dựa trên dữ liệu từ database
            int caloriesPer100g = foodCalo.getCaloPerOneHundredGrams();
            int totalCalories = (int) Math.round((weight * caloriesPer100g) / 100.0);
            
            Log.d(TAG, String.format("Found food: %s, calo/100g: %d, weight: %.1f, total: %d", 
                foodName, caloriesPer100g, weight, totalCalories));
            
            // Tạo FoodItem với thông tin từ database
            FoodItem foodItem = new FoodItem(foodName, weight, totalCalories, foodCalo.getFoodThumb());
            foodList.add(foodItem);
            // Sau mỗi lần thêm/xóa món ăn, gọi updateHistoryList() thay vì adapter.notifyDataSetChanged()
            updateHistoryList();
            
            updateTotalCalories();
            
            // Hiển thị thông tin chi tiết
            showCalculationDetails(foodName, weight, caloriesPer100g, totalCalories);
            
            // Xóa dữ liệu input
            editTextFoodName.setText("");
            editTextWeight.setText("");
            editTextFoodName.requestFocus();
            
        } else {
            // Không tìm thấy trong database, sử dụng dữ liệu fallback
            Log.w(TAG, "Food not found in database: " + foodName + ", using fallback data");
            
            // Hiển thị dialog tìm kiếm
            searchFood(foodName);
        }
    }

    private FoodCalo findFoodCalo(String foodName) {
        if (foodName == null) return null;
        String trimmedName = foodName.trim();
        // 1. Tìm kiếm chính xác (không normalize, chỉ trim)
        FoodCalo exact = foodCaloDao.findByName(trimmedName);
        if (exact != null) return exact;

        // 2. Tìm kiếm không phân biệt hoa thường
        List<FoodCalo> results = foodCaloDao.searchByNameIgnoreCase(trimmedName.toLowerCase());
        if (!results.isEmpty()) return results.get(0);

        // 3. Tìm kiếm LIKE
        results = foodCaloDao.searchByName(trimmedName);
        if (!results.isEmpty()) return results.get(0);

        // 4. Tìm kiếm nhiều từ khóa (không normalize)
        String[] keywords = trimmedName.split("\\s+");
        if (keywords.length > 1) {
            results = foodCaloDao.searchByMultipleKeywords(keywords);
            if (!results.isEmpty()) return results.get(0);
        }

        // 5. Cuối cùng mới thử normalize (không dấu)
        String normalizedName = normalizeString(foodName);
        if (!normalizedName.equals(trimmedName)) {
            // Thử lại các bước trên với tên không dấu
            exact = foodCaloDao.findByName(normalizedName);
            if (exact != null) return exact;
            results = foodCaloDao.searchByNameIgnoreCase(normalizedName);
            if (!results.isEmpty()) return results.get(0);
            results = foodCaloDao.searchByName(normalizedName);
            if (!results.isEmpty()) return results.get(0);
            String[] normKeywords = normalizedName.split("\\s+");
            if (normKeywords.length > 1) {
                results = foodCaloDao.searchByMultipleKeywords(normKeywords);
                if (!results.isEmpty()) return results.get(0);
            }
        }
        return null;
    }

    private void showCalculationDetails(String foodName, double weight, int caloriesPer100g, int totalCalories) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chi tiết tính toán");
        
        String message = String.format(
            "Món ăn: %s\n" +
            "Khối lượng: %.1f g\n" +
            "Calo/100g: %d calo\n\n" +
            "Công thức: (%.1f × %d) ÷ 100 = %d calo\n\n" +
            "Đã thêm vào danh sách!",
            foodName, weight, caloriesPer100g, weight, caloriesPer100g, totalCalories
        );
        
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private String normalizeString(String input) {
        if (input == null) return "";
        
        // Loại bỏ dấu tiếng Việt và chuyển về chữ thường
        String normalized = input.toLowerCase()
            .replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a")
            .replaceAll("[èéẹẻẽêềếệểễ]", "e")
            .replaceAll("[ìíịỉĩ]", "i")
            .replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o")
            .replaceAll("[ùúụủũưừứựửữ]", "u")
            .replaceAll("[ỳýỵỷỹ]", "y")
            .replaceAll("[đ]", "d")
            .replaceAll("[^a-z0-9\\s]", "") // Loại bỏ ký tự đặc biệt
            .replaceAll("\\s+", " ") // Thay nhiều khoảng trắng thành 1
            .trim();
        
        return normalized;
    }

    private void deleteSelectedFood() {
        if (foodList.isEmpty()) {
            Toast.makeText(getContext(), "Danh sách trống", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xóa món ăn");
        builder.setMessage("Bạn có muốn xóa món ăn cuối cùng không?");

        builder.setPositiveButton("Có", (dialog, which) -> {
            foodList.remove(foodList.size() - 1);
            // Sau mỗi lần thêm/xóa món ăn, gọi updateHistoryList() thay vì adapter.notifyDataSetChanged()
            updateHistoryList();
            updateTotalCalories();
            Toast.makeText(getContext(), "Đã xóa món ăn", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Không", null);
        builder.show();
    }

    private void showFoodDetails(FoodItem food) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chi tiết món ăn");
        
        String message = String.format(
            "Tên: %s\n" +
            "Khối lượng: %.1f g\n" +
            "Calo: %.0f calo\n" +
            "Calo/100g: %.1f calo",
            food.getName(),
            food.getWeight(),
            food.getCalories(),
            (food.getCalories() * 100.0) / food.getWeight()
        );
        
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void searchFood(String query) {
        Log.d(TAG, "Searching for: '" + query + "'");

        if (query == null || query.trim().isEmpty()) {
            Log.d(TAG, "Query is empty, clearing results");
            clearSearchResults();
            return;
        }

        String normalizedQuery = normalizeString(query.trim());
        Log.d(TAG, "Normalized query: '" + normalizedQuery + "'");

        List<FoodCalo> results = new ArrayList<>();

        // 1. Tìm kiếm chính xác
        FoodCalo exact = foodCaloDao.findByName(normalizedQuery);
        if (exact != null) {
            results.add(exact);
            Log.d(TAG, "Exact search found 1 result");
        }

        // 2. Nếu không tìm thấy, tìm kiếm không phân biệt hoa thường
        if (results.isEmpty()) {
            results = foodCaloDao.searchByNameIgnoreCase(normalizedQuery);
            Log.d(TAG, "Case-insensitive search results: " + results.size());
        }

        // 3. Nếu vẫn không tìm thấy, tìm kiếm LIKE
        if (results.isEmpty()) {
            results = foodCaloDao.searchByName(normalizedQuery);
            Log.d(TAG, "LIKE search results: " + results.size());
        }

        // 4. Nếu vẫn không tìm thấy, tìm kiếm với nhiều từ khóa
        if (results.isEmpty()) {
            String[] keywords = normalizedQuery.split("\\s+");
            if (keywords.length > 1) {
                results = foodCaloDao.searchByMultipleKeywords(keywords);
                Log.d(TAG, "Multi-keyword search results: " + results.size());
            }
        }

        // Hiển thị kết quả
        if (!results.isEmpty()) {
            Log.d(TAG, "Found " + results.size() + " results");
            showSearchResults(results);
        } else {
            Log.d(TAG, "No results found in DB, searching online...");
            searchFoodOnline(query); // Gọi API online nếu không có kết quả
        }
    }

    // Hàm tìm calo online qua Open Food Facts (không cần API key)
    private void searchFoodOnline(String foodName) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://world.openfoodfacts.org/cgi/search.pl?search_terms=" +
            foodName.replace(" ", "+") +
            "&search_simple=1&action=process&json=1";

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                new Handler(Looper.getMainLooper()).post(() ->
                    showNoResultsFound(foodName)
                );
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    try {
                        JSONObject obj = new JSONObject(json);
                        JSONArray products = obj.getJSONArray("products");
                        if (products.length() > 0) {
                            JSONObject first = products.getJSONObject(0);
                            String name = first.optString("product_name", foodName);
                            String imageUrl = first.optString("image_url", "");
                            double calories = -1;
                            if (first.has("nutriments")) {
                                JSONObject nutriments = first.getJSONObject("nutriments");
                                calories = nutriments.optDouble("energy-kcal_100g", -1);
                            }
                            double finalCalories = calories;
                            new Handler(Looper.getMainLooper()).post(() -> showOnlineFoodResult(name, imageUrl, finalCalories));
                            return;
                        }
                    } catch (JSONException e) {
                        // ignore
                    }
                }
                // Nếu tìm bằng tiếng Việt không có, thử dịch sang tiếng Anh rồi tìm lại
                translateToEnglish(foodName, new TranslationCallback() {
                    @Override
                    public void onTranslated(String english) {
                        if (english != null && !english.equalsIgnoreCase(foodName)) {
                            searchFoodOnline_EnglishFallback(foodName, english);
                        } else {
                            new Handler(Looper.getMainLooper()).post(() -> showNoResultsFound(foodName));
                        }
                    }
                });
            }
        });
    }

    // Hàm fallback: tìm lại bằng tiếng Anh, nếu vẫn không có thì báo không tìm thấy
    private void searchFoodOnline_EnglishFallback(String original, String english) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://world.openfoodfacts.org/cgi/search.pl?search_terms=" +
            english.replace(" ", "+") +
            "&search_simple=1&action=process&json=1";
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> showNoResultsFound(original));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    try {
                        JSONObject obj = new JSONObject(json);
                        JSONArray products = obj.getJSONArray("products");
                        if (products.length() > 0) {
                            JSONObject first = products.getJSONObject(0);
                            String name = first.optString("product_name", english);
                            String imageUrl = first.optString("image_url", "");
                            double calories = -1;
                            if (first.has("nutriments")) {
                                JSONObject nutriments = first.getJSONObject("nutriments");
                                calories = nutriments.optDouble("energy-kcal_100g", -1);
                            }
                            double finalCalories = calories;
                            new Handler(Looper.getMainLooper()).post(() -> showOnlineFoodResult(original + " (" + english + ")", imageUrl, finalCalories));
                            return;
                        }
                    } catch (JSONException e) {
                        // ignore
                    }
                }
                new Handler(Looper.getMainLooper()).post(() -> showNoResultsFound(original));
            }
        });
    }

    // Callback interface cho dịch tự động
    private interface TranslationCallback {
        void onTranslated(String english);
    }

    // Hàm dịch tự động sang tiếng Anh (dùng Google Translate API miễn phí hoặc endpoint public)
    private void translateToEnglish(String text, TranslationCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=vi&tl=en&dt=t&q=" + java.net.URLEncoder.encode(text);
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onTranslated(text));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    try {
                        // Kết quả là một mảng JSON lồng nhau
                        JSONArray arr = new JSONArray(body);
                        JSONArray arr2 = arr.getJSONArray(0);
                        JSONArray arr3 = arr2.getJSONArray(0);
                        String translated = arr3.getString(0);
                        new Handler(Looper.getMainLooper()).post(() -> callback.onTranslated(translated));
                        return;
                    } catch (Exception e) {
                        // ignore
                    }
                }
                new Handler(Looper.getMainLooper()).post(() -> callback.onTranslated(text));
            }
        });
    }

    // Hiển thị kết quả tìm online
    private void showOnlineFoodResult(String name, String imageUrl, double calories) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Kết quả từ Internet");
        StringBuilder msg = new StringBuilder();
        msg.append("Tên: ").append(name).append("\n");
        if (calories > 0) {
            msg.append("Calo/100g: ").append((int)calories).append(" calo\n");
        } else {
            msg.append("Không có thông tin calo\n");
        }
        if (imageUrl != null && !imageUrl.isEmpty()) {
            msg.append("Ảnh: ").append(imageUrl).append("\n");
        }
        builder.setMessage(msg.toString());
        builder.setPositiveButton("OK", null);
        builder.show();

        // Nếu có calo, tự động thêm vào danh sách
        if (calories > 0) {
            double weight = 0;
            try {
                String weightText = editTextWeight.getText().toString().trim();
                weight = Double.parseDouble(weightText);
            } catch (Exception e) {
                weight = 100;
            }
            foodList.add(new FoodItem(name, weight, weight * calories / 100.0, imageUrl));
            // Sau mỗi lần thêm/xóa món ăn, gọi updateHistoryList() thay vì adapter.notifyDataSetChanged()
            updateHistoryList();
            updateTotalCalories();
        }
    }

    private void showSearchResults(List<FoodCalo> results) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Kết quả tìm kiếm");

        String[] items = new String[results.size()];
        for (int i = 0; i < results.size(); i++) {
            FoodCalo food = results.get(i);
            items[i] = String.format("%s (%d calo/100g)", 
                food.getFoodName(), 
                food.getCaloPerOneHundredGrams());
            Log.d(TAG, "Result " + i + ": " + items[i]);
        }

        builder.setItems(items, (dialog, which) -> {
            FoodCalo selectedFood = results.get(which);
            Log.d(TAG, "User selected: " + selectedFood.getFoodName());
            
            // Tự động điền tên món ăn
            editTextFoodName.setText(selectedFood.getFoodName());
            
            // Hiển thị thông tin chi tiết
            showFoodInfo(selectedFood);
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void showFoodInfo(FoodCalo food) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thông tin món ăn");
        
        String message = String.format(
            "Tên: %s\n" +
            "Calo: %d calo/100g\n" +
            "Ảnh: %s\n\n" +
            "Công thức tính:\n" +
            "Calo = (Khối lượng × %d) ÷ 100",
            food.getFoodName(),
            food.getCaloPerOneHundredGrams(),
            food.getFoodThumb() != null ? food.getFoodThumb() : "Không có",
            food.getCaloPerOneHundredGrams()
        );
        
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void clearSearchResults() {
        // Có thể thêm logic để xóa kết quả tìm kiếm nếu cần
        Log.d(TAG, "Search results cleared");
    }

    private void showNoResultsFound(String query) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Không tìm thấy kết quả");
        
        String message = String.format(
            "Không tìm thấy món ăn: '%s'\n\n" +
            "Gợi ý:\n" +
            "• Kiểm tra lại chính tả\n" +
            "• Thử tìm kiếm với từ khóa ngắn hơn\n" +
            "• Ví dụ: 'gà' thay vì 'thịt gà nướng'",
            query
        );
        
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void updateTotalCalories() {
        int total = 0;
        for (FoodItem food : foodList) {
            total += food.getCalories();
        }
        textViewTotalCalories.setText("Tổng calo: " + total + " calo");
        Log.d(TAG, "Total calories updated: " + total);
    }

    private void updateHistoryList() {
        llHistory.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (FoodItem item : foodList) {
            View v = inflater.inflate(R.layout.item_food_history, llHistory, false);
            TextView tvName = v.findViewById(R.id.textViewFoodName);
            TextView tvWeight = v.findViewById(R.id.textViewWeight);
            TextView tvCalories = v.findViewById(R.id.textViewCalories);
            ImageView imgFood = v.findViewById(R.id.imageViewFood);
            if(tvName!=null) tvName.setText(item.getName());
            if(tvWeight!=null) tvWeight.setText(String.format("Khối lượng: %.1f g", item.getWeight()));
            if(tvCalories!=null) tvCalories.setText(String.format("Calo: %.1f kcal", item.getCalories()));
            if(imgFood!=null) {
                if(item.getImageUrl()!=null && !item.getImageUrl().isEmpty()) {
                    Glide.with(getContext()).load(item.getImageUrl()).placeholder(R.drawable.food_placeholder).into(imgFood);
                } else if(item.getImageResId()!=0) {
                    imgFood.setImageResource(item.getImageResId());
                } else {
                    imgFood.setImageResource(R.drawable.food_placeholder);
                }
            }
            llHistory.addView(v);
        }
    }

    // --- Barcode ---
    private final androidx.activity.result.ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            // Có thể tra cứu barcode ra tên món ăn nếu có DB, tạm thời điền vào ô nhập
            editTextFoodName.setText(result.getContents());
            editTextFoodName.requestFocus();
            Toast.makeText(getContext(), "Đã quét mã: " + result.getContents(), Toast.LENGTH_SHORT).show();
        }
    });
    private void startBarcodeScan() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Quét mã vạch sản phẩm");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        barcodeLauncher.launch(options);
    }

    // --- Voice ---
    private void startVoiceInput() {
        if (!SpeechRecognizer.isRecognitionAvailable(getContext())) {
            Toast.makeText(getContext(), "Thiết bị không hỗ trợ nhận diện giọng nói", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Nói tên món ăn...");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Không thể mở mic", Toast.LENGTH_SHORT).show();
        }
    }

    // --- Camera ---
    private void startCamera() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQ_CODE_CAMERA);
            return;
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(getContext(), "Không thể tạo file ảnh", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQ_CODE_CAMERA);
            }
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_SPEECH && resultCode == Activity.RESULT_OK && data != null) {
            java.util.ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                editTextFoodName.setText(result.get(0));
                editTextFoodName.requestFocus();
            }
        } else if (requestCode == REQ_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (photoUri != null) {
                // Có thể lưu ảnh này làm minh họa cho món ăn, hoặc hiển thị preview
                Toast.makeText(getContext(), "Đã chụp ảnh thành công", Toast.LENGTH_SHORT).show();
                // (Nâng cao: gán ảnh này cho món ăn mới thêm vào)
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(getContext(), "Bạn cần cấp quyền camera để sử dụng chức năng này", Toast.LENGTH_SHORT).show();
            }
        }
    }
} 
