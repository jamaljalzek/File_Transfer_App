package fileTransfer.downloadMode;

import default_group.file_transfer_app.AttemptToReceiveConnectionScreenController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private static Socket clientSocket;


    public static boolean attemptToEstablishNewSocketConnection(int userEnteredPortNumber)
    {
        ServerSocket serverSocket = attemptToCreateNewServerSocket(userEnteredPortNumber);
        if (serverSocket == null)
            return false;
        clientSocket = attemptToCreateNewClientSocketFromServerSocket(serverSocket);
        return clientSocket != null;
    }


    private static ServerSocket attemptToCreateNewServerSocket(int userEnteredPortNumber)
    {
        try
        {
            return new ServerSocket(userEnteredPortNumber);
        }
        catch (IOException e)
        {
            AttemptToReceiveConnectionScreenController.displayErrorMessage("ERROR: failed to create a new server socket.");
        }
        return null;
    }


    private static Socket attemptToCreateNewClientSocketFromServerSocket(ServerSocket serverSocket)
    {
        try
        {
            return serverSocket.accept();
        }
        catch (IOException e)
        {
            AttemptToReceiveConnectionScreenController.displayErrorMessage("ERROR: failed to accept a connection through the server socket.");
        }
        return null;
    }


    public static Socket getCurrentConnectionSocket()
    {
        return clientSocket;
    }


    private static void attemptToCloseServerSocket(ServerSocket serverSocket)
    {
        try
        {
            serverSocket.close();
        }
        catch (IOException e)
        {
            AttemptToReceiveConnectionScreenController.displayErrorMessage("ERROR: failed to close the server socket.");
        }
    }


    public static void attemptToCloseClientSocket()
    {
        try
        {
            clientSocket.close();
        }
        catch (IOException e)
        {
            AttemptToReceiveConnectionScreenController.displayErrorMessage("ERROR: failed to close the client socket.");
        }
    }
}