package info.androidhive.slidingmenu;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import entity.ChannelEntity;

@SuppressLint("InflateParams") public class ChannelAdapter extends BaseAdapter {

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
//		return result.length;
		return mListChannel.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public class Holder
    {
        TextView tv;
        ImageView img;
    }
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=new Holder();
		View rowView;       
		rowView = inflater.inflate(R.layout.program_list, null);
		holder.tv=(TextView) rowView.findViewById(R.id.program_name);
		holder.img=(ImageView) rowView.findViewById(R.id.program_image);       
		holder.tv.setText(mListChannel.get(position).name);
		holder.img.setImageBitmap(mListChannel.get(position).logo);
		rowView.setOnClickListener(new OnClickListener() {            
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
								Intent intent = new Intent(v.getContext(), ScheduleChannel.class);
								intent.putExtra("channel",mListChannel.get(position).name);
								intent.putExtra("channelId",mListChannel.get(position).channelId);
								v.getContext().startActivity(intent);
			}
		});   
		return rowView;
	}
	String [] result;
	Context context;
	int [] imageId;
	 List<ChannelEntity> mListChannel;
	private static LayoutInflater inflater=null;
	public ChannelAdapter (ChannelFragment mainActivity, List<ChannelEntity> listChannel) {
		// TODO Auto-generated constructor stub	
		mListChannel = listChannel;
		context=mainActivity.getActivity();		
		inflater = ( LayoutInflater )context.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}	

}
