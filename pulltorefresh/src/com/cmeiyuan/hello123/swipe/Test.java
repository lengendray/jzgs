package com.cmeiyuan.hello123.swipe;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.R;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2015/2/27.
 */
public class Test {

    public void test(Context context){
        int a = context.getResources().getIdentifier("shape_progress_bg","drawable","com.cmeiyuan.hello123");

        int b = context.getResources().getIdentifier("shape_progress_bg","drawable","com.handmark.pulltorefresh.library");

        String c = a+"\n"+b;


        b = getResourceId("com.handmark.pulltorefresh.library","drawable","shape_progress_bg");

        b = getResourceId("com.cmeiyuan.hello123.R","drawable","shape_progress_bg");

        TextView textView = new TextView(context);
        textView.setBackgroundResource(b);

        Toast.makeText(context, "" + b, Toast.LENGTH_SHORT).show();
    }

    public static int getResourceId(String paramString1, String paramString2, String paramString3)
    {
        if ((paramString1 != null) && (paramString2 != null) && (paramString3 != null))
            try
            {
                Class localClass = Class.forName(paramString1 + "$" + paramString2);
                Field localField = localClass.getField(paramString3);
                Object localObject = localField.get(localClass.newInstance());
                return Integer.parseInt(localObject.toString());
            }
            catch (Exception localException)
            {
                localException.printStackTrace();
            }
        return -1;
    }

}
