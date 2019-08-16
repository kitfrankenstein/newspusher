package com.kitfrankenstein.newspusher.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Kit
 * @date: 2019/8/13 16:52
 */
public class MqttPropertiesUtil {

    public static String[] HOST;
    public static String CLIENT_ID;
    public static String USERNAME;
    public static char[] PASSWORD;
    public static boolean CLEAN_SESSION;
    public static int KEEP_ALIVE;
    public static int MAX_INFLIGHT;
    public static boolean ASYNC;
    public static int QOS;
    public static int TIMEOUT;

    static {
        HOST = new String[]{loadMqttProperties().getProperty("host")};
        CLIENT_ID = loadMqttProperties().getProperty("clientId");
        USERNAME = loadMqttProperties().getProperty("username");
        PASSWORD = loadMqttProperties().getProperty("password").toCharArray();
        CLEAN_SESSION = Boolean.parseBoolean(loadMqttProperties().getProperty("cleanSession"));
        KEEP_ALIVE = Integer.valueOf(loadMqttProperties().getProperty("keepAlive"));
        MAX_INFLIGHT = Integer.valueOf(loadMqttProperties().getProperty("maxInflight"));
        ASYNC = Boolean.parseBoolean(loadMqttProperties().getProperty("async"));
        QOS = Integer.valueOf(loadMqttProperties().getProperty("qos"));
        TIMEOUT = Integer.valueOf(loadMqttProperties().getProperty("timeout"));
    }

    private static Properties loadMqttProperties() {
        try (InputStream inputstream = MqttPropertiesUtil.class.getResourceAsStream("/mqtt.properties")){
            Properties properties = new Properties();
            properties.load(inputstream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
