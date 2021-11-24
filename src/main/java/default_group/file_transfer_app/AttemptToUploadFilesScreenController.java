package default_group.file_transfer_app;

import fileTransfer.uploadMode.FileUploader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class AttemptToUploadFilesScreenController
{
    @FXML
    private Label currentSelectedFileLabel;
    @FXML
    private Label currentStatusLabel;
    @FXML
    private Button startFileUploadButton;
    @FXML
    private ProgressBar fileUploadProgressBar;
    @FXML
    private Label fileUploadProgressPercentageLabel;
    private List<File> userChosenFiles;
    private static AttemptToUploadFilesScreenController currentInstance;


    @FXML
    protected void initialize()
    {
        currentSelectedFileLabel.setText("Current selected file: N/A");
        currentStatusLabel.setText("Current status: awaiting for file(s) selection");
        startFileUploadButton.setDisable(true);
        currentInstance = this;
    }


    @FXML
    protected void onChooseFileButtonClick()
    {
        FileChooser fileChooser = new FileChooser();
        userChosenFiles = fileChooser.showOpenMultipleDialog(MainApplication.getMainWindow());
        if (userChosenFiles != null && !userChosenFiles.isEmpty())
        {
            currentStatusLabel.setText("Current status: ready to start file(s) upload");
            startFileUploadButton.setDisable(false);
        }
    }


    @FXML
    protected void onStartFileUploadButtonClick()
    {
        currentStatusLabel.setText("Current status: uploading file(s)...");
        currentStatusLabel.setTextFill(Color.BLUE);
        for (File currentFile : userChosenFiles)
        {
            currentSelectedFileLabel.setText("Current selected file: " + currentFile.getName());
            fileUploadProgressBar.setProgress(0.0);
            fileUploadProgressPercentageLabel.setText("0 %");
            FileUploader.attemptToUploadFileOverCurrentConnection(currentFile);
        }
        currentStatusLabel.setText("Current status: all file(s) uploaded!");
        currentStatusLabel.setTextFill(Color.GREEN);
    }


    public static void updateFileUploadProgress(double newFileUploadProgress)
    {
        currentInstance.fileUploadProgressBar.setProgress(newFileUploadProgress);
        double fileUploadProgressOutOf100 = Math.ceil(100 * newFileUploadProgress);
        currentInstance.fileUploadProgressPercentageLabel.setText((int) fileUploadProgressOutOf100 + " %");
    }


    public static void displayErrorMessage(String errorMessage)
    {
        currentInstance.currentStatusLabel.setText(errorMessage);
        currentInstance.currentStatusLabel.setTextFill(Color.RED);
    }


    @FXML
    public void onGoBackToConnectToRecipientScreenButtonClick()
    {
        MainApplication.attemptToDisplayNewScreenOnMainWindow("attemptToConnectToRecipientScreen.fxml");
    }
}