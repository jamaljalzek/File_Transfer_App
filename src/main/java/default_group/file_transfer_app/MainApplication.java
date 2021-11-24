package default_group.file_transfer_app;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application
{
    private static Stage mainWindow;


    public static void callLaunch()
    {
        launch();
    }


    @Override
    public void start(Stage stage)
    {
        // Each Stage is a window, with the above stage being the initially provided one.
        // We can have multiple Stages running simultaneously, if needed, in order to have multiple windows.
        // Once all Stages/windows have been closed, the program terminates.
        mainWindow = stage;
        setWidthAndHeightOfMainWindow();
        mainWindow.setTitle("LAN File Transfer");
        attemptToAddInitialScreenToMainWindow();
        // When the application is running in download mode (which uses an extra thread), closing the mainWindow does NOT
        // shut down the JVM, indicating that the application is still running (without any GUI).
        // Using the custom shutdown handler below terminates all existing threads once the mainWindow is closed, thus
        // releasing all application resources.
        mainWindow.setOnCloseRequest(new ApplicationShutdownHandler());
        mainWindow.show();
    }


    private class ApplicationShutdownHandler implements EventHandler
    {
        @Override
        public void handle(Event event)
        {
            System.exit(0);
        }
    }


    private static void setWidthAndHeightOfMainWindow()
    {
        // Let's try to maintain a 16:9 aspect ratio, since this will hopefully run on smartphones eventually.
        Screen primaryScreen = Screen.getPrimary();
        // Using .getVisualBounds instead of .getBounds() seems to take the size of the taskbar on Windows into account:
        Rectangle2D primaryScreenVisualBounds =  primaryScreen.getVisualBounds();
        double heightOfMainWindow = primaryScreenVisualBounds.getHeight();
        double widthOfMainWindow = heightOfMainWindow * 9 / 16;
        mainWindow.setHeight(heightOfMainWindow);
        mainWindow.setWidth(widthOfMainWindow);
    }


    private static void attemptToAddInitialScreenToMainWindow()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("startScreen.fxml"));
        try
        {
            Scene newScene = new Scene(fxmlLoader.load());
            mainWindow.setScene(newScene);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public static Stage getMainWindow()
    {
        return mainWindow;
    }


    public static void attemptToDisplayNewScreenOnMainWindow(String nameOfScreenFile)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(nameOfScreenFile));
        try
        {
            Scene mainScene = mainWindow.getScene();
            mainScene.setRoot(fxmlLoader.load());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}