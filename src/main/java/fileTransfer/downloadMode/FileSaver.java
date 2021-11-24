package fileTransfer.downloadMode;

import default_group.file_transfer_app.AttemptToDownloadFilesScreenController;
import fileTransfer.FileInformationGenerator;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileSaver
{
    public static void attemptToSaveIncomingFileOverCurrentConnection(DataInputStream clientInputStream)
    {
        String fileInformation = attemptToDownloadFileInformation(clientInputStream);
        String fileNameWithExtension = FileInformationParser.extractNameFromFileInformation(fileInformation);
        AttemptToDownloadFilesScreenController.displayNewFileDownloadThroughMainThread(fileNameWithExtension);
        int fileSize = FileInformationParser.extractSizeFromFileInformation(fileInformation);
        String providedFileHash = FileInformationParser.extractHashFromFileInformation(fileInformation);
        String fullPathToDownloadFileTo = getFullPathOfDownloadsFolder() + fileNameWithExtension;
        FileOutputStream fileOutputStream = attemptToObtainFileOutputStream(fullPathToDownloadFileTo);
        attemptToDownloadFile(clientInputStream, fileOutputStream, fileSize);
        attemptToCloseFileOutputStream(fileOutputStream);
        String calculatedHashOfDownloadedFile = FileInformationGenerator.attemptToCalculateFileHash(fullPathToDownloadFileTo);
        if (providedFileHash.equals(calculatedHashOfDownloadedFile))
        {
            AttemptToDownloadFilesScreenController.displayFileDownloadCompleteThroughMainThread();
        }
        else
        {
            AttemptToDownloadFilesScreenController.displayErrorMessageThroughMainThread("ERROR, file integrity check failed.");
        }
    }


    private static String attemptToDownloadFileInformation(DataInputStream clientInputStream)
    {
        try
        {
            return clientInputStream.readUTF();
        }
        catch (IOException e)
        {
            AttemptToDownloadFilesScreenController.displayErrorMessageThroughMainThread("ERROR: failed to read from the client input stream.");
        }
        return null;
    }


    private static String getFullPathOfDownloadsFolder()
    {
        StringBuilder fullPathOfDownloadsFolder = new StringBuilder();
        String fullPathOfHomeFolder = System.getProperty("user.home");
        fullPathOfDownloadsFolder.append(fullPathOfHomeFolder);
        // On Windows, file paths are separated by "\" while on Linux and MacOS, files are separated by "/":
        String filePathSeparatorSlash = System.getProperty("file.separator");
        fullPathOfDownloadsFolder.append(filePathSeparatorSlash);
        fullPathOfDownloadsFolder.append("Downloads");
        fullPathOfDownloadsFolder.append(filePathSeparatorSlash);
        return fullPathOfDownloadsFolder.toString();
    }


    private static FileOutputStream attemptToObtainFileOutputStream(String fullPathToDownloadFileTo)
    {
        try
        {
            return new FileOutputStream(fullPathToDownloadFileTo);
        }
        catch (FileNotFoundException e)
        {
            AttemptToDownloadFilesScreenController.displayErrorMessageThroughMainThread("ERROR: failed to obtain a file output stream.");
        }
        return null;
    }


    private static void attemptToDownloadFile(DataInputStream clientInputStream, FileOutputStream fileOutputStream, int totalSizeOfFileInBytes)
    {
        byte[] byteBuffer = new byte[4096];
        double numberOfBytesDownloadedSoFar = 0;
        // When multiple files are sent, we may end up reading the bytes for the next file in line,
        // when we should only read the exact amount of bytes needed for the current file, and then return from
        // this method.
        // So, each iteration of the below loop, we must check if the number of bytes left to read is less than
        // the capacity of the buffer.
        int numberOfBytesToRead = Math.min(totalSizeOfFileInBytes, 4096);
        int numberOfBytesRead = attemptToWriteFromInputStreamToBuffer(clientInputStream, byteBuffer, numberOfBytesToRead);
        while (numberOfBytesRead != -1)
        {
            attemptToWriteFromBufferToOutputStream(byteBuffer, numberOfBytesRead, fileOutputStream);
            numberOfBytesDownloadedSoFar += numberOfBytesRead;
            AttemptToDownloadFilesScreenController.updateFileDownloadProgressThroughMainThread(numberOfBytesDownloadedSoFar / totalSizeOfFileInBytes);
            if (numberOfBytesDownloadedSoFar == totalSizeOfFileInBytes)
            {
                // If we do not break once all "totalSizeOfFileInBytes" have been downloaded, we will get stuck in the below call
                // to attemptToWriteFromInputStreamToBuffer(), since it will wait until new bytes are in the clientInputStream.
                // This of course will not happen until we receive a second file download.
                break;
            }
            int numberOfBytesLeftToRead = totalSizeOfFileInBytes - (int) numberOfBytesDownloadedSoFar;
            numberOfBytesToRead = Math.min(numberOfBytesLeftToRead, 4096);
            numberOfBytesRead = attemptToWriteFromInputStreamToBuffer(clientInputStream, byteBuffer, numberOfBytesToRead);
        }
    }


    private static int attemptToWriteFromInputStreamToBuffer(DataInputStream clientInputStream, byte[] byteBuffer, int numberOfBytesToWrite)
    {
        try
        {
            return clientInputStream.read(byteBuffer, 0, numberOfBytesToWrite);
        }
        catch (IOException e)
        {
            AttemptToDownloadFilesScreenController.displayErrorMessageThroughMainThread("ERROR: failed to read from the client input stream.");
        }
        return -1;
    }


    private static void attemptToWriteFromBufferToOutputStream(byte[] byteBuffer, int numberOfBytesToWrite, FileOutputStream fileOutputStream)
    {
        try
        {
            fileOutputStream.write(byteBuffer, 0, numberOfBytesToWrite);
        }
        catch (IOException e)
        {
            AttemptToDownloadFilesScreenController.displayErrorMessageThroughMainThread("ERROR: failed to write to the file output stream.");
        }
    }


    private static void attemptToCloseFileOutputStream(FileOutputStream fileOutputStream)
    {
        try
        {
            fileOutputStream.close();
        }
        catch (IOException e)
        {
            AttemptToDownloadFilesScreenController.displayErrorMessageThroughMainThread("ERROR: failed to close the file output stream.");
        }
    }
}