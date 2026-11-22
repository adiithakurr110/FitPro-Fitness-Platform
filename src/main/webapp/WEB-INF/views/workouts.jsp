<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Workouts" />
<%@ include file="header.jspf" %>

<div class="page-header">
  <span class="eyebrow"><i class="fa-solid fa-dumbbell"></i> Fitness Catalogue</span>
  <h1 class="page-title">Curated Training Programs</h1>
  <p class="page-desc">
    Explore expert-designed workouts calibrated for progressive overload, aerobic endurance, and mindful mobility.
  </p>
</div>

<!-- Search and Category Filters -->
<div style="display: flex; justify-content: space-between; align-items: center; gap: 16px; margin-bottom: 28px; flex-wrap: wrap;">
  <div class="filter-bar">
    <a class="filter-chip ${empty activeType ? 'active' : ''}" href="${ctx}/app/workouts">
      <i class="fa-solid fa-border-all"></i> All Programs
    </a>
    <a class="filter-chip ${activeType == 'CARDIO' ? 'active' : ''}" href="${ctx}/app/workouts?type=CARDIO">
      <i class="fa-solid fa-heart-pulse"></i> Cardio
    </a>
    <a class="filter-chip ${activeType == 'STRENGTH' ? 'active' : ''}" href="${ctx}/app/workouts?type=STRENGTH">
      <i class="fa-solid fa-dumbbell"></i> Strength
    </a>
    <a class="filter-chip ${activeType == 'YOGA' ? 'active' : ''}" href="${ctx}/app/workouts?type=YOGA">
      <i class="fa-solid fa-spa"></i> Yoga
    </a>
  </div>

  <form method="get" action="${ctx}/app/workouts" style="position: relative; min-width: 260px; flex: 1; max-width: 360px;">
    <c:if test="${not empty activeType}">
      <input type="hidden" name="type" value="${activeType}">
    </c:if>
    <input type="text" id="workoutSearch" name="q" value="<c:out value='${searchQuery}'/>" placeholder="Search by title, coach, or level..." style="padding-left: 38px; border-radius: var(--radius-pill); height: 42px; width: 100%;">
    <i class="fa-solid fa-magnifying-glass" style="position: absolute; left: 14px; top: 13px; color: var(--text-tertiary); font-size: 0.9rem;"></i>
  </form>
</div>

<!-- Publisher Drawer for Trainers & Admins -->
<c:if test="${sessionScope.user.trainer || sessionScope.user.admin}">
  <details class="card" style="margin-bottom: 28px; cursor: pointer;">
    <summary style="font-weight: 600; font-size: 1.05rem; display: flex; align-items: center; gap: 8px; user-select: none;">
      <i class="fa-solid fa-plus-circle text-primary"></i> <span>Publish a New Workout Program</span>
    </summary>
    <div style="cursor: default; padding-top: 20px;">
      <form method="post" action="${ctx}/app/workouts" class="validate grid two">
        <input type="hidden" name="action" value="create">
        <div class="form-group">
          <label>Program Title</label>
          <input type="text" name="title" minlength="3" placeholder="e.g. 30-Day HIIT Metabolism Boost" required>
        </div>
        <div class="form-group">
          <label>Estimated Duration (minutes)</label>
          <input type="number" name="duration" min="5" max="240" value="30" required>
        </div>
        <div class="form-group">
          <label>Discipline Category</label>
          <select name="type">
            <option value="CARDIO">Cardio Endurance</option>
            <option value="STRENGTH">Strength &amp; Hypertrophy</option>
            <option value="YOGA">Yoga &amp; Mobility</option>
          </select>
        </div>
        <div class="form-group">
          <label>Difficulty Tier</label>
          <select name="level">
            <option value="BEGINNER">Beginner</option>
            <option value="INTERMEDIATE">Intermediate</option>
            <option value="ADVANCED">Advanced</option>
          </select>
        </div>
        <div class="form-group">
          <label>Metabolic Intensity (MET 1.0 – 15.0)</label>
          <input type="number" name="met" min="1" max="15" step="0.5" value="6.0" required>
        </div>
        <div class="form-group">
          <label>Routine Overview &amp; Instructions</label>
          <input type="text" name="description" maxlength="500" placeholder="Summary of sets, intervals, or target muscle groups">
        </div>
        <div style="grid-column: 1 / -1;">
          <button class="btn blue-btn full-width" type="submit" style="height: 44px;">
            <i class="fa-solid fa-cloud-arrow-up"></i> Publish Program to Platform
          </button>
        </div>
      </form>
    </div>
  </details>
</c:if>

<!-- Workouts Grid -->
<div class="grid three workout-grid">
  <c:forEach var="w" items="${workouts}">
    <article class="workout-card">
      <div>
        <div class="workout-card-top">
          <span class="badge <c:out value='${w.type}'/>"><c:out value="${w.type}"/></span>
          <span class="badge neutral"><i class="fa-solid fa-layer-group"></i> <c:out value="${w.level}"/></span>
        </div>
        <h3 class="workout-card-title"><c:out value="${w.title}"/></h3>
        <p class="workout-card-desc"><c:out value="${w.description}"/></p>
      </div>

      <div>
        <div class="workout-meta-ribbon">
          <span><i class="fa-regular fa-clock"></i> <c:out value="${w.durationMin}"/> min</span>
          <span><i class="fa-solid fa-user-ninja"></i> <c:out value="${w.trainerName}" default="FitPro Team"/></span>
        </div>

        <div class="workout-card-action">
          <c:choose>
            <c:when test="${!sessionScope.user.trainer && !sessionScope.user.admin}">
              <c:choose>
                <c:when test="${enrolledIds.contains(w.id)}">
                  <div class="enrolled-check">
                    <i class="fa-solid fa-circle-check"></i> Enrolled
                  </div>
                </c:when>
                <c:otherwise>
                  <form method="post" action="${ctx}/app/workouts">
                    <input type="hidden" name="action" value="enroll">
                    <input type="hidden" name="workoutId" value="${w.id}">
                    <button class="btn primary-btn full-width" type="submit">
                      <i class="fa-solid fa-plus"></i> Enroll in Program
                    </button>
                  </form>
                </c:otherwise>
              </c:choose>
            </c:when>
            <c:otherwise>
              <form method="post" action="${ctx}/app/workouts" onsubmit="return confirm('Remove this workout program from the catalogue?');">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="workoutId" value="${w.id}">
                <button class="btn outline-btn small-btn full-width" type="submit" style="color: var(--alert-err-text);">
                  <i class="fa-solid fa-trash-can"></i> Remove Program
                </button>
              </form>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
    </article>
  </c:forEach>

  <c:if test="${empty workouts}">
    <div class="empty-state" style="grid-column: 1 / -1;">
      <i class="fa-solid fa-magnifying-glass empty-icon"></i>
      <h3 style="font-size: 1.15rem; font-weight: 600;">No programs found</h3>
      <p class="muted">Try adjusting your category filter or search keywords.</p>
    </div>
  </c:if>
</div>

<%@ include file="footer.jspf" %>
