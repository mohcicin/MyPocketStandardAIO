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
import com.dolibarrmaroc.mypocketstandard.utils.UrlImage;

public class MyUpCltAdapter extends BaseAdapter implements Filterable{

	private Context context;
	private List<MyClientAdapter> facts;
	private List<MyClientAdapter> factsfilter;
	private List<MyClientAdapter> facttmp;
	private List<MyClientAdapter> filterlist;
	private LayoutInflater inflater;
	
	private ValueFilter valueFilter;
	
	
	public MyUpCltAdapter(Context ctx,List<MyClientAdapter> fc){
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
	public MyClientAdapter getItem(int arg0) {
		// TODO Auto-generated method stub
		return facts.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return facts.get(arg0).getRefclient();
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {

			holder = new ViewHolder();
			convertView = this.inflater.inflate(R.layout.cltview,
					parent, false);

			holder.id = (TextView) convertView
					.findViewById(R.id.idclt);
			
			holder.name = (TextView) convertView
					.findViewById(R.id.lblListItem);
			holder.tel = (TextView) convertView
					.findViewById(R.id.prix);
			holder.adres = (TextView) convertView.findViewById(R.id.listItemInfo);
			
			holder.logo = (ImageView) convertView.findViewById(R.id.flag);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MyClientAdapter fc = facts.get(position);
		holder.id.setText(fc.getRefclient()+"");
		holder.name.setText(fc.getName());
		holder.tel.setText(fc.getTel());
		holder.adres.setText(fc.getAddresse());
		
		if("".equals(fc.getLogo()) || fc.getLogo() == null){
        	holder.logo.setImageResource(R.drawable.nophoto);
        }else{
        	holder.logo.setImageURI(Uri.parse(UrlImage.pathimg+"/client_img/"+fc.getLogo()));
        }

		return convertView;
	}

	private class ViewHolder {
		TextView id;
		TextView name;
		TextView tel;
		TextView adres;
		ImageView logo;
	}

	public MyUpCltAdapter() {
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

			if (constraint != null && constraint.length() > 0) {
				filterlist = new ArrayList<>();
				for (int i = 0; i < facttmp.size(); i++) {
					if ( (facttmp.get(i).getName() ).toLowerCase().contains(constraint.toString().toLowerCase())) {
						filterlist.add( facttmp.get(i));
					}
				}
				results.count = filterlist.size();
				results.values = filterlist;
			} else {
				results.count = facttmp.size();
				results.values = facttmp;
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			// TODO Auto-generated method stub
			
			facts = new ArrayList<>();
			facts = (List<MyClientAdapter>) results.values;
			notifyDataSetChanged();
			
			//Log.e("LIST",facts.toString());
			filterlist = facts;
		}

	}

	public List<MyClientAdapter> fitredData(){
		return facts;
	}

}
