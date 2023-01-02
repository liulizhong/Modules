package aaa课程代码.day07_类￥属性;

/**
     1、声明一个员工类型Employee，属性：编号、姓名、薪资、生日
     2、生日的类型是一个特殊的类型MyDate类型，属性：年、月、日
     3、创建两个员工对象：
         编号	姓名	薪资		生日
         1		张三	10000.0		1990年12月1日
         2		李四	15000.0		1993年1月1日

     数据类型：
         基本数据类型（8种）
         引用数据类型：类、接口、数组
 */
public class  类和属性练习题3_员工类属性是类 {
    public static void main(String[] args){
        //1、先创建一个员工对象
        Employee e1 = new Employee();

        //为e1对象的属性赋值
        e1.id = 1;
        e1.name = "张三";
        e1.salary = 10000.0;
        //因为birthday是一个引用数据类型的属性，所以赋值为一个对象
        e1.birthday = new MyDate();
        //如果没有上一行，下面代码报java.lang.NullPointerException  e1.birthday=null
        e1.birthday.year = 1990;
        e1.birthday.month = 12;
        e1.birthday.day = 1;

        //创建第二个员工对象
        Employee e2 = new Employee();

        //为e2对象的属性赋值
        e2.id = 2;
        e2.name = "李四";
        e2.salary = 15000.0;
        e2.birthday = new MyDate();//因为birthday是一个引用数据类型的属性，所以赋值为一个对象
        e2.birthday.year = 1993;//如果没有上一行报java.lang.NullPointerException  e2.birthday=null
        e2.birthday.month = 1;
        e2.birthday.day = 1;

        System.out.println("编号\t姓名\t薪资\t生日");
        System.out.println(e1.id + "\t" + e1.name + "\t"  + e1.salary + "\t" + e1.birthday.year + "年" + e1.birthday.month + "月" + e1.birthday.day + "日");
        System.out.println(e2.id + "\t" + e2.name + "\t"  + e2.salary + "\t" + e2.birthday.year + "年" + e2.birthday.month + "月" + e2.birthday.day + "日");
    }
}
class Employee{
    //[修饰符] 数据类型 属性名;
    int id;
    String name;
    double salary;

    //MyDate是一个类，是一种数据类型
    MyDate birthday;//默认值是null
}

//自定义日期类型
class MyDate{
    int year;
    int month;
    int day;

}