package com.example.bookeep.frag_record;


import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookeep.R;
import com.example.bookeep.db.AccountBean;

import com.example.bookeep.db.TypeBean;
import com.example.bookeep.utils.BeiZhuDialog;
import com.example.bookeep.utils.KeyBoardUtils;
import com.example.bookeep.utils.SelectTimeDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 记录页面支出模块,BaseRecordFragment
 */
public abstract class OutcomeFragment extends Fragment implements View.OnClickListener {

    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView typeIv;
    TextView typeTv,beizhuTv,timeTv;
    GridView typeGv;
    List<TypeBean> typelist;
    AccountBean accountBean;//将需要插入的数据保存为对象
    TypeBaseAdapter typeBaseAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public OutcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBean = new AccountBean();//创建对象
        accountBean.setTypename("其他");
        accountBean.setSimageId(R.mipmap.ic_qita_fs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);
        setInitTime();
        //给GridView填充数据
        loadDataToGV();
        setGVListener();//设置GridView各项的点击事件
        return view;
    }

    //获取当前时间
    private void setInitTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = sdf.format(date);
        timeTv.setText(time);
        accountBean.setTime(time);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);
    }

    //设置GridView各项的点击事件方法
    private void setGVListener() {
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                typeBaseAdapter.selectPos = i;
                typeBaseAdapter.notifyDataSetChanged();//提示绘制发生变化
                TypeBean typeBean = typelist.get(i);
                String typename = typeBean.getTypename();
                typeTv.setText(typename);
                accountBean.setTypename(typename);
                int simageId = typeBean.getSimageId();
                typeIv.setImageResource(simageId);
                accountBean.setSimageId(simageId);
            }
        });
    }

    public void loadDataToGV() {
        typelist = new ArrayList<>();
        typeBaseAdapter = new TypeBaseAdapter(getContext(), typelist);
        typeGv.setAdapter(typeBaseAdapter);
//        //获取数据库中的数据
//        List<TypeBean> outlist = DBManger.getTypeList(0);
//        typelist.addAll(outlist);
//        typeBaseAdapter.notifyDataSetChanged();
    }

    public void initView(View view){
        keyboardView=view.findViewById(R.id.frag_record_keyboard);
        moneyEt=view.findViewById(R.id.frag_record_et_money);
        typeIv=view.findViewById(R.id.frag_record_iv);
        typeGv=view.findViewById(R.id.frag_record_gv);
        typeTv=view.findViewById(R.id.frag_record_tv_type);
        beizhuTv=view.findViewById(R.id.frag_record_tv_beizhu);
        timeTv=view.findViewById(R.id.frag_record_tv_time);
        beizhuTv.setOnClickListener(this);
        timeTv.setOnClickListener(this);
        //自定义软键盘显示出来
        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView,moneyEt);
        boardUtils.showKey();
        //设置接口，监听确定按钮被点击
        boardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {

                    //获取输入钱数
                String moneyStr = moneyEt.getText().toString();
                if(TextUtils.isEmpty(moneyStr)||moneyStr.equals("0")){
                    getActivity().finish();
                    return;
                }
                float money = Float.parseFloat(moneyStr);
                accountBean.setMoney(money);
//                 获取记录信息，保存在数据库
                    saveAccountToDB();
//                 返回上一级页面
                getActivity().finish();
            }
        });
    }

    public abstract void saveAccountToDB() ;

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.frag_record_tv_time:
                showTimeDialog();
                break;
            case R.id.frag_record_tv_beizhu:
                showBZDialog();
                break;
        }
    }

    //弹出显示时间的对话框
    private void showTimeDialog() {
        SelectTimeDialog dialog = new SelectTimeDialog(getContext());
        dialog.show();
        //设定确定按钮被点击了
        dialog.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                timeTv.setText(time);
                accountBean.setTime(time);
                accountBean.setYear(year);
                accountBean.setMonth(month);
                accountBean.setDay(day);
            }
        });
    }

    //弹出备注对话框
    public void showBZDialog() {
        final BeiZhuDialog dialog = new BeiZhuDialog(getContext());
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BeiZhuDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String msg = dialog.getEditText();
                if(!TextUtils.isEmpty(msg)){
                    beizhuTv.setText(msg);
                    accountBean.setBeizhu(msg);
                }
                dialog.cancel();
            }
        });
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
