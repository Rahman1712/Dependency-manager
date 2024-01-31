package com.ar.fxmlcontroller;

import com.ar.alert.AlertSection;
import com.ar.db.DBSection;
import com.ar.util.ClipBoardCreation;
import com.ar.util.Dependency;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
//import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class PreviewAndEditController implements Initializable {

   DBSection db = DBSection.getInstance();
   String dependencyId = "";
   Dependency dep = new Dependency();
   Dependency afterSav = new Dependency();
   Boolean isEditable = Boolean.FALSE;

    private double xOffset = 0;
    private double yOffset = 0;
    private Stage stage;
    
    @FXML
    private AnchorPane mainAnchor;
    
    @FXML
    private Label labelHeading;
    
    @FXML
    private JFXTextField textDependency;
    
    @FXML
    private JFXTextField textGroupId;
    
    @FXML
    private JFXTextField textArtifactId;
    
    @FXML
    private JFXTextField textVersion;
    
    @FXML
    private JFXComboBox<String> textScope;
    
    @FXML
    private JFXComboBox<String> textOptional;
    
    @FXML
    private JFXTextArea textDescription;
    
    @FXML
    private JFXTextArea textPOM;
    
    @FXML
    private JFXButton saveButton;
    
    @FXML
    private JFXButton editBarIcon;
    
    @FXML
    private JFXButton closeBarIcon;
    
    @FXML
    private JFXButton scopeClear;
    
    @FXML
    private JFXButton optionalClear;
    
    @FXML
    private JFXButton refreshIcon;
    
    @FXML
    private HBox headBar;
    
    @FXML
    private JFXButton copyIcon;
    
    private Image image = new Image("dependency.png"); 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textPOM.setEditable(false);
        textDependency.setEditable(false);
        closeBarIcon.setVisible(false);
        textOptional.getItems().addAll("true", "false");
        textScope.getItems().addAll("compile", "provided", "runtime", "test", "system", "import");
        stageDraggable();
        ImageView imgview = new ImageView();
        imgview.setImage(image);
        imgview.setFitWidth(30);
        imgview.setFitHeight(25);
        AnchorPane.setTopAnchor(imgview, 2.0);
        AnchorPane.setLeftAnchor(imgview, 5.0);
        mainAnchor.getChildren().add(imgview);
    }

    @FXML
    private boolean saveButtonAction(ActionEvent event) throws SQLException {
        if((! textDependency.getText().matches(".*\\w.*")) ||
             (! textGroupId.getText().matches(".*\\w.*"))||
                (! textArtifactId.getText().matches(".*\\w.*"))){
            AlertSection.showWarningMessage("please must fill fields : dependency,groupId,artifactId", 
                    "Empty fields found","Dependency warning",saveButton);
            return false;
        }
        String version = textVersion.getText();
        String description = textDescription.getText();

        if (version == null) {
            version = null;
        } else {
            version = version.matches(".*\\w.*") ? version : null;
        }
        if (description == null) {
            description = null;
        } else {
            description = description.matches(".*\\w.*") ? description : null;
        }

//        System.out.println(textDependency.getText());
//        System.out.println(textGroupId.getText());
//        System.out.println(textArtifactId.getText());
//        System.out.println(textVersion.getText());
//        System.out.println(textScope.getValue());
//        System.out.println(textOptional.getValue());
//        System.out.println(textDescription.getText());
        Dependency dependency = new Dependency(textDependency.getText().toUpperCase(),
                textGroupId.getText(), textArtifactId.getText(),
                version, description, textScope.getValue(), textOptional.getValue());
        System.out.println(dependency);
        boolean bool = db.updateDependency(dependency, dependencyId);
        System.out.println("DEPENDENCY_ID(before):"+dependencyId);
        dependencyId = textDependency.getText();//after updating may be change dependency value.
        System.out.println("DEPENDENCY_ID(after):"+dependencyId);
        if (bool) {
            AlertSection.showControFxDialogMessage("Update successful",
                    "dependency updated successfully\n", Pos.BOTTOM_RIGHT);
            afterSavValueSetting(afterSav,dependency); ///
            afterSavValueSetting(dep,dependency); ///
           
        } else {
            AlertSection.showControFxErrorMessage("Update Error!",
                    "Dependency updation Failed\nCheck manually for error correction\n"
                            + "# check already in table\n"
                            + "  if Depedency Name already exist(Primary key violation)\n"
                            + "  choose new Dependeny name", Pos.CENTER);
        }

        return bool;
    }

    @FXML
    private void cancelButtonAction(ActionEvent event) throws SQLException {
        boolean isSame = dependencyObjectsCompare();
        if (!isSame && isEditable) {
            int result = AlertSection.showConfirmationMessage("Do you want save changes to table & database", "Dependency", headBar);
            switch (result) {
                case 1:
                    boolean bool = saveButtonAction(null);
                    if (bool == false) {
                        AlertSection.showControFxErrorMessage("Update Error!",
                                "Dependency updation Failed\nCheck manually for error correction", Pos.CENTER);
                        System.out.println("[FAILED] Saving and closing!");
                        return;
                    }
                    System.out.println("Saving and closing....");
                    break;
                case 2:
                    System.out.println("Don't save closing....");
                    break;
                default:
                    return;
            }
        }
        ((Stage) mainAnchor.getScene().getWindow()).close();
    }

    public void valueTakingForEdit(Dependency dependency) {
        dependencyId = dependency.getDependency();
        textDependency.setText(dependency.getDependency());
        textGroupId.setText(dependency.getGroupId());
        textArtifactId.setText(dependency.getArtifactId());
        textDescription.setText(dependency.getDescription());
        textVersion.setText(dependency.getVersion());
        textScope.setValue(dependency.getScope());
        textOptional.setValue(dependency.getOptional());
        labelHeading.setText("EDIT DEPENDENCY");
        String POM = ClipBoardCreation.clipValueToTextArea(dependency);
        textPOM.setText(POM);
        isEditable = Boolean.TRUE;
//        dep = dependency;
        afterSavValueSetting(dep,dependency);
        afterSavValueSetting(afterSav,dependency);

        textFiledEditTextAreaChange();
    }

    public void valueTakingForView(Dependency dependency) {
        textDependency.setText(dependency.getDependency());
        textGroupId.setText(dependency.getGroupId());
        textArtifactId.setText(dependency.getArtifactId());
        textDescription.setText(dependency.getDescription());
        textVersion.setText(dependency.getVersion());
        textScope.setValue(dependency.getScope());
        textOptional.setValue(dependency.getOptional());

        textDependency.setEditable(false);
        textGroupId.setEditable(false);
        textArtifactId.setEditable(false);
        textDescription.setEditable(false);
        textVersion.setEditable(false);
        editBarIcon.setVisible(false);
        scopeClear.setVisible(false);
        optionalClear.setVisible(false);
        refreshIcon.setVisible(false);

        saveButton.setDisable(true);
        labelHeading.setText("DEPENDENCY DETAILS");

        String POM = ClipBoardCreation.clipValueToTextArea(dependency);
        textPOM.setText(POM);
        dep = dependency;
    }

    @FXML
    private void editBarIconAction(ActionEvent event) {
        textDependency.setEditable(true);
        closeBarIcon.setVisible(true);
        editBarIcon.setVisible(false);
    }

    @FXML
    private void closeBarIconAction(ActionEvent event) {
        textDependency.setEditable(false);
        editBarIcon.setVisible(true);
        closeBarIcon.setVisible(false);
    }

    @FXML
    private void scopeClearAction(ActionEvent event) {
//        System.out.println(textScope.getValue());
        textScope.setValue(null);
        textPOM.setText(ClipBoardCreation.clipValueToTextArea(dep));
    }

    @FXML
    private void optionalClearAction(ActionEvent event) {
        textOptional.setValue(null);
        textPOM.setText(ClipBoardCreation.clipValueToTextArea(dep));
    }

    private void textFiledEditTextAreaChange() {

         textDependency.textProperty().addListener((obs, oldval, newval) -> {
            dep.setDependency(newval);
        });
        textDescription.textProperty().addListener((obs, oldval, newval) -> {
            String strDependecy = textPOM.getText();
            if (!strDependecy.contains("<!-- ")) {
                strDependecy = "<!--   -->" + strDependecy;
            }
            String newstr = strDependecy.replaceAll("(<!-- )[^&]*( -->)", "$1" + newval + "$2");
            textPOM.setText(newstr);
            dep.setDescription(newval);
        });
        textGroupId.textProperty().addListener((obs, oldval, newval) -> {
            String strDependecy = textPOM.getText();
            String newstr = strDependecy.replaceAll("(<groupId>)[^&]*(</groupId>)", "$1" + newval + "$2");
            textPOM.setText(newstr);
            dep.setGroupId(newval);
        });
        textArtifactId.textProperty().addListener((obs, oldval, newval) -> {
            String strDependecy = textPOM.getText();
            String newstr = strDependecy.replaceAll("(<artifactId>)[^&]*(</artifactId>)", "$1" + newval + "$2");
            textPOM.setText(newstr);
            dep.setArtifactId(newval);
        });
        textVersion.textProperty().addListener((obs, oldval, newval) -> {
            String strDependecy = textPOM.getText();
            if (!strDependecy.contains("<version>")) {
                strDependecy = strDependecy.replace("</artifactId>", "</artifactId>\n \t" + "<version> </version>");
            }
            String newstr = strDependecy.replaceAll("(<version>)[^&]*(</version>)", "$1" + newval + "$2");
            textPOM.setText(newstr);
            dep.setVersion(newval);
        });
        textScope.valueProperty().addListener((obs, oldval, newval) -> {
            String strDependecy = textPOM.getText();
            if (!strDependecy.contains("<scope>") && newval != null) {
                if (strDependecy.contains("<version>")) {
                    strDependecy = strDependecy.replace("</version>", "</version>\n \t" + "<scope> </scope>");
                } else {
                    strDependecy = strDependecy.replace("</artifactId>", "</artifactId>\n \t" + "<scope> </scope>");
                }
            }
            if (newval == null) {
                newval = "";
            }
            String newstr = strDependecy.replaceAll("(<scope>)[^&]*(</scope>)", "$1" + newval + "$2");
            textPOM.setText(newstr);
            dep.setScope(newval);
        });
        textOptional.valueProperty().addListener((obs, oldval, newval) -> {
            String strDependecy = textPOM.getText();
            if (!strDependecy.contains("<optional>") && newval != null) {
                if (strDependecy.contains("<scope>")) {
                    strDependecy = strDependecy.replace("</scope>", "</scope>\n \t" + "<optional> </optional>");
                } else if (strDependecy.contains("<version>")) {
                    strDependecy = strDependecy.replace("</version>", "</version>\n \t" + "<optional> </optional>");
                } else {
                    strDependecy = strDependecy.replace("</artifactId>", "</artifactId>\n \t" + "<optional> </optional>");
                }
            }
            if (newval == null) {
                newval = "";
            }
            String newstr = strDependecy.replaceAll("(<optional>)[^&]*(</optional>)", "$1" + newval + "$2");
            textPOM.setText(newstr);
            dep.setOptional(newval);
        });

        textDescription.focusedProperty().addListener((obs, olVal, newVal) -> {
            if (newVal == false) {
                textPOM.setText(ClipBoardCreation.clipValueToTextArea(dep));
            }
        });
        textGroupId.focusedProperty().addListener((obs, olVal, newVal) -> {
            if (newVal == false) {
                textPOM.setText(ClipBoardCreation.clipValueToTextArea(dep));
            }
        });
        textArtifactId.focusedProperty().addListener((obs, olVal, newVal) -> {
            if (newVal == false) {
                textPOM.setText(ClipBoardCreation.clipValueToTextArea(dep));
            }
        });
        textVersion.focusedProperty().addListener((obs, olVal, newVal) -> {
            if (newVal == false) {
                textPOM.setText(ClipBoardCreation.clipValueToTextArea(dep));
            }
        });

    }

    @FXML
    private void refreshIcon(ActionEvent event) {
        textPOM.setText(ClipBoardCreation.clipValueToTextArea(dep));
    }

    @FXML
    private void copyAction(ActionEvent event) {
        ClipBoardCreation.clipValue(dep);
    }

    private void stageDraggable() {

        headBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        headBar.setOnMouseDragged(event -> {
//            stage =(Stage)((Node)event.getSource()).getScene().getWindow();
            stage = (Stage) mainAnchor.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
            stage.setOpacity(0.8f);
        });
        headBar.setOnDragDone(event -> {
            stage.setOpacity(1.0f);
        });
        headBar.setOnMouseReleased(event -> {
            stage.setOpacity(1.0f);
        });
    }

    private void afterSavValueSetting(Dependency main,Dependency dependency) {
        main.setDependency(dependency.getDependency());
        main.setGroupId(dependency.getGroupId());
        main.setArtifactId(dependency.getArtifactId());
        main.setVersion(dependency.getVersion());
        main.setDescription(dependency.getDescription());
        main.setScope(dependency.getScope());
        main.setOptional(dependency.getOptional());
    }

    private boolean dependencyObjectsCompare() {
//        System.out.println(dep);
//        System.out.println(afterSav);
//        System.out.println("EQUALS :="+dep.equals(afterSav)); 
        
        String dep1 = dep.getDependency(); 
        String dep2 = afterSav.getDependency();
        String grop1 = dep.getGroupId();
        String grop2 = afterSav.getGroupId();
        String artf1 = dep.getArtifactId();
        String artf2 = afterSav.getArtifactId();
        String ver1 = dep.getVersion(); 
        if(ver1 == null) ver1 = "";
        else ver1 = ver1.matches(".*\\w.*") ? ver1 : ""; 
        String ver2 = afterSav.getVersion(); 
        if(ver2 == null) ver2 = "";
        else ver2 = ver2.matches(".*\\w.*") ? ver2 : "";
        String desc1 = dep.getDescription(); 
        if(desc1 == null) desc1 = "";
        else desc1 = desc1.matches(".*\\w.*") ? desc1 : "";
        String desc2 = afterSav.getDescription(); 
        if(desc2 == null) desc2 = "";
        else desc2 = desc2.matches(".*\\w.*") ? desc2 : "";
        String scop1 = dep.getScope(); 
        if(scop1 == null) scop1 = "";
        else scop1 = scop1.matches(".*\\w.*") ? scop1 : "";
        String scop2 = afterSav.getScope(); 
        if(scop2 == null) scop2 = "";
        else scop2 = scop2.matches(".*\\w.*") ? scop2 : "";
        String opt1 = dep.getOptional(); 
        if(opt1 == null) opt1 = "";
        else opt1 = opt1.matches(".*\\w.*") ? opt1 : "";
        String opt2 = afterSav.getOptional(); 
        if(opt2 == null) opt2 = "";
        else opt2 = opt2.matches(".*\\w.*") ? opt2 : "";
        
//        System.out.println(dep1.equals(dep2));
//        System.out.println(grop1.equals(grop2));
//        System.out.println(artf1.equals(artf2));
//        System.out.println(ver1.equals(ver2));
//        System.out.println(desc1.equals(desc2));
//        System.out.println(scop1.equals(scop2));
//        System.out.println(opt1.equals(opt2));
        
        boolean b1 = (dep1.equals(dep2));
        boolean b2 =(grop1.equals(grop2));
        boolean b3 =(artf1.equals(artf2));
        boolean b4 =(ver1.equals(ver2));
        boolean b5 =(desc1.equals(desc2));
        boolean b6 =(scop1.equals(scop2));
        boolean b7 =(opt1.equals(opt2));

       if(b1 && b2 && b3 && b4 && b5 && b6 && b7){
//           System.out.println("TRUE OP");
            return true;
       }
       else{
//            System.out.println("FALSE OP");
            return false;
       }
    }
}
