package nil.acme.wm.service;

import javax.ejb.EJB;

import nil.acme.wm.bl.WatermarkService;
import nil.acme.wm.bl.ejb.WatermarkServiceBean;
import nil.acme.wm.domain.AbstractDocument;
import nil.acme.wm.domain.Book;
import nil.acme.wm.domain.Journal;
import nil.acme.wm.domain.Ticket;
import nil.acme.wm.persistence.DocumentDAO;
import nil.acme.wm.persistence.TicketDAO;
import nil.acme.wm.persistence.ejb.DocumentDAOBean;
import nil.acme.wm.persistence.ejb.TicketDAOBean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class WatermarkTest {
  private static final String TDC_WATERMARK = "{content:\"book\", title:\"The Dark Code\", author:\"Bruce Wayne\", topic:\"Science\"}";
  private static final String HTMM_WATERMARK = "{content:\"book\", title:\"How to make money\", author:\"Dr. Evil\", topic:\"Business\"}";
  private static final String JOHFR_WATERMARK = "{content:\"journal\", title:\"Journal of human flight routes\", author:\"Clark Kent\"}";
  
  @EJB
  private DocumentDAO documentDAO;
  @EJB
  private WatermarkService watermarkService;

  @Deployment
  public static Archive<?> createArchive() {
    final JavaArchive archive = ShrinkWrap
            .create(JavaArchive.class)
            .addClasses(WatermarkService.class, WatermarkServiceBean.class, AbstractDocument.class, Book.class,
                    Journal.class, Ticket.class, DocumentDAO.class, DocumentDAOBean.class, TicketDAO.class,
                    TicketDAOBean.class)
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    return archive;
  }

  @Test
  public void documentDAO() throws Exception {
    Book book = new Book("The Dark Code", "Bruce Wayne", "Science");
    book = documentDAO.createDocument(book);
    Book savedBook = documentDAO.readDocument(book.getId(), Book.class);
    Assert.assertEquals(book.getTitle(), savedBook.getTitle());
    Assert.assertEquals(book.getAuthor(), savedBook.getAuthor());
    Assert.assertEquals(book.getTopic(), savedBook.getTopic());
    Assert.assertNull(savedBook.getWatermark());
  }

  @Test(timeout = 10000)
  public void theDarkCode() throws Exception {
    Book book = new Book("The Dark Code", "Bruce Wayne", "Science");
    book = documentDAO.createDocument(book);
    Assert.assertFalse(book.isWatermarked());
    Ticket t = watermarkService.createWatermark(book);
    while (watermarkService.getProcessingStatus(t) == WatermarkService.Status.PROCESSING) {
      Thread.sleep(1000l);
    }
    AbstractDocument doc = watermarkService.getDocument(t);
    Assert.assertTrue(doc.isWatermarked());
    Assert.assertEquals(TDC_WATERMARK, doc.getWatermark());
  }

  @Test(timeout = 10000)
  public void howToMakeMoney() throws Exception {
    Book book = new Book("How to make money", "Dr. Evil", "Business");
    book = documentDAO.createDocument(book);
    Assert.assertFalse(book.isWatermarked());
    Ticket t = watermarkService.createWatermark(book);
    while (watermarkService.getProcessingStatus(t) == WatermarkService.Status.PROCESSING) {
      Thread.sleep(1000l);
    }
    AbstractDocument doc = watermarkService.getDocument(t);
    Assert.assertTrue(doc.isWatermarked());
    Assert.assertEquals(HTMM_WATERMARK, doc.getWatermark());
  }

  @Test(timeout = 10000)
  public void journalOfHumanFlightRoutes() throws Exception {
    Journal journal = new Journal("Journal of human flight routes", "Clark Kent");
    journal = documentDAO.createDocument(journal);
    Assert.assertFalse(journal.isWatermarked());
    Ticket t = watermarkService.createWatermark(journal);
    while (watermarkService.getProcessingStatus(t) == WatermarkService.Status.PROCESSING) {
      Thread.sleep(1000l);
    }
    AbstractDocument doc = watermarkService.getDocument(t);
    Assert.assertTrue(doc.isWatermarked());
    Assert.assertEquals(JOHFR_WATERMARK, doc.getWatermark());
  }

}
