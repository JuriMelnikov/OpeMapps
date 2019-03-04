/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entity.Person;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.util.List;
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
import secure.UserRoles;
import session.PersonFacade;
import session.RoleFacade;
import session.UserFacade;
import session.UserRolesFacade;
import util.EncriptPass;
import util.ImageFolder;
import util.PageReturner;

/**
 *
 * @author Melnikov
 */
@WebServlet(loadOnStartup = 1,name = "Secure", urlPatterns = {
    "/login",
    "/logout",
    "/showLogin",
    
    "/showRegistration",
    "/registration",
    
})
public class SecureController extends HttpServlet {
   
    @EJB RoleFacade roleFacade;
    @EJB UserFacade userFacade;
    @EJB UserRolesFacade userRolesFacade;
    @EJB PersonFacade personFacade;
    
     private String imageFolder;

    @Override
    public void init() throws ServletException {
        List<User> listUsers = userFacade.findAll();
        if(listUsers.isEmpty()){
            EncriptPass ep = new EncriptPass();
            String salts = ep.createSalts();
            String encriptPass = ep.setEncriptPass("admin", salts);
            Person person = new Person("Juri", "Melnikov", "juri.melnikov@gmail.com");
            personFacade.create(person);
            User user = new User("admin", encriptPass, salts, person);
            userFacade.create(user);
            Role role = new Role();
            role.setName(RolesList.ADMINISTRATOR.toString());
            roleFacade.create(role);
            role.setName(RolesList.JUHATAJA.toString());
            roleFacade.create(role);
            role.setName(RolesList.RUHMAJUHATAJA.toString());
            roleFacade.create(role);
            role.setName(RolesList.OPILANE.toString());
            roleFacade.create(role);
            
            SecureLogic secureLogic = new SecureLogic();
            secureLogic.setRole(secureLogic.getRole(RolesList.ADMINISTRATOR.toString()), user);
           
            
        }
    }
    
    
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF8");
        this.imageFolder = ImageFolder.getImageFolder("pathToImageFolder");
        EncriptPass ep = new EncriptPass();
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
                case "/showLogin":
                    request.getRequestDispatcher(PageReturner.getPage("showLogin"))
                            .forward(request, response);
                    break;        
                case "/login":
                    String login = request.getParameter("login");
                    String password = request.getParameter("password");
                    request.setAttribute("info", "Нет такого пользователя!");
                    regUser = userFacade.findByLogin(login);
                    if(regUser == null){
                        request.getRequestDispatcher(PageReturner.getPage("showLogin"))
                            .forward(request, response);
                        break;
                    }
                    String salts = regUser.getSalts();
                    String encriptPass = ep.setEncriptPass(password, salts);
                    if(encriptPass.equals(regUser.getPassword())){
                        session = request.getSession(true);
                        session.setAttribute("regUser", regUser);
                        request.setAttribute("info", "Привет "+regUser.getPerson().getName()
                                +"! Вы вошли в систему.");
                        request.getRequestDispatcher(PageReturner.getPage("welcome"))
                                .forward(request, response);
                        break;
                    }
                    request.getRequestDispatcher(PageReturner.getPage("showLogin"))
                            .forward(request, response);
                    break;

                case "/logout":
                    if(session != null){
                        session.invalidate();
                        request.setAttribute("info", "Вы вышли из системы");
                    }
                    request.getRequestDispatcher(PageReturner.getPage("welcome"))
                            .forward(request, response);
                    break;
                    
                case "/showRegistration":
                    request.getRequestDispatcher(PageReturner.getPage("showRegistration")).forward(request, response);
                    break;
                case "/registration":
                    String name = request.getParameter("name");
                    String surname = request.getParameter("surname");
                    String phone = request.getParameter("phone");
                    String email = request.getParameter("email");
                           login = request.getParameter("login");
                    String password1 = request.getParameter("password1");
                    String password2 = request.getParameter("password2");
                    if(!password1.equals(password2)){
                      request.setAttribute("info", "Неправильно введен логин или пароль");  
                      request.setAttribute("name", name);
                      request.setAttribute("surname", surname);
                      request.setAttribute("phone", phone);
                      request.setAttribute("email", email);
                      request.setAttribute("login", login);
                      request.getRequestDispatcher(PageReturner.getPage("welcome"))
                              .forward(request, response);
                      break;
                    }
                    
                    salts = ep.createSalts();
                    encriptPass = ep.setEncriptPass(password1, salts);
                    Person person = new Person(name, surname, email);
                    personFacade.create(person);
                    User user = new User(login, encriptPass, salts, person);
                    userFacade.create(user);
                    Role role = new Role();
                    role = sl.getRole(RolesList.OPILANE.toString());
                    UserRoles ur = new UserRoles();
                    ur.setRole(role);
                    ur.setUser(user);
                    userRolesFacade.create(ur);
                    String pathDir = this.imageFolder+request.getContextPath()+File.separator+user.getId().toString();
                    new File(pathDir).mkdirs();
                    request.setAttribute("reader", user);
                    request.getRequestDispatcher(PageReturner.getPage("welcome"))
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
