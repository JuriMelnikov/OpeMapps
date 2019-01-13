/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import secure.UserRoles;

/**
 *
 * @author Melnikov
 */
@Stateless
public class UserRolesFacade extends AbstractFacade<UserRoles> {

    @PersistenceContext(unitName = "OpeMappsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserRolesFacade() {
        super(UserRoles.class);
    }

    public List<UserRoles> findByUser(User user) {
        return em.createQuery("SELECT ur FROM UserRoles ur WHERE ur.user = :user")
                .setParameter("user", user)
                .getResultList();
    }

   
    
}
