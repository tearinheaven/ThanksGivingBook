package com.zte.thanksbook.activities;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zte.thanksbook.R;

public class PhotoShowFragment extends Fragment{

	private List<Uri> photos;
	
	public PhotoShowFragment(List<Uri> photos)
	{
		this.photos = photos;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.photo_show_fragment, null);
		layout.setBackgroundResource(R.drawable.border_corner);
		GridView gridView = (GridView)layout.findViewById(R.id.gridview);
		gridView.setAdapter(new MyPhotoAdapter(getActivity(),photos));
		return layout;
	}

}

class MyPhotoAdapter extends BaseAdapter
{
	private Context context;
	private List<Uri> photos;
	
	public MyPhotoAdapter(Context context,List<Uri> maps)
	{
		this.context = context;
		this.photos =maps; 
	}
	
	@Override
	public int getCount() {
		return photos.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		ImageView imageView;
        if (convertView == null) {  
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(400, 400));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(getCompressBitmap(photos.get(index)));
        return imageView;
	}
	
	private Bitmap getCompressBitmap(Uri photo)
	{
		Bitmap compressPhoto = null;
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(photo.getPath(), options);
		options.inSampleSize = calculateInSampleSize(options,400,400);	//缩放值后续计算
		options.inJustDecodeBounds = false;
		compressPhoto=  BitmapFactory.decodeFile(photo.getPath(), options);	
		return compressPhoto;
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight)
	{
		final int height = options.outHeight;//获取图片的高
		final int width = options.outWidth;//获取图片的框
		int inSampleSize = 4;
		if (height > reqHeight || width > reqWidth)
		{
			final int heightRatio = Math.round((float) height/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		Log.i("size",""+inSampleSize);
		return inSampleSize;
	}
}
