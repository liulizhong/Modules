package 算法java;

public class 链表_单向链表 {

    public static void main(String[] args) {
        HeroNode head = new HeroNode();
        HeroNode newHeroNode = new HeroNode();
        newHeroNode.name = "宋江";
        newHeroNode.num = 1;
        newHeroNode.nickName = "及时雨";

        HeroNode newHeroNode1 = new HeroNode();
        newHeroNode1.name = "卢俊义";
        newHeroNode1.num = 2;
        newHeroNode1.nickName = "玉麒麟";

        HeroNode newHeroNode2 = new HeroNode();
        newHeroNode2.name = "jack";
        newHeroNode2.num = 4;
        newHeroNode2.nickName = "jack1";

        insertNodeSort(head,newHeroNode2);
        insertNodeSort(head,newHeroNode);
        insertNodeSort(head,newHeroNode1);

        showLinkList(head);
        deleteNode(head,2);
        System.out.println();
        showLinkList(head);
    }

    //未经排序的 插入算法(不能满足开发的实际需求)
    public static void insertNode(HeroNode head,HeroNode newHeroNode){
        HeroNode temp = head;
        //遍历链表
        while (temp.next != null){
            //移动temp指针
            temp = temp.next;
        }
        //将新节点 加入到链表尾部
        temp.next = newHeroNode;
    }

    //支持排序的插入方式

    public static void insertNodeSort(HeroNode head,HeroNode newHeroNode){
        HeroNode temp = head;
        boolean flag = true;
        while (temp.next !=null){
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
            //这个顺序绝对不能反了(里面比较有内涵)
            newHeroNode.next = temp.next;
            temp.next = newHeroNode;
        }
    }
    
    public static void deleteNode(HeroNode head,int num){
        HeroNode temp = head;
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
        }else {
            System.out.println("没有找到要删除的节点");
        }
    }

    public static void showLinkList(HeroNode head){
        HeroNode temp = head;
        if (temp.next == null){
            System.out.println("这个链表是空链表...");
            return;
        }

        do {
            System.out.print(temp.next.name + " " + temp.next.nickName + "->");
            temp = temp.next;
        }while (temp.next != null);
    }

}

class HeroNode{
    int num;
    String name;
    String nickName;
    HeroNode next;

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

    public HeroNode getNext() {
        return next;
    }

    public void setNext(HeroNode next) {
        this.next = next;
    }
}
