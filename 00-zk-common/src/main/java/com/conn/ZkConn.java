package com.conn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Description： 获取zk连接信息
 * @Author: nkc
 * @Date: 2021/10/29 23:47
 */
public class ZkConn {

    public static Map<String,Object> getConnectMsg() {
        Properties properties = new Properties();
        InputStream stream = ZkConn.class.getClassLoader().getResourceAsStream("zk.properties");
        try {
            properties.load(stream);
            String connectString = properties.getProperty("zk.connectString");
            Integer sessionTimeout = Integer.parseInt(properties.getProperty("zk.sessionTimeout"));
            Map<String,Object> map = new HashMap<>(2);
            map.put("connectString",connectString);
            map.put("sessionTimeout",sessionTimeout);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

