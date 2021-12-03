package aa面试题.java;

import org.junit.jupiter.api.Test;

/**
 * @author lizhong.liu
 * @version TODO
 * @class 代码吗i按实体1-10
 * @CalssName point1
 * @create 2020-08-27 14:27
 * @Des TODO
 */
public class point1_10 {

    @Test  // 面试题1：闭包
    public void interview1() {
        /*
        function Foo() {
            var i = 0;
            return function() {
                console.log(i++);
            }
        }
        var f1 = Foo(), // function() {console.log(i++);}
        var f2 = Foo(); // function() {console.log(i++);}

        // 1、一般来说函数执行完后它的局部变量就会随着函数调用结束被销毁，此题foo函数返回了一个匿名函数的引用（即一个闭包），它可以访问到foo()被调用产生的环境，
        //     而局部变量i一直处在这个环境中，只要一个环境有可能被访问到，它就不会被销毁，所以说闭包有延续变量作用域的功能。这就好理解为什么：
        // 2、重点是f2()输出的结果不是“2”，因为foo()返回的是一个匿名函数，所以f1,f2相当于指向了两个不同的函数对象，引用的变量不同，所以输出结果并不是2，而是0
        f1();   // 0
        f1();   // 1
        f2();   // 0
        */
    }


    @Test  // 面试题2：闭包
    public void interview2() {

    }
}
