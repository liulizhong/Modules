package hbaseanalyze.mr;

import hbaseanalyze.kv.CommDimension;
import hbaseanalyze.output.MySQLOutPutFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class CountDurationDriver extends Configuration implements Tool {

    private Configuration configuration;

    @Override
    public int run(String[] args) throws Exception {

        //1.获取job对象
        Job job = Job.getInstance(configuration);

        //2.设置jar包
        job.setJarByClass(CountDurationDriver.class);

        //3.设置Mapper
        TableMapReduceUtil.initTableMapperJob(args[0],
                new Scan(),
                CountDurationMapper.class,
                CommDimension.class,
                Text.class,
                job);

        //4.设置Reducer
        job.setReducerClass(CountDurationReducer.class);

        //5.设置OutPutFormat
        job.setOutputFormatClass(MySQLOutPutFormat.class);

        //6.提交
        boolean result = job.waitForCompletion(true);

        return result ? 0 : 1;
    }

    @Override
    public void setConf(Configuration conf) {
        configuration = conf;
    }

    @Override
    public Configuration getConf() {
        return configuration;
    }

    public static void main(String[] args) {

        //获取配置信息
        Configuration configuration = HBaseConfiguration.create();

        try {
            int run = ToolRunner.run(configuration, new CountDurationDriver(), args);

            System.exit(run);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
