package com.example.zooapplication;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class Name: ShareData
 * Description: Store the information that every activity may use.
 *              This is class cannot store object, so if we want to
 *              store information in this class, we must use Gson to
 *              convert to string(or other similar way). When we get
 *              info, it will return a string, so we need to convert
 *              back to Object on your own activity.
 */
public class ShareData {

    public static SharedPreferences getSharePref(Context context){
        return context.getSharedPreferences("X", Context.MODE_PRIVATE);
    }

    /**
     * Decide which activity should be started
     *
     * @param context must use App.getContext();
     * @param key "last activity"
     * @param value set the class's name
     */
    public static void setLastActivity(Context context, String key, String value){
        SharedPreferences.Editor edit = getSharePref(context).edit();
        edit.putString(key, value);
        edit.commit();
    }

    /**
     * contains what user has clicked and add to the plan list
     * this should be set in ExhibitsActivity.class
     * It will be used in DisplayPlanActivity
     * @param context must use App.getContext()
     * @param key "result name"
     * @param value store the result name in here
     */
    public static void setResultName(Context context, String key, String value){
        SharedPreferences.Editor edit = getSharePref(context).edit();
        edit.putString(key, value);
        edit.commit();
    }

    /**
     * contains what user has clicked and add to the plan list
     * this should be set in ExhibitsActivity.class
     * It will be used in DisplayPlanActivity
     * @param context must use App.getContext()
     * @param key "result id"
     * @param value store the result id in here
     */
    public static void setResultId(Context context, String key, String value){
        SharedPreferences.Editor edit = getSharePref(context).edit();
        edit.putString(key, value);
        edit.commit();
    }

    /**
     * set the sorted route in here
     * It need to be used in DirectionActivity
     * @param context must use App.getContext()
     * @param key "names"
     * @param value store the sorted list description in here
     */
    public static void setNames(Context context, String key, String value){
        SharedPreferences.Editor edit = getSharePref(context).edit();
        edit.putString(key, value);
        edit.commit();
    }


    /**
     * get the last activity's name
     * @param context
     * @param key
     * @return
     */
    public static String getLastActivity(Context context, String key){
        return getSharePref(context).getString(key, "");
    }

    /**
     * get the decription which is used in DirectionActivity
     * @param context
     * @param key
     * @return
     */
    public static String getNames(Context context, String key){
        return getSharePref(context).getString(key, "");
    }

    /**
     * get the String that contains all the word that user
     * have added. It need to parse to list in its own activity
     * @param context
     * @param key
     * @return
     */
    public static String getResultName(Context context, String key){
        return getSharePref(context).getString(key, "");
    }

    /**
     * same as getResultName
     * @param context
     * @param key
     * @return
     */
    public static String getResultId(Context context, String key){
        return getSharePref(context).getString(key, "");
    }

    public static void setIds(Context context, String key, String value){
        SharedPreferences.Editor edit = getSharePref(context).edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getIds(Context context, String key){
        return getSharePref(context).getString(key, "");
    }
}
