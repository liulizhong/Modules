package java设计模式;

/*
    1、饿汉式 - 枚举
    2、饿汉式 - 直接创建
    3、饿汉式 - 方法返回
    4、懒汉式 - 调用再创建
 */

// 【1、饿汉式】 - 枚举
public enum 单例设计模式 {
    INSTANCE
}

// 【2、饿汉式】 - 直接创建
class Hungry {
    public static final Hungry INSTANCE = new Hungry();

    private Hungry() {
    }
}

// 【3、饿汉式】 - 方法返回
class Hungry2 {
    private static final Hungry2 INSTANCE = new Hungry2();

    private Hungry2() {
    }

    public static Hungry2 getInstance() {
        return INSTANCE;
    }
}

// 【4、懒汉式】 - 调用再创建
class Lazy {
    private static Lazy instance;

    private Lazy() {
    }

    public static Lazy getInstance() {
        if (instance == null) {//为了提高效率用
            synchronized (Lazy.class) {
                if (instance == null) {//为了保证唯一的对象用的
                    instance = new Lazy();
                }
            }
        }
        return instance;
    }
}
