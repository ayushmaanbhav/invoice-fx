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
public class Item {
    private final SimpleStringProperty id;
    final SimpleStringProperty name;
    final SimpleStringProperty type;
    final SimpleStringProperty category;
    private final SimpleStringProperty discount;
    final SimpleStringProperty price;
    private final SimpleStringProperty soldmonth;
    private final SimpleStringProperty totalsold;
    private final SimpleStringProperty available;
    private final SimpleStringProperty glasssize;
 
    Item(String id, String name, String type, String category, String discount, String price, String soldmonth, String totalsold, String glasssize, String available) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.category = new SimpleStringProperty(category);
        this.discount = new SimpleStringProperty(discount);
        this.price = new SimpleStringProperty(price);
        this.soldmonth = new SimpleStringProperty(soldmonth);
        this.totalsold = new SimpleStringProperty(totalsold);
        this.available = new SimpleStringProperty(available);
        this.glasssize = new SimpleStringProperty(glasssize);
    }
    
    public String getId() {
        return id.get();
    }
    
    public String getName() {
        return name.get();
    }
    
    public String getType() {
        return type.get();
    }
    
    public String getCategory() {
        return category.get();
    }
    
    public String getDiscount() {
        return discount.get();
    }
    
    public String getPrice() {
        return price.get();
    }
    
    public String getSoldmonth() {
        return soldmonth.get();
    }
    
    public String getTotalsold() {
        return totalsold.get();
    }
    
    public String getAvailable() {
        return available.get();
    }
    
    public String getGlasssize() {
        return glasssize.get();
    }
    
}
