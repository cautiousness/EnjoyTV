package com.fuj.enjoytv.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 公共数据适配器
 * @author dell
 *
 * @param <T>
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
	private int layoutId;
	protected Context context;
	protected List<T> dataLists;
	protected LayoutInflater layoutInflater;

	public CommonAdapter(Context context, List<T> datas, int layoutId) {
		this.context = context;
		this.dataLists = datas;
		this.layoutId = layoutId;
		layoutInflater = LayoutInflater.from(context);
	}
	
	public void setDataList(List<T> dataLists) {
		this.dataLists = dataLists;
	}

	@Override
	public int getCount() {
		return dataLists.size();
	}

	@Override
	public T getItem(int position) {
		return dataLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommonViewHolder commonViewHolder = CommonViewHolder.get(context, convertView, parent,
		        layoutId, position);
		bindViewToViewHolder(commonViewHolder, getItem(position));
		return commonViewHolder.getConvertView();
	}
	
	public void updateListView(List<T> list){
		dataLists = list;
		notifyDataSetChanged();
	}

	public abstract void bindViewToViewHolder(CommonViewHolder commonViewHolder, T object);
}
