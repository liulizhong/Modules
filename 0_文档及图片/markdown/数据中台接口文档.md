# 瑞华大数据中台统一接口文档
目录

一. 查询自动化点表实时数据 \
二. 查询自动化点表历史数据 \
三. 数据中台通用写数接口<待完善> \
四. 数据中台通用读数接口<待完善>

---

**1\. 查询自动化点表实时数据**
######  接口功能
> 获取自动化点表数据的实时值，可通过<系统名称、分类名称、设备名称、点表名、
描述模糊查询等方式>获取一或多个实时值

###### 请求URL
> http://10.238.255.252:9008/docs/index

###### 详见protobuf设计文档
> http://106.120.237.30:8088/base/opcredis/blob/master/rhtect-restful/src/main/proto/ruihua.rpc.apiService.proto
> http://106.120.237.30:8088/base/opcredis/blob/master/rhtect-restful/src/main/proto/ruihua.rpc.common.proto

###### 参数支持格式
> GetLatestDataRequest

###### 返回值类型
> StreamObserver<GetLatestDataResponse>

###### HTTP请求方式
> GET

###### 请求参数
|参数               |必选       |类型             |例子                     |说明                                |
|-----              |-------    |-----            |---                      |-----                               |
|appAuth            |true       |DataNameItem     |  {"ruihua":"pwd@123"} |类比String类型的数组，可填0~n个值|
|systemNames        |false       |DataNameItem    | "安全监测系统"        |类比String类型的数组，可填0~n个值|
|categoryNames      |false       |DataNameItem    |  "地面环境监测"       |类比String类型的数组，可填0~n个值|
|equipmentNames     |false       |DataNameItem    |  "虎龙沟风井湿度"     |类比String类型的数组，可填0~n个值|
|clients            |true       |DataNameItem     |  "HJJC01FT02"         |类比String类型的数组，可填0~n个值|
|descriptions      |false       |DataNameItem     |  "温度","湿度"        |类比String类型的数组，可填0~n个值|
|columns           |false       |DataNameItem     | "DATA_TIME","POINT_NAME","POINT_VALUE","POINT_QUALITY","PULL_TIME"  |类比String类型的数组，可填0~n个值， 可选列设备时间"DATA_TIME"、点表名"POINT_NAME"、点表值"POINT_VALUE"、点表状态"POINT_QUALITY"、拉取时间"PULL_TIME"|

###### 返回字段
|返回字段   |字段类型                           | 说明                              |
|-----      |------                             | -----------------------------   |
|header     |CommonResponse                     | 返回结果状态。       |
|dataMap    |map<string,string> dataMap         | 属性和值的Map集合  |

