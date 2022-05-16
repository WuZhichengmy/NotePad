package notepad.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * @author wuzhicheng
 * @create 2022-05-10 16:33
 */
public class FontView {
    @FXML
    private Button cancelButton;
    @FXML
    private ComboBox<String> fontBox;
    @FXML
    private ComboBox<String> sizeBox;
    @FXML
    private ComboBox<String> typeBox;
    @FXML
    private Label expLabel;
    @FXML
    private Button confirmButton;
    private TextArea textArea;//记事本文本框


    /**
     * 点击取消
     */
    @FXML
    private void onActionCancel(){
        getStage().close();
    }
    /**
     * 点击确认
     */
    @FXML
    private void onActionConfirm(){
        textArea.setFont(getFont());
        getStage().close();
    }
    public void setTextArea(TextArea t){
        //将记事本的文本框传入该对象
        textArea=t;
        //将字体、字号、示例设置为当前文本框的
        fontBox.setValue(textArea.getFont().getName());
        sizeBox.setValue((int)textArea.getFont().getSize()+"");
        expLabel.setFont(Font.font(fontBox.getValue(),
                Integer.parseInt(sizeBox.getValue())));
    }
    public Stage getStage(){
        return (Stage) fontBox.getScene().getWindow();
    }
    public void initialize(){
        //添加字体
        fontBox.getItems().addAll(Font.getFamilies());
        //添加字号
        for(int i=2;i<=32;i++){
            sizeBox.getItems().add(i+"");
        }
        //添加字形
        typeBox.getItems().addAll("常规","粗体","斜体","粗斜体");
        fontBox.setValue("Arial");
        typeBox.setValue("常规");
        sizeBox.setValue(12+"");
        //字体选项改变时
        fontBox.setOnAction(event -> {
            expLabel.setFont(getFont());
            System.out.println(getFont());
        });
        //字号改变时
        sizeBox.setOnAction(event -> {
            expLabel.setFont(getFont());
            System.out.println(getFont());
        });
        //字形改变时
        typeBox.setOnAction(event -> {
            System.out.println(getFont());
            expLabel.setFont(getFont());
        });
    }

    /**
     * 返回当前选中的字体
     * @return
     */
    private Font getFont(){
        Font font;
        if(typeBox.getValue().equals("常规")){
            font=Font.font(fontBox.getValue(), FontWeight.NORMAL,
                    FontPosture.REGULAR,Integer.parseInt(sizeBox.getValue()));
        }else if(typeBox.getValue().equals("粗体")){
            font=Font.font(fontBox.getValue(), FontWeight.BOLD,
                    FontPosture.REGULAR,Integer.parseInt(sizeBox.getValue()));
        }else if(typeBox.getValue().equals("斜体")){
            font=Font.font(fontBox.getValue(), FontWeight.NORMAL,
                    FontPosture.ITALIC,Integer.parseInt(sizeBox.getValue()));
        }else{
            font=Font.font(fontBox.getValue(), FontWeight.BOLD,
                    FontPosture.ITALIC,Integer.parseInt(sizeBox.getValue()));
        }
        return font;
    }
}
