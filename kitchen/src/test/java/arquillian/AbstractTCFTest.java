package arquillian;

import fr.unice.polytech.isa.tcf.asynchronous.KitchenPrinter;
import fr.unice.polytech.isa.tcf.components.KitchenBean;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.utils.CookieScheduler;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public abstract class AbstractTCFTest {

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
                // Persistence file
                .addAsManifestResource(new ClassLoaderAsset("META-INF/persistence.xml"), "persistence.xml");
    }

}
