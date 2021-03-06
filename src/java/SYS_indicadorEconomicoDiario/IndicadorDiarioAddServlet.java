/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SYS_indicadorEconomicoDiario;

import Helpers.Format;
import SYS_indicadorEconomicoEstadistico.IndicadorEconomicoDAO;
import java.io.IOException;
import java.sql.Connection;
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
@WebServlet(name = "IndicadorDiarioAddServlet", urlPatterns = {"/IndicadorDiarioAddServlet"})
public class IndicadorDiarioAddServlet extends HttpServlet {

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

        Connection conexion = null;

        ///////////////////////////
        // ESTABLECER CONEXION
        ///////////////////////////
        try {
            conexion = ds.getConnection();

            IndicadorDiarioDAO idDAO = new IndicadorDiarioDAO();
            idDAO.setConexion(conexion);

            ////////////////////////
            // COMPROBAR SESSION
            ////////////////////////
            try {
                /* recuperar sesion */
                HttpSession session = request.getSession(false);

                /* obtener parametros de session */
                int idUserX = Integer.parseInt((String) session.getAttribute("idUserX"));
                int userTypeX = Integer.parseInt((String) session.getAttribute("userTypeX"));
                String usernameX = (String) session.getAttribute("usernameX");

                ///////////////////////////////////
                // COMPROBAR PERMISOS DE USUARIO
                ///////////////////////////////////

                /* comprobar permisos de usuario */
                if (userTypeX > 4) {
                    request.getRequestDispatcher("/ForbiddenServlet").forward(request, response);
                } else {
                    /* establecer variables de usuario en sesion */
                    request.setAttribute("idUserX", idUserX);
                    request.setAttribute("usernameX", usernameX);
                    request.setAttribute("userTypeX", userTypeX);

                    ////////////////////////////////////
                    // RECIBIR Y COMPROBAR PARAMETROS
                    ////////////////////////////////////

                    String suf = request.getParameter("uf");
                    String seuro = request.getParameter("euro");
                    String sdolar = request.getParameter("dolar");                    

                    /* establecer variables de sesion */
                    session.setAttribute("redirectAdd", "indicadorDiario");
                    session.setAttribute("uf", suf);
                    session.setAttribute("euro", seuro);
                    session.setAttribute("dolar", sdolar);                    

                    /* instanciar indicador */
                    IndicadorEconomicoDiarioBean indicador = new IndicadorEconomicoDiarioBean();

                    /* flag de error */
                    boolean error = false;

                    /* comprobar UF */
                    if (suf == null || suf.trim().equals("")) {
                        session.setAttribute("msgErrorUF", "Debe ingresar el valor de la UF.");
                        error = true;
                    } else {
                        try {
                            indicador.setUf(Float.parseFloat(suf));
                        } catch (NumberFormatException n) {
                            session.setAttribute("msgErrorUF", "El valor de la UF debe ser numérico.");
                            error = true;
                        }
                    }

                    /* comprobar euro */
                    if (seuro == null || seuro.trim().equals("")) {
                        session.setAttribute("msgErrorEuro", "Debe ingresar el valor del Euro.");
                        error = true;
                    } else {
                        try {
                            indicador.setEuro(Float.parseFloat(seuro));
                        } catch (NumberFormatException n) {
                            session.setAttribute("msgErrorEuro", "El valor del Euro debe ser numérico.");
                            error = true;
                        }
                    }

                    /* comprobar dolar */
                    if (sdolar == null || sdolar.trim().equals("")) {
                        session.setAttribute("msgErrorDolar", "Debe ingresar el valor del Dólar.");
                        error = true;
                    } else {
                        try {
                            indicador.setDolar(Float.parseFloat(sdolar));
                        } catch (NumberFormatException n) {
                            session.setAttribute("msgErrorDolar", "El valor del Dólar debe ser numérico.");
                            error = true;
                        }
                    }     
                    
                    /* establecer id user */
                    indicador.setIdUser(idUserX);

                    ///////////////////////
                    // LOGICA DE NEGOCIO
                    ///////////////////////

                    /* comprobar fecha duplicada - Atencion: un indicador no puede ser ingresado dos veces el mismo dia */
                    try {
                        String dateDup = idDAO.indicadorDiarioGetLastDate();

                        /* comparar ultima fecha con fecha actual */
                        if (Format.currentDateYYMMDDDD().equals(Format.dateYYYYMMDD(dateDup))) {
                            session.setAttribute("msgErrorAdd", "El indicador para hoy ya fue ingresado.");
                            error = true;
                        }
                    } catch (Exception ex) {
                        session.setAttribute("msgErrorAdd", "Ha ocurrido un problema y no puede comprobar las fechas. Error:" + ex.getLocalizedMessage());
                        error = true;
                        ex.getCause();
                    }

                    /* actualizar registro */
                    if (!error) {
                        try {
                            idDAO.indicadorDiarioInsert(indicador);
                            session.setAttribute("msgOk", "Registro ingresado exitosamente.");
                        } catch (Exception ex) {
                            session.setAttribute("msgErrorAdd", "Ha ocurrido un problema y no puede ingresar el nuevo registro. Error:" + ex.getLocalizedMessage());
                            ex.getCause();
                        }
                    }

                    /* send redirect */
                    response.sendRedirect("/ERP/IndicadorDiarioGetAddServlet");
                }
            } catch (Exception sessionException) {
                /* enviar a la vista de login */
                System.out.println("no ha iniciado session");
                request.getRequestDispatcher("/login/login.jsp").forward(request, response);
            }
        } catch (Exception connectionException) {
            connectionException.printStackTrace();
        } finally {
            /* cerrar conexion */
            try {
                conexion.close();
            } catch (Exception noGestionar) {
            }
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
