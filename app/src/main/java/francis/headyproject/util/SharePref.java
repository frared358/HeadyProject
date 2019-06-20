package francis.headyproject.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePref {

    //Data Share Prefrences
    public SharedPreferences getPrefData(Context context) {
        return context.getSharedPreferences("PREF_DATA", Context.MODE_PRIVATE);
    }

    public boolean getData(Context context,String Key) {
        return getPrefData(context).getBoolean(Key, false);
    }

    public void setData(Context context,String Key, boolean input) {
        SharedPreferences.Editor editor = getPrefData(context).edit();
        editor.putBoolean(Key, input);
        editor.apply();
    }

}
