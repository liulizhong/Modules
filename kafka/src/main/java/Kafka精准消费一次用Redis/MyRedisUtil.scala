package Kafka精准消费一次用Redis

import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

/**
  * Author: lizhong.liu
  * Date: 2020/9/12
  * Desc: 获取Jedis的工具类
  */
object MyRedisUtil {
  //声明JedisPool连接池对象
  private var jedisPool: JedisPool = null

  //获取Jedis
  def getJedisClient(): Jedis = {
    if (jedisPool == null) {
      build()
    }
    jedisPool.getResource
  }

  //创建连接池对象
  def build(): Unit = {
    val host = MyPropertiesUtil.load("config.properties","redis.host")
    val port = MyPropertiesUtil.load("config.properties","redis.port")

    val jedisPoolConfig = new JedisPoolConfig()
    jedisPoolConfig.setMaxTotal(100) //最大连接数
    jedisPoolConfig.setMaxIdle(20) //最大空闲
    jedisPoolConfig.setMinIdle(20) //最小空闲
    jedisPoolConfig.setBlockWhenExhausted(true) //忙碌时是否等待
    jedisPoolConfig.setMaxWaitMillis(5000) //忙碌时等待时长 毫秒
    jedisPoolConfig.setTestOnBorrow(true) //每次获得连接的进行测试

    jedisPool = new JedisPool(jedisPoolConfig, host, port.toInt)
  }

  def main(args: Array[String]): Unit = {
    val jedis: Jedis = getJedisClient()
    println(jedis.ping())
    jedis.close()
  }

}
