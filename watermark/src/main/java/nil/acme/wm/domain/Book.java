package nil.acme.wm.domain;

import javax.persistence.Entity;

@Entity
public class Book extends AbstractDocument {
  private String topic;

  public Book() {
  }

  public Book(String title, String author, String topic) {
    super(title, author);
    this.topic = topic;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }
}
