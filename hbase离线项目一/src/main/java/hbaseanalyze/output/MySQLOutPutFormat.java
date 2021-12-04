package hbaseanalyze.output;

import hbaseanalyze.convertor.DimensionConvertor;
import hbaseanalyze.kv.CommDimension;
import hbaseanalyze.kv.CountDurationValue;
import utils.JDBCUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLOutPutFormat extends OutputFormat<CommDimension, CountDurationValue> {

    private FileOutputCommitter committer = null;

    @Override
    public RecordWriter<CommDimension, CountDurationValue> getRecordWriter(TaskAttemptContext context) throws IOException, InterruptedException {
        try {
            return new MysqlRecodeWriter();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void checkOutputSpecs(JobContext context) throws IOException, InterruptedException {

    }

    @Override
    public OutputCommitter getOutputCommitter(TaskAttemptContext context) throws IOException, InterruptedException {
        if (committer == null) {
            Path output = getOutputPath(context);
            committer = new FileOutputCommitter(output, context);
        }
        return committer;
    }

    public static Path getOutputPath(JobContext job) {
        String name = job.getConfiguration().get(FileOutputFormat.OUTDIR);
        return name == null ? null : new Path(name);
    }

    protected static class MysqlRecodeWriter extends RecordWriter<CommDimension, CountDurationValue> {

        //维度转换类
        DimensionConvertor dimensionConvertor;

        //连接
        Connection connection;

        //sql语句
        String sql;

        //批量提交的初始值
        int count;

        //批量提交的阈值
        int countBound;

        //预编译SQl对象
        PreparedStatement preparedStatement;


        public MysqlRecodeWriter() throws SQLException {

            //相应属性的初始化
            dimensionConvertor = new DimensionConvertor();

            connection = JDBCUtil.getInstance();
            //设置不自动提交
            connection.setAutoCommit(false);

            sql = "INSERT INTO `tb_call` VALUES(?,?,?,?,?) ON DUPLICATE KEY UPDATE `call_sum`=?,`call_duration_sum`=?";

            preparedStatement = connection.prepareStatement(sql);

            count = 0;

            countBound = 500;
        }

        //核心方法，写数据到MySQL
        @Override
        public synchronized void write(CommDimension key, CountDurationValue value) throws IOException, InterruptedException {

            try {
                //根据维度信息获取维度id
                int contactID = dimensionConvertor.getID(key.getContactDimension());
                int dateID = dimensionConvertor.getID(key.getDateDimension());

                //拼接主键
                String contactDateDimension = contactID + "_" + dateID;

                //取出通话次数
                int callCount = value.getCallCount();

                //取出通话时长
                int callDuration = value.getCallDuration();

                int i = 0;

                //给预编译SQL赋值
                preparedStatement.setString(++i, contactDateDimension);
                preparedStatement.setInt(++i, dateID);
                preparedStatement.setInt(++i, contactID);
                preparedStatement.setInt(++i, callCount);
                preparedStatement.setInt(++i, callDuration);
                preparedStatement.setInt(++i, callCount);
                preparedStatement.setInt(++i, callDuration);

                preparedStatement.addBatch();

                count++;

                //判断当前缓存中SQL是否达到阈值
                if (count >= countBound) {
                    preparedStatement.executeBatch();
                    count = 0;

                    //提交
                    connection.commit();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void close(TaskAttemptContext context) throws IOException, InterruptedException {

            //关闭资源&清空缓存中的数据
            try {
                preparedStatement.executeBatch();

                //提交
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                JDBCUtil.close(null, preparedStatement, connection);
            }

        }
    }

}
