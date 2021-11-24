package fileTransfer.uploadMode;

import default_group.file_transfer_app.AttemptToConnectToRecipientScreenController;

import java.io.IOException;
import java.net.Socket;

public class Client
{
    private static Socket clientSocket;


    public static boolean attemptToEstablishNewSocketConnection(String userEnteredIpAddress, int userEnteredPortNumber)
    {
        try
        {
            clientSocket = new Socket(userEnteredIpAddress, userEnteredPortNumber);
        }
        catch (IOException e)
        {
            AttemptToConnectToRecipientScreenController.displayErrorMessage("ERROR: failed to create a new client socket.");
            return false;
        }
        return true;
    }


    public static Socket getCurrentConnectionSocket()
    {
        return clientSocket;
    }


    public static void attemptToCloseCurrentSocketConnection()
    {
        try
        {
            clientSocket.close();
        }
        catch (IOException e)
        {
            AttemptToConnectToRecipientScreenController.displayErrorMessage("ERROR: failed to close the client socket.");
        }
    }
}