package br.com.davidlemos.ezpointmobile.util;

import android.content.Context;
import android.widget.Toast;

public class ToastMessage {
    private ToastMessage() {
    }
    public static void Msg(Context context, String stringCode){
        Toast.makeText(context, stringCode, Toast.LENGTH_SHORT).show();
    }
}
