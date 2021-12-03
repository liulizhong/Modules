package java设计模式;
/*
     静态代理模式:（了解）

     	代理可以替被代理者做一些事情，辅助的，多变，复杂的事情。
     例如：房产中介，替我们“找房源，筛房源，跑流程。。。”    最核心事情：付钱，交钥匙
         红娘：红娘替我们“找女友,。。。。”    最核心事情：恋爱，结婚....

     角色：
     （1）代理者
     （2）被代理者
     （3）主题：接口（标准）

     要求：
     （1）代理者和被代理者实现同一个主题接口
     （2）代理者中要持被代理者的引用：target，然后把核心业务逻辑交给target被代理者自己完成。

      情景：
         项目经理说了，在测试阶段，
         要在每一个方法前面加上“开始...记录日志，获取开始时间，权限验证...”
         要在每一个方法后面加上“结束...获取开始时间，计算时间差，记录日志，释放资源等”

         上线时，要去掉

 */
public class 静态代理设计模式 {
    public static void main(String[] args) {
//		IGoods g = new Proxy(new MySQLVersion());
        IGoods g = new Proxy(new OracleVersion(),true);
//		IGoods g = new Proxy(new OracleVersion());
        g.add();
        g.delete();
        g.update();
        g.list();
    }
}

interface IGoods{
    void add();
    void delete();
    void update();
    void list();
}

/*interface IUser{
    void add();
    void delete();
    void update();
    void list();
}*/
class Proxy implements IGoods{
    private IGoods target;//被代理者
    private boolean flag;
    public Proxy(IGoods target) {
        super();
        this.target = target;
    }
    public Proxy(IGoods target, boolean flag) {
        super();
        this.target = target;
        this.flag = flag;
    }
    @Override
    public void add() {
        if(flag){
            System.out.println("开始...记录日志，获取开始时间，权限验证...");
            target.add();//核心的业务逻辑，还是被代理者自己完成
            System.out.println("结束...获取开始时间，计算时间差，记录日志，释放资源等");
        }else{
            target.add();//核心的业务逻辑，还是被代理者自己完成
        }
    }

    @Override
    public void delete() {
        if(flag){
            System.out.println("开始...记录日志，获取开始时间，权限验证...");
            target.delete();//核心的业务逻辑，还是被代理者自己完成
            System.out.println("结束...获取开始时间，计算时间差，记录日志，释放资源等");
        }else{
            target.add();//核心的业务逻辑，还是被代理者自己完成
        }
    }

    @Override
    public void update() {
        if(flag){
            System.out.println("开始...记录日志，获取开始时间，权限验证...");
            target.update();//核心的业务逻辑，还是被代理者自己完成
            System.out.println("结束...获取开始时间，计算时间差，记录日志，释放资源等");
        }else{
            target.add();//核心的业务逻辑，还是被代理者自己完成
        }
    }

    @Override
    public void list() {
        if(flag){
            System.out.println("开始...记录日志，获取开始时间，权限验证...");
            target.list();//核心的业务逻辑，还是被代理者自己完成
            System.out.println("结束...获取开始时间，计算时间差，记录日志，释放资源等");
        }else{
            target.add();//核心的业务逻辑，还是被代理者自己完成
        }
    }
}

class MySQLVersion implements IGoods{
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

class OracleVersion implements IGoods{
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