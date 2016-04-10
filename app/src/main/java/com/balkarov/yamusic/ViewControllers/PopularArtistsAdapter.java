package com.balkarov.yamusic.ViewControllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.balkarov.yamusic.Model.Artist;
import com.balkarov.yamusic.R;
import com.balkarov.yamusic.Utilities.Prefix;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class PopularArtistsAdapter extends BaseAdapter {

    Context context;
    List<Artist> artists;
    LayoutInflater inflater;

    public PopularArtistsAdapter(Context ct, List<Artist> artistList) {
        context = ct;
        artists = artistList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return artists.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_artist, null);
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
            viewHolder.tracksTextView = (TextView) convertView.findViewById(R.id.tracksTextView);
            viewHolder.coverImageView = (ImageView) convertView.findViewById(R.id.coverImageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Artist artist = artists.get(position);
        if (viewHolder.nameTextView != null)
            viewHolder.nameTextView.setText(artist.getName());
        if (viewHolder.tracksTextView != null)
            viewHolder.tracksTextView.setText(artist.getTracks() + " " + "трек" + Prefix.getStringPrefix(artist.getTracks()));
        if (viewHolder.coverImageView != null)
            ImageLoader.getInstance().displayImage(artist.cover.getSmall(), viewHolder.coverImageView);

        return convertView;
    }
}

class ViewHolder {
    TextView nameTextView;
    TextView tracksTextView;
    ImageView coverImageView;
}
