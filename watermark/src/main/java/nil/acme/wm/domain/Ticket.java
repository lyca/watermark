package nil.acme.wm.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Ticket {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @OneToOne
  private AbstractDocument document;

  public Ticket() {
  }

  public Ticket(AbstractDocument document) {
    this.document = document;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public AbstractDocument getDocument() {
    return document;
  }

  public void setDocument(AbstractDocument document) {
    this.document = document;
  }
}
