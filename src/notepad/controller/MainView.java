package notepad.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import notepad.Utility.JavafxUtility;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author wuzhicheng
 * @create 2022-04-27 19:48
 */
public class MainView {
    private String title="新建文本文档.txt"; //当前stage的标题
    private ObjectProperty<File> fileObjectProperty=new SimpleObjectProperty<File>(); //当前记事本文件
    private BooleanProperty isChange=new SimpleBooleanProperty(false); //记事本内容自保存之后是否发生改变
    @FXML
    private Label positionLabel;
    @FXML
    private Label sysLabel;
    @FXML
    private Label codingLabel;
    @FXML
    private Label zoomLabel;
    @FXML
    private TextArea textArea;
    @FXML
    private MenuItem undoButton;
    @FXML
    private MenuItem cutButton;
    @FXML
    private MenuItem copyButton;
    @FXML
    private MenuItem pasteButton;
    @FXML
    private MenuItem deleteButton;
    @FXML
    private MenuItem findButton;
    @FXML
    private MenuItem findNextButton;
    @FXML
    private MenuItem findPreButton;

    /**
     * 打开新的窗口
     * @param event
     * @throws IOException
     */
    @FXML
    private void onActionNewWindow(ActionEvent event) throws IOException {
        Stage newStage= JavafxUtility.createNewWindow(
                JavafxUtility.getFxmlloader("/notepad/view/MainView.fxml"));
        newStage.show();
    }
    /**
     * 点击新建
     * @param event
     */
    @FXML
    private void onActionNew(ActionEvent event){
        if(isChange.get()){
            SaveType st=getSaveType();
            if(st.equals(SaveType.CANCEL)){
                return;
            }else if(st.equals(SaveType.SAVE)){
                if(fileObjectProperty.isNotNull().get()){//之前保存过
                    writeFile();
                } else{//之前未保存
                    saveFileDialog();
                    saveFile();
                }
            }
        }
        resetWindow();
    }

    /**
     * 点击另存为按钮
     * @param event
     */
    @FXML
    private void onActionSaveAs(ActionEvent event){
        saveFileDialog();
        readFile();
    }

    /**
     * 点击保存按钮
     * @param event
     */
    @FXML
    private void onActionSave(ActionEvent event){
        //如果文件先前不存在，先弹出另存为保存对话框来保存
        if(fileObjectProperty.isNotNull().get()){
            writeFile();
        }
        //如果文件存在，就直接保存到原文件中
        else{
            saveFileDialog();
            saveFile();
        }
        readFile();
    }

    /**
     * 点击打开按钮
     * @param event
     */
    @FXML
    private void onActionOpen(ActionEvent event){
        if(isChange.get()){//如果用户之前修改过记事本的内容，提示是否更改
            SaveType st=getSaveType();
            if(st.equals(SaveType.SAVE)){//用户选择保存
                if(fileObjectProperty.isNotNull().get()){//之前保存过
                    writeFile();
                } else{//之前未保存
                    saveFileDialog();
                    saveFile();
                }
            }
            else if(st.equals(SaveType.CANCEL)){
                return;
            }
            readFile();
        }
        openFileDialog();//弹出打开新文档的对话框
        readFile();
    }

    /**
     * 点击退出按钮
     * @param event
     */
    @FXML
    private void onActionExit(ActionEvent event){
        if(isChange.get()){//如果已经被修改过
            SaveType st=getSaveType();//提示用户做出选择
            if(st.equals(SaveType.SAVE)){
                if(fileObjectProperty.isNotNull().get()){
                    writeFile();//如果有指定文件位置，直接写入指定文件
                }else{//如果没有指定文件位置，先提示用户选择文件，再保存
                    saveFileDialog();
                    saveFile();
                }
            }
            else if(st.equals(SaveType.CANCEL)){//如果用户选择取消，退出对话框
                return;
            }
        }
        System.exit(0);
    }

    /**
     * 点击撤销
     * @param event
     */
    @FXML
    private void onActionUndo(ActionEvent event){
        textArea.undo();
    }

    /**
     * 点击复制
     * @param event
     */
    @FXML
    private void onActionCopy(ActionEvent event){
        textArea.copy();
    }

    /**
     * 点击粘贴
     * @param event
     */
    @FXML
    private void onActionPaste(ActionEvent event){
        textArea.paste();
    }

    /**
     * 点击剪切
     * @param event
     */
    @FXML
    private void onActionCut(ActionEvent event){
        textArea.cut();
    }

    /**
     * 点击删除
     */
    @FXML
    private void onActionDelete(){
        textArea.deleteText(textArea.getSelection());
    }

    /**
     * 点击全选
     */
    @FXML
    private void onActionSelectAll(){
        textArea.selectAll();
    }

    /**
     * 点击日期/时间
     */
    @FXML
    private void onActionDateAndTime(){
        LocalDateTime localDateTime=LocalDateTime.now();
        String date= DateTimeFormatter.ofPattern("HH:mm yyyy/M/d").format(localDateTime);
        if(textArea.getSelection().getLength()==0){//如果有选中的内容
            textArea.appendText(date);
        }else{//没有选中的内容
            textArea.replaceSelection(date);
        }
    }

