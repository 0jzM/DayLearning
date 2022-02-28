package com.example.bookeep.frag_record;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import androidx.fragment.app.Fragment;

import com.example.bookeep.R;
import com.example.bookeep.db.DBManger;
import com.example.bookeep.db.TypeBean;

import java.util.List;


public class OutcomeFragment1 extends OutcomeFragment {


    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        //获取数据库中的数据
        List<TypeBean> outlist = DBManger.getTypeList(0);
        typelist.addAll(outlist);
        typeBaseAdapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.ic_qita_fs);
    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(0);
        DBManger.insertItemToAccounttb(accountBean);
    }
}
