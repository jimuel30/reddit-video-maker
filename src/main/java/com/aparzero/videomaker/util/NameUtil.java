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
}
