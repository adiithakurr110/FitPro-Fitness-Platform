<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Register" />
<%@ include file="header.jspf" %>
<section class="card auth">
  <h1>Join FitPro</h1>
  <p class="muted">Create your free account in a minute.</p>
  <form method="post" action="${ctx}/register" class="validate">
    <label>Full name
      <input type="text" name="fullName" value="<c:out value='${fullName}'/>" minlength="2" required>
    </label>
    <label>Email
      <input type="email" name="email" value="<c:out value='${email}'/>" required>
    </label>
    <label>Password (min 6 characters)
      <input type="password" name="password" minlength="6" required>
    </label>
    <label>I am a
      <select name="role">
        <option value="MEMBER">Member (I want to train)</option>
        <option value="TRAINER">Trainer (I publish programs)</option>
      </select>
    </label>
    <button class="btn" type="submit">Create account</button>
  </form>
  <p class="muted">Already registered? <a href="${ctx}/login">Log in</a></p>
</section>
<%@ include file="footer.jspf" %>
