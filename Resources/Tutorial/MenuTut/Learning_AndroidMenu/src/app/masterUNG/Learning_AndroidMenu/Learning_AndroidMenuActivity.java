package app.masterUNG.Learning_AndroidMenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Learning_AndroidMenuActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mymain);
	}

	// Intitiating manu xml file
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		MenuInflater myMenuInflater = getMenuInflater();
		myMenuInflater.inflate(R.layout.mymenu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case R.id.Connectivity:

			Intent goConnectivity = new Intent(getApplicationContext(),
					ConnectivityActivity.class);
			startActivity(goConnectivity);

			return true;

		case R.id.DeviceAccess:

			Intent goDeviceAccess = new Intent(getApplicationContext(),
					DeviceAccessActivity.class);
			startActivity(goDeviceAccess);

			return true;

		case R.id.Multimedia:

			Intent goMultimedia = new Intent(getApplicationContext(),
					MultimediaActivity.class);
			startActivity(goMultimedia);

			return true;

		case R.id.OfflineStorage:

			Intent goOffline = new Intent(getApplicationContext(),
					Offline_StorageActivity.class);
			startActivity(goOffline);

			return true;

		case R.id.HTML5:
			
			Intent goHTML5 = new Intent(getApplicationContext(), HTML5activity.class);
			startActivity(goHTML5);
			
			return true;

		case R.id.Performance:
			
			Intent goPerformant = new Intent(getApplicationContext(), PerformentActivity.class);
			startActivity(goPerformant);
			
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}
}