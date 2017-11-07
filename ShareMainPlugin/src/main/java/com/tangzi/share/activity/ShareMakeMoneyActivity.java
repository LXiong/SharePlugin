package com.tangzi.share.activity;

import com.tangzi.base.activity.BaseActivity;
import com.tangzi.share.R;

/**
 * Created by liubin on 2017/11/7.
 */

public class ShareMakeMoneyActivity extends BaseActivity {
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_share_make_money;
    }

    @Override
    protected void initView() {
        setTitleText(getString(R.string.share_make_money));
        setRightText(getString(R.string.income_detail));
    }

    @Override
    protected void doRightClick() {
        super.doRightClick();
    }

    @Override
    protected void initData() {

    }
}
