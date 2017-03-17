package fr.unice.polytech.isa.tcf.business;

import fr.unice.polytech.isa.tcf.ControlledPayment;
import fr.unice.polytech.isa.tcf.Payment;
import fr.unice.polytech.isa.tcf.asynchronous.KitchenPrinter;
import fr.unice.polytech.isa.tcf.components.CashierBean;
import fr.unice.polytech.isa.tcf.components.KitchenBean;
import fr.unice.polytech.isa.tcf.entities.Cookies;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;
import fr.unice.polytech.isa.tcf.entities.Order;
import fr.unice.polytech.isa.tcf.exceptions.PaymentException;
import fr.unice.polytech.isa.tcf.utils.BankAPI;
import fr.unice.polytech.isa.tcf.utils.CookieScheduler;
import java.util.HashSet;
import java.util.Set;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class CashierTest {

    @EJB
    private ControlledPayment cashier;
    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private UserTransaction utx;

    // Test context
    private Set<Item> items;
    private Customer john;
    private Customer pat;

    @Deployment
    public static WebArchive createDeployment() {
        // Building a Web ARchive (WAR) containing the following elements:
        return ShrinkWrap.create(WebArchive.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                // Message-Driven beans
                .addPackage(KitchenPrinter.class.getPackage())
                // Entities
                .addPackage(Customer.class.getPackage())
                // Bean
                .addPackage(KitchenBean.class.getPackage())
                .addPackage(CookieScheduler.class.getPackage())
                .addPackage(Payment.class.getPackage())
                .addPackage(ControlledPayment.class.getPackage())
                .addPackage(CashierBean.class.getPackage())
                // Persistence file
                .addAsManifestResource(new ClassLoaderAsset("META-INF/persistence.xml"), "persistence.xml");
    }

    @Before
    public void setUpContext() throws Exception {
        initData();
        initMock();
    }

    @After
    public void cleanUpContext() throws Exception {
        utx.begin();
        john = entityManager.merge(john);
        entityManager.remove(john);
        pat = entityManager.merge(pat);
        entityManager.remove(pat);
        utx.commit();
    }

    @Test
    public void processToPayment() throws Exception {
        // paying order
        System.out.println("###CASHIER");
        System.out.println(cashier);
        System.out.println("###CASHIER" + cashier);
        String id = cashier.payOrder(john, items);

        // memory contents from the Order point of view
        Order order = entityManager.find(Order.class, Integer.parseInt(id));
        john = entityManager.merge(john);

        assertNotNull(order);
        assertEquals(john, order.getCustomer());
        assertEquals(items, order.getItems());
        double price = (3 * Cookies.CHOCOLALALA.getPrice()) + (2 * Cookies.DARK_TEMPTATION.getPrice());
        assertEquals(price, order.getPrice(), 0.0);
    }

    @Test(expected = PaymentException.class)
    public void identifyPaymentError() throws Exception {
        cashier.payOrder(pat, items);
    }

    private void initData() throws Exception {
        items = new HashSet<>();
        items.add(new Item(Cookies.CHOCOLALALA, 3));
        items.add(new Item(Cookies.DARK_TEMPTATION, 2));
        john = new Customer("john", "1234896983");
        entityManager.persist(john);
        pat = new Customer("pat", "1234567890");
        entityManager.persist(pat);
    }

    private void initMock() throws Exception {
        // Mocking the external partner
        BankAPI mocked = mock(BankAPI.class);
        cashier.useBankReference(mocked);
        when(mocked.performPayment(eq(john), anyDouble())).thenReturn(true);
        when(mocked.performPayment(eq(pat), anyDouble())).thenReturn(false);
    }

}
