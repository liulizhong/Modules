package 枚举和注解;

import java.lang.annotation.*;

/*
    1、注解：代码级别的注释又称为注释，它是代码级别的注释，即用代码去注释代码，由代码去读取注释。单行注释，多行注释是给人看。 JDK1.5之后引入
    2、注解由个部分组成：
    （1）注解的声明
    （2）注解的使用
    （3）注解的读取

    3、常见的四大类注解
    （1）系统预定义的3个基本注解
    （2）系统预定义的文档注解
    （3）后期web或框架中的提供的注解：作用是替代原来的xml配置文件
    （4）在JUnit框架中提供的注解：辅助完成单元测试

    4、系统预定义的三个基本注解：
    @Override：加在重写的方法上，表示给方法是重写的方法，告诉编译器按照重写的要求检查这个方法。
    声明：java.lang.Override
    使用：在重写的方法上
    读取：编译器
    @Deprecated：加在类、方法、属性等上，表示该元素是已过时的，不建议程序员继续使用，否则编译器报警告，若强行使用后果自负。
    声明：java.lang.Deprecated
    使用：标记在方法、类....
    读取：编译器
    @SuppressWarnings：加在类、方法、属性等上，表示抑制xx警告，告诉编译器不在弹出xx的警告
    声明：java.lang.SuppressWarnings
    使用：标记在方法、类....
    读取：编译器

    5、系统预定义的文档注解
        @author：代码作者，格式： @author 作者
        @since：从xx版本开始，格式：@since 版本
        @see：另请参阅，其他的文档参考，格式：@see 其他类型
        @version：表明当前版本，格式：@version 版本

    方法上的注解：有存在的才有注解，没有的话就不写
        @param：表明方法的形参，格式：@param 形参的名称  形参的类型  形参的说明* @param a int 一个整数
        @return：表明方法的返回值类型，格式：@return 返回值类型  返回值的说明 * @return int 返回两个整数a,b中的最大值
        @exception/@throws：表明方法的抛出的异常类型，
        格式：@exception/@throws 异常类型 说明xx情况下会抛出该异常* @throws FileNotFoundException 当src不存在时，会抛出文件找不到的异常

    文档注解要配合javadoc.exe使用，生成API文档用

    6、在JUnit框架中提供的注解
    引入JUnit的库：
    项目名称上右键-->Build Path --> Add Libraries -->JUnit 4
    、JUnit工具中的注解（了解）

    @Test：标记在方法上，表示该方法一个单元测试方法
        方法要求：public，void，()空参、非静态方法,必须是公共的无返回值的无参的非静态方法

    @BeforeClass ：在所有的@Test标记的方法之前，只运行一次
            方法要求：public，void，()的静态方法，必须是公共的无返回值的无参的非静态方法

    @AfterClass：在所有@Test之后，只运行一次
            方法要求：public，void，()的静态方法，必须是公共的无返回值的无参的非静态方法

    @Before：在所的@Test标记的方法之前，每一个@Test之前都会运行
        方法要求：public，void，()的非静态方法，必须是公共的无返回值的无参的非静态方法

    @After：在@Test之后，每一个@Test之后都会运行
            方法要求：public，void，()的非静态方法，必须是公共的无返回值的无参的非静态方法

    @Ignore：本次测试忽略某方法

    8、跟踪代码依赖性，实现替代配置文件功能，例put请求类似：@WebServlet("/denglu")
    应用在后面的web开发，框架（Spring，Hibernate，MyBatis等..
    用注解替代xml配置文件

    xml：详细，可读性好，但是太复杂
    注解：和被注释的代码更近，更方便的查看，但是信息不如xml丰富
    所以后面框架柱会  注解 + xml


  注解：（1）声明（2）使用（3）读取

  自定义注解
  一、声明注解
      1、语法格式
          【修饰符】 @interface 注解名{
          }
          或
          @元注解 【修饰符】 @interface 注解名{
          配置参数
          }
          
          配置参数的格式：  数据类型  参数名();
          
      2、元注解:  java.lang.annotation.*
      元注解：注解注解的注解，给注解加的注解
      一共有四个元注解：
          （1）@Target：标记某个注解可以被使用在哪些位置
              @Target它有一个配置参数：ElementType[]，只能赋值为ElementType（枚举类型）的常量对象 因为它是数组，所以
              @Target(ElementType.TYPE)
              @Target({ElementType.TYPE,ElementType.METHOD})
          （2）@Retention：标记某个注解可以被滞留到什么时候
              @Retention也有一个配置参数：RetentionPolicy类型（枚举类型）
              @Retention(RetentionPolicy.SOURCE) RetentionPolicy：SOURCE(给编译器读)，CLASS（类加载），RUNTIME(后期运行时以及反射才能读取到）
          （3）@Documented：标记某个注解是否可以被javadoc.exe读取到API中
          （4）@Inherited：表示某个注解是否可以被继承到子类中
  
  二、使用注解
      （1）要注意被使用的注解的@Target，因为要看它可以被用在什么位置
      （2）要注意被使用的注解是否有配置参数，如果有，就要给他传对应类型的参数值
              @注解名(参数名 = 参数值)
              @注解名(参数名 = 参数值,参数名 = 参数值。。。)
              如果参数类型是数组类型，那么参数值需要用{}
          如果配置参数只有一个需要赋值，并且它的名字是value，那么可以省略"value="
          配置参数的类型：类型只能是八种基本数据类型、String类型、Class类型、enum类型、Annotation类型、以上所有类型的数组
  
  三、读取注解
        通过反射读取
 */
public class 自定义注解 {
    public static void main(String[] args) {
        //读取注解信息
        //需要用到反射
//		(1)//获取MyClass类的类型信息
        Class clazz = MyClass.class;

//		(2)获取MyClass上的注解信息
        MyAnnotation my = (MyAnnotation) clazz.getAnnotation(MyAnnotation.class);

        //(3)显示注解信息
        System.out.println(my);//要被反射读取到，该注解必须是RetentionPolicy.RUNTIME
        System.out.println(my.value());

        Class c2 = SubClass.class;
        MyAnnotation my2 = (MyAnnotation) c2.getAnnotation(MyAnnotation.class);
        System.out.println(my2);//子类要读取到，那么@MyAnnotation需要标记@Inherited

    }
}

//使用注解
@MyAnnotation(value = "bizhan")
class MyClass {
}

class SubClass extends MyClass {
}

//声明了一个注解
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@interface MyAnnotation {
    String value() default "哔站";
}