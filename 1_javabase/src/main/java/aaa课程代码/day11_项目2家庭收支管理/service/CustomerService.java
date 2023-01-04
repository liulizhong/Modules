package aaa课程代码.day11_项目2家庭收支管理.service;

import aaa课程代码.day11_项目2家庭收支管理.bean.Customer;

import java.util.Arrays;

/**
 * 这个类目前充当两层的任务：业务管理、数据访问
 * 1、存储和管理数据：通过数组
 * 2、业务逻辑的管理：方法
 */
public class CustomerService {
    //1、声明一个数组，用来存储客户对象
    //private Customer[] all = new Customer[5];
    private Customer[] all;
    private int total;

    public CustomerService(int length) {
        all = new Customer[length];
    }

    //2、声明各种方法，实现业务逻辑
    //（1）功能：负责接收一个客户对象，存储到all数组中
    public boolean addCustomer(Customer c) {
        if (total == all.length) {//数组是否已满
            //(1)提示错误
            //System.out.println("数组已满");
            //return false;//提前结束方法

            //(2)把all扩容
            all = Arrays.copyOf(all, all.length * 2);
        }

        //第一次调用all[0] = c,然后total++ 变成1
        //第二次调用all[1] = c,然后total++ 变成2
        all[total++] = c;
        return true;
    }


    //（2）功能：查询所有的客户对象
    public Customer[] getAll() {
        //return all;//不够友好，里面包含null

        //没有添加是total是0
        //添加了一个后，total=1
        //参数一：all表示源数组
        //参数二：新数组的长度
        return Arrays.copyOf(all, total);
    }

    //(3)功能：根据客户编号查询客户对象
    public Customer getById(int id) {
        //1、判断id的合法性
        //添加完一个客户时，total=1
        if (id <= 0 || id > total) {
            System.out.println("编号为" + id + "的客户不存在");
            return null;
        }
        //2、返回有效对象
        return all[id - 1];
    }

    //(4)功能：替换某个客户
    public boolean replace(int id, Customer newCustomer) {
        //1、判断id的合法性
        if (id <= 0 || id > total) {
            System.out.println("编号为" + id + "的客户不存在");
            return false;
        }
        all[id - 1] = newCustomer;
        return true;
    }

    //(5)功能：根据id删除客户
    public boolean deleteById(int id) {
        //1、判断id的合法性
        if (id <= 0 || id > total) {
            System.out.println("编号为" + id + "的客户不存在");
            return false;
        }
        int index = id - 1;//被删除的元素的位置

        //2、删除客户
        //(1)先把要删除的对象右边的元素往左移动
        /*
         * 第一个：源数组
         * 第二个：原数组的起始位置
         * 第三个：目标数组（可以和源数组是同一个数组，那么就变成了移动，如果目标数组和源数组不是同一个，那么就是复制元素）
         * 第四个：目标数组的起始位置
         * 第五个：一共要移动/复制几个元素
         */
        System.arraycopy(all, index + 1, all, index, total - index - 1);


        //(2)把最后一个位置变为null
        //(3)人数减少一个
        all[--total] = null;
        //现在total=3，里面有三个人，要删除一个人，要删除的id=1，index=0
        //移动[1]->[0],[2]->[1]
        //all[2]=null
        return true;
    }
}
