package oracle.druid工具类;

import org.apache.commons.dbutils.handlers.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

// MySQL测试链接类

/**
 * ArrayHandler：把结果集中的第一行数据转成对象数组。
 * ArrayListHandler：把结果集中的每一行数据都转成一个数组，再存放到List中。
 * BeanHandler：将结果集中的第一行数据封装到一个对应的JavaBean实例中。
 * BeanListHandler：将结果集中的每一行数据都封装到一个对应的JavaBean实例中，存放到List里。
 * ColumnListHandler：将结果集中某一列的数据存放到List中。
 * KeyedHandler(name)：将结果集中的每一行数据都封装到一个Map里，再把这些map再存到一个map里，其key为指定的key。
 * MapHandler：将结果集中的第一行数据封装到一个Map里，key是列名，value就是对应的值。
 * MapListHandler：将结果集中的每一行数据都封装到一个Map里，然后再存放到List
 */
public class OracleTest {
    @Test  //1、插入数据：不演示
    public void test()throws Exception {
        String sql = "INSERT INTO TB_EMP_ATTENDTIMERULE VALUES(NULL,?,?)";
        int len = DBUtils.qr.update(sql, "新的",60);
        System.out.println(len>0?"添加成功":"添加失败");
    }

    @Test       //2、查询数据：query->ArrayListHandler(不可取不直观)
    public void test2() throws SQLException{
        String sql = "select * from TB_EMP_ATTENDTIMERULE";
        List<Object[]> query = DBUtils.qr.query(sql, new ArrayListHandler());
        for (Object[] objects : query) {
            for (Object object : objects) {
                System.out.println(object);
            }
        }
    }

    @Test       //3、查询：query -> BeanListHandler(返回bean对象集合)
    public void test3() throws SQLException{
        String sql = "select * from TB_EMP_ATTENDTIMERULE";
        List<TB_EMP_ATTENDTIMERULE> list = DBUtils.qr.query(sql, new BeanListHandler<TB_EMP_ATTENDTIMERULE>(TB_EMP_ATTENDTIMERULE.class));
        for (TB_EMP_ATTENDTIMERULE department : list) {
            System.out.println(department);
        }
    }

    @Test       //4、查询：query -> BeanHandler(返回bean单个对象)
    public void test5() throws SQLException{
        String sql = "select * from TB_EMP_ATTENDTIMERULE where id =?";//建议还是取别名
        //如果明确知道返回的是一个对象，不是一堆对象
        TB_EMP_ATTENDTIMERULE emp = DBUtils.qr.query(sql, new BeanHandler<TB_EMP_ATTENDTIMERULE>(TB_EMP_ATTENDTIMERULE.class), 27);
        System.out.println(emp);
    }

    @Test       //5、查询值返回：query -> ScalarHandler（单个值）
    public void test6()throws SQLException{
        String sql = "select count(*) from TB_EMP_ATTENDTIMERULE";
        BigDecimal count = DBUtils.qr.query(sql, new ScalarHandler<BigDecimal>());
        System.out.println(count);
    }

    @Test           //6、查询多个值返回给数组：query -> ArrayHandler
    public void test7()throws SQLException{
//		查询全公司的最高成绩，最低成绩，平均成绩，总成绩
        String sql = "select max(LINKWORKTIMELONG),min(LINKWORKTIMELONG),avg(LINKWORKTIMELONG),sum(LINKWORKTIMELONG) from TB_EMP_ATTENDTIMERULE";
        System.out.println("最高成绩\t最低成绩\t平均成绩\t总成绩\t");
        Object[] objects = DBUtils.qr.query(sql, new ArrayHandler());
        for (Object object : objects) {
            System.out.print(object+"\t");
        }
    }
    @Test       //7、查询之返回到map直观显示：query -> MapHandler
    public void test8()throws SQLException{
//		查询全公司的最高成绩，最低成绩，平均成绩，总成绩
        String sql = "select max(LINKWORKTIMELONG) as 最高成绩,min(LINKWORKTIMELONG) as 最低成绩,avg(LINKWORKTIMELONG) as 平均成绩,sum(LINKWORKTIMELONG) as 总成绩 from TB_EMP_ATTENDTIMERULE";
        Map<String, Object> map = DBUtils.qr.query(sql, new MapHandler());
        //key是字段名
        //value是字段值
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
//			String name = entry.getKey();
//			Object value = entry.getValue();
//			System.out.println(name + ":" + value);

            System.out.println(entry);
        }
    }

    @Test       //8、分组查询返回list<Map>：query -> MapListHandler
    public void test9()throws SQLException {
//		查询每个部门的最高成绩，最低成绩，平均成绩，总成绩
        String sql = "select id,max(LINKWORKTIMELONG) as 最高成绩,min(LINKWORKTIMELONG) as 最低成绩,avg(LINKWORKTIMELONG) as 平均成绩,sum(LINKWORKTIMELONG) as 总成绩 from TB_EMP_ATTENDTIMERULE where id is not null group by id ";
        List<Map<String, Object>> query = DBUtils.qr.query(sql, new MapListHandler());
        for (Map<String, Object> map : query) {
            Set<Map.Entry<String, Object>> entrySet = map.entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                System.out.println(entry);
            }
            System.out.println("\t");
        }
    }
}
