<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Dashboard" />
<%@ include file="header.jspf" %>
<section class="hero">
  <h1><c:out value="${sessionScope.user.welcomeMessage}"/></h1>
  <p class="muted">Signed in as <c:out value="${sessionScope.user.role}"/></p>
</section>

<c:choose>
  <c:when test="${sessionScope.user.trainer}">
    <div class="card">
      <h2>Trainer tools</h2>
      <p>Publish programs from the <a href="${ctx}/app/workouts">Workouts</a> page. Members can then enrol and log sessions.</p>
    </div>
  </c:when>
  <c:otherwise>
    <div class="stats">
      <div class="stat"><span class="num"><c:out value="${summary.sessions}"/></span><span>Sessions</span></div>
      <div class="stat"><span class="num"><c:out value="${summary.totalMinutes}"/></span><span>Minutes trained</span></div>
      <div class="stat"><span class="num"><c:out value="${summary.totalCalories}"/></span><span>Calories burned</span></div>
      <div class="stat"><span class="num"><c:out value="${summary.latestWeight}"/></span><span>Latest weight (kg)</span></div>
    </div>

    <div class="grid two">
      <div class="card">
        <h2>My programs</h2>
        <c:choose>
          <c:when test="${empty myWorkouts}">
            <p class="muted">You haven't joined any program yet. <a href="${ctx}/app/workouts">Browse workouts</a></p>
          </c:when>
          <c:otherwise>
            <ul class="plain">
              <c:forEach var="w" items="${myWorkouts}">
                <li><strong><c:out value="${w.title}"/></strong>
                  <span class="badge <c:out value='${w.type}'/>"><c:out value="${w.type}"/></span>
                  <span class="muted"> &middot; <c:out value="${w.durationMin}"/> min</span></li>
              </c:forEach>
            </ul>
          </c:otherwise>
        </c:choose>
      </div>

      <div class="card">
        <h2>Calories by workout</h2>
        <c:choose>
          <c:when test="${empty summary.caloriesByWorkout}">
            <p class="muted">Log a session to see your chart.</p>
          </c:when>
          <c:otherwise>
            <c:forEach var="e" items="${summary.caloriesByWorkout}">
              <div class="bar-row">
                <span><c:out value="${e.key}"/></span>
                <div class="bar"><div class="fill" data-value="${e.value}" data-total="${summary.totalCalories}"></div></div>
                <span><c:out value="${e.value}"/> kcal</span>
              </div>
            </c:forEach>
            <p class="muted">Favourite: <strong><c:out value="${summary.favouriteWorkout}"/></strong></p>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
  </c:otherwise>
</c:choose>
<%@ include file="footer.jspf" %>
