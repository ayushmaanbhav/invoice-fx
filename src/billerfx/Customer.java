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
public class Customer {
    private final SimpleStringProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty phone;
    private final SimpleStringProperty email;
    private final SimpleStringProperty address;
    private final SimpleStringProperty visited;
 
    Customer(String id, String name, String phone, String email, String address, String visited) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
        this.address = new SimpleStringProperty(address);
        this.visited = new SimpleStringProperty(visited);
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
    
    public String getVisited() {
        return visited.get();
    }
}
