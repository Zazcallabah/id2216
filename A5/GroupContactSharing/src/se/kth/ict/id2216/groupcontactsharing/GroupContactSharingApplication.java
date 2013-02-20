package se.kth.ict.id2216.groupcontactsharing;

import android.app.Application;

public class GroupContactSharingApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
	}

	ContactViewModel _model = null;
	public ContactViewModel getModel()
	{
		if( _model == null )
			_model = new ContactViewModel();
		return _model;
	}
}