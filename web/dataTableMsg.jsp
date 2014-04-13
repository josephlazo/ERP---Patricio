<%-- 
    Document   : dataTableMsg
    Created on : 20-03-2014, 11:16:05 AM
    Author     : patricio
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<article class="col-sm-12">       

    <!-- MENSAJE INFORMATIVO -->
    <c:if test="${msg != null}" >
        <div class="alert alert-info fade in">
            <button class="close" data-dismiss="alert">
                ×
            </button>
            <i class="fa-fw fa fa-info"></i>
            <strong>Información!</strong> <c:out value="${msg}" />
        </div>
    </c:if>

    <!-- MENSAJE DE ELIMINACION -->
    <c:if test="${msgDel != null}" >
        <div class="alert alert-warning fade in">
            <button class="close" data-dismiss="alert">
                ×
            </button>
            <i class="fa-fw fa fa-warning"></i>
            <strong>Atención!</strong> <c:out value="${msgDel}" />
        </div>
    </c:if>

    <!-- MENSAJE DE ERROR DE ELIMINACION -->
    <c:if test="${msgErrorConstraint != null}" >
        <div class="alert alert-danger fade in">
            <button class="close" data-dismiss="alert">
                ×
            </button>
            <i class="fa-fw fa fa-times"></i>
            <strong>Error!</strong> <c:out value="${msgErrorConstraint}" />
        </div>
    </c:if>

    <!-- LISTA DE MENSAJES DE ERROR DE ELIMINACION -->
    <c:if test="${msgListErrorConstraint != null}" >
        <div class="alert alert-block alert-danger">
            <a class="close" data-dismiss="alert" href="#">×</a>
            <h4 class="alert-heading"><i class="fa fa-times-circle"></i> Error!</h4>
            <ul class="list-style">                                                                          
                <c:forEach var="msgListErrorConstraint" items="${msgListErrorConstraint}">
                    <li><c:out value="${msgListErrorConstraint.msg}" /></li>
                    </c:forEach>
            </ul>
        </div>
    </c:if>    

</article>
