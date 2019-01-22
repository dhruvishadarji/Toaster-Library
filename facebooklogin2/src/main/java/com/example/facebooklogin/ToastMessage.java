package com.example.facebooklogin;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by dhruvisha on 1/22/2019.
 */

public class ToastMessage {
    public static void s(Context c, String message) {

        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();

    }
}
