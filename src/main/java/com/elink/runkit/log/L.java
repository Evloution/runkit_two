package com.elink.runkit.log;

import android.util.Log;

import static android.support.constraint.Constraints.TAG;


/**
 * @Description：
 * @Author： Evloution_
 * @Date： 2019/9/20
 * @Email： 15227318030@163.com
 */
public class L {
    private static boolean debug = true;

    public static void e(String msg) {
        Log.e(TAG, msg);
    }
}
