package default_group.file_transfer_app;

import fileTransfer.downloadMode.FileDownloader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;

public class AttemptToDownloadFilesScreenController
{
    @FXML
    private Label currentFileBeingDownloadedLabel;
    @FXML
    private Label currentStatusLabel;
    @FXML
    private ProgressBar fileDownloadProgressBar;
    @FXML
    private Label fileDownloadProgressPercentageLabel;
    private static AttemptToDownloadFilesScreenController currentInstance;


    @FXML
    protected void initialize()
    {
        currentStatusLabel.setText("Current status: ready to receive any file(s)");
        currentInstance = this;
        FileDownloader.attemptToDownloadAnyIncomingFilesOverCurrentConnectionOnNewThread();
    }


    public static void displayNewFileDownloadThroughMainThread(String fileNameAndExtension)
    {
        Platform.runLater(() -> displayNewFileDownload(fileNameAndExtension));
    }


    private static void displayNewFileDownload(String fileNameAndExtension)
    {
        currentInstance.currentFileBeingDownloadedLabel.setText("Current file: " + fileNameAndExtension);
        currentInstance.currentStatusLabel.setText("Downloading file...");
        currentInstance.currentStatusLabel.setTextFill(Color.BLUE);
        currentInstance.fileDownloadProgressBar.setProgress(0.0);
        currentInstance.fileDownloadProgressPercentageLabel.setText("0 %");
    }


    public static void updateFileDownloadProgressThroughMainThread(double newFileDownloadProgress)
    {
        Platform.runLater(() -> updateFileDownloadProgress(newFileDownloadProgress));
    }


    private static void updateFileDownloadProgress(double newFileDownloadProgress)
    {
        currentInstance.fileDownloadProgressBar.setProgress(newFileDownloadProgress);
        double fileDownloadProgressOutOf100 = Math.ceil(100 * newFileDownloadProgress);
        currentInstance.fileDownloadProgressPercentageLabel.setText((int) fileDownloadProgressOutOf100 + " %");
    }


    public static void displayFileDownloadCompleteThroughMainThread()
    {
        Platform.runLater(() -> displayFileDownloadComplete());
    }


    public static void displayFileDownloadComplete()
    {
        currentInstance.currentStatusLabel.setText("Current status: file download complete!");
        currentInstance.currentStatusLabel.setTextFill(Color.GREEN);
    }


    public static void displayErrorMessageThroughMainThread(String errorMessage)
    {
        Platform.runLater(() -> displayErrorMessage(errorMessage));
    }


    private static void displayErrorMessage(String errorMessage)
    {
        currentInstance.currentStatusLabel.setText(errorMessage);
        currentInstance.currentStatusLabel.setTextFill(Color.RED);
    }


    @FXML
    protected void onGoBackToReceiveConnectionScreenButtonClick()
    {
        FileDownloader.shutDownCurrentFileDownloaderThread();
        MainApplication.attemptToDisplayNewScreenOnMainWindow("attemptToReceiveConnectionScreen.fxml");
    }
}