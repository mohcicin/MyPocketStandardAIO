package com.dolibarrmaroc.mypocketstandard.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.dolibarrmaroc.mypocketstandard.R;
import com.dolibarrmaroc.mypocketstandard.models.MyClientAdapter;
import com.dolibarrmaroc.mypocketstandard.models.MyfactureAdapter;
import com.dolibarrmaroc.mypocketstandard.models.Tournee;
import com.dolibarrmaroc.mypocketstandard.utils.UrlImage;

public class TourneeAdapter extends BaseAdapter implements Filterable{

	private Context context;
	private List<Tournee> facts;
	private List<Tournee> facttmp;
	private List<Tournee> filterlist;
	private LayoutInflater inflater;

	private ValueFilter valueFilter;


	public TourneeAdapter(Context ctx,List<Tournee> fc){
		this.context = ctx;
		this.facts = fc;
		this.inflater = LayoutInflater.from(ctx);
		this.facttmp = fc;
		getFilter();
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return facts.size();
	}

	@Override
	public Tournee getItem(int arg0) {
		// TODO Auto-generated method stub
		return facts.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return facts.get(arg0).getRowid();
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {

			holder = new ViewHolder();
			convertView = this.inflater.inflate(R.layout.listeviewtourne,
					parent, false);

			holder.id = (TextView) convertView
					.findViewById(R.id.tskid);

			holder.name = (TextView) convertView
					.findViewById(R.id.tskl);
			holder.tel = (TextView) convertView
					.findViewById(R.id.tskdt);
			holder.adres = (TextView) convertView.findViewById(R.id.tskrec);


			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Tournee fc = facts.get(position);
		holder.id.setText(fc.getRowid()+"");
		holder.name.setText(fc.getLabel());
		holder.tel.setText(fc.getDebut()+ " // "+fc.getFin());
		holder.adres.setText(prepaDays(fc.getRecur()).replace(" ", ","));

		return convertView;
	}

	private class ViewHolder {
		TextView id;
		TextView name;
		TextView tel;
		TextView adres;
	}

	public TourneeAdapter() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Filter getFilter() {
		if (valueFilter == null) {
			valueFilter = new ValueFilter();
		}
		return valueFilter;
	}


	private class ValueFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			
			Log.e("out ",constraint.toString());
			if (constraint != null && constraint.length() > 0) {
				filterlist = new ArrayList<>();
				for (int i = 0; i < facttmp.size(); i++) {
					if (facttmp.get(i).getRecur().contains(Integer.parseInt(constraint.toString()))) {
						filterlist.add( facttmp.get(i));
						Log.e("in  ",constraint.toString());
					}
					
					
				}
				results.count = filterlist.size();
				results.values = filterlist;
			} else {
				results.count = facttmp.size();
				results.values = facttmp;
			}
			Log.e("fil  ",filterlist.toString()+"");
			
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			// TODO Auto-generated method stub

			facts = new ArrayList<>();
			facts = (List<Tournee>) results.values;
			notifyDataSetChanged();

			//Log.e("LIST",facts.toString());
			filterlist = facts;
		}

	}

	private String prepaDays(List<Integer> in){

		String out = "";

		for (int i = 0; i < in.size(); i++) {
			switch (in.get(i)) {
			case 1:
				out += " lun";				
				break;
			case 2:
				out += " mar";
				break;
			case 3:
				out += " mer";
				break;
			case 4:
				out += " jeu";
				break;
			case 5:
				out += " ven";
				break;

			default:
				out += " sam";
				break;
			}
		}

		if(out != ""){
			out = out.substring(1,out.length());
		}
		return out;
	}



}
