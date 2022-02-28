package com.example.bookeep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.bookeep.adaptor.RecordPagerAdaptor;
import com.example.bookeep.frag_record.IncomeFragment;
import com.example.bookeep.frag_record.OutcomeFragment;
import com.example.bookeep.frag_record.OutcomeFragment1;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        //查找控件
        tabLayout=findViewById(R.id.record_tabs);
        viewPager=findViewById(R.id.record_vp);
        //设置加载页面
        initPager();
    }

    private void initPager(){
        List<Fragment> fragmentList=new ArrayList<>();
        OutcomeFragment1 outFrag = new OutcomeFragment1();
        IncomeFragment inFrag = new IncomeFragment();
        fragmentList.add(outFrag);
        fragmentList.add(inFrag);
        //创建适配器
        RecordPagerAdaptor pagerAdaptor = new RecordPagerAdaptor(getSupportFragmentManager(),fragmentList);
        //设置是匹配器
        viewPager.setAdapter(pagerAdaptor);
        //进行关联
        tabLayout.setupWithViewPager(viewPager);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.record_iv_back:
                finish();
                break;
        }
    }
}
