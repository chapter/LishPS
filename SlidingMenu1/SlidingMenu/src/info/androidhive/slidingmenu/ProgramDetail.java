package info.androidhive.slidingmenu;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import webservice.RequestData;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import database.MySQLiteHelper;
import entity.FavEntity;
import entity.ProgramEntity;
public class ProgramDetail extends Fragment{
	ListView lv;
	View rootView ;
	ParserTask mParserTask;
	int mIndex;
	String mChannelId;
	MySQLiteHelper mySql;
	ArrayList<ProgramEntity> result;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.channel_detail, container, false);
		lv=(ListView) rootView.findViewById(R.id.list_program);
		registerForContextMenu(lv);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		mySql = new MySQLiteHelper(rootView.getContext());
		if(mIndex==0)
		{
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			String[] result = dateFormat.format(cal.getTime()).split("/");
			if(result.length>=3)
			{
				new ParserTask().execute(result[0],result[1],result[2],mChannelId);
			}
		}
		else if(mIndex ==1)
		{
			Calendar cal = Calendar.getInstance();
			String[] result = dateFormat.format(cal.getTime()).split("/");
			if(result.length>=3)
			{
				new ParserTask().execute(result[0],result[1],result[2],mChannelId);
			}
		}
		else if(mIndex ==2)
		{
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 1);
			String[] result = dateFormat.format(cal.getTime()).split("/");
			if(result.length>=3)
			{
				new ParserTask().execute(result[0],result[1],result[2],mChannelId);
			}
		}
		return rootView;
	}
	public ProgramDetail(int index, String channelId) {
		mIndex = index;
		mChannelId = channelId;
	}
	public ProgramDetail()
	{
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {		       
		if (v.getId()==R.id.list_program) 
		{
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
			menu.setHeaderTitle(result.get(info.position).name);
			menu.add(Menu.NONE, 0, 0,"Thêm vào yêu thích");					
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item){
		if(getUserVisibleHint())
		{
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
			Toast toast;		
			Context context = rootView.getContext();
			int duration = Toast.LENGTH_SHORT;
			String nameStory= (String)result.get(info.position).name;
			String[] tmp = nameStory.replace("Phát lại", "").replace("Plại", "").replace("–","-").replace(":", "-").replace("Tập ", "-").split("-");
			if(tmp.length>0)
			{
				mySql.openDataBase();
				if(!mySql.checkContainsFav(tmp[0]))
				{
					FavEntity favEntity = new FavEntity();
					favEntity.setName(tmp[0]);
					favEntity.setChannelId(Integer.parseInt(mChannelId));					
					mySql.insertFav(favEntity);
					toast = Toast.makeText(context, "Đã thêm chương trình \""+tmp[0]+"\" vào mục yêu thích", duration);
					toast.show();
				}
				else
				{
					toast = Toast.makeText(context, "Chương trình đã có trong mục yêu thích", duration);
					toast.show();
				}
			}
			return true;
		}

		return false;

	}

	private class ParserTask extends AsyncTask<String, Integer, ArrayList<ProgramEntity>>
	{
		RequestData requestData = new RequestData();
		@Override
		protected void onPreExecute()
		{
		}

		@Override
		protected ArrayList<ProgramEntity> doInBackground(String... params) //0 is day , 1 is month, 2 is year, 3 is channel id
		{
			ArrayList<ProgramEntity> resultRequest = new ArrayList<ProgramEntity>();
			mySql.openDataBase();
			resultRequest = mySql.getAllProgramByChannelAndDay(Integer.parseInt(params[3]), params[0]+"/"+params[1]+"/"+params[2]); 
			if(resultRequest.size()==0)
			{
				resultRequest = requestData.getAllProgram(params[0], params[1], params[2], params[3]);
				if(resultRequest.size()>0)
				{
					mySql.insertRegion(resultRequest);
				}
			}			
			//			resultRequest = requestData.getAllProgram(params[0], params[1], params[2], params[3]);
			return resultRequest;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
		}

		@Override
		protected void onPostExecute(ArrayList<ProgramEntity> arg0)
		{ 
			lv.setAdapter(new ProgramAdapter(ProgramDetail.this.getActivity(),arg0));
			result= arg0;
		}	


	}
}
