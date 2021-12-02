package java设计模式;

/*
     工厂模式：（了解）
     	生活中工厂：批量生产符合某种标准规范的产品。
      Java中工厂：批量生产某种接口的实现类对象。

      生活中：把造产品的和使用产品的分离。
      Java中工厂：把对象的创建者和对象的使用者分离。

      分工

     没工厂模式：自己new对象，自己用。
     工厂模式：工厂new对象，我来用。

     情景：
     	 需求：完成用户的登录、注册、个人信息修改...
      要求：测试阶段可能用的是sql server，生产环境可能要求mysql，后期可能升级为oracle

 */
public class 工厂设计模式 {
    public static void main(String[] args) {
        //手动创建
        //测试用sql server实现功能
//		IUser s = new SqlServer();
        //上线用sql server实现功能
//		IUser s = new MySql();
        //后期修改为Oracle
//		IUser s = new Oracle();
        //因为创建对象是复杂多变的，因此把这部分独立出去
        //通过工厂
        IUser s = SimpleFactory.createIUser("mysql");//命令行参数

        //(1)注册
        s.register();
        //(2)登录
        s.login();
        //(3)修改个人信息
        s.update();
    }
}

//工厂的目的是生产对象
//这里用来生产IUser接口的实现类的对象
class SimpleFactory {
    public static IUser createIUser(String type) {
        if ("sqlServer".equals(type)) {
            return new SqlServer();
        } else if ("mysql".equals(type)) {
            return new MySql();
        } else if ("oracle".equals(type)) {
            return new Oracle();
        }
        return null;
    }
}

interface IUser {
    void login();
    void register();
    void update();
}

class SqlServer implements IUser {
    @Override
    public void login() {
        System.out.println("sql server版登录");
    }
    @Override
    public void register() {
        System.out.println("sql server版注册");
    }
    @Override
    public void update() {
        System.out.println("sql server版修改");
    }
}

class MySql implements IUser {
    @Override
    public void login() {
        System.out.println("mysql版登录");
    }
    @Override
    public void register() {
        System.out.println("mysql版注册");
    }
    @Override
    public void update() {
        System.out.println("mysql版修改");
    }
}

class Oracle implements IUser {
    @Override
    public void login() {
        System.out.println("Oracle版登录");
    }
    @Override
    public void register() {
        System.out.println("Oracle版注册");
    }
    @Override
    public void update() {
        System.out.println("Oracle版修改");
    }
}
