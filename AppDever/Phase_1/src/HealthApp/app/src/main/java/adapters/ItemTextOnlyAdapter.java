package adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.appdever.healthapp.R;


public class ItemTextOnlyAdapter extends BaseAdapter {

	private Activity activity;
	private static LayoutInflater inflater = null;
    private String[] name;
	//public ImageLoader imageLoader;
	public ItemTextOnlyAdapter(Activity a, String[] name) {
		activity = a;
        this.name = name;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//imageLoader = new ImageLoader(activity.getApplicationContext(),R.drawable.img_profile, 160, 160);
	}

	public int getCount() {
		return name.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		vi = inflater.inflate(R.layout.item_text_only, null);

        TextView txt_category = (TextView) vi.findViewById(R.id.txt_name);
        txt_category.setText(name[position]);
		
		return vi;
	}
}