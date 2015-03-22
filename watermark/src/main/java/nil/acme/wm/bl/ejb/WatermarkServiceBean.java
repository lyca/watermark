package nil.acme.wm.bl.ejb;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import nil.acme.wm.bl.WatermarkService;
import nil.acme.wm.domain.AbstractDocument;
import nil.acme.wm.domain.Book;
import nil.acme.wm.domain.Journal;
import nil.acme.wm.domain.Ticket;
import nil.acme.wm.persistence.DocumentDAO;
import nil.acme.wm.persistence.TicketDAO;

@Stateless
public class WatermarkServiceBean implements WatermarkService {

  private static final String BOOK_FORMAT = "{content:\"book\", title:\"%s\", author:\"%s\", topic:\"%s\"}";
  private static final String JOURNAL_FORMAT = "{content:\"journal\", title:\"%s\", author:\"%s\"}";

  private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  @EJB
  private DocumentDAO documentDAO;
  @EJB
  private TicketDAO ticketDAO;

  public Ticket createWatermark(AbstractDocument document) {
    Ticket ticket = ticketDAO.createTicket(new Ticket(document));
    executorService.execute(new WatermarkTask(ticket, documentDAO));
    return ticket;
  }

  public Status getProcessingStatus(Ticket ticket) {
    Ticket t = ticketDAO.readTicket(ticket.getId());
    AbstractDocument d = t.getDocument();
    return d.getWatermark() == null ? Status.PROCESSING : Status.FINISHED;
  }

  public AbstractDocument getDocument(Ticket ticket) {
    Ticket t = ticketDAO.readTicket(ticket.getId());
    return t.getDocument();
  }

  private static class WatermarkTask implements Runnable {

    private final Ticket ticket;
    private final DocumentDAO documentDAO;

    public WatermarkTask(Ticket ticket, DocumentDAO documentDAO) {
      this.ticket = ticket;
      this.documentDAO = documentDAO;
    }

    public void run() {
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      AbstractDocument document = ticket.getDocument();
      String watermark = createWatermark(document);
      if (document instanceof Book) {
        Book book = documentDAO.readDocument(document.getId(), Book.class);
        book.setWatermark(watermark);
        documentDAO.updateDocument(book);
      } else if (document instanceof Journal) {
        Journal journal = documentDAO.readDocument(document.getId(), Journal.class);
        journal.setWatermark(watermark);
        documentDAO.updateDocument(journal);
      }
    }

    private String createWatermark(AbstractDocument d) {
      if (d == null) {
        throw new IllegalArgumentException("Document is null.");
      }
      if (d instanceof Book) {
        return String.format(BOOK_FORMAT, d.getTitle(), d.getAuthor(), ((Book) d).getTopic());
      } else if (d instanceof Journal) {
        return String.format(JOURNAL_FORMAT, d.getTitle(), d.getAuthor());
      }
      throw new IllegalArgumentException("Cannot create Watermark for document class: '" + d.getClass() + "'.");
    }
  }

}
