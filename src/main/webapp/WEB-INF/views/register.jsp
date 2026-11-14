<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Create Account" />
<%@ include file="header.jspf" %>

<div class="auth-wrapper">
  <div class="auth-card">
    <div class="auth-header">
      <div class="brand-emblem" style="margin: 0 auto 16px; width: 36px; height: 36px; font-size: 1.1rem;">
        <i class="fa-solid fa-bolt"></i>
      </div>
      <h1>Create your FitPro ID</h1>
      <p>Start tracking workouts and reaching your athletic goals.</p>
    </div>

    <form method="post" action="${ctx}/register" class="validate">
      <div class="form-group">
        <label for="regFullName">Full Name</label>
        <input type="text" id="regFullName" name="fullName" value="<c:out value='${fullName}'/>" placeholder="Alex Morgan" minlength="2" autocomplete="name" required>
      </div>

      <div class="form-group">
        <label for="regEmail">Email Address</label>
        <input type="email" id="regEmail" name="email" value="<c:out value='${email}'/>" placeholder="name@example.com" autocomplete="email" required>
      </div>

      <div class="form-group">
        <label for="regPassword">Password (min 6 characters)</label>
        <div class="input-password-wrap">
          <input type="password" id="regPassword" name="password" placeholder="••••••••" minlength="6" autocomplete="new-password" required>
          <button type="button" class="password-toggle-btn" data-target="regPassword" aria-label="Toggle password visibility">
            <i class="fa-regular fa-eye"></i>
          </button>
        </div>
      </div>

      <div class="form-group">
        <label for="regRole">Account Role</label>
        <select id="regRole" name="role" required>
          <option value="MEMBER">Member (I want to train &amp; track progress)</option>
          <option value="TRAINER">Trainer (I coach &amp; publish programs)</option>
          <option value="ADMIN">Administrator (Platform &amp; system management)</option>
        </select>
      </div>

      <button class="btn primary-btn full-width" type="submit" style="height: 46px; margin-top: 10px;">
        Create Account
      </button>
    </form>

    <div class="auth-footer">
      Already have a FitPro ID? <a href="${ctx}/login">Sign in here</a>
    </div>
  </div>
</div>

<%@ include file="footer.jspf" %>
