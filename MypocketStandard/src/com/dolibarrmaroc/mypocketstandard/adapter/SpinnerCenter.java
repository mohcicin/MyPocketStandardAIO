package com.dolibarrmaroc.mypocketstandard.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.dolibarrmaroc.mypocketstandard.R;
import com.dolibarrmaroc.mypocketstandard.models.MyfactureAdapter;

public class SpinnerCenter extends BaseAdapter {

	private Context context;
	private List<String> facts;
	private LayoutInflater inflater;

	public SpinnerCenter(Context ctx,List<String> fc){
		this.context = ctx;
		this.facts = fc;
		this.inflater = LayoutInflater.from(ctx);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return facts.size();
	}

	@Override
	public String getItem(int arg0) {
		// TODO Auto-generated method stub
		return facts.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {

			holder = new ViewHolder();
			convertView = this.inflater.inflate(R.layout.spinnercenter,
					parent, false);

			holder.clt = (TextView) convertView
					.findViewById(R.id.spcenter);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String fc = facts.get(position);
		holder.clt.setText(fc);

		return convertView;
	}

	private class ViewHolder {
		TextView clt;
	}


}