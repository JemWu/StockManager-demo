package com.app.jem.stockmanager.kinds_detail.brand_detail.goods_detail.goods_item_detail;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.jem.stockmanager.R;
import com.app.jem.stockmanager.data.Db_Data;
import com.app.jem.stockmanager.domain.goods_item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jem on 2017/1/19.
 */

public class goods_detail extends Activity implements View.OnClickListener {
    private TextView brand, model, price, numbers;
    private TextView price_tag;
    private String item_brand, table_name;
    private int item_number, position;
    private String add_text;
    private boolean flag;
    private ImageButton num_AddButton, num_DelButton, changePrice;
    private SQLiteDatabase db3;
    private List<goods_item> goods_list_data = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_item_detail);
        item_number = getIntent().getIntExtra("detail_number", 0);
        position = getIntent().getIntExtra("detail_item", 0);
        item_brand = getIntent().getStringExtra("detail_brand");
        table_name = getIntent().getStringExtra("table_name");
        initView();
        initData();

    }



    private void initData() {
        db3 = new Db_Data(goods_detail.this, table_name, null, 2).getReadableDatabase();
        Cursor curcor = db3.query(table_name, null, null, null, null, null, null);
        if (curcor != null && curcor.getCount() > 0) {
            while (curcor.moveToNext()) {
                goods_item detail = new goods_item();
                detail.setModel(curcor.getString(1));
                detail.setPrice(curcor.getInt(2));
                detail.setNumber(curcor.getInt(3));
                goods_list_data.add(detail);

            }
            curcor.close();
        }
        item_number = goods_list_data.get(position).getNumber();

        db3.close();

        brand.setText(item_brand);
        model.setText(goods_list_data.get(position).getModel());
        price.setText(String.valueOf(goods_list_data.get(position).getPrice()));
        numbers.setText(String.valueOf(goods_list_data.get(position).getNumber()));

    }


    private void initView() {
        price_tag = (TextView) findViewById(R.id.textView);
        brand = (TextView) findViewById(R.id.goods_brand_name);
        model = (TextView) findViewById(R.id.goods_model_name);
        price = (TextView) findViewById(R.id.price);
        numbers = (TextView) findViewById(R.id.goodsNumber);
        num_AddButton = (ImageButton) findViewById(R.id.add);
        num_DelButton = (ImageButton) findViewById(R.id.delete);
        changePrice = (ImageButton) findViewById(R.id.changePrice);
        changePrice.setOnClickListener(this);
        num_DelButton.setOnClickListener(this);
        num_AddButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changePrice:
                final EditText edit = new EditText(goods_detail.this);
                new AlertDialog.Builder(goods_detail.this)
                        .setTitle("请输入价格")
                        .setView(edit)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                add_text = edit.getText().toString();

                                if (add_text != null) {
                                    addDetailToDb();
                                    price.setText(add_text);
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();


                break;
            case R.id.add:
                db3 = new Db_Data(goods_detail.this, table_name, null, 2).getWritableDatabase();
                goods_list_data.clear();
                Cursor curcor_add = db3.query(table_name, null, null, null, null, null, null);

                if (curcor_add != null && curcor_add.getCount() > 0) {
                    while (curcor_add.moveToNext()) {
                        goods_item detail = new goods_item();
                        detail.setModel(curcor_add.getString(1));
                        detail.setPrice(curcor_add.getInt(2));
                        detail.setNumber(curcor_add.getInt(3));
                        goods_list_data.add(detail);
                    }
                    curcor_add.close();
                }
                ContentValues values_add = new ContentValues();
                Integer new_add_number = goods_list_data.get(position).getNumber() + 1;
                values_add.put("NUMBER", new_add_number);
                db3.update(table_name, values_add,  "MODEL=?", new String[]{goods_list_data.get(position).getModel()});
                values_add.clear();
                db3.close();
                numbers.setText(String.valueOf(new_add_number));
                break;
            case R.id.delete:
                db3 = new Db_Data(goods_detail.this, table_name, null, 2).getWritableDatabase();
                goods_list_data.clear();
                Cursor curcor_del = db3.query(table_name, null, null, null, null, null, null);
                if (curcor_del != null && curcor_del.getCount() > 0) {
                    while (curcor_del.moveToNext()) {
                        goods_item detail = new goods_item();
                        detail.setModel(curcor_del.getString(1));
                        detail.setPrice(curcor_del.getInt(2));
                        detail.setNumber(curcor_del.getInt(3));
                        goods_list_data.add(detail);
                    }
                    curcor_del.close();
                }
                ContentValues values_del = new ContentValues();
                Integer new_del_number = null;
                if (goods_list_data.get(position).getNumber() != 0) {
                    new_del_number = goods_list_data.get(position).getNumber() - 1;
                } else {
                    new_del_number = 0;
                }
                values_del.put("NUMBER", new_del_number);
                db3.update(table_name, values_del, "MODEL=?", new String[]{goods_list_data.get(position).getModel()});
                values_del.clear();
                db3.close();
                numbers.setText(String.valueOf(new_del_number));

                break;

        }
    }

    public void change_name(View view) {
        if (flag == false) {
            price_tag.setText("进货价：");
            flag = true;
        } else {
            price_tag.setText("232db");
            flag = false;
        }

    }


    private void addDetailToDb() {
        db3 = new Db_Data(goods_detail.this, table_name, null, 2).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PRICE", add_text);
        db3.update(table_name, values,  "MODEL=?", new String[]{goods_list_data.get(position).getModel()});
        values.clear();
        db3.close();
    }
}