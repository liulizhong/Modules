package hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author lizhong.liu
 * @version TODO
 * @class ??
 * @CalssName HdfsClient
 * @create 2020-06-05 10:18
 * @Des TODO
 */
public class HdfsClient {
    @Test
    public void testMkdirs() throws IOException, InterruptedException, URISyntaxException {

        // 1、获取文件系统
        Configuration configuration = new Configuration();
        // 可配置在集群上运行
        configuration.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());      //防止打包运行不报错
        // configuration.set("fs.defaultFS", "hdfs://hadoop102:8020");   //configuration中配置URL
        // System.setProperty("HADOOP_USER_NAME", "hdfs");               //configuration中配置用户名
        // FileSystem fs = FileSystem.get(configuration);
        FileSystem fs = FileSystem.get(new URI("hdfs://192.168.1.241:8020"), configuration, "root");

        // 2 创建目录
        fs.mkdirs(new Path("/tmp/test/haha"));

        // 3、上传文件
        fs.copyFromLocalFile(new Path("e:/banzhang.txt"), new Path("/banzhang.txt"));

        // 4、文件下载
        // boolean delSrc 指是否将原文件删除
        // Path src 指要下载的文件路径
        // Path dst 指将文件下载到的路径
        // boolean useRawLocalFileSystem 是否开启文件校验
        fs.copyToLocalFile(false, new Path("/banzhang.txt"), new Path("e:/banhua.txt"), true);

        // 5、文件夹删除
        fs.delete(new Path("/0508/"), true);

        // 6、文件名更改
        fs.rename(new Path("/banzhang.txt"), new Path("/banhua.txt"));

        // 7、查看文件详情
        RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);
        while (listFiles.hasNext()) {
            LocatedFileStatus status = listFiles.next();
            // (1)文件名称
            System.out.println(status.getPath().getName());
            // (2)长度
            System.out.println(status.getLen());
            // (3)权限
            System.out.println(status.getPermission());
            // (4)分组
            System.out.println(status.getGroup());
            // (5)获取存储的块信息
            BlockLocation[] blockLocations = status.getBlockLocations();
            for (BlockLocation blockLocation : blockLocations) {
                // 获取块存储的主机节点
                String[] hosts = blockLocation.getHosts();
                for (String host : hosts) {
                    System.out.println(host);
                }
            }
        }
        // 8、文件和文件夹的判断
        FileStatus[] listStatus = fs.listStatus(new Path("/"));
        for (FileStatus fileStatus : listStatus) {
            // 如果是文件
            if (fileStatus.isFile()) {
                System.out.println("f:" + fileStatus.getPath().getName());
            } else {
                System.out.println("d:" + fileStatus.getPath().getName());
            }
        }

        // 9、文件上传的IO流
        //     (1) 创建输入流
        FileInputStream ipFis = new FileInputStream(new File("\\\\10.238.255.189\\liulizhong\\opcdata\\2020-06\\2020-06-08.txt"));
        //     (2.1)获取输出流
        FSDataOutputStream upFos = fs.create(new Path("/opc/2020-06/2020-06-08.txt"), false);  //下边方法加了是否存在的判断。
/*
        //     (2.2)增加判断文件是否存在，创建后再写入，更安全
        Path newPath = new Path("/opc/2020-06/2020-06-08.txt");
        FSDataOutputStream upFos = null;
        if (!fs.exists(newPath)) {
            upFos = fs.create(newPath, false);
        } else {
            //存在就追加
            upFos = fs.append(newPath);
        }
*/
        //     (2.2)缓冲IO流 [似乎可有可无，有的话把copyBytes前两个参数替换掉就ok了]
        //BufferedInputStream bufferedInputStream = new BufferedInputStream(ipFis);
        //BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(upFos);
        //     (3)流对拷
        IOUtils.copyBytes(ipFis, upFos, configuration);
        //     (3.2) 不用流对拷，可以直接写入数据，并写入后刷新同步
        //upFos.write("也可以写入String的getBytes\r\n".getBytes());
        //upFos.hsync();
        //     (4)关闭资源
        IOUtils.closeStream(upFos);
        IOUtils.closeStream(ipFis);

        // 10、文件下载的IO流
        //     (1)获取输入流
        FSDataInputStream downFis = fs.open(new Path("/banhua.txt"));
        //     (2)获取输出流
        FileOutputStream downFos = new FileOutputStream(new File("e:/banhua.txt"));
        //     (3)流的对拷
        IOUtils.copyBytes(downFis, downFos, configuration);
        //     (4)关闭资源
        IOUtils.closeStream(downFos);
        IOUtils.closeStream(downFis);

        // 10、关闭资源
        fs.close();
    }
}