package mapreducewordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WcMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    private Text word = new Text();
    private IntWritable one = new IntWritable(1);

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String newValue = value.toString().replaceAll("---", "^");
        this.word.set(newValue);
        context.write(this.word, one);
/*      // wordcount案例：
        //1. 一行内容按空格拆分成word
        String[] words = value.toString().split(" ");
        //2. 遍历单词，映射成(word, 1)形式写出去
        for (String word : words) {
            this.word.set(word);
            context.write(this.word, one);
        }
*/
    }
}
