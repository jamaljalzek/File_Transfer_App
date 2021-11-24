package default_group.file_transfer_app;

public class MainApplicationLauncher
{
    public static void main(String[] args)
    {
        // For whatever reason, having main() in HelloApplication, which is a class that extends Application, does NOT run
        // when we build this project into an executable JAR.
        // Trying to call HelloApplication.launch(), nor Application.launch(), from this class also does NOT work (in general),
        // with error messages stating that this class does not extend from Application.
        // So, to build to an executable JAR that actually runs, we must have a class (such as this one) that does NOT extend
        // from Application be our class with main().
        // We also have to call .launch() from within HelloApplication, hence why we wrap it in the below .callLaunch() method,
        // so that we can call it from this class.
        MainApplication.callLaunch();
    }
}