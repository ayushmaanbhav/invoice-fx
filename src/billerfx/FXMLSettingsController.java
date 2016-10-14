/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package billerfx;

import static billerfx.BillerFX.prefs;
import static billerfx.BillerFX.primaryStage;
import static billerfx.BillerFX.scene;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Ayush Jain
 */
public class FXMLSettingsController implements Initializable {
    
    @FXML private TextField e1,e3,e4,e5,e6,e7,e8,e9,e10,e13,e14,e15,e16,e17,e19,e20,e21;
    @FXML private CheckBox e11,e12,e18;
    @FXML private TextArea e2;
    @FXML TableView t1,t2;
    @FXML TableColumn t1c1,t2c1;
    
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
    private void add1(ActionEvent event) {
        final String res = Dialogs.showInputDialog(primaryStage, "Enter Type: ");
        if(res == null || res.equals(""))
            return;
        new Thread(){
            @Override
            public void run()
            {
                Database.add("types",res);
                Platform.runLater(new Runnable() {                    
                    @Override 
                    public void run() {
                        loadTable1();
                    }    
                });
            }
        }.start();
    }
    
    @FXML
    private void add2(ActionEvent event) {
        final String res = Dialogs.showInputDialog(primaryStage, "Enter Category: ");
        if(res == null || res.equals(""))
            return;
        new Thread(){
            @Override
            public void run()
            {
                Database.add("category",res);
                Platform.runLater(new Runnable() {                    
                    @Override 
                    public void run() {
                        loadTable2();
                    }    
                });
            }
        }.start();
    }
    
    @FXML
    private void remove1(ActionEvent event) {
        final Type t = (Type) t1.getSelectionModel().getSelectedItem();
        if(t == null)
            return;
        new Thread(){
            @Override
            public void run()
            {
                Database.remove("types",t.getName());
                Platform.runLater(new Runnable() {                    
                    @Override 
                    public void run() {
                        loadTable1();
                    }    
                });
            }
        }.start();
    }
    
    @FXML
    private void remove2(ActionEvent event) {
        final Type t = (Type) t2.getSelectionModel().getSelectedItem();
        if(t == null)
            return;
        new Thread(){
            @Override
            public void run()
            {
                Database.remove("category",t.getName());
                Platform.runLater(new Runnable() {                    
                    @Override 
                    public void run() {
                        loadTable2();
                    }    
                });
            }
        }.start();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initialiseSettings();
    }   
    
    void loadTable1()
    {
        new Thread(){
            @Override
            public void run()
            {
                final ObservableList ob = Database.get("types");
                Platform.runLater(new Runnable() {                    
                    @Override 
                    public void run() {
                        t1c1.setCellValueFactory(new PropertyValueFactory<Type,String>("name"));
                        t1.setItems(ob);
                    }    
                });
            }
        }.start();
    }
    
    void loadTable2()
    {
        new Thread(){
            @Override
            public void run()
            {
                final ObservableList ob = Database.get("category");
                Platform.runLater(new Runnable() {                    
                    @Override 
                    public void run() {
                        t2c1.setCellValueFactory(new PropertyValueFactory<Type,String>("name"));
                        t2.setItems(ob);
                    }    
                });
            }
        }.start();
    }
    
    void initialiseSettings()
    {
        new Thread(){
            @Override
            public void run()
            {
                final String s[] = Database.getCurrentSettings();
                Platform.runLater(new Runnable() {                    
                    @Override 
                    public void run() {
                        if(s == null)
                        {
                            e4.setText(Database.DB_URL);
                            e5.setText(Database.USER);
                            e6.setText(Database.PASS);
                            Dialogs.showErrorDialog(primaryStage, "Could not connect to the database !", "Error", "Connection Error");
                        }
                        else
                        {
                            e1.setText(s[0]);
                            e2.setText(s[1]);
                            e3.setText(s[2]);
                            e4.setText(Database.DB_URL);
                            e5.setText(Database.USER);
                            e6.setText(Database.PASS);
                            e7.setText(s[3]);
                            e8.setText(s[4]);
                            e9.setText(s[5]);
                            e10.setText(s[6]);
                            e11.setSelected(s[7].equals("1"));
                            e12.setSelected(s[8].equals("1"));
                            e13.setText(s[9]);
                            e14.setText(s[10]);
                            e15.setText(s[11]);
                            e16.setText(s[12]);
                            e17.setText(s[13]);
                            e18.setSelected(s[14].equals("1"));
                            Database.adminpass = s[15];
                            loadTable1();
                            loadTable2();
                        }
                    }
                });
            }
        }.start();
    }
    
    @FXML
    private void saveChanges(ActionEvent event) {
        final String s[] = new String[15];
        s[0] = e1.getText();
        s[1] = e2.getText();
        s[2] = e3.getText();
        s[3] = e7.getText();
        s[4] = e8.getText();
        s[5] = e9.getText();
        s[6] = e10.getText();
        s[7] = e11.isSelected()?"1":"0";
        s[8] = e12.isSelected()?"1":"0";
        s[9] = e13.getText();
        s[10] = e14.getText();
        s[11] = e15.getText();
        s[12] = e16.getText();
        s[13] = e17.getText();
        s[14] = e18.isSelected()?"1":"0";
        Database.DB_URL = e4.getText();
        Database.USER = e5.getText();
        Database.PASS = e6.getText();
        
        //if(BillerFX.con.e19.getText() != null)
        
        new Thread(){
            @Override
            public void run()
            {
                prefs.put("dburl", Database.DB_URL);
                prefs.put("user", Database.USER);
                prefs.put("pass", Database.PASS);
                final boolean result = Database.setCurrentSettings(s);
                Platform.runLater(new Runnable() {                    
                    @Override 
                    public void run() {
                        if(result)
                            Dialogs.showInformationDialog(primaryStage, "Settings saved to the database !", "Info", "Settings Saved");
                        else
                            Dialogs.showErrorDialog(primaryStage, "Could not connect to the database !", "Error", "Connection Error");
                    }
                });
            }
        }.start();
    }
}
