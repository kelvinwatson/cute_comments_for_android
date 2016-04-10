package com.watsonlogic.cutecomment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommentDate extends Date {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM");

    public String getCurrentDate(){
        return sdf.format(new Date());
    }
}
