package 网络编程.UDP变成__简单示例;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class 接收端 {
    public static void main(String[] args) throws Exception{
        //(1)建立Socket
        DatagramSocket ds = new DatagramSocket(8888);//需要指定端口号

        //(2)准备一个数据报，用来接收数据
        byte[] data = new byte[1024];
        /*
         * 第一个参数：用来装数据的字节数组
         * 第二个参数：最多可以装几个字节
         */
        DatagramPacket dp = new DatagramPacket(data, data.length);

        //(3)接收数据
        ds.receive(dp);//这是一个阻塞方法，如果么有消息，就一直等着

        //(4)处理
        int len = dp.getLength();
        System.out.println(new String(data,0,len));

        ds.close();
    }
}