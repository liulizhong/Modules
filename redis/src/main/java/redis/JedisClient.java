package redis;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * @class redis的五种数据常用方法
 */
public class JedisClient {
    Jedis jedis = JedisPooltool.jedis;

    @Test   // 【一】、Key通用方法
    public void testKey() {
        Set<String> keys = jedis.keys("*");           // 1、获取所有key
        System.out.println(jedis.exists("oooooo"));     // 2、查看指定key是否存在
        System.out.println(jedis.type("alarm102"));     // 3、查看键的类型
        jedis.del("oooooo");                            // 4、删除某个键
        jedis.expire("oooooo", 60 * 60 * 24 * 7);     // 5、设置键过期时间
        System.out.println(jedis.ttl("alarm102"));       // 6、查看键过期时间
        System.out.println(jedis.dbSize());                     // 7、查看当前数据库key数量
    }

    @Test   // 【二】、String类型方法
    public void testString() {
        System.out.println(jedis.get("jedis-s-key-01"));                                     // 1、获取一条string类型的数据
        System.out.println(jedis.set("jedis-s-key-01", "abcdefg"));                        // 2、插入一条string类型的数据，插入成功的话返回 OK(String)
        jedis.setnx("jedis-s-key-01", "000-");                                              // 3、若key存在则不插入不覆盖(set方法会覆盖)
        jedis.setnx("jedis-s-key-01", "000-");                                              // 4、只有在 key 不存在时设置 key 的值
        jedis.setex("jedis-s-key-01", 60 * 60 * 24 * 7, "abcdefg");        // 5、设置值同时设置过期时间
        jedis.getrange("jedis-s-key-01", 1, 3);                  // 6、字符串截取出来
        jedis.setrange("jedis-s-key-01", 1, "i");                       // 7、字符串替换，从第 offset个开始
        jedis.setrange("jedis-s-key-01", 50, "trainningschool");     // 8、若offset超出范围则自动补全：\0x00
        jedis.append("jedis-s-key-01", "pppppp");                                           // 9、将给定的<value> 追加到原值的末尾
        System.out.println(jedis.strlen("jedis-s-key-01"));                           // 10、获取值的长度
        jedis.incr("数字类型值");                                                        // 11、将 key 中储存的数字值增1，若为空，新增值为1
        jedis.decr("数字类型值");                                                        // 12、将 key 中储存的数字值减1，若为空，新增值为-1
        jedis.incrBy("数字类型值", 3);                                          // 13、自增自定义步长
        jedis.mset("jedis-s-key-01", "abcdefg", "jedis-s-key-02");           // 14、同时设置多个k-v
        List<String> mget = jedis.mget("jedis-s-key-01", "jedis-s-key-02");         // 15、同时获取多个k-v
    }

    @Test   // 【三】、List类型方法
    public void testList() {
        jedis.lpush("jedis-s-key-03", "a", "bbb");                     // 1、从左边/右边(rpush)插入一个或多个值。
        System.out.println(jedis.lpop("jedis-s-key-03"));                         // 2、从左边/右边吐出一个值。值在键在，值亡键亡。
        jedis.rpoplpush("jedis-s-key-03", "jedis-s-key-04");         // 3、从<key1>列表右边吐出一个值，插到<key2>列表左边。
        System.out.println(jedis.lrange("jedis-s-key-03", 0, 100));  // 4、按照索引下标获得元素(从左到右)
        System.out.println(jedis.lindex("jedis-s-key-03", 5));            // 5、按照索引下标获得指定单个元素(从左到右)
        Long llen = jedis.llen("jedis-s-key-03");                                 // 6、获取list长度
        jedis.linsert("jedis-s-key-03", BinaryClient.LIST_POSITION.BEFORE, "a", "c");   // 7、在第一个<value>的前面插入<newvalue>
        jedis.lrem("jedis-s-key-03", 3, "a");                     // 8、从左边共删除n个value(从左到右)
    }

