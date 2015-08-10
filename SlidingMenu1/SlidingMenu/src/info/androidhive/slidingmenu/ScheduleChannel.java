package info.androidhive.slidingmenu;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class ScheduleChannel extends FragmentActivity implements ActionBar.TabListener
{
	private ViewPager viewPager;
    private TabsChannelByDayAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = {"a","b","c" };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_channel);
        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        String channelId = String.valueOf(getIntent().getExtras().getInt("channelId"));
        mAdapter = new TabsChannelByDayAdapter(getSupportFragmentManager(),channelId);
        String title = getIntent().getExtras().getString("channel");
        actionBar.setTitle(title);

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        
 
        // Adding Tabs
        DateFormat dateFormat = new SimpleDateFormat("dd/MM");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        tabs[0] = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, 1);
        tabs[1] = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, 1);
        tabs[2] = dateFormat.format(cal.getTime());
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }
        
        
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        	 
            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }
 
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
 
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }
    
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

}
