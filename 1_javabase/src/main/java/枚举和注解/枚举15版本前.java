package 枚举和注解;

/*
     枚举：JDK1.5之后

     枚举：列举，穷举，罗列出某个类型的所有对象。即某个类型的对象是限个，不是任意可以创建的无限个。
     例如：Season季节，要求它的对象只有四个：春夏秋冬

     JDK1.5之前实现方式：
     (1)把类的构造器私化
     (2)在类中创建好限的几个对象，并用静态变量存储起来

     枚举改变的是使用者获取对象的方式。
 */
public class 枚举15版本前 {
    public static void main(String[] args) {
//		Season s = new Season();//错误的，构造器私化，在外部不可访问

        Season s1 = Season.SPRING;
        s1.test();
    }
}

class Season {
    //列出它的四个对象
    public static final Season SPRING = new Season();
    public static final Season SUMMER = new Season();
    public static final Season AUTUMN = new Season();
    public static final Season WINTER = new Season();

    private Season() {
    }

    public void test() {
        System.out.println("xx");
    }
}
