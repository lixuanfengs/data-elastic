package com.mysql.to.elastic.zookeepers;


import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * @author: cactusli
 * @date: 2022.04.07
 */
@Slf4j
public class ZkClinett {

    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;

    public static void main(String[] args) throws Exception {

        // Retry strategy. Retry 3 times, and will increase the sleep time between retries.
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        CuratorFramework zkClient = CuratorFrameworkFactory.builder()
                // the server to connect to (can be a server list)
                .connectString("192.168.1.107:2181")
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();


        //zkClient.create().forPath("node1");
        //zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/node1/00001"); //创建永久节点
        //Stat stat = zkClient.checkExists().forPath("/node1/00001");//不为null的话，说明节点创建成功
       // log.info("Stat: {}", stat.toString());

        //创建节点并指定数据内容, 获取内容并更新节点内容
        zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/node2/00002", "java clinet".getBytes());
        //byte[] javabytes = zkClient.getData().forPath("/node2/00002");
        //String java = new String(javabytes);
        //log.info("/node2/00002: {}", java);
        //更新节点数据
        //zkClient.setData().forPath("/node2/00002","php clinet".getBytes());//更新节点数据内容
        //byte[] phpbytes = zkClient.getData().forPath("/node2/00002");
        //String php = new String(phpbytes);
        //log.info("/node2/00002: {}", php);

        //删除一个节点以及其下的所有子节点
       // zkClient.delete().deletingChildrenIfNeeded().forPath("/node2");

        //获取某个节点的所有子节点路径
        List<String> childrenPaths = zkClient.getChildren().forPath("/node2");
        childrenPaths.stream().forEach(f->{
            log.info("childrenPaths: {}", new String (f.getBytes()));
        });
    }
}
