package notepad.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author wuzhicheng
 * @create 2022-05-07 23:32
 */
public class ReplaceView {
    @FXML
    private Button findNextButton;
    @FXML
    private Button replaceButton;
    @FXML
    private TextField findText;
    @FXML
    private TextField replaceText;
    @FXML
    private Button cancelButton;
    @FXML
    private Button replaceAllButton;
    @FXML
    private CheckBox matchCaseCheck;
    @FXML
    private CheckBox aroundCheck;

    /**
     * 点击关取消按钮
     */
    @FXML
    private void onActionCancel(){
        getStage().close();
    }

    /**
     * 点击查找下一个
     */
    @FXML
    private void onActionFindNext(){
        findNextDown();
    }

    /**
     * 点击替换
     */
    @FXML
    private void onActionReplace(){
        //如果当前选中的不是需要替换的内容
        if(!textArea.getSelectedText().equals(findText.getText())){
            findNextDown();
            return;
        }
        System.out.println("abc");
        textArea.replaceSelection(replaceText.getText());
        findNextDown();
    }

    /**
     * 点击替换全部
     */
    @FXML
    private void onActionReplaceAll(){
        String oldStr=findText.getText();
        String modStr=findText.getText();
        if(!matchCaseCheck.isSelected()){
            oldStr=oldStr.toLowerCase();
            modStr=modStr.toLowerCase();
        }
        String str=textArea.getText();
        String newStr=str.replaceAll(oldStr,modStr);
        textArea.setText(newStr);
    }
    public void initialize(){
        //按钮是否可以点击和输入框是否有文字绑定
        replaceButton.disableProperty().bind(findText.textProperty().isEmpty());
        replaceAllButton.disableProperty().bind(findText.textProperty().isEmpty());
        findNextButton.disableProperty().bind(findText.textProperty().isEmpty());
    }
    private TextArea textArea;
    public void setTextArea(TextArea textArea){
        this.textArea=textArea;
    }
    private Stage getStage(){
        return (Stage) findText.getScene().getWindow();
    }

    /**
     * 往后查找
     */
    private boolean findNextDown(){
        int nowPos=textArea.getCaretPosition();
        String textStr=textArea.getText();
        String find=findText.getText();
        if(!matchCaseCheck.isSelected()){
            textStr.toLowerCase();
            find.toLowerCase();
        }
        int next=textStr.indexOf(find,nowPos);
        if(next!=-1){
            textArea.selectRange(next,next+find.length());
            return true;
        }else{
            if(aroundCheck.isSelected()){
                next = textStr.indexOf(find);
                if (next != -1) {
                    textArea.positionCaret(next);
                    textArea.selectRange(next, next + find.length());
                    return false;
                }
            }
            Alert alert = new Alert(Alert.AlertType.NONE,
                    "找不到"+find,
                    ButtonType.OK);
            alert.initModality(Modality.WINDOW_MODAL);
            alert.initOwner(getStage());
            alert.setTitle("记事本");
            alert.showAndWait();
            return false;
        }
    }
}
