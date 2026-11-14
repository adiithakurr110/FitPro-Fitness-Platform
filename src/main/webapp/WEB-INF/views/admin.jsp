<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Admin Portal" />
<%@ include file="header.jspf" %>

<div class="page-header">
  <span class="eyebrow"><i class="fa-solid fa-shield-halved"></i> Control Plane</span>
  <h1 class="page-title">Platform Administration Center</h1>
  <p class="page-desc">
    Manage user roles, moderate workout catalogue content, and monitor real-time platform diagnostics.
  </p>
</div>

<!-- Telemetry Summary Cards -->
<div class="grid four" style="margin-bottom: 28px;">
  <div class="stat-card">
    <div class="stat-header">
      <span class="stat-label">Registered Users</span>
      <div class="stat-icon"><i class="fa-solid fa-users"></i></div>
    </div>
    <div class="stat-num"><c:out value="${users.size()}"/></div>
  </div>
  <div class="stat-card">
    <div class="stat-header">
      <span class="stat-label">Active Programs</span>
      <div class="stat-icon"><i class="fa-solid fa-dumbbell"></i></div>
    </div>
    <div class="stat-num"><c:out value="${workouts.size()}"/></div>
  </div>
  <div class="stat-card">
    <div class="stat-header">
      <span class="stat-label">Total Subscriptions</span>
      <div class="stat-icon"><i class="fa-solid fa-user-check"></i></div>
    </div>
    <div class="stat-num"><c:out value="${enrollments.size()}"/></div>
  </div>
  <div class="stat-card">
    <div class="stat-header">
      <span class="stat-label">Completed Sessions</span>
      <div class="stat-icon"><i class="fa-solid fa-fire"></i></div>
    </div>
    <div class="stat-num"><c:out value="${logs.size()}"/></div>
  </div>
</div>

<!-- Section 1: User & Role Management -->
<div class="card" style="margin-bottom: 28px;">
  <div class="section-header-bar">
    <div>
      <h2 class="section-title"><i class="fa-solid fa-users-gear"></i> User &amp; Role Management</h2>
      <p class="muted" style="font-size: 0.9rem; margin-top: 2px;">Review accounts, reassign roles, or delete users.</p>
    </div>
    <span class="badge neutral"><c:out value="${users.size()}"/> Accounts</span>
  </div>

  <div class="table-wrap">
    <table class="styled-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Full Name</th>
          <th>Email</th>
          <th>Assigned Role</th>
          <th>Change Role</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="u" items="${users}">
          <tr>
            <td>#<c:out value="${u.id}"/></td>
            <td><strong><c:out value="${u.fullName}"/></strong></td>
            <td><a href="mailto:${u.email}"><c:out value="${u.email}"/></a></td>
            <td>
              <span class="badge ${u.admin ? 'ADMIN' : (u.trainer ? 'STRENGTH' : 'neutral')}">
                <c:out value="${u.role}"/>
              </span>
            </td>
            <td>
              <form method="post" action="${ctx}/app/admin" style="display: flex; gap: 8px; align-items: center;">
                <input type="hidden" name="action" value="updateRole">
                <input type="hidden" name="userId" value="${u.id}">
                <select name="newRole" style="padding: 4px 8px; font-size: 0.82rem; width: auto; height: 32px; border-radius: var(--radius-pill);">
                  <option value="MEMBER" ${u.role == 'MEMBER' ? 'selected' : ''}>MEMBER</option>
                  <option value="TRAINER" ${u.role == 'TRAINER' ? 'selected' : ''}>TRAINER</option>
                  <option value="ADMIN" ${u.role == 'ADMIN' ? 'selected' : ''}>ADMIN</option>
                </select>
                <button type="submit" class="btn small-btn outline-btn" style="padding: 4px 10px;">
                  Update
                </button>
              </form>
            </td>
            <td>
              <c:if test="${u.id != sessionScope.user.id}">
                <form method="post" action="${ctx}/app/admin" onsubmit="return confirm('Delete user account and all their records?');" style="display: inline;">
                  <input type="hidden" name="action" value="deleteUser">
                  <input type="hidden" name="userId" value="${u.id}">
                  <button type="submit" class="btn small-btn outline-btn" style="color: var(--alert-err-text); border-color: var(--alert-err-border); padding: 4px 10px;">
                    <i class="fa-solid fa-trash-can"></i>
                  </button>
                </form>
              </c:if>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
</div>

