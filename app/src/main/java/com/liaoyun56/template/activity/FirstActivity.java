package com.liaoyun56.template.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liaoyun56.template.BaseActivity;
import com.liaoyun56.template.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_first)
public class FirstActivity extends BaseActivity {

    @AfterViews
    void init(){

    }
}