    @Test   // 【四】、Set类型方法
    public void testSet() {
        jedis.sadd("jedis-s-key-14", "a", "aa", "d", "b", "c", "e", "a", "");   // 1、将一个或多个 member 元素加入到集合 key 当中，会去重
        Set<String> smembers = jedis.smembers("jedis-s-key-14");                                // 2、取出该集合的所有值。
        jedis.sismember("jedis-s-key-14", "aa");                                     // 3、判断集合<key>是否为含有该<value>值，有返回true，没有返回false
        Long aLong = jedis.scard("jedis-s-key-14");                                             // 4、返回改集合的元素个数
        jedis.srem("jedis-s-key-14", "aa", "", "aa", "h", "f");                       // 5、删除集合中的某些元素。没有不会报错
        jedis.spop("jedis-s-key-14");                                                           // 6、随机从该集合中吐出一个值。也会删除掉
        List<String> srandmember = jedis.srandmember("jedis-s-key-14", 2);              // 7、随机从该集合中取出n个值。不会从集合中删除
        //sinter <key1> <key2>  返回两个集合的交集元素
        // sunion <key1> <key2>  返回两个集合的并集元素。
        // sdiff <key1> <key2>  返回两个集合的差集元素。在key1中减去key2中的元素
    }

    @Test   // 【五】、Hash类型方法
    public void testHash() {
        jedis.hset("jedis-s-key-15", "name", "miss");        // 1、给<key>集合中的  <field>键赋值<value>
        jedis.hget("jedis-s-key-15", "name");                        // 2、从<key1>集合<field> 取出 value
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "hahah");
        map.put("age", "男");
        map.put("num", "1");
        jedis.hmset("jedis-s-key-15", map);                                  // 3、批量设置hash的值
        jedis.hexists("jedis-s-key-15", "name");                     // 4、查看哈希表 key 中，给定域 field 是否存在。
        Set<String> hkeys = jedis.hkeys("jedis-s-key-15");                        // 5、列出该hash集合的所有field
        List<String> hvals = jedis.hvals("jedis-s-key-15");                  // 6、列出该hash集合的所有value
        jedis.hincrBy("jedis-s-key-15", "num", 17);
        jedis.hsetnx("jedis-s-key-15", "name", "没有这个名字吗");
    }

    @Test   // 【六】、ZSet类型方法
    public void testZSet() {
        jedis.zadd("jedis-s-key-16", 60.0, "admin1");
        jedis.zadd("jedis-s-key-16", 80.0, "admin2");
        jedis.zadd("jedis-s-key-16", 72.0, "admin5");
        jedis.zadd("jedis-s-key-16", 72.0, "admin3");
        jedis.zadd("jedis-s-key-16", 70.0, "admin4");                        // 1、将一个或多个member元素及其score从小到大值加入到有序集 key 当中
        Set<String> zrange = jedis.zrange("jedis-s-key-16", 0, 10);                // 2、返回有序集 key 中，下标在<start> <stop>之间的元素，不含score
        Set<String> zbs = jedis.zrangeByScore("jedis-s-key-16", 65, 75);           // 3、返回有序集 key 中，所有 score 值介于 [min，max],
        Set<String> zabs = jedis.zrevrangeByScore("jedis-s-key-16", 80, 70);       // 4、同上，改为从大到小排序输出
        jedis.zincrby("jedis-s-key-16", 10, "admin3");                       // 5、为元素的score加上增量
        jedis.zrem("jedis-s-key-16", "admin5");                                   // 6、删除该集合下，指定值的元素
        Long zcount = jedis.zcount("jedis-s-key-16", 60, 70);                      // 7、统计该集合，分数区间内的元素个数
        Long admin4 = jedis.zrank("jedis-s-key-16", "admin4");                      // 8、返回该值在集合中的排名，从0开始。
    }
}
