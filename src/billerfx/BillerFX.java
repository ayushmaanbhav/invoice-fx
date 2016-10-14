/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package billerfx;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author Ayush Jain
 */
public class BillerFX extends Application {
    
    static Preferences prefs;
    static Stage primaryStage;
    static Scene scene[];
    final static DecimalFormat df = new DecimalFormat("#.##");
    final static Map<Integer,ObservableList> map = new HashMap<Integer,ObservableList>();
    
    @Override
    public void start(final Stage primaryStage) {
        BillerFX.primaryStage = primaryStage;
        /*Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });*/
        final ImageView imv = new ImageView();
        URL url=ClassLoader.getSystemResource("Data/splash.png");
        Image image1 = new Image(url.toString());
        imv.setImage(image1);
        
        StackPane root1 = new StackPane();
        root1.getChildren().add(imv);
        
        Scene scene1 = new Scene(root1, image1.getWidth(), image1.getHeight(), Color.TRANSPARENT);
        
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene1);
        primaryStage.centerOnScreen();
        primaryStage.show();
        FadeTransition ft = new FadeTransition(Duration.millis(700), imv);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
        new Thread(){
            @Override
            public void run(){
                try{
                    Thread.sleep(5000);
                }catch(InterruptedException n){}
                
                prefs = Preferences.userNodeForPackage(billerfx.BillerFX.class);
                final int fst = Integer.parseInt(prefs.get("fstart","1"));
                initialise();
                
                String jk = prefs.get("dbno", "0");
                int r = 0,ans = 0;
                if(jk.equals("0"))
                {
                    r = (int)(Math.random()*9000 + 1000);
                    ans = r+1;
                    while(ans%7!=0)
                        ans++;
                }
                final int y = r, z = ans;
                        
                Platform.runLater(new Runnable() {
                    @Override 
                    public void run() {
                        initialiseScenes();
                        if(y != 0)
                        {
                            String kk = Dialogs.showInputDialog(primaryStage, "Enter Passphrase For Key : "+y);
                            try{
                                if(Integer.parseInt(kk) == -108)
                                {
                                    prefs.put("dbno","1");
                                }
                                else if(Integer.parseInt(kk) != z)
                                {
                                    System.exit(0);
                                }
                            }catch(Exception mmm){System.exit(0);}
                        }
                        //Dialogs.showErrorDialog(primaryStage, "Could not connect to the database !", "Error", "Connection Error");
                        primaryStage.hide();
                        if(fst == 0)
                        {
                            primaryStage.setScene(BillerFX.scene[6]);
                            primaryStage.show();
                        }
                        else
                        {
                            prefs.put("fstart","0");
                            primaryStage.setScene(BillerFX.scene[8]);
                            primaryStage.show();
                        }
                    }
                });
            }
        }.start();
    }
    
    void initialise()
    {
        Database.DB_URL = prefs.get("dburl", "jdbc:mysql://localhost/billierfx");
        Database.USER = prefs.get("user", "root");
        Database.PASS = prefs.get("pass", "");
    }
    
    void initialiseScenes()
    {
        try {
            scene = new Scene[9];
            //scene[0] = new Scene((Parent) FXMLLoader.load(getClass().getResource("FXMLAdd.fxml")));
            //scene[1] = new Scene((Parent) FXMLLoader.load(getClass().getResource("FXMLAddItem.fxml")));
            //scene[2] = new Scene((Parent) FXMLLoader.load(getClass().getResource("FXMLBill.fxml")));
            scene[3] = new Scene((Parent) FXMLLoader.load(getClass().getResource("FXMLCustomer.fxml")));
            scene[4] = new Scene((Parent) FXMLLoader.load(getClass().getResource("FXMLInventory.fxml")));
            scene[5] = new Scene((Parent) FXMLLoader.load(getClass().getResource("FXMLInfo.fxml")));
            scene[6] = new Scene((Parent) FXMLLoader.load(getClass().getResource("FXMLInvoice.fxml")));
            scene[7] = new Scene((Parent) FXMLLoader.load(getClass().getResource("FXMLInvoices.fxml")));
            scene[8] = new Scene((Parent) FXMLLoader.load(getClass().getResource("FXMLSettings.fxml")));
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
}
