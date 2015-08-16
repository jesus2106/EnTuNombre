package com.dulcerefugio.app.entunombre.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dulcerefugio.app.entunombre.R;
import com.dulcerefugio.app.entunombre.data.dao.YoutubeVideo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

    //=======================================================================
    //FIELDS
    //=======================================================================
    private ImageLoader mImageLoader;
    private List<YoutubeVideo> youtubeVideoList;

    //=======================================================================
    //CONSTRUCTORS
    //=======================================================================

    public VideosAdapter(List<YoutubeVideo> youtubeVideoList) {
        this.youtubeVideoList = youtubeVideoList;
        this.mImageLoader = ImageLoader.getInstance();
    }

    //=======================================================================
    //OVERRIDEN METHODS
    //=======================================================================

    @Override
    public VideosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_video, parent, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.ivVideoThumbnail);
        TextView tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        TextView tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);

        return new ViewHolder(itemView, imageView, tvTitle, tvDescription);
    }

    @Override
    public void onBindViewHolder(VideosAdapter.ViewHolder viewHolder, int position) {
        YoutubeVideo youtubeVideo = youtubeVideoList.get(position);
        if (youtubeVideo != null) {
            viewHolder.tvTitle.setText(youtubeVideo.getTitle());
            viewHolder.tvDescription.setText(getShortDescription(youtubeVideo.getDescription()));
            mImageLoader.displayImage(youtubeVideo.getThumbnail_url(), viewHolder.imageView);
            Log.d("", youtubeVideo.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return youtubeVideoList.size();
    }

    //=======================================================================
    //METHODS
    //=======================================================================
    public YoutubeVideo getItem(int position) {
        return youtubeVideoList.get(position);
    }

    private String getShortDescription(String description) {
        if (description == null)
            return "No overview available";

        if (description.length() <= 111)
            return description;

        return description.trim().substring(0, 111) + "...";
    }

    //=======================================================================
    //INNER CLASSES
    //=======================================================================
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView tvTitle;
        public TextView tvDescription;

        public ViewHolder(View itemView, ImageView imageView, TextView tvTitle, TextView tvDescription) {
            super(itemView);
            this.imageView = imageView;
            this.tvTitle = tvTitle;
            this.tvDescription = tvDescription;
        }
    }
}
