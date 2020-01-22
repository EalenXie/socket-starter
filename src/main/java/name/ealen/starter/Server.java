package name.ealen.starter;


import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author EalenXie Created on 2019/10/8 21:16.
 * SocketServer  启动,并且accept收听连接
 */
@Slf4j
public class Server {
    private static boolean running = true;
    public static void main(String[] args) throws IOException {
        //1. 启动ServerSocket
        try (ServerSocket serverSocket = new ServerSocket(2044)) {
            log.info("服务器准备就绪 ~ ");
            log.info("服务器信息 : {}  port : {} ", serverSocket.getInetAddress(), serverSocket.getLocalPort());
            //等待客户端连接
            while (running) {
                //serverSocket 开始监听
                Socket socket = serverSocket.accept();
                //接收到 一个socket请求 就为其启动一个线程 单独处理
                ClientHandle clientHandle = new ClientHandle(socket);
                clientHandle.start();
            }
        }
    }
    public static void quit() {
        running = false;
    }
    private static class ClientHandle extends Thread {
        private Socket socket;
        private boolean clientRunning = true;
        public ClientHandle(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            log.info("新客户端连接 : {}  port : {} ", socket.getInetAddress(), socket.getPort());
            try (//获取一个打印流， 用于数据输出，服务器回送数据使用
                 PrintStream printStream = new PrintStream(socket.getOutputStream());
                 //得到一个输入流，用于接受数据
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                while (clientRunning) {
                    //客户端 拿到一条数据
                    String str = bufferedReader.readLine();
                    if ("bye".equalsIgnoreCase(str)) {
                        clientRunning = false;
                        //回送
                        printStream.println("bye");
                    } else {
                        //打印
                        log.info(str);
                        printStream.println("回送数据长度" + str.length());
                    }
                }
            } catch (IOException e) {
                log.error("printStackTrace", e);
            }
            log.info("客户端已经关闭");
        }
    }
}
