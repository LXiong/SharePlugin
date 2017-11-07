package com.tangzi.share.activity;

import com.tangzi.base.activity.BaseActivity;
import com.tangzi.share.R;

/**
 * Created by liubin on 2017/11/7.
 */

public class MyExpendActivity extends BaseActivity {
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_my_expend;
    }

    @Override
    protected void initView() {
        setTitleText(getString(R.string.my_expend));
    }

    @Override
    protected void initData() {

    }
}
