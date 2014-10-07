package com.example.bloghome_shaodf.collect;

import java.util.List;
import com.example.bloghome_shaodf.R;
import com.example.bloghome_shaodf.news.SaveContent;
import com.example.bloghome_shaodf.sql.NewsDataBaseManger;

import android.content.ContentValues;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class MarkAdapter extends BaseAdapter {
	List<SaveContent> list=MarkActivity.list;
	LayoutInflater inflater;
	static boolean flag = false;
	NewsDataBaseManger manger;

	public MarkAdapter(Context context, List<SaveContent> list) {
		inflater = LayoutInflater.from(context);
		manger=new NewsDataBaseManger(context,"newscontents");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.mark_list, null);

		}
		if (position % 2 == 0) {

			convertView.setBackgroundColor(0xffcdddfa);

		} else {

			convertView.setBackgroundColor(0xfff5fffa);
		}

		TextView markTitle = (TextView) convertView
				.findViewById(R.id.marktitle);
		final CheckBox cb = (CheckBox) convertView
				.findViewById(R.id.check_button);

		markTitle.setText(list.get(position).getTitle());
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (cb.isChecked()) {
					list.get(position).setChecked(true);
				} else {
					list.get(position).setChecked(false);
				}
			}
		});
		if (flag) {
			cb.setChecked(true);
			list.get(position).setChecked(true);
		} else {
			cb.setChecked(false);
			list.get(position).setChecked(false);
		}

		return convertView;
	}
}
