package 算法java;


class HeroTestNode{
    int num;
    String name;
    HeroTestNode left;
    HeroTestNode right;
}
public class 二叉树_遍历 {
    public static void main(String[] args) {
        HeroTestNode root = new HeroTestNode();
        root.num = 1;
        root.name = "宋江";

        HeroTestNode left1Node = new HeroTestNode();
        left1Node.num = 2;
        left1Node.name = "吴用";

        HeroTestNode right1Node = new HeroTestNode();
        right1Node.num = 3;
        right1Node.name = "卢俊义";
        root.left = left1Node;
        root.right = right1Node;

        HeroTestNode right2Node = new HeroTestNode();
        right2Node.num = 4;
        right2Node.name = "林冲";
        right1Node.right = right2Node;
        preOrder(root);

    }
    //前序遍历：先输出root结点，然后在输出左边的结点，完后在输出右边的结点

    public static void preOrder(HeroTestNode node){
        if (node !=null){
            System.out.println("num:"+ node.num+"name:"+node.name);
            preOrder(node.left);
            preOrder(node.right);
        }
    }

    //中序遍历：先输出左边结点，然后在输出root的结点，完后在输出右边的结点



    //后序遍历：先输出左边结点，然后在输出右边的结点，完后在输出左边的结点
}

