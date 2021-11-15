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
package com.kennie.library.model;

import androidx.annotation.NonNull;

/**
 * @项目名 KennieIndexBar
 * @类名称 SortModel
 * @类描述 排序基类model
 * @创建人 kennie
 * @修改人
 * @创建时间 2021/10/21 22:49
 */
public class SortModel {

    private long id; // ID

    private String sortLetter; // 排序首字母

    private int type; // 类型 0 字母 1.其他

    private String extra; // 用于拓展的自定义字段

    public SortModel() {

    }

    public SortModel(@NonNull String sortLetter) {
        this.sortLetter = sortLetter;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSortLetter() {
        return sortLetter;
    }

    public void setSortLetter(String sortLetter) {
        this.sortLetter = sortLetter;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
