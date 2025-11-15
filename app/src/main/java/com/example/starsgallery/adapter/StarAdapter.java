package com.example.starsgallery.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.starsgallery.MainActivity;
import com.example.starsgallery.R;
import com.example.starsgallery.beans.Star;
import com.example.starsgallery.service.StarService;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.StarViewHolder> implements Filterable {

    private static final String TAG = "StarAdapter";
    private List<Star> stars;
    private List<Star> starsFilter;
    private Context context;
    private NewFilter mfilter;

    public StarAdapter(Context context, List<Star> stars) {
        this.context = context;
        this.stars = stars;
        this.starsFilter = new ArrayList<>(stars);
        this.mfilter = new NewFilter(this);
    }



    @Override
    public void onBindViewHolder(@NonNull StarViewHolder holder, int position) {
        Glide.with(context)
                .asBitmap()
                .load(starsFilter.get(position).getImg())
                .apply(new RequestOptions().override(100, 100))
                .into(holder.img);

        holder.name.setText(starsFilter.get(position).getName().toUpperCase());
        holder.stars.setRating(starsFilter.get(position).getRating());
        holder.idss.setText(String.valueOf(starsFilter.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        return starsFilter.size();
    }

    @Override
    public Filter getFilter() {
        return mfilter;
    }

    public static class StarViewHolder extends RecyclerView.ViewHolder {
        TextView idss, name;
        ImageView img;
        RatingBar stars;

        public StarViewHolder(@NonNull View itemView) {
            super(itemView);
            idss = itemView.findViewById(R.id.idss);
            img = itemView.findViewById(R.id.imgStar);
            name = itemView.findViewById(R.id.tvName);
            stars = itemView.findViewById(R.id.rating);
        }
    }

    public class NewFilter extends Filter {
        public RecyclerView.Adapter mAdapter;

        public NewFilter(RecyclerView.Adapter mAdapter) {
            this.mAdapter = mAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Star> filtered = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filtered.addAll(stars);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Star p : stars) {
                    if (p.getName().toLowerCase().startsWith(filterPattern)) {
                        filtered.add(p);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered;
            results.count = filtered.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            starsFilter = (List<Star>) filterResults.values;
            mAdapter.notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public StarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.star_item, viewGroup, false);
        final StarAdapter.StarViewHolder holder = new StarAdapter.StarViewHolder(v);

        holder.itemView.setOnClickListener(view -> {
            // Get the selected star from the data model
            Star star = starsFilter.get(holder.getAdapterPosition());

            // Inflate popup layout
            View popup = LayoutInflater.from(context).inflate(R.layout.star_edit_item, null, false);
            final ImageView img = popup.findViewById(R.id.img);
            final RatingBar bar = popup.findViewById(R.id.ratingBar);
            final TextView idss = popup.findViewById(R.id.idsss);

            // Set values in popup
            Glide.with(context).load(star.getImg()).into(img); // load image
            bar.setRating(star.getRating()); // set rating from model
            idss.setText(String.valueOf(star.getId())); // set id

            // Show dialog
            new AlertDialog.Builder(context)
                    .setTitle("Notez :")
                    .setMessage("Donner une note entre 1 et 5 :")
                    .setView(popup)
                    .setPositiveButton("Valider", (dialog1, which) -> {
                        // Update rating
                        star.setRating(bar.getRating());
                        StarService.getInstance().update(star);
                        notifyItemChanged(holder.getAdapterPosition());
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });

        return holder;
    }

}