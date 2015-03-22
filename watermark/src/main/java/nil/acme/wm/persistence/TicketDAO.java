package nil.acme.wm.persistence;

import javax.ejb.Local;

import nil.acme.wm.domain.Ticket;

@Local
public interface TicketDAO {
  Ticket createTicket(Ticket ticket);

  Ticket readTicket(Long id);
}
