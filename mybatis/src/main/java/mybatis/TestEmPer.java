package mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @class 测试方法类
 */
public class TestEmPer {
    private static SqlSessionFactory sqlSessionFactory;

    //初始化SqlSessionFactory对象
    static {
        try {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("src/main/java/mybatis/mybatis-config.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 1、获取一个员工
    @Test
    public void testGetEmployeeById() throws IOException {
        // 1.创建SqlSessionFactory对象
        // 2.获取SqlSession对象，相当于JDBC中的Connection
        SqlSession session = sqlSessionFactory.openSession();
        try {
            // 3.获取Mapper接口的代理实现类
            EmployeeMapper employeeMapper = session.getMapper(EmployeeMapper.class);
            System.out.println(employeeMapper.getClass().getName());
            // 4.调用EmployeeMapper中获取Employee的方法
            Employee employee = employeeMapper.getEmployeeById(1);
//            Employee employee = employeeMapper.getEmployeAndDepById(1);   //高级映射 => 可关联查询
            System.out.println(employee);
        } finally {
            // 5.关闭SqlSession
            session.close();
        }
    }

    // 2、根据姓名和邮箱获取一个员工
    @Test
    public void testGetEmployeeByLastNameAndEmail() throws IOException {
        // 1.创建SqlSessionFactory对象
        // 2.获取SqlSession对象，相当于JDBC中的Connection
        SqlSession session = sqlSessionFactory.openSession();
        try {
            // 3.获取Mapper接口的代理实现类
            EmployeeMapper employeeMapper = session.getMapper(EmployeeMapper.class);
            // 4.调用EmployeeMapper中获取Employee的方法
            Employee employee = employeeMapper.getEmployeeByLastNameAndEmail("Mayun", "mayun@alibaba.com");
            System.out.println(employee);
        } finally {
            // 5.关闭SqlSession
            session.close();
        }
    }

    // 3、根据Map获取一个员工
    @Test
    public void testGetEmployeeByMap() throws IOException {
        // 1.创建SqlSessionFactory对象
        // 2.获取SqlSession对象，相当于JDBC中的Connection
        SqlSession session = sqlSessionFactory.openSession();
        try {
            // 3.获取Mapper接口的代理实现类
            EmployeeMapper employeeMapper = session.getMapper(EmployeeMapper.class);
            // 4.调用EmployeeMapper中获取Employee的方法
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("l", "mayun");
            map.put("e", "mayun@alibaba.com");
            Employee employee = employeeMapper.getEmployeeByMap(map);
            System.out.println(employee);
        } finally {
            // 5.关闭SqlSession
            session.close();
        }
    }

    // 4、获取一个员工
    @Test
    public void testGetEmployees() throws IOException {
        // 1.创建SqlSessionFactory对象
        // 2.获取SqlSession对象，相当于JDBC中的Connection
        SqlSession session = sqlSessionFactory.openSession();
        try {
            // 3.获取Mapper接口的代理实现类
            EmployeeMapper employeeMapper = session.getMapper(EmployeeMapper.class);
            // 4.调用EmployeeMapper中获取所有Employee的方法
            List<Employee> employees = employeeMapper.getEmployees();
            for (Employee employee2 : employees) {
                System.out.println(employee2);
            }
        } finally {
            // 5.关闭SqlSession
            session.close();
        }
    }

    // 5、添加员工
    @Test
    public void testAddEmployee() throws IOException {
        // 1.获取SqlSessionFactory对象
        // 2.获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            // 3.获取Mapper接口对象
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            // 4.创建Employee对象
            Employee employee = new Employee(null, "李彦宏", "lyh@baidu.com", 9000.00, new Department(2, "啥呀"));
            // 5.调用EmployeeMapper中添加员工的方法
            employeeMapper.addEmployee(employee);
            // 6.手动提交
            sqlSession.commit();
        } finally {
            // 7.关闭sqlSession
            sqlSession.close();
        }
    }

    // 6、更新员工
    @Test
    public void testUpdateEmployee() throws IOException {
        // 1.获取SqlSessionFactory对象
        // 2.获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            // 3.获取Mapper接口对象
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            // 4.创建要更新Employee对象
            Employee employee = new Employee(5, "李彦宏2", "lyh@baidu.com", 900.00, new Department(2, "啥呀"));
            // 5.调用EmployeeMapper中更新员工的方法
            employeeMapper.updateEmployee(employee);
            // 6.手动提交
            sqlSession.commit();
        } finally {
            // 7.关闭sqlSession
            sqlSession.close();
        }
    }

    // 7、删除员工
    @Test
    public void testDeleteEmployeeById() throws IOException {
        // 1.获取SqlSessionFactory对象
        // 2.获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            // 3.获取Mapper接口对象
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            // 4.调用EmployeeMapper中删除员工的方法
            employeeMapper.deleteEmployeeById(5);
            // 5.手动提交
            sqlSession.commit();
        } finally {
            // 6.关闭sqlSession
            sqlSession.close();
        }
    }
}