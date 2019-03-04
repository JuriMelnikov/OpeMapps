/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package secure;

import entity.User;
import java.util.ArrayList;
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
    
    public void setRole(Role role, User user){
        this.removeAllRoles(user);
        Role newRole;
        UserRoles ur=new UserRoles();
        ur.setUser(user);
        if(null != role.getName())
            switch (role.getName()) {
                case "ADMINISTRATOR":
                    newRole = this.getRole(RolesList.ADMINISTRATOR.toString());
                    ur.setRole(newRole);
                    userRolesFacade.create(ur);
                    newRole = this.getRole(RolesList.JUHATAJA.toString());
                    ur.setRole(newRole);
                    userRolesFacade.create(ur);
                    newRole = this.getRole(RolesList.RUHMAJUHATAJA.toString());
                    ur.setRole(newRole);
                    userRolesFacade.create(ur);
                    newRole = this.getRole(RolesList.OPILANE.toString());
                    ur.setRole(newRole);
                    userRolesFacade.create(ur);
                    break;
                case "JUHATAJA":
                    newRole = this.getRole(RolesList.JUHATAJA.toString());
                    ur.setRole(newRole);
                    userRolesFacade.create(ur);
                    newRole = this.getRole(RolesList.RUHMAJUHATAJA.toString());
                    ur.setRole(newRole);
                    userRolesFacade.create(ur);
                    newRole = this.getRole(RolesList.OPILANE.toString());
                    ur.setRole(newRole);
                    userRolesFacade.create(ur);
                    break;
                case "RUHMAJUHATAJA":
                    newRole = this.getRole(RolesList.RUHMAJUHATAJA.toString());
                    ur.setRole(newRole);
                    userRolesFacade.create(ur);
                    newRole = this.getRole(RolesList.OPILANE.toString());
                    ur.setRole(newRole);
                    userRolesFacade.create(ur);
                    break;
                case "OPILANE":
                    newRole = this.getRole(RolesList.OPILANE.toString());
                    ur.setRole(newRole);
                    userRolesFacade.create(ur);
                    break;
            }
        
        
    }
    public void removeAllRoles(User user){
        List<UserRoles> listUserRoles = userRolesFacade.findUserRoles(user);
        int n = listUserRoles.size();
        for(int i=0; i<n; i++){
            userRolesFacade.remove(listUserRoles.get(i));
        }
    }

    public boolean isRole(String roleName,User user){
        boolean res=false;
        List<UserRoles> listUserRoles = userRolesFacade.findUserRoles(user);
        List<String> listRolesByUser = new ArrayList<>();
        for(UserRoles ur : listUserRoles){
            listRolesByUser.add(ur.getRole().getName());
        }
        return listRolesByUser.contains(roleName);
    }
    
    public  Role getRole(String roleName){
        List<Role> roles = roleFacade.findAll();
        for(Role role: roles){
            if(roleName.equals(role.getName())){
                return role;
            }
        }
        return null;
    }
    
    public Role getRole(User user){
        List<UserRoles> listUserRoles = userRolesFacade.findUserRoles(user);
        List<String> nameRoles = new ArrayList<>();
        for(UserRoles ur : listUserRoles){
            nameRoles.add(ur.getRole().getName());
        }
        if(nameRoles.contains(RolesList.ADMINISTRATOR.toString())){
            return getRole(RolesList.ADMINISTRATOR.toString());
        }
        if(nameRoles.contains(RolesList.JUHATAJA.toString())){
            return getRole(RolesList.JUHATAJA.toString());
        }
        if(nameRoles.contains(RolesList.RUHMAJUHATAJA.toString())){
            return getRole(RolesList.RUHMAJUHATAJA.toString());
        }
        if(nameRoles.contains(RolesList.OPILANE.toString())){
            return getRole(RolesList.OPILANE.toString());
        }else{
            return null;
        }
    }
    
}
