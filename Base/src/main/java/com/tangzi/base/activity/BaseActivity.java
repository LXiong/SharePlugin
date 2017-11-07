package com.tangzi.base.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tangzi.base.R;
import com.tangzi.base.fragment.DisconnectFragment;
import com.tangzi.base.receiver.NetworkReceiver;
import com.tangzi.base.service.KeepLiveService;
import com.tangzi.base.utils.KeepLiveManager;
import com.tangzi.base.utils.LogUtils;
import com.tangzi.base.utils.NetUtils;
import com.tangzi.base.utils.BaseStringUtils;
import com.tangzi.base.utils.TActivityManager;

/**
 * Created by liubin on 2017/10/10.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public static final String BUNDLE_EXTRA = "bundleExtra";
    private static final int TABS_SIZE = 5;
    protected Activity instance;
    protected LinearLayout leftTitleLayout = null;
    protected LinearLayout rightTitleLayout = null;
    protected LinearLayout titleLayout = null;
    protected TextView titleTextView = null;
    protected TextView leftTextView = null;
    protected TextView rightTextView = null;
    protected ImageView leftImageView = null;
    protected ImageView rightImageView = null;
    protected FrameLayout rootContentLayout = null;
    protected RelativeLayout rootTitleLayout = null;
    protected LinearLayout rootBottomLayout = null;
    protected TabItem[] tabs;
    protected RelativeLayout[] tabLayouts = new RelativeLayout[TABS_SIZE];
    protected ImageView[] tabImageViews = new ImageView[TABS_SIZE];
    protected TextView[] tabTextViews = new TextView[TABS_SIZE];
    private NetworkReceiver receiver;
    private View realContentView;
    private View disconnectView;
    private Fragment disconnectFragment;
    private int tabPos = 0;
    private boolean isConnected = false;
    protected Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtils.v("BaseActivity onCreate");
        instance = this;
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        TActivityManager.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        bundle = getIntent().getBundleExtra(BUNDLE_EXTRA);
        tabs = getTabs();
        initBase(getContentLayoutId());
        initView();
        initData();
        registerNetworkReceiver();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        TActivityManager.getInstance().finishActivity(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        leftTitleLayout.performClick();
    }

    private void initBase(int layoutResID) {
        setContentView(R.layout.base_layout);
        rootContentLayout = (FrameLayout) findViewById(R.id.root_content_layout);
        rootTitleLayout = (RelativeLayout) findViewById(R.id.root_title_layout);
        rootBottomLayout = (LinearLayout) findViewById(R.id.root_bottom_layout);
        titleLayout = (LinearLayout) findViewById(R.id.title_layout);
        leftTitleLayout = (LinearLayout) findViewById(R.id.left_title_layout);
        rightTitleLayout = (LinearLayout) findViewById(R.id.right_title_layout);
        titleTextView = (TextView) findViewById(R.id.title_txt);
        leftImageView = (ImageView) findViewById(R.id.left_title_image);
        leftTextView = (TextView) findViewById(R.id.left_title_txt);
        rightImageView = (ImageView) findViewById(R.id.right_title_image);
        rightTextView = (TextView) findViewById(R.id.right_title_txt);
        //默认显示标题栏
        initTitles(null, getResources().getString(R.string.app_name), null, R.drawable.back, 0);
        if (tabs == null) {
            rootBottomLayout.setVisibility(View.GONE);
            realContentView = LayoutInflater.from(this).inflate(layoutResID, null);
            disconnectView = LayoutInflater.from(this).inflate(R.layout.disconnect_layout, null);
            rootContentLayout.addView(disconnectView);
            rootContentLayout.addView(realContentView);
            disconnectView.setOnClickListener(new DisconnectListener());
        }
    }

    /**
     * 布局id
     * 需要子类重写
     *
     * @return
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 左侧点击响应事件
     */
    protected void doLeftClick() {
        instance.finish();
    }

    /**
     * 右侧点击响应事件
     */
    protected void doRightClick() {

    }

    protected TabItem[] getTabs() {
        return null;
    }

    /**
     * @param leftTxt    标题栏左侧文本
     * @param titleTxt   标题栏中心文本
     * @param rightTxt   标题栏右侧文本
     * @param leftImage  标题栏左侧图标
     * @param rightImage 标题栏右侧图标
     */
    protected void initTitles(String leftTxt, String titleTxt, String rightTxt, int leftImage, int rightImage) {
        rootTitleLayout.setVisibility(View.VISIBLE);
        setViewVisible(leftTextView, BaseStringUtils.isNotBlank(leftTxt));
        leftTextView.setText(leftTxt);
        setViewVisible(titleTextView, BaseStringUtils.isNotBlank(titleTxt));
        titleTextView.setText(titleTxt);
        setViewVisible(rightTextView, BaseStringUtils.isNotBlank(rightTxt));
        rightTextView.setText(rightTxt);
        setImageSource(leftImageView, leftImage);
        setImageSource(rightImageView, rightImage);
        if (BaseStringUtils.isNotBlank(leftTxt) || leftImage > 0) {
            leftTitleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doLeftClick();
                }
            });
        }
        if (BaseStringUtils.isNotBlank(rightTxt) || rightImage > 0) {
            rightTitleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doRightClick();
                }
            });
        }
    }

    private void selectedTabs() {
        for (int i = 0; i < TABS_SIZE; i++) {
            if (tabLayouts[i] == null) {
                tabLayouts[i] = (RelativeLayout) rootBottomLayout.findViewById(getResources().getIdentifier("base_title_tab" + i, "id", getPackageName()));
                tabLayouts[i].setOnClickListener(new TabClickListener(i));
            }
            if (tabImageViews[i] == null) {
                tabImageViews[i] = (ImageView) tabLayouts[i].findViewById(R.id.tabImg);
            }
            if (tabTextViews[i] == null) {
                tabTextViews[i] = (TextView) tabLayouts[i].findViewById(R.id.tabText);
            }
            if (i > tabs.length - 1) {
                tabLayouts[i].setVisibility(View.GONE);
            } else {
                tabTextViews[i].setText(tabs[i].tabName);
                if (tabPos == i) {
                    tabImageViews[i].setImageResource(tabs[i].selectedImageRes);
                    tabTextViews[i].setTextColor(tabs[i].selectedTextColor);
                } else {
                    tabImageViews[i].setImageResource(tabs[i].normalImageRes);
                    tabTextViews[i].setTextColor(tabs[i].normalTextColor);
                }
            }
        }
        setSelectedFragment();
    }

    private void setSelectedFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        for (int i = 0; i < tabs.length; i++) {
            if (tabs[i].fragment != null) {
                if (i != tabPos || !isConnected) {
                    if (tabs[i].fragment.isAdded() && tabs[i].fragment.isVisible()) {
                        fragmentTransaction.hide(tabs[i].fragment);
                    }
                }
            }
        }
        Fragment fragment;
        if (disconnectFragment == null) {
            disconnectFragment = new DisconnectFragment();
        }
        if (isConnected) {
            if (isConnected && disconnectFragment.isAdded() && disconnectFragment.isVisible()) {
                fragmentTransaction.hide(disconnectFragment);
            }
            fragment = tabs[tabPos].fragment;
        } else {
            fragment = disconnectFragment;
        }
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.root_content_layout, fragment);
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        }
        if (!fragment.isVisible()) {
            fragmentTransaction.show(fragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }


    private void registerNetworkReceiver() {
        if (receiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            receiver = new NetworkReceiver(new BaseCallBack());
            registerReceiver(receiver, filter);
        }

    }


    private void setImageSource(ImageView imageView, int resId) {
        if (resId > 0) {
            imageView.setImageResource(resId);
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    private void setViewVisible(View view, boolean visiable) {
        if (view != null) {
            view.setVisibility(visiable ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void showContentLayout(boolean isVisable) {
        if (tabs == null) {
            realContentView.setVisibility(isVisable ? View.VISIBLE : View.GONE);
            disconnectView.setVisibility(isVisable ? View.GONE : View.VISIBLE);
        } else {
            selectedTabs();
        }
    }

    private class BaseCallBack implements NetUtils.CallBack {
        @Override
        public void connectNetwork() {
            isConnected = true;
            showContentLayout(true);
        }

        @Override
        public void connectWifi() {
        }

        @Override
        public void disConnectNetwork() {
            isConnected = false;
            showContentLayout(false);
        }
    }

    private class DisconnectListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            NetUtils.openSetting(instance);
        }
    }

    public class TabItem {
        public Fragment fragment;
        public int normalImageRes;
        public int selectedImageRes;
        public int normalTextColor;
        public int selectedTextColor;
        public String tabName;
    }

    private class TabClickListener implements View.OnClickListener {
        private int pos;

        public TabClickListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View view) {
            tabPos = pos;
            selectedTabs();
        }
    }

    public static void start(Context mContext, Bundle bundle, Class cls) {
        Intent intent = new Intent(mContext, cls);
        if (bundle != null) {
            intent.putExtra(BUNDLE_EXTRA, bundle);
        }
        if (mContext instanceof Activity) {
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mContext.startActivity(intent);
    }
}
