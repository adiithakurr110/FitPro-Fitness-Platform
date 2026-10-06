<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Login" />
<%@ include file="header.jspf" %>
<section class="card auth">
  <h1>Welcome back</h1>
  <p class="muted">Log in to continue your training.</p>
  <form method="post" action="${ctx}/login" class="validate">
    <label>Email
      <input type="email" name="email" value="<c:out value='${email}'/>" required>
    </label>
    <label>Password
      <input type="password" name="password" required>
    </label>
    <button class="btn" type="submit">Log in</button>
  </form>
  <p class="muted">New here? <a href="${ctx}/register">Create an account</a></p>
</section>
<%@ include file="footer.jspf" %>
