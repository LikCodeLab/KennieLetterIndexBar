package com.kennie.example.indexbar.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @项目名 KennieLetterIndexBar
 * @类名称 KeyboardUtils
 * @类描述
 * @创建人 Administrator
 * @修改人
 * @创建时间 2021/12/21 0:26
 */
public class KeyboardUtils {

    /**
     * 隐藏键盘
     *
     * @param activity
     */
    public static void hideKeyboard(final Activity activity) {
        if (activity != null) {
            hideKeyboard(activity.getCurrentFocus());
        }
    }

    public static void hideKeyboard(final View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
