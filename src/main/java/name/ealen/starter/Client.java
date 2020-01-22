package name.ealen.starter;


import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.*;

/**
 * @author EalenXie Created on 2019/10/8 21:16.
 * socket 客户端  和socketServer 建立连接
 */
@Slf4j
public class Client {

    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket()) {
            //设置 读取流的超时时间
            socket.setSoTimeout(3000);
            //连接本地server,端口2000 ,超时时间 3000
            socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 2044), 3000);
            log.info("已经发起服务器连接 ~ ");
            log.info("客户端信息 : {}  port : {} ", socket.getLocalAddress(), socket.getLocalPort());
            log.info("服务器信息 : {}  port : {} ", socket.getInetAddress(), socket.getPort());
            sendMessage(socket);
        }
        log.info("客户端已经退出");
    }

    public static void sendMessage(Socket socket) throws IOException {
        //构建键盘输入流
        InputStream in = System.in;
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        //得到Socket输出流，并转换为打印流
        OutputStream outputStream = socket.getOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        //得到Socket输入流,接受服务器端的信息
        InputStream inputStream = socket.getInputStream();
        BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while (true) {
            //从键盘读取一行
            String str = input.readLine();
            //将读取到的一行 发送到服务器
            printStream.println(str);
            // 从服务器读取一行
            String result = socketBufferedReader.readLine();
            if ("bye".equals(result)) {
                break;
            } else {
                log.info("get message : {}", result);
            }
        }
        outputStream.close();
        socketBufferedReader.close();
    }
}
