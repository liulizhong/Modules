package filetool;

import com.jcraft.jsch.*;

import java.io.*;

/*
    Windows上的代码：把本地文件上传到linux的文件系统
 */
public class 文件_从windows到Linux复制 {
    public static void main(String[] args) throws JSchException, IOException, SftpException {
        String src_file_198_file = "\\\\10.238.255.198\\liulizhong\\opcdata\\2020-06\\2020-06-06.txt";    // Windows文件
        String dec_file_241_path = "/home/liulizhong/opcdata/2020-06/";        // Linux上父目录
        String dec_file_241_file = dec_file_241_path + "2020-06-06.txt";          // Linux上文件全目录
        // 1、Linux服务器连接
        JSch jsch = new JSch();
        Session session = jsch.getSession("root", "192.168.1.241", 22);
        // 2、设置登陆主机的密码
        session.setPassword("abc@123@!@#");// 设置密码
        // 设置第一次登陆的时候提示，可选值：(ask | yes | no)
        session.setConfig("StrictHostKeyChecking", "no");
        // 设置登陆超时时间
        session.connect(300000);
        Channel channel = (Channel) session.openChannel("sftp");
        channel.connect(10000000);
        ChannelSftp sftp = (ChannelSftp) channel;

        // 3、创建缓冲输入流
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(src_file_198_file));
        // 4、判断父文件目录是否存在，不存在即创建
        SftpATTRS attrs = null;
        try {
            attrs = sftp.stat(dec_file_241_path);
        } catch (Exception e) {
            // TODO: handle exception
        }
        if (attrs == null) {
            sftp.mkdir(dec_file_241_path);
        }

        // 5、进入父目录操作
        sftp.cd(dec_file_241_path);
        // 6、创建输出流
        File decFile = new File(dec_file_241_file);
        // 6.将文件输出流进行缓冲(提升复制速度)
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(sftp.put(decFile.getName(), ChannelSftp.APPEND));
        // 7、递归循环复制文件到Linux
        int len;
        byte[] bytes = new byte[4096];
        while (true) {
            len = bufferedInputStream.read(bytes);
            if (len == -1) {
                break;
            }
            bufferedOutputStream.write(bytes);
        }
    }
}
