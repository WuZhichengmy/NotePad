package notepad.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author wuzhicheng
 * @create 2022-05-02 23:24
 */
public class GotoView {
    @FXML
    private TextField lineNumText;
    @FXML
    private Button cancelButton;
    @FXML
    private Button gotoButton;
    private TextArea textArea;
    public void initialize(){
    }
    public void setTextArea(TextArea t){
        textArea=t;
    }
    private Stage getStage(){
        Stage stage= (Stage) gotoButton.getScene().getWindow();
        return stage;
    }
    @FXML
    private void onActionCancel(){
        getStage().close();
    }
    @FXML
    private void onActionGoto(){
        String[] lines= textArea.getText().lines().toArray(String[]::new);//将文本框的内容存入数组
        try {
            int lineNum = Integer.parseInt(lineNumText.getText());//总共的行数
            //指定的行数输入错误，弹出对应的提示
            if(lineNum<=0||lineNum>lines.length){
                Alert alert = new Alert(Alert.AlertType.NONE,
                        "行数小于1或超过了总行数",
                        ButtonType.OK);
                alert.initModality(Modality.WINDOW_MODAL);
                alert.initOwner(getStage());
                alert.setTitle("记事本-跳行");
                alert.showAndWait();
                return;
            }
            int newPos=-1;
            //将行数转换为位置
            for(int i=0;i<lineNum-1;i++){
                newPos+=lines[i].length();
            }
            textArea.positionCaret(newPos+lineNum);
            getStage().close();
        } catch (NumberFormatException e){
            //如果输入的不是数字，弹出对应的提示
            Alert alert = new Alert(Alert.AlertType.NONE,
                    "您只能在此处键入数字",
                    ButtonType.OK);
            alert.initModality(Modality.WINDOW_MODAL);
            alert.initOwner(getStage());
            alert.setTitle("不能接收的字符");
            alert.showAndWait();
        }
    }
}
