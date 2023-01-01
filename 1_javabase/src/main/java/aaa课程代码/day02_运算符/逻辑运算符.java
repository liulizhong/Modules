package aaa课程代码.day02_运算符;

/**
 四、逻辑运算符
 逻辑与：（并且）&
 true & true 结果才为true
 true & false 结果false
 false & true 结果false
 false & false 结果false

 逻辑或：（或） |
 true | true 结果true
 true | false 结果true
 false | true 结果true
 false | false 结果false

 逻辑异或：^
 true ^ true 结果false
 true ^ false 结果true
 false ^ true 结果true
 false ^ false 结果false

 逻辑非：!
 单目运算符  !true 结果为false
 !false 结果为true

 短路与：（并且）&&
 true && true 结果才为true
 true && false 结果false
 false && ？ 结果false
 false && ？ 结果false

 当左边为false时，右边不看，结果直接是false

 短路或：（或） ||
 true || ? 结果true
 true || ? 结果true
 false || true 结果true
 false || false 结果false

 当左边为true时，右边不看，结果直接为true

 短路与和短路或，比逻辑与和逻辑或效率高，一般用短路与和短路或
 */
public class 逻辑运算符 {
    public static void main(String[] args) {
        int age = 8;
        char gender = '女';

        //要求：年龄在18岁以上，并且性别是女
        if(age>=18 & gender == '女'){
            System.out.println("可以考虑");
        }else{
            System.out.println("不考虑");
        }
        System.out.println("---------------异或------------------");
        System.out.println(true ^ true);
        System.out.println(true ^ false);
        System.out.println(false ^ true);
        System.out.println(false ^ false);
        System.out.println("---------------非------------------");
        boolean flag = false;
        if(!flag){
            System.out.println("1");
        }else{
            System.out.println("2");
        }

        //年龄要求：[18,35]之间
        //18 <=age结果boolean
        //boolean <= 35错误
        //if(18 <=age <=35){

        //}
        if(age>=18 && age<=35){
            // 此处不会进行比较 “age<=35”
        }
    }
}
