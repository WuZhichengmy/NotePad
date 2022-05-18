package notepad.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.util.Locale;

/**
 * @author wuzhicheng
 * @create 2022-05-03 17:04
 */
public class FindView {
    @FXML
    private Button findNextButton;
    @FXML
    private ToggleGroup directionGroup;
    @FXML
    public TextField findText;
    @FXML
    private RadioButton upRadioButton;
    @FXML
    private Button cancelButton;
    @FXML
    private RadioButton downRadioButton;
    @FXML
    private CheckBox matchCaseCheck;
    @FXML
    private CheckBox aroundCheck;
    private TextArea textArea;
    public static StringProperty findString;
    static {
        findString=new SimpleStringProperty("");
    }

    /**
     * 点击查找下一个
     */
    @FXML
    private void onActionFindNext(){
        if(upRadioButton.isSelected()){
            findNextPre(getStage());
        }else {
            findNextDown(getStage());
        }
    }

    /**
     * 点击Cancel
     */
    @FXML
    private void onActionCancel(){
        getStage().close();
    }
    private Stage getStage(){
        return (Stage) findText.getScene().getWindow();
    }
    public void initialize(){
        //空白时无法查找
        findNextButton.disableProperty().bind(findText.textProperty().isEmpty());
        findText.textProperty().addListener((o,oldStr,newStr)->{
            findString.set(newStr);
        });
    }
    public void setTextArea(TextArea textArea){
        this.textArea=textArea;
    }

    /**
     * 往后查找
     */
    public void findNextDown(Stage stage){
        int nowPos=textArea.getCaretPosition();
        String textStr=textArea.getText();
        String find=findText.getText();
        //没有选中区分大小写，就将目标的查找字段都转换成小写
        if(matchCaseCheck==null||!matchCaseCheck.isSelected()){
            textStr.toLowerCase();
            find.toLowerCase();
        }
        int next=textStr.indexOf(find,nowPos);
        if(next!=-1){//找不到下一个
            textArea.selectRange(next,next+find.length());
        }else{
            if(!(aroundCheck ==null) &&aroundCheck.isSelected()){//选中循环
                next = textStr.indexOf(find);
                if (next != -1) {//在当前光标之前找到了
                    textArea.positionCaret(next);
                    textArea.selectRange(next, next + find.length());
                    return;
                }
            }
            //如果在当前条件下没有找到下一个
            Alert alert = new Alert(Alert.AlertType.NONE,
                    "找不到"+find,
                    ButtonType.OK);
            alert.initModality(Modality.WINDOW_MODAL);
            alert.initOwner(stage);
            alert.setTitle("记事本");
            alert.showAndWait();
            return;
        }
    }

    /**
     * 往前查找
     */
    public void findNextPre(Stage stage){
        int nowPos=textArea.getCaretPosition();
        String textStr=textArea.getText();
        String find=findText.getText();
        if(matchCaseCheck==null||!matchCaseCheck.isSelected()){
            textStr.toLowerCase();
            find.toLowerCase();
        }
        int next=textStr.lastIndexOf(find,nowPos-find.length()-1);
        if(next!=-1){
            textArea.positionCaret(next);
            textArea.selectRange(next,next+find.length());
        }else{
            if(!(aroundCheck ==null) &&aroundCheck.isSelected()){
                next = textStr.lastIndexOf(find);
                if (next != -1) {
                    textArea.positionCaret(next);
                    textArea.selectRange(next, next + find.length());
                    return;
                }
            }
            Alert alert = new Alert(Alert.AlertType.NONE,
                    "找不到"+find,
                    ButtonType.OK);
            alert.initModality(Modality.WINDOW_MODAL);
            alert.initOwner(stage);
            alert.setTitle("记事本");
            alert.showAndWait();
            return;
        }
    }
}
