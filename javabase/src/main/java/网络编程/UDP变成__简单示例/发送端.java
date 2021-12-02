package 网络编程.UDP变成__简单示例;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/*
    TCP协议：面向连接的，可靠的，基于字节流的传输层的通信协议
    UDP协议：非面向连接的，不可靠的，基于数据报的传输层的通信协议
        不管是TCP还是UDP，通信两端都要Socket
    分为：
    流套接字：使用TCP提供可依赖的字节流服务
    		ServerSocket ,Socket
    数据报套接字：使用UDP提供“尽力而为”的数据报服务
    		DatagramSocket
 */
public class 发送端 {
    public static void main(String[] args) throws Exception{
        //(1)建立Socket
        DatagramSocket ds = new DatagramSocket();//自动分配IP地址和端口号

        //（2)创建一个数据报
        String str = "马上下课了";
        byte[] data = str.getBytes();
        InetAddress dest = InetAddress.getByName("192.168.34.53");
        /*
         * 第一个参数：要发送的数据的字节数组
         * 第二个参数：长度
         * 第个参数：接收方的IP地址，并且是InetAddress
         * 第四个参数：接收方的端口号
         */
        DatagramPacket dp = new DatagramPacket(data, data.length, dest, 8888);

        //(3)发送
        ds.send(dp);
        System.out.println("发送完毕");

        ds.close();
    }
}