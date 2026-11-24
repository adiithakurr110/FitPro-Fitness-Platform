<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Sign In" />
<%@ include file="header.jspf" %>

<div class="auth-wrapper">
  <div class="auth-card">
    <div class="auth-header">
      <div class="brand-emblem" style="margin: 0 auto 16px; width: 36px; height: 36px; font-size: 1.1rem;">
        <i class="fa-solid fa-bolt"></i>
      </div>
      <h1>Sign in to FitPro</h1>
      <p>Enter your credentials to access your training dashboard.</p>
    </div>

    <form method="post" action="${ctx}/login" class="validate">
      <div class="form-group">
        <label for="loginEmail">Email Address or Username</label>
        <input type="text" id="loginEmail" name="email" value="<c:out value='${email}'/>" placeholder="name@example.com or Full Name" autocomplete="username" required>
      </div>

      <div class="form-group">
        <label for="loginPassword">Password</label>
        <div class="input-password-wrap">
          <input type="password" id="loginPassword" name="password" placeholder="••••••••" autocomplete="current-password" required>
          <button type="button" class="password-toggle-btn" data-target="loginPassword" aria-label="Toggle password visibility">
            <i class="fa-regular fa-eye"></i>
          </button>
        </div>
      </div>

      <button class="btn primary-btn full-width" type="submit" style="height: 46px; margin-top: 10px;">
        Sign In
      </button>
    </form>

    <div class="auth-footer">
      Don't have an account? <a href="${ctx}/register">Create one now</a>
    </div>

    <!-- Quick Demonstration Credentials -->
    <div class="demo-credentials-box">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;">
        <strong><i class="fa-solid fa-key text-primary"></i> Quick Demo Accounts</strong>
        <span class="muted" style="font-size: 0.75rem;">Click to Autofill</span>
      </div>
      <div class="demo-credential-item demo-autofill-btn" data-email="member@fitpro.com" data-pass="member123" style="cursor: pointer;" title="Click to autofill Member credentials">
        <span><i class="fa-solid fa-user"></i> <strong>Member:</strong> member@fitpro.com</span>
        <span>Pass: <code>member123</code> <i class="fa-solid fa-arrow-turn-down" style="font-size: 0.75rem; margin-left: 4px; color: var(--accent-blue);"></i></span>
      </div>
      <div class="demo-credential-item demo-autofill-btn" data-email="trainer@fitpro.com" data-pass="trainer123" style="cursor: pointer;" title="Click to autofill Trainer credentials">
        <span><i class="fa-solid fa-user-ninja"></i> <strong>Trainer:</strong> trainer@fitpro.com</span>
        <span>Pass: <code>trainer123</code> <i class="fa-solid fa-arrow-turn-down" style="font-size: 0.75rem; margin-left: 4px; color: var(--accent-blue);"></i></span>
      </div>
      <div class="demo-credential-item demo-autofill-btn" data-email="admin@fitpro.com" data-pass="admin123" style="cursor: pointer;" title="Click to autofill Admin credentials">
        <span><i class="fa-solid fa-user-shield"></i> <strong>Admin:</strong> admin@fitpro.com</span>
        <span>Pass: <code>admin123</code> <i class="fa-solid fa-arrow-turn-down" style="font-size: 0.75rem; margin-left: 4px; color: var(--accent-blue);"></i></span>
      </div>
    </div>
  </div>
</div>

<%@ include file="footer.jspf" %>
