package 网络编程.TCP编程__多人聊天;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/*

 */
public class 客户端 {
    public static void main(String[] args)throws Exception {
        //(1)连接服务器
        Socket socket = new Socket("172.16.192.128",6666);
        System.out.println("连接成功");

        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

        //(2)输入用户名和密码，做登录
        //如果失败，就重新输入用户名和密码，再次登录
        Scanner input = new Scanner(System.in);
        String username;
        while(true){
            System.out.print("用户名：");
            username = input.nextLine();

            System.out.print("密码：");
            String password = input.nextLine();

            //给服务器发
            Message msg = new Message(username, password, Code.LOGIN);
            oos.writeObject(msg);//给服务发送的是一个消息对象

            //接收服务器返回的结果
            Message result = (Message) ois.readObject();
            if(result.getCode() == Code.SUCCESS){
                System.out.println("登录成功！");
                break;
            }else if(result.getCode() == Code.FAIL){
                System.out.println("登录失败，请重新登录！");
            }
        }

        //如果成功，就可以聊天（1发送（2接收
        //(3)启动一个发送的线程
        //(4)启动一个接收的线程
        SendThread st = new SendThread(username, oos);
        st.start();

        ReceiveThread rt = new ReceiveThread(ois);
        rt.start();

        //发送线程.join()
        st.join();
        //当发送线程结束后，要使得接收线程也接收
        rt.setFlag(false);
        //接收线程.join()
        rt.join();

        //(5)断开连接
        socket.close();
        input.close();
    }
}
//发送消息
class SendThread extends Thread{
    private ObjectOutputStream oos;
    private String username;

    public SendThread(String username,ObjectOutputStream oos) {
        this.username = username;
        this.oos = oos;
    }

    public void run(){
        Scanner input = new Scanner(System.in);
        try {
            //从键盘不断输入消息内容，给服务器发送
            while(true){
                System.out.print("输入消息内容：");
                String content = input.nextLine();

                //假设：输入bye，表退出
                if("bye".equals(content)){
                    Message msg = new Message(username, content, Code.LOGOUT);
                    oos.writeObject(msg);
                    break;
                }else{
                    Message msg = new Message(username, content, Code.CHAT);
                    oos.writeObject(msg);
                }
            }
        } catch (IOException e) {
            System.out.println("网络异常，请重新登录！");
        }finally{
            input.close();
        }
    }
}

class ReceiveThread extends Thread{
    private ObjectInputStream ois ;
    private volatile boolean flag = true;

    public ReceiveThread(ObjectInputStream ois) {
        super();
        this.ois = ois;
    }

    public void run(){
        try {
            while(flag){
                Message msg = (Message) ois.readObject();
                System.out.println(msg.getUsername() + "说:" + msg.getContent());
            }
        } catch (Exception e) {
            System.out.println("网络异常，请重新登录！");
        }
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

}