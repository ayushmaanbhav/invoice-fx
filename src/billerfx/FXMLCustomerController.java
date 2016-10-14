/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package billerfx;

import static billerfx.BillerFX.primaryStage;
import static billerfx.BillerFX.scene;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Ayush Jain
 */
public class FXMLCustomerController implements Initializable {
    
    @FXML private TableColumn t1c1,t1c2,t1c3,t1c4,t1c5,t1c6;
    @FXML private TableView t1;
    @FXML private TextField butt6;
    @FXML private ChoiceBox cb1;
    
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cb1.setValue("Name");
        cb1.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue ob, Number val, Number newval)
            {
                String[] s = {"Name","Phone","Email","Address"};
                initialiseCustomer(s[newval.intValue()], butt6.getText());
            }
        });
        butt6.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            { 
                initialiseCustomer(cb1.getValue().toString(), butt6.getText());
            }
        });
        initialiseCustomer(null, null);
    }    
    
    void initialiseCustomer(final String a, final String b)
    {
        new Thread(){
            @Override
            public void run()
            {
                final ObservableList s = Database.getCustomers(a, b);
                Platform.runLater(new Runnable() {                    
                    @Override 
                    public void run() {
                        if(s == null)
                            Dialogs.showErrorDialog(primaryStage, "Could not connect to the database !", "Error", "Connection Error");
                        else
                        {
                            t1c1.setCellValueFactory(new PropertyValueFactory<Customer,String>("id"));
                            t1c2.setCellValueFactory(new PropertyValueFactory<Customer,String>("name"));
                            t1c3.setCellValueFactory(new PropertyValueFactory<Customer,String>("phone"));
                            t1c4.setCellValueFactory(new PropertyValueFactory<Customer,String>("email"));
                            t1c5.setCellValueFactory(new PropertyValueFactory<Customer,String>("address"));
                            t1c6.setCellValueFactory(new PropertyValueFactory<Customer,String>("visited"));
                            t1.setItems(s);
                        }
                    }
                });
            }
        }.start();
    }
    
    @FXML
    public void searchCustomers(ActionEvent event)
    {
        initialiseCustomer(cb1.getValue().toString(), butt6.getText());
    }
    
    @FXML void clear(ActionEvent event)
    {
        butt6.clear();
        initialiseCustomer(null, null);
    }
    
}
