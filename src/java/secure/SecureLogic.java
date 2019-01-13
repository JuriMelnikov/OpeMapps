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
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import session.RoleFacade;
import session.UserRolesFacade;

/**
 *
 * @author Melnikov
 */
public class SecureLogic {
    private UserRolesFacade userRolesFacade;
    private RoleFacade roleFacade;

    public SecureLogic() {
        Context context;
        try {
            context = new InitialContext();
            this.userRolesFacade = (UserRolesFacade) context.lookup("java:module/UserRolesFacade");
            this.roleFacade = (RoleFacade) context.lookup("java:module/RoleFacade");
        } catch (NamingException ex) {
            Logger.getLogger(SecureLogic.class.getName()).log(Level.SEVERE, "SecureLogic не удалось найти Бин", ex);
        }
    }
    
    public void addRoleToUser(UserRoles ur){
        
        this.deleteRoleToUser(ur.getUser());
        Role newRole;
        UserRoles newUserRoles;
        int n = RolesList.values().length;
        if(ur.getRole().getName().equals(RolesList.ADMINISTRATOR.toString())){
            userRolesFacade.create(ur);
            newRole = roleFacade.findRoleByName(RolesList.JUHATAJA.toString());
            newUserRoles = new UserRoles(ur.getUser(),newRole);
            userRolesFacade.create(newUserRoles);
            newRole = roleFacade.findRoleByName(RolesList.RUHMAJUHATAJA.toString());
            newUserRoles = new UserRoles(ur.getUser(),newRole);
            userRolesFacade.create(newUserRoles);
            newRole = roleFacade.findRoleByName(RolesList.OPILANE.toString());
            newUserRoles = new UserRoles(ur.getUser(),newRole);
            userRolesFacade.create(newUserRoles);
        }else if(ur.getRole().getName().equals(RolesList.JUHATAJA.toString())){
            userRolesFacade.create(ur);
            newRole = roleFacade.findRoleByName(RolesList.RUHMAJUHATAJA.toString());
            newUserRoles = new UserRoles(ur.getUser(),newRole);
            userRolesFacade.create(newUserRoles);
            newRole = roleFacade.findRoleByName(RolesList.OPILANE.toString());
            newUserRoles = new UserRoles(ur.getUser(),newRole);
            userRolesFacade.create(newUserRoles);
        }else if(ur.getRole().getName().equals(RolesList.RUHMAJUHATAJA.toString())){
            userRolesFacade.create(ur);
            newRole = roleFacade.findRoleByName(RolesList.OPILANE.toString());
            newUserRoles = new UserRoles(ur.getUser(),newRole);
            userRolesFacade.create(newUserRoles);
        }else if(ur.getRole().getName().equals(RolesList.OPILANE.toString())){
            userRolesFacade.create(ur);
        }
        
    }
    public void deleteRoleToUser(User user){
        List<UserRoles> listUserRoles = userRolesFacade.findByUser(user);
        int n = listUserRoles.size();
        for(int i=0; i<n; i++){
            userRolesFacade.remove(listUserRoles.get(i));
        }
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
    
    public String getRole(User regUser) {
        List<UserRoles> listUserRoles = userRolesFacade.findByUser(regUser);
        int n = listUserRoles.size();
        for(int i = 0; i<n; i++){
            if(RolesList.ADMINISTRATOR.toString().equals(listUserRoles.get(i).getRole().getName())){
                return listUserRoles.get(i).getRole().getName();
            }
        }
        for(int i = 0; i<n; i++){
            if(RolesList.JUHATAJA.toString().equals(listUserRoles.get(i).getRole().getName())){
                return listUserRoles.get(i).getRole().getName();
            }
        }
        for(int i = 0; i<n; i++){
            if(RolesList.RUHMAJUHATAJA.toString().equals(listUserRoles.get(i).getRole().getName())){
                return listUserRoles.get(i).getRole().getName();
            }
        }
        for(int i = 0; i<n; i++){
            if(RolesList.OPILANE.toString().equals(listUserRoles.get(i).getRole().getName())){
                return listUserRoles.get(i).getRole().getName();
            }
        }
        return null;
    }
    
}
