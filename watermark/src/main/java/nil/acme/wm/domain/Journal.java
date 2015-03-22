package nil.acme.wm.domain;

import javax.persistence.Entity;

@Entity
public class Journal extends AbstractDocument {

  public Journal() {
  }

  public Journal(String title, String author) {
    super(title, author);
  }
}
