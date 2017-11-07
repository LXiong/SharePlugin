package com.tangzi.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tangzi.base.R;
import com.tangzi.base.activity.BaseActivity;
import com.tangzi.base.utils.NetUtils;

/**
 * Created by liubin on 2017/10/12.
 */

public class DisconnectFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.disconnect_layout, null);
        view.setOnClickListener(new DisconnectListener());
        return view;
    }

    private class DisconnectListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            NetUtils.openSetting(getActivity());
        }
    }

}
