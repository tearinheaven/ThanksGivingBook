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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zte.thanksbook.R;

public class PhotoShowFragment extends Fragment{

	private List<Uri> photos;
	
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	private DisplayImageOptions options;
	
	public PhotoShowFragment(List<Uri> photos)
	{
		this.photos = photos;
		options = new DisplayImageOptions.Builder()
		//.showImageOnLoading(R.drawable.ic_stub)
		//.showImageForEmptyUri(R.drawable.ic_empty)
		//.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true)
		.cacheOnDisc(false)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
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
	            imageView.setLayoutParams(new GridView.LayoutParams(120, 120));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(5, 5, 5, 5);
	        } else {
	            imageView = (ImageView) convertView;
	        }
	        Log.i("--------------------",photos.get(index).getScheme());
	        imageLoader.displayImage("file:"+photos.get(index).getSchemeSpecificPart(), imageView, options, null);
	        return imageView;
		}
		
	}
}


