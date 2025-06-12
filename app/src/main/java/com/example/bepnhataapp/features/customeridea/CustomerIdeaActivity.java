package com.example.bepnhataapp.features.customeridea;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bepnhataapp.R;

public class CustomerIdeaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_idea);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupClearButtonWithAction((EditText) findViewById(R.id.edtName));
        setupClearButtonWithAction((EditText) findViewById(R.id.edtPhone));
        setupClearButtonWithAction((EditText) findViewById(R.id.edtEmail));
        setupClearButtonWithAction((EditText) findViewById(R.id.edtProblem));
        setupClearButtonWithAction((EditText) findViewById(R.id.edtContent));
    }

    private void setupClearButtonWithAction(EditText editText) {
        editText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Drawable drawableEnd = editText.getCompoundDrawables()[2];
                if (drawableEnd != null) {
                    int drawableWidth = drawableEnd.getBounds().width();
                    int right = editText.getRight();
                    int left = right - drawableWidth - editText.getPaddingRight();
                    if (event.getRawX() >= left) {
                        editText.setText("");
                        return true;
                    }
                }
            }
            return false;
        });
    }
}