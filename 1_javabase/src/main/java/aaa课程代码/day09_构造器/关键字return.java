package aaa课程代码.day09_构造器;


public class 关键字return {
    public static void main(String[] args){
        for(int i=0; i<5; i++){
			/*
			当i=0; j=0  break
			当i=1; j=0,1  break
			...
			*/
            for(int j=0; j<5; j++){
                System.out.print("#");
                if(i == j){
                    //break;
                    //continue;
                    return ;  // 第一遍循环便直接if判断为true，执行return结束了main方法【跳出了循环并且结束了方法的运行】
                }
                System.out.print("&");
            }
            System.out.println();
        }
    }
}