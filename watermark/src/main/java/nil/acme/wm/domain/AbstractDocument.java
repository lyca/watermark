package nil.acme.wm.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public abstract class AbstractDocument {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  protected String title;
  protected String author;
  protected String watermark;

  public AbstractDocument() {
  }

  public AbstractDocument(String title, String author) {
    this.title = title;
    this.author = author;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getWatermark() {
    return watermark;
  }

  public void setWatermark(String watermark) {
    this.watermark = watermark;
  }

  public boolean isWatermarked() {
    return watermark != null;
  }
}
