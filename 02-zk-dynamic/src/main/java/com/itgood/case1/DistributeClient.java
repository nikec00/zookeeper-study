package com.itgood.case1;

import com.conn.ZkConn;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description：
 * @Author: nkc
 * @Date: 2021/10/30 00:18
 */
public class DistributeClient {

    private static Integer sessionTimeout;
    private static String connectString;

    private ZooKeeper zkClient;

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        DistributeClient server = new DistributeClient();
        // 1.连接zk集群
        server.getConnect();
        //2. 获取节点数据
        server.getServerList();
        //3. 业务(睡觉)
        TimeUnit.SECONDS.sleep(Long.MAX_VALUE);
    }

    private void getServerList() throws KeeperException, InterruptedException {
        List<String> list = zkClient.getChildren("/servers", true);
        List<String> servers = new ArrayList<>();
        for (String child : list) {
            byte[] data = zkClient.getData("/servers/" + child, false, null);
            servers.add(new String(data));
        }
        System.out.println(servers);
    }

    private void getConnect() throws IOException {
        Map<String, Object> connectMsg = ZkConn.getConnectMsg();
        connectString = connectMsg.get("connectString").toString();
        sessionTimeout = Integer.parseInt(connectMsg.get("sessionTimeout").toString());
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                try {
                    getServerList();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
