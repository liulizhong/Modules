package aaa课程代码.day01_数据类型;

/**
 一、Java程序开发步骤：
     1、编写源代码，编辑源文件
         代码结构：
         类{
            方法{
                代码;
            }
         }
     要求：源文件必须保存为.java

     2、编译
         用javac.exe这个编译工具把“源代码（.java）”编译成“字节码（.class）”     格式：javac 源文件名.java
         Java的程序是在JVM的虚拟机中运行，那么JVM只认识字节码数据（.class）
         每次代码编辑后，要重新编译

     3、运行
         在JVM中运行“字节码（.class）”，JVM把字节码转成CPU能识别的指令
         格式：
         “java 类名” 或 “java 字节码文件名”
 二、乱码问题
    1、字符编码：不同的字符编码之间是不认识
         GBK：支持中文（支持简体和繁体），中文操作系统默认的一个字符编码方式
         UTF-8：支持中文，支持全世界所有的常用文字
         ISO-8859-1：不支持中文
         GB2312：支持中文，不支持繁体等
         BIG5：支持繁体字
         ANSI：表示用当前操作系统默认的字符编码
    2、解决方法
        （1）修改当前文件： 【格式】菜单->转为xx
        （2）修改所有的新建文件：【设置】菜单->【首选项】->【新建】->Windows->ANSI
 */
public class HelloWorld {
    //主方法：main()，是Java程序的入口
    public static void main(String[] args) {
        // 输出语句，打印语句
        // ln代表line，行
        // ""中间的原样显示
        // 每一个语句后面加;
        System.out.println("Hello Word!!!");
    }
}
