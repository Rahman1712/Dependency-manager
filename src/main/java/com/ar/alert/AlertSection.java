package com.ar.alert;

import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class AlertSection {
    
     public static void showErrorMessage(String content,Node node){
         Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(null);
            alert.setContentText(content);
            alert.initOwner((Stage)node.getScene().getWindow()); //making alert on top
            alert.showAndWait();
    }
     public static void showWarningMessage(String content,String header,String title,Node node){
         Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.initOwner((Stage)node.getScene().getWindow()); //making alert on top
            alert.showAndWait();
    }
     
    public static void showControFxDialogMessage(String title,String content,Pos position){
        Notifications notificationBuilderResendError = Notifications.create()
                     .title(title)
                     .text(content)
                     .graphic(null)
                     .hideAfter(Duration.seconds(5))
                     .position(position)
                      .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Clicked dependency updated notification");
                    }
                });
                notificationBuilderResendError.darkStyle();
                notificationBuilderResendError.showInformation();
    }
    public static void showControFxErrorMessage(String title,String content,Pos position){
        Notifications notificationBuilderResendError = Notifications.create()
                     .title(title)
                     .text(content)
                     .graphic(null)
                     .hideAfter(Duration.seconds(10))
                     .position(position)
                      .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Clicked error notification");
                    }
                });
                
                notificationBuilderResendError.showError();
    }
    
    public static int showConfirmationMessage(String content ,String title,Node node){
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION,content,ButtonType.YES,ButtonType.NO,ButtonType.CANCEL);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.initOwner((Stage)node.getScene().getWindow());//to top
        Optional<ButtonType> response = alert.showAndWait();
        if(ButtonType.YES == response.get()){
            return 1;
        }else if(ButtonType.NO == response.get()){
            return 2;
        }
        else{
            return 3;
        }
    }
    public static boolean showConfirmationMessageDelete(String content ,String title,Node node){
    	Alert alert =new Alert(Alert.AlertType.CONFIRMATION,content);
    	alert.setTitle(title);
    	alert.setHeaderText(null);
    	alert.setContentText(content);
    	alert.initOwner((Stage)node.getScene().getWindow());//to top
    	Optional<ButtonType> response = alert.showAndWait();
    	return ButtonType.OK == response.get();
    }
    
    public static void showFilesClipBoardSave(String title,String content,Pos position){
        Notifications notificationBuilderResendError = Notifications.create()
                     .title(title)
                     .text(content)
                     .graphic(null)
                     .hideAfter(Duration.seconds(5))
                     .position(position)
                     .darkStyle()
                      .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Clicked files notification");
                    }
                });
                
                notificationBuilderResendError.showInformation();
    }
}
