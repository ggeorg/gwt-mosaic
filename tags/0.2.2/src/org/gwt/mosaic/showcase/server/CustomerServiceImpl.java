package org.gwt.mosaic.showcase.server;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;

import org.gwt.mosaic.showcase.client.content.other.Customer;
import org.gwt.mosaic.showcase.client.content.other.CustomerService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class CustomerServiceImpl extends RemoteServiceServlet implements CustomerService {
  private static final long serialVersionUID = 4558037815093135955L;

  private EntityManager samplePUEntityManager;

  @Override
  public void init() throws ServletException {
    super.init();
    samplePUEntityManager = Persistence.createEntityManagerFactory("samplePU").createEntityManager();

    samplePUEntityManager.getTransaction().begin();
    
    Customer c = new Customer();
    c.setDiscountCode('A');
    c.setZip("Zip1");
    c.setName("Georgios Georgopoulos");
    samplePUEntityManager.persist(c);
    
    c = new Customer();
    c.setDiscountCode('B');
    c.setZip("Zip2");
    c.setName("Maria Matsouka");
    samplePUEntityManager.persist(c);
    
    c = new Customer();
    c.setDiscountCode('C');
    c.setZip("Zip3");
    c.setName("Evi Matsouka");
    samplePUEntityManager.persist(c);
    
    samplePUEntityManager.getTransaction().commit();
  }

  @Override
  public void destroy() {
    samplePUEntityManager.close();
    samplePUEntityManager = null;
    super.destroy();
  }

  @SuppressWarnings("unchecked")
  public List<Customer> getCustomers() {
    Query customerQuery = samplePUEntityManager.createQuery("SELECT c FROM Customer c");
    return customerQuery.getResultList();
  }

}
