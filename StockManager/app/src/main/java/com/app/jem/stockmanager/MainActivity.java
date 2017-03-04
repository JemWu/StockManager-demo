package com.app.jem.stockmanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.app.jem.stockmanager.kinds_detail.BigFragment;
import com.app.jem.stockmanager.kinds_detail.SmallFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private ViewPager pager;
    private RadioGroup rg;

    private List<Fragment> fragmentLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // 隐藏actionbar
        getSupportActionBar().hide();
        addFragment();
        initView();

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                RadioButton button = (RadioButton) rg.getChildAt(position);
                button.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        rg.setOnCheckedChangeListener(this);

    }

    private void addFragment() {

        if (fragmentLists.isEmpty()) {
            fragmentLists.add(new BigFragment());
            fragmentLists.add(new SmallFragment());
        }
    }

    private void initView() {
        pager = (ViewPager) findViewById(R.id.main_pager);
        rg = (RadioGroup) findViewById(R.id.rg);
        pager.setAdapter(new mAdapter(getSupportFragmentManager()));

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.one:
                pager.setCurrentItem(0);
                break;
            case R.id.two:
                pager.setCurrentItem(1);
                break;
        }
    }


    class mAdapter extends FragmentPagerAdapter {


        public mAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentLists.get(position);
        }

        @Override
        public int getCount() {
            return fragmentLists.size();
        }
    }
}
