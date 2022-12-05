package com.eltorofuerte.net.sample;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author xinyu.zhang
 * @since 2022/12/5 17:56
 */
public class NetApiTest {
    @Test
    public void t1() throws UnknownHostException {
        /* 根据主机名或者IP地址的字符串表示得到IP地址对象 */
        InetAddress inetAddress = InetAddress.getByName("10.21.12.218");
        InetAddress localHost = InetAddress.getLocalHost();

        String hostName = inetAddress.getHostName();
        String hostAddress = inetAddress.getHostAddress();

        System.out.println(hostName);
        System.out.println(hostAddress);


    }
}
