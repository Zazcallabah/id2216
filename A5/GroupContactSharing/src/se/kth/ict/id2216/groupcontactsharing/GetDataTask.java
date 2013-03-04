package se.kth.ict.id2216.groupcontactsharing;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
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
		for (int i = 0; i < models.length; i++) {


			try {

				HttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet("http://130.229.179.246:8880/api/"+models[i].getLabel());//"http://tmp.prefect.se:8888/api/"+models[i].getLabel());
				HttpResponse response = httpclient.execute(httpget);


				if( response.getStatusLine().getStatusCode() == 200 )
				{
					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();
					JsonReader jr = new JsonReader(is);
					String data = jr.getStringData();
					if( data == null || data.length() == 0 )
						return false;
					List<ContactDetails> list = _gson.fromJson(data, new TypeToken<List<ContactDetails>>(){}.getType() );	
					if(!models[i].isActive())
						return false;
					
					models[i].mergeContacts( list );

					if( _count > 1 )
					{
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						} 
						new GetDataTask(_count-1).execute(models[i]);
					}

				}


			} catch (ClientProtocolException e) {
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