package com.example.flynow.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.flynow.databinding.ActivityIntroBinding;

//מסך כניסה שבלחיצה על get started מעביר אותי למסך הראשי של בחירת מידע על טיסה
public class IntroActivity extends BaseActivity {
    private ActivityIntroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.starBtn.setOnClickListener(v -> startActivity(new Intent(IntroActivity.this, MainActivity.class)));//בלחיצה על כפתור עובר למסך המיין
    }
}