    public void initialize(){
        //文本内容改变时，isChange设置为false
        textArea.textProperty().addListener((o)->{
            if(fileObjectProperty.isNull().get()&&textArea.getText().isEmpty()){
                isChange.set(false);
            }else{
                isChange.set(true);
            }
        });
        //如果当前的文本状态被改变，修改stage的标题
        isChange.addListener((o,oldValue,newValue)->{
            Stage stage=getStage();
            String title=stage.getTitle();
            if(!oldValue&&newValue){//如果之前的状态是未改变，新的状态是改变
                stage.setTitle("*"+title);
            } else if(!newValue){//如果新的状态是未改变，就将文件名前的*去掉
                title=title.replace("*","");
                stage.setTitle(title);
            }
        });
        //当前光标的位置
        textArea.caretPositionProperty().addListener((o,oldValue,newValue)->{
            int num= newValue.intValue();
            String textContain=textArea.getText();
            int ln=1,col=1;
            for(int i=0;i<num;i++){
                if(textContain.charAt(i)=='\n'){
                    ln++;
                    col=1;
                }else{
                    col++;
                }
            }
            String posText=String.format("第 %d 行，第 %d 列",ln,col);
            positionLabel.setText(posText);
        });
        //可点击的选项
        undoButton.disableProperty().bind(textArea.undoableProperty().not());
        copyButton.disableProperty().bind(textArea.selectedTextProperty().isEmpty());
        cutButton.disableProperty().bind(textArea.selectedTextProperty().isEmpty());
        findButton.disableProperty().bind(textArea.textProperty().isEmpty());
        findNextButton.disableProperty().bind(textArea.textProperty().isEmpty());
        findPreButton.disableProperty().bind(textArea.textProperty().isEmpty());
    }

    /**
     * 获得当前界面所处的stage
     * @return 当前界面的stage
     */
    private Stage getStage(){
        return (Stage) textArea.getScene().getWindow();
    }

    /**
     * 从当前记事本存储的文件中读取信息，并设置textArea
     */
    private void readFile(){
        if(fileObjectProperty.isNotNull().get()){
            try {
                byte[] readBytes=Files.readAllBytes(fileObjectProperty.get().toPath());
                String text=new String(readBytes,StandardCharsets.UTF_8);
                textArea.setText(text);
                isChange.set(false);
                getStage().setTitle(fileObjectProperty.get().getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存当前文件
     */
    private void saveFile(){
        if(fileObjectProperty.isNotNull().get()){
            StringBuilder stringBuilder=new StringBuilder();
            textArea.getText().lines().forEach((line)->stringBuilder
                    .append(line).append("\n"));
            try{
                Files.writeString(fileObjectProperty.get().toPath(),
                        stringBuilder, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE);
                //将记事本标题中代表已修改未保存的*去掉
                String newTitle=getStage().getTitle().replace("*","");
                getStage().setTitle(newTitle);
                isChange.set(false);//设置当前文档为未修改
            } catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
    }
    /**
     * 弹出另存为文件对话框
     */
    private void saveFileDialog(){
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("另存为");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("文档文件","*.txt"),
                new FileChooser.ExtensionFilter("所有文件","*.*")
        );
        fileChooser.setInitialFileName("*.txt");
        File selectedFile=fileChooser.showSaveDialog(textArea.getScene().getWindow());
        System.out.println(selectedFile);
        fileObjectProperty.set(selectedFile);//将fileObjectProperty设置为需要保存的文件
        saveFile();
    }
    /**
     *打开文件对话框
     */
    private void openFileDialog(){
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("打开");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("文档文件","*.txt"),
                new FileChooser.ExtensionFilter("所有文件","*.*")
        );
        File selectedFile=fileChooser.showOpenDialog(getStage());
        fileObjectProperty.set(selectedFile);
    }
    /**
     * 将当前文本写入指定文件
     */
    private void writeFile(){
        if(fileObjectProperty.isNotNull().get()){
            StringBuilder stringBuilder=new StringBuilder();
            textArea.getText().lines().forEach((line)->stringBuilder.append(line).append("\n"));
            try {
                //将StringBuilder写入文件
                Files.writeString(fileObjectProperty.get().toPath(),stringBuilder,
                        StandardCharsets.UTF_8,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE);
                //将标题中的*去掉
                String newTitle= getStage().getTitle().replace("*","");
                getStage().setTitle(newTitle);
                isChange.set(false);//设置为当前文档未更改
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存对话框
     * @param title 标题
     * @param text  内容
     * @return  用户选择的保存类型
     */
    private SaveType saveDialog(String title,String text){
        Dialog<ButtonType> dialog=new Dialog<ButtonType>();
        dialog.setTitle(title);
        dialog.setHeaderText("");
        dialog.setContentText(text);
        ButtonType saveButton=new ButtonType("保存(S)", ButtonBar.ButtonData.YES);
        ButtonType notSaveButton=new ButtonType("不保存(N)", ButtonBar.ButtonData.NO);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton,notSaveButton,ButtonType.CANCEL);
        dialog.showAndWait();
        ButtonType result=dialog.getResult();
        if(result.equals(saveButton)){
            return SaveType.SAVE;
        }else if(result.equals(notSaveButton)){
            return SaveType.NOTSAVE;
        }else {
            return SaveType.CANCEL;
        }
    }
    /**
     * 重置当前界面
     */
    private void resetWindow(){
        Stage stage=getStage();
        stage.setTitle("新建文本文件");
        isChange.set(false);
        fileObjectProperty.set(null);
        textArea.clear();
    }

    /**
     * 获取用户需要的保存类型
     * @return SAVE\NOTSAVE\CANCEL
     */
    private SaveType getSaveType(){
        SaveType st;
        if(fileObjectProperty.isNotNull().get()){
            st = saveDialog("记事本",
                    String.format("你想将更改保存到%s吗？", fileObjectProperty.get().toPath()));
        } else {
            st = saveDialog("记事本",
                    String.format("你想将更改保存到%s吗？", title));
        }
        return st;
    }

}
