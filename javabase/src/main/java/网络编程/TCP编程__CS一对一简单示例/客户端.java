package 网络编程.TCP编程__CS一对一简单示例;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/*
      1、主动连接服务器
      通过创建一个Socket，主动连接服务器

      2、通信
      如果要收，用输入流
      如果要发，用输出流
 */
public class 客户端 {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("172.16.192.128", 9999);

        OutputStream out = socket.getOutputStream();
        out.write("你好".getBytes());
        socket.shutdownOutput();//关闭输出通道

        InputStream is = socket.getInputStream();
        byte[] data = new byte[1024];
        StringBuilder s = new StringBuilder();
        while(true){
            int len = is.read(data);
            if(len == -1){
                break;
            }
            s.append(new String(data,0,len));
        }
        System.out.println("服务器回复的内容：" + s);

        socket.close();
    }
}