package fileTransfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class FileInformationGenerator
{
    private static String potentialErrorMessage;


    public static String obtainFileNameSizeAndHash(File fileToAnalyze)
    {
        StringBuilder fileNameSizeAndHash = new StringBuilder();
        fileNameSizeAndHash.append(fileToAnalyze.getName());
        fileNameSizeAndHash.append(':');
        fileNameSizeAndHash.append(fileToAnalyze.length());
        fileNameSizeAndHash.append(':');
        String fileHash = attemptToCalculateFileHash(fileToAnalyze);
        fileNameSizeAndHash.append(fileHash);
        return fileNameSizeAndHash.toString();
    }


    public static String attemptToCalculateFileHash(String fullPathOfFileToHash)
    {
        File fileToHash = new File(fullPathOfFileToHash);
        return attemptToCalculateFileHash(fileToHash);
    }


    public static String attemptToCalculateFileHash(File fileToHash)
    {
        MessageDigest messageDigest = attemptToCreateMessageDigest();
        FileInputStream fileInputStream = attemptToObtainFileInputStream(fileToHash);
        return attemptToExecuteFileHash(messageDigest, fileInputStream);
    }


    private static MessageDigest attemptToCreateMessageDigest()
    {
        try
        {
            return MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e)
        {
            potentialErrorMessage = "ERROR: failed to create a message digest.";
        }
        return null;
    }


    private static FileInputStream attemptToObtainFileInputStream(File fileToStreamFrom)
    {
        try
        {
            return new FileInputStream(fileToStreamFrom);
        }
        catch (FileNotFoundException e)
        {
            potentialErrorMessage = "ERROR: failed to open a file input stream.";
        }
        return null;
    }


    private static String attemptToExecuteFileHash(MessageDigest messageDigest, FileInputStream fileInputStream)
    {
        byte[] byteBuffer = new byte[4096];
        int numberOfBytesRead = attemptToWriteFromInputStreamToBuffer(fileInputStream, byteBuffer);
        while (numberOfBytesRead != -1)
        {
            messageDigest.update(byteBuffer, 0, numberOfBytesRead);
            numberOfBytesRead = attemptToWriteFromInputStreamToBuffer(fileInputStream, byteBuffer);
        }
        byte[] fileHashInBytes = messageDigest.digest();
        HexFormat hexFormat = HexFormat.of();
        String lowerCaseFileHash = hexFormat.formatHex(fileHashInBytes);
        return lowerCaseFileHash.toUpperCase();
    }


    private static int attemptToWriteFromInputStreamToBuffer(FileInputStream fileInputStream, byte[] byteBuffer)
    {
        try
        {
            return fileInputStream.read(byteBuffer);
        }
        catch (IOException e)
        {
            potentialErrorMessage = "ERROR: failed to read from the file input stream.";
        }
        return -1;
    }
}