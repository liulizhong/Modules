#!/bin/bash
echo "================     开始启动所有节点服务            ==========="
echo "================     正在启动Zookeeper               ==========="
for i in atguigu@hadoop102 atguigu@hadoop103 atguigu@hadoop104
do
	ssh $i 'source /etc/profile;/opt/module/zookeeper-3.4.10/bin/zkServer.sh start'
done
echo "================     正在启动HDFS                    ==========="
ssh atguigu@hadoop102 '/opt/module/hadoop-2.7.2/sbin/start-dfs.sh'
echo "================     正在启动YARN                    ==========="
ssh atguigu@hadoop103 '/opt/module/hadoop-2.7.2/sbin/start-yarn.sh'
echo "================     正在开启JobHistoryServer        ==========="
ssh atguigu@hadoop102 '/opt/module/hadoop-2.7.2/sbin/mr-jobhistory-daemon.sh start historyserver'

####################################
# 以下是对应hdfs_yarn_jobhistory-stop.sh脚本
#!/bin/bash
echo "================     开始关闭所有节点服务            ==========="
echo "================     正在关闭Zookeeper               ==========="
for i in atguigu@hadoop102 atguigu@hadoop103 atguigu@hadoop104
do
	ssh $i 'source /etc/profile;/opt/module/zookeeper-3.4.10/bin/zkServer.sh stop'
done
echo "================     正在关闭HDFS                    ==========="
ssh atguigu@hadoop102 '/opt/module/hadoop-2.7.2/sbin/stop-dfs.sh'
echo "================     正在关闭YARN                    ==========="
ssh atguigu@hadoop103 '/opt/module/hadoop-2.7.2/sbin/stop-yarn.sh'
echo "================     正在关闭JobHistoryServer        ==========="
ssh atguigu@hadoop102 '/opt/module/hadoop-2.7.2/sbin/mr-jobhistory-daemon.sh stop historyserver'
#####################################