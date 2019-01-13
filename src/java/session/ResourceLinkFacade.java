/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.ResourceLink;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jvm
 */
@Stateless
public class ResourceLinkFacade extends AbstractFacade<ResourceLink> {

    @PersistenceContext(unitName = "OpeMappsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ResourceLinkFacade() {
        super(ResourceLink.class);
    }
    
}
