package fr.unice.polytech.isa.tcf;

import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;
import fr.unice.polytech.isa.tcf.exceptions.PaymentException;
import java.util.Set;
import javax.ejb.Local;

@Local
public interface Payment {

    String payOrder(Customer customer, Set<Item> items) throws PaymentException;

}
