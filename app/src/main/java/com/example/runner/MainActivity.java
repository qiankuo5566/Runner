package com.example.runner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.runner.eventmessage.EventMessage;
import com.example.runner.fragment.MineFragment;
import com.example.runner.fragment.MoreFragment;
import com.example.runner.fragment.StartFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private static final String      TAG = "vivi";
    private              FrameLayout mContent;
    private              RadioButton mRbHome;
    private              RadioButton mRbShop;
    private              RadioButton mRbMessage;
    private              RadioButton mRbMine;
    private              RadioGroup  mRgTools;
    private              Fragment[]  mFragments;

    private int mIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContent = (FrameLayout) findViewById(R.id.content);
        mRbMine = (RadioButton) findViewById(R.id.rbMine);
        mRbShop = (RadioButton) findViewById(R.id.rbStart);
        mRbMessage = (RadioButton) findViewById(R.id.rbMore);
        mRgTools = (RadioGroup) findViewById(R.id.rgTools);
        EventBus.getDefault().register(this);
        initFragment();
        //initRadioGroup();

    }

   /* private void initRadioGroup() {

    }*/

    private void initFragment() {
        //首页
        MineFragment mineFragment = new MineFragment();
        //购物车
        StartFragment startFragment = new StartFragment();

        //消息
        MoreFragment moreFragment = new MoreFragment();
        //个人中心

        //添加到数组
        mFragments = new Fragment[]{mineFragment, startFragment, moreFragment};

        //开启事务
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //添加首页
        ft.add(R.id.content, mineFragment).commit();

        //默认设置为第0个
        setIndexSelected(0);

    }


    private void setIndexSelected(int index) {

        if (mIndex == index) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();


        //隐藏
        ft.hide(mFragments[mIndex]);
        //判断是否添加
        if (!mFragments[index].isAdded()) {
            ft.add(R.id.content, mFragments[index]).show(mFragments[index]);
        } else {
            ft.show(mFragments[index]);
        }

        ft.commit();
        //再次赋值
        mIndex = index;

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rbMine:
                setIndexSelected(0);
                break;
            case R.id.rbStart:
                setIndexSelected(1);
                break;
            case R.id.rbMore:
                setIndexSelected(2);
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setGoIndex(EventMessage eventMessage) {
        Log.d(TAG, "setGoIndex: " + eventMessage.getTag());
        if (eventMessage != null) {
            int tag = eventMessage.getTag();


            if (tag == EventMessage.EventMessageAction.TAG_GO_MAIN) {
                mRbHome.performClick();
                setIndexSelected(0);
            } else if (tag == EventMessage.EventMessageAction.TAG_GO_SHOPCART) {
                mRbShop.performClick();

                setIndexSelected(1);
            } else if (tag == EventMessage.EventMessageAction.TAG_GO_MESSAGE) {
                mRbMessage.performClick();
                setIndexSelected(2);
            }


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // mRbHome.performClick();
    }
}
