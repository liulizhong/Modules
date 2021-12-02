package 网络编程.TCP编程__多个客户端发单词逆转返回;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/*

 */
public class 客户端 {
    public static void main(String[] args) throws Exception{
        //(1)连接服务器
        Socket socket = new Socket("172.16.192.128",8888);

        Scanner input = new Scanner(System.in);
        OutputStream os = socket.getOutputStream();
        PrintStream ps = new PrintStream(os);//用它的目的，就是可以行打印输出

        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);//把字节流转成字符流
        BufferedReader br = new BufferedReader(isr);

        //(2)循环从键盘输入
        while(true){
            System.out.print("词语：");
            String word = input.nextLine();

            if("stop".equals(word)){
                break;
            }

            //（3）给服务器打印过去
            ps.println(word);

            //(4)接收返回的消息
            String returnWord = br.readLine();
            System.out.println("服务器返回的词语：" + returnWord);
        }

        //（5）关闭
        socket.close();
    }
}