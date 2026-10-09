<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Dashboard" />
<%@ include file="header.jspf" %>

<section class="hero dashboard-hero">
  <div class="hero-content">
    <span class="greeting-badge"><i class="fa-solid fa-sparkles"></i> Welcome Back</span>
    <h1><c:out value="${sessionScope.user.welcomeMessage}"/></h1>
    <p class="muted">Role: <strong class="role-highlight"><c:out value="${sessionScope.user.role}"/></strong> &middot; Keep pushing towards your fitness goals!</p>
  </div>
  <div class="hero-actions">
    <c:choose>
      <c:when test="${sessionScope.user.trainer}">
        <a href="${ctx}/app/admin" class="btn secondary-btn"><i class="fa-solid fa-user-shield"></i> Open Admin Portal</a>
        <a href="${ctx}/app/workouts" class="btn primary-btn"><i class="fa-solid fa-plus"></i> Publish Program</a>
      </c:when>
      <c:otherwise>
        <a href="${ctx}/app/workouts" class="btn secondary-btn"><i class="fa-solid fa-compass"></i> Explore Workouts</a>
        <a href="${ctx}/app/progress" class="btn primary-btn"><i class="fa-solid fa-plus-circle"></i> Log Session</a>
      </c:otherwise>
    </c:choose>
  </div>
</section>

