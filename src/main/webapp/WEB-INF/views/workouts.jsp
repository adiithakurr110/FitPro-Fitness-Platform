<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Workouts" />
<%@ include file="header.jspf" %>

<div class="page-header">
  <div>
    <h1><i class="fa-solid fa-dumbbell"></i> Workout Programs</h1>
    <p class="muted">Discover and enroll in expert-designed fitness programs.</p>
  </div>
</div>

<div class="filters">
  <a class="chip ${empty activeType ? 'active' : ''}" href="${ctx}/app/workouts"><i class="fa-solid fa-grid-2"></i> All Categories</a>
  <a class="chip ${activeType == 'CARDIO' ? 'active' : ''}" href="${ctx}/app/workouts?type=CARDIO"><i class="fa-solid fa-heart-pulse"></i> Cardio</a>
  <a class="chip ${activeType == 'STRENGTH' ? 'active' : ''}" href="${ctx}/app/workouts?type=STRENGTH"><i class="fa-solid fa-dumbbell"></i> Strength</a>
  <a class="chip ${activeType == 'YOGA' ? 'active' : ''}" href="${ctx}/app/workouts?type=YOGA"><i class="fa-solid fa-spa"></i> Yoga</a>
</div>

<c:if test="${sessionScope.user.trainer}">
  <details class="card publish-card">
    <summary class="publish-summary"><i class="fa-solid fa-plus-circle"></i> <strong>Publish a New Program</strong></summary>
    <form method="post" action="${ctx}/app/workouts" class="validate grid two mt-3">
      <input type="hidden" name="action" value="create">
      <label>Program Title <input type="text" name="title" minlength="3" placeholder="e.g. 30-Day HIIT Challenge" required></label>
      <label>Duration (minutes) <input type="number" name="duration" min="5" max="240" value="30" required></label>
      <label>Category Type
        <select name="type"><option>CARDIO</option><option>STRENGTH</option><option>YOGA</option></select>
      </label>
      <label>Difficulty Level
        <select name="level"><option>BEGINNER</option><option>INTERMEDIATE</option><option>ADVANCED</option></select>
      </label>
      <label>Intensity Level (MET 1-15) <input type="number" name="met" min="1" max="15" step="0.5" value="5" required></label>
      <label>Description <input type="text" name="description" maxlength="500" placeholder="Brief overview of training routine"></label>
      <button class="btn primary-btn full-width" type="submit"><i class="fa-solid fa-cloud-arrow-up"></i> Publish Program</button>
    </form>
  </details>
</c:if>

<div class="grid three workout-grid">
  <c:forEach var="w" items="${workouts}">
    <article class="card workout-card">
      <div class="workout-card-header">
        <span class="badge <c:out value='${w.type}'/>"><c:out value="${w.type}"/></span>
        <span class="level-tag"><i class="fa-solid fa-layer-group"></i> <c:out value="${w.level}"/></span>
      </div>
      <h3><c:out value="${w.title}"/></h3>
      <p class="muted workout-desc"><c:out value="${w.description}"/></p>
      <div class="workout-meta-bar">
        <span><i class="fa-regular fa-clock"></i> <c:out value="${w.durationMin}"/> min</span>
        <span><i class="fa-solid fa-user-ninja"></i> Coach: <c:out value="${w.trainerName}" default="FitPro Team"/></span>
      </div>
      <div class="workout-card-footer">
        <c:if test="${!sessionScope.user.trainer}">
          <c:choose>
            <c:when test="${enrolledIds.contains(w.id)}">
              <span class="enrolled-badge"><i class="fa-solid fa-circle-check"></i> Enrolled</span>
            </c:when>
            <c:otherwise>
              <form method="post" action="${ctx}/app/workouts" class="enroll-form">
                <input type="hidden" name="action" value="enroll">
                <input type="hidden" name="workoutId" value="${w.id}">
                <button class="btn primary-btn full-width" type="submit"><i class="fa-solid fa-user-plus"></i> Enroll Now</button>
              </form>
            </c:otherwise>
          </c:choose>
        </c:if>
      </div>
    </article>
  </c:forEach>
  <c:if test="${empty workouts}">
    <div class="empty-state full-width">
      <i class="fa-solid fa-magnifying-glass empty-icon"></i>
      <p class="muted">No programs found for this filter.</p>
    </div>
  </c:if>
</div>

<%@ include file="footer.jspf" %>
