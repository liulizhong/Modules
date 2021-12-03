package 算法java;


import java.util.ArrayList;
import java.util.List;

public class 稀疏数组 {
    public static void main(String[] args) {
        int[][] array = new int[11][11];
        array[1][2] = 1;
        array[7][9] = 6;
        System.out.println("原始数组如下:");
        for (int i = 0;i<array.length;i++ ){
            for (int j = 0;j<array[i].length;j++){
                System.out.print(array[i][j]+ " ");
            }
            System.out.println();
        }

        arrayToSparse(array);

    }

    public static void arrayToSparse(int[][] array){
        NodeStruct node = null;
        List<NodeStruct> sparseNodeArray = new ArrayList<NodeStruct>();
        for (int i =0 ;i< array.length;i++){
            for (int j = 0;j<array[i].length;j++){
                node = new NodeStruct();
                if (array[i][j]!=0){
                    node.row = i;
                    node.column = j;
                    node.value = array[i][j];
                    sparseNodeArray.add(node);
                }
            }
        }
        System.out.println("输出稀疏数组:");
        for (NodeStruct nodeStruct : sparseNodeArray) {
            System.out.println(nodeStruct.row + " " + nodeStruct.column +" "+ nodeStruct.value);
        }
    }
}

class NodeStruct{
    int row = 11;
    int column = 11;
    int value = 0;
}
