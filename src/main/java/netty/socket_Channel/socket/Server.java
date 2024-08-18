package netty.socket_Channel.socket;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class Server {
    private static final Logger log = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9000);
            // serverSocket建立连接后建立的仍然是一个socket对象
            Socket socket = serverSocket.accept();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            int serRecvVal = ois.readInt();
            log.info("serRecvVal : {}", serRecvVal);
            oos.writeObject("Hello Client.");
            oos.flush();
            while (true){

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
