/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;


import entity.Person;
import entity.User;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import secure.RolesList;
import secure.SecureLogic;
import session.PersonFacade;
import session.UserFacade;
import util.EncriptPass;
import util.PageReturner;

/**
 *
 * @author Melnikov
 */
@WebServlet(name = "Library", urlPatterns = {
    "/newUser",
    "/addUser",
    "/showBooks",
    "/showListUsers",
    
    
})
public class AdminController extends HttpServlet {
    

@EJB PersonFacade personFacade;
@EJB UserFacade userFacade;

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF8");
        HttpSession session = request.getSession(false);
        User regUser = null;
        if(session != null){
            try {
                regUser = (User) session.getAttribute("regUser");
            } catch (Exception e) {
                regUser = null;
            }
        }
            
        SecureLogic sl = new SecureLogic();
        String path = request.getServletPath();
        if(null != path)
            switch (path) {
        case "/newUser":
            request.getRequestDispatcher(PageReturner.getPage("newUser")).forward(request, response);
            break;
        case "/addUser":{
            String name = request.getParameter("name");
            String surname = request.getParameter("surname");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String login = request.getParameter("login");
            String password1 = request.getParameter("password1");
            String password2 = request.getParameter("password2");
            if(!password1.equals(password2)){
              request.setAttribute("info", "Неправильно введен логин или пароль");  
              request.getRequestDispatcher(PageReturner.getPage("listUsers"))
                      .forward(request, response);
              break;
            }
            EncriptPass ep = new EncriptPass();
            String salts = ep.createSalts();
            String encriptPass = ep.setEncriptPass(password1, salts);
            Person person = new Person(name, surname, email);
            personFacade.create(person);
            User user = new User(login, encriptPass, salts, person);
            userFacade.create(user);
            request.setAttribute("reader", user);
            request.getRequestDispatcher(PageReturner.getPage("welcome"))
                    .forward(request, response);
                break;
            }
        
        case "/showListUsers":
            if(!sl.isRole(regUser, RolesList.ADMINISTRATOR.toString())){
                request.setAttribute("info", "У вас нет прав доступа к ресурсу");
                request.getRequestDispatcher(PageReturner.getPage("showLogin"))
                        .forward(request, response);
                break;
            } 
            List<User> listUsers = userFacade.findAll();
            request.setAttribute("listUsers", listUsers);
            request.getRequestDispatcher(PageReturner.getPage("listUsers")).forward(request, response);
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
