package info.androidhive.slidingmenu;

import java.io.InputStream;
import java.util.List;

import database.MySQLiteHelper;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import entity.FavEntity;

public class FavAdapter  extends BaseAdapter{	
	String [] result;
	Context context;
	int [] imageId;
	List<FavEntity> m_listFav;
	MySQLiteHelper mySql ;
	FavAdapter favAdapter;
	private static LayoutInflater inflater=null;
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return m_listFav.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rowView;       
		rowView = inflater.inflate(R.layout.fav_list, null);
		TextView tv=(TextView) rowView.findViewById(R.id.txt_fav_name);
		tv.setText(m_listFav.get(position).getName());
		ImageView image = (ImageView) rowView.findViewById(R.id.image_fav_channel);
		image.setImageBitmap(getBitmapFromAsset(rowView.getContext(), Integer.toString(m_listFav.get(position).getChannelId()))); 
		ImageView image_delete = (ImageView) rowView.findViewById(R.id.image_fav_delete);
		image_delete.setOnClickListener(new OnClickListener() {
	        // Start new list activity
	        public void onClick(View v) {	        	
	        	mySql.openDataBase();
	        	mySql.deleteFav((m_listFav.get(position).getName()));
	        	m_listFav.clear();
	        	m_listFav =  mySql.getAllFav();	
	        	favAdapter.notifyDataSetChanged();
	        	}
	    });
		return rowView;

	}
	
	public FavAdapter (FavChannelFragment mainActivity, List<FavEntity> listFav) {
		// TODO Auto-generated constructor stub	
		m_listFav = listFav;
		context=mainActivity.getActivity();		
		inflater = ( LayoutInflater )context.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mySql = new MySQLiteHelper(context);
		favAdapter = this;
	}
	
	public static Bitmap getBitmapFromAsset(Context context, String filePath) {
	    AssetManager assetManager = context.getAssets();
	    
	    InputStream istr;
	    Bitmap bitmap = null;
	    try {
	    	istr = assetManager.open(filePath+".png");
	        bitmap = BitmapFactory.decodeStream(istr);
	    } catch (Exception e) {
	    }
	    return bitmap;
	}

}
