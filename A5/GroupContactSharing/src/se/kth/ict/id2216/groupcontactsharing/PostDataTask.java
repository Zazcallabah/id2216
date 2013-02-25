package se.kth.ict.id2216.groupcontactsharing;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;

import android.os.*;

public class PostDataTask extends AsyncTask<ContactViewModel, Integer, Boolean> {

	public boolean Done = false;
	public boolean Result = false;
	private HttpResponse send( String label, HttpEntity payload ) throws ClientProtocolException, IOException
	{
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter("http.socket.timeout", 3000);
		httpclient.getParams().setParameter("http.connection.timeout", 3000);
		httpclient.getParams().setParameter("http.connection-manager.timeout", 3000);
		httpclient.getParams().setParameter("http.protocol.head-body-timeout", 3000);
		
		HttpPost httppost = new HttpPost("http://tmp.prefect.se:8888/api/"+label);
		httppost.setEntity(payload );
		return  httpclient.execute(httppost);
	}
	protected Boolean doInBackground(ContactViewModel... models) {
		int count = models.length;
		for (int i = 0; i < count; i++) {


			try {

				HttpEntity he = new StringEntity(ContactDetails.Create(models[i]).Serialize(), "UTF-8");
				// Execute HTTP Post Request
				HttpResponse response =send( models[i].getLabel(), he );
				if( response.getStatusLine().getStatusCode() == 200 )
					return true;
				if(response.getStatusLine().getStatusCode() == 503  )
				{
					// we got 503, retry
					HttpResponse response2 =send( models[i].getLabel(), he );
					if( response2.getStatusLine().getStatusCode() == 200 )
						return true;

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