package nil.acme.wm.bl;

import javax.ejb.Local;

import nil.acme.wm.domain.AbstractDocument;
import nil.acme.wm.domain.Ticket;

@Local
public interface WatermarkService {
  public enum Status {
    PROCESSING, FINISHED
  }

  Ticket createWatermark(AbstractDocument document);

  Status getProcessingStatus(Ticket ticket);

  AbstractDocument getDocument(Ticket ticket);
}
