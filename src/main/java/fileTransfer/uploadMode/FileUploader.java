package fileTransfer.uploadMode;

import default_group.file_transfer_app.AttemptToUploadFilesScreenController;
import fileTransfer.FileInformationGenerator;

import java.io.*;
import java.net.Socket;

public class FileUploader
{
    
    
    public static void attemptToUploadFileOverCurrentConnection(File fileToUpload)
    {
        DataOutputStream clientOutputStream = attemptToObtainClientOutputStream(Client.getCurrentConnectionSocket());
        attemptToUploadFileInformation(fileToUpload, clientOutputStream);
        FileInputStream fileInputStream = attemptToOpenFileInputStream(fileToUpload);
        attemptToUploadFile(fileToUpload, fileInputStream, clientOutputStream);
        attemptToCloseFileInputStream(fileInputStream);
    }


    private static DataOutputStream attemptToObtainClientOutputStream(Socket clientSocket)
    {
        try
        {
            OutputStream outputStream = clientSocket.getOutputStream();
            return new DataOutputStream(outputStream);
        }
        catch (IOException e)
        {
            AttemptToUploadFilesScreenController.displayErrorMessage("ERROR: failed to obtain a client output stream.");
        }
        return null;
    }


    private static void attemptToUploadFileInformation(File fileToUpload, DataOutputStream clientOutputStream)
    {
        String fileInformation = FileInformationGenerator.obtainFileNameSizeAndHash(fileToUpload);
        try
        {
            clientOutputStream.writeUTF(fileInformation);
        }
        catch (IOException e)
        {
            AttemptToUploadFilesScreenController.displayErrorMessage("ERROR: failed to send file information.");
        }
    }


    private static FileInputStream attemptToOpenFileInputStream(File fileToUpload)
    {
        try
        {
            return new FileInputStream(fileToUpload);
        }
        catch (FileNotFoundException e)
        {
            AttemptToUploadFilesScreenController.displayErrorMessage("ERROR: failed to open a file input stream.");
        }
        return null;
    }


    private static void attemptToUploadFile(File fileToUpload, FileInputStream fileInputStream, DataOutputStream clientOutputStream)
    {
        byte[] byteBuffer = new byte[4096];
        int numberOfBytesRead = attemptToWriteFromInputStreamToBuffer(fileInputStream, byteBuffer);
        double numberOfBytesUploadedSoFar = 0;
        long totalSizeOfFileInBytes = fileToUpload.length();
        while (numberOfBytesRead != -1)
        {
            attemptToWriteFromBufferToOutputStream(byteBuffer, numberOfBytesRead, clientOutputStream);
            numberOfBytesUploadedSoFar += numberOfBytesRead;
            AttemptToUploadFilesScreenController.updateFileUploadProgress(numberOfBytesUploadedSoFar / totalSizeOfFileInBytes);
            numberOfBytesRead = attemptToWriteFromInputStreamToBuffer(fileInputStream, byteBuffer);
        }
    }


    private static int attemptToWriteFromInputStreamToBuffer(FileInputStream fileInputStream, byte[] byteBuffer)
    {
        try
        {
            return fileInputStream.read(byteBuffer);
        }
        catch (IOException e)
        {
            AttemptToUploadFilesScreenController.displayErrorMessage("ERROR: failed to read from the file input stream.");
        }
        return -1;
    }


    private static void attemptToWriteFromBufferToOutputStream(byte[] byteBuffer, int numberOfBytesToWrite, DataOutputStream clientOutputStream)
    {
        try
        {
            clientOutputStream.write(byteBuffer, 0, numberOfBytesToWrite);
        }
        catch (IOException e)
        {
            AttemptToUploadFilesScreenController.displayErrorMessage("ERROR: failed to write to the client output stream.");
        }
    }


    private static void attemptToCloseClientOutputStream(DataOutputStream clientOutputStream)
    {
        // We do not want to close the clientOutputStream after each time we upload a file, as it appears that once it has
        // been closed, it cannot be reopened (for the current clientSocket connection).
        // Calling "clientSocket.getOutputStream();" throws an IOException when we try to upload a second file, since we
        // previously closed the clientOutputStream after uploading the first file.
        try
        {
            clientOutputStream.close();
        }
        catch (IOException e)
        {
            AttemptToUploadFilesScreenController.displayErrorMessage("ERROR: failed to close the client output stream.");
        }
    }


    private static void attemptToCloseFileInputStream(FileInputStream fileInputStream)
    {
        try
        {
            fileInputStream.close();
        }
        catch (IOException e)
        {
            AttemptToUploadFilesScreenController.displayErrorMessage("ERROR: failed to close the file input stream.");
        }
    }
}