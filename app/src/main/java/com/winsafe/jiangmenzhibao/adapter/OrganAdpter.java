package com.winsafe.jiangmenzhibao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.winsafe.jiangmenzhibao.R;
import com.winsafe.jiangmenzhibao.entity.OrganDataBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shijie.yang on 2017/5/21.
 */

public class OrganAdpter extends BaseAdapter {
    private Context context;
    private List<OrganDataBean> list;
    private LayoutInflater inflater = null;

    public OrganAdpter(Context context, List<OrganDataBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrganDataBean bean=list.get(position);

        ViewHolder holder=null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_out_item, null);
            holder=new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(bean.getOrganName());
        holder.tvOther.setVisibility(View.GONE);
//        holder.tvOther.setText(bean.getOrganID());

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvOther)
        TextView tvOther;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
