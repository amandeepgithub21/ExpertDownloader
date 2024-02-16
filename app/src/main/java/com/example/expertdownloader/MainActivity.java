package com.example.expertdownloader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.bottomnavbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if (id == R.id.vid ){
                    fragmentopen(new VideoFragment(),false);
                }  else if (id== R.id.img) {
                    fragmentopen(new Testfrag(),true);
                } else {
                    //img
                    fragmentopen(new musicfragment(),false);

                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.img);
    }
    void fragmentopen(Fragment fragment,boolean flag ){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        ft.addToBackStack(null);
        if (flag)
            ft.add(R.id.framelayout,fragment);
        else
            ft.replace(R.id.framelayout,fragment);
        ft.commit();
    }
}