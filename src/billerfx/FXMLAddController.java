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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 *
 * @author Ayush Jain
 */
public class FXMLAddController implements Initializable {
    
    @FXML Label l1,j,k,l,ba;
    @FXML ChoiceBox cb1, cb2;
    @FXML TextField a,b,c,d,e,g;
    @FXML TableView f;
    @FXML Button h,i;
    @FXML TableColumn ppl,gpl;
    ObservableList obl;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        final ActionEvent pp = event;
        new Thread(){
            @Override
            public void run()
            {
                boolean ss=false;
                if(cb1.getValue().toString().equalsIgnoreCase("drinks") || cb2.getValue().toString().equalsIgnoreCase("drinks"))
                {
                    String id = l1.getText();
                    String name = a.getText();
                    String type = cb1.getValue().toString();
                    String cate = cb2.getValue().toString();
                    String dis = e.getText();
                    ObservableList ob = f.getItems();
                    String avai = g.getText();
                    ss = Database.addItem(id, name, type, cate, dis, "0", "0", "0", avai);
                    if(ss)
                        ss = Database.addDrink(id, ob);
                }
                else
                {
                    String id = l1.getText();
                    String name = a.getText();
                    String type = cb1.getValue().toString();
                    String cate = cb2.getValue().toString();
                    String dis = e.getText();
                    String price = b.getText();
                    ss = Database.addItem(id, name, type, cate, dis, price, "0", "0", "0");                    
                }
                final boolean s = ss;
                Platform.runLater(new Runnable() {                    
                    @Override 
                    public void run() {
                        if(!s)
                            Dialogs.showErrorDialog((Stage) a.getParent().getScene().getWindow(), "Could not connect to the database !", "Error", "Connection Error");
                        ((Button)pp.getSource()).getParent().getScene().getWindow().hide();
                    }
                });
            }
        }.start();
    }
    
    @FXML
    private void handleButtonAction2(ActionEvent event) {
        ((Button)event.getSource()).getParent().getScene().getWindow().hide();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cb1.setDisable(true);
        cb2.setDisable(true);
        
        c.setDisable(true);
        d.setDisable(true);
        f.setDisable(true);
        g.setDisable(true);
        h.setDisable(true);
        i.setDisable(true);
        j.setDisable(true);
        k.setDisable(true);
        l.setDisable(true);
        
        ppl.setCellValueFactory(new PropertyValueFactory<Drink,String>("glasssize"));
        gpl.setCellValueFactory(new PropertyValueFactory<Drink,String>("price"));
        obl = FXCollections.observableArrayList();
        
        new Thread(){
            @Override
            public void run()
            {
                final int id = Database.getNextItemID();
                final ObservableList ob1 = Database.getTypes();
                final ObservableList ob2 = Database.getCategories();
                Platform.runLater(new Runnable() {                    
                    @Override 
                    public void run() {
                        if(id>=0)
                            l1.setText(id+"");
                        if(ob1!=null)
                            cb1.setItems(ob1);
                        if(ob2!=null)
                            cb2.setItems(ob2);
                        cb1.setDisable(false);
                        cb2.setDisable(false);
                        cb1.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
                            @Override
                            public void changed(ObservableValue ob, Number val, Number newval)
                            {
                                if(ob1.get(newval.intValue()).toString().equalsIgnoreCase("drinks") || (cb2.getValue()!=null && cb2.getValue().toString().equalsIgnoreCase("drinks")))
                                {
                                    c.setDisable(false);
                                    d.setDisable(false);
                                    f.setDisable(false);
                                    g.setDisable(false);
                                    h.setDisable(false);
                                    i.setDisable(false);
                                    j.setDisable(false);
                                    k.setDisable(false);
                                    l.setDisable(false);
                                    b.setDisable(true);
                                    ba.setDisable(true);
                                    
                                }
                                else
                                {
                                    c.setDisable(true);
                                    d.setDisable(true);
                                    f.setDisable(true);
                                    g.setDisable(true);
                                    h.setDisable(true);
                                    i.setDisable(true);
                                    j.setDisable(true);
                                    k.setDisable(true);
                                    l.setDisable(true);
                                    b.setDisable(false);
                                    ba.setDisable(false);
                                }
                            }
                        });
                        cb2.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
                            @Override
                            public void changed(ObservableValue ob, Number val, Number newval)
                            {
                                if(ob2.get(newval.intValue()).toString().equalsIgnoreCase("drinks") || (cb1.getValue()!=null && cb1.getValue().toString().equalsIgnoreCase("drinks")))
                                {
                                    c.setDisable(false);
                                    d.setDisable(false);
                                    f.setDisable(false);
                                    g.setDisable(false);
                                    h.setDisable(false);
                                    i.setDisable(false);
                                    j.setDisable(false);
                                    k.setDisable(false);
                                    l.setDisable(false);
                                    b.setDisable(true);
                                    ba.setDisable(true);
                                }
                                else
                                {
                                    c.setDisable(true);
                                    d.setDisable(true);
                                    f.setDisable(true);
                                    g.setDisable(true);
                                    h.setDisable(true);
                                    i.setDisable(true);
                                    j.setDisable(true);
                                    k.setDisable(true);
                                    l.setDisable(true);
                                    b.setDisable(false);
                                    ba.setDisable(false);
                                }
                            }
                        });
                    }
                });
            }
        }.start();
    }  
    
    @FXML
    private void handleButtonAction5(ActionEvent event) {
        ObservableList ob = f.getItems();
        ob.add(new Drink(c.getText(),d.getText()));
        f.setItems(ob);
    }
    
    @FXML
    private void handleButtonAction6(ActionEvent event) {
        int i = f.getSelectionModel().getSelectedIndex();
        if(i >=0 )
        {
            ObservableList ob = f.getItems();
            ob.remove(i);
            f.setItems(ob);
        }
        else
            Dialogs.showErrorDialog(primaryStage, "No item selected !", "Error", "Error");
    }
     
}
