package com.example.bookeep.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.bookeep.R;
import com.example.bookeep.adaptor.CalendarAdapter;
import com.example.bookeep.db.DBManger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarDialog extends Dialog implements View.OnClickListener {
    ImageView errorIv;
    GridView gv;
    LinearLayout hsvLayout;
    List<TextView> hsvViewList;
    List<Integer> yearList;
    int selectPos = -1;//正在被点击的年份的位置
    private CalendarAdapter adapter;
    int selectMonth = -1;

    public interface OnRefreshListener{
        public void onRefresh(int selPos,int year,int month);
    }
    OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public CalendarDialog(@NonNull Context context,int selectPos,int selectMonth) {
        super(context);
        this.selectPos = selectPos;
        this.selectMonth = selectMonth;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar);
        gv=findViewById(R.id.dialog_calendar_gv);
        errorIv = findViewById(R.id.dialog_calendar_iv);
        hsvLayout=findViewById(R.id.dialog_calendar_layout);
        errorIv.setOnClickListener(this);
        //向横向的ScrollView当中添加View方法
        addViewLayout();
        initGridView();
        //设置GridView当中每一个item的点击事件
        setGVListener();
    }

    private void setGVListener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.selPos = i;
                adapter.notifyDataSetInvalidated();
                int month = i+1;
                int year = adapter.year;
                //获取被选中的年份和月份
                onRefreshListener.onRefresh(selectPos,year,month);
                cancel();
            }
        });
    }

    private void initGridView() {
        int selYear = yearList.get(selectPos);
        adapter = new CalendarAdapter(getContext(), selYear);
        if (selectMonth == -1) {
            int month = Calendar.getInstance().get(Calendar.MONTH);
            adapter.selPos = month;
        }else {
            adapter.selPos = selectMonth -1;
        }
        gv.setAdapter(adapter);
    }

    private void addViewLayout() {
        hsvViewList = new ArrayList<>();//将添加进入线性布局中的TextView进行统一管理
        yearList = DBManger.getYearListFromAccounttb();//获取数据库存储了多少数据
        //如果数据库当中没有记录，就添加今年的记录
        if(yearList.size()==0){
            int year = Calendar.getInstance().get(Calendar.YEAR);
            yearList.add(year);
        }
        //遍历年份，有几年就向ScrollView当中添加几个View
        for(int i=0;i<yearList.size();i++){
            int year = yearList.get(i);
            View view = getLayoutInflater().inflate(R.layout.item_dialogcal_hsv,null);
            hsvLayout.addView(view);//将View添加到布局中
            TextView hsvTv = view.findViewById(R.id.item_dialogcal_hsv_tv);
            hsvTv.setText(year+"");
            hsvViewList.add(hsvTv);
        }
        if (selectPos == -1) {
            selectPos = hsvViewList.size()-1;//设置当前被选中时最近的年份
        }
        changeTvbg(selectPos);//将最后设置为选中状态
        setHSVClickListener();
    }

    //给横向的ScrollView当中每一个TextView设置点击事件
    private void setHSVClickListener() {
        for (int i = 0; i < hsvViewList.size(); i++) {
            TextView view = hsvViewList.get(i);
            final int pos = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeTvbg(pos);
                    selectPos = pos;
                    //获取被选中的年份，然后下面的GridView显示数据源发生变化
                    int year = yearList.get(selectPos);
                    adapter.setYear(year);
                }
            });
        }
    }

    //传入被选中的位置，改变此位置上的北京和颜色文字
    private void changeTvbg(int selectPos) {
        for (int i = 0; i < hsvViewList.size(); i++) {
            TextView tv = hsvViewList.get(i);
            tv.setBackgroundResource(R.drawable.dialog_btn_bg);
            tv.setTextColor(Color.BLACK);
        }
        TextView selView = hsvViewList.get(selectPos);
        selView.setBackgroundResource(R.drawable.main_record_bg);
        selView.setTextColor(Color.WHITE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_calendar_iv:
                cancel();
                break;
        }
    }

    //设置dialog尺寸和屏幕尺寸一致
    public void setDialogSize(){
        //获取当前窗口对象
        Window window = getWindow();
        //获取窗口对象参数
        WindowManager.LayoutParams wlp = window.getAttributes();
        //获取屏幕宽度
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width=d.getWidth();//对话框窗口为屏幕窗口
        wlp.gravity= Gravity.TOP;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }
}
