<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="My Progress" />
<%@ include file="header.jspf" %>
<h1>My progress</h1>

<c:choose>
  <c:when test="${sessionScope.user.trainer}">
    <div class="card"><p>Progress tracking is available for members.</p></div>
  </c:when>
  <c:otherwise>
    <div class="card">
      <h2>Log a session</h2>
      <c:choose>
        <c:when test="${empty myWorkouts}">
          <p class="muted">Enrol in a program first from the <a href="${ctx}/app/workouts">Workouts</a> page.</p>
        </c:when>
        <c:otherwise>
          <form method="post" action="${ctx}/app/progress" class="validate grid three">
            <label>Workout
              <select name="workoutId" required>
                <c:forEach var="w" items="${myWorkouts}">
                  <option value="${w.id}"><c:out value="${w.title}"/></option>
                </c:forEach>
              </select>
            </label>
            <label>Minutes trained <input type="number" name="minutes" min="1" max="600" required></label>
            <label>Body weight (kg) <input type="number" name="weight" min="20" max="300" step="0.1" required></label>
            <button class="btn" type="submit">Save session</button>
          </form>
        </c:otherwise>
      </c:choose>
    </div>

    <div class="card">
      <h2>History</h2>
      <c:choose>
        <c:when test="${empty logs}"><p class="muted">No sessions logged yet.</p></c:when>
        <c:otherwise>
          <div class="table-wrap">
            <table>
              <thead><tr><th>Date</th><th>Workout</th><th>Minutes</th><th>Weight (kg)</th><th>Calories</th></tr></thead>
              <tbody>
                <c:forEach var="l" items="${logs}">
                  <tr>
                    <td><c:out value="${l.loggedOn}"/></td>
                    <td><c:out value="${l.workoutTitle}"/></td>
                    <td><c:out value="${l.minutes}"/></td>
                    <td><c:out value="${l.weightKg}"/></td>
                    <td><c:out value="${l.calories}"/></td>
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
