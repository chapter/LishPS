package info.androidhive.slidingmenu;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingsFragment extends Fragment {

	public SettingsFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
		Spinner spinner = (Spinner) rootView.findViewById(R.id.spinnerMinus);

		// Spinner click listener

		// Spinner Drop down elements
		List<String> time = new ArrayList<String>();
		time.add("15");
		time.add("30");
		time.add("45");
		time.add("60");
		time.add("75");
		time.add("90");

		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, time);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);    
		return rootView;
	}
}
