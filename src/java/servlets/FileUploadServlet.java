/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
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
        ResourceBundle pageName = ResourceBundle.getBundle("properties.pathToImageFolder");
        this.imageFolder=pageName.getString("pathToImageFolder");
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
        // Укажите в в поле imageFolder путь к каталогу, где будут храниться загруженные файлы (изображения)
        // Не забудьте дать права этой директории на запись и чтение 
        HttpSession session = request.getSession(false);
        if(session == null){
            request.setAttribute("info", "Необходима авторизация");
            request.getRequestDispatcher(PageReturner.getPage("showLogin")).forward(request, response);
        }
        User regUser = (User) session.getAttribute("regUser");
        if(regUser == null){
            request.setAttribute("info", "Необходима авторизация");
            request.getRequestDispatcher(PageReturner.getPage("showLogin")).forward(request, response);
        }
        String path = this.imageFolder; // инициируется в методе init
        String directoryName = path.concat(regUser.getId().toString());
        //String fileName = id + getTimeStamp() + ".txt";

        
        List<Part> fileParts = request.getParts().stream().filter(part->"file".equals(part.getName()))
                .collect(Collectors.toList());
        for(Part filePart : fileParts){
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            File directory = new File(directoryName);
            if (! directory.exists()){
                directory.mkdir();
                // If you require it to make the entire directory path including parents,
                // use directory.mkdirs(); here instead.
            }
            File nameFile = new File(directory.getName().concat(fileName));
            try(InputStream fileContent = filePart.getInputStream()){
                Files.copy(fileContent, directory.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            
        }
        
//        final Part filePart = request.getPart("file");
//        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
//        
        //final String fileName = (String) getFileName(filePart);
//        File file = new File(directory.getPath()+File.separator+fileName);
//        
//        FileWriter fw = new FileWriter(file.getAbsoluteFile());
//        BufferedWriter bw = new BufferedWriter(fw);
//        FileReader fr = new FileReader(file);
//        bw.write(fr.toString());
//        bw.close();
            
        
        
        
//        OutputStream out = null;
//        InputStream filecontent = null;
//        
//        try {
//            out = new FileOutputStream(new File(this.imageFolder + File.separator
//                    + fileName));
//            filecontent = filePart.getInputStream();
//
//            int read = 0;
//            final byte[] bytes = new byte[1024];
//
//            while ((read = filecontent.read(bytes)) != -1) {
//                out.write(bytes, 0, read);
//            }
//           
//            LOGGER.log(Level.INFO, "Файл {0} начал загружаться в {1}", 
//                    new Object[]{fileName,this.imageFolder});
//        } catch (FileNotFoundException fne) {
//            LOGGER.log(Level.SEVERE, "Проблемы загрузки файла. Error: {0}", 
//                    new Object[]{fne.getMessage()});
//        } finally {
//            if (out != null) {
//                out.close();
//            }
//            if (filecontent != null) {
//                filecontent.close();
//            }
//            
//        }
//        if(!"".equals(fileName)){
//            request.setAttribute("linkImg", this.imageFolder+File.separator+fileName);
//            request.setAttribute("info", "Файл загружен.<br>Ссылка для копирования: "
//                    +this.imageFolder+File.separator+fileName);
//        }else{
//            request.setAttribute("info", "Не выбран файл для загрузки!");
//  
//        }
        request.getRequestDispatcher("/newarticle").forward(request, response);
    }
    
    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
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
