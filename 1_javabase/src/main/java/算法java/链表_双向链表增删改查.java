package 算法java;

/*
    双向链表的增删改查
*/
public class 链表_双向链表增删改查 {

    public static void main(String[] args) {
        HeroNode0 head = new HeroNode0();
        HeroNode0 newHeroNode = new HeroNode0();
        newHeroNode.name = "宋江";
        newHeroNode.num = 1;
        newHeroNode.nickName = "及时雨";

        HeroNode0 newHeroNode1 = new HeroNode0();
        newHeroNode1.name = "卢俊义";
        newHeroNode1.num = 2;
        newHeroNode1.nickName = "玉麒麟";

        HeroNode0 newHeroNode2 = new HeroNode0();
        newHeroNode2.name = "jack";
        newHeroNode2.num = 4;
        newHeroNode2.nickName = "jack1";

        insertNode(head,newHeroNode);
        insertNode(head,newHeroNode1);
        System.out.println();
        insertNode(head,newHeroNode2);

        showLinkList(head);
        //能逆序打印出来 说明这就是一个双向链表
        showLinkList2(head);
//        deleteNode(head,2);
//        System.out.println();
//        showLinkList2(head);
    }

    //未经排序的 插入算法(不能满足开发的实际需求)
    public static void insertNode(HeroNode0 head,HeroNode0 newHeroNode){
        HeroNode0 temp = head;
        //遍历链表
        while (true){
            if (temp.next == null){
                break;
            }
            //移动temp指针
            temp = temp.next;
        }
        //将新节点 加入到链表尾部
        temp.next = newHeroNode;
        newHeroNode.pre = temp;
    }

    //支持排序的插入方式
    //这个也就是为什么LinkedHashMap采用了双向链表能实现有序的原因
    public static void insertNodeSort(HeroNode0 head,HeroNode0 newHeroNode){
        HeroNode0 temp = head;
        boolean flag = true;
        while (true){
            if (temp.next == null){
                break;
            }
            if (temp.next.num > newHeroNode.num){
                break;
            }else if(temp.next.num == newHeroNode.num){
                flag = false;
                break;
            }
            temp = temp.next;
        }
        if (!flag){
            System.out.println("节点已经存在....");
        }else {
            //这个顺序绝对不能反了(里面比较有内涵),动插入结点的线，连接完之后在改变已有结点的线
            newHeroNode.next = temp.next;
            //双向链表的关键,先把新结点前后的线确定好
            newHeroNode.pre = temp;
            //防止我添加的结点就是最后一个结点，这样的话temp.next==null就废掉了
            if (temp.next!=null){
                //改变已有结点的线
                temp.next.pre = newHeroNode;
                temp.next = newHeroNode;
            }


        }
    }

    public static void deleteNode(HeroNode0 head,int num){
        HeroNode0 temp = head;
        boolean flag = false;
        while (temp.next !=null){
            if (temp.next.num == num){
                flag = true;
                break;
            }
            temp = temp.next;
        }
        if (flag){
            temp.next = temp.next.next;
            if (temp.next !=null){
                temp.next.pre = temp;
            }
        }else {
            System.out.println("没有找到要删除的节点");
        }
    }

    public static void showLinkList(HeroNode0 head){
        HeroNode0 temp = head;
        if (temp.next == null){
            System.out.println("这个链表是空链表...");
            return;
        }

        do {
            System.out.print(temp.next.name + " " + temp.next.nickName + "->");
            temp = temp.next;
        }while (temp.next != null);
    }

    //逆序打印出链表，证明这是一个双向的链表
    public static void showLinkList2(HeroNode0 head){
        HeroNode0 temp = head;

        if (temp.next == null){
            System.out.println("这个链表是空链表...");
            return;
        }
        /*让temp结点定位到链表的最后*/
        while(true){
            if (temp.next == null){
                break;
            }
            temp = temp.next;
        }
        //倒序的往前走
        while (true){
            System.out.print(temp.name + " " + temp.nickName + "->");
            temp = temp.pre;
            //判断是不是已经到了链表的head结点
            if (temp.pre == head){
                System.out.print(temp.name + " " + temp.nickName + "->");
                break;
            }
        }
    }

}

class HeroNode0{
    int num;
    String name;
    String nickName;
    HeroNode0 next;
    HeroNode0 pre;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public HeroNode0 getNext() {
        return next;
    }

    public void setNext(HeroNode0 next) {
        this.next = next;
    }
}
