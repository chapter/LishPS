package database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import entity.ChannelEntity;
import entity.FavEntity;
import entity.ProgramEntity;

public class MySQLiteHelper extends SQLiteOpenHelper{

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.myContext = context;
		ContextWrapper cw =new ContextWrapper(myContext);
		DB_PATH =cw.getFilesDir().getAbsolutePath()+ "/databases/"; 
		File databaseFile = new File( DB_PATH);
		if (!databaseFile.exists()){
			databaseFile.mkdir();
		}

		// TODO Auto-generated constructor stub
	}
	private final Context myContext;
	private static final int DATABASE_VERSION = 2;
	private SQLiteDatabase myDataBase; 
	// Database Name
	private static final String DATABASE_NAME = "LichPhatSong";
	private String DB_PATH = "/data/data/com.quasar.lichphatsong/databases/";
	// Table Names
	private static final String Table_Channel = "Channel";
	private static final String Table_Program = "Program";
	private static final String Table_Fav = "Favourite";
	private static final String Table_Config = "Config";

	// Common column names
	private static final String KEY_ID = "_id";

	// NOTES Table - column nmaes
	private static final String KEY_NAME = "name";
	private static final String KEY_CHANNEL_ID = "channel_id";
	private static final String KEY_BRIEF = "brief";
	private static final String KEY_LOGO = "logo";
	private static final String KEY_SOURCE = "source";
	
	private static final String KEY_START = "start_time";
	private static final String KEY_END = "end_time";
	private static final String KEY_DAY = "day";
	
	private static final String KEY_KEY = "key";
	private static final String KEY_VALUE = "value";

	private static final String CREATE_TABLE_CHANNEL  = "CREATE TABLE "
			+ Table_Channel + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME
			+ " TEXT," + KEY_CHANNEL_ID + " INTEGER," + KEY_BRIEF
			+ " TEXT," + KEY_LOGO + " BLOB, " + KEY_SOURCE + " TEXT )";
	
	private static final String CREATE_TABLE_PROGRAM  = "CREATE TABLE "
			+ Table_Program + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME
			+ " TEXT," + KEY_CHANNEL_ID + " INTEGER," + KEY_START
			+ " INTEGER," + KEY_END + " INTEGER, " + KEY_DAY + " TEXT )";

	private static final String CREATE_TABLE_FAV  = "CREATE TABLE if not exists "
			+ Table_Fav + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_NAME
			+ " TEXT, "+KEY_CHANNEL_ID+" INTEGER )";
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TABLE_CHANNEL);
		db.execSQL(CREATE_TABLE_PROGRAM);
		db.execSQL(CREATE_TABLE_FAV);	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		//db.execSQL("DROP TABLE IF EXISTS " + Table_Channel);
		//db.execSQL("DROP TABLE IF EXISTS " + Table_Channel);
		//db.execSQL("DROP TABLE IF EXISTS " + Table_Channel);
		onCreate(db);
	}

	public List<ChannelEntity> getAllChannel()
	{
		List<ChannelEntity> results= new ArrayList<ChannelEntity>();
		String query = " SELECT "+KEY_ID+" , "+KEY_NAME+" , "+KEY_CHANNEL_ID+" , "+ KEY_BRIEF+" , "+ KEY_LOGO+" FROM "+Table_Channel;
		Cursor c = myDataBase.rawQuery(query,null);		
		if (c!=null) 
		{
			if(!c.moveToFirst())
				return results;			
		}
		do
		{
			ChannelEntity channel  = new ChannelEntity();
			channel.id=c.getInt(c.getColumnIndex(KEY_ID));
			channel.name=c.getString(c.getColumnIndex(KEY_NAME));
			channel.channelId=c.getInt(c.getColumnIndex(KEY_CHANNEL_ID));
			channel.brief=c.getString(c.getColumnIndex(KEY_BRIEF));
			
			
			channel.logo = getBitmapFromAsset(myContext, Integer.toString(channel.channelId)+"");
			results.add(channel);
		}
		while(c.moveToNext());
		return results;	
	}

	public List<FavEntity> getAllFav()
	{
		List<FavEntity> results= new ArrayList<FavEntity>();
		String query = " SELECT "+KEY_ID+" , "+KEY_NAME+" , "+ KEY_CHANNEL_ID +" FROM "+Table_Fav;
		Cursor c = myDataBase.rawQuery(query,null);		
		if (c!=null) 
		{
			if(!c.moveToFirst())
				return results;			
		}
		do
		{
			FavEntity favEntiity  = new FavEntity();
			favEntiity.setId(c.getInt(c.getColumnIndex(KEY_ID)));
			favEntiity.setName(c.getString(c.getColumnIndex(KEY_NAME)));	
			favEntiity.setChannelId(c.getInt(c.getColumnIndex(KEY_CHANNEL_ID)));
			results.add(favEntiity);
		}
		while(c.moveToNext());
		return results;	
	}
	
	public Boolean checkContainsFav(String name)
	{
		String query = " SELECT "+KEY_ID+" , "+KEY_NAME+" FROM "+Table_Fav +" WHERE "
						+ KEY_NAME + " = ? "; 
		Cursor c = myDataBase.rawQuery(query, new String [] {name});		
		if (c!=null) 
		{
			if(!c.moveToFirst())
				return false;
			return true;
		}
		return false;	
	}
	
	public void insertFav(FavEntity favEntity)
	{
		ContentValues content = new ContentValues();
		content.put(KEY_NAME, favEntity.getName());
		content.put(KEY_CHANNEL_ID, favEntity.getChannelId());
		myDataBase.insert(Table_Fav, null, content);
	}
	
	public void deleteFav(String name)
	{
//		SQLiteDatabase db =this.getWritableDatabase();
		int i=0;
		myDataBase.delete(Table_Fav, KEY_NAME+" = ? ", new String[] {name});
		i++;
	}
	
	public ArrayList<ProgramEntity> getAllProgramByChannelAndDay(int channelId, String day)
	{
		ArrayList<ProgramEntity> results= new ArrayList<ProgramEntity>();
		String query = " SELECT "+KEY_ID+" , "+KEY_NAME+" , "+KEY_CHANNEL_ID+" , "+ KEY_START+" , "+ KEY_END+" , "+ KEY_DAY+" FROM "+Table_Program+" WHERE "		
						+KEY_CHANNEL_ID+ " = ? AND "
						+KEY_DAY+ " = ?";
		Cursor c = myDataBase.rawQuery(query, new String [] {String.valueOf(channelId), day});		
		if (c!=null) 
		{
			if(!c.moveToFirst())
				return results;			
		}
		do
		{
			ProgramEntity program  = new ProgramEntity();
			program.id=c.getInt(c.getColumnIndex(KEY_ID));
			program.name=c.getString(c.getColumnIndex(KEY_NAME));
			program.channelId=c.getInt(c.getColumnIndex(KEY_CHANNEL_ID));
			program.start=c.getInt(c.getColumnIndex(KEY_START));
			//int hour = tmp/60;
			//int minus = tmp - hour*60;
			//program.start= String.format("%02d", hour)+":"+String.format("%02d", minus);
			program.end=c.getInt(c.getColumnIndex(KEY_END));
			//hour = tmp/60;
			//minus = tmp - hour*60;
			//program.end= String.format("%02d", hour)+":"+String.format("%02d", minus);
			program.day=c.getString(c.getColumnIndex(KEY_DAY));
			results.add(program);
		}
		while(c.moveToNext());
		return results;	
	}
	
	public ArrayList<ProgramEntity> getAllProgramByName(String name)
	{
		ArrayList<ProgramEntity> results= new ArrayList<ProgramEntity>();
		String query = " SELECT "+KEY_ID+" , "+KEY_NAME+" , "+KEY_CHANNEL_ID+" , "+ KEY_START+" , "+ KEY_END+" , "+ KEY_DAY+" FROM "+Table_Program+" WHERE "		
						+KEY_NAME+ " LIKE ?  order by "+ KEY_DAY +" asc";
		Cursor c = myDataBase.rawQuery(query, new String [] {"%"+ name+ "%"});		
		if (c!=null) 
		{
			if(!c.moveToFirst())
				return results;			
		}
		do
		{
			ProgramEntity program  = new ProgramEntity();
			program.id=c.getInt(c.getColumnIndex(KEY_ID));
			program.name=c.getString(c.getColumnIndex(KEY_NAME));
			program.channelId=c.getInt(c.getColumnIndex(KEY_CHANNEL_ID));
			program.start=c.getInt(c.getColumnIndex(KEY_START));
			//int hour = tmp/60;
			//int minus = tmp - hour*60;
			//program.start= String.format("%02d", hour)+":"+String.format("%02d", minus);
			program.end=c.getInt(c.getColumnIndex(KEY_END));
			//hour = tmp/60;
			//minus = tmp - hour*60;
			//program.end= String.format("%02d", hour)+":"+String.format("%02d", minus);
			program.day=c.getString(c.getColumnIndex(KEY_DAY));
			results.add(program);
		}
		while(c.moveToNext());
		return results;	
	}
	
	
	public ArrayList<ProgramEntity> getAllProgram()
	{
		ArrayList<ProgramEntity> results= new ArrayList<ProgramEntity>();
		String query = " SELECT "+KEY_ID+" , "+KEY_NAME+" , "+KEY_CHANNEL_ID+" , "+ KEY_START+" , "+ KEY_END+" , "+ KEY_DAY+" FROM "+Table_Program;	
						
		Cursor c = myDataBase.rawQuery(query, null);		
		if (c!=null) 
		{
			if(!c.moveToFirst())
				return results;			
		}
		do
		{
			ProgramEntity program  = new ProgramEntity();
			program.id=c.getInt(c.getColumnIndex(KEY_ID));
			program.name=c.getString(c.getColumnIndex(KEY_NAME));
			program.channelId=c.getInt(c.getColumnIndex(KEY_CHANNEL_ID));
			program.start=c.getInt(c.getColumnIndex(KEY_START));
			program.end=c.getInt(c.getColumnIndex(KEY_END));
			program.day=c.getString(c.getColumnIndex(KEY_DAY));
			results.add(program);
		}
		while(c.moveToNext());
		return results;	
	}
	
	public void insertRegion(ArrayList<ProgramEntity> programs)
	{
		for (ProgramEntity program : programs) {
			ContentValues content = new ContentValues();
			content.put(KEY_CHANNEL_ID, program.channelId);
			content.put(KEY_START, program.start);
			content.put(KEY_END, program.end);
			content.put(KEY_NAME, program.name);
			content.put(KEY_DAY, program.day);
			myDataBase.insert(Table_Program, null, content);			
		}	
	}
	
	public void deleteOldProgramData(String day)
	{
//		SQLiteDatabase db =this.getWritableDatabase();
		myDataBase.delete(Table_Program, KEY_DAY+" = ? ", new String[] { day});
	}	
	
	private boolean checkDataBase(){

		SQLiteDatabase checkDB = null;

		try{
			String myPath = DB_PATH + DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
			
		}catch(SQLiteException e){

			//database does't exist yet.

		}

		if(checkDB != null)
		{
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}
	private void copyDataBase() throws IOException{

		byte[] buffer = new byte[1024];
		OutputStream myOutput = null;
		int length;
		// Open your local db as the input stream
		InputStream myInput = null;
		try
		{
			myInput =myContext.getAssets().open(DATABASE_NAME);
			// transfer bytes from the inputfile to the
			// outputfile
			myOutput =new FileOutputStream(DB_PATH+ DATABASE_NAME);
			while((length = myInput.read(buffer)) > 0)
			{
				myOutput.write(buffer, 0, length);
			}
			myOutput.close();
			myOutput.flush();
			myInput.close();

		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public void createDataBase() throws IOException{

		boolean dbExist = checkDataBase();
//		dbExist=false;
		if(dbExist){
		}
		else{ 
			this.getReadableDatabase(); 
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database "+e.toString());
			}
		}

	}
	public void openDataBase() throws SQLException{

		//Open the database
		String myPath = DB_PATH + DATABASE_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		//myDataBase.execSQL("DROP TABLE IF EXISTS " + Table_Fav);
		//myDataBase.execSQL(CREATE_TABLE_FAV);	
		ArrayList<ProgramEntity> allPro = getAllProgram();
		int i=0;
		i++;


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
}
