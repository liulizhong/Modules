// 0、网络及端口转发(cmd里执行就ok)
    netsh interface portproxy show all       //查看本机所有端口转发情况
    netsh interface portproxy add v4tov4 listenaddress=0.0.0.0 listenport=19093 connectaddress=10.238.255.151 connectport=8080     //设置机器端口映射，访问本机的20000端口会转发到10.8.0.6:1525
    netsh interface portproxy add v4tov4 listenaddress=10.8.0.6 listenport=9093 connectaddress=10.238.255.151 connectport=9092
    netsh interface portproxy add v4tov4 listenaddress=10.8.0.6 listenport=9094 connectaddress=10.238.255.152 connectport=9092
    netsh interface portproxy add v4tov4 listenaddress=10.8.0.6 listenport=9095 connectaddress=10.238.255.153 connectport=9092
    netsh interface portproxy delete v4tov4 listenaddress=10.8.0.6 listenport=9092       //删除本机9006端口的转发
// 1、shell
    jps ps -ef grep java                            //查看所有java进程/进程中删选有java的jvm实例
    tar -zxvf kafka_2.11-0.11.0.0.tgz -C /opt/module/       //解压安装包到指定目录
    xsync kafka/                                            //集群间拷贝资料
    sudo vim /etc/profile                                   //使用root权限修改文件
    source /etc/profile                                     //是配置文件生效
// 2、zookeeper
    zookeeper-3.4.6/bin/zkServer.sh status                  //查看
// 3、Kafka
    bin/kafka-server-start.sh config/server.properties &    //启动Kafka的broker(节点)
    bin/kafka-server-stop.sh stop                           //关闭kafka
    bin/kafka-topics.sh --zookeeper 10.238.251.3:2181 --list   //查看所有topic
    bin/kafka-topics.sh --zookeeper 10.238.251.3:2181 --create --replication-factor 3 --partitions 1 --topic first     //创建topic：--replication-factor  定义副本数，不能大于节点数
    bin/kafka-topics.sh --zookeeper 10.238.251.3:2181 --delete --topic first                                           //删除topic
    bin/kafka-console-producer.sh --broker-list 10.238.251.3:9092 --topic first                                        //命令行给topic发送消息
    bin/kafka-console-consumer.sh --bootstrap-server 10.238.251.3:9092 --from-beginning --topic opc-olddata  --consumer.config config/consumer.properties    //消费消息(--from-beginning 把主题下所有数据都消费出来)
    bin/kafka-topics.sh --zookeeper 10.238.251.3:2181 --describe --topic first                                         //查看某个topic详情
    bin/kafka-console-consumer.sh --topic __consumer_offsets --zookeeper 10.238.251.3:2181 \
        --formatter "kafka.coordinator.group.GroupMetadataManager\$OffsetsMessageFormatter" \
        --consumer.config config/consumer.properties --from-beginning                                               //读取本地保存的offset(--from-beginning只看一次，第二次不加这个参数)
//4、Hadoop
//5、zookeeper
    stat path [watch]
    set path data [version]
    ls path [watch]
    delquota [-n|-b] path
    ls2 path [watch]
    setAcl path acl
    setquota -n|-b val path
    history
    redo cmdno
    printwatches on|off
    delete path [version]
    sync path
    listquota path
    rmr path
    get path [watch]
    create [-s] [-e] path data acl
    addauth scheme auth
    quit
    getAcl path
    close
    connect host:port






