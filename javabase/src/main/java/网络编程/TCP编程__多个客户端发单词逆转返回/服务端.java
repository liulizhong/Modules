package 网络编程.TCP编程__多个客户端发单词逆转返回;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/*

 */
public class 服务端 {
    public static void main(String[] args)throws Exception {
        //一会服务器监听端口号是8888
        //服务器在192.168.34.53
        ServerSocket server = new ServerSocket(8888);

        while(true){
            //(2)等待连接
            //这句代码执行一次，意味着一个连接
            Socket accept = server.accept();
            System.out.println(accept.getInetAddress().getHostAddress() +"连接成功了");
            ClientMessageThread ct = new ClientMessageThread(accept);
            ct.start();
        }

//		server.close();//不写，意味着服务器一直开着

    }
}

class ClientMessageThread extends Thread{
    private Socket accept;

    public ClientMessageThread(Socket accept) {
        super();
        this.accept = accept;
    }

    public void run(){
        try {
            InputStream is = accept.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);//把字节流转成字符流
            BufferedReader br = new BufferedReader(isr);

            OutputStream os = accept.getOutputStream();
            PrintStream ps = new PrintStream(os);//用它的目的，就是可以行打印输出

            while(true){
                //接收客户端的词语
                String word = br.readLine();
                if(word == null){
                    break;
                }

                //反转
                StringBuilder s = new StringBuilder(word);
                s.reverse();

                //给客户端返回去
                ps.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                accept.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}