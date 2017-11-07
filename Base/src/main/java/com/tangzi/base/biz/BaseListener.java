package com.tangzi.base.biz;

import com.tangzi.base.bean.BaseBean;

/**
 * Created by liubin on 2017/11/7.
 */

public interface BaseListener {
    public void requestDataSuccess(BaseBean baseBean);

    public void requestDataFailure();
}
