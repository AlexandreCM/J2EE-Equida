/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.ChevalDAO;
import database.CourseDAO;
import database.Utilitaire;
import formulaires.ChevalForm;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modele.Cheval;
import modele.Course;
import modele.TypeCheval;

/**
 *
 * @author ts2slam
 */
public class ServletCheval extends HttpServlet {
    
    Connection connection ;
    
    @Override
    public void init()
    {     
        ServletContext servletContext=getServletContext();
        connection=(Connection)servletContext.getAttribute("connection");
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response, Cheval cheval)
            throws ServletException, IOException {
       /* response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            //TODO output your page here. You may use following sample code. 
        
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ServletCheval</title>");            
            out.println("</head>");
            out.println("<body>");
                out.println("<h1>Servlet ServletCheval at " + request.getContextPath() + "</h1>");
                out.println(cheval.getNom());
            out.println("</body>");
            out.println("</html>");
        }
        */
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
        //processRequest(request, response);
        
        
        String url = request.getRequestURI();
        // Récup et affichage toute les chevaux      
        if(url.equals("/EQUI01/ServletCheval/listerLesChevaux"))
        {
            ArrayList<Cheval> lesChevaux = ChevalDAO.getLesChevaux(connection);
            request.setAttribute("pLesChevaux", lesChevaux);
            getServletContext().getRequestDispatcher("/vues/cheval/listerLesChevaux.jsp").forward(request, response);
        }
        
        if(url.equals("/EQUI01/ServletCheval/detailCheval"))
        {  
            
            
            int codeCheval = Integer.parseInt(request.getParameter("codeCheval"));
            
            
           Cheval unCheval = ChevalDAO.getDetailCheval(connection, codeCheval);
           
           /*for (int i=0; i<unCheval.getLesParticipations().size(); i++){
               
               System.out.println("particip   =    " + unCheval.getLesParticipations().get(i).getPlace() + " nom course  " +unCheval.getLesParticipations().get(i).getUneCourse().getNom() );
           }

           System.out.println("jkghjk");*/
           //processRequest(request, response, unCheval);
            request.setAttribute("pUnCheval", unCheval);
            getServletContext().getRequestDispatcher("/vues/cheval/chevalDetail.jsp").forward(request, response);
        }
        
        if(url.equals("/EQUI01/ServletCheval/listerLesCourses"))
        {
            ArrayList<Course> lesCourses = CourseDAO.getLesCourses(connection);
            request.setAttribute("pLesCourses", lesCourses);
            //System.out.println("les courses");
            getServletContext().getRequestDispatcher("/vues/course/listerLesCourses.jsp").forward(request, response);
        }
        
        if(url.equals("/EQUI01/ServletCheval/ajouterCheval"))
        {
            ArrayList<TypeCheval> lesTypesCheval = ChevalDAO.getLesTypesCheval(connection);
            request.setAttribute("pLesTypes", lesTypesCheval);
            /*System.out.println("liste type:");
            for (TypeCheval uneType : lesTypesCheval) {
                System.out.println(uneType.getLibelle());
            }
            System.out.println("fin liste");*/
            
            this.getServletContext().getRequestDispatcher("/vues/cheval/ajouterCheval.jsp" ).forward( request, response );
        }
        
        
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
            throws ServletException, IOException 
    {
        
        /* Préparation de l'objet formulaire */
        ChevalForm form = new ChevalForm();
		
        /* Appel au traitement et à la validation de la requête, et récupération du bean en résultant */
        Cheval unCheval = form.ajouterCheval(request);
        
        /* Stockage du formulaire et de l'objet dans l'objet request */
        request.setAttribute( "form", form );
        request.setAttribute( "pUnCheval", unCheval );
		
        if (form.getErreurs().isEmpty()){
            // Il n'y a pas eu d'erreurs de saisie, donc on renvoie la vue affichant les infos du lot
            ChevalDAO.ajouterCheval(connection, unCheval);
            this.getServletContext().getRequestDispatcher("/vues/cheval/afficherUnCheval.jsp" ).forward(request, response);
        }
        else
        { 
		// il y a des erreurs. On réaffiche le formulaire avec des messages d'erreurs
            
            ArrayList<TypeCheval> lesTypes = ChevalDAO.getLesTypesCheval(connection);
            request.setAttribute("pLesTypes", lesTypes);
            
            this.getServletContext().getRequestDispatcher("/vues/cheval/ajouterCheval.jsp" ).forward(request, response);
        }
        
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
    
    public void destroy(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
    {
        try
        {
            //fermeture
            System.out.println("Connexion fermée");
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            System.out.println("Erreur lors de l’établissement de la connexion");
        }
        finally
        {
            //Utilitaire.fermerConnexion(rs);
            //Utilitaire.fermerConnexion(requete);
            Utilitaire.fermerConnexion(connection);
        }
    }

}
