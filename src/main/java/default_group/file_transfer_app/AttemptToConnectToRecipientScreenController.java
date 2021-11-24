package default_group.file_transfer_app;

import fileTransfer.uploadMode.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class AttemptToConnectToRecipientScreenController
{
    @FXML
    private TextField userEnteredIpv4AddressField;
    @FXML
    private TextField userEnteredPortNumberField;
    @FXML
    private Label currentStatusLabel;
    @FXML
    private Button attemptToConnectButton;
    private static AttemptToConnectToRecipientScreenController currentInstance;


    @FXML
    protected void initialize()
    {
        userEnteredIpv4AddressField.setText("192.168.XXX.XXX");
        userEnteredPortNumberField.setText("5555");
        currentStatusLabel.setText("Current status: ready to connect");
        currentInstance = this;
    }


    @FXML
    protected void onAttemptToEstablishConnectionButtonClick()
    {
        currentStatusLabel.setText("Current status: attempting to connect...");
        currentStatusLabel.setTextFill(Color.BLUE);
        int userEnteredPortNumber = Integer.parseInt(userEnteredPortNumberField.getText());
        boolean isSuccessfulConnection = Client.attemptToEstablishNewSocketConnection(userEnteredIpv4AddressField.getText(), userEnteredPortNumber);
        if (isSuccessfulConnection)
        {
            currentStatusLabel.setText("Current status: connection attempt successful!");
            currentStatusLabel.setTextFill(Color.GREEN);
            // We disable the "Connect" button after a connection has been successfully established, since the receiving
            // device will have already accepted the first connection, and won't be listening for any attempts of a second
            // connection (until the user on that device manually does it):
            attemptToConnectButton.setDisable(true);
            attemptToPauseFor1Second();
            MainApplication.attemptToDisplayNewScreenOnMainWindow("attemptToUploadFilesScreen.fxml");
        }
        else
        {
            currentStatusLabel.setText("Current status: connection attempt unsuccessful.");
            currentStatusLabel.setTextFill(Color.RED);
        }
    }


    private static void attemptToPauseFor1Second()
    {
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }


    public static void displayErrorMessage(String errorMessage)
    {
        currentInstance.currentStatusLabel.setText(errorMessage);
        currentInstance.currentStatusLabel.setTextFill(Color.RED);
    }


    @FXML
    protected void onGoBackToStartScreenButtonClick()
    {
        Client.attemptToCloseCurrentSocketConnection();
        MainApplication.attemptToDisplayNewScreenOnMainWindow("startScreen.fxml");
    }
}