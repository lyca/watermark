package nil.acme.wm.persistence.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import nil.acme.wm.domain.AbstractDocument;
import nil.acme.wm.persistence.DocumentDAO;

@Stateless
public class DocumentDAOBean implements DocumentDAO {

  @PersistenceContext
  private EntityManager em;

  @Override
  public <T extends AbstractDocument> T createDocument(T document) {
    em.persist(document);
    return document;
  }

  @Override
  public <T extends AbstractDocument> T readDocument(Long id, Class<T> type) {
    return em.find(type, id);
  }

  @Override
  public <T extends AbstractDocument> T updateDocument(T document) {
    return em.merge(document);
  }
}
