package rssreadtest.com.rssreadtest.Module;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class adapter2 extends BaseAdapter {
	private List<View> list;
	public adapter2(List<View> list){
		this.list = list;
	}
	
	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return list.get(position);
	}
	
}
