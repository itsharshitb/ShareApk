package com.example.shareapk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class AppAdopter extends RecyclerView.Adapter<AppAdopter.AppViewHolder> {

    Context context;
    List<App> apps;

    public AppAdopter(Context context, List<App> apps) {
        this.context = context;
        this.apps = apps;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_row,parent,false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.appName.setText(apps.get(position).getName());
        long apkSize = apps.get(position).getApkSize();
        holder.appSize.setText(getReadableSize(apkSize));
        holder.appIcon.setImageDrawable(apps.get(position).getIcon());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareApkIntent = new Intent();
                shareApkIntent.setAction(Intent.ACTION_SEND);

                shareApkIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                        context,BuildConfig.APPLICATION_ID + ".provider",new File(apps.get(position).getApkPath())
                ));
                shareApkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareApkIntent.setType("application/vnd.amdroid.package-archive");

                context.startActivity(Intent.createChooser(shareApkIntent,"Share APK"));
            }
        });

    }

    private String getReadableSize(long apkSize) {
        String size;
        if(apkSize<1024){
            size = String.format(
                    context.getString(R.string.app_size_b),
                    (double) apkSize
            );
        } else if(apkSize<Math.pow(1024,2)){
            size = String.format(
                    context.getString(R.string.app_size_kib),
                    (double) (apkSize/1024)
            );
        }else if(apkSize<Math.pow(1024,3)){
            size = String.format(
                    context.getString(R.string.app_size_mib),
                    (double)(apkSize/Math.pow(1024,2))
            );
        }else{
            size = String.format(
                    context.getString(R.string.app_size_mib),
                    (double)(apkSize/Math.pow(1024,3))
            );
        }
        return size;
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class AppViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView appIcon;
        TextView appName,appSize;
        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.app_row);
            appIcon = itemView.findViewById(R.id.app_icon);
            appName = itemView.findViewById(R.id.app_name);
            appSize = itemView.findViewById(R.id.app_size);
        }
    }
}
