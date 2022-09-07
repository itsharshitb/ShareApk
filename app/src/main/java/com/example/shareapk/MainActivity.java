package com.example.shareapk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    List<App> apps = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.app_list);

        //Getting apps
        PackageManager pm = getApplicationContext().getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for(ApplicationInfo packageInfo: packages){
            String name;
            if((name = String.valueOf(pm.getApplicationLabel(packageInfo))).isEmpty()){
                name = packageInfo.packageName;
            }
            Drawable icon = pm.getApplicationIcon(packageInfo);
            String ApkPath = packageInfo.sourceDir;
            long apkSize = new File(packageInfo.sourceDir).length();

            apps.add(new App(name,icon,ApkPath,apkSize));
        }

        Collections.sort(apps, new Comparator<App>(){
            @Override
            public int compare(App app, App t1) {
                return app.getName().toLowerCase().compareTo(t1.getName().toLowerCase());
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        AppAdopter appAdopter = new AppAdopter(this,apps);
        recyclerView.setAdapter(appAdopter);
    }
}