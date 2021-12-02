package hbase.mr_hbase_hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobStatus;
import org.apache.hadoop.util.Tool;

import java.util.HashMap;

/*

 */
public class MRTool implements Tool {
    public int run(String[] args) throws Exception {

        // 0、配置app执行时参数[为多次用此工具]
        HashMap<String, String> argsMap = new HashMap<String, String>();
        for (int i = 0; i < args.length; i = i + 2) {
            argsMap.put(args[i], args[i + 1]);
        }

        // 1、配置Job
        Job job = Job.getInstance();
        job.setJarByClass(MRTool.class);

        // 2、mapper
        TableMapReduceUtil.initTableMapperJob(
                //               argsMap.get("--from"),
                "fruit",
                new Scan(),
                ReadFruitMapper.class,
                ImmutableBytesWritable.class,
                Put.class,
                job
        );

        // 3、reducer
        TableMapReduceUtil.initTableReducerJob(
//                argsMap.get("--to"),
                "fruit_mr",
                WriteFruitMRReducer.class,
                job
        );

        // 4、run
        boolean b = job.waitForCompletion(true);

        // 5、返回执行结果
        return b ? JobStatus.State.SUCCEEDED.getValue() : JobStatus.State.FAILED.getValue();
    }

    public void setConf(Configuration configuration) {

    }

    public Configuration getConf() {
        return null;
    }
}