<c:choose>
  <c:when test="${sessionScope.user.trainer}">
    <!-- Trainer / Admin Quick Metrics -->
    <div class="stats grid three">
      <div class="stat card-glow-primary">
        <div class="stat-icon"><i class="fa-solid fa-dumbbell"></i></div>
        <div class="stat-info">
          <span class="num"><c:out value="${allWorkouts.size()}"/></span>
          <span class="label">Programs Published</span>
        </div>
      </div>
      <div class="stat card-glow-success">
        <div class="stat-icon"><i class="fa-solid fa-user-check"></i></div>
        <div class="stat-info">
          <span class="num"><c:out value="${allEnrollments.size()}"/></span>
          <span class="label">Total Member Enrollments</span>
        </div>
      </div>
      <div class="stat card-glow-accent">
        <div class="stat-icon"><i class="fa-solid fa-users"></i></div>
        <div class="stat-info">
          <span class="num"><c:out value="${allUsers.size()}"/></span>
          <span class="label">Registered Accounts</span>
        </div>
      </div>
    </div>

    <div class="card admin-promo-card">
      <div class="promo-text">
        <h2><i class="fa-solid fa-chart-pie"></i> Trainer Management Hub</h2>
        <p>Publish new programs, track member subscriptions, and review platform performance from your Admin Portal.</p>
      </div>
      <div class="promo-buttons">
        <a href="${ctx}/app/admin" class="btn primary-btn"><i class="fa-solid fa-users-rectangle"></i> View All Enrolled Persons</a>
        <a href="${ctx}/app/workouts" class="btn outline-btn"><i class="fa-solid fa-list-check"></i> Manage Workouts</a>
      </div>
    </div>
  </c:when>

  <c:otherwise>
    <!-- Member Metrics -->
    <div class="stats grid four">
      <div class="stat card-glow-primary">
        <div class="stat-icon"><i class="fa-solid fa-fire"></i></div>
        <div class="stat-info">
          <span class="num"><c:out value="${summary.sessions}"/></span>
          <span class="label">Sessions Logged</span>
        </div>
      </div>
      <div class="stat card-glow-success">
        <div class="stat-icon"><i class="fa-solid fa-clock"></i></div>
        <div class="stat-info">
          <span class="num"><c:out value="${summary.totalMinutes}"/></span>
          <span class="label">Minutes Trained</span>
        </div>
      </div>
      <div class="stat card-glow-accent">
        <div class="stat-icon"><i class="fa-solid fa-bolt"></i></div>
        <div class="stat-info">
          <span class="num"><c:out value="${summary.totalCalories}"/></span>
          <span class="label">Calories Burned</span>
        </div>
      </div>
      <div class="stat card-glow-info">
        <div class="stat-icon"><i class="fa-solid fa-heart-pulse"></i></div>
        <div class="stat-info">
          <span class="num"><c:out value="${myWorkouts.size()}"/></span>
          <span class="label">Enrolled Workouts</span>
        </div>
      </div>
    </div>

    <div class="dashboard-layout grid two">
      <!-- Section: Enrolled Workouts (Previously Enrolled) -->
      <div class="card enrolled-card-section">
        <div class="section-title-bar">
          <h2><i class="fa-solid fa-dumbbell text-primary"></i> My Enrolled Workouts</h2>
          <span class="count-pill"><c:out value="${myWorkouts.size()}"/> Active</span>
        </div>

        <c:choose>
          <c:when test="${empty myWorkouts}">
            <div class="empty-state">
              <div class="empty-icon-wrap"><i class="fa-solid fa-calendar-plus"></i></div>
              <h3>No Workouts Enrolled Yet</h3>
              <p class="muted">Browse our workout catalogue and enroll in Cardio, Strength, or Yoga programs to kickstart your training!</p>
              <a href="${ctx}/app/workouts" class="btn primary-btn mt-3"><i class="fa-solid fa-compass"></i> Explore & Enroll in Programs</a>
            </div>
          </c:when>
          <c:otherwise>
            <div class="enrolled-grid">
              <c:forEach var="w" items="${myWorkouts}">
                <div class="enrolled-workout-item">
                  <div class="enrolled-item-header">
                    <span class="badge <c:out value='${w.type}'/>"><c:out value="${w.type}"/></span>
                    <span class="duration-tag"><i class="fa-regular fa-clock"></i> <c:out value="${w.durationMin}"/> min</span>
                  </div>
                  <h3 class="enrolled-item-title"><c:out value="${w.title}"/></h3>
                  <p class="enrolled-item-desc"><c:out value="${w.description}"/></p>
                  <div class="enrolled-item-footer">
                    <span class="coach-tag"><i class="fa-solid fa-user-ninja"></i> Coach: <c:out value="${w.trainerName}" default="FitPro Team"/></span>
                    <a href="${ctx}/app/progress" class="btn small-btn primary-btn"><i class="fa-solid fa-plus"></i> Log Session</a>
                  </div>
                </div>
              </c:forEach>
            </div>
          </c:otherwise>
        </c:choose>
      </div>

      <!-- Section: Calories Analytics & Performance -->
      <div class="card analytics-card-section">
        <div class="section-title-bar">
          <h2><i class="fa-solid fa-chart-column text-accent"></i> Calories Burned</h2>
        </div>
        <c:choose>
          <c:when test="${empty summary.caloriesByWorkout}">
            <div class="empty-state">
              <div class="empty-icon-wrap"><i class="fa-solid fa-chart-line"></i></div>
              <h3>No Performance Data</h3>
              <p class="muted">Log your first workout session to see your calorie breakdown chart!</p>
            </div>
          </c:when>
          <c:otherwise>
            <div class="chart-container">
              <c:forEach var="e" items="${summary.caloriesByWorkout}">
                <div class="bar-row">
                  <div class="bar-label">
                    <span><c:out value="${e.key}"/></span>
                    <strong class="bar-val"><c:out value="${e.value}"/> kcal</strong>
                  </div>
                  <div class="bar-track">
                    <div class="bar-fill" style="width: ${summary.totalCalories > 0 ? (e.value * 100 / summary.totalCalories) : 0}%;"></div>
                  </div>
                </div>
              </c:forEach>
            </div>
            <div class="favourite-workout-box">
              <i class="fa-solid fa-crown fav-icon"></i>
              <div>
                <span class="fav-label">Top Favorite Activity</span>
                <strong class="fav-name"><c:out value="${summary.favouriteWorkout}"/></strong>
              </div>
            </div>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
  </c:otherwise>
</c:choose>

<%@ include file="footer.jspf" %>
