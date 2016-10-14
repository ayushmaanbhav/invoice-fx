/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package billerfx;

import java.io.File;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

/**
 *
 * @author Ayush Jain
 */
public class Invoice {
    private final SimpleStringProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty phone;
    private final SimpleStringProperty email;
    private final SimpleStringProperty address;
    private final File invoicef;
    private final SimpleStringProperty date;
 
    Invoice(String id, String name, String phone, String email, String address, File invoicef, String date) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
        this.address = new SimpleStringProperty(address);
        this.date = new SimpleStringProperty(date);
        this.invoicef = invoicef;
    }
    
    public String getId() {
        return id.get();
    }
    
    public String getName() {
        return name.get();
    }
    
    public String getPhone() {
        return phone.get();
    }
    
    public String getEmail() {
        return email.get();
    }
    
    public String getAddress() {
        return address.get();
    }
    
    public String getDate() {
        return date.get();
    }
    
    public File getFile() {
        return invoicef;
    }
}
