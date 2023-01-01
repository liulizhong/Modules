package aaa课程代码.day06_数组;


public class 数组练习7_复制 {
    public static void main(String[] args) {
        // 1、数组的赋值 - 直接复制-去空值，且增加一个新name，放到index=1的地方，其他name延后一位
        String[] names = {"张三",null,"李四","王五",null};//本来数组中5个人，走了两个，即有两个元素是空的
        int count =0;
        for (int i = 0; i < names.length; i++) {
            if (names[i] != null) {
                count++;
            }
        }
        String[] nameNews = new String[count+1];
        int len = -1;
        for (String name : names) {
            if (name != null) {
                nameNews[++len]=name;
            }
        }
        for (String nameNew : nameNews) {
            System.out.println(nameNew);
        }
        System.out.println("--------------------------------------------------------------");
        //(3)增加新的人
        //假设增加在[1]位置
        int index = 1;//插入的位置
        //循环的次数：移动的元素的个数
        for(int i = nameNews.length-2; i>=index; i--){
            //后面的元素 = 前面的元素
            nameNews[i+1] = nameNews[i];
        }
        nameNews[index] = "赵六";
        for (String nameNew : nameNews) {
            System.out.println(nameNew);
        }
    }
}
