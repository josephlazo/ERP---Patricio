/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import Helpers.Message;
import Helpers.MessageList;
import Helpers.StringMD;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 *
 * @author patricio
 */
@WebServlet(name = "AdminGetAddServlet", urlPatterns = {"/AdminGetAddServlet"})
public class AdminGetAddServlet extends HttpServlet {

    @Resource(name = "jdbc/ERP")
    private DataSource ds;

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        ///////////////////////
        // COMPROBAR SESSION
        ///////////////////////
        try {
            /* recuperar sesion */
            HttpSession session = request.getSession(false);

            /* obtener parametros de session */
            int access = Integer.parseInt((String) session.getAttribute("access"));
            String user = (String) session.getAttribute("admin");

            /* comprobar permisos de usuario */
            if (access != 777) {
                request.getRequestDispatcher("/ForbiddenServlet").forward(request, response);
            } else {
                /* obtener los valores de session y asignar valores a la jsp */
                request.setAttribute("userJsp", user); // username
                request.setAttribute("access", access); // nivel de acceso

                ////////////////////////////////////
                // RECIBIR Y COMPROBAR PARAMETROS
                ////////////////////////////////////

                /* obtener atributos de session */
                String redirect = (String) session.getAttribute("redirectAdd");
                String username = (String) session.getAttribute("username");
                String email = (String) session.getAttribute("email");
                String stype = (String) session.getAttribute("type");

                /* obtener mensajes de session */
                String msgErrorUsername = (String) session.getAttribute("msgErrorUsername");
                String msgErrorEmail = (String) session.getAttribute("msgErrorEmail");
                String msgErrorPwd1 = (String) session.getAttribute("msgErrorPwd1");
                String msgErrorPwd2 = (String) session.getAttribute("msgErrorPwd2");
                String msgErrorDup = (String) session.getAttribute("msgErrorDup");
                String msgOk = (String) session.getAttribute("msgOk");

                /* limpiar variables de session */
                session.setAttribute("redirectAdd", null);
                session.setAttribute("username", null);
                session.setAttribute("email", null);
                session.setAttribute("type", null);
                session.setAttribute("msgErrorUsername", null);
                session.setAttribute("msgErrorEmail", null);
                session.setAttribute("msgErrorPwd1", null);
                session.setAttribute("msgErrorPwd2", null);
                session.setAttribute("msgErrorDup", null);
                session.setAttribute("msgOk", null);

                /* comprobar redirect */
                if (redirect == null || redirect.trim().equals("")) {
                    request.setAttribute("msg", "Ingrese un nuevo Admin.");

                } else if (redirect.equals("admin")) {
                    /* establecer atributos a la vista */
                    request.setAttribute("username", username);
                    request.setAttribute("email", email);
                    try {
                        request.setAttribute("type", Integer.parseInt(stype));
                    } catch (NumberFormatException n) {
                    }

                    /* instanciar lista de mensajes */
                    Collection<Message> msgList = new ArrayList<Message>();

                    /* comprobar error de username */
                    if (msgErrorUsername == null || msgErrorUsername.trim().equals("")) {
                    } else {
                        request.setAttribute("msgErrorUsername", true);
                        msgList.add(MessageList.addMessage(msgErrorUsername));
                    }

                    /* comprobar error de email */
                    if (msgErrorEmail == null || msgErrorEmail.trim().equals("")) {
                    } else {
                        request.setAttribute("msgErrorEmail", true);
                        msgList.add(MessageList.addMessage(msgErrorEmail));
                    }

                    /* comprobar pwd1 */
                    if (msgErrorPwd1 == null || msgErrorPwd1.trim().equals("")) {
                    } else {
                        request.setAttribute("msgErrorPwd1", true);
                        msgList.add(MessageList.addMessage(msgErrorPwd1));
                    }

                    /* comprobar pwd2 */
                    if (msgErrorPwd2 == null || msgErrorPwd2.trim().equals("")) {
                    } else {
                        request.setAttribute("msgErrorPwd2", true);
                        msgList.add(MessageList.addMessage(msgErrorPwd2));
                    }

                    /* comprobar duplicado */
                    if (msgErrorDup == null || msgErrorDup.trim().equals("")) {
                    } else {
                        request.setAttribute("msgErrorDup", true);
                        msgList.add(MessageList.addMessage(msgErrorDup));
                    }

                    /* comprobar mensaje de exito */
                    if (msgOk == null || msgOk.trim().equals("")) {
                    } else {
                        request.setAttribute("msgOk", msgOk);
                    }

                    /* establecer lista de mensajes a la peticion */
                    if (!msgList.isEmpty()) {
                        request.setAttribute("msgList", msgList);
                    }
                }

                /* despachar a la vista */
                request.getRequestDispatcher("/admin/adminAdd.jsp").forward(request, response);
            }
        } catch (Exception sesionException) {
            /* enviar a la vista de login */
            System.err.println("no ha iniciado session");
            request.getRequestDispatcher("/login/login.jsp").forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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
