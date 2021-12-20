package com.kennie.example.indexbar.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kennie.example.indexbar.PinyinComparator;
import com.kennie.example.indexbar.R;
import com.kennie.example.indexbar.adapter.ContactAdapter;
import com.kennie.example.indexbar.adapter.TitleItemDecoration;
import com.kennie.example.indexbar.entity.Contact;
import com.kennie.library.indexbar.LetterIndexBar;

import com.kennie.library.indexbar.OnLetterIndexChangeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestContactActivity extends AppCompatActivity {

    private LetterIndexBar mLetterIndexBar;
    private TextView tv_letter ;

    private RecyclerView mRV;

    private List<Contact> mDateList;
    private LinearLayoutManager mLayoutManager;
    private ContactAdapter mContactAdapter;
    private int mScrollState = -1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        tv_letter = findViewById(R.id.tv_letter);
        initData();
        initView();
        initRV();
        initAdapter();
    }

    private void initData() {
        mDateList = getContactMockData();
    }

    private void initView() {
        mLetterIndexBar = (LetterIndexBar) findViewById(R.id.side_view);
        mLetterIndexBar.setOverlayTextView(tv_letter).setOnLetterChangeListener(new OnLetterIndexChangeListener() {
            /**
             * 手指按下和抬起会回调这里
             *
             * @param touched true|false
             */
            @Override
            public void onTouched(boolean touched) {

            }

            /**
             * 索引字母改变时会回调这里
             *
             * @param letter 选中的索引字符
             * @param index  RecyclerView将要滚动到的位置(-1代表未找到目标位置，则列表不用滚动)
             */
            @Override
            public void onLetterChanged(String letter, int index) {
                //该字母首次出现的位置
                int pos = mContactAdapter.getPositionForSection(letter.charAt(0));
                if (pos != -1) {
                    mLayoutManager.scrollToPositionWithOffset(pos, 0);
                }
            }
        });
        mRV = (RecyclerView) findViewById(R.id.rv);
        mRV.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mScrollState = newState;
                Log.i("TestContactActivity" , "onScrollStateChanged");

            }


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i("TestContactActivity" , "onScrolled");
                if (mScrollState != -1) {
                    //第一个可见的位置
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    //判断是当前layoutManager是否为LinearLayoutManager
                    // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                    int firstItemPosition = 0;
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                        //获取第一个可见view的位置
                        firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    }
                    mLetterIndexBar.onItemScrollUpdateLetter(mDateList.get(firstItemPosition).getFirstLetter());
                    if (mScrollState == RecyclerView.SCROLL_STATE_IDLE) {
                        mScrollState = -1;
                    }
                }
            }
        });
    }

    private void initRV() {
        //RecyclerView设置manager
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRV.setLayoutManager(mLayoutManager);
        TitleItemDecoration mDecoration = new TitleItemDecoration(this, mDateList);
        //如果add两个，那么按照先后顺序，依次渲染。
        mRV.addItemDecoration(mDecoration);
        mRV.addItemDecoration(new DividerItemDecoration(TestContactActivity.this, DividerItemDecoration.VERTICAL));
        mRV.setNestedScrollingEnabled(false);//解决滑动不流畅

    }

    private void initAdapter() {
        mContactAdapter = new ContactAdapter(mDateList);
        mRV.setAdapter(mContactAdapter);
    }

    private final String[] contactMock = {"裘豆豆", "B李莎", "jb", "Jobs", "动力火车", "伍佰", "#蔡依林", "$797835344$",
            "Jack", "9527", "戚薇", "齐期浩二", "齐天大圣", "品冠", "吴克群", "贲素琴", "缪丝", "成龙",
            "王力宏", "汪峰", "王菲", "那英", "张伟", "~夏先生", "阿aaa",
            "阿李珊", "陈奕迅", "周杰伦", "曾一鸣", "哈林", "高进", "高雷", "阮金天", "龚琳娜",
            "苏醒", "陈奕迅", "周杰伦", "张学友", "哈林", "李德华", "高雷", "白亮", "龚琳娜",};


    /**
     * 配置联系人模拟数据
     *
     * @return
     */
    private List<Contact> getContactMockData() {
        List<Contact> list = new ArrayList<>();
        for (String str : contactMock
        ) {
            Contact contact = new Contact(str);
            list.add(contact);
        }
        PinyinComparator mComparator = new PinyinComparator();
        // 根据a-z进行排序源数据
        Collections.sort(list, mComparator);
        return list;
    }
}
