package backgroundprocess;

import info.androidhive.slidingmenu.R;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import webservice.RequestData;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import database.MySQLiteHelper;
import entity.ChannelEntity;
import entity.FavEntity;
import entity.ProgramEntity;


import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Days;

public class NotificationProcess extends Service{

	MySQLiteHelper mySql;
	List<ChannelEntity> allChannel ;
	String two_day_before;
	NotificationManager manager;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// start first
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onStart(Intent intent, int startId) {
	}



	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mySql = new MySQLiteHelper(getApplicationContext());
		new Thread(new ThreadReminder()).start();  		
		return super.onStartCommand(intent, flags, startId);
	}

	private class ThreadReminder implements Runnable
	{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Calendar two_day_cal = Calendar.getInstance();
	        two_day_cal.add(Calendar.DATE, -2);
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			two_day_before = dateFormat.format(two_day_cal.getTime());
			mySql.openDataBase();
			mySql.deleteOldProgramData(two_day_before);
	        allChannel =  mySql.getAllChannel();
			while (true)
			{
				two_day_cal = Calendar.getInstance();
		        two_day_cal.add(Calendar.DATE, -2);
				if(dateFormat.format(two_day_cal.getTime()) != two_day_before)
				{
					two_day_before = dateFormat.format(two_day_cal.getTime());
					mySql.deleteOldProgramData(two_day_before);
					String[] listChannelId = new String[allChannel.size()] ;
					for (int i=0;i<allChannel.size();i++)
					{
						listChannelId[i] = Integer.toString(allChannel.get(i).channelId);
					}
			        new PaserDownloadAll().execute(listChannelId);
				}				
				Calendar current_cal = Calendar.getInstance();
				List<FavEntity> listFav =  mySql.getAllFav();
				for(int i=0;i<listFav.size();i++)
				{
					FavEntity fav = listFav.get(i);
					int hour = current_cal.get(Calendar.HOUR_OF_DAY);
					int minute = current_cal.get(Calendar.MINUTE);
					int currentTime = hour*60+minute;
					String result = dateFormat.format(two_day_cal.getTime());
					ArrayList<ProgramEntity> listProgram = mySql.getAllProgramByChannelAndDay(fav.getChannelId(), result);
					for(int j =0;j< listProgram.size() ;j++ )
					{
						int startTime = listProgram.get(j).start;						
						// && startTime-currentTime >= 10 && startTime-currentTime < 20
						if(listProgram.get(j).name.contains(fav.getName()) && startTime-currentTime >= 10 && startTime-currentTime <= 20)
						{
							createNotification(listProgram.get(j),j);				
						}
					}
				}
				try {
					Thread.sleep(10*60*1000);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}

		}

	}
	public void createNotification(ProgramEntity programEntity,int id) {

		int tmp =programEntity.start;
		int hour = tmp/60;
		int minus = tmp - hour*60;
		
		NotificationCompat.Builder builder =  new NotificationCompat.Builder(this)  ;
		builder.setLargeIcon(getBitmapFromAsset(getApplicationContext(), Integer.toString(programEntity.channelId)));	
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(programEntity.name.replace("  ","").replace("\n", ""));
		builder.setContentText("Thời gian bắt đầu : "+String.format("%02d", hour)+":"+String.format("%02d", minus));
//		Intent notificationIntent = new Intent(this, ImageView.class);  
//		notificationIntent.putExtra("Url",chapter.Url);
//		notificationIntent.putExtra("StoryName",chapter.StoryName);
//		notificationIntent.putExtra("ChapNumber", chapter.ChapterNumber);
//		notificationIntent.putExtra("Type", 0);
//		PendingIntent contentIntent = PendingIntent.getActivity(this, id, notificationIntent,   
//				PendingIntent.FLAG_UPDATE_CURRENT);  

//		builder.setContentIntent(contentIntent);  
		builder.setAutoCancel(true);
		builder.setLights(Color.BLUE, 5000, 500);
		builder.setStyle(new NotificationCompat.InboxStyle());
		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		builder.setSound(alarmSound);
		// Add as notification  
		manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
		manager.notify(id, builder.build());    
	}
	
	public static Bitmap getBitmapFromAsset(Context context, String filePath) {
	    AssetManager assetManager = context.getAssets();
	    
	    InputStream istr;
	    Bitmap bitmap = null;
	    try {
	    	istr = assetManager.open(filePath+".png");
	        bitmap = BitmapFactory.decodeStream(istr);
	    } catch (Exception e) {
	    }
	    return bitmap;
	}
	
	private class PaserDownloadAll extends AsyncTask<String, Void, Void>
	{
		RequestData requestData = new RequestData();
		@Override
		protected void onPreExecute()
		{
		}

		@Override
		protected Void doInBackground(String... allChannelId) //0 is day , 1 is month, 2 is year, 3 is channel id
		{
			Calendar current_cal = Calendar.getInstance();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String[] result = dateFormat.format(current_cal.getTime()).split("/");
			ArrayList<ProgramEntity> resultRequest = new ArrayList<ProgramEntity>();
			for (String channelId : allChannelId) {
				resultRequest = mySql.getAllProgramByChannelAndDay(Integer.parseInt(channelId), dateFormat.format(current_cal.getTime()).toString());
				if(resultRequest.size()==0)
				{
					resultRequest = requestData.getAllProgram(result[0], result[1], result[2], channelId);
					if(resultRequest.size()>0)
					{
						mySql.insertRegion(resultRequest);
					}
				}
				resultRequest.clear();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... progress) {
		}

		@Override
		protected void onPostExecute(Void arg0)
		{ 
		
		}	


	}
}
