package default_group.file_transfer_app;

import fileTransfer.downloadMode.Server;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.SocketException;

public class AttemptToReceiveConnectionScreenController
{
    @FXML
    private ListView<String> listViewOfDeviceIpv4Addresses;
    @FXML
    private TextField userEnteredPortNumberField;
    @FXML
    private Label currentStatusLabel;
    private static AttemptToReceiveConnectionScreenController currentInstance;


    @FXML
    protected void initialize()
    {
        addIpv4AddressesToListView();
        ObservableList<String> observableListOfDeviceIpv4Addresses = listViewOfDeviceIpv4Addresses.getItems();
        // We will display no more than 5 detected IPv4 addresses at a time, with the extras needing to be scrolled into view:
        int numberOfListViewRowsToDisplay = Math.min(5, observableListOfDeviceIpv4Addresses.size());
        // We must account for an extra pixel at the top and bottom of the ListView when we set the size,
        // otherwise the size we set will always be too small, and the scrollbar on the right side will always show:
        int listViewRowDefaultHeightInPixels = 24;
        listViewOfDeviceIpv4Addresses.setPrefHeight(listViewRowDefaultHeightInPixels * numberOfListViewRowsToDisplay + 2);
        userEnteredPortNumberField.setText("5555");
        currentStatusLabel.setText("Current status: ready to connect");
        currentInstance = this;
    }


    private void addIpv4AddressesToListView()
    {
        try
        {
            listViewOfDeviceIpv4Addresses.setItems(Ipv4AddressLister.getObservableListOfAllIpv4Addresses());
        }
        catch (SocketException e)
        {
            ObservableList<String> observableListContainingErrorMessage = FXCollections.observableArrayList();
            observableListContainingErrorMessage.add("ERROR: cannot display IPv4 address(es) at this time.");
            listViewOfDeviceIpv4Addresses.setItems(observableListContainingErrorMessage);
        }
    }


    @FXML
    protected void onAttemptToReceiveConnectionButtonClick() throws InterruptedException
    {
        currentStatusLabel.setText("Current status: attempting to connect...");
        currentStatusLabel.setTextFill(Color.BLUE);
        int userEnteredPortNumber = Integer.parseInt(userEnteredPortNumberField.getText());
        boolean isSuccessfulConnection = Server.attemptToEstablishNewSocketConnection(userEnteredPortNumber);
        if (isSuccessfulConnection)
        {
            currentStatusLabel.setText("Current status: connection attempt successful!");
            currentStatusLabel.setTextFill(Color.GREEN);
            attemptToPauseFor1Second();
            MainApplication.attemptToDisplayNewScreenOnMainWindow("attemptToDownloadFilesScreen.fxml");
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
        Server.attemptToCloseClientSocket();
        MainApplication.attemptToDisplayNewScreenOnMainWindow("startScreen.fxml");
    }
}