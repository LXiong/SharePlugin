package com.tangzi.base.bean;

/**
 * Created by liubin on 2017/11/7.
 */

public class HttpParams {
    public static final int GET = 0;
    public static final int POST = GET + 1;
    /**
     * post 或者 get
     */
    protected int requestType = GET;
    /**
     * 同步 或者 异步
     */
    protected boolean sync = true;
    /**
     * 请求地址
     */
    protected String url = null;

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
