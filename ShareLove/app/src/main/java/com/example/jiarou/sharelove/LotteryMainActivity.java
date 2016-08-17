package com.example.jiarou.sharelove;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LotteryMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.LotteryDisplay_layout, AddVendorFragment.newInstance(), "LotteryDisplay")
                    .commit();
        }
    }
}
