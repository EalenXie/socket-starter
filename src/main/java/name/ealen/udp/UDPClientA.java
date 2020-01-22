package name.ealen.udp;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author EalenXie Created on 2019/10/10 20:54.
 * UDP A端
 * 先接收 再发送
 */
@Slf4j
public class UDPClientA {

    public static void main(String[] args) throws IOException {
        log.info("UDP A Started...");
        //A 端 要先进行接收 , 需要指定一个端口进行数据接收
        try (DatagramSocket datagramSocket = new DatagramSocket(20000)) {
            //接收数据
            //创建接收实体
            final byte[] buf = new byte[512];
            DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
            //接收B端
            String data = udpReceive(datagramSocket, receivePacket);
            //A 端 通过此连接向 B端 发送数据
            String responseData = "Receive data with length : " + data.length();
            byte[] responseDataBytes = responseData.getBytes();

            //构建发送包 根据取回的包获取 地址和端口
            DatagramPacket responsePacket = new DatagramPacket(
                    responseDataBytes,
                    responseDataBytes.length,
                    receivePacket.getAddress(),
                    receivePacket.getPort()
            );
            //发送回送数据
            datagramSocket.send(responsePacket);
            log.info("UDP A Finished...");
        }
    }


    /**
     * udp 接收 数据包
     */
    public static String udpReceive(DatagramSocket socket, DatagramPacket packet) throws IOException {
        socket.receive(packet);
        //打印 接收到的信息 和 发送者的信息
        //获取到发送者的ip
        String ip = packet.getAddress().getHostAddress();
        //获取到发送者的端口
        int port = packet.getPort();
        //获取到发送者的数据大小
        int dataLength = packet.getLength();
        //提取数据 用String 对象进行接收
        String data = new String(packet.getData(), 0, dataLength);
        log.info("receive from ip:{}\t port : {}\t data : {}", ip, port, data);
        return data;
    }


}
