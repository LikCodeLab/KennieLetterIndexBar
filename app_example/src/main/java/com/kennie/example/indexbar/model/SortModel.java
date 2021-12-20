/*
 * Copyright (c) 2021 Kennie<xdpvxv@163.com>
 *
 * The software is licensed under the Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *     http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v2 for more details.
 */
package com.kennie.example.indexbar.model;


import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.kennie.example.indexbar.utils.PinyinUtils;

/**
 * @项目名 KennieIndexBar
 * @类名称 SortModel
 * @类描述 排序基类model
 * @创建人 kennie
 * @修改人
 * @创建时间 2021/10/21 22:49
 */
public class SortModel implements Parcelable {

    private String content; // 文本内容

    private String pinyin; // 拼音

    private String firstLetter; // 首字母

    private String extra; // 用于拓展的自定义字段

    public SortModel(String content) {
        setContent(content);
    }


    public static final Creator<SortModel> CREATOR = new Creator<SortModel>() {
        @Override
        public SortModel createFromParcel(Parcel in) {
            return new SortModel(in);
        }

        @Override
        public SortModel[] newArray(int size) {
            return new SortModel[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        if (!TextUtils.isEmpty(content)) {
            //汉字转换成拼音
            setPinyin(PinyinUtils.getPingYin(content));
            String letter = getPinyin().substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (letter.matches("[A-Z]")) {
                setFirstLetter(letter);
            } else {
                setFirstLetter("#");
            }
        } else {
            // 内容为空
            setPinyin("######");
            setFirstLetter("#");
        }
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "SortModel{" +
                "pinyin='" + pinyin + '\'' +
                ", firstLetter='" + firstLetter + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.pinyin);
        dest.writeString(this.firstLetter);
        dest.writeString(this.extra);
    }

    public void readFromParcel(Parcel source) {
        this.content = source.readString();
        this.pinyin = source.readString();
        this.firstLetter = source.readString();
        this.extra = source.readString();
    }

    protected SortModel(Parcel in) {
        this.content = in.readString();
        this.pinyin = in.readString();
        this.firstLetter = in.readString();
        this.extra = in.readString();
    }

}
