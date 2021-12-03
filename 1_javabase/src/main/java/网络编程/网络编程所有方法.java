package 网络编程;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.*;

/*
        一、java.net.InetAddress
            public static InetAddress getLocalHost()                //获取本机的InetAddress实例
            public static InetAddress getByAddress(byte[] addr)	    //得到byte[]数组，做了特殊处理，范围可以包下
            public static InetAddress getByName(String host)		//例如hoet输入"www.baidu.com."就得到百度的ip地址，
            public String getHostAddress()                          //返回 IP 地址字符串（以文本表现形式。这个方法就是把getLocalHost方法得到结果分解了
            public String getHostName()                             //获取此 IP 地址的主机名
            public String getCanonicalHostName()                    //获取此 IP 地址的完全限定域名
            public boolean isReachable(int timeout)                 //测试是否可以达到该地址。
    二、
            new URL("http://192.168.32.34:8080/docs/index.html");   //获取URL实例
            System.out.println("协议：" + url.getProtocol());
            System.out.println("主机名：" + url.getHost());
            System.out.println("端口号：" + url.getPort());
            System.out.println("路径名：" + url.getPath());
            System.out.println("文件名：" + url.getFile());        //注意如果存在锚点，那么查询名返回null，因为#后面全部当做锚点了
            System.out.println("锚点：" + url.getRef());
            System.out.println("查询名：" + url.getQuery());
            InputStream inputStream = url.openStream();             //获取InputStream对象
            URLConnection conn = url.openConnection();              //获取URLConnection
            conn.setDoOutput(true);                                 //设置属性，例如，设置该连接可以给服务器传数据
            OutputStream out = conn.getOutputStream();              //可以给服务器发数据，也可获取getInputStream
    三、TCP/IP变成
            new ServerSocket(9999);                     //默认服务器的IP地址是启动这个程序的主机ip地址
            Socket socket = server.accept();            //阻塞方法，没有客户端一直阻塞，有客户端连接了会新建一个Socket对象，专门负责和该客户通信
            InputStream is = socket.getInputStream();   //获取与该客户端连接的输入流，也可获取输出流socket.getOutputStream();
            socket.shutdownOutput();                    //关闭输出通道
 */
public class 网络编程所有方法 {

    @Test //java.net.InetAddress
    public void test1() throws IOException {
        // 1、获取本地/指定网址的 InetAddress 对象
        InetAddress inet = InetAddress.getLocalHost();
		InetAddress inet2 =InetAddress.getByName("www.baidu.com");
        // 2、获取指定网址的网络信息
        String host = inet.getHostName();           // 获取此 IP 地址的主机名
        String ip = inet.getHostAddress();          // 获取ip地址
        String chost = inet.getCanonicalHostName(); // 获取此 IP 地址的完全限定域名
        boolean reachable = inet.isReachable(5000); //
    }

    @Test //java.net.URL/URLcontetion
    public void test2() throws IOException {
        // 1、打印网络信息
        URL url = new URL("http://192.168.32.34:8080/docs/index.html");
        System.out.println("协议：" + url.getProtocol());
        System.out.println("主机名：" + url.getHost());
        System.out.println("端口号：" + url.getPort());
        System.out.println("路径名：" + url.getPath());
        System.out.println("文件名：" + url.getFile());
        System.out.println("锚点：" + url.getRef());//注意如果存在锚点，那么查询名返回null，因为#后面全部当做锚点了
        System.out.println("查询名：" + url.getQuery());
        // 2、以下是接收服务器数据(网页源代码下载)
        InputStream inputStream = url.openStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        FileWriter fileWriter = new FileWriter("url.txt");
        while (true) {
            String s = bufferedReader.readLine();
            if (s == null) { break; }
            fileWriter.write(s+"\r\n");
        }
        bufferedReader.close();
        inputStream.close();
        inputStreamReader.close();
        fileWriter.close();
        // 3、下载网络图片
        URL url2 = new URL("http://192.168.32.34:8080/hehe/截屏.jpg");
        InputStream inputStream2 = url2.openStream();
        FileOutputStream fileOutputStream = new FileOutputStream("url.jpg");
        byte[] bytes = new byte[1024];
        while (true) {
            int read = inputStream2.read(bytes);
            if (read == -1) {
                break;
            }
            fileOutputStream.write(bytes, 0, read);
        }
        System.out.println("复制完成");
        inputStream2.close();
        fileOutputStream.close();
        // 4、向服务器发送和接收数据
        URL url3 = new URL("http://192.168.32.34:8080/docs/denglu");
        URLConnection conn = url3.openConnection(); // 通过URL对象的openConnection()来获取URLConnection
        conn.setDoOutput(true); // 设置属性，例如，设置该连接可以给服务器传数据
        OutputStream out = conn.getOutputStream();  // 可以给服务器发数据
        out.write("username=chai&password=123".getBytes());
        conn.connect(); //建立连接
        InputStream is = conn.getInputStream(); //接收服务器返回的结果
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        while (true) {
            String str = br.readLine();
            if (str == null) {
                break;
            }
            System.out.println(str);
        }
        br.close();
        isr.close();
        is.close();
    }
}
