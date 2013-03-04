package se.kth.ict.id2216.groupcontactsharing;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;

import android.os.*;

public class PostDataTask extends AsyncTask<ContactDetails, Integer, Boolean> {
	private String _label;

	public PostDataTask( String label )
	{
		super();
		_label=label;
	}

	public boolean Done = false;
	public boolean Result = false;
	private HttpResponse send( HttpEntity payload ) throws ClientProtocolException, IOException
	{
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter("http.socket.timeout", 3000);
		httpclient.getParams().setParameter("http.connection.timeout", 3000);
		httpclient.getParams().setParameter("http.connection-manager.timeout", 3000);
		httpclient.getParams().setParameter("http.protocol.head-body-timeout", 3000);

		HttpPost httppost = new HttpPost(StartActivity.ServerUriString+_label);
		android.util.Log.i("PostDataTask", "send " + httppost.getURI());
		httppost.setEntity(payload );
		return  httpclient.execute(httppost);
	}
	protected Boolean doInBackground(ContactDetails... detail) {
		int count = detail.length;
		for (int i = 0; i < count; i++) {


			try {

				HttpEntity he = new StringEntity(detail[i].Serialize(), "UTF-8");
				// Execute HTTP Post Request
				HttpResponse response =send( he );
				if( response.getStatusLine().getStatusCode() == 200 )
					return true;
				if(response.getStatusLine().getStatusCode() == 503  )
				{
					// we got 503, retry
					HttpResponse response2 =send( he );
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