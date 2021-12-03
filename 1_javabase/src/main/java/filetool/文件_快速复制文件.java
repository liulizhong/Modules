package filetool;

import java.io.*;

/*
    windows间文件复制
 */
public class 文件_快速复制文件 {
    public static void main(String[] args) throws Exception {
//        if (args.length != 1 || args[0].split("-").length != 3 || !args[0].split("-")[0].startsWith("202")) {
//            throw new Exception("请输入要同步的数据日期，或日期格式不对！！！！！！！！！！！");
//        }
//        String date = args[0];
        String date = "2020-06-10";
        String src = "D:\\tmp\\hive\\data.txt";
        String dec_event = "D:\\tmp\\hive\\event_log.txt";
        String dec_startup = "D:\\tmp\\hive\\startup_log.txt";

        // 测试方法 1
        copyFileIOToTwoFile(
                src,
                dec_event,
                dec_startup);  //用时5380

        // 测试方法 4
        File[] files = new File("C:\\tmp\\result\\").listFiles();
        for (File filesrc : files) {
            copyFileIOAddStr(filesrc.getAbsolutePath(), "C:\\tmp\\oneopcalltest.txt", filesrc.getName().split("_")[0]);
//            System.out.println(filesrc.getName().split("_")[0]);
        }

    }

    // 1、根据数据内容分流写入到不通过文件夹
    private static void copyFileIOToTwoFile(String src, String dec_event, String dec_startup) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(src));
        BufferedWriter eventWriter = new BufferedWriter(new FileWriter(dec_event, true));
        BufferedWriter startupWriter = new BufferedWriter(new FileWriter(dec_startup, true));
        int num = 0;
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                break;
            }
            if (readLine.contains("\"type\":\"event\"") && readLine.contains("\"type\":\"startup\"")) {
                System.out.println("数据有问题" + ++num);
            } else if (readLine.contains("\"type\":\"event\"")) {
                eventWriter.write(readLine + "\r\n");
            } else if (readLine.contains("\"type\":\"startup\"")) {
                startupWriter.write(readLine + "\r\n");
            } else {
                System.out.println("数据有问题" + ++num);
            }
        }
        eventWriter.close();
        startupWriter.close();
        bufferedReader.close();
    }

    // 2、普通复制文件
    private static void copyFileIO(String src, String dec) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(src);
        FileOutputStream fileOutputStream = new FileOutputStream(dec, true);
        int len;
        byte[] bytes = new byte[1024];
        while (true) {
            len = fileInputStream.read(bytes);
            if (len == -1) {
                break;
            }
            fileOutputStream.write(bytes);
        }
        fileOutputStream.close();
        fileInputStream.close();
    }

    // 3、高效复制文件
    private static void copyFileIOFast(String src, String dec) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(src));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(dec, true));
        int len;
        byte[] bytes = new byte[2048];
        while (true) {
            len = bufferedInputStream.read(bytes);
            if (len == -1) {
                break;
            }
            bufferedOutputStream.write(bytes);
        }
        bufferedOutputStream.close();
        bufferedInputStream.close();
    }

    // 4、复制文件，并添加指定字符串
    private static void copyFileIOAddStr(String src, String dec, String str) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(src));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dec, true));
        int num = 0;
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                break;
            }
            bufferedWriter.write(str + "," + readLine + "\r\n");
        }
        bufferedWriter.close();
        bufferedReader.close();
    }
}
