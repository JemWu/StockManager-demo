package com.app.jem.stockmanager.kinds_detail.brand_detail.goods_detail;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.app.jem.stockmanager.R;
import com.app.jem.stockmanager.data.Db_Data;
import com.app.jem.stockmanager.domain.goods_item;
import com.app.jem.stockmanager.kinds_detail.brand_detail.goods_detail.goods_item_detail.goods_detail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jem on 2017/1/19.
 */

public class goods_list extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener {
    private ListView goodslist;
    private String item, table_name;
    private String add_text;
    private mAdapter myAdapter;
    private ImageButton goodsAddButton;
    private SQLiteDatabase db3;
    private List<goods_item> goods_list_data = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_list);
        item = getIntent().getStringExtra("brands");
        table_name = getIntent().getStringExtra("table_name");
        initData();
        initView();
    }

    private void initData() {
        db3 = new Db_Data(goods_list.this, table_name, null, 2).getReadableDatabase();
        Cursor curcor = db3.query(table_name, null, null, null, null, null, null);
        if (curcor != null && curcor.getCount() > 0) {
            while (curcor.moveToNext()) {
                goods_item detail = new goods_item();
                detail.setModel(curcor.getString(1));
                detail.setPrice(curcor.getInt(2));
                detail.setNumber(curcor.getInt(3));
                goods_list_data.add(detail);
            }
        }
        db3.close();

    }

    private void initView() {
        goodslist = (ListView) findViewById(R.id.goods_list);
        goodsAddButton = (ImageButton) findViewById(R.id.goods_addButton);
        myAdapter = new mAdapter();
        goodslist.setAdapter(myAdapter);
        goodslist.setOnItemClickListener(this);
        goodslist.setOnItemLongClickListener(this);
        goodsAddButton.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent detailIntent = new Intent(goods_list.this, goods_detail.class);
        String click_item_brand = item;
        detailIntent.putExtra("table_number",goods_list_data.get(position).getNumber() );
        detailIntent.putExtra("table_name", table_name);
        detailIntent.putExtra("detail_item", position);
        detailIntent.putExtra("detail_brand", click_item_brand);
        startActivity(detailIntent);

        db3.close();
    }

    @Override
    public void onClick(View v) {
        final EditText edit = new EditText(goods_list.this);
        new AlertDialog.Builder(goods_list.this)
                .setTitle("请输入商品型号")
                .setView(edit)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        add_text = edit.getText().toString();
                        goods_item new_item = new goods_item();

                        if (!add_text.isEmpty()) {
                            new_item.setModel(add_text);

                            if (goods_list_data.contains(add_text)) {
                                new AlertDialog.Builder(goods_list.this)
                                        .setTitle("提示")
                                        .setMessage("该型号已存在")
                                        .setPositiveButton("确定", null)
                                        .show();

                            } else {
                                goods_list_data.add(new_item);
                                myAdapter.notifyDataSetChanged();
                                addGoodsToDb();
                            }
                        }else{
                            new AlertDialog.Builder(goods_list.this)
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

    private void addGoodsToDb() {
        /**
         * 添加到三表
         */
        db3 = new Db_Data(goods_list.this, table_name, null, 2).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MODEL", add_text);
        db3.insert(table_name, null, values);
        values.clear();
        db3.close();


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        new AlertDialog.Builder(goods_list.this)
                .setTitle("提示")
                .setView(new TextView(goods_list.this))
                .setMessage("是否删除该型号")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db3 = new Db_Data(goods_list.this, table_name, null, 2).getWritableDatabase();
                        db3.delete(table_name, "MODEL=?", new String[]{goods_list_data.get(position).getModel()});
                        db3.close();
                        goods_list_data.remove(position);
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
            return goods_list_data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
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
                convertView = View.inflate(goods_list.this, R.layout.goods_item, null);
                holder.goodsName = (TextView) convertView.findViewById(R.id.name);
                holder.numText = (TextView) convertView.findViewById(R.id.goods_number);
                convertView.setTag(holder);
            } else {
                holder = (brandViewHolder) convertView.getTag();
            }
            holder.goodsName.setText(goods_list_data.get(position).getModel());
            if (!(String.valueOf(goods_list_data.get(position).getNumber()).equals(null))) {
                holder.numText.setText(String.valueOf(goods_list_data.get(position).getNumber()));
            } else {
                holder.numText.setText("0");
            }
            return convertView;
        }
    }



    static class brandViewHolder {
        public TextView goodsName, numText;
    }
}