package notepad.Utility;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import notepad.controller.MainView;

import java.io.IOException;
import java.net.URL;

/**
 * @author wuzhicheng
 * @create 2022-04-27 22:32
 */
public class JavafxUtility {
    public static Stage createNewWindow(FXMLLoader fxmlLoader) throws IOException {
        Parent root=fxmlLoader.load();
        Scene scene=new Scene(root);
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.setTitle("新建文本文档");
        return stage;
    }
    public static FXMLLoader getFxmlloader(String str){
        FXMLLoader fxmlLoader=new FXMLLoader(JavafxUtility.class.getResource(str));
        return fxmlLoader;
    }
}
