package 反射;

import org.junit.jupiter.api.Test;

import java.lang.reflect.*;

/*
    一、获取反射对象
        （1）类型名.class
        （2）对象.getClass()	//返回对象的运行时类型
        （3）Class.forName("类型的全名称")	//只能用引用数据类型  //参数全名称也就是“包.类名”
        （4）ClassLoader对象.loadClass("类型的全名称")
    二、创建对象
        （1）Class对象.newInstance()                                     //调用公共的无参构造器创建对象
        （2）Constructor<?>[] getConstructors()                          //获取所有的公共的构造方法
             Constructor<?>[] getDeclaredConstructors():                 //所有构造方法，公共的不公共的都有
             Constructor<T> getConstructor(Class<?>... parameterTypes)    //返回一个指定的公共的构造方法
             Constructor<T> getDeclaredConstructor(Class<?>... parameterTypes) ：     //返回指定的构造方法，可以不是公共的
             constructor.setAccessible(true);                                 //设置构造器可访问
             constructor.newInstance(1,"张");                                 //返回该类实例对象
    三、获取和设置属性值
        Field getField(String name)                         //获取指定的公共的属性
        Field[] getFields()                                 //获取所有的公共的属性
        Field getDeclaredField(String name)                 //获取指定的属性
        Field[] getDeclaredFields()                         //获取所有的属性
        属性对象.set(实例对象，value)                       //设置属性值为value
        属性对象.get(实例对象)                              //获取该实例对象的该属性值
    四、利用反射调用方法
        Method[] 实例对象.getMethods()                      //获取所有公共的方法
        Method getMethod(xxx)                               //获取指定的公共的方法。参数：①"方法名"②型擦类型参数Student.Class
        Method[] getDeclaredMethods()                       //获取所有声明的方法
        Method[] getDeclaredMethod(xxx)                     //获取指定的方法。参数：①"方法名"②型擦类型参数Student.Class
        addMethod.invoke(obj, stuClass.newInstance());      //调用方法。参数：①实例对象，即那个对象的方法②方法的实参列表
    五、获取任意类型的信息
        （1）包：getPackage() &ndash;&gt;Package &ndash;&gt;
        （2）修饰符：getModifier() &ndash;&gt; int整数值  &ndash;&gt; Modifier类型
        （3）父类：  A：不带泛型信息：getSuperClass() &ndash;&gt; Class
        	  	  B：带泛型信息：getGenericSuperClass() &ndash;&gt; Type
        （4）接口
        A：不带泛型信息：getInterfaces() &ndash;&gt;Class[]
        B：带泛型信息：getGenericInterfaces() &ndash;&gt;Type[]
        （5）属性
        A：获取所公共的Fields  &ndash;&gt; Field[] getFields()
        B：获取所已经声明的 &ndash;&gt; Field[]  getDeclaredFields()
        C：获取其中一个公共的 &ndash;&gt; Field getField(属性名)
        D：获取一个已经声明的 &ndash;&gt; Field getDeclaredField(属性名)
        （6）构造器
        A：获取所公共的构造器 &ndash;&gt;Constructor[] getConstructors()
        B：获取所已经声明的构造器 &ndash;&gt;Constructor[] getDeclaredConstructors()
        C：获取一个公共的构造器 &ndash;&gt; Constructor getConstructor(形参类型列表)
        D：获取一个已经声明的构造器 &ndash;&gt;  Constructor getDeclaredConstructor(形参类型列表)
        E:形参列表们：getParameterTypes（）；
        （7）方法
        A：获取所的公共的方法 &ndash;&gt; Method[] getMethods()
        B：获取所已经声明的方法 &ndash;&gt; Method[] getDeclaredMethods()
        C：获取其中一个公共的方法 &ndash;&gt; Method getMethod(方法名, 方法的形参类型列表)
        D：获取其中一个已经声明的的方法 &ndash;&gt; Method getDeclaredMethod(方法名, 方法的形参类型列表)
        E：getModifiers	修饰符
        F：method.getReturnType()；	//方法的返回值们
        G：method.getName()；		//方法名们
        H：method.getParameterTypes		//形参列表们，返回class数组，比那里打印他们的getName
        I：method.getExceptionTypes();	//异常们，便利直接打印即可
    六、注解和泛型
        （1）c.getAnnotation(MyAnnotation.class); my.value();     //获取类/属性/方法上的注解信息，并取出值
        （2）subClass.getSuperclass();                            //获取反省父类（这个得不到泛型信息）
        （3）(ParameterizedType)subClass.getGenericSuperclass();  //获取泛型父类信息，并且把type类型强制转化为ParameterizedType类型
        （4）p.getActualTypeArguments();                          //获取实际的类型参数
 */
