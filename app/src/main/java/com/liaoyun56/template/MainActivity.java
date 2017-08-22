package com.liaoyun56.template;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.liaoyun56.template.utils.LogUtil;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @AfterViews
    void init(){
        LogUtil.debug("Helloworld");
    }
}
