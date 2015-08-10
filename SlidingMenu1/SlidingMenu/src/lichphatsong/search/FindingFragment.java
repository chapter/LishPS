package lichphatsong.search;

import info.androidhive.slidingmenu.R;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import database.MySQLiteHelper;
import entity.ProgramEntity;

public class FindingFragment extends Fragment {
	
	public FindingFragment(){}
	ImageView image_search;
	TextView txt_search_box;
	MySQLiteHelper mySql ;
	ArrayList<ProgramEntity> search_result;
	ListView lvSearch;
	FindingFragment tmp;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		tmp =this;
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        image_search = (ImageView) rootView.findViewById(R.id.image_search); 
        txt_search_box = (TextView) rootView.findViewById(R.id.txt_search_box);
        lvSearch = (ListView) rootView.findViewById(R.id.lv_search_result);
        
		mySql = new MySQLiteHelper(rootView.getContext());
		mySql.openDataBase();
        image_search.setOnClickListener(new OnClickListener() {
	        // Start new list activity
	        public void onClick(View v) {
	        	search_result =  mySql.getAllProgramByName(txt_search_box.getText().toString().trim());
	        	lvSearch.setAdapter(new FindingAdapter(tmp, search_result));
	        	
//	        	int days = Days.daysBetween(date1, date2).getDays();
	        	}
	    });
        return rootView;
    }
}
