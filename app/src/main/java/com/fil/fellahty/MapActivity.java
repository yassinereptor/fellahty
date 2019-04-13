package com.fil.fellahty;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.fil.fellahty.fragments.MapDialogFragment;
import com.fil.fellahty.fragments.SMSCodeDialogFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity {

    private ImageButton btn_open_map;
    private TextInputEditText signup_map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        btn_open_map = findViewById(R.id.btn_open_map);
        signup_map = findViewById(R.id.signup_map);


        btn_open_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentManager manager = getFragmentManager();
                Fragment frag = manager.findFragmentByTag("fragment_map");
                if (frag != null) {
                    manager.beginTransaction().remove(frag).commit();
                }
                MapDialogFragment editNameDialog = new MapDialogFragment();
                editNameDialog.show(getSupportFragmentManager().beginTransaction(), "fragment_map");
            }
        });

    }
}
