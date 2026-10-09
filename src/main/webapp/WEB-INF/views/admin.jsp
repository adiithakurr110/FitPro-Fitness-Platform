<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Admin Portal" />
<%@ include file="header.jspf" %>

<section class="hero admin-hero">
  <div class="hero-content">
    <h1><i class="fa-solid fa-user-shield"></i> Admin & Trainer Portal</h1>
    <p class="muted">Manage all enrolled members, view subscriptions, and monitor platform activity.</p>
  </div>
</section>

<div class="stats grid three">
  <div class="stat card-glow-primary">
    <div class="stat-icon"><i class="fa-solid fa-users"></i></div>
    <div class="stat-info">
      <span class="num"><c:out value="${enrollments.size()}"/></span>
      <span class="label">Total Enrolled Members</span>
    </div>
  </div>
  <div class="stat card-glow-success">
    <div class="stat-icon"><i class="fa-solid fa-id-card"></i></div>
    <div class="stat-info">
      <span class="num"><c:out value="${users.size()}"/></span>
      <span class="label">Registered Users</span>
    </div>
  </div>
  <div class="stat card-glow-accent">
    <div class="stat-icon"><i class="fa-solid fa-dumbbell"></i></div>
    <div class="stat-info">
      <span class="num"><c:out value="${workouts.size()}"/></span>
      <span class="label">Published Programs</span>
    </div>
  </div>
</div>

<!-- Section 1: All Persons Enrolled -->
<div class="card admin-section">
  <div class="section-header">
    <h2><i class="fa-solid fa-list-check"></i> Enrolled Members Directory</h2>
    <p class="muted">Complete list of members enrolled in workout programs.</p>
  </div>

  <c:choose>
    <c:when test="${empty enrollments}">
      <div class="empty-state">
        <i class="fa-solid fa-folder-open empty-icon"></i>
        <p class="muted">No member enrollments recorded yet.</p>
      </div>
    </c:when>
    <c:otherwise>
      <div class="table-wrap">
        <table class="styled-table">
          <thead>
            <tr>
              <th>#</th>
              <th>Member Name</th>
              <th>Email</th>
              <th>Enrolled Workout</th>
              <th>Category</th>
              <th>Enrolled Date</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="e" items="${enrollments}" varStatus="status">
              <tr>
                <td><c:out value="${status.index + 1}"/></td>
                <td><strong><c:out value="${e.userName}"/></strong></td>
                <td><a href="mailto:${e.userEmail}" class="email-link"><i class="fa-regular fa-envelope"></i> <c:out value="${e.userEmail}"/></a></td>
                <td><span class="highlight-text"><c:out value="${e.workoutTitle}"/></span></td>
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

<!-- Section 2: All System Users -->
<div class="card admin-section">
  <div class="section-header">
    <h2><i class="fa-solid fa-users-gear"></i> User Directory</h2>
    <p class="muted">All registered Member and Trainer accounts.</p>
  </div>
  <div class="table-wrap">
    <table class="styled-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Full Name</th>
          <th>Email Address</th>
          <th>Role</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="u" items="${users}">
          <tr>
            <td>#<c:out value="${u.id}"/></td>
            <td><strong><c:out value="${u.fullName}"/></strong></td>
            <td><c:out value="${u.email}"/></td>
            <td>
              <span class="role-badge ${u.trainer ? 'trainer-role' : 'member-role'}">
                <i class="fa-solid ${u.trainer ? 'fa-user-ninja' : 'fa-user'}"></i> <c:out value="${u.role}"/>
              </span>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
</div>

<%@ include file="footer.jspf" %>
