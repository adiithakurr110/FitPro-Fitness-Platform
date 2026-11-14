<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Page Not Found" />
<%@ include file="header.jspf" %>

<div class="auth-wrapper" style="max-width: 520px; margin: 60px auto;">
  <div class="auth-card" style="text-align: center;">
    <div class="brand-emblem" style="margin: 0 auto 16px; width: 44px; height: 44px; font-size: 1.3rem; background: var(--bg-canvas); color: var(--text-tertiary);">
      <i class="fa-solid fa-compass"></i>
    </div>
    <h1 style="font-size: 2rem; font-weight: 700; letter-spacing: -0.025em; margin-bottom: 8px;">The page you’re looking for can’t be found.</h1>
    <p class="muted" style="margin-bottom: 28px; line-height: 1.5;">
      The requested resource may have moved, or you may have entered an unrecognized URL.
    </p>
    <div style="display: flex; gap: 12px; justify-content: center;">
      <a class="btn primary-btn" href="${ctx}/">
        <i class="fa-solid fa-house"></i> Return to FitPro Home
      </a>
      <a class="btn outline-btn" href="${ctx}/app/dashboard">
        <i class="fa-solid fa-chart-line"></i> Go to Dashboard
      </a>
    </div>
  </div>
</div>

<%@ include file="footer.jspf" %>
