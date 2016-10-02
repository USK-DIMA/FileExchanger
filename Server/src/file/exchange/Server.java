package file.exchange;

import file.exchange.until.FileInfo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Dmitry on 02.10.2016.
 */
public class Server {
    private static final int MAX_BUFFER_SIZE = 65536;

    private static final int PORT = 6667;

    public static final String DEFAULT_FILES_PATH = "download";

    public void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server has started");
        Socket socket = serverSocket.accept();
        System.out.println("Client has connected");

        FileInfo fileInfo = readFileInfo(socket);
        System.out.println(fileInfo.getName());
        File file = generateFile(fileInfo);
        System.out.println("File:  " + file.getAbsolutePath());
        System.out.println("File:  " + file.getName());
        sendValue(socket, file.length());

        receiveAndWriteFileBody(socket, file, fileInfo.getSize());

        socket.close();
        serverSocket.close();
    }

    private void receiveAndWriteFileBody(Socket socket, File file, long fileSize) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        long downloadedFileSize = file.length();

        FileOutputStream fileout = new FileOutputStream(file, downloadedFileSize>0);
        byte[] buffer = new byte[MAX_BUFFER_SIZE];
        while (downloadedFileSize<fileSize){
            int readed = dataInputStream.read(buffer);
            fileout.write(buffer, 0, readed);
            downloadedFileSize+=readed;

        }
        fileout.close();
    }


    /**
     * Отправляет значение по соккету, переданное в паравметре. Пока только long
     * @param socket сокет, куда передаём
     * @param value значение, которое передаем
     * @throws IOException всяие разные сетвые неполадки
     */
    private void sendValue(Socket socket, long value) throws IOException {
        new DataOutputStream(socket.getOutputStream()).writeLong(value);
    }


    private FileInfo readFileInfo(Socket socket) throws IOException {
        FileInfo fileInfo = new FileInfo();
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        fileInfo.setSize(dataInputStream.readLong());
        long fileNameLength = dataInputStream.readLong();
        byte[] name = new byte[(int)fileNameLength];
        dataInputStream.read(name);
        fileInfo.setName(new String(name, "UTF-8"));
        return fileInfo;
    }

    private File generateFile(FileInfo fileInfo) throws IOException {
        System.out.println("File path: " + DEFAULT_FILES_PATH+File.separator+fileInfo.getName());
        File file = new File(DEFAULT_FILES_PATH+File.separator+fileInfo.getName());
        File dir = new File(file.getParent());
        if(!dir.exists()) {
            dir.mkdirs();
        }
        if(!file.exists()) {
            file.createNewFile();
        }
        return file;
    }
}
