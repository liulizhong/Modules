package 网络编程.TCP编程__多个客户端上传文件;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class 服务端 {
    public static void main(String[] args) throws Exception{
        //一会服务器监听端口号是8888
        //服务器在192.168.34.53
        ServerSocket server = new ServerSocket(8888);

        while(true){
            //(2)等待连接
            //这句代码执行一次，意味着一个连接
            Socket accept = server.accept();
            System.out.println(accept.getInetAddress().getHostAddress() +"连接成功了");

            FileUploadThread ft = new FileUploadThread(accept);
            ft.start();
        }

    }
}
class FileUploadThread extends Thread{
    private Socket socket;
    private String dir = "upload/";

    public FileUploadThread(Socket socket) {
        super();
        this.socket = socket;
    }

    public void run(){
        FileOutputStream fos = null;
        try {
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            //读取文件名（含后缀名）
            String filename = dis.readUTF();

            String ext = filename.substring(filename.lastIndexOf("."));
            Date now = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            filename = sf.format(now);

            File file = new File(dir + filename + ext);
            fos = new FileOutputStream(file);

            //结束文件内容
            byte[] data = new byte[1024];
            while(true){
                int len = is.read(data);
                if(len==-1){
                    break;
                }
                //写入到服务器的某个文件中
                fos.write(data, 0, len);
            }

            //返回结果
            OutputStream out = socket.getOutputStream();
            PrintStream ps = new PrintStream(out);
            ps.println(filename + ext + "上传完毕");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                fos.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}