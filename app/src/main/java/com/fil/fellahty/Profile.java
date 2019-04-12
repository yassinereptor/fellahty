package com.fil.fellahty;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.fil.fellahty.classes.LineView;
import android.widget.Button;
import android.widget.RadioButton;
import java.util.ArrayList;
import android.view.View;

public class Profile extends AppCompatActivity {

    RadioButton lineButton;
    RadioButton infoButton;
    RadioButton settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        lineButton = (RadioButton) findViewById(R.id.chart_btn);
        infoButton = (RadioButton) findViewById(R.id.info_btn);
        settingsButton = (RadioButton) findViewById(R.id.settings_btn);
        chart fragment = new chart();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fr_profile, fragment);
        ft.commit();
        infoButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                info fragment = new info();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fr_profile, fragment);
                ft.commit();
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                settings fragment = new settings();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fr_profile, fragment);
                ft.commit();
            }
        });
        lineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chart fragment = new chart();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fr_profile, fragment);
                ft.commit();
            }
        });
    }

}
