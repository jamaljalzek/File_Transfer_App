package fileTransfer.downloadMode;

import default_group.file_transfer_app.AttemptToDownloadFilesScreenController;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class FileDownloader extends Thread
{
    private static FileDownloader currentFileDownloaderThread;


    public static void attemptToDownloadAnyIncomingFilesOverCurrentConnectionOnNewThread()
    {
        currentFileDownloaderThread = new FileDownloader();
        currentFileDownloaderThread.start();
    }


    public static void shutDownCurrentFileDownloaderThread()
    {
        currentFileDownloaderThread.interrupt();
        currentFileDownloaderThread = null;
    }


    @Override
    public void run()
    {
        DataInputStream clientInputStream = attemptToObtainClientInputStream(Server.getCurrentConnectionSocket());
        while (!this.isInterrupted())
        {
            if (hasAnyNewDataBeenReceived(clientInputStream))
            {
                FileSaver.attemptToSaveIncomingFileOverCurrentConnection(clientInputStream);
            }
        }
        attemptToCloseClientInputStream(clientInputStream);
    }


    private DataInputStream attemptToObtainClientInputStream(Socket clientSocket)
    {
        try
        {
            InputStream inputStream = clientSocket.getInputStream();
            return new DataInputStream(inputStream);
        }
        catch (IOException e)
        {
            AttemptToDownloadFilesScreenController.displayErrorMessageThroughMainThread("ERROR: failed to obtain a client input stream.");
        }
        return null;
    }


    private boolean hasAnyNewDataBeenReceived(DataInputStream clientInputStream)
    {
        try
        {
            return (clientInputStream.available() > 0);
        }
        catch (IOException e)
        {
            AttemptToDownloadFilesScreenController.displayErrorMessageThroughMainThread("ERROR: failed to check client input stream for any new data.");
        }
        return false;
    }


    private static void attemptToCloseClientInputStream(DataInputStream clientInputStream)
    {
        try
        {
            clientInputStream.close();
        }
        catch (IOException e)
        {
            AttemptToDownloadFilesScreenController.displayErrorMessageThroughMainThread("ERROR: failed to close the client input stream.");
        }
    }
}