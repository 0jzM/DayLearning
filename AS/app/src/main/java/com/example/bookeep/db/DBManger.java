package com.example.bookeep.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.bookeep.utils.FloatUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/*
* 负责管理数据库,对表中的数据进行操作
* */
public class DBManger {
    private static SQLiteDatabase db;
    //初始化数据库对象
    public static void initDB(Context context){
        DBOpenHelper helper = new DBOpenHelper(context);//帮助类对象
        db = helper.getWritableDatabase();//得到数据库对象
    }

    /*
    * 读取数据库当中的数据，写入内存集合
    * kind 表示收入或支出
    * */
    public static List<TypeBean> getTypeList(int kind){
        List<TypeBean> list = new ArrayList<>();
        //读取typetb表中的数据
        String sql = "select * from typetb where kind = "+kind;
        Cursor cursor = db.rawQuery(sql,null);
        //循环读取图标内容，存储到对象
        while(cursor.moveToNext()){
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
            int simageId = cursor.getInt(cursor.getColumnIndex("simageId"));
            int kind1 = cursor.getInt(cursor.getColumnIndex("kind"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            TypeBean typeBean = new TypeBean(id,typename,imageId,simageId,kind);
            list.add(typeBean);
        }


        return list;
    }

    /*
    * 向记账表插入一条元素
    * */
    public static void insertItemToAccounttb(AccountBean bean){
        ContentValues values = new ContentValues();
        values.put("typename",bean.getTypename());
        values.put("simageId",bean.getSimageId());
        values.put("beizhu",bean.getBeizhu());
        values.put("money",bean.getMoney());
        values.put("time",bean.getTime());
        values.put("year",bean.getYear());
        values.put("month",bean.getMonth());
        values.put("day",bean.getDay());
        values.put("kind",bean.getKind());
        db.insert("accounttb",null,values);
//        Log.i("animee", "insertItemToAccounttb: ok!!!!!");
    }

    /*
    * 获取记账表中某一天所有支出或收入情况
    * */
    public static List<AccountBean> getAccountOneDayFromAccounttb(int year,int month,int day){
        List<AccountBean> list = new ArrayList<>();
        String sql="select * from accounttb where year =? and month =? and day =? order by id desc";
        Cursor cursor = db.rawQuery(sql,new String[]{year+"",month+"",day+""});
        //遍历符合要求的每一行数据
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename=cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu=cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int simageId=cursor.getInt(cursor.getColumnIndex("simageId"));
            int kind=cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            AccountBean accountBean = new AccountBean(id,typename,simageId,beizhu,money,time,year,month,day,kind);
            list.add(accountBean);
        }
        return list;
    }

    /*
     * 获取记账表中某一月所有支出或收入情况
     * */
    public static List<AccountBean> getAccountOneMonthFromAccounttb(int year,int month){
        List<AccountBean> list = new ArrayList<>();
        String sql="select * from accounttb where year =? and month =? order by id desc";
        Cursor cursor = db.rawQuery(sql,new String[]{year+"",month+""});
        //遍历符合要求的每一行数据
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename=cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu=cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int simageId=cursor.getInt(cursor.getColumnIndex("simageId"));
            int kind=cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id,typename,simageId,beizhu,money,time,year,month,day,kind);
            list.add(accountBean);
        }
        return list;
    }

    /*
    * 获取某一天的支出或收入情况，kind 支出==0 收入==1
    * */
    public static float getSumMoneyOneDay(int year,int month,int day,int kind){
        float total = 0.0f;
        String sql="select sum(money) from accounttb where year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql,new String[]{year+"",month+"",day+"",kind+""});
        //遍历
        if(cursor.moveToFirst()){
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }

    /*
     * 获取某一月的支出或收入情况，kind 支出==0 收入==1
     * */
    public static float getSumMoneyOneMonth(int year,int month,int kind){
        float total = 0.0f;
        String sql="select sum(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql,new String[]{year+"",month+"",kind+""});
        //遍历
        if(cursor.moveToFirst()){
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }

    /**获取一个月收入和支出记录总条数 支出0 收入1*/
    public static int getCountOneMonth(int year,int month,int kind){
        int total = 0;
        String sql = "select count(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql,new String[]{year+"",month+"",kind+""});
        //遍历
        if(cursor.moveToFirst()){
            int count = cursor.getInt(cursor.getColumnIndex("count(money)"));
            total = count;
        }
        return total;
    }

    /*
     * 获取某一年的支出或收入情况，kind 支出==0 收入==1
     * */
    public static float getSumMoneyOneYear(int year,int kind){
        float total = 0.0f;
        String sql="select sum(money) from accounttb where year=? and kind=?";
        Cursor cursor = db.rawQuery(sql,new String[]{year+"",kind+""});
        //遍历
        if(cursor.moveToFirst()){
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }

    //根据传入的id，删除数据库中accounttb表中的数据
    public static int deleteItemFromAccounttbById(int id){
        int i = db.delete("accounttb","id=?",new String[]{id+""});
        return i;
    }

    //根据备注搜索记录
    public static List<AccountBean> getAccountListByRemark(String beizhu){
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where beizhu like '%"+beizhu+"%'";
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String bz = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int simageId = cursor.getInt(cursor.getColumnIndex("simageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id,typename,simageId,bz,money,time,year,month,day,kind);
            list.add(accountBean);
        }
        return list;
    }

    /*
    * 查询记账表中有几个年份信息
    * */
    public static List<Integer> getYearListFromAccounttb(){
        List<Integer> list = new ArrayList<>();
        String sql = "select distinct(year) from accounttb order by year asc";
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            list.add(year);
        }
        return list;
    }

    /**
     * 删除accounttb表中的所有数据
     */
    public static void deleteAllAccount(){
        String sql = "delete from accounttb";
        db.execSQL(sql);
    }

    /*
    查询指定年份和月份的收入或支出每一种类型的总钱数
    **/
    public static List<ChartItemBean> getChartListFromAccounttb(int year,int month,int kind){
        List<ChartItemBean> list = new ArrayList<>();
        float sumMoneyOneMonth = getSumMoneyOneMonth(year, month, kind);//求出支出或收入总钱数
        String sql = "select typename,simageId,sum(money)as total from accounttb where year=? and month=? and kind=?" +
                "group by typename order by total desc";
        Cursor cursor = db.rawQuery(sql,new String[]{year+"",month+"",kind+""});
        while(cursor.moveToNext()){
            int simageId = cursor.getInt(cursor.getColumnIndex("simageId"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            float total = cursor.getFloat(cursor.getColumnIndex("total"));
            //计算所占百分比 total/sumMonth
            float ratio = FloatUtils.div(total,sumMoneyOneMonth);
            ChartItemBean bean = new ChartItemBean(simageId,typename,ratio,total);
            list.add(bean);
        }
        return list;
    }

    /**
     * 获取这个月当中某一天收入支出最大金额，金额是多少
     * */
    public static float getMaxMoneyOneDayInMonth(int year,int month,int kind){
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=? group by day order by sum(money) desc";
        Cursor cursor = db.rawQuery(sql,new String[]{year+"",month+"",kind+""});
        if(cursor.moveToFirst()){
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            return money;
        }
        return 0;
    }
    //根据指定月份每一日收入或支出的总钱数集合
    public static List<BarChartItemBean> getSumMoneyOneDayInMonth(int year,int month,int kind){
        String sql = "select day,sum(money) from accounttb where year=? and month=? and kind=? group by day";
        Cursor cursor = db.rawQuery(sql,new String[]{year+"",month+"",kind+""});
        List<BarChartItemBean> list = new ArrayList<>();
        while(cursor.moveToNext()){
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            float smoney = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            BarChartItemBean itemBean = new BarChartItemBean(year,month,day,smoney);
            list.add(itemBean);
        }
        return list;
    }
}
