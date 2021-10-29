package com.itgood;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import java.util.concurrent.TimeUnit;

/**
 * @Description：
 * @Author: nkc
 * @Date: 2021/10/28 21:59
 */
public class ZookeeperClient {

    private static String connectString = "192.168.60.169:2181,192.168.60.170:2181,192.168.60.171:2181";

    private static Integer sessionTimeout = 2000000;

    private ZooKeeper zkClient;

    private List<String> children;

    @Before
    public void init() throws IOException {
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                // 收到事件通知后的回调函数（用户的业务逻辑）
                System.out.println(watchedEvent.getType() + "----" + watchedEvent.getPath());
                try {
                    // 再次启动监听
                    children = zkClient.getChildren("/", true);
                    for (String child : children) {
                        System.out.println(child);
                    }
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 获取节点信息
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void getChildren() throws KeeperException, InterruptedException {
        children = zkClient.getChildren("/", true);
        for (String child : children) {
            System.out.println(child);
        }
        TimeUnit.SECONDS.sleep(Long.MAX_VALUE);
    }


    /**
     * 创建节点
     * 参数1：要创建节点的路径； 参数2：节点数据；  参数3：节点权限；   参数4：节点类型
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void create() throws KeeperException, InterruptedException {
        String zNode = zkClient.create("/atguigu", "nkc".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    /**
     * 查看节点是否存在
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void exist() throws KeeperException, InterruptedException {
        Stat isExists = zkClient.exists("/atguigu", false);
        System.out.println(isExists == null ? "no exist" : "exist");
    }


    /**
     * 修改节点数据
     */
    @Test
    public void set() {
        try {
            zkClient.setData("/atguigu", "你好啊".getBytes(), -1);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    class IStringCallback implements AsyncCallback.StringCallback {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            System.out.println("Create path result: [" + rc + "," + path + "," + ctx + ", real path name " + name);
        }
    }

}


