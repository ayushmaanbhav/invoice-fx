/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package billerfx;

import static billerfx.BillerFX.map;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 *
 * @author Ayush Jain
 */
public class FXMLAddItemController implements Initializable {
    
    @FXML ImageView imv;
    @FXML ComboBox cb;
    @FXML Button ok;
    @FXML Label a,b,c,d,e;
    @FXML ChoiceBox chb;
    @FXML TextField tf;
    ObservableList obl5;
    Item it;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        ((Stage)imv.getParent().getScene().getWindow()).hide();
    }
    
    @FXML
    private void handleButtonAction2(ActionEvent event) {
        Stage pp = ((Stage)imv.getParent().getScene().getWindow());
        pp.hide();
        int q = Integer.parseInt(tf.getText());
        double p = Double.parseDouble(e.getText());
        double total = (p - (Double.parseDouble(it.getDiscount())*p)/100)*q;
        total = BillerFX.round(total, 2);
        String ss = BillerFX.df.format(total);
        Item2 ipp = new Item2(it.getId(),it.getName(),it.getType(),it.getCategory(),it.getDiscount(),it.getPrice(),q+"",ss);
        if(it.getType().equalsIgnoreCase("drinks") || it.getCategory().equalsIgnoreCase("drinks"))
        {
            ipp.price.set(e.getText());
            ipp.glasssize = (String) chb.getSelectionModel().getSelectedItem();
            ipp.glasssize = ipp.glasssize.substring(0,ipp.glasssize.length() - 3);
        }
        map.get(Integer.parseInt(pp.getTitle())).add(ipp);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue ob, Number val, Number newval)
            {
                e.setText(((Drink)obl5.get(newval.intValue())).getPrice());
            }
        });
        cb.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if(newValue.contains(":") && newValue.contains("#"))
                {
                    ObservableList obl3 = cb.getItems();
                    for(int i=0; i<obl3.size(); i++)
                    {
                        if(((String)obl3.get(i)).equals(newValue))
                        {
                            setFields(newValue.substring(newValue.indexOf('#')+1, newValue.indexOf(':')));
                            return;
                        }
                    }
                }
                removeFields();
                new Thread(){
                    @Override
                    public void run()
                    {
                        ObservableList obl4;
                        try{
                            int id = Integer.parseInt(cb.getEditor().getText());
                            obl4 = Database.getInventory1("id", id+"");
                        }catch(Exception mm){
                            obl4 = Database.getInventory1("name", cb.getEditor().getText());
                        }
                        final ObservableList obl = obl4;
                        Platform.runLater(new Runnable() {                    
                            @Override 
                            public void run() {      
                                if(obl == null)
                                {
                                    Dialogs.showErrorDialog(((Stage)imv.getParent().getScene().getWindow()), "Could not connect to the database !", "Error", "Connection Error");
                                    return;
                                }
                                ObservableList obl2 = FXCollections.observableArrayList();
                                for(int i=0; i<obl.size(); i++)
                                {
                                    Item ii = ((Item)obl.get(i));
                                    obl2.add("#"+ii.getId()+": "+ii.getName());
                                }
                                cb.setItems(obl2);
                                if(obl.size() == 1)
                                {
                                    Item ii = ((Item)obl.get(0));
                                    cb.getEditor().setText("#"+ii.getId()+": "+ii.getName());
                                    setFields(ii.getId());
                                    return;
                                }
                                Platform.runLater(new Runnable() {                    
                                    @Override 
                                    public void run() {
                                        cb.show();
                                    }
                                });
                            }
                        });
                    }
                }.start();
            }
        });
    }  
    
    void setFields(final String id)
    {
        new Thread(){
            @Override
            public void run()
            {
                ObservableList obl = Database.getInventory1("id", id);
                if(obl.size()!=1)
                    return;
                it = (Item)obl.get(0);
                ObservableList dr = null;
                if(it.getType().equalsIgnoreCase("drinks") || it.getCategory().equalsIgnoreCase("drinks"))
                    dr = Database.getInventory2(id);
                obl5 = dr;
                Platform.runLater(new Runnable() {                    
                    @Override 
                    public void run() {
                        a.setText(it.getId());
                        b.setText(it.getName());
                        c.setText(it.getType());
                        d.setText(it.getCategory());
                        if(obl5 == null)
                        {
                            e.setText(it.getPrice());
                            chb.setDisable(true);
                        }
                        else
                        {
                            ObservableList obl3 = FXCollections.observableArrayList();
                            for(int i=0; i<obl5.size(); i++)
                            {
                                Drink ii = ((Drink)obl5.get(i));
                                if(Integer.parseInt(ii.getGlasssize()) <= Integer.parseInt(it.getAvailable()))
                                    obl3.add(ii.getGlasssize()+" ml");
                            }
                            if(obl3.size() < obl5.size())
                            {
                                Dialogs.showErrorDialog(((Stage)imv.getParent().getScene().getWindow()), "It seems that the drink is not available or is running low. Please update the availablity of the drink by going to the Inventory section.", "Error", "Error");
                            }
                            chb.setItems(obl3);
                            chb.setDisable(false);
                            chb.setVisible(false);
                            chb.setVisible(true);
                            e.setText("");
                        }
                        ok.setDisable(false);
                        tf.setDisable(false);
                    }
                });
            }
        }.start();
    }
    
    void removeFields()
    {
        ok.setDisable(true);
        tf.setDisable(true);
        chb.setDisable(true);
    }
}
