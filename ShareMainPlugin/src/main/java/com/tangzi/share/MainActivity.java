package com.tangzi.share;

import android.view.View;

import com.tangzi.base.activity.BaseActivity;
import com.tangzi.share.activity.MyExpendActivity;
import com.tangzi.share.activity.RankingListActivity;
import com.tangzi.share.activity.ShareMakeMoneyActivity;

public class MainActivity extends BaseActivity {


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        findViewById(R.id.share_make_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.start(MainActivity.this, null, ShareMakeMoneyActivity.class);
            }
        });
        findViewById(R.id.ranking_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.start(MainActivity.this, null, RankingListActivity.class);
            }
        });
        findViewById(R.id.my_expend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.start(MainActivity.this, null, MyExpendActivity.class);
            }
        });
    }

    @Override
    protected void initData() {

    }
}
