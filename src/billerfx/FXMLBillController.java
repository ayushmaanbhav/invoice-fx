/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package billerfx;

import static billerfx.BillerFX.map;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Ayush Jain
 */
public class FXMLBillController implements Initializable {
    
    @FXML ImageView imv;
    @FXML Label a,b,c,h,i,j,k,l,m,aa,bb;
    @FXML TextField d,e,f,n;
    @FXML TextArea g;
    @FXML TableView t1;
    @FXML TableColumn t1c1,t1c2,t1c3,t1c4,t1c5,t1c6,t1c7,t1c8;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        imv.getParent().getScene().getWindow().hide();
    }
    
    @FXML
    private void handleButtonAction2(ActionEvent event) {
        ((Stage)imv.getParent().getScene().getWindow()).setIconified(true);
    }
    
    Stage additem = null;
    
    @FXML 
    void handleButtonAction51(ActionEvent event) //add
    {
        if(additem == null || !additem.isShowing())
        {
            try {
                Stage pp = ((Stage)imv.getParent().getScene().getWindow());
                additem = new Stage();
                additem.initOwner(pp);
                additem.initModality(Modality.WINDOW_MODAL);
                additem.initStyle(StageStyle.UNDECORATED);
                additem.setScene(new Scene((Parent) FXMLLoader.load(getClass().getResource("FXMLAddItem.fxml"))));
                additem.setX(pp.getX() + 200);
                additem.setY(pp.getY() + 200);
                additem.setTitle(pp.getTitle());
                additem.showAndWait();
                t1.setItems(map.get(Integer.parseInt(pp.getTitle())));
                calculateBill();
            } catch (IOException ex) {
            }            
        }
    }
    
    @FXML
    private void handleButtonAction52(ActionEvent event) { //remove
        final Stage pp = ((Stage)imv.getParent().getScene().getWindow());
        final Item2 ppp = (Item2) t1.getSelectionModel().getSelectedItem();
        if(ppp == null)
            Dialogs.showErrorDialog(pp, "No item selected !", "Error", "Error");
        else
        {
            Dialogs.DialogResponse reply = Dialogs.showConfirmDialog(pp, "Are you sure ?");
            if(reply == Dialogs.DialogResponse.YES)
            {
                map.get(Integer.parseInt(pp.getTitle())).remove(ppp);
                t1.setItems(map.get(Integer.parseInt(pp.getTitle())));
                calculateBill();
            }
        }
    }
    
    @FXML
    private void handleButtonAction53(ActionEvent event) { //print
        calculateBill();
        new Thread(){
            @Override
            public void run(){
                File fi = generatePDF();
                Invoice in = new Invoice(a.getText(),d.getText() == null ? "": d.getText(),e.getText() == null ? "": e.getText(),f.getText() == null ? "": f.getText(),g.getText() == null ? "": g.getText(),fi,b.getText());
                if(!Database.saveBill(in))
                {
                    Platform.runLater(new Runnable() {                    
                        @Override 
                        public void run() {
                            Dialogs.showErrorDialog(((Stage)imv.getParent().getScene().getWindow()), "Already Printed ! To print it again go to Invoices section.", "Error", "Error");                    
                        }
                    });
                    return;
                }
                ObservableList ob = map.get(Integer.parseInt(((Stage)imv.getParent().getScene().getWindow()).getTitle()));
                for(int i=0; i<ob.size(); i++)
                {
                    Item2 ii = (Item2) ob.get(i);
                    Item kk = (Item) Database.getInventory("id", ii.getId()).get(0);
                    if(ii.getType().equalsIgnoreCase("drinks") || ii.getCategory().equalsIgnoreCase("drinks"))
                    {
                        int avai = Integer.parseInt(kk.getAvailable()) - Integer.parseInt(ii.glasssize);
                        Database.updateColumnInDB("items", "id", ii.getId(), null, null, "available", avai+"");
                    }
                    int total = Integer.parseInt(kk.getTotalsold()) + Integer.parseInt(ii.getQuantity());
                    Database.updateColumnInDB("items", "id", ii.getId(), null, null, "total_sold", total+"");
                }
                String fileName = fi.getAbsolutePath();
                String[] commands = {"cmd", "/c", "start", "\"Invoice\"",fileName};
                try {
                    Runtime.getRuntime().exec(commands);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                Platform.runLater(new Runnable() {                    
                    @Override 
                    public void run() {
                        handleButtonAction54(null);
                    }
                });
            }
        }.start();
    }
    
    @FXML
    private void handleButtonAction54(ActionEvent event) { //discard
        Stage gg = ((Stage)imv.getParent().getScene().getWindow());
        gg.hide();
        int index = FXMLInvoiceController.stages2.indexOf(gg);
        FXMLInvoiceController.stages2.remove(index);
        FXMLInvoiceController.stages.remove(index);
    }
    
    private double xOffset = 0;
    private double yOffset = 0;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new Thread(){
            @Override
            public void run()
            {
                final int id = Database.getNextInvoiceID() + FXMLInvoiceController.stages.size();
                final String[] ll = Database.getCurrentSettings();               
                Platform.runLater(new Runnable() {                    
                    @Override 
                    public void run() {
                        if(imv.getParent().getScene().getWindow() == null)
                            return;
                        if(ll[7].equals("1"))
                        {
                            aa.setText(ll[3]);
                            k.setVisible(true);
                        }
                        else
                        {
                            aa.setText("");
                            k.setVisible(false);
                        }
                        if(ll[8].equals("1"))
                        {
                            bb.setText(ll[4]);
                            l.setVisible(true);
                        }
                        else
                        {
                            bb.setText("");
                            l.setVisible(false);
                        }
                        a.setText(id+"");
                        try{
                            c.setText(((Stage)imv.getParent().getScene().getWindow()).getTitle());
                        }catch(Exception nn){}
                        b.setText(Calendar.getInstance().get(Calendar.DATE)+"/"+(1+Calendar.getInstance().get(Calendar.MONTH))+"/"+Calendar.getInstance().get(Calendar.YEAR));                        
                        if(map.containsKey(Integer.parseInt(((Stage)imv.getParent().getScene().getWindow()).getTitle())))
                            map.remove(Integer.parseInt(((Stage)imv.getParent().getScene().getWindow()).getTitle()));
                        ObservableList obl = FXCollections.observableArrayList();
                        map.put(Integer.parseInt(((Stage)imv.getParent().getScene().getWindow()).getTitle()), obl);
                        imv.setOnMousePressed(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                xOffset = event.getSceneX();
                                yOffset = event.getSceneY();
                            }
                        });
                        imv.setOnMouseDragged(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                ((Stage)imv.getParent().getScene().getWindow()).setX(event.getScreenX() - xOffset);
                                ((Stage)imv.getParent().getScene().getWindow()).setY(event.getScreenY() - yOffset);
                            }
                        });
                        t1c1.setCellValueFactory(new PropertyValueFactory<Item2,String>("id"));
                        t1c2.setCellValueFactory(new PropertyValueFactory<Item2,String>("name"));
                        t1c3.setCellValueFactory(new PropertyValueFactory<Item2,String>("type"));
                        t1c4.setCellValueFactory(new PropertyValueFactory<Item2,String>("category"));
                        t1c5.setCellValueFactory(new PropertyValueFactory<Item2,String>("discount"));
                        t1c6.setCellValueFactory(new PropertyValueFactory<Item2,String>("price"));
                        t1c7.setCellValueFactory(new PropertyValueFactory<Item2,String>("quantity"));
                        t1c8.setCellValueFactory(new PropertyValueFactory<Item2,String>("total"));
                        t1.setItems(map.get(Integer.parseInt(((Stage)imv.getParent().getScene().getWindow()).getTitle())));
                        n.textProperty().addListener(new ChangeListener<String>() {
                            @Override
                            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
                            { 
                                calculateBill();
                            }
                        });
                    }
                });
            }
        }.start();
    }  
    
    void calculateBill()
    {
        new Thread(){
            @Override
            public void run()
            {
                String[] ll = Database.getCurrentSettings();
                double z = 0.0,y = 0.0;
                if(ll[7].equals("1"))
                    y = Double.parseDouble(ll[5]);
                if(ll[8].equals("1"))
                    z = Double.parseDouble(ll[6]);
                final double v = y, w = z;
                Platform.runLater(new Runnable() {                    
                    @Override 
                    public void run() {       
                        double total = 0.0, totalindis = 0.0, gt = 0.0;
                        double discount = 0.0;
                        double tax1 = v;
                        double tax2 = w;
                        ObservableList ob = t1.getItems();
                        for(int i=0; i<ob.size(); i++)
                        {
                            total += Double.parseDouble(((Item2)ob.get(i)).getTotal());
                        }
                        h.setText(BillerFX.df.format(total));
                        try{
                            discount = Double.parseDouble(n.getText());
                        }catch(Exception ml){
                            discount = 0.0;
                        }
                        discount = (total * discount)/100;
                        discount = BillerFX.round(discount, 2);
                        totalindis = total - discount;
                        i.setText(BillerFX.df.format(totalindis));
                        tax1 = (tax1 * total)/100;
                        tax1 = BillerFX.round(tax1, 2);
                        k.setText(BillerFX.df.format(tax1));
                        tax2 = (tax2 * total)/100;
                        tax2 = BillerFX.round(tax2, 2);
                        l.setText(BillerFX.df.format(tax2));
                        gt = total - discount + tax1 +tax2;
                        gt = Math.round(gt);
                        m.setText(BillerFX.df.format(gt));
                        double tid = total - discount;
                        tid = BillerFX.round(tid, 2);
                        j.setText(BillerFX.df.format(tid));
                    }
                });
            }
        }.start();
    }
    
    File generatePDF()
    {
        File fi = null;
        try{
            Stage gg = ((Stage)imv.getParent().getScene().getWindow());
            fi = File.createTempFile("billerfx_"+gg.getTitle(), ".pdf");
            fi.deleteOnExit();
            
            
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fi));
            Rectangle r = new RectangleReadOnly(207,575);
            document.setPageSize(r);
            document.setMargins(15 , 15, 0, 0);
            document.open();
            Font fontbold = FontFactory.getFont("Times-Roman", 10, Font.NORMAL);
            Font fontbold2 = FontFactory.getFont("Times-Roman", 9, Font.NORMAL);

            String[] ll = Database.getCurrentSettings();
            String ss = "-----------------------------------------------------\n";
            ss       += ll[0]+"\nAddress: "+ll[1]+"\nPhone: "+ll[2]+"\n";
            String kk = Calendar.getInstance().get(Calendar.AM_PM) == Calendar.AM ? "AM": (Calendar.getInstance().get(Calendar.AM_PM) == Calendar.PM ? "PM": "");
            ss       += Calendar.getInstance().get(Calendar.DATE)+"/"+(1+Calendar.getInstance().get(Calendar.MONTH))+"/"+Calendar.getInstance().get(Calendar.YEAR)+" at "+Calendar.getInstance().get(Calendar.HOUR)+":"+Calendar.getInstance().get(Calendar.MINUTE)+" "+kk+"\n";
            ss       += "Table No. "+c.getText()+"     Bill No. "+a.getText()+"\n";
            ss       += "-----------------------------------------------------\n";
            Paragraph para = new Paragraph(ss, fontbold);
            para.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(para);
            
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            float[] columnWidths = {3f, 1f};
            table.setWidths(columnWidths);
            PdfPCell defaultCell = table.getDefaultCell();
            defaultCell.setBorder(PdfPCell.NO_BORDER);

            ObservableList ob = map.get(Integer.parseInt(gg.getTitle()));
            for(int i=0; i<ob.size(); i++)
            {
                Item2 ii = (Item2) ob.get(i);
                String s1 = ii.getQuantity()+" x "+ii.getName();
                String s2 = "Rs. "+ii.getTotal();
                Paragraph para1 = new Paragraph(s1, fontbold2);
                para1.setAlignment(Paragraph.ALIGN_LEFT);
                Paragraph para2 = new Paragraph(s2, fontbold2);
                para2.setAlignment(Paragraph.ALIGN_RIGHT);
                PdfPCell cell1 = new PdfPCell(para1);
                cell1.setBorder(PdfPCell.NO_BORDER);
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                PdfPCell cell2 = new PdfPCell(para2);
                cell2.setBorder(PdfPCell.NO_BORDER);
                cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell1);
                table.addCell(cell2);
            }
            document.add(table);
            
            para = new Paragraph("-----------------------------------------------------\n", fontbold);
            para.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(para);
            
            table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(columnWidths);
            defaultCell = table.getDefaultCell();
            defaultCell.setBorder(PdfPCell.NO_BORDER);
            
            String s1 = "Total:";
            String s2 = "Rs. "+h.getText();
            Paragraph para1 = new Paragraph(s1, fontbold2);
            para1.setAlignment(Paragraph.ALIGN_LEFT);
            Paragraph para2 = new Paragraph(s2, fontbold2);
            para2.setAlignment(Paragraph.ALIGN_RIGHT);
            PdfPCell cell1 = new PdfPCell(para1);
            cell1.setBorder(PdfPCell.NO_BORDER);
            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell cell2 = new PdfPCell(para2);
            cell2.setBorder(PdfPCell.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell1);
            table.addCell(cell2);
            
            if(Double.parseDouble(h.getText()) - Double.parseDouble(i.getText()) > 0.0)
            {
                s1 = "Discount:";
                s2 = "Rs. "+BillerFX.df.format(Double.parseDouble(h.getText()) - Double.parseDouble(i.getText()));
                para1 = new Paragraph(s1, fontbold2);
                para1.setAlignment(Paragraph.ALIGN_LEFT);
                para2 = new Paragraph(s2, fontbold2);
                para2.setAlignment(Paragraph.ALIGN_RIGHT);
                cell1 = new PdfPCell(para1);
                cell1.setBorder(PdfPCell.NO_BORDER);
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell2 = new PdfPCell(para2);
                cell2.setBorder(PdfPCell.NO_BORDER);
                cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell1);
                table.addCell(cell2);
            }
            
            if(ll[7].equals("1"))
            {
                s1 = ll[3];
                s2 = "Rs. "+k.getText();
                para1 = new Paragraph(s1, fontbold2);
                para1.setAlignment(Paragraph.ALIGN_LEFT);
                para2 = new Paragraph(s2, fontbold2);
                para2.setAlignment(Paragraph.ALIGN_RIGHT);
                cell1 = new PdfPCell(para1);
                cell1.setBorder(PdfPCell.NO_BORDER);
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell2 = new PdfPCell(para2);
                cell2.setBorder(PdfPCell.NO_BORDER);
                cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell1);
                table.addCell(cell2);
            }
            
            if(ll[8].equals("1"))
            {
                s1 = ll[4];
                s2 = "Rs. "+l.getText();
                para1 = new Paragraph(s1, fontbold2);
                para1.setAlignment(Paragraph.ALIGN_LEFT);
                para2 = new Paragraph(s2, fontbold2);
                para2.setAlignment(Paragraph.ALIGN_RIGHT);
                cell1 = new PdfPCell(para1);
                cell1.setBorder(PdfPCell.NO_BORDER);
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell2 = new PdfPCell(para2);
                cell2.setBorder(PdfPCell.NO_BORDER);
                cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell1);
                table.addCell(cell2);
            }
            
            s1 = "Grand Total:";
            s2 = "Rs. "+m.getText();
            para1 = new Paragraph(s1, fontbold2);
            para1.setAlignment(Paragraph.ALIGN_LEFT);
            para2 = new Paragraph(s2, fontbold2);
            para2.setAlignment(Paragraph.ALIGN_RIGHT);
            cell1 = new PdfPCell(para1);
            cell1.setBorder(PdfPCell.NO_BORDER);
            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell2 = new PdfPCell(para2);
            cell2.setBorder(PdfPCell.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell1);
            table.addCell(cell2);
            
            document.add(table);
            
            para = new Paragraph("-----------------------------------------------------\n", fontbold);
            para.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(para);
            
            fontbold2 = FontFactory.getFont("Times-Roman", 8, Font.NORMAL);
            para = new Paragraph("Thank You.\nThis invoice was created using BillerFX.\n (BillerFX Contact: ayushmaanbhav1008@gmail.com)\n", fontbold2);
            para.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(para);
            
            para = new Paragraph("-----------------------------------------------------\n", fontbold);
            para.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(para);
            
            document.close();
        }catch(Exception mm){}
        return fi;
    }
}
