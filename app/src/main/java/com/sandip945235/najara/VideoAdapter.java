package com.sandip945235.najara;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VH> {
    Context ctx;
    List<Video> list;

    public VideoAdapter(Context c, List<Video> l) {
        ctx = c;
        list = l;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.item_video, p, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Video v = list.get(pos);
        h.title.setText(v.title);
        h.rating.setText("⭐ " + v.rating);
        Glide.with(ctx).load(v.poster).into(h.thumb);

        h.itemView.setOnClickListener(x -> {
            Intent i = new Intent(ctx, DetailActivity.class);
            i.putExtra("title", v.title);
            i.putExtra("poster", v.poster);
            i.putExtra("category", v.category);
            i.putExtra("embedLink", v.embedLink);
            i.putExtra("downloadLink", v.downloadLink);
            i.putExtra("rating", v.rating);
            i.putExtra("print", v.print);
            i.putExtra("industry", v.industry);
            i.putExtra("language", v.language);
            i.putExtra("quality", v.quality);
            ctx.startActivity(i);
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView thumb;
        TextView title, rating;
        VH(View v) {
            super(v);
            thumb = v.findViewById(R.id.thumb);
            title = v.findViewById(R.id.itemTitle);
            rating = v.findViewById(R.id.itemRating);
        }
    }
                                      }
