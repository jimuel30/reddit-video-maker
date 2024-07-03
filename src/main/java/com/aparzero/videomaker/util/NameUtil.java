package com.aparzero.videomaker.util;

import com.aparzero.videomaker.constant.DateConstant;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NameUtil {
    public static String generateUniqueName(){
        SimpleDateFormat formatter = new SimpleDateFormat(DateConstant.DATE_FORMAT);
        long timestamp = System.currentTimeMillis();
        return formatter.format(new Date(timestamp));
    }

    public static String extractTitle(final String redditUrl){
        final int lastIndex = redditUrl.lastIndexOf("/");
        int startIndex = 0;
        char[] chars = redditUrl.toCharArray();

        for (int i = lastIndex-1; i > 25; i--) {
            if(chars[i] == '/'){
                startIndex = i;
                break;
            }
        }
        return redditUrl.substring(startIndex+1,lastIndex);
    }
}
