package com.app.jem.stockmanager.kinds_detail;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.app.jem.stockmanager.R;
import com.app.jem.stockmanager.Settings;
import com.app.jem.stockmanager.data.Db_Data;
import com.app.jem.stockmanager.kinds_detail.brand_detail.brand_list;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jem on 2017/1/19.
 */

public class BigFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listView;
    private ImageButton ig, set;
    private SQLiteDatabase db1, db2;
    private boolean flag;
    private String add_text;
    private List<String> itemlistdata = new ArrayList<>();
    private List<String> del_brandList = new ArrayList<>();

    private mAdapter myAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list, null);
        listView = (ListView) view.findViewById(R.id.kinds_list);
        ig = (ImageButton) view.findViewById(R.id.kinds_addButton);
        set = (ImageButton) view.findViewById(R.id.set);


        initData();
        myAdapter = new mAdapter();
        listView.setAdapter(myAdapter);
        ig.setOnClickListener(this);
        set.setOnClickListener(this);


        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        return view;
    }


    private void initData() {
        itemlistdata.clear();
        db1 = new Db_Data(getActivity(), "main_big", "种类", 1).getReadableDatabase();
        Cursor curcor = db1.query("main_big", null, null, null, null, null, null);
        if (curcor != null && curcor.getCount() > 0) {
            while (curcor.moveToNext()) {
                itemlistdata.add(curcor.getString(1));
            }
        }
        db1.close();
    }


    //添加按键
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.kinds_addButton:
                final EditText edit = new EditText(getActivity());
                new AlertDialog.Builder(getActivity())
                        .setTitle("请输入商品种类")
                        .setView(edit)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                add_text = edit.getText().toString();


                                if (!add_text.isEmpty()) {
                                    if (itemlistdata.contains(add_text)) {
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("提示")
                                                .setMessage("该商品类已存在")
                                                .setPositiveButton("确定", null)
                                                .show();

                                    } else {
                                        itemlistdata.add(add_text);
                                        myAdapter.notifyDataSetChanged();
                                        addKindsToDb();
                                    }
                                } else {
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle("提示")
                                            .setMessage("输入不能为空")
                                            .setPositiveButton("确定", null)
                                            .show();
                                }

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.set:
                Intent intent = new Intent(getActivity(), Settings.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoon_in, R.anim.zoon_out);
                break;


        }


    }

    private void addKindsToDb() {

        /**
         * 添加到一表
         */
        db1 = new Db_Data(getActivity(), "main_big", "种类", 1).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("种类", add_text);
        db1.insert("main_big", null, values);


        /**
         * 创建二表
         */
        db2 = new Db_Data(getActivity(), "main_big_" + add_text, "品牌", 1).getWritableDatabase();

        db2.close();

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent kinds_intent1 = new Intent(getActivity(), brand_list.class);
        String item = "main_big_" + itemlistdata.get(position);
        kinds_intent1.putExtra("kinds", item);
        startActivity(kinds_intent1);
        db1.close();

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setView(new TextView(getActivity()))
                .setMessage("是否删除该系类商品")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String delete_name = itemlistdata.get(position);
                        //删除db3
                        db2 = new Db_Data(getActivity(), "main_big_" + delete_name, "品牌", 1)
                                .getReadableDatabase();
                        Cursor curcor = db2.query("main_big_" + delete_name, null, null, null
                                , null, null, null);
                        if (curcor != null && curcor.getCount() > 0) {
                            while (curcor.moveToNext()) {
                                del_brandList.add(curcor.getString(1));
                            }
                        }
                        db2.close();
                        for (int i = 0; i < del_brandList.size(); i++) {
                            getActivity().deleteDatabase("main_big_" + delete_name + "_" +
                                    del_brandList.get(i));
                        }

                        //删除db2表
                        boolean isdelete = getActivity().deleteDatabase("main_big_" + delete_name);
                        if (isdelete) {
                            //删除db1的item
                            db1 = new Db_Data(getActivity(), "main_big", "种类", 1).getWritableDatabase();
                            db1.delete("main_big", "种类=?", new String[]{delete_name});
                            db1.close();
                            itemlistdata.remove(position);
                            myAdapter.notifyDataSetChanged();
                        }


                    }
                })
                .setNegativeButton("取消", null)
                .show();

        return true;
    }

    class mAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return itemlistdata.size();
        }

        @Override
        public Object getItem(int position) {
            return itemlistdata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BigViewHolder holder = null;
            if (convertView == null) {
                holder = new BigViewHolder();
                convertView = View.inflate(getActivity(), R.layout.item, null);
                holder.kindsName = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);
            } else {
                holder = (BigViewHolder) convertView.getTag();
            }
            holder.kindsName.setText(itemlistdata.get(position));
            return convertView;
        }
    }

    static class BigViewHolder {
        public TextView kindsName;
    }
}
