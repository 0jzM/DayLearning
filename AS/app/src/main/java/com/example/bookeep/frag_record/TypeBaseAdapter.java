package com.example.bookeep.frag_record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookeep.R;
import com.example.bookeep.db.TypeBean;

import java.util.List;

public class TypeBaseAdapter extends BaseAdapter {
    Context context;
    List<TypeBean> mDatas;
    int selectPos=0;//选中位置

    public TypeBaseAdapter(Context context, List<TypeBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.item_recordfrag_gv,viewGroup,false);
        //查找布局中的控件
        ImageView iv = view.findViewById(R.id.item_recordfrag_iv);
        TextView tv = view.findViewById(R.id.item_recordfrag_tv);
        //获取指定位置数据源
        TypeBean typeBean = mDatas.get(i);
        tv.setText(typeBean.getTypename());
        //判断当前位置是否为选中位置
        if(selectPos==i){
            iv.setImageResource(typeBean.getSimageId());
        }else {
            iv.setImageResource(typeBean.getImageId());
        }
        return view;
    }
}
