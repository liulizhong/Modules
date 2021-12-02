package 算法java;

import java.util.Scanner;

public class 队列_单向队列 {
    public static void main(String[] args) {
        CustomerQueue queue = new CustomerQueue();
        while (true){
            Scanner scanner = new Scanner(System.in);
            System.out.println("1.输入1 表示添加数据到队列");
            System.out.println("2.输入2 表示从队列里获取数据");
            System.out.println("3.输入3 表示显示队列");
            int key = scanner.nextInt();
            switch (key){
                case 1:{
                    System.out.println("请输入一个数字添加到队列里");
                    queue.addQueue(scanner.nextInt());
                }
                break;
                case 2:{
                    int value = queue.getQueue();
                    System.out.println("取出来的值为:" + value );
                }
                break;
                case 3:{
                    queue.showQueue();
                }
                break;
            }
        }

    }

}

class CustomerQueue{
    int maxSize;
    int[] array;
    int front;
    int rear;
    {
        this.maxSize = 5;
        this.array = new int[5];
        this.front = -1;
        this.rear = -1;
    }
    //添加值
    public void addQueue(int value){
        if (this.rear == this.maxSize -1){
            System.out.println("队列已经满了");
            return;
        }
        this.rear++;
        this.array[this.rear] = value;
    }
    //取值
    public int getQueue(){
        if (this.rear == this.front){
            System.out.println("队列已经空了");
            return -1;

        }
        this.front++;
        int value = this.array[this.front];
        return value;
    }
    //展示
    public void showQueue(){
        for (int i = this.front + 1;i<= this.rear;i++){
            System.out.println(array[i]);
        }
    }

    /*
    由此可以看出 单向队列 不能充分利用数组的空间,要想充分利用数组的空间需要使用环形队列
    */

}


