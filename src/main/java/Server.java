import Message_.TypeUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by claireliu on 2017/5/6.
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        while (true) {
            Socket socket = null;
            InputStream in = null;
            OutputStream out = null;
            try {
                System.out.println("初始化。。。");
                socket = serverSocket.accept();// 从连接队列中取出一个连接，如果没有则等待
                System.out.println("收到请求。。。");
                in = socket.getInputStream();
                byte[] headerBuf = new byte[4];
                in.read(headerBuf);
                int bodyLength = TypeUtil.bytesToInt(headerBuf, 0);
                System.out.println("bodyLength:" + bodyLength);
                byte[] bodyBuf = new byte[bodyLength];
                in.read(bodyBuf);
                System.out.println("client said:" + new String(bodyBuf));
                out = socket.getOutputStream();
                String response = "Ok";
                byte[] responseHeaderBuf = TypeUtil.int2Bytes(response.getBytes().length);
                byte[] responseBodyBuf = response.getBytes();
                out.write(responseHeaderBuf);
                out.write(responseBodyBuf);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }
}