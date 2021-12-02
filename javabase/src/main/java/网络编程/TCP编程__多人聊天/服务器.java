package 网络编程.TCP编程__多人聊天;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/*
 用一个map模拟数据库
 key:用户名
 value:密码

 1、服务器要处理的事情件：
    （1）接收连接
    （2）接收用户名和密码，验证，告诉客户端是否成功
    （3）接收客户端的消息，给所“在线”客户端转发

 2、问题？
（1）如何区别是登录还是聊天
 */
public class 服务器 {
    public static void main(String[] args) throws Exception{
        //(1)启动服务器，在某个端口监听
        ServerSocket server = new ServerSocket(6666);

        //(2)不断的接收客户端的连接
        //每一个客户端要有自己的Socket，线程来处理
        while(true){
            Socket accept = server.accept();
            System.out.println("一个客户端连接进来了");

            //启动一个线程处理它
            ClientMessageThread ct = new ClientMessageThread(accept);
            ct.start();
        }
    }
}

class ClientMessageThread extends Thread{
    private static Properties allUsers = new Properties();
    private static Set<ObjectOutputStream> online = Collections.synchronizedSet(new HashSet<ObjectOutputStream>());//所在线的客户端的输出流通道

    static{
        allUsers.put("chai", "123");
        allUsers.put("zhangchao", "250");
        allUsers.put("wanba", "666");
    }

    private Socket socket;
    private ObjectOutputStream oos;

    public ClientMessageThread(Socket socket) {
        super();
        this.socket = socket;
    }

    public void run(){
        Message msg = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            while(true){
                //(1)先接收消息对象
                msg  = (Message) ois.readObject();
                //判断该消息对象是“登录”、“退出”、“聊天”，根据它的code值
                //如果是“登录”，取出用户名和密码进行验证，并返回结果，如果成功，告知其他客户端，xxx上线了
                //如果是“退出”，告知其他客户端，xx下线了，并断开连接
                //如果是“聊天”，给其他客户端转发消息对象
                if(msg.getCode() == Code.LOGIN){
                    String user = msg.getUsername();
                    String pwd = msg.getContent();

                    //验证
                    if(allUsers.getProperty(user) == null || !allUsers.getProperty(user).equals(pwd)){
                        //用户名或密码错误，登录失败
                        //给客户端返回结果
                        Message m = new Message();
                        m.setCode(Code.FAIL);
                        oos.writeObject(m);
                    }else{
                        //登录成功
                        //给客户端返回结果
                        Message m = new Message();
                        m.setCode(Code.SUCCESS);
                        oos.writeObject(m);

                        online.add(oos);//添加到在线客户端集合中

                        Message m2 = new Message(user, "上线了", Code.CHAT);
                        //告知其他客户端，xxx上线了
                        sendToOther(m2);
                    }

                }else if(msg.getCode() == Code.LOGOUT){
                    //告知其他客户端，xxx下线了
                    Message m2 = new Message(msg.getUsername(), "下线了", Code.CHAT);
                    sendToOther(m2);
                    break;
                }else if(msg.getCode() == Code.CHAT){
                    //给其他客户端转发消息对象
                    sendToOther(msg);
                }
            }
        } catch (Exception e) {
            //我这里异常，说明当前客户端掉线了
            Message m2 = new Message(msg.getUsername(), "掉线了", Code.CHAT);
            sendToOther(m2);
        }finally{
            //当前客户端结束了，要从online中依次
            online.remove(oos);
        }
    }

    private void sendToOther(Message msg) {
        ArrayList<ObjectOutputStream> offline = new ArrayList<ObjectOutputStream>();

        for (ObjectOutputStream on : online) {
            try {
                if(!on.equals(oos)){//给其他客户端转发，自己不需要
                    on.writeObject(msg);
                }
            } catch (IOException e) {
                //如果异常，说明on这个客户端的输出流问题了，它可能掉线了
                offline.add(on);
            }
        }

        //再次统一处理掉线的
        for (ObjectOutputStream off : offline) {
            online.remove(off);
        }
    }
}