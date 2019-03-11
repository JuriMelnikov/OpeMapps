/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.User;
import entity.UsersTeam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jvm
 */
@Stateless
public class UsersTeamFacade extends AbstractFacade<UsersTeam> {

    @PersistenceContext(unitName = "OpeMappsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsersTeamFacade() {
        super(UsersTeam.class);
    }

    public UsersTeam findByUser(User user) {
        try {
            return  (UsersTeam) em.createQuery("SELECT ut FROM UsersTeam ut WHERE ut.user = :user")
                    .setParameter("user", user)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
}
