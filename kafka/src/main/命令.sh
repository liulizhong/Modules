    一、producer
        (1) new KafkaProducer<String, String>(pro);  //创建生产者实例
        (2) producer.send( new ProducerRecord<String, String>(topicString, partition, key, value), 【new Callback(){***}】)  //发送消息：带回调函数
        (3) void onCompletion(RecordMetadata recordMetadata, Exception e)    //回调函数抽象方法(消息元数据，异常)
            recordMetadata.topic()； //回调函数中获取主题
            recordMetadata.offset()； //获取偏移量
            recordMetadata.partition()； //获取分区号
        (4) producer.close(); //关闭生产者
        (5) implements Partitioner    //自定义分区类
        (6) implements ProducerInterceptor<String, String>      //自定义Kafka拦截器
    二、consumer
        (1) new KafkaConsumer<String, String>(pros); //创建消费者实例
        (2) consumer.subscribe(Arrays.asList("first","second","third")); //设置消费者订阅那些topic
        (3) ConsumerRecords<String, String> records = consumer.poll(100);  //消费数据并返回
        (4) record.topic(); record.offset(); record.key(); record.value(); record.partition();      //获取主题、偏移量、分区、key、value
        (5) new SimpleConsumer(leader, 9092, 3000, 1024 * 1024, "消费数据");    //创建消费者
        (6) new FetchRequestBuilder().addFetch(topic, partition, offset, 1024 * 1024).build();  //封装获取数据的请求
        (7) FetchResponse fetchResponse = consumer.fetch(fetchRequest); //发送请求获取响应
        (8) fetchResponse.messageSet(topic, partition);     //筛选出指定topic和partition的响应集
        (9) messageAndOffset.offset(); //获取offset
        (10) messageAndOffset.message(); message.payload()get(new byte[payload.limit()]); new String(bytes); //将message解析成String