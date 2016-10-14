/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package billerfx;

import static billerfx.BillerFX.primaryStage;
import static billerfx.BillerFX.scene;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Ayush Jain
 */
public class FXMLInvoiceController implements Initializable {
    
    static ObservableList stages,stages2;
    @FXML ListView lv;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.exit(0);
    }
    
    @FXML
    private void handleButtonAction2(ActionEvent event) {
        BillerFX.primaryStage.setIconified(true);
    }
    
    @FXML
    private void handleButtonAction3(ActionEvent event) {
        primaryStage.setScene(scene[8]);
    }
    
    @FXML
    private void handleButtonAction4(ActionEvent event) {
        primaryStage.setScene(scene[6]);
    }
    
    @FXML
    private void handleButtonAction5(ActionEvent event) {
        primaryStage.setScene(scene[3]);
    }
    
    @FXML
    private void handleButtonAction51(ActionEvent event) {
        primaryStage.setScene(scene[7]);
    }
    
    @FXML
    private void handleButtonAction61(ActionEvent event) {
        primaryStage.setScene(scene[4]);
    }
    
    @FXML
    private void handleButtonAction71(ActionEvent event) {
        primaryStage.setScene(scene[5]);
    }
    
    @FXML
    private void handleButtonAction8(ActionEvent event) {
        final String i = (String) lv.getSelectionModel().getSelectedItem();
        if(i == null)
            Dialogs.showErrorDialog(primaryStage, "No item selected !", "Error", "Error");
        else
        {
            int ij = lv.getSelectionModel().getSelectedIndex();
            if(!((Stage)stages2.get(ij)).isShowing())
                ((Stage)stages2.get(ij)).show();
            else
                ((Stage)stages2.get(ij)).toFront();
        }
    }
    
    @FXML
    private void handleButtonAction9(ActionEvent event) {
        final String i = (String) lv.getSelectionModel().getSelectedItem();
        if(i == null)
            Dialogs.showErrorDialog(primaryStage, "No item selected !", "Error", "Error");
        else
        {
            int ij = lv.getSelectionModel().getSelectedIndex();
            ((Stage)stages2.get(ij)).hide();
            stages.remove(ij);
            stages2.remove(ij);
        }
    }
    
    @FXML
    private void handleButtonAction7(ActionEvent event) {
        try {
            String result = Dialogs.showInputDialog(primaryStage, "Enter table number. eg: 1, 2, 3 etc");
            if(result == null || result.trim().equals(""))
                return;
            int op = Integer.parseInt(result);
            for(int i=0; i<stages.size();i++)
            {
                if(Integer.parseInt(((String)stages.get(i)).substring(22)) == Integer.parseInt(result))
                    return;
            }
            Stage additem = new Stage();
            additem.setTitle(result);
            //additem.initOwner(primaryStage);
            additem.initModality(Modality.NONE);
            additem.initStyle(StageStyle.UNDECORATED);
            additem.setScene(new Scene((Parent) FXMLLoader.load(getClass().getResource("FXMLBill.fxml"))));
            additem.setX(primaryStage.getX()+80);
            additem.setY(primaryStage.getY()+80);
            additem.show();
            result = "Invoice for Table No. "+result;
            stages.add(result);
            stages2.add(additem);
            lv.setItems(stages);
        } catch (IOException ex) {
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stages = FXCollections.observableArrayList();
        stages2 = FXCollections.observableArrayList();
        lv.setItems(stages);
        stages.addListener(new ListChangeListener(){
            @Override
            public void onChanged(ListChangeListener.Change change) {
                lv.setItems(stages);
            }
        });
    }  
}
