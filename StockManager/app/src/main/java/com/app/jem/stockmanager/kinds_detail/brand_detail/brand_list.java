package com.app.jem.stockmanager.kinds_detail.brand_detail;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.jem.stockmanager.R;
import com.app.jem.stockmanager.data.Db_Data;
import com.app.jem.stockmanager.kinds_detail.brand_detail.goods_detail.goods_list;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jem on 2017/1/19.
 */

public class brand_list extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener {
    private ListView brandlist;
    private SQLiteDatabase db2, db3;
    private String item;
    private String add_text;
    private ImageButton brandAddButton;
    private mAdapter myAdapter;
    private List<String> brand_list_data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brand_list);
        item = getIntent().getStringExtra("kinds");
        initData();
        initView();
    }


    private void initData() {
        db2 = new Db_Data(brand_list.this, item, "品牌", 1).getReadableDatabase();
        Cursor curcor = db2.query(item, null, null, null, null, null, null);
        if (curcor != null && curcor.getCount() > 0) {
            while (curcor.moveToNext()) {
                brand_list_data.add(curcor.getString(1));
            }
        }
        db2.close();
    }


    private void initView() {
        brandlist = (ListView) findViewById(R.id.brand_list);
        brandAddButton = (ImageButton) findViewById(R.id.brand_addButton);
        myAdapter = new mAdapter();
        brandlist.setAdapter(myAdapter);
        brandlist.setOnItemClickListener(this);
        brandAddButton.setOnClickListener(this);
        brandlist.setOnItemLongClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent goods_intent1 = new Intent(brand_list.this, goods_list.class);
        String item_2 = item + "_" + brand_list_data.get(position);
        String item_1 = brand_list_data.get(position);
        goods_intent1.putExtra("brands", item_1);
        goods_intent1.putExtra("table_name", item_2);
        startActivity(goods_intent1);

        db2.close();
    }


    @Override
    public void onClick(View v) {
        final EditText edit = new EditText(brand_list.this);
        new AlertDialog.Builder(brand_list.this)
                .setTitle("请输入商品品牌")
                .setView(edit)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        add_text = edit.getText().toString();
                        if (!add_text.isEmpty()) {
                            if (brand_list_data.contains(add_text)) {
                                new AlertDialog.Builder(brand_list.this)
                                        .setTitle("提示")
                                        .setMessage("该品牌已存在")
                                        .setPositiveButton("确定", null)
                                        .show();

                            } else {
                                brand_list_data.add(add_text);
                                myAdapter.notifyDataSetChanged();
                                addBrandToDb();
                            }
                        }else{
                            new AlertDialog.Builder(brand_list.this)
                                    .setTitle("提示")
                                    .setMessage("输入不能为空")
                                    .setPositiveButton("确定", null)
                                    .show();
                        }

                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }


    private void addBrandToDb() {
        /**
         * 添加到二表
         */
        db2 = new Db_Data(brand_list.this, item, "品牌", 1).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("品牌", add_text);
        db2.insert(item, null, values);
        values.clear();
        db2.close();


        /**
         * 创建三表
         */
        db3 = new Db_Data(brand_list.this, item + "_" + add_text, null, 2).getWritableDatabase();
        db3.close();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        new AlertDialog.Builder(brand_list.this)
                .setTitle("提示")
                .setView(new TextView(brand_list.this))
                .setMessage("是否删除该品牌")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String delete_name = brand_list_data.get(position);
                        //删除d3表
                        brand_list.this.deleteDatabase(item + "_" + delete_name);

                        //删除d2表item
                        db2 = new Db_Data(brand_list.this, item, "品牌", 1).getWritableDatabase();
                        db2.delete(item, "品牌=?", new String[]{delete_name});
                        db2.close();
                        brand_list_data.remove(position);
                        myAdapter.notifyDataSetChanged();

                    }
                })
                .setNegativeButton("取消", null)
                .show();

        return true;
    }


    class mAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return brand_list_data.size();
        }

        @Override
        public Object getItem(int position) {
            return brand_list_data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            brandViewHolder holder = null;
            if (convertView == null) {
                holder = new brandViewHolder();
                convertView = View.inflate(brand_list.this, R.layout.item, null);
                holder.brandName = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);
            } else {
                holder = (brandViewHolder) convertView.getTag();
            }
            holder.brandName.setText(brand_list_data.get(position));
            return convertView;
        }
    }

    static class brandViewHolder {
        public TextView brandName;
    }
}