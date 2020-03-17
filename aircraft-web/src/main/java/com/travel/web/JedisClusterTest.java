package com.travel.web;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class JedisClusterTest {

    public static void main(String[] args) throws IOException {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(10);
        config.setMinIdle(5);
        Set<HostAndPort> jedisClusterNode = new HashSet<HostAndPort>();
        jedisClusterNode.add(new HostAndPort("192.168.1.12", 8001));
        jedisClusterNode.add(new HostAndPort("192.168.1.13", 8002));
        jedisClusterNode.add(new HostAndPort("192.168.1.14", 8003));
        jedisClusterNode.add(new HostAndPort("192.168.1.12", 8004));
        jedisClusterNode.add(new HostAndPort("192.168.1.13", 8005));
        jedisClusterNode.add(new HostAndPort("192.168.1.14", 8006));
        JedisCluster jedisCluster = null;
        try {
            //connectionTimeout：指的是连接一个url的连接等待时间 20
            // soTimeout：指的是连接上一个url，获取response的返回等待时间
            jedisCluster = new JedisCluster(jedisClusterNode, 6000, 5000, 10, "qiurunze", config);
            System.out.println(jedisCluster.set("cluster", "zhuge"));
            System.out.println(jedisCluster.get("cluster"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedisCluster != null)
                jedisCluster.close();
        }
    }
}
