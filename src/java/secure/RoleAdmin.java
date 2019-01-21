/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package secure;

import entity.User;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import session.RoleFacade;
import session.UserRolesFacade;

/**
 *
 * @author jvm
 */
public class RoleAdmin {
    @EJB RoleFacade roleFacade;
    @EJB UserRolesFacade userRolesFacade;
    
    private Context context;

    public RoleAdmin() {
        try {
            context = new InitialContext();
            this.roleFacade = (RoleFacade) context.lookup("java:module/RoleFacade");
            this.userRolesFacade = (UserRolesFacade) context.lookup("java:module/UserRolesFacade");
            
        } catch (NamingException ex) {
            Logger.getLogger(RoleAdmin.class.getName()).log(Level.SEVERE, RoleAdmin.class.getName()+" - не удалось найти бин.", ex);
        }
    }
    
    public Role addNewRole(String roleName, int layer){
        roleName = roleName.toUpperCase();
        Role role = roleFacade.findRoleByName(roleName);
        if(role == null){
            List<Role> roles = roleFacade.findAll();
            if(roles == null) {
               return this.addAdmin();
            }
            for(Role r : roles){
                if(r.getLayer()>=layer){
                    r.setLayer(r.getLayer()+1);
                    roleFacade.edit(r);
                }
            }
            Role newRole = new Role();
            newRole.setName(roleName);
            newRole.setLayer(layer);
            roleFacade.create(newRole);
        }
        return role;
    }
    
    public void deleteRole(String nameRole){
        Role role = roleFacade.findRoleByName(nameRole);
        if(role != null){
            List<Role> roles = roleFacade.findAll();
            for(Role r : roles){
                if(r.getLayer()>=role.getLayer()){
                    r.setLayer(r.getLayer()-1);
                    roleFacade.edit(r);
                }
            }
            roleFacade.remove(role);
        }
    }
     public void deleteRole(Role role){
         this.deleteRole(role.getName());
     }
   
    public void addRoleToUser(String roleName,User user){
        this.deleteRoleToUser(user);
        UserRoles ur= new UserRoles();
        Role role = roleFacade.findRoleByName(roleName);
        List<Role> listRoles = roleFacade.findAll();
        listRoles.stream()
                .filter(r -> r.getLayer() >= role.getLayer()) //роли из списка с уровнем больше чем указанная роль включительно
                .forEach(r -> { //добавим отфильтрованные роли в юзеру
                    ur.setRole(r); 
                    ur.setUser(user);
                    userRolesFacade.create(ur);
                });
    }
    public void addRoleToUser(Role role, User user){
        this.addRoleToUser(role.getName(), user);
    }
    public void deleteRoleToUser(User user){
        List<UserRoles> listUserRoles = userRolesFacade.findByUser(user);
        int n = listUserRoles.size();
        for(int i=0; i<n; i++){
            userRolesFacade.remove(listUserRoles.get(i));
        }
    }

    public Role addAdmin() {
        Role role = new Role();
        role.setName("ADMINISTRATOR");
        role.setLayer(0);
        roleFacade.create(role);
        return role;
    }
    
    public boolean isRole(User user, String roleName){
        if(user == null) return false;
        List<UserRoles> listUserRoles = userRolesFacade.findByUser(user);
        Role role = roleFacade.findRoleByName(roleName);
        int n = listUserRoles.size();
        for(int i = 0; i < n; i++){
            if(listUserRoles.get(i).getRole().equals(role)){
                return true;
            }
        }
        return false;
    }
    public Role getRoleUser(User user){
        List<UserRoles> userRoles = userRolesFacade.findByUser(user);
        List<Role> listRoles = roleFacade.findAll();
        int maxRoleIndex = listRoles.size()-1;
        int count = (int) userRoles.stream()
                .filter(user::equals)
                .count();
        return listRoles.get(maxRoleIndex-count);
                
    }
}
