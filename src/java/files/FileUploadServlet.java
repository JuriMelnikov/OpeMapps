/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package files;

import entity.User;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import util.FileResizer;
import util.ImageFolder;
import util.PageReturner;

/**
 *
 * @author jvm
 */
@WebServlet(name = "FileUploadServlet", urlPatterns = {"/upload"})
@MultipartConfig()
public class FileUploadServlet extends HttpServlet {
    
    private String imageFolder;
    
    @Override
    public void init() throws ServletException {
        
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
        this.imageFolder  = ImageFolder.getImageFolder("pathToImageFolder");
        // Укажите в в поле imageFolder путь к каталогу, где будут храниться загруженные файлы (изображения)
        // Не забудьте дать права этой директории на запись и чтение 
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
        String pathDir = this.imageFolder+request.getContextPath()+File.separator+regUser.getId().toString();
        new File(pathDir).mkdirs();
        List<Part> fileParts = request.getParts().stream().filter(part->"file".equals(part.getName()))
                .collect(Collectors.toList());
        for(Part filePart : fileParts){
//            //Path fileName = Paths.get(filePart.getSubmittedFileName()).getFileName();
            String path = pathDir+File.separatorChar+getFileName(filePart);
//            File targetFile = new File(path);
            File tempFile = new File("/tmp/"+getFileName(filePart));
            File parent = tempFile.getParentFile();
            if(!parent.exists() 
                    && 
                    !parent.mkdirs()){
                throw new IllegalStateException("Couldn't create dir: " + parent);
            }
            try(InputStream fileContent = filePart.getInputStream()){
                Files.copy(fileContent, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            writeToFile(FileResizer.resize(tempFile), path);
            tempFile.delete();
        }
        List<File> filesInFolder = Files.walk(Paths.get(pathDir))
                                .filter(Files::isRegularFile)
                                .map(Path::toFile)
                                .collect(Collectors.toList());
        request.setAttribute("filesInFolder", filesInFolder);
        request.getRequestDispatcher("/showUpload.jsp").forward(request, response);
    }
    
    
    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
    public void writeToFile(byte[] data, String fileName) throws IOException{
        try (FileOutputStream out = new FileOutputStream(fileName)) {
            out.write(data);
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
