package app.masterUNG.Learning_AndroidMenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class HTML5activity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.html5);
	}

	public void backHome(View v) {
		finish();
	}
	
}