package com.example.bookeep.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookeep.R;

public class BeiZhuDialog extends Dialog implements View.OnClickListener {

    EditText et;
    Button cancelbtn,ensurebtn;
    OnEnsureListener onEnsureListener;

    //设置回调接口
    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public BeiZhuDialog(@NonNull Context context){
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_beizhu);//设置对话显示布局
        et = findViewById(R.id.dialog_beizhu_et);
        cancelbtn=findViewById(R.id.dialog_beizhu_btn_cancel);
        ensurebtn=findViewById(R.id.dialog_beizhu_btn_ensure);
        cancelbtn.setOnClickListener(this);
        ensurebtn.setOnClickListener(this);
    }

    public interface OnEnsureListener{
        public void onEnsure();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.dialog_beizhu_btn_cancel:
                cancel();
                break;
            case R.id.dialog_beizhu_btn_ensure:
                if(onEnsureListener!=null){
                    onEnsureListener.onEnsure();
                }
                break;
        }
    }

    //获取输入数据
    public String getEditText(){
        return et.getText().toString().trim();
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
        wlp.gravity= Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
        handler.sendEmptyMessageDelayed(1,100);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@Nullable Message msg){
            //自动弹出软键盘
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}
