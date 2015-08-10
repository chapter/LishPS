package webservice;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import entity.ProgramEntity;

public class RequestData {
	String SOAP_ACTION;
	final String NAMESPACE = "http://quasar.com.vn/";
	String METHOD_NAME;
	final String URL = "http://ndchuong.name.vn/LichPhatSong.asmx";
	SoapObject client =null;
	SoapObject responseBody = null;                                 
	SoapSerializationEnvelope sse = null;
	AndroidHttpTransport androidHttpTransport;
	public ArrayList<ProgramEntity> getAllProgram(String day, String month, String year, String channel)
	{
		ArrayList<ProgramEntity> entities =new ArrayList<ProgramEntity>();
		SOAP_ACTION = "http://quasar.com.vn/getAllPriogram";
		METHOD_NAME = "getAllPriogram";	
		sse = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
		sse.addMapping(NAMESPACE, "RequestData", this.getClass());
		sse.dotNet = true; 
		androidHttpTransport = new AndroidHttpTransport(URL);
		try 
		{
			client = new SoapObject(NAMESPACE, METHOD_NAME);
			client.addProperty("day", day);
			client.addProperty("month", month);
			client.addProperty("year", year);
			client.addProperty("channel", channel);
			sse.setOutputSoapObject(client);
			sse.bodyOut = client;
			androidHttpTransport.call(SOAP_ACTION, sse);             
			responseBody = (SoapObject) sse.getResponse();
			int count  =responseBody.getPropertyCount();
			for(int i=0;i<count;i++)
			{
				ProgramEntity entity =new ProgramEntity();
				SoapObject storyTemp =(SoapObject)responseBody.getProperty(i);
				String[] tmp = storyTemp.getProperty(0).toString().split(":");
				if(tmp.length>=2)
				{
					int hour = Integer.parseInt(tmp[0]);
					int minute = Integer.parseInt(tmp[1]);
					entity.start = 60*hour+minute;
				}
				tmp = storyTemp.getProperty(1).toString().split(":");
				if(tmp.length>=2)
				{
					int hour = Integer.parseInt(tmp[0]);
					int minute = Integer.parseInt(tmp[1]);
					entity.end = 60*hour+minute;
				}				
				entity.name =storyTemp.getProperty(2).toString();
				entity.day = day+"/"+month+"/"+year;
				entity.channelId =Integer.parseInt(channel);
				entities.add(entity);
			}
			return entities;
		} 
		catch (Exception e) 
		{
			//ProgramEntity error =new ProgramEntity();
			//error.name="Mat ket noi mang";
			//entities.add(error);
			return entities;
		}
	}
	public RequestData() {
		// TODO Auto-generated constructor stub
	}
}
