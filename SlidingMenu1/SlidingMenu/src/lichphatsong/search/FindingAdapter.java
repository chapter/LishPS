package lichphatsong.search;

import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.R.id;
import info.androidhive.slidingmenu.R.layout;

import java.util.ArrayList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import entity.ProgramEntity;

public class FindingAdapter extends BaseAdapter {

	ArrayList<ProgramEntity> mListProgram;
	Context mContext;
	private static LayoutInflater inflater=null;
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListProgram.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rowView;       
		rowView = inflater.inflate(R.layout.finding_list, null);
		TextView tvDay=(TextView) rowView.findViewById(R.id.txt_search_result_day);
		TextView tvStart=(TextView) rowView.findViewById(R.id.txt_search_result_start_hour);
		TextView tvEnd=(TextView) rowView.findViewById(R.id.txt_search_result_end_hour);
		TextView tvChannel=(TextView) rowView.findViewById(R.id.txt_search_result_channel);
		TextView tvProgram=(TextView) rowView.findViewById(R.id.txt_search_result_program);
		
		tvDay.setText(mListProgram.get(position).day);
		
		int tmp = mListProgram.get(position).start;
		int hour = tmp/60;
		int minus = tmp - hour*60;
		tvStart.setText(String.format("%02d", hour)+":"+String.format("%02d", minus));
		
		tmp = mListProgram.get(position).end;
		hour = tmp/60;
		minus = tmp - hour*60;
		tvEnd.setText(String.format("%02d", hour)+":"+String.format("%02d", minus));
		
		tvChannel.setText(mListProgram.get(position).channelName);
		tvProgram.setText(mListProgram.get(position).name);
		return rowView;
	}

	
	public FindingAdapter (FindingFragment mainActivity, ArrayList<ProgramEntity> listProgram) {
		// TODO Auto-generated constructor stub	
		mListProgram = listProgram;
		mContext=mainActivity.getActivity();		
		inflater = ( LayoutInflater )mContext.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
}
