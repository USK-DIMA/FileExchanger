package file.exchange.until;

import java.net.Socket;

/**
 * Created by Dmitry on 01.10.2016.
 */
public class SessionInfo {

    private Socket socket;

    public SessionInfo(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }
}
