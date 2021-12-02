package redis;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;

/*
 * @class redis连接池
 */
public class JedisPooltool {
    private static JedisPoolConfig poolConfig;
    private static JedisPool pool;
    public static Jedis jedis;

    // 3、建立简单连接处链接
    static {
        poolConfig = new JedisPoolConfig();  //先配置连接池
        poolConfig.setMaxIdle(8);   //最大空数
        poolConfig.setMaxTotal(18); //最大链接数
        pool = new JedisPool(poolConfig, "10.238.255.198", 6379, 2000);  //参数里还可设置密码
        jedis = pool.getResource();
//        System.out.println("ping:" + jedis.ping());     //测试连通性
    }

    @Test   // 2、安全连接池使用（集群时使用，因为涉及到master更换，我需要知道，所有用JedisSentinelPool）
    public void jedisMasterPool() {
        HashSet<String> sentinels = new HashSet<String>();
        sentinels.add("10.238.255.198:6379");
        sentinels.add("10.238.255.197:6379");
        sentinels.add("10.238.255.196:6379");
        poolConfig = new JedisPoolConfig();  //先配置连接池
        poolConfig.setMaxIdle(8);   //最大空数
        poolConfig.setMinIdle(20);  //最小空闲
        poolConfig.setMaxTotal(18); //最大链接数
        poolConfig.setBlockWhenExhausted(true);  //忙碌时是否等待
        poolConfig.setMaxWaitMillis(5000);        //忙碌时等待时长 毫秒
        poolConfig.setTestOnBorrow(true);        //每次获得连接的进行测试
        JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster", sentinels, poolConfig, 2000);//参数里还可设置密码
        jedis = sentinelPool.getResource();
        System.out.println("ping:" + jedis.ping());     //测试连通性
        jedis.close();
        pool.close();
    }

    @Test   // 1、普通建立客户端连接
    public void jedis() {
        Jedis jedis = new Jedis("10.238.255.198", 6379);
//        jedis.auth("xxxx"); //如果Redis服务连接需要密码，制定密码
        System.out.println("ping:" + jedis.ping());
        jedis.close(); //使用完关闭连接
    }
}
