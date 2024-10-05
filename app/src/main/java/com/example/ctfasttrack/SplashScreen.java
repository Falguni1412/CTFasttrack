package com.example.ctfasttrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    ImageView img_logo;
    TextView txt_title;

    Animation lefttoright;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        //to hide hole screen means no status bar and no toolbar that time we used this above code
        //if we just want to hide toolbar that time we used NoTitleBar in .xml file and from themes.

        img_logo=findViewById(R.id.img_logo);
        txt_title=findViewById(R.id.txt_title);

        lefttoright = AnimationUtils.loadAnimation(this,R.anim.lefttoright);
        img_logo.setAnimation(lefttoright);

        setTitle("City Fast Track");

        new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(4000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(new Intent(SplashScreen.this,LoginPage.class));
                    finish();
                }
            }
        }.start();
    }
}
