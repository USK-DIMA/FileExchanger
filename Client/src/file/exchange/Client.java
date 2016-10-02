package file.exchange;

import file.exchange.until.SessionInfo;

import java.io.*;
import java.net.Socket;

/**
 * Created by Dmitry on 01.10.2016.
 */
public class Client {

    public static final int MAX_BUFFER_SIZE = 65536;


    public void sendFile(SessionInfo sessionInfo, String filePath) throws IOException {
        File file = new File(filePath);
        if(!file.exists() || !file.isFile()){
            throw new FileNotFoundException("По пути " + filePath + " нет файла для загрузки");
        }

        sendFileInfo(sessionInfo.getSocket(), file);
        long downloadingFileSize = writeDownloadingFileSize(sessionInfo.getSocket());
        System.out.println("downloadingFileSize: "+ downloadingFileSize);
        sendFileBody(sessionInfo.getSocket(), file, downloadingFileSize);
    }

    private void sendFileBody(Socket socket, File file, long downloadingFileSize) throws IOException {
        OutputStream outputStream  = socket.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(file);
        long fileSize = file.length();
        fileInputStream.skip(downloadingFileSize);

        byte[] buffer = new byte[MAX_BUFFER_SIZE];
        while (downloadingFileSize<fileSize){
            int readed = fileInputStream.read(buffer);
            outputStream.write(buffer, 0, readed);
            downloadingFileSize+=readed;
        }
    }

    private long writeDownloadingFileSize(Socket socket) throws IOException {
        DataInputStream dataInputStream= new DataInputStream(socket.getInputStream());
        return dataInputStream.readLong();
    }

    private void sendFileInfo(Socket socket, File file) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        long fileSize = file.length();
        byte[] fileName = file.getName().getBytes("UTF-8");
        long fileNameLength = fileName.length;
        dataOutputStream.writeLong(fileSize);
        dataOutputStream.writeLong(fileNameLength);
        dataOutputStream.write(fileName);
    }
}
