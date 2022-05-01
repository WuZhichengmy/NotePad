import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import notepad.Utility.JavafxUtility;

/**
 * @author wuzhicheng
 * @create 2022-04-27 22:31
 */
public class Notepad extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Stage s=JavafxUtility.createNewWindow(
                JavafxUtility.getFxmlloader("/notepad/view/MainView.fxml")
        );
        s.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
