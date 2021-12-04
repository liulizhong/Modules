package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//张三 ，2019-06，2019-08
public class HBaseScanUtil {

    public static List<String[]> getStartStopRow(String phone, String start, String stop) throws ParseException {

        //创建集合，存放多个月份的StartStopRow
        ArrayList<String[]> startStopRow = new ArrayList<String[]>();

        //创建时间格式化对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        //获取分区（健）数
        int regions = Integer.parseInt(PropertiesUtil.getProperty("hbase.regions"));

        //获取日历类对象
        Calendar startPoint = Calendar.getInstance();
        Calendar stopPoint = Calendar.getInstance();

        //给日历类对象赋值
        startPoint.setTime(sdf.parse(start));
        stopPoint.setTime(sdf.parse(stop));

        while (startPoint.getTimeInMillis() <= stopPoint.getTimeInMillis()) {

            String startFormat = sdf.format(startPoint.getTime());

            //获取当月的分区号
            String splitNum = HBaseDAO.getSplitNum(phone, startFormat, regions);

            //拼接分区号、手机号以及时间
            String[] rows = new String[2];

            rows[0] = splitNum + "_" + phone + "_" + startFormat;
            rows[1] = splitNum + "_" + phone + "_" + startFormat + "|";

            //将当月的startstopRow添加至集合
            startStopRow.add(rows);

            //日期自增（一月）
            startPoint.add(Calendar.MONTH, 1);
        }
        return startStopRow;
    }
}
