package com.wk.xin.net.sample;

import org.junit.Test;

import java.io.*;
import java.net.*;

/**
 * @author xinyu.zhang
 * @since 2022/12/5 18:00
 */
public class SocketTest {

    @Test
    public void receive() throws IOException {
        receiveSocket();
    }

    @Test
    public void send() throws IOException {
        sendSocket();
    }

    public void sendSocket() throws IOException {
        int port = 10086; // 端口
        // 传输数据
        byte[] data = "刘亦菲是一个大美女".getBytes();
        // 数据字节长度
        int length = data.length;
        // ip地址对象
        InetAddress inetAddress = InetAddress.getLocalHost();
        //            InetAddress.getByName("192.168.3.5");

        /* 1 创建一个发送端的套接字对象 */
        DatagramSocket sendSocket = new DatagramSocket();

        /* 2 打包数据 */
        DatagramPacket sendPacket = new DatagramPacket(data, length, inetAddress, port);

        /* 3 发送数据 */
        sendSocket.send(sendPacket);

        /* 4 释放资源 */
        sendSocket.close();
    }

    public void receiveSocket() throws IOException {
        byte[] receiveDataBuff = new byte[1024];
        int length = receiveDataBuff.length;
        int port = 10086;

        /* 1 创建一个接收端的Socket对象 */
        DatagramSocket receiveSocket = new DatagramSocket(port);

        /* 2 创建一个DatagramPacket对象用来接受数据 */
        DatagramPacket receivePacket = new DatagramPacket(receiveDataBuff, length);

        /* 3 接收数据 */
        receiveSocket.receive(receivePacket);

        /* 4 解析数据包 */
        byte[] receiveData = receivePacket.getData(); // 数据缓冲区
        int receiveDataLength = receivePacket.getLength(); // 接收到的实际数据字节长度
        System.out.println("接受到来自" + receivePacket.getAddress() + "的数据：" + new String(receiveData, 0, receiveDataLength));

        /* 5 释放资源 */
        receiveSocket.close();
    }

    /**
     * 接收多次数据
     */
    @Test
    public void receive2() throws IOException {
        receiveSocket2();
    }

    @Test
    public void send2() throws IOException {
        sendSocket2();
    }

    public void sendSocket2() throws IOException {
        // 1 获取键盘录入对象
        // Scanner sc = new Scanner(System.in);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        // 2 创建一个发送端Socket对象
        DatagramSocket sendSocket = new DatagramSocket();

        while ((line = br.readLine()) != null) {
            if (line.equals("over")) {
                break;
            }
            byte[] data = line.getBytes();
            // 3 打包数据
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 10086);
            // 4 发送数据
            sendSocket.send(sendPacket);

        }
        sendSocket.close();
    }

    public void receiveSocket2() throws IOException {
        // 1 创建一个接收端的socket对象
        @SuppressWarnings("resource") DatagramSocket receiveSocket = new DatagramSocket(10086);
        while (true) {
            // 2 创建一个packet用来接收数据
            byte[] buffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            // 3 持续接收数据
            receiveSocket.receive(receivePacket);
            // 4 解析数据
            System.out.println(receivePacket.getAddress() + ":" + new String(receivePacket.getData(), 0, receivePacket.getLength()));
        }
        // 5 服务端应该一直开启 不需要释放
    }

    /**
     * 模拟聊天窗口
     *
     * @throws SocketException
     */
    @Test
    public void testChat() throws SocketException {
        DatagramSocket senSocket = new DatagramSocket();
        DatagramSocket receiveSocket = new DatagramSocket(10086);

        SendSocketThread sendThread = new SendSocketThread(senSocket);
        ReceiveSocketThread receiveThread = new ReceiveSocketThread(receiveSocket);

        Thread t1 = new Thread(sendThread);
        Thread t2 = new Thread(receiveThread);

        t1.start();
        t2.start();
    }

    static class ReceiveSocketThread implements Runnable {
        /**
         * 接收端套接字对象
         */
        private DatagramSocket receiveSocket;

        public ReceiveSocketThread(DatagramSocket receiveSocket) {
            this.receiveSocket = receiveSocket;
        }

        @Override
        public void run() {
            // 1 创建一个接收端的socket对象
            try {
                while (true) {
                    // 2 创建一个packet用来接收数据
                    byte[] buffer = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                    // 3 持续接收数据
                    receiveSocket.receive(receivePacket);
                    // 4 解析数据
                    System.out.println(receivePacket.getAddress() + ":" + new String(receivePacket.getData(), 0, receivePacket.getLength()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 5 服务端应该一直开启 不需要释放
        }
    }

    static class SendSocketThread implements Runnable {

        /**
         * 发送端套接字对象
         */
        private DatagramSocket sendSocket;

        public SendSocketThread(DatagramSocket sendSocket) {
            this.sendSocket = sendSocket;
        }

        @Override
        public void run() {
            // DatagramSocket sendSocket = null;
            DatagramPacket sendPacket = null;
            BufferedReader br = null;
            String line = null;
            try {
                // sendSocket = new DatagramSocket();
                // 键盘录入对象
                br = new BufferedReader(new InputStreamReader(System.in));
                while ((line = br.readLine()) != null) {
                    if (line.equals("over")) {
                        break;
                    }
                    // 对键盘录入的数据进行打包
                    byte[] sendData = line.getBytes();
                    sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getLocalHost(), 10086);

                    // 发送数据
                    sendSocket.send(sendPacket);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                sendSocket.close();
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
