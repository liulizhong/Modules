package 算法java;


class CatNode{
    int no;
    String name;
    CatNode next;
}

public class 链表_单向环形链表 {
    public static void main(String[] args) {

        CatNode head = new CatNode();
        CatNode node1 = new CatNode();
        node1.name = "tom";
        node1.no = 1;
        ;
        CatNode node2 = new CatNode();
        node2.name = "tom2";
        node2.no = 2;
        ;
        CatNode node3 = new CatNode();
        node3.name = "tom3";
        node3.no = 3;

        insertNode(head,node1);
        insertNode(head,node2);
        insertNode(head,node3);
        showSingleCircleLink(head);
        head = deleteNode(head,1);
        showSingleCircleLink(head);
    }

    public static void insertNode(CatNode head, CatNode newNode) {
        if (head.next == null) {
            head.no = newNode.no;
            head.name = newNode.name;
            head.next = head;
            return;
        }
        CatNode temp = head;
        //找到链表最后一个节点
        while (true){
            if (temp.next == head){
                break;
            }
            temp = temp.next;
        }
        //然后将新节点加入到环形链表中
        temp.next = newNode;
        newNode.next = head;
    }

    public static void showSingleCircleLink(CatNode head) {
        System.out.println("环形链表情况如下");
        CatNode temp = head;
        if (temp.next == null){
            System.out.println("这是一个空链表");
            return;
        }
        //最后一个节点就是temp.next == head
        while (true){
            System.out.print("id=" + temp.no + "," + "name="+ temp.name + " ->");
            if (temp.next == head){
                break;
            }
            temp = temp.next;
        }
    }

    /*
        1、思路如下:
        2、创建一个temp节点指向head
        3、创建一个helper节点指向last
        4、让传入的id 和 temp.no比较,如果相同，用helper删除
        5、这里非常重要的一个逻辑就是如果删除的是头节点怎么办
    */

    //之所以要返回CatNode 就是因为，我们可能会把头节点删除掉,需要返回一个新的头节点
    public static CatNode deleteNode(CatNode head,int num){
        CatNode temp = head;
        CatNode helper = head;

        //如果一上来就是空链表的判断
        if (temp.next == null){
            System.out.println("这是一个空链表");
            return head;
        }
        //只有一个节点的情况
        if (temp.next == head){
            temp.next = null;
            return head;
        }

        //将helper定位到链表的最后一个节点
        do {
            helper = helper.next;
        }
        while (helper.next != head);

        boolean flag = true;
        //多个节点的情况
        do {
            //找到了要删除的节点
            if (temp.no == num){
                //如果这个节点是头节点的话
                if (temp == head){
                    head = head.next;//返回一个新的头节点
                }
                helper.next = temp.next;
                System.out.println("catid=:" + num);
                flag = false;
                break;
            }
            temp = temp.next;
            helper = helper.next;
        }while (temp.next !=head);

        //加flag 防止最后一个节点没删掉的情况
     if (flag){
            if (temp.no == num){
                helper.next = temp.next;
                System.out.println("catid=:" + num);
            }else {
                System.out.println("对不起，没有id相同的cat");
            }
        }
        return head;
    }
}
