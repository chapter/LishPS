package info.androidhive.slidingmenu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsChannelByDayAdapter extends FragmentPagerAdapter {

	private String mChannelId;
	public TabsChannelByDayAdapter(FragmentManager fm,String channelId) {
        super(fm);
        mChannelId = channelId;
    }
 
    @Override
    public Fragment getItem(int index) { 
    	return new ProgramDetail(index , mChannelId);
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}
