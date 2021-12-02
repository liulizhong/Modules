package 网络编程.TCP编程__多人聊天;
import java.io.Serializable;

//因为要在网络中传输对象，即输入输出对象，所以这个类型要实现序列化接口
public class Message implements Serializable{
    private static final long serialVersionUID = 1L;
    private String username;//谁发的
    private String content;//消息内容
    private int code;//状态码，用于标识该消息是什么类型的消息，是登录、聊天、退出....
    public Message(String username, String content, int code) {
        super();
        this.username = username;
        this.content = content;
        this.code = code;
    }
    public Message() {
        super();
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
}