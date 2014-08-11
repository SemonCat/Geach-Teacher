package com.semoncat.geach.teacher.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.semoncat.geach.teacher.R;
import com.semoncat.geach.teacher.bean.PPTImage;
import com.semoncat.geach.teacher.bean.PPTsEntity;
import com.semoncat.geach.teacher.bean.Video;
import com.semoncat.geach.teacher.bean.VideosEntity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import me.grantland.widget.AutofitTextView;

/**
 * Created by SemonCat on 2014/8/5.
 */
public class VideoListAdapter extends BaseAdapter {

    private VideosEntity videosEntity;

    public VideoListAdapter(VideosEntity videosEntity) {
        this.videosEntity = videosEntity;
    }

    @Override
    public int getCount() {
        return videosEntity.getVideos().size();
    }

    @Override
    public Video getItem(int position) {
        return videosEntity.getVideos().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = ((LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_video_list,
                    parent, false);

            mViewHolder = new ViewHolder();
            mViewHolder.VideoFileName = (TextView) convertView.findViewById(R.id.VideoFileName);
            mViewHolder.VideoPreview = (ImageView) convertView.findViewById(R.id.VideoPreview);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        Video video = getItem(position);

        mViewHolder.VideoFileName.setText(video.getName());

        return convertView;
    }

    public Bitmap getThumbnail(ContentResolver cr, Uri uri) {
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Cursor cursor = cr.query(uri, new String[]{MediaStore.Video.Media._ID}, null, null, null);

        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }
        cursor.moveToFirst();
        String videoId = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));  //image id in image table.s

        if (videoId == null) {
            return null;
        }
        cursor.close();
        long videoIdLong = Long.parseLong(videoId);
        bitmap = MediaStore.Video.Thumbnails.getThumbnail(cr, videoIdLong, MediaStore.Images.Thumbnails.MICRO_KIND, options);

        return bitmap;
    }

    class ViewHolder {
        TextView VideoFileName;
        ImageView VideoPreview;
    }
}
