package com.example.bepnhataapp.features.policy;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bepnhataapp.R;

public class PolicyActivity extends AppCompatActivity implements PolicyFragment.OnPolicyTabSelectedListener {

    private TextView activityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        activityTitle = findViewById(R.id.tvTitle);

        if (savedInstanceState == null) {
            PolicyFragment policyFragment = new PolicyFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(PolicyFragment.ARG_POLICY_TYPE, PolicyFragment.TYPE_TERMS);
            policyFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_host_container, policyFragment)
                    .commit();
        }
    }

    @Override
    public void onPolicyTabSelected(String title) {
        if (activityTitle != null) {
            activityTitle.setText(title);
        }
    }
} 
