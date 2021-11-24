package default_group.file_transfer_app;

import javafx.fxml.FXML;

public class StartScreenController
{
    @FXML
    protected void initialize()
    {
    }

    
    @FXML
    protected void onEnterSendModeButtonClick()
    {
        MainApplication.attemptToDisplayNewScreenOnMainWindow("attemptToConnectToRecipientScreen.fxml");
    }


    @FXML
    protected void onEnterReceiveModeButtonClick()
    {
        MainApplication.attemptToDisplayNewScreenOnMainWindow("attemptToReceiveConnectionScreen.fxml");
    }
}