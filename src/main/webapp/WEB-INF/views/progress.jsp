<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="My Progress" />
<%@ include file="header.jspf" %>

<div class="page-header">
  <div>
    <h1><i class="fa-solid fa-fire text-primary"></i> My Progress & Session Logger</h1>
    <p class="muted">Track your completed workout sessions, body weight, and calories burned.</p>
  </div>
</div>

<c:choose>
  <c:when test="${sessionScope.user.trainer}">
    <div class="card"><p class="muted">Progress tracking is available for members.</p></div>
  </c:when>
  <c:otherwise>
    <div class="card log-card">
      <h2><i class="fa-solid fa-pen-to-square"></i> Log a Training Session</h2>
      <c:choose>
        <c:when test="${empty myWorkouts}">
          <div class="empty-state">
            <p class="muted">You must enroll in a workout program first before logging sessions.</p>
            <a href="${ctx}/app/workouts" class="btn primary-btn mt-2"><i class="fa-solid fa-plus-circle"></i> Browse & Enroll in Workouts</a>
          </div>
        </c:when>
        <c:otherwise>
          <form method="post" action="${ctx}/app/progress" class="validate grid three mt-3">
            <label>Select Enrolled Workout
              <select name="workoutId" required>
                <c:forEach var="w" items="${myWorkouts}">
                  <option value="${w.id}"><c:out value="${w.title}"/> (<c:out value="${w.type}"/>)</option>
                </c:forEach>
              </select>
            </label>
            <label>Minutes Trained <input type="number" name="minutes" min="1" max="600" placeholder="e.g. 45" required></label>
            <label>Current Weight (kg) <input type="number" name="weight" min="20" max="300" step="0.1" placeholder="e.g. 70.5" required></label>
            <button class="btn primary-btn full-width" type="submit"><i class="fa-solid fa-floppy-disk"></i> Save Session</button>
          </form>
        </c:otherwise>
      </c:choose>
    </div>

    <div class="card history-card">
      <h2><i class="fa-solid fa-clock-rotate-left"></i> Session History</h2>
      <c:choose>
        <c:when test="${empty logs}">
          <p class="muted">No sessions logged yet. Log your first workout session above!</p>
        </c:when>
        <c:otherwise>
          <div class="table-wrap">
            <table class="styled-table">
              <thead>
                <tr><th>Date Logged</th><th>Workout Title</th><th>Duration</th><th>Body Weight</th><th>Estimated Calories</th></tr>
              </thead>
              <tbody>
                <c:forEach var="l" items="${logs}">
                  <tr>
                    <td><c:out value="${l.loggedOn}"/></td>
                    <td><strong><c:out value="${l.workoutTitle}"/></strong></td>
                    <td><c:out value="${l.minutes}"/> min</td>
                    <td><c:out value="${l.weightKg}"/> kg</td>
                    <td><span class="calorie-badge"><i class="fa-solid fa-bolt"></i> <c:out value="${l.calories}"/> kcal</span></td>
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
