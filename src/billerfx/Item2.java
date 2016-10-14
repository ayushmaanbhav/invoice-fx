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
public class Item2 {
    private final SimpleStringProperty id;
    final SimpleStringProperty name;
    final SimpleStringProperty type;
    final SimpleStringProperty category;
    private final SimpleStringProperty discount;
    final SimpleStringProperty price;
    private final SimpleStringProperty quantity;
    private final SimpleStringProperty total;
    String glasssize;
 
    Item2(String id, String name, String type, String category, String discount, String price, String quantity, String total) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.category = new SimpleStringProperty(category);
        this.discount = new SimpleStringProperty(discount);
        this.price = new SimpleStringProperty(price);
        this.quantity = new SimpleStringProperty(quantity);
        this.total = new SimpleStringProperty(total);
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
    
    public String getQuantity() {
        return quantity.get();
    }
    
    public String getTotal() {
        return total.get();
    }
    
}
