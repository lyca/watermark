package nil.acme.wm.persistence;

import javax.ejb.Local;

import nil.acme.wm.domain.AbstractDocument;

@Local
public interface DocumentDAO {
  <T extends AbstractDocument> T createDocument(T document);

  <T extends AbstractDocument> T readDocument(Long id, Class<T> type);

  <T extends AbstractDocument> T updateDocument(T document);
}
