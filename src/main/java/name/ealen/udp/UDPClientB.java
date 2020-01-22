package name.ealen.udp;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author EalenXie Created on 2019/10/10 20:55.
 * UDP B端
 * 先发送
 * 再接收
 */
@Slf4j
public class UDPClientB {

    public static void main(String[] args) throws IOException {
        log.info("UDP B Started...");
        //B端 先 发送 , 无需指定自身的端口 只需要 指定发送给谁
        try (DatagramSocket datagramSocket = new DatagramSocket()) {
            //构建一份请求数据
            String requestData = "Hello World!";
            byte[] requestDataBytes = requestData.getBytes();
            //构建发送包 发送给 localhost:20000
            DatagramPacket requestPacket = new DatagramPacket(requestDataBytes, requestDataBytes.length,InetAddress.getLocalHost(),20000);
            //发送数据
            datagramSocket.send(requestPacket);

            //接收数据
            //创建接收实体
            final byte[] buf = new byte[512];
            DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
            //接收A 端
            UDPClientA.udpReceive(datagramSocket, receivePacket);
            log.info("UDP B Finished...");
        }

    }
}
