import file.exchange.Server;

import java.io.*;
/**
 * Created by Dmitry on 21.09.2016.
 */
public class MainServer {


    public static void main(String[] args) {
        try {
            new Server().startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
