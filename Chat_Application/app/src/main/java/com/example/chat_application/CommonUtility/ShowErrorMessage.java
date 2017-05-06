package com.example.chat_application.CommonUtility;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by BANDINI on 02-05-2017.
 */

public class ShowErrorMessage {

    public static void ShowError(Context context, String strErrorMessage) {
        Toast.makeText(context, strErrorMessage, Toast.LENGTH_LONG).show();
    }
}
