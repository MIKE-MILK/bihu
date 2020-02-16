package com.mike_milk.bihu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mike_milk.bihu.R;

public class SplashActivity extends AppCompatActivity {
    private View ivBacground;
    private AlphaAnimation alphaAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initview();
        initAnimation();
        initListen();
    }
    private void initListen(){
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void initAnimation(){
        alphaAnimation=new AlphaAnimation(0f,1f);
        alphaAnimation.setDuration(3000);
        ivBacground.setAnimation(alphaAnimation);
    }
    private void initview(){
        setContentView(R.layout.splashlayout);
        ivBacground=findViewById(R.id.spash_backgroud);
    }
}
