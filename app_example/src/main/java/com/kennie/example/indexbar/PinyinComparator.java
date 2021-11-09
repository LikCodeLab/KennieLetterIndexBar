package com.kennie.example.indexbar;


import com.kennie.library.model.SortModel;

import java.util.Comparator;

/**
 * @项目名 KennieIndexBar
 * @类名称 PinyinComparator
 * @类描述 自定义拼音排序规则比较器
 * @创建人 kennie
 * @修改人
 * @创建时间 2021/10/21 22:49
 */
public class PinyinComparator implements Comparator<SortModel> {
    @Override
    public int compare(SortModel o1, SortModel o2) {
        // A-Z 升序
        return o1.getSortLetter().compareTo(o2.getSortLetter());
    }

}
