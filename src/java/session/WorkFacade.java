/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Subject;
import entity.Team;
import entity.Work;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jvm
 */
@Stateless
public class WorkFacade extends AbstractFacade<Work> {

    @PersistenceContext(unitName = "OpeMappsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WorkFacade() {
        super(Work.class);
    }

    public List<Work> findBySubject(Subject subject, Team team) {
        try {
            return em.createQuery("SELECT w FROM Work w WHERE w.subject = :subject AND w.team = :team")
                    .setParameter("subject", subject)
                    .setParameter("team", team)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
}
