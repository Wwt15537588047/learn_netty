package netty.part1.socket;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


@Slf4j
public class Client {

    private static final Logger log = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1",9000);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            oos.writeInt(123);
            oos.flush();

            Thread.sleep(3000);
            Object object = ois.readObject();
            log.info("client receive messgae : {}", object);
        } catch (IOException |InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
