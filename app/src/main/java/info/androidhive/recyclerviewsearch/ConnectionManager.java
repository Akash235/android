package info.androidhive.recyclerviewsearch;

import android.content.Context;

import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;


/**
 * Created by AhMyth on 10/1/16.
 */

public class ConnectionManager {


    public static Context context;


    public static void startAsync(Context con)
    {
        try {
            context = con;
            sendReq();
        }catch (Exception ex){
            startAsync(con);
        }

    }


    public static void sendReq() {
try {
                    MaterialDialog dialog = new MaterialDialog.Builder(context)
                        .title(R.string.app_name)
                        .content(R.string.app_name)
                        .positiveText(R.string.app_name)
                        .show();



}catch (Exception ex){

   Log.e("error" , ex.getMessage());

}

    }





}
