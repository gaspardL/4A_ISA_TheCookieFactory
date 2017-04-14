package fr.unice.polytech.isa.tcf.exceptions;

import java.io.Serializable;
import javax.xml.ws.WebFault;

@WebFault(targetNamespace = "http://www.polytech.unice.fr/si/4a/isa/tcf/cart")
public class UnknownCustomerException extends Exception implements Serializable {

    private String name;

    public UnknownCustomerException(String name) {
        this.name = name;
    }

    public UnknownCustomerException() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
