package com.example.bookeep;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bookeep.adaptor.AccountAdapter;
import com.example.bookeep.db.AccountBean;
import com.example.bookeep.db.DBManger;
import com.example.bookeep.utils.BudgetDialog;
import com.example.bookeep.utils.MoreDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ListView todayLv;//展示今天收支情况的ListView
    ImageView searchIv;
    Button editBtn;
    ImageButton moreBtn;
    //声明数据源
    List<AccountBean> mDatas;
    AccountAdapter adapter;
    int year,month,day;
    //头布局相关控件
    View headerView;
    TextView topOutTv,topInTv,topbudgetTv,topConTv;
    ImageView topShowIv;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();
        initView();
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);
        //添加ListView的头布局
        addLVHeaderView();
        mDatas = new ArrayList<>();
        //设置适配器，加载每一行数据到列表中
        adapter=new AccountAdapter(this,mDatas);
        todayLv.setAdapter(adapter);
    }

    //初始化自带方法
    private void initView() {
        todayLv=findViewById(R.id.main_lv);
        editBtn=findViewById(R.id.main_btn_edit);
        moreBtn=findViewById(R.id.main_btn_more);
        searchIv=findViewById(R.id.main_iv_search);
        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        searchIv.setOnClickListener(this);
        setLVLongClickListener();
    }
    //设置ListView的长按事件
    private void setLVLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    return false;
                }
                int pos = i-1;
                AccountBean clickBean = mDatas.get(pos);
                //弹出提示用户删除的对话框
                showDeleteItemDialog(clickBean);
                return false;
            }
        });
    }

    //弹出删除对话框
    private void showDeleteItemDialog(final AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("你确定删除这条记录吗？")
                .setNegativeButton("取消",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int click_id = clickBean.getId();
                        //执行删除的操作
                        DBManger.deleteItemFromAccounttbById(click_id);
                        mDatas.remove(clickBean);//实时刷新，移出集合当中的对象
                        adapter.notifyDataSetChanged();//提示适配器更新数据
                        setTopTvShow();//改变头布局Textview显示的内容
                    }
                });
        builder.create().show();//显示对话框
    }

    //给ListView添加头布局的方法
    private void addLVHeaderView() {
        //将布局转化成View对象inflate;
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        todayLv.addHeaderView(headerView);
        //查找头布局可用控件
        topOutTv=headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv=headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topbudgetTv=headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv=headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topShowIv=headerView.findViewById(R.id.item_mainlv_top_iv_hide);

        topbudgetTv.setOnClickListener(this);
        headerView.setOnClickListener(this);
        topShowIv.setOnClickListener(this);
    }

    //获取今日时间
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH)+1;
        day=calendar.get(Calendar.DAY_OF_MONTH);
    }

    //当activity获取焦点时，会调用的方法
    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
        setTopTvShow();
    }

    //设置头布局文本内容显示
    private void setTopTvShow() {
        //获取今日支出和收入的总金额，显示在view中
        float incomeOneDay = DBManger.getSumMoneyOneDay(year,month,day,1);
        float outcomeOneDay = DBManger.getSumMoneyOneDay(year,month,day,0);
        String infoOneDay = "今日支出 ￥"+outcomeOneDay+",收入 ￥"+incomeOneDay;
        topConTv.setText(infoOneDay);
        //获取本月收入和支出总金额
        float incomeOneMonth = DBManger.getSumMoneyOneMonth(year,month,1);
        float outcomeOneMonth = DBManger.getSumMoneyOneMonth(year,month,0);
        topInTv.setText("￥"+incomeOneMonth);
        topOutTv.setText("￥"+outcomeOneMonth);
        //设置显示预算剩余
        float bmoney = preferences.getFloat("bmoney",0);
        if(bmoney == 0){
            topbudgetTv.setText("￥ 0");
        }else {
            float syMoney = bmoney-outcomeOneMonth;
            topbudgetTv.setText("￥"+syMoney);
        }
    }

    //加载数据库数据
    private void loadDBData() {
        List<AccountBean> list = DBManger.getAccountOneDayFromAccounttb(year,month,day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    //button相关的点击事件
    public void onClick(View view){
        switch (view.getId()){
            case R.id.main_iv_search:
                Intent it = new Intent(this,SearchActivity.class);
                startActivity(it);
                break;
            case R.id.main_btn_edit:
                Intent it1 = new Intent(this,RecordActivity.class);
                startActivity(it1);
                break;
            case R.id.main_btn_more:
                MoreDialog moreDialog = new MoreDialog(this);
                moreDialog.show();
                moreDialog.setDialogSize();
                break;
            case R.id.item_mainlv_top_tv_budget:
                showBudgetDialog();
                break;
            case R.id.item_mainlv_top_iv_hide:
                //切换TextView明文和密文
                toggleShow();
                break;
        }
        if(view == headerView){
            //头布局被点击了
            Intent intent = new Intent();
            intent.setClass(this, MonthChartActivity.class);
            startActivity(intent);
        }
    }

    //显示预算设置
    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                //将预算金额心如共享参数，进行存储
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("bmoney",money);
                editor.commit();
                //计算剩余金额
                float outcomeOneMonth = DBManger.getSumMoneyOneMonth(year,month,0);
                float syMoney = money-outcomeOneMonth;
                topbudgetTv.setText("￥"+syMoney);
            }
        });
    }

    boolean isShow = true;
    //切换明文和密文的方法
    private void toggleShow() {
        if(isShow){//明文->密文
            PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
            topInTv.setTransformationMethod(passwordMethod);
            topOutTv.setTransformationMethod(passwordMethod);
            topbudgetTv.setTransformationMethod(passwordMethod);
            topShowIv.setImageResource(R.mipmap.ih_hide);
            isShow=false;//设置标志位为隐藏状态
        }else {
            HideReturnsTransformationMethod hideMethod = HideReturnsTransformationMethod.getInstance();
            topInTv.setTransformationMethod(hideMethod);
            topOutTv.setTransformationMethod(hideMethod);
            topbudgetTv.setTransformationMethod(hideMethod);
            topShowIv.setImageResource(R.mipmap.ih_show);
            isShow=true;//设置标志位为显示状态
        }
    }

}
