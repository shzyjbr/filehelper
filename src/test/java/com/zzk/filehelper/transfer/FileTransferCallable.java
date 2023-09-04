package com.zzk.filehelper.transfer;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class FileTransferCallable implements Callable<String> {
    private final String sourceFilePath;
    private final String destinationFilePath;
    private final String host;
    private final int port;
    private final ProgressCallback progressCallback;

    public FileTransferCallable(String sourceFilePath, String destinationFilePath, String host, int port, ProgressCallback progressCallback) {
        this.sourceFilePath = sourceFilePath;
        this.destinationFilePath = destinationFilePath;
        this.host = host;
        this.port = port;
        this.progressCallback = progressCallback;
    }

    @Override
    public String call() throws Exception {
        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            File sourceFile = new File(sourceFilePath);
            long fileSize = sourceFile.length();

            socket = new Socket(host, port);
            inputStream = new FileInputStream(sourceFile);
            outputStream = socket.getOutputStream();

            byte[] buffer = new byte[8192];
            int bytesRead;
            long totalBytesRead = 0;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                double progress = (double) totalBytesRead / fileSize;
                progressCallback.update(progress);
            }
            outputStream.flush();

            return "File transferred successfully!";
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        }
    }
}