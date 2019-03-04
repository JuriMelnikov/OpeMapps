/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entity.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.ImageFolder;
import util.PageReturner;

/**
 *
 * @author jvm
 */
@WebServlet(name = "UserController", urlPatterns = {
    "/showUpload",
    
})
public class UserController extends HttpServlet {
private String imageFolder;
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
        request.setCharacterEncoding("UTF-8");
        this.imageFolder = ImageFolder.getImageFolder("pathToImageFolder");
        HttpSession session = request.getSession(false);
        if(session == null){
            request.setAttribute("info", "Необходима авторизация");
            request.getRequestDispatcher(PageReturner.getPage("showLogin")).forward(request, response);
            return;
        }
        User regUser = (User) session.getAttribute("regUser");
        if(regUser == null){
            request.setAttribute("info", "Необходима авторизация");
            request.getRequestDispatcher(PageReturner.getPage("showLogin")).forward(request, response);
            return;
        }
        String path = request.getServletPath();
        if(path != null)
            switch (path) {
                case "/showUpload":
                    String pathDir = this.imageFolder+request.getContextPath()+File.separator+regUser.getId().toString();
                    List<File> filesInFolder;
                    try {
                        filesInFolder = Files.walk(Paths.get(pathDir))
                                .filter(Files::isRegularFile)
                                .map(Path::toFile)
                                .collect(Collectors.toList());
                    } catch (Exception e) {
                        filesInFolder=new ArrayList<>();
                    }
                    
                    request.setAttribute("filesInFolder", filesInFolder);
                    request.getRequestDispatcher("/showUpload.jsp").forward(request, response);
                    break;
                case "":
                    
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
