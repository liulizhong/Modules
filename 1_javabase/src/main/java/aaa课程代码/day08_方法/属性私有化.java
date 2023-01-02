package aaa课程代码.day08_方法;

/**
 * 声明一个银行账户类型：有属性：账户、密码、户主名字、余额
 * <p>
 * 一个类的属性，对外界想要控制访问，那么可以给属性设置修饰符，例如：private
 * <p>
 * 权限修饰符/访问控制修饰符：可见的范围从小到大
 * 范围		    本类	本包	其他包子类		其他包非子类
 * private		可以	不可以   不可以          不可以
 * 缺省		    可以	可以     不可以          不可以
 * protected	    可以	可以	 可以			 不可以
 * public 		可以	可以	 可以			 可以
 * <p>
 * 权限修饰符可以修改：属性、方法、构造器、内部类
 * 其中的public和缺省，还可以修改类
 * <p>
 * 回忆：
 * 属性的声明：
 * 【修饰符】 数据类型  属性名;
 * <p>
 * 类的声明：
 * 【修饰符】 class  类名{
 * <p>
 * }
 */
public class 属性私有化 {
    public static void main(String[] args) {
        Accounts a = new Accounts();
//        a.id = "10001001"; // 私有的属性，不是本类 不能访问
//        a.password = "123456";
        a.name = "chailinyan";
//        a.balance = 1200;

//        a.balance = -12000;
//        System.out.println("账户：" +a.id + "，余额：" + a.balance);
    }
}

//设计了一个银行账户类
//账户对象创建（卡办理），这个账户，户主的姓名是不可以修改的，
//余额是不想设置为负数
class Accounts {
    private String id;//账户
    private String password;//密码
    String name;//户主的姓名
    private double balance;//余额
}