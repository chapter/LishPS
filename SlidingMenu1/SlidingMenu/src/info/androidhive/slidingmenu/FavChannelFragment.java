package info.androidhive.slidingmenu;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import database.MySQLiteHelper;
import entity.FavEntity;

public class FavChannelFragment extends Fragment {
	
	public FavChannelFragment(){}
	ListView lv;
    Context context;
	MySQLiteHelper mySql;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_fav_channel, container, false);
        lv=(ListView) rootView.findViewById(R.id.list_fav);
        context = rootView.getContext();
		mySql = new MySQLiteHelper(context);
    	mySql.openDataBase();
        List<FavEntity> allFav =  mySql.getAllFav();
        lv.setAdapter(new FavAdapter (this, allFav));
        return rootView;
    }
}