<!-- Section 2: Workout Content Management -->
<div class="card" style="margin-bottom: 28px;">
  <div class="section-header-bar">
    <div>
      <h2 class="section-title"><i class="fa-solid fa-dumbbell"></i> Workout Content Catalogue Moderation</h2>
      <p class="muted" style="font-size: 0.9rem; margin-top: 2px;">Review and moderate published workout programs.</p>
    </div>
    <a href="${ctx}/app/workouts" class="btn small-btn blue-btn"><i class="fa-solid fa-plus"></i> Add Workout</a>
  </div>

  <div class="table-wrap">
    <table class="styled-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Program Title</th>
          <th>Category</th>
          <th>Difficulty</th>
          <th>Duration</th>
          <th>Intensity (MET)</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="w" items="${workouts}">
          <tr>
            <td>#<c:out value="${w.id}"/></td>
            <td><strong><c:out value="${w.title}"/></strong></td>
            <td><span class="badge <c:out value='${w.type}'/>"><c:out value="${w.type}"/></span></td>
            <td><span class="badge neutral"><c:out value="${w.level}"/></span></td>
            <td><c:out value="${w.durationMin}"/> min</td>
            <td><c:out value="${w.metValue}"/></td>
            <td>
              <form method="post" action="${ctx}/app/admin" onsubmit="return confirm('Remove program from catalogue?');" style="display: inline;">
                <input type="hidden" name="action" value="deleteWorkout">
                <input type="hidden" name="workoutId" value="${w.id}">
                <button type="submit" class="btn small-btn outline-btn" style="color: var(--alert-err-text); padding: 4px 10px;">
                  <i class="fa-solid fa-trash-can"></i> Remove
                </button>
              </form>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
</div>

<!-- Section 3: Platform Telemetry & System Diagnostics -->
<div class="grid two" style="margin-bottom: 28px;">
  <div class="card">
    <div class="section-header-bar">
      <h2 class="section-title"><i class="fa-solid fa-microchip"></i> JVM &amp; Server Diagnostics</h2>
      <span class="badge neutral">Active</span>
    </div>
    <div style="display: flex; flex-direction: column; gap: 12px; margin-top: 10px; font-size: 0.92rem;">
      <div style="display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid var(--hairline);">
        <span class="muted">Java Runtime Version</span>
        <strong><c:out value="${javaVersion}" default="Java 11+"/></strong>
      </div>
      <div style="display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid var(--hairline);">
        <span class="muted">Allocated JVM Memory</span>
        <strong><c:out value="${jvmUsedMemory}"/> MB / <c:out value="${jvmTotalMemory}"/> MB</strong>
      </div>
      <div style="display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid var(--hairline);">
        <span class="muted">Available CPU Cores</span>
        <strong><c:out value="${jvmAvailableProcessors}"/> Cores</strong>
      </div>
      <div style="display: flex; justify-content: space-between; padding: 8px 0;">
        <span class="muted">Relational Database Mode</span>
        <strong>Embedded H2 (MySQL Compatible)</strong>
      </div>
    </div>
  </div>

  <div class="card">
    <div class="section-header-bar">
      <h2 class="section-title"><i class="fa-solid fa-network-wired"></i> Architecture &amp; Security Specs</h2>
    </div>
    <div style="display: flex; flex-direction: column; gap: 12px; margin-top: 10px; font-size: 0.92rem;">
      <div style="display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid var(--hairline);">
        <span class="muted">Authentication Mechanism</span>
        <strong>Salted PBKDF2 (SHA-256)</strong>
      </div>
      <div style="display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid var(--hairline);">
        <span class="muted">Filter Security Barrier</span>
        <strong>AuthFilter on /app/*</strong>
      </div>
      <div style="display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid var(--hairline);">
        <span class="muted">Thread Safety Mechanism</span>
        <strong>NotificationService Async Executor</strong>
      </div>
      <div style="display: flex; justify-content: space-between; padding: 8px 0;">
        <span class="muted">Object Polymorphism</span>
        <strong>CalorieCalculable &amp; User Model Hierarchy</strong>
      </div>
    </div>
  </div>
</div>

<!-- Section 4: All Enrollments Directory -->
<div class="card">
  <div class="section-header-bar">
    <div>
      <h2 class="section-title"><i class="fa-solid fa-clipboard-user"></i> Master Enrollment Log</h2>
      <p class="muted" style="font-size: 0.9rem; margin-top: 2px;">Comprehensive mapping of members and their enrolled workout tracks.</p>
    </div>
  </div>

  <c:choose>
    <c:when test="${empty enrollments}">
      <div class="empty-state">
        <i class="fa-solid fa-folder-open empty-icon"></i>
        <p class="muted">No enrollments currently recorded.</p>
      </div>
    </c:when>
    <c:otherwise>
      <div class="table-wrap">
        <table class="styled-table">
          <thead>
            <tr>
              <th>#</th>
              <th>Member Name</th>
              <th>Email Address</th>
              <th>Program Title</th>
              <th>Category</th>
              <th>Enrolled Date</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="e" items="${enrollments}" varStatus="status">
              <tr>
                <td><c:out value="${status.index + 1}"/></td>
                <td><strong><c:out value="${e.userName}"/></strong></td>
                <td><a href="mailto:${e.userEmail}"><c:out value="${e.userEmail}"/></a></td>
                <td><c:out value="${e.workoutTitle}"/></td>
                <td><span class="badge <c:out value='${e.workoutType}'/>"><c:out value="${e.workoutType}"/></span></td>
                <td><c:out value="${e.enrolledAt}"/></td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </c:otherwise>
  </c:choose>
</div>

<%@ include file="footer.jspf" %>
