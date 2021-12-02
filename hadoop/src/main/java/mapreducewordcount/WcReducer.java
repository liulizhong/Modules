package mapreducewordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class WcReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    private IntWritable total = new IntWritable(1);

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        context.write(key, total);
/*      //wordcount案例：
        //累加，输入的时候是(word, 1)
        int sum = 0;
        for (IntWritable value : values) {
            sum += value.get();
        }
        //以(word, n)形式输出
        total.set(sum);
        context.write(key, total);
*/
    }
}
