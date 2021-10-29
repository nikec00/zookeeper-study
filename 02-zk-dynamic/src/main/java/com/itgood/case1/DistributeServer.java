package com.itgood.case1;

import com.conn.ZkConn;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description：
 * @Author: nkc
 * @Date: 2021/10/29 23:31
 */
public class DistributeServer {

    private static Integer sessionTimeout;
    private static String connectString;

    private ZooKeeper zkClient;

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

        DistributeServer server = new DistributeServer();
        // 1.连接zk
        server.getConnection();
        // 2.注册服务器到zk集群
        server.regist(args[0]);
        // 3.业务（睡觉）
        TimeUnit.SECONDS.sleep(Long.MAX_VALUE);

    }

    private void regist(String hostName) throws KeeperException, InterruptedException {
        String s = zkClient.create("/servers/" + hostName, hostName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("创建成功：" + s);
    }

    private void getConnection() throws IOException {
        Map<String, Object> connectMsg = ZkConn.getConnectMsg();
        connectString = connectMsg.get("connectString").toString();
        sessionTimeout = Integer.parseInt(connectMsg.get("sessionTimeout").toString());

        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent event) {

            }
        });
    }


}
