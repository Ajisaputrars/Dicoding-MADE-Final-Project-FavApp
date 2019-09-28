package com.example.ajisaputrars.myfavoriteapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.RecycleViewHolder> {

    private ArrayList<Movie> movies = new ArrayList<>();
    private Context context;

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public FavoriteMovieAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<Movie> items) {
        movies.clear();
        movies.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_movie, parent, false);
        return new FavoriteMovieAdapter.RecycleViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {
        holder.tvTitle.setText(movies.get(position).getTitle());
        holder.tvOverview.setText(movies.get(position).getOverview());
        Glide.with(context).load(movies.get(position).getPoster_path_string())
                .into(holder.imgPhoto);
    }

    @Override
    public int getItemCount() {
        Log.d("movies.size", "jumlahnya adalah " +movies.size());
        return movies.size();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvOverview;
        ImageView imgPhoto;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.txt_favorite_movie_name);
            tvOverview = itemView.findViewById(R.id.txt_favorite_movie_description);
            imgPhoto = itemView.findViewById(R.id.img_favorite_movie_photo);
        }
    }
}
