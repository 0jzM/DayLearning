package com.example.bookeep.frag_record;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.bookeep.R;
import com.example.bookeep.db.DBManger;
import com.example.bookeep.db.TypeBean;

import java.util.List;


public class IncomeFragment extends OutcomeFragment {
    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        //获取数据库中的数据
        List<TypeBean> inlist = DBManger.getTypeList(1);
        typelist.addAll(inlist);
        typeBaseAdapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.in_qt_fs);
    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(1);
        DBManger.insertItemToAccounttb(accountBean);
    }
}
