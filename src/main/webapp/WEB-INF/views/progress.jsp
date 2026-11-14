<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="My Progress" />
<%@ include file="header.jspf" %>

<div class="page-header">
  <span class="eyebrow"><i class="fa-solid fa-chart-line"></i> Performance Tracking</span>
  <h1 class="page-title">Session Logger &amp; History</h1>
  <p class="page-desc">
    Log completed workout sessions, track body weight trends, and review verified caloric expenditures.
  </p>
</div>

<c:choose>
  <c:when test="${sessionScope.user.trainer || sessionScope.user.admin}">
    <div class="card">
      <div class="empty-state">
        <i class="fa-solid fa-shield-halved empty-icon"></i>
        <h3>Trainer / Admin Mode</h3>
        <p class="muted">Progress logging is designed for members. Switch to your Dashboard to view member performance feeds.</p>
        <a href="${ctx}/app/dashboard" class="btn blue-btn" style="margin-top: 14px;">Return to Dashboard</a>
      </div>
    </div>
  </c:when>

  <c:otherwise>
    <!-- Log Session Card -->
    <div class="card" style="margin-bottom: 28px;">
      <div class="section-header-bar">
        <div>
          <h2 class="section-title"><i class="fa-solid fa-pen-to-square"></i> Record a Workout Session</h2>
          <p class="muted" style="font-size: 0.9rem; margin-top: 2px;">Enter your session duration and weight to compute exact energy expenditure.</p>
        </div>
      </div>

      <c:choose>
        <c:when test="${empty myWorkouts}">
          <div class="empty-state">
            <i class="fa-solid fa-dumbbell empty-icon"></i>
            <h3>No Enrolled Workouts</h3>
            <p class="muted" style="margin-bottom: 16px;">Enroll in at least one workout program before logging sessions.</p>
            <a href="${ctx}/app/workouts" class="btn blue-btn"><i class="fa-solid fa-compass"></i> Browse Workout Catalogue</a>
          </div>
        </c:when>
        <c:otherwise>
          <form method="post" action="${ctx}/app/progress" class="validate grid three" style="align-items: flex-end;">
            <div class="form-group" style="margin-bottom: 0;">
              <label for="workoutSelect">Select Enrolled Workout</label>
              <select id="workoutSelect" name="workoutId" required>
                <c:forEach var="w" items="${myWorkouts}">
                  <option value="${w.id}"><c:out value="${w.title}"/> (<c:out value="${w.type}"/>)</option>
                </c:forEach>
              </select>
            </div>

            <div class="form-group" style="margin-bottom: 0;">
              <label for="sessionMinutes">Duration (minutes)</label>
              <input type="number" id="sessionMinutes" name="minutes" min="1" max="600" placeholder="e.g. 45" required>
            </div>

            <div class="form-group" style="margin-bottom: 0;">
              <label for="sessionWeight">Body Weight (kg)</label>
              <input type="number" id="sessionWeight" name="weight" min="20" max="300" step="0.1" placeholder="e.g. 72.5" required>
            </div>

            <div style="grid-column: 1 / -1; margin-top: 10px;">
              <button class="btn blue-btn full-width" type="submit" style="height: 46px;">
                <i class="fa-solid fa-check"></i> Calculate Calories &amp; Save Session
              </button>
            </div>
          </form>
        </c:otherwise>
      </c:choose>
    </div>

    <!-- Session History Card with CSV Export -->
    <div class="card">
      <div class="section-header-bar">
        <div>
          <h2 class="section-title"><i class="fa-solid fa-clock-rotate-left"></i> Training Session Log</h2>
          <p class="muted" style="font-size: 0.9rem; margin-top: 2px;">Chronological record of all completed workouts.</p>
        </div>
        <c:if test="${not empty logs}">
          <a href="${ctx}/app/progress?action=export" class="btn outline-btn small-btn" title="Download historical logs as CSV">
            <i class="fa-solid fa-file-arrow-down"></i> Export CSV Report
          </a>
        </c:if>
      </div>

      <c:choose>
        <c:when test="${empty logs}">
          <div class="empty-state">
            <i class="fa-solid fa-notes-medical empty-icon"></i>
            <h3>No Training Sessions Yet</h3>
            <p class="muted">Your logged sessions will appear here with calculated caloric burn and dates.</p>
          </div>
        </c:when>
        <c:otherwise>
          <div class="table-wrap">
            <table class="styled-table">
              <thead>
                <tr>
                  <th>Date</th>
                  <th>Workout Routine</th>
                  <th>Duration</th>
                  <th>Body Weight</th>
                  <th>Energy Expended</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="l" items="${logs}">
                  <tr>
                    <td><c:out value="${l.loggedOn}"/></td>
                    <td><strong><c:out value="${l.workoutTitle}"/></strong></td>
                    <td><c:out value="${l.minutes}"/> min</td>
                    <td><c:out value="${l.weightKg}"/> kg</td>
                    <td><span style="font-weight: 700; color: var(--cardio-text);"><c:out value="${l.calories}"/> kcal</span></td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
  </c:otherwise>
</c:choose>

<%@ include file="footer.jspf" %>
