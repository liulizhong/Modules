package filetool;

import org.junit.jupiter.api.Test;

import java.io.File;

/*
    递归修改文件夹下文件的名字(正则匹配)
 */
public class 文件_递归修改名字 {
    @Test
    public void reNameFile() throws Exception {
        reNameFiles(
                new File("D:\\1_java课程内容\\bizdata_module\\21、实时项目(13day)\\"),
                "__",
                "_");
//        reNameFiles(new File("D:\\studyware\\bizdata_module\\10、李海波_Spark_JVM_Thread\\1、哔站大数据技术之spark基础\\4.视频\\00 - Spark之前视频排版补充"),
//                "00");
    }

    /**
     * 方法一： 文件夹下递归改文件名：将一个字符串修改为另一个字符串
     *
     * @param file
     * @param regex       正则匹配内容
     * @param replacement 更改后字符串
     * @throws Exception
     */
    public static void reNameFiles(File file, String regex, String replacement) throws Exception {
        if (!file.exists()) {
            throw new Exception("文件夹不存在");
        }
        if (file.isDirectory()) {
            //改目录名
            file.renameTo(new File(file.getCanonicalPath().replaceAll(regex, replacement)));
            File[] files = file.listFiles();
            if (!file.exists() || null == file || files.length == 0) {
            } else {
                for (File newFile : files) {
                    reNameFiles(newFile, regex, replacement);
                }
            }
        } else if (file.isFile()) {
            //该文件名
            file.renameTo(new File(file.getCanonicalPath().replaceAll(regex, replacement)));
        }
    }

    /**
     * 方法二： 文件夹下递归改文件的名：前边加指定字符串
     *
     * @param file
     * @throws Exception
     */
    public static void reNameFiles(File file, String str) throws Exception {
        if (!file.exists()) {
            throw new Exception("文件夹不存在");
        }
        if (file.isDirectory()) {
            //改目录名
//            file.renameTo(new File("00" + file.getCanonicalPath()));
//            file.renameTo(new File(file.getParent() + "\\" + str + file.getName()));
//            System.out.println(file.getParent() + "\\00" + file.getName());
            File[] files = file.listFiles();
            if (!file.exists() || null == file || files.length == 0) {
            } else {
                for (File newFile : files) {
                    reNameFiles(newFile, str);
                }
            }
        } else if (file.isFile()) {
            //该文件名
            file.renameTo(new File(file.getParent() + "\\" + str + file.getName()));
            System.out.println(file.getParent() + "\\" + str + file.getName());
        }
    }
}