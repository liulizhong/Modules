package 网络编程.TCP编程__CS一对一简单示例;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/*
     服务器端：等着被连接的

     1、先启动服务器，并且指定监听的端口号
     通过创建一个ServerSocket

     2、接收连接
     accept()

     3、通信
     如果要收，用输入流
     如果要发，用输出流

     4、关闭
 */
public class 服务器 {
    public static void main(String[] args)throws Exception {
        ServerSocket server = new ServerSocket(9999);//默认服务器的IP地址是启动这个程序的主机ip地址
        System.out.println("等待连接...");

        //这个方法是一个阻塞的方法，如果没有客户端连接，那么一直等待
        //如果有人连接了，就会新建一个Socket对象，专门负责和这个客户端进行通信
        Socket socket = server.accept();
        System.out.println(socket.getInetAddress().getHostAddress() + "一个客户端连接成功...");

        //收消息
        InputStream is = socket.getInputStream();
        byte[] data = new byte[1024];
        StringBuilder s = new StringBuilder();
        while(true){
            int len = is.read(data);
            //
            if(len == -1){
                break;
            }
            s.append(new String(data,0,len));
        }
        System.out.println(s);

        //回复一个hello
        OutputStream out = socket.getOutputStream();
        out.write("hello".getBytes());

        //如果不再与它通信了，就可以断开连接
        socket.close();//如果socket关闭，那么通过它获得输入流和输出流都会关闭

        //如果服务器也不再接收别人的连接了，那么服务器也可以关闭
        server.close();//一般不关
    }
}