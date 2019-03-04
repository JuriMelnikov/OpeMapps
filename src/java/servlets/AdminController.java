/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;


import entity.User;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import secure.Role;
import secure.RolesList;
import secure.SecureLogic;
import session.PersonFacade;
import session.RoleFacade;
import session.UserFacade;
import util.PageReturner;

/**
 *
 * @author Melnikov
 */
@WebServlet(name = "Library", urlPatterns = {
    
    "/showListUsers",
    "/listUsers",
    "/showChangeRole",
    "/changeUserRole",
    "/changeRole",
    
    
})
public class AdminController extends HttpServlet {
    

@EJB PersonFacade personFacade;
@EJB UserFacade userFacade;
@EJB RoleFacade roleFacade;

    public AdminController() {
    }


    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF8");
        SecureLogic sl= new SecureLogic();
        HttpSession session = request.getSession(false);
        User regUser = null;
        if(session == null){
            request.setAttribute("info", "Вы должны войти как администратор!");
            request.getRequestDispatcher(PageReturner.getPage("showLogin")).forward(request, response);
            return;
        }
        regUser = (User) session.getAttribute("regUser");
        if(regUser == null){
            request.setAttribute("info", "Вы должны войти как администратор!");
            request.getRequestDispatcher(PageReturner.getPage("showLogin")).forward(request, response);
            return;
        }
        if(!sl.isRole(RolesList.ADMINISTRATOR.toString(), regUser)){
            request.setAttribute("info", "У Вас должны быть права администратора!");
            request.getRequestDispatcher(PageReturner.getPage("showLogin")).forward(request, response);
            return;
        }
            
        String path = request.getServletPath();
        if(null != path) switch (path) {
            case "/showListUsers":
                if(!sl.isRole(RolesList.ADMINISTRATOR.toString(), regUser)){
                    request.setAttribute("info", "У вас нет прав доступа к ресурсу");
                    request.getRequestDispatcher(PageReturner.getPage("showLogin"))
                            .forward(request, response);
                    break;
                } 
                List<User> listUsers = userFacade.findAll();
                request.setAttribute("listUsers", listUsers);
                request.getRequestDispatcher(PageReturner.getPage("listUsers")).forward(request, response);
                break;
             case "/showChangeRole":
                List<Role> listRoles = roleFacade.findAll();
                    listUsers = userFacade.findAll();
                Role role;
                Map<User,Role> mapUsers = new HashMap<>();
                for(User user : listUsers){
                    role=sl.getRole(user);
                    mapUsers.put(user, role);
                }
                request.setAttribute("listRoles", listRoles);
                request.setAttribute("mapUsers", mapUsers);
                request.getRequestDispatcher(PageReturner.getPage("goToChangeRole"))
                        .forward(request, response);
                break;
            
            case "/changeRole":
                String roleId = request.getParameter("roleId");
                String userId = request.getParameter("userId");
                role = roleFacade.find(Long.parseLong(roleId));
                User user = userFacade.find(Long.parseLong(userId));
                if(!"admin".equals(user.getLogin())){
                    sl.setRole(role,user);
                }
                request.getRequestDispatcher("/showChangeRole")
                        .forward(request, response);
                break;
            case "/listUsers":
                listUsers = userFacade.findAll();
                request.setAttribute("listUsers", listUsers);
                request.getRequestDispatcher(PageReturner.getPage("listUsers"))
                        .forward(request, response);
                break;
        }
        
    
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
