package com.example.asus.onlinecanteen.utils;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Steven Albert on 3/23/2018.
 */

public class ActivityUtil {

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
