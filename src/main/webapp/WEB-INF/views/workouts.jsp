<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Workouts" />
<%@ include file="header.jspf" %>
<h1>Workout programs</h1>

<div class="filters">
  <a class="chip ${empty activeType ? 'active' : ''}" href="${ctx}/app/workouts">All</a>
  <a class="chip ${activeType == 'CARDIO' ? 'active' : ''}" href="${ctx}/app/workouts?type=CARDIO">Cardio</a>
  <a class="chip ${activeType == 'STRENGTH' ? 'active' : ''}" href="${ctx}/app/workouts?type=STRENGTH">Strength</a>
  <a class="chip ${activeType == 'YOGA' ? 'active' : ''}" href="${ctx}/app/workouts?type=YOGA">Yoga</a>
</div>

<c:if test="${sessionScope.user.trainer}">
  <details class="card">
    <summary><strong>+ Publish a new program</strong></summary>
    <form method="post" action="${ctx}/app/workouts" class="validate grid two">
      <input type="hidden" name="action" value="create">
      <label>Title <input type="text" name="title" minlength="3" required></label>
      <label>Duration (minutes) <input type="number" name="duration" min="5" max="240" value="30" required></label>
      <label>Type
        <select name="type"><option>CARDIO</option><option>STRENGTH</option><option>YOGA</option></select>
      </label>
      <label>Level
        <select name="level"><option>BEGINNER</option><option>INTERMEDIATE</option><option>ADVANCED</option></select>
      </label>
      <label>Intensity (MET 1-15) <input type="number" name="met" min="1" max="15" step="0.5" value="5" required></label>
      <label>Description <input type="text" name="description" maxlength="500"></label>
      <button class="btn" type="submit">Publish</button>
    </form>
  </details>
</c:if>

<div class="grid three">
  <c:forEach var="w" items="${workouts}">
    <article class="card workout">
      <span class="badge <c:out value='${w.type}'/>"><c:out value="${w.type}"/></span>
      <h3><c:out value="${w.title}"/></h3>
      <p class="muted"><c:out value="${w.description}"/></p>
      <p class="meta"><c:out value="${w.level}"/> &middot; <c:out value="${w.durationMin}"/> min &middot; Coach: <c:out value="${w.trainerName}"/></p>
      <c:if test="${!sessionScope.user.trainer}">
        <c:choose>
          <c:when test="${enrolledIds.contains(w.id)}"><span class="enrolled">&#10003; Enrolled</span></c:when>
          <c:otherwise>
            <form method="post" action="${ctx}/app/workouts">
              <input type="hidden" name="action" value="enroll">
              <input type="hidden" name="workoutId" value="${w.id}">
              <button class="btn small" type="submit">Enrol</button>
            </form>
          </c:otherwise>
        </c:choose>
      </c:if>
    </article>
  </c:forEach>
  <c:if test="${empty workouts}"><p class="muted">No programs found.</p></c:if>
</div>
<%@ include file="footer.jspf" %>