public class 反射的所有方法 {

    @Test   // 一：获取Class对象
    public void test1() throws ClassNotFoundException {
        // 1、获取Class对象方法（1）类型名.class
        Class<Singleton> singletonClass = Singleton.class;
        // 2、获取Class对象方法（2）对象.getClass()
        Class<?> singletonClass2 = new Singleton().getClass();
        // 3、获取Class对象方法（3）Class.forName("类型的全名称")
        Class<?> singletonClass3 = Class.forName("反射.Singleton");
        // 4、获取Class对象方法（4）ClassLoader对象.loadClass("类型的全名称")
        Class<?> singletonClass4 = ClassLoader.getSystemClassLoader().loadClass("反射.Singleton");
    }

    @Test // 二：动态创建对象
    public void test2() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        // (1)获取Class对象
        Class singleton = Class.forName("反射.Singleton");
        // (2-1) 方法一：创建Class对象代表的类型的对象，这里Class对象代表的是反射.Singleton类型
        Object obj1 = singleton.newInstance();
        System.out.println(obj1);
        // (2-2) 方法二：获取构造器对象Singleton的构造器对象
        Constructor constructor = singleton.getDeclaredConstructor(String.class, int.class);    // 传参 指定构造器的形参的类型对应的Class
        constructor.setAccessible(true);    //设置构造器可访问，即使private也可访问
        Object obj2 = constructor.newInstance("张三", 30);
        System.out.println(obj2);
    }

    @Test // 三：动态的获取对象的属性值或设置属性值
    public void test3() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        //（1）获取Class对象
        Class c = Class.forName("反射.Singleton");//....

        //（2）创建Class对象代表的类型的实例对象
        Object obj = c.newInstance();//这里obj是代表Student对象

        //（3）获取属性对象
        Field ageField = c.getDeclaredField("age");//ageField代表Student的age属性
        Field nameField = c.getDeclaredField("name");
        ageField.setAccessible(true);   // 设置属性可访问，即使属性/get/set方法都是private也可用
        nameField.setAccessible(true);

        //（4）设置属性值
        //第一个：哪个对象的age属性值
        //第二个：设置为什么值
        ageField.set(obj, 99);
        nameField.set(obj, "张三");
        System.out.println(obj);

        //（5）获取属性值
        Object age = ageField.get(obj);//获取obj对象的age属性值
        Object name = nameField.get(obj);
        System.out.println("学号：" + age);
        System.out.println("姓名：" + name);
    }

    @Test // 四：动态的调用任意一个方法
    public void test4() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        //（1）获取Class对象
        Class singletonClass = Class.forName("反射.Singleton");
        //（2）创建Class对象代表的类型的实例对象
        Object obj = singletonClass.newInstance();//obj是Singleton的对象
        Class printClass = Class.forName("反射.Print");
        //（3）获取某个要调用的方法
        /*
         * 第一个参数：方法名
         * 第二个参数：方法的形参类型列表
         */
        Method addMethod = singletonClass.getDeclaredMethod("print2", Print.class, String.class);   //addMethod代表public void print2(Print print, String str) {*}
        addMethod.setAccessible(true);

        //（4）执行/调用addMethod代表的方法
        /*
         * 第一个参数：实例对象，即那个对象的方法
         * 第二个参数：方法的实参列表
         */
        Object all = addMethod.invoke(obj, printClass.newInstance(), "第二个参数！");
        System.out.println(all);    // 方法无返回值，所以打印null
    }

    @Test // 五：动态的获取任意类型的信息
    public void test5() throws ClassNotFoundException {
		// 获取Class对象
        Class c = Class.forName("反射.Singleton");

		// A：获取包信息
        Package pkg = c.getPackage();
        System.out.println("包名：" + pkg.getName());

		// B：获取类名
        String typeName = c.getName();
        System.out.println("类名称：" + typeName);

        // C：修饰符
        int mod = c.getModifiers();
        System.out.println("修饰符：" + mod);
        System.out.println(Modifier.toString(mod));

        // D：父类
        Class superclass = c.getSuperclass();
        System.out.println("父类：" + superclass);

        // E：接口们
        System.out.println("接口们：");
        Class[] interfaces = c.getInterfaces();
        for (Class inter : interfaces) {
            System.out.println(inter);
        }

        // F：所有的属性
        /*
         * 所的属性的共同的特征：
         * 	修饰符  数据类型 属性名;
         * 	set属性值和get属性值
         */
        System.out.println("属性们：");
        Field[] declaredFields = c.getDeclaredFields();
        for (Field field : declaredFields) {
            int fMod = field.getModifiers();
            System.out.println("属性的修饰符：" + Modifier.toString(fMod));

            Class<?> type = field.getType();
            System.out.println("属性类型：" + type.getName());

            String name2 = field.getName();
            System.out.println("属性名：" + name2);
        }

        // G:所的构造器
        /*
         * 所的构造器：
         * 		修饰符   构造器名(形参列表)
         * 		行为：创建对象  newInstance
         */
        System.out.println("构造器们：");
        Constructor[] declaredConstructors = c.getDeclaredConstructors();
        for (Constructor constructor : declaredConstructors) {
            int fMod = constructor.getModifiers();
            System.out.println("构造器的修饰符：" + Modifier.toString(fMod));

            String name2 = constructor.getName();
            System.out.println("构造器名：" + name2);

            System.out.println("形参列表们：");
            Class[] parameterTypes = constructor.getParameterTypes();
            for (Class class1 : parameterTypes) {
                System.out.println(class1.getName());
            }
        }

        // H:所有的方法
        /*
         * 所的方法：
         * 		修饰符  返回值类型  方法名(形参列表)抛出的异常列表们
         * 		行为：被调用  invoke
         */
        System.out.println("方法们：");
        Method[] declaredMethods = c.getDeclaredMethods();
        for (Method method : declaredMethods) {
            int fMod = method.getModifiers();
            System.out.println("方法的修饰符：" + Modifier.toString(fMod));

            Class<?> returnType = method.getReturnType();
            System.out.println("方法的返回值类型："  + returnType.getName());

            String name2 = method.getName();
            System.out.println("方法名：" + name2);

            System.out.println("形参列表们：");
            Class[] parameterTypes = method.getParameterTypes();
            for (Class class1 : parameterTypes) {
                System.out.println(class1.getName());
            }

            System.out.println("异常们：");
            Class<?>[] exceptionTypes = method.getExceptionTypes();
            for (Class<?> class1 : exceptionTypes) {
                System.out.println(class1);
            }
        }
    }
}

// 类对象
class Singleton {
    private String name;
    private int age;

    public Singleton() {
    }

    private String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    private int getAge() {
        return age;
    }

    private void setAge(int age) {
        this.age = age;
    }

    private Singleton(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Singleton{" + "name='" + name + '\'' + ", age=" + age + '}';
    }

    public void print2(Print print, String str) {
        System.out.println(toString() + " ==> " + print.print() + " ==>> " + str);
    }
}

class Print {
    public String print() {
        return "调用了Print的方法";
    }
}