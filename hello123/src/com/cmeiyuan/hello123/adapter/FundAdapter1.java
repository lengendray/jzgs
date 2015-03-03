package com.cmeiyuan.hello123.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cmeiyuan.hello123.R;
import com.cmeiyuan.hello123.bean.v2.Fund;

public class FundAdapter1 extends BaseAdapter {
	private List<Fund> list;

	public List<Fund> getList() {
		return list;
	}

	public void setList(List<Fund> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			arg1 = LayoutInflater.from(arg2.getContext()).inflate(
					R.layout.list_item_fund1, null);
			arg1.setTag(new ViewHolder(arg1));
		}
		ViewHolder viewHolder = (ViewHolder) arg1.getTag();
		viewHolder.fillView(getItem(arg0));
		return arg1;
	}

	public class ViewHolder {

		TextView tv_name;
		TextView tv_code;

		public ViewHolder(View view) {
			tv_name = (TextView) view.findViewById(R.id.tv_fundName);
			tv_code = (TextView) view.findViewById(R.id.tv_fundCode);
		}

		public void fillView(Object item) {
			if (item != null && item instanceof Fund) {

				Fund fund = (Fund) item;
				tv_name.setText(fund.getFundName());
				tv_code.setText(fund.getFundCode());
			}
		}
	}

}
