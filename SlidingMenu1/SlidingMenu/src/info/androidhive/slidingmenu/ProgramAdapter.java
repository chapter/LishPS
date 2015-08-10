package info.androidhive.slidingmenu;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import entity.ProgramEntity;

public class ProgramAdapter extends BaseAdapter{

	ArrayList<ProgramEntity> result = new ArrayList<ProgramEntity>();
	Context context;
	private static LayoutInflater inflater=null;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return result.size();
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
		View rowView = inflater.inflate(R.layout.channel_entity, null);
		TextView txtStart = (TextView) rowView.findViewById(R.id.txtStart);
		TextView txtEnd = (TextView) rowView.findViewById(R.id.txtEnd);
		TextView txtName = (TextView) rowView.findViewById(R.id.txtName);
		int tmp = result.get(position).start;
		int hour = tmp/60;
		int minus = tmp - hour*60;
		txtStart.setText(String.format("%02d", hour)+":"+String.format("%02d", minus));
		
		tmp = result.get(position).end;
		hour = tmp/60;
		minus = tmp - hour*60;
		txtEnd.setText(String.format("%02d", hour)+":"+String.format("%02d", minus));
//		txtStart.setText(result.get(position).start);
//		txtEnd.setText(result.get(position).end);
		txtName.setText(result.get(position).name);		
		return rowView;
	}

	
	public ProgramAdapter(Activity mainActivity,ArrayList<ProgramEntity> tmp)
	{
		context=mainActivity;
		result = tmp;
		inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	


	}	

}
