package com.kennie.example.indexbar.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kennie.example.indexbar.R;
import com.kennie.example.indexbar.entity.Contact;

import java.util.List;

/**
 * @项目名 KennieLetterIndexBar
 * @类名称 ContactAdapter
 * @类描述
 * @创建人 Administrator
 * @修改人
 * @创建时间 2021/12/20 21:48
 */
public class ContactAdapter extends BaseQuickAdapter<Contact, BaseViewHolder> {
    public ContactAdapter() {
        super(R.layout.item_contact);
    }

    public ContactAdapter(@Nullable List<Contact> data) {
        super(R.layout.item_contact, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, Contact contact) {
        holder.setText(R.id.tv_contact_name, contact.getName());
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = getData().get(i).getFirstLetter();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }
}