###### 接口源码
> 地址：[http://106.120.237.30:8088/base/opcredis/blob/master/rhtect-restful/src/main/java/rhtect/data/restful/rhtectrestful/gRPC/grpcserver/GRpcApiServer.java]

###### 返回数据格式实例
[client: "HJJC01FT02",
datetime: "2020-09-23 19:37:13",
dataMap { 
"DATA_TIME": "2020-09-22 19:15:18",
"POINT_NAME":"HJJC01FT02",
"POINT_VALUE": "0.0",
"POINT_QUALITY": "Good",
"PULL_TIME": "2020-09-23 19:25:21"
}] 

###### 测试调用实例
<details>
  <summary>调用实例</summary>
  <pre><code> 
    /**
     * 查询实时数据 grpc
     */
    @Test
    public void grpcNewOpcNmae() {
    	GetLatestDataRequest getNewPointOPC1 = GetLatestDataRequest.newBuilder()
    			.addSystemNames(DataNameItem.newBuilder().setName("安全监测系统"))
    			.addCategoryNames(DataNameItem.newBuilder().setName("地面环境监测"))
    			.addDescriptions(DataNameItem.newBuilder().setName("虎龙沟风井湿度"))
    			.addColumns(DataNameItem.newBuilder().setName("DATA_TIME"))
    			.addColumns(DataNameItem.newBuilder().setName("POINT_NAME"))
    			.addColumns(DataNameItem.newBuilder().setName("POINT_VALUE"))
    			.addColumns(DataNameItem.newBuilder().setName("POINT_QUALITY"))
    			.addColumns(DataNameItem.newBuilder().setName("PULL_TIME"))
    			.build();
    	GetLatestDataResponse opcs1 = rpcClientApiService.getNewOPC(getNewPointOPC1);
    	assertThat( opcs1 ).isNotNull();
    	assertThat( opcs1.getHeader().getCode() ).isEqualTo( EnumRpcCode.RPC_SUCCESSFUL );
    	assertThat( opcs1.getDataMapList() ).isNotNull();
    	assertThat( opcs1.getDataMapList() ).isNotEmpty();
    	assertThat( opcs1.getDataMapList().get(0).getClient() ).isEqualTo( "HJJC01FT02" );
    	GetLatestDataRequest getNewPointOPC2 = GetLatestDataRequest.newBuilder()
    			.addClients(DataNameItem.newBuilder().setName("HJJC01FT01"))
    			.build();
    	GetLatestDataResponse opcs2 = rpcClientApiService.getNewOPC(getNewPointOPC2);
    	assertThat( opcs2 ).isNotNull();
    	assertThat( opcs2.getHeader().getCode() ).isEqualTo( EnumRpcCode.RPC_SUCCESSFUL );
    	assertThat( opcs2.getDataMapList() ).isNotNull();
    	assertThat( opcs2.getDataMapList() ).isNotEmpty();
    	//assertThat( opcs2.getDataMapList().get(0).getDataMapList().get(0).getValue() ).isEqualTo( "HJJC01FT02" );
    }
  </code></pre>
</details>

**2\. 查询自动化点表历史数据**
######  接口功能
> 获取自动化点表的历史数据记录，可通过<系统名称、分类名称、设备名称、点表名、
描述模糊查询等方式>获取一或多个点表的一段时间内的数据

###### 请求URL
> http://10.238.255.252:9008/docs/index

###### 详见protobuf设计文档
> http://106.120.237.30:8088/base/opcredis/blob/master/rhtect-restful/src/main/proto/ruihua.rpc.apiService.proto
> http://106.120.237.30:8088/base/opcredis/blob/master/rhtect-restful/src/main/proto/ruihua.rpc.common.proto

###### 参数支持格式
> GetCustomizedDataRequest

###### 返回值类型
> StreamObserver<DataValueItem>

###### HTTP请求方式
> GET

###### 请求参数
|参数               |必选       |类型            |例子                     |说明                              |
|-----              |-------    |-----           |---                      |-----                             |
|appAuth            |true       |DataNameItem    |  {"ruihua":"pwd@123"}   |类比String类型的数组，可填0~n个值|
|systemNames        |false       |DataNameItem   | "安全监测系统"          |类比String类型的数组，可填0~n个值|
|categoryNames      |false       |DataNameItem   |  "地面环境监测"         |类比String类型的数组，可填0~n个值|
|equipmentNames     |false       |DataNameItem   |  "虎龙沟风井湿度"       |类比String类型的数组，可填0~n个值|
|clients            |true       |DataNameItem    |  "HJJC01FT02"           |类比String类型的数组，可填0~n个值|
|descriptions      |false       |DataNameItem    |  "温度","湿度"          |类比String类型的数组，可填0~n个值|
|columns           |false       |DataNameItem    | "DATA_TIME","POINT_NAME","POINT_VALUE","POINT_QUALITY","PULL_TIME"  |类比String类型的数组，可填0~n个值， 可选列设备时间"DATA_TIME"、点表名"POINT_NAME"、点表值"POINT_VALUE"、点表状态"POINT_QUALITY"、拉取时间"PULL_TIME"|
|starttime         |true        |string          | "2020-09-21 13:50:33"  |筛选数据的开始时间|
|endtime           |true        |string          | "2020-09-21 15:50:33"  |筛选数据的结束时间|
|limit             |false       |DataNameItem    |  1000                  |数据条数限制，sql语句中的limit|

###### 返回字段
|返回字段           |字段类型           |说明                              |
|-----              |------             |-----------------------------   |
|client             |string             | 返回点表名|
|datetime           |string             | 数据的时间|
|columnValues       |map<string,string> | 属性和值的Map集合   |

###### 接口源码
> 地址：[http://106.120.237.30:8088/base/opcredis/blob/master/rhtect-restful/src/main/java/rhtect/data/restful/rhtectrestful/gRPC/grpcserver/GRpcApiServer.java]

###### 返回数据格式实例
[client: "HJJC01FT02",
datetime: "2020-09-23 19:37:13",
dataMap { 
"DATA_TIME": "2020-09-22 19:15:18",
"POINT_NAME":"HJJC01FT02",
"POINT_VALUE": "0.0",
"POINT_QUALITY": "Good",
"PULL_TIME": "2020-09-23 19:25:21"
}] 

###### 测试调用实例
<details>
  <summary>调用实例</summary>
  <pre><code> 
    /**
     * 查询历史数据 RestFul
     */
    @Test
    public void grpcOldOpcNmae() throws IOException {
        GetCustomizedDataRequest getNewPointOPC1 = GetCustomizedDataRequest.newBuilder()
                .addSystemNames(DataNameItem.newBuilder().setName("安全监测系统"))
                .addCategoryNames(DataNameItem.newBuilder().setName("地面环境监测"))
                .addDescriptions(DataNameItem.newBuilder().setName("虎龙沟风井温度"))
                .addColumns(DataNameItem.newBuilder().setName("DATA_TIME"))
                .addColumns(DataNameItem.newBuilder().setName("POINT_NAME"))
                .addColumns(DataNameItem.newBuilder().setName("POINT_VALUE"))
                .addColumns(DataNameItem.newBuilder().setName("POINT_QUALITY"))
                .addColumns(DataNameItem.newBuilder().setName("PULL_TIME"))
                .setStartTime("2020-08-28 17:48:27")
                .setEndTime("2020-08-28 17:48:30")
                .build();
        Iterator<DataValueItem> opcs1 = rpcClientApiService.getOldOPC(getNewPointOPC1);
        /*
        while (opcs.hasNext()){
            DataValueItem oRes = opcs.next();
            // 测试将查出的数据写入文件
            System.out.println(oRes);
        }*/
        assertThat( opcs1 ).isNotNull();
        assertThat( opcs1.hasNext() ).isNotNull();
    }
  </code></pre>
</details>