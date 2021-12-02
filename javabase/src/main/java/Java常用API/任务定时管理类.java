package Java常用API;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/*
    设置Java定时任务，每天的凌晨一点 01:00 执行一次，用Calendar类
 */
public class 任务定时管理类 {
    public static void main(String[] args) {
        TimerManager();
    }
    public static void TimerManager() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 1);   // 第一次执行在 凌晨1 点，然后按照下边参数指定时间间隔重复执行
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();         //第一次执行定时任务的时间
        // 如果第一次执行定时任务的时间 小于当前的时间
        // 此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
        if (date.before(new Date())) {
            date = addDay(date, 1);
        }
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("每次执行的代码！！！");
            }
        };
        // 安排指定的任务在指定的时间开始进行重复的固定延迟执行。
        timer.schedule(timerTask, date, 24 * 60 * 60 * 1000);
    }

    // 增加或减少天数工具方法
    public static Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }
}