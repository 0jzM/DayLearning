package com.example.bookeep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bookeep.adaptor.ChartVPAdapter;
import com.example.bookeep.db.DBManger;
import com.example.bookeep.frag_chart.IncomeFragment;
import com.example.bookeep.frag_chart.OutcomeFragment;
import com.example.bookeep.utils.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthChartActivity extends AppCompatActivity {
    Button inBtn,outBtn;
    TextView dateTv,inTv,outTv;
    ViewPager chartVp;
    private int year;
    private int month;
    int selectPos = -1,selectMonth = -1;
    List<Fragment> chartFragList;
    private IncomeFragment incomeFragment;
    private OutcomeFragment outcomeFragment;
    private ChartVPAdapter chartVPAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_chart);
        initView();
        initTime();
        initStatistics(year,month);
        initFrag();
        setVPSelectListener();
    }

    private void setVPSelectListener() {
        chartVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                setButtonStyle(position);
            }
        });
    }

    private void initFrag() {
        chartFragList = new ArrayList<>();
        //添加Fragment的对象
        incomeFragment = new IncomeFragment();
        outcomeFragment = new OutcomeFragment();
        //添加数据到Fragment当中
        Bundle bundle = new Bundle();
        bundle.putInt("year",year);
        bundle.putInt("month",month);
        incomeFragment.setArguments(bundle);
        outcomeFragment.setArguments(bundle);
        //将Fragment添加到数据源当中
        chartFragList.add(outcomeFragment);
        chartFragList.add(incomeFragment);
        //使用适配器
        chartVPAdapter = new ChartVPAdapter(getSupportFragmentManager(), chartFragList);
        chartVp.setAdapter(chartVPAdapter);
        //将Fragment加载到Activity当中
    }

    /**统计某年某月收支情况数据*/
    private void initStatistics(int year, int month) {
        float inSumMoney = DBManger.getSumMoneyOneMonth(year,month,1);//一月总收入
        float outSumMoney = DBManger.getSumMoneyOneMonth(year,month,0);//一月总支出
        int inCount = DBManger.getCountOneMonth(year,month,1);//收入总记录数
        int outCount = DBManger.getCountOneMonth(year,month,0);//支出总记录数
        dateTv.setText(year+"年"+month+"月账单");
        inTv.setText("共"+inCount+"笔收入，￥"+inSumMoney);
        outTv.setText("共"+outCount+"笔支出，￥"+outSumMoney);
    }

    /**初始化时间*/
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
    }

    //初始化控件
    private void initView() {
        inBtn = findViewById(R.id.chart_btn_in);
        outBtn = findViewById(R.id.chart_btn_out);
        dateTv = findViewById(R.id.chart_tv_date);
        inTv = findViewById(R.id.chart_tv_in);
        outTv = findViewById(R.id.chart_tv_out);
        chartVp = findViewById(R.id.chart_vp);
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.chart_iv_back:
                finish();
                break;
            case R.id.chart_iv_rili:
                showCalendarDialog();
                break;
            case R.id.chart_btn_in:
                setButtonStyle(1);
                chartVp.setCurrentItem(1);
                break;
            case R.id.chart_btn_out:
                setButtonStyle(0);
                chartVp.setCurrentItem(0);
                break;
        }
    }

    //显示账单详情中的日历对话框
    private void showCalendarDialog() {
        CalendarDialog dialog = new CalendarDialog(this,selectPos,selectMonth);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
            @Override
            public void onRefresh(int selPos, int year, int month) {
                MonthChartActivity.this.selectPos = selPos;
                MonthChartActivity.this.selectMonth = month;
                initStatistics(year,month);
                incomeFragment.setDate(year,month);
                outcomeFragment.setDate(year, month);
            }
        });
    }

    //设置按钮样式的改变
    private void setButtonStyle(int kind){
        if(kind == 0){
            outBtn.setBackgroundResource(R.drawable.main_record_bg);
            outBtn.setTextColor(Color.WHITE);
            inBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            inBtn.setTextColor(Color.BLACK);
        }else if(kind == 1){
            inBtn.setBackgroundResource(R.drawable.main_record_bg);
            inBtn.setTextColor(Color.WHITE);
            outBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            outBtn.setTextColor(Color.BLACK);
        }
    }
}
