package aaa课程代码.day07_类￥属性;

/**
 * 2、练习题
 * 声明一个学生类Students，并声明它的属性：姓名、性别、年龄、电话号码
 * 创建两个对象，并为他们的属性赋值，并显示他们的信息
 */
public class  类和属性练习题2_学生类和属性 {
    public static void main(String[] args) {
        //1、创建学生对象
        Students s1 = new Students();
        Students s2 = new Students();

        s1.name = "张三";
        s1.gender = '男';
        s1.age = 23;
        s1.tel = "10086";

        s2.name = "李四";
        s2.gender = '女';
        s2.age = 24;
        s2.tel = "10010";

        System.out.println("姓名\t性别\t年龄\t电话");
        System.out.println(s1.name + "\t" + s1.gender + "\t" + s1.age + "\t" + s1.tel);
        System.out.println(s2.name + "\t" + s2.gender + "\t" + s2.age + "\t" + s2.tel);
    }
}

class Students {
    //【修饰符】 数据类型  属性名;
    String name;
    char gender;
    int age;
    String tel;
    //电话号码，身份证号码，订单编号，邮政编码等数字比较长，而且不会进行“算术运算”
    //像这样的一般不用整型
}