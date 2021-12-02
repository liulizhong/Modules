package Java常用API;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;

/*
    一、Date
        (1) date.getTime();	//获取当前毫秒值
    二、GregorianCalendar
        (1)Calendar c = Calendar.getInstance();	    //建对象
        (2)int year = c.get(Calendar.YEAR);         //取出时间对象Calendar的year，还可MONTH、DAY_OF_MONTH、HOUR_OF_DAY、SECOND等
        (3)c.set(Calendar.YEAR, 2018);              //设置时间对象Calendar的值
    三、java.text.DateFormat
        (1) SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");   //创建实现类对象(指定格式)
        (2) String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date());   //返回目前时间的string
    四、java.time.LocalDate/LocalTime/LocalDateTime   不可改变日期类型
        (1) LocalDate jin = LocalDate.now();     //获取现在时间 2020-03-31
        (2) int year = jin.getYear();DayOfWeek dayOfWeek = jin.getDayOfWeek();      //获取具体值方法
        (3) LocalDate next = jin.plusMonths(3);     //三月后时间(也可jitianhou)
        (4) boolean b = jin.isLeapYear();           //是否是今年最后一天
    五、java.time.Instant/ZoneId/Duration/Period/format.DateTimeFormatter
        (1) Instant t = Instant.now();		//本初字母线时间
            OffsetDateTime atOffset = t.atOffset(ZoneOffset.ofHours(8));    //本初字母线偏移8小时时间
        (2) ZonedDateTime t = ZonedDateTime.now();  //现在的时区时间
            ZonedDateTime t1 = ZonedDateTime.now(ZoneId.of("America/New_York"));    //指定时区的时间
        (3) LocalDateTime t1 = LocalDateTime.now();
            LocalDateTime t2 = LocalDateTime.of(2018, 12, 31, 23, 59, 59, 999);
            Duration between = Duration.between(t1, t2);
            //Period between = Period.between(t1, t2);      //两种方法计算时间差
            between.toDays()；between.toHours()；     //计算时间差
        (4) DateTimeFormatter.ISO_DATE.format(LocalDate.now())          //格式化时间
            DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now()) //格式化时间
            DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(LocalDate.now())     //FULL和SHORT适用于LocalDate和LocalTime
            DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(LocalTime.now())    //FULL和SHORT适用于LocalDate和LocalTime
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(LocalDateTime.now())   //LONG和MEDIUM适用于LocalDateTime
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())      //格式化目前时间
 */
public class 日期类 {
    public static void main(String[] args) throws ParseException {

        //// 【1】、Date类
        Date date = new Date(Long.MAX_VALUE);       // 创建今天的Date对象
        long time = date.getTime();                  // 获取当前毫秒值

        //// 【2】、GregorianCalendar抽象类
        Calendar c = Calendar.getInstance();         // 创建对象从
        int year = c.get(Calendar.YEAR);            // 获取当前时间的年
        int month = c.get(Calendar.MONTH) + 1;        // 月从0开始
        int day = c.get(Calendar.DAY_OF_MONTH);
        int week = c.get(Calendar.DAY_OF_WEEK);
//		int hour = c.get(Calendar.HOUR);            //12小时制
        int hour = c.get(Calendar.HOUR_OF_DAY);   //24小时制
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        int mil = c.get(Calendar.MILLISECOND);
        c.setTime(date);                             // 按照传入date设置Calendar对象
        c.set(Calendar.YEAR, 2018);                 // 设置年
        c.set(Calendar.MONTH, 11 - 1);                // 设置月
        c.set(Calendar.DAY_OF_MONTH, 11);         // 设置日

        //// 【3】、DateFormat 抽象类：日期格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        String formatDate = simpleDateFormat.format(new Date());            // 按照指定格式获取当前时间
        Date parseDate = simpleDateFormat.parse("2020-06-10");    // 按照传入日期解析成指定时间格式的日期格式

        //// 【4】、JDK1.8后：java.time.LocalDate/LocalTime/LocalDateTime
        LocalDate nowLocalDate = LocalDate.now();                                        // 获取当前日期  "yyyy-MM-dd"
        LocalTime nowLocalTime = LocalTime.now();                                        // 获取当前时间  "HH:mm:ss SSS"
        LocalDateTime nowLocalDateTime = LocalDateTime.now();                            // 获取时间日期  "yyyy-MM-dd HH:mm:ss SSS"
        LocalDate ofLocalDate = LocalDate.of(2021, 3, 8);   // 指定日期的LocalDate对象
        Duration dt = Duration.between(LocalDateTime.now(), nowLocalDateTime);           // ★计算两个时间的差。【相差的总 天/小时/分钟/秒 数：dt.toDays()，dt.toHours()，dt.toMinutes()，dt.getSeconds()】
        Period pd = Period.between(nowLocalDate, ofLocalDate);                           // ★计算两个日期的差。【相差的总 年/月/天/总月：pd.pd.getYears()，pd.getMonths()，pd.getDays()，pd.toTotalMonths()】
        String format = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());   // ★格式化时间：2021-03-09T16:01:43.838
        DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(LocalDate.now());    //  ★格式化时间：2021年3月9日 星期二
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());//★格式化时间：2021-03-09 16:04:07
        int years = ofLocalDate.getYear();                                               // 2021
        Month months = ofLocalDate.getMonth();                                           // months.getValue() => 3  ; months.name() => MARCH
        int days = ofLocalDate.getDayOfMonth();                                          // 9
        DayOfWeek dayOfWeek = ofLocalDate.getDayOfWeek();                                // dayOfWeek.getValue() => 1 ; dayOfWeek.name() => MONDAY
        int total = ofLocalDate.getDayOfYear();                                          // 68
        LocalDate next = ofLocalDate.plusMonths(3).plusDays(10);                          // 3个月后，10天后的日期 ：2021-06-18
        LocalDate pre = ofLocalDate.minusMonths(3).minusDays(10);                         // 3个月前，10天前的日期 ：2020-11-28
        boolean b = ofLocalDate.isLeapYear();                                            // 这一天是不是闰年 ： false

        //// 【5】、JDK1.8后：其他不常用时间
        // java.time.Instant(瞬时时间)
        Instant t = Instant.now();                                                         // 本初子母线时间
        OffsetDateTime atOffset = t.atOffset(ZoneOffset.ofHours(8));                     // 本初子母线偏移8小时的时间，即北京时间
        long milli = t.toEpochMilli();                                                  // 获取该时间的毫秒值，同System.currentTimeMillis()
        Instant in2 = Instant.ofEpochSecond(milli / 1000);                                 // 输入时间戳(秒)得到时间
        // java.time.ZoneId/Clock(时区时间)
        ZonedDateTime z = ZonedDateTime.now();                                           // 当前的时区时间"Asia/Shanghai"，可指定获取纽约时间.now(ZoneId.of("America/New_York"))
        Clock c2 = Clock.system(ZoneId.of("America/New_York"));                       // 获取纽约日期时间，c2.getZone()打印时区，c2.instant()打印日期时间

        //// 【6】、日期工具方法求n天前日期
        System.out.println(getLastsDay("2020-06-10", 3));
    }

    /*
      日期工具方法：取n(daysAgo)天前日期:输入格式String(2020-06-10)输出格式String(2020-06-07)
     */
    public static String getLastsDay(String time, int daysAgo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        //                      此处修改为+1则是获取后一天
        calendar.set(Calendar.DATE, day - daysAgo);
        String lastDay = sdf.format(calendar.getTime());
        return lastDay;
    }
}
