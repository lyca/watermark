package nil.acme.wm.persistence.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import nil.acme.wm.domain.Ticket;
import nil.acme.wm.persistence.TicketDAO;

@Stateless
public class TicketDAOBean implements TicketDAO {

  @PersistenceContext
  EntityManager em;
  
  public Ticket createTicket(Ticket ticket) {
    em.persist(ticket);
    return ticket;
  }

  public Ticket readTicket(Long id) {
    return em.find(Ticket.class, id);
  }

}
