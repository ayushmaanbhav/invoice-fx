/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package billerfx;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Sony
 */
public class Type{
    final SimpleStringProperty name;
    Type(String hh)
    {
        name = new SimpleStringProperty(hh);
    }

    public String getName() {
        return name.get();
    }
}
