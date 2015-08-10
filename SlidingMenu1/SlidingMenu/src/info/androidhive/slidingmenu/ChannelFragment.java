package info.androidhive.slidingmenu;

import java.io.IOException;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import database.MySQLiteHelper;
import entity.ChannelEntity;

public class ChannelFragment extends Fragment {
	
	public ChannelFragment(){}
	
	ListView lv;
    Context context;
	MySQLiteHelper mySql;

    public static int [] prgmImages={R.drawable.ic_channel,R.drawable.ic_channel,R.drawable.ic_channel,R.drawable.ic_channel,R.drawable.ic_channel,R.drawable.ic_channel,R.drawable.ic_channel,R.drawable.ic_channel,R.drawable.ic_channel};
    public static String [] prgmNameList={"Let Us C","c++","JAVA","Jsp","Microsoft .Net","Android","PHP","Jquery","JavaScript"};

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) { 
        View rootView = inflater.inflate(R.layout.fragment_channel, container, false);
        lv=(ListView) rootView.findViewById(R.id.list_channel);
        context = rootView.getContext();
        initSql();
        List<ChannelEntity> allChannel =  mySql.getAllChannel();
//        lv.setAdapter(new ChannelAdapter (this, prgmNameList,prgmImages));
        lv.setAdapter(new ChannelAdapter (this, allChannel));
        return rootView;
    }
	private void initSql()
	{
		mySql = new MySQLiteHelper(context);
		try {

			mySql.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}

		try {

			mySql.openDataBase();

		}catch(SQLException sqle){

			throw sqle;

		}
	}
	


  
}
