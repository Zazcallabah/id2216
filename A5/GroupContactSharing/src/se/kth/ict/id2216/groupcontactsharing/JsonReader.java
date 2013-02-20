package se.kth.ict.id2216.groupcontactsharing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import com.google.gson.Gson;

public class JsonReader {
	private InputStream _in;
	private static Gson _gson = new Gson();
	public JsonReader( InputStream in )
	{
		_in=in;
	}

	public String getStringData()
	{
		StringBuilder data = new StringBuilder() ;
		try {
			InputStreamReader isr = new InputStreamReader ( _in ,"UTF-8") ;
			BufferedReader buffreader = new BufferedReader ( isr ) ;

			String readString = buffreader.readLine ( ) ;
			while ( readString != null ) {
				data.append(readString);
				data.append('\n');
				readString = buffreader.readLine ( ) ;
			}

			isr.close ( ) ;

		} catch ( IOException ioe ) {
			ioe.printStackTrace ( ) ;
		}
		return data.toString();

	}
	public Object get( Type type )
	{
		return _gson.fromJson(getStringData(), type );	
	}
	public<T> T get( Class<T> type )
	{
		return _gson.fromJson(getStringData(), type );

	}

	public void close()
	{
		try {
			_in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
