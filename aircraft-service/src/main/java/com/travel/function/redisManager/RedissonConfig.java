package com.travel.function.redisManager;

import org.springframework.context.annotation.Configuration;

/**
 * @author 邱润泽 bullock
 */
@Configuration
public class RedissonConfig {

//    @Value("${spring.redis.cluster.nodes}")
//    private  String cluster;
//    @Value("${spring.redis.password}")
//    private String password;
//
//    @Bean
//    public RedissonClient getRedisson(){
//        String[] nodes = cluster.split(",");
//        //redisson版本是3.5，集群的ip前面要加上“redis://”，不然会报错，3.2版本可不加
//        for(int i=0;i<nodes.length;i++){
//            nodes[i] = "redis://"+nodes[i];
//        }
//        RedissonClient redisson = null;
//        Config config = new Config();
//        config.useClusterServers() //这是用的集群server
//                .setScanInterval(2000) //设置集群状态扫描时间
//                .addNodeAddress(nodes)
//                .setPassword(password);
//        redisson = Redisson.create(config);
//        //可通过打印redisson.getConfig().toJSON().toString()来检测是否配置成功
//        return redisson;
//    }
}