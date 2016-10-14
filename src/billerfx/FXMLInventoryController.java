/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package billerfx;

import static billerfx.BillerFX.df;
import static billerfx.BillerFX.primaryStage;
import static billerfx.BillerFX.scene;
import billerfx.Dialogs.DialogResponse;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Ayush Jain
 */
public class FXMLInventoryController implements Initializable {
    
    @FXML private TableColumn t1c1,t1c2,t1c3,t1c4,t1c5,t1c6,t1c7,t1c8,t1c9,t1c10;
    @FXML private TableView t1;
    @FXML private TextField butt6;
    @FXML private ChoiceBox cb1;
    EventHandler evhndlr;
    
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
        evhndlr = new EventHandler<CellEditEvent<Item, String>>() {
            @Override
            public void handle(CellEditEvent<Item, String> t) {
                final String ss = t.getTableColumn().getText().toLowerCase();
                String gg1 = t.getNewValue();
                final String hhh = t.getOldValue();
                final Item item = (Item) t.getTableView().getItems().get(t.getTablePosition().getRow());
                final TableColumn pl = t.getTableColumn();
                
                try{
                    if(ss.equals("price") || ss.equals("discount"))
                        gg1 = df.format(BillerFX.round(Double.parseDouble(gg1),2));
                } catch(Exception e)
                { 
                    item.name.set(hhh); 
                    pl.setVisible(false);
                    pl.setVisible(true);
                    return ;
                }
                final String gg = gg1;
                
                new Thread(){
                    @Override
                    public void run()
                    {
                        boolean s1 = false;
                        if(ss.equals("price")&&(item.type.get().equalsIgnoreCase("drinks")||item.category.get().equalsIgnoreCase("drinks")))
                            s1 = Database.updateColumnInDB("drinks", "id", item.getId(), "size", item.getGlasssize(), ss, gg);
                        else if(ss.indexOf("glass")!=-1 && (item.type.get().equalsIgnoreCase("drinks")||item.category.get().equalsIgnoreCase("drinks")))
                            s1 = Database.updateColumnInDB("drinks", "id", item.getId(), "price", item.getPrice(), "size", gg);
                        else if(ss.indexOf("available")!=-1)
                            s1 = Database.updateColumnInDB("items", "id", item.getId(), null, null, "available", gg);
                        else
                            s1 = Database.updateColumnInDB("items", "id", item.getId(), null, null, ss, gg);
                        final boolean s = s1;
                        Platform.runLater(new Runnable() {                    
                            @Override 
                            public void run() {
                                if(!s)
                                {
                                    Dialogs.showErrorDialog(primaryStage, "Could not connect to the database !", "Error", "Connection Error");
                                    /*item.name.set(hhh);
                                    pl.setVisible(false);
                                    pl.setVisible(true);*/
                                    initialiseInventory(cb1.getValue().toString(), butt6.getText());
                                }
                                else
                                {
                                    /*item.name.set(gg);
                                    pl.setVisible(false);
                                    pl.setVisible(true);*/
                                    initialiseInventory(cb1.getValue().toString(), butt6.getText());
                                }
                            }
                        });
                    }
                }.start();
            }
        };
        cb1.setValue("Name");
        cb1.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue ob, Number val, Number newval)
            {
                String[] s = {"id","name","type","category","price"};
                initialiseInventory(s[newval.intValue()], butt6.getText());
            }
        });
        butt6.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            { 
                initialiseInventory(cb1.getValue().toString(), butt6.getText());
            }
        });
        t1c2.setCellFactory(TextFieldTableCell.forTableColumn());
        t1c2.setOnEditCommit(evhndlr);
        t1c5.setCellFactory(TextFieldTableCell.forTableColumn());
        t1c5.setOnEditCommit(evhndlr);
        t1c6.setCellFactory(TextFieldTableCell.forTableColumn());
        t1c6.setOnEditCommit(evhndlr);
        t1c9.setCellFactory(TextFieldTableCell.forTableColumn());
        t1c9.setOnEditCommit(evhndlr);
        t1c10.setCellFactory(TextFieldTableCell.forTableColumn());
        t1c10.setOnEditCommit(evhndlr);
        initialiseInventory(null, null);
    }    
    
    void initialiseInventory(final String a, final String b)
    {
        new Thread(){
            @Override
            public void run()
            {
                final ObservableList s = Database.getInventory(a, b);
                Platform.runLater(new Runnable() {                    
                    @Override 
                    public void run() {
                        if(s == null)
                            Dialogs.showErrorDialog(primaryStage, "Could not connect to the database !", "Error", "Connection Error");
                        else
                        {
                            t1c1.setCellValueFactory(new PropertyValueFactory<Item,String>("id"));
                            t1c2.setCellValueFactory(new PropertyValueFactory<Item,String>("name"));
                            t1c3.setCellValueFactory(new PropertyValueFactory<Item,String>("type"));
                            t1c4.setCellValueFactory(new PropertyValueFactory<Item,String>("category"));
                            t1c5.setCellValueFactory(new PropertyValueFactory<Item,String>("discount"));
                            t1c6.setCellValueFactory(new PropertyValueFactory<Item,String>("price"));
                            t1c7.setCellValueFactory(new PropertyValueFactory<Item,String>("soldmonth"));
                            t1c8.setCellValueFactory(new PropertyValueFactory<Item,String>("totalsold"));
                            t1c9.setCellValueFactory(new PropertyValueFactory<Item,String>("glasssize"));
                            t1c10.setCellValueFactory(new PropertyValueFactory<Item,String>("available"));
                            t1.setItems(s);
                        }
                    }
                });
            }
        }.start();
    }
    
    @FXML
    public void searchInventory(ActionEvent event)
    {
        initialiseInventory(cb1.getValue().toString(), butt6.getText());
    }
    
    @FXML 
    void clear(ActionEvent event)
    {
        butt6.clear();
        initialiseInventory(null, null);
    }
    
    Stage additem = null;
    
    @FXML 
    void showAddItem(ActionEvent event)
    {
        if(additem == null || !additem.isShowing())
        {
            try {
                additem = new Stage();
                additem.initOwner(primaryStage);
                additem.initModality(Modality.WINDOW_MODAL);
                additem.initStyle(StageStyle.UNDECORATED);
                additem.setScene(new Scene((Parent) FXMLLoader.load(getClass().getResource("FXMLAdd.fxml"))));
                additem.centerOnScreen();
                additem.showAndWait();
                initialiseInventory(cb1.getValue().toString(), butt6.getText());
            } catch (IOException ex) {
            }            
        }
    }
    
    @FXML 
    void deleteItem(ActionEvent event)
    {
        final Item i = (Item) t1.getSelectionModel().getSelectedItem();
        if(i == null)
            Dialogs.showErrorDialog(primaryStage, "No item selected !", "Error", "Error");
        else
        {
            DialogResponse reply = Dialogs.showConfirmDialog(primaryStage, "Are you sure ?");
            if(reply == DialogResponse.YES)
            {
                new Thread(){
                    @Override
                    public void run()
                    {
                        final boolean s = Database.deleteItemFromInventory(i.getId());
                        Platform.runLater(new Runnable() {                    
                            @Override 
                            public void run() {
                                if(!s)
                                    Dialogs.showErrorDialog(primaryStage, "Could not connect to the database !", "Error", "Connection Error");
                                else
                                    initialiseInventory(cb1.getValue().toString(), butt6.getText());
                            }
                        });
                    }
                }.start();
            }
        }
    }
}
