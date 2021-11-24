package fileTransfer.downloadMode;

public class FileInformationParser
{
    public static String extractNameFromFileInformation(String fileInformation)
    {
        int firstColonIndex = fileInformation.indexOf(':');
        return fileInformation.substring(0, firstColonIndex);
    }


    public static int extractSizeFromFileInformation(String fileInformation)
    {
        int firstColonIndex = fileInformation.indexOf(':');
        int secondColonIndex = fileInformation.indexOf(':', firstColonIndex + 1);
        String fileSize = fileInformation.substring(firstColonIndex + 1, secondColonIndex);
        return Integer.parseInt(fileSize);
    }


    public static String extractHashFromFileInformation(String fileInformation)
    {
        int firstColonIndex = fileInformation.indexOf(':');
        int secondColonIndex = fileInformation.indexOf(':', firstColonIndex + 1);
        return fileInformation.substring(secondColonIndex + 1);
    }
}