package 算法java;

import java.util.Scanner;

/*
//使用数组模拟环形队列
关键点 就在于
1、(rear+1)%maxsize == head（队列列满了) rear == head(队列是空的)

2、统计队列有多少个元素(rear + maxsize - head)%maxsize 因为 理论上rear是比head要大的

举例来说(rear(1) + maxsize(5) - 0) % 5 = 1
 */
public class 环形队列_数组模拟 {
    public static void main(String[] args) {
        RingQueue queue = new RingQueue();
        while (true){
            Scanner scanner = new Scanner(System.in);
            System.out.println("1.输入a 表示添加数据到队列");
            System.out.println("2.输入p 表示从队列里获取数据");
            System.out.println("3.输入s 表示显示队列");
            String str = scanner.nextLine();
            char[] chars = str.toCharArray();
            char key = chars[0];
            switch (key){
                case 'a':{
                    System.out.println("请输入一个数字添加到队列里");
                    queue.pushQueue(scanner.nextInt());
                }
                break;
                case 'p':{
                    int value = queue.popQueue();
                    System.out.println("取出来的值为:" + value );
                }
                break;
                case 's':{
                    queue.showRingQueue();
                }
                break;
            }
        }
    }
}

class RingQueue {
    int maxSize;
    int[] array;
    int front;
    int rear;

    {
        this.maxSize = 5;
        this.array = new int[5];
        this.front = 0;
        this.rear = 0;
    }

    //添加值
    public void pushQueue(int value) {
        if (this.isFullQueue()){
            System.out.println("队列已经满了");
            return;
        }
        this.array[this.rear] = value;
        this.rear = (this.rear +1)%this.maxSize;
    }

    //取值
    public int popQueue() {
        if(this.isEmpty()){
            System.out.println("队列是空的");
            return 0;
        }
        int value = this.array[this.front];
        this.front = (this.front + 1) % maxSize;
        return value;
    }

    //展示
    public void showRingQueue() {
        int size = this.size();
        if (size == 0){
            System.out.println("队列是空的");
        }

        int tempFront = this.front;
        for(int i = 0;i<size;i++){
            System.out.println(array[tempFront]);
            tempFront = (tempFront + 1) % maxSize;
        }
    }

    //判断队列是否已经满了

    public Boolean isFullQueue(){
        return (this.rear + 1) % maxSize  == this.front;
    }

    //判断队列是不是空的

    public Boolean isEmpty(){
        return (this.rear == this.front);
    }

    //判断队列有多少个元素

    public int size(){
        return (this.rear + this.maxSize - this.front) % maxSize;

    }
}
