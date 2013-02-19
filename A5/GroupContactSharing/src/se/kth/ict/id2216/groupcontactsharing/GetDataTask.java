package se.kth.ict.id2216.groupcontactsharing;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.*;

public class GetDataTask extends AsyncTask<ContactViewModel, Integer, Boolean> {
	
	public GetDataTask( int count )
	{
		_count=count;
	}
	private int _count;
	private static Gson _gson = new Gson();
	public boolean Done = false;
	public boolean Result = false;
	
    protected Boolean doInBackground(ContactViewModel... models) {
        int count = models.length;
        for (int i = 0; i < count; i++) {
        	

            try {

            	HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://tmp.prefect.se:8888/api/"+models[i].getLabel());
            	HttpResponse response = httpclient.execute(httpget);


                if( response.getStatusLine().getStatusCode() == 200 )
                {
                	HttpEntity entity = response.getEntity();
                		InputStream is = entity.getContent();
        	            JsonReader jr = new JsonReader(is);
        	            jr.getStringData();
        	           List<ContactDetails> list = _gson.fromJson(jr.getStringData(),  new TypeToken<List<ContactDetails>>(){}.getType()  );	
        	           models[i].mergeContacts( list );
        	          
        	           if( count > 1 )
        	           {
        	        	 try {
        	               Thread.sleep(1000);
        	           } catch (InterruptedException e) {
        	               // TODO Auto-generated catch block
        	           } 
        	        	 new GetDataTask(count-1).execute(models[i]);
        	           }

                }
                	
                
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
            	android.util.Log.i("ioePostDataTask", "ioe on post contact data", e);

            }
        	
        }
        return false;
    }

    protected void onPostExecute(Boolean result) {
    	Result= result;
    	Done=true;
    }
}