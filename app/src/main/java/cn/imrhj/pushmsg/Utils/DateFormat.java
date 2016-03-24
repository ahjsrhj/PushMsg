package cn.imrhj.pushmsg.Utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by rhj on 16/3/17.
 */
public class DateFormat {
    public static String getDateString(Date date) {
        Calendar old = Calendar.getInstance();
        old.setTimeInMillis(date.getTime());

        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());

        int oldYear = old.get(Calendar.YEAR);
        int nowYear = now.get(Calendar.YEAR);
        if (oldYear == nowYear && now.get(Calendar.DAY_OF_YEAR) == old.get(Calendar.DAY_OF_YEAR)) {
            int hour = old.get(Calendar.HOUR_OF_DAY);
            int min = old.get(Calendar.MINUTE);
            return hour + ":" + (min < 10 ? "0" + min : min);
        } else {
            return oldYear + "年"
                    + old.get(Calendar.MONTH) + "月"
                    + old.get(Calendar.DAY_OF_MONTH) + "日";
        }

    }
}
