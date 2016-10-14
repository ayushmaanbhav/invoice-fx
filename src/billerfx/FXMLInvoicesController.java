/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package billerfx;

import static billerfx.BillerFX.primaryStage;
import static billerfx.BillerFX.scene;
import com.sun.prism.impl.Disposer.Record;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 *
 * @author Ayush Jain
 */
public class FXMLInvoicesController implements Initializable {
    
    @FXML private TableColumn t1c1,t1c2,t1c3,t1c4,t1c5,t1c6,t1c7;
    @FXML private TableView t1;
    @FXML private TextField butt6;
    @FXML private ChoiceBox cb1;
    
    private class ButtonCell extends TableCell<Record, Boolean> {
        final Button cellButton = new Button("Open");
        ButtonCell(){            
            cellButton.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent t) {
                    Invoice in = (Invoice) getTableRow().getItem();
                    File f = in.getFile();
                    String fileName = f.getAbsolutePath();
                    String[] commands = {"cmd", "/c", "start", "\"Invoice\"",fileName};
                    try {
                        Runtime.getRuntime().exec(commands);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
 
        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                setGraphic(cellButton);
            }
        }
    }
    
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
                String[] s = {"id","name","date"};
                initialiseInvoices(s[newval.intValue()], butt6.getText());
            }
        });
        butt6.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            { 
                initialiseInvoices(cb1.getValue().toString(), butt6.getText());
            }
        });
        initialiseInvoices(null, null);
    }    
    
    void initialiseInvoices(final String a, final String b)
    {
        new Thread(){
            @Override
            public void run()
            {
                final ObservableList s = Database.getInvoices(a, b);
                Platform.runLater(new Runnable() {                    
                    @Override 
                    public void run() {
                        if(s == null)
                            Dialogs.showErrorDialog(primaryStage, "Could not connect to the database !", "Error", "Connection Error");
                        else
                        {
                            t1c1.setCellValueFactory(new PropertyValueFactory<Invoice,String>("id"));
                            t1c2.setCellValueFactory(new PropertyValueFactory<Invoice,String>("name"));
                            t1c3.setCellValueFactory(new PropertyValueFactory<Invoice,String>("phone"));
                            t1c4.setCellValueFactory(new PropertyValueFactory<Invoice,String>("email"));
                            t1c5.setCellValueFactory(new PropertyValueFactory<Invoice,String>("address"));
                            t1c6.setSortable(false);
                            t1c6.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Record, Boolean>,ObservableValue<Boolean>>() {
                                @Override
                                public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> p) {
                                    return new SimpleBooleanProperty(p.getValue() != null);
                                }
                            });
                            t1c6.setCellFactory(new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {
                                @Override
                                public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> p) {
                                    return new ButtonCell();
                                } 
                            });
                            t1c7.setCellValueFactory(new PropertyValueFactory<Invoice,String>("date"));
                            t1.setItems(s);
                        }
                    }
                });
            }
        }.start();
    }
    
    @FXML
    public void searchInvoices(ActionEvent event)
    {
        initialiseInvoices(cb1.getValue().toString(), butt6.getText());
    }
    
    @FXML void clear(ActionEvent event)
    {
        butt6.clear();
        initialiseInvoices(null, null);
    }
    
    @FXML 
    void deleteItem(ActionEvent event)
    {
        final Invoice i = (Invoice) t1.getSelectionModel().getSelectedItem();
        if(i == null)
            Dialogs.showErrorDialog(primaryStage, "No item selected !", "Error", "Error");
        else
        {
            Dialogs.DialogResponse reply = Dialogs.showConfirmDialog(primaryStage, "Are you sure ?");
            if(reply == Dialogs.DialogResponse.YES)
            {
                new Thread(){
                    @Override
                    public void run()
                    {
                        final boolean s = Database.deleteItemFromInvoices(i.getId());
                        Platform.runLater(new Runnable() {                    
                            @Override 
                            public void run() {
                                if(!s)
                                    Dialogs.showErrorDialog(primaryStage, "Could not connect to the database !", "Error", "Connection Error");
                                else
                                    initialiseInvoices(cb1.getValue().toString(), butt6.getText());
                            }
                        });
                    }
                }.start();
            }
        }
    }
}
