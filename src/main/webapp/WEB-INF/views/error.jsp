<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Oops" />
<%@ include file="header.jspf" %>
<section class="card auth">
  <h1>Something went wrong</h1>
  <p class="muted">The page you asked for could not be shown. Please go back and try again.</p>
  <a class="btn" href="${ctx}/">Back to home</a>
</section>
<%@ include file="footer.jspf" %>
