/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package billerfx;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Ayush Jain
 */
public class Drink {
    private final SimpleStringProperty glasssize;
    private final SimpleStringProperty price;
 
    Drink(String name, String phone) {
        this.glasssize = new SimpleStringProperty(name);
        this.price = new SimpleStringProperty(phone);
    }
    
    public String getGlasssize() {
        return glasssize.get();
    }
    
    public String getPrice() {
        return price.get();
    }
    
}
