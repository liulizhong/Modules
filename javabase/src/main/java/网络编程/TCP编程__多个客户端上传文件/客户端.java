package 网络编程.TCP编程__多个客户端上传文件;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/*

 */
public class 客户端 {
    public static void main(String[] args) throws Exception {
        // (1)连接服务器
        Socket socket = new Socket("172.16.192.128", 8888);

        Scanner input = new Scanner(System.in);

        //(2)从键盘输入文件的路径和名称
        System.out.print("请择要上传的文件：");
        String path = input.nextLine();
        File file = new File(path);

        OutputStream out = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(out);//用它的目的是为了既可以单独传一个字符串，又可以写字节内容

        //先发送文件名（含后缀名）
        dos.writeUTF(file.getName());//单独发一个字符串

        //还需要一个IO流，从文件读取内容，给服务器发过去
        FileInputStream fis = new FileInputStream(file);

        //（3把文件内容给服务器传过去，类似与复制文件
        byte[] data = new byte[1024];
        while(true){
            int len = fis.read(data);
            if(len==-1){
                break;
            }
            dos.write(data, 0, len);
        }
        socket.shutdownOutput();//目的是让服务器读-1

        //(4)接收服务器的结果
        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);//把字节流转成字符流
        BufferedReader br = new BufferedReader(isr);
        String result = br.readLine();

        System.out.println(result);

        //(5)关闭
        fis.close();
        socket.close();
    }
}