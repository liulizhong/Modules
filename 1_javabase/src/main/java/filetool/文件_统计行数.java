package filetool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
    统计window上文件总行数
 */
public class 文件_统计行数 {
    public static void main(String[] args) throws IOException {
        // 1、简单计算一个文件的总行数
        System.out.println(Files.lines(Paths.get("C:\\tmp\\oneopcall.txt")).count());
        int i = "abc,df,v,s,e".split(",").length;
/*      // 2、计算一个文件夹下所有文件的总行数
        String[] src = {"C:\\tmp\\result"};
        Long count = 0L;
        for (String arg : src) {
            File file = new File(arg);
            File[] files = file.listFiles();
            for (File file1 : files) {
                count += countFileLines(file1.getAbsolutePath());
            }
        }
        System.out.println("count:" + count);
*/
    }
    public static Long countFileLines(String dir) throws IOException {
        long count = Files.lines(Paths.get(dir)).count();

        return count;
    }
}
