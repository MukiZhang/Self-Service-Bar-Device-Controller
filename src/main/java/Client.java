import Message_.TypeUtil;
import Message_.light_getM;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        InputStream in = null;
        OutputStream out = null;
        Socket socket = null;
        try {
//        socket = new Socket("localhost",9999);
            socket = new Socket("192.168.1.10 ", 7000);
            out = socket.getOutputStream();
            String response = new light_getM().ColTemp_Swi_Lum("000B57FFFEDEEFBD", 1, 2);
            System.out.println("reponse:" + response.getBytes().length);
            byte[] responseHeaderBuf = TypeUtil.int2Bytes(response.getBytes().length + 2);
            byte[] buf = response.getBytes();
            byte[] responseBodyBuf = new byte[response.getBytes().length + 2];
            int count = 1;
            responseBodyBuf[0] = 0x02;
            for (byte b : buf) {
                responseBodyBuf[count] = b;
                count++;
            }
            responseBodyBuf[count] = 0x03;
            System.out.println(responseHeaderBuf);
            out.write(responseHeaderBuf);
            out.write(responseBodyBuf);
            out.flush();
            in = socket.getInputStream(); // 读头信息，即Body长度
            byte[] headerBuf = new byte[4];
            in.read(headerBuf);
            int bodyLength = TypeUtil.bytesToInt(headerBuf, 0);
            System.out.println("bodyLenth.." + bodyLength);
            byte[] bodyBuf = new byte[bodyLength];

            byte[] Buff = new byte[bodyLength - 2];

            in.read(bodyBuf); // 输出

            String a = new String(bodyBuf).substring(0, 1024);
            System.out.println("server said:" + a);

        } catch (Exception e) {
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