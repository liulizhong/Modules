package java设计模式;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/*
     动态代理的实现步骤：
     1、编写一个代理工作处理器
     	代理要替被代理做xxx事情。
     要求：这个类必须实现InvocationHandler接口

     2、创建代理者的对象
     要用到，Proxy工具类，它里面一个方法，可以直接创建代理类的对象。
     		代理类是在JVM当中自动生成的一个类型


     类加载器：加载类的工具类
     如何获取类加载器对象？
     通过这个类型的Class对象，就可以得到它的类加载器对象
 */
public class 动态代理设计模式 {
    public static void main(String[] args) {
        //(1)先获取被代理者的Class对象
        Class c = NewMySQLVersion.class;
        //(2)获取它的类加载器对象
        ClassLoader loader = c.getClassLoader();
        /*
         * 第一个参数：被代理者
         * 第二个参数：是否代理工作
         */
        MyInvocationHandler my = new MyInvocationHandler(new MySQLVersion(), true);
        /*
         * 第一个参数：类加载器对象，给它传被代理者的类加载器对象
         * 第二个参数：被代理者实现的主题接口们
         * 第个参数：代理者要替被代理者完成的代理工作的处理器对象
         */
        Object proxyInstance = Proxy.newProxyInstance(loader, c.getInterfaces(), my);
        //转型
        //proxyInstance是代理者对象，它和被代理者一样都实现了NewIGoods
        NewIGoods ig = (NewIGoods) proxyInstance;
        ig.add();
        ig.delete();
        ig.update();
        ig.delete();

        //被代理者
        Class uc = UserManager.class;
        MyInvocationHandler my2 = new MyInvocationHandler(new UserManager(), true);
        NewIUser iu = (NewIUser) Proxy.newProxyInstance(uc.getClassLoader(), uc.getInterfaces(), my2);
        iu.add();
        iu.update();
        iu.delete();
        iu.list();
    }
}

class MyInvocationHandler implements InvocationHandler {
    private Object target;
    private boolean flag;

    public MyInvocationHandler(Object target, boolean flag) {
        super();
        this.target = target;
        this.flag = flag;
    }

    /*
     * 第一个参数：Object proxy：代理对象
     * 第二个参数：被代理者要执行的方法
     * 第个参数：要传给被代理者要执行的方法的实参列表
     * 说明，这个方法不是程序员手动调用的，是在已经实现好别的类中帮我们调用
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //代理要替被代理做xxx事情。
        Object result = null;//被代理者要执行的方法的返回值，如果该方法的返回值类型是void，那么result就是null
        if (flag) {
            System.out.println("开始...记录日志，获取开始时间，权限验证...");
            result = method.invoke(target, args);//被代理者执行method方法
            System.out.println("结束...获取开始时间，计算时间差，记录日志，释放资源等");
        } else {
            result = method.invoke(target, args);//被代理者执行method方法
        }
        return result;
    }

}

//新主题
interface NewIUser {
    void add();

    void delete();

    void update();

    void list();
}

//新的被代理类
class UserManager implements NewIUser {
    @Override
    public void add() {
        System.out.println("用户添加");
    }

    @Override
    public void delete() {
        System.out.println("用户删除");
    }

    @Override
    public void update() {
        System.out.println("用户修改");
    }

    @Override
    public void list() {
        System.out.println("用户查询");
    }
}


//主题接口
interface NewIGoods {
    void add();

    void delete();

    void update();

    void list();
}

//被代理者们
class NewMySQLVersion implements NewIGoods {
    @Override
    public void add() {
        System.out.println("mysql版的添加商品");
    }

    @Override
    public void delete() {
        System.out.println("mysql版的删除商品");
    }

    @Override
    public void update() {
        System.out.println("mysql版的修改商品");
    }

    @Override
    public void list() {
        System.out.println("mysql版的查询商品");
    }
}

class NewOracleVersion implements NewIGoods {
    @Override
    public void add() {
        System.out.println("oracle版的添加商品");
    }

    @Override
    public void delete() {
        System.out.println("oracle版的删除商品");
    }

    @Override
    public void update() {
        System.out.println("oracle版的修改商品");
    }

    @Override
    public void list() {
        System.out.println("oracle版的查询商品");
    }
}