package com.kennie.example.indexbar.ui;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kennie.example.indexbar.PinyinComparator;
import com.kennie.example.indexbar.R;
import com.kennie.example.indexbar.adapter.SortAdapter;
import com.kennie.example.indexbar.adapter.TitleItemDecoration;
import com.kennie.example.indexbar.entity.ContactModel;
import com.kennie.example.indexbar.utils.PinyinUtils;
import com.kennie.library.indexbar.LetterIndexBarView;
import com.kennie.library.indexbar.OnLetterChangeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private LetterIndexBarView mLetterIndexBar;

    private RecyclerView mRV;

    private PinyinComparator mComparator;
    private List<ContactModel> mDateList;
    private LinearLayoutManager manager;
    private SortAdapter mAdapter;
    private TitleItemDecoration mDecoration;


    private String[] data = {"裘豆豆", "B李莎", "jb", "Jobs", "动力火车", "伍佰", "#蔡依林", "$797835344$",
            "Jack", "9527", "戚薇", "齐期浩二", "齐天大圣", "品冠", "吴克群", "贲素琴", "缪丝", "成龙",
            "王力宏", "汪峰", "王菲", "那英", "张伟", "~夏先生", "阿aaa",
            "阿李珊", "陈奕迅", "周杰伦", "曾一鸣", "哈林", "高进", "高雷", "阮金天", "龚琳娜",
            "苏醒", "陈奕迅", "周杰伦", "张学友", "哈林", "李德华", "高雷", "白亮", "龚琳娜",};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        mLetterIndexBar = (LetterIndexBarView) findViewById(R.id.side_view);
        mRV = (RecyclerView) findViewById(R.id.rv);
        mComparator = new PinyinComparator();
        mLetterIndexBar.setOnLetterChangeListener(new OnLetterChangeListener() {
            @Override
            public void onLetterChanged(int position, String letter) {
                //该字母首次出现的位置
                int pos = mAdapter.getPositionForSection(letter.charAt(0));
                if (pos != -1) {
                    manager.scrollToPositionWithOffset(pos, 0);
                }
            }

            @Override
            public void onLetterClosed(int position, String letter) {

            }
        });
        mDateList = mockData(data);
        // 根据a-z进行排序源数据
        Collections.sort(mDateList, mComparator);
        //RecyclerView设置manager
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRV.setLayoutManager(manager);
        mAdapter = new SortAdapter(this, mDateList);
        mRV.setAdapter(mAdapter);
        mDecoration = new TitleItemDecoration(this, mDateList);
        //如果add两个，那么按照先后顺序，依次渲染。
        mRV.addItemDecoration(mDecoration);
        mRV.addItemDecoration(new DividerItemDecoration(TestActivity.this, DividerItemDecoration.VERTICAL));
    }

    private List<ContactModel> mockData(String[] date) {
        List<ContactModel> mSortList = new ArrayList<>();
        for (String str : date
        ) {
            ContactModel contactModel = new ContactModel();
            contactModel.setName(str);
            //汉字转换成拼音
            String pinyin = PinyinUtils.getPingYin(str);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                contactModel.setSortLetter(sortString.toUpperCase());
            } else {
                contactModel.setSortLetter("#");
            }
            mSortList.add(contactModel);
        }
        return mSortList;
    }


    /**
     * 根据输入框中的值来过滤数据并更新RecyclerView
     *
     * @param filterStr
     */
    private void mockData(String filterStr) {
        List<ContactModel> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mockData(data);
        } else {
            filterDateList.clear();
            for (ContactModel sortModel : mDateList) {
                String name = sortModel.getName();
                if (name.contains(filterStr) ||
                        PinyinUtils.getFirstSpell(name).startsWith(filterStr.toString())
                        //不区分大小写
                        || PinyinUtils.getFirstSpell(name).toLowerCase().startsWith(filterStr.toString())
                        || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr.toString())
                ) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, mComparator);
        mDateList.clear();
        mDateList.addAll(filterDateList);
        //mAdapter.notifyDataSetChanged();
    }
}
