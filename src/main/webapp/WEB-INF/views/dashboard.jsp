<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Dashboard" />
<%@ include file="header.jspf" %>

<c:choose>
  <%-- =========================================================================
       1. ADMINISTRATOR EXECUTIVE CONSOLE
       ========================================================================= --%>
  <c:when test="${sessionScope.user.admin}">
    <section class="hero-banner">
      <div class="hero-banner-content">
        <span class="eyebrow"><i class="fa-solid fa-shield-halved"></i> Administrator Console</span>
        <h1><c:out value="${sessionScope.user.welcomeMessage}"/></h1>
        <p>Real-time platform telemetry, user role orchestration, and workout catalogue oversight.</p>
        <div class="hero-banner-actions">
          <a href="${ctx}/app/admin" class="btn blue-btn">
            <i class="fa-solid fa-users-gear"></i> Open Administration Center
          </a>
          <a href="${ctx}/app/workouts" class="btn outline-btn">
            <i class="fa-solid fa-dumbbell"></i> Moderate Catalogue
          </a>
        </div>
      </div>
      <div style="text-align: right; min-width: 200px;">
        <span class="badge ADMIN" style="font-size: 0.85rem; padding: 6px 14px;">
          <i class="fa-solid fa-circle-check"></i> System Operational
        </span>
      </div>
    </section>

    <!-- Platform Stats -->
    <div class="grid four" style="margin-bottom: 28px;">
      <div class="stat-card">
        <div class="stat-header">
          <span class="stat-label">Total Accounts</span>
          <div class="stat-icon"><i class="fa-solid fa-users"></i></div>
        </div>
        <div class="stat-num"><c:out value="${allUsers.size()}"/></div>
      </div>
      <div class="stat-card">
        <div class="stat-header">
          <span class="stat-label">Published Programs</span>
          <div class="stat-icon"><i class="fa-solid fa-dumbbell"></i></div>
        </div>
        <div class="stat-num"><c:out value="${allWorkouts.size()}"/></div>
      </div>
      <div class="stat-card">
        <div class="stat-header">
          <span class="stat-label">Total Enrollments</span>
          <div class="stat-icon"><i class="fa-solid fa-user-check"></i></div>
        </div>
        <div class="stat-num"><c:out value="${allEnrollments.size()}"/></div>
      </div>
      <div class="stat-card">
        <div class="stat-header">
          <span class="stat-label">Completed Sessions</span>
          <div class="stat-icon"><i class="fa-solid fa-fire"></i></div>
        </div>
        <div class="stat-num"><c:out value="${allLogs.size()}"/></div>
      </div>
    </div>

    <!-- Quick Navigation to Admin Management -->
    <div class="grid two">
      <div class="card">
        <div class="section-header-bar">
          <h2 class="section-title"><i class="fa-solid fa-users-gear"></i> User &amp; Role Management</h2>
          <a href="${ctx}/app/admin" class="btn small-btn outline-btn">Manage Users</a>
        </div>
        <p class="muted" style="margin-bottom: 16px; font-size: 0.92rem;">
          Promote or reassign user roles between Member, Trainer, and Administrator.
        </p>
        <div class="table-wrap">
          <table class="styled-table">
            <thead>
              <tr><th>Name</th><th>Email</th><th>Role</th></tr>
            </thead>
            <tbody>
              <c:forEach var="u" items="${allUsers}" begin="0" end="4">
                <tr>
                  <td><strong><c:out value="${u.fullName}"/></strong></td>
                  <td><c:out value="${u.email}"/></td>
                  <td>
                    <span class="badge ${u.admin ? 'ADMIN' : (u.trainer ? 'STRENGTH' : 'neutral')}">
                      <c:out value="${u.role}"/>
                    </span>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </div>

      <div class="card">
        <div class="section-header-bar">
          <h2 class="section-title"><i class="fa-solid fa-server"></i> System Telemetry</h2>
          <span class="badge neutral">Live</span>
        </div>
        <div style="display: flex; flex-direction: column; gap: 14px; margin-top: 12px;">
          <div style="display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid var(--hairline);">
            <span class="muted">Runtime Environment</span>
            <strong>Java EE Servlets 4.0 / Tomcat 9</strong>
          </div>
          <div style="display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid var(--hairline);">
            <span class="muted">Database Engine</span>
            <strong>Relational H2 / MySQL Dual Mode</strong>
          </div>
          <div style="display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid var(--hairline);">
            <span class="muted">Session Security</span>
            <strong>PBKDF2 Hashing (65,536 rounds)</strong>
          </div>
          <div style="display: flex; justify-content: space-between; padding: 10px 0;">
            <span class="muted">Access Control</span>
            <strong>Strict Role-Based Servlet Filters</strong>
          </div>
        </div>
        <div style="margin-top: 20px;">
          <a href="${ctx}/app/admin" class="btn primary-btn full-width">
            <i class="fa-solid fa-sliders"></i> Go to Complete Administrator Portal
          </a>
        </div>
      </div>
    </div>
  </c:when>

  <%-- =========================================================================
       2. CERTIFIED TRAINER STUDIO CONSOLE
       ========================================================================= --%>
  <c:when test="${sessionScope.user.trainer}">
    <section class="hero-banner">
      <div class="hero-banner-content">
        <span class="eyebrow"><i class="fa-solid fa-user-ninja"></i> Coach Studio</span>
        <h1><c:out value="${sessionScope.user.welcomeMessage}"/></h1>
        <p>Monitor enrolled member progress, manage workout routines, and publish new programs.</p>
        <div class="hero-banner-actions">
          <a href="${ctx}/app/workouts" class="btn blue-btn">
            <i class="fa-solid fa-plus-circle"></i> Publish New Program
          </a>
          <a href="#assignedMembers" class="btn outline-btn">
            <i class="fa-solid fa-users"></i> Review Enrolled Members
          </a>
        </div>
      </div>
      <div>
        <span class="badge STRENGTH" style="font-size: 0.85rem; padding: 6px 14px;">
          <i class="fa-solid fa-certificate"></i> Certified Coach
        </span>
      </div>
    </section>

    <!-- Trainer Metrics -->
    <div class="grid three" style="margin-bottom: 28px;">
      <div class="stat-card">
        <div class="stat-header">
          <span class="stat-label">Assigned Member Subscriptions</span>
          <div class="stat-icon"><i class="fa-solid fa-user-check"></i></div>
        </div>
        <div class="stat-num"><c:out value="${allEnrollments.size()}"/></div>
      </div>
      <div class="stat-card">
        <div class="stat-header">
          <span class="stat-label">Workout Programs Managed</span>
          <div class="stat-icon"><i class="fa-solid fa-dumbbell"></i></div>
        </div>
        <div class="stat-num"><c:out value="${allWorkouts.size()}"/></div>
      </div>
      <div class="stat-card">
        <div class="stat-header">
          <span class="stat-label">Member Sessions Recorded</span>
          <div class="stat-icon"><i class="fa-solid fa-chart-line"></i></div>
        </div>
        <div class="stat-num"><c:out value="${recentMemberLogs.size()}"/></div>
      </div>
    </div>

    <!-- Assigned Members Table with Status Indicators -->
    <div class="card" id="assignedMembers" style="margin-bottom: 28px;">
      <div class="section-header-bar">
        <div>
          <h2 class="section-title"><i class="fa-solid fa-list-check"></i> Assigned Members &amp; Program Subscriptions</h2>
          <p class="muted" style="font-size: 0.92rem; margin-top: 4px;">Members currently enrolled in platform workout programs.</p>
        </div>
        <span class="badge neutral"><c:out value="${allEnrollments.size()}"/> Active</span>
      </div>

      <c:choose>
        <c:when test="${empty allEnrollments}">
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
                  <th>Member Name</th>
                  <th>Email</th>
                  <th>Workout Plan</th>
                  <th>Discipline</th>
                  <th>Status</th>
                  <th>Enrolled On</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="e" items="${allEnrollments}">
                  <tr>
                    <td><strong><c:out value="${e.userName}"/></strong></td>
                    <td><a href="mailto:${e.userEmail}"><c:out value="${e.userEmail}"/></a></td>
                    <td><c:out value="${e.workoutTitle}"/></td>
                    <td><span class="badge <c:out value='${e.workoutType}'/>"><c:out value="${e.workoutType}"/></span></td>
                    <td><span class="badge YOGA"><i class="fa-solid fa-circle-dot"></i> Active</span></td>
                    <td><c:out value="${e.enrolledAt}"/></td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </c:otherwise>
      </c:choose>
    </div>

    <!-- Member Workout History & Progress Feed -->
    <div class="grid two">
      <div class="card">
        <div class="section-header-bar">
          <h2 class="section-title"><i class="fa-solid fa-clock-rotate-left"></i> Recent Member Training Logs</h2>
        </div>
        <c:choose>
          <c:when test="${empty recentMemberLogs}">
            <div class="empty-state">
              <i class="fa-solid fa-chart-line empty-icon"></i>
              <p class="muted">No session logs logged by members yet.</p>
            </div>
          </c:when>
          <c:otherwise>
            <div class="table-wrap">
              <table class="styled-table">
                <thead>
                  <tr><th>Member</th><th>Workout</th><th>Duration</th><th>Burn</th></tr>
                </thead>
                <tbody>
                  <c:forEach var="l" items="${recentMemberLogs}" begin="0" end="5">
                    <tr>
                      <td><strong><c:out value="${l.userName}" default="Member"/></strong></td>
                      <td><c:out value="${l.workoutTitle}"/></td>
                      <td><c:out value="${l.minutes}"/> min</td>
                      <td><span style="font-weight: 600; color: var(--cardio-text);"><c:out value="${l.calories}"/> kcal</span></td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>
          </c:otherwise>
        </c:choose>
      </div>

      <!-- Coach Recommendations & Tools -->
      <div class="card">
        <div class="section-header-bar">
          <h2 class="section-title"><i class="fa-solid fa-lightbulb"></i> Coaching Recommendations &amp; Guidance</h2>
        </div>
        <div style="display: flex; flex-direction: column; gap: 14px; font-size: 0.92rem; line-height: 1.5;">
          <div style="background: var(--bg-canvas); padding: 14px 18px; border-radius: 12px; border: 1px solid var(--hairline);">
            <strong style="display: block; margin-bottom: 4px; color: var(--text-main);"><i class="fa-solid fa-scale-balanced text-primary"></i> Progressive Overload</strong>
            <span class="muted">Encourage members logging strength routines to increase resistance or repetition volume every 2 weeks.</span>
          </div>
          <div style="background: var(--bg-canvas); padding: 14px 18px; border-radius: 12px; border: 1px solid var(--hairline);">
            <strong style="display: block; margin-bottom: 4px; color: var(--text-main);"><i class="fa-solid fa-heart-pulse text-primary"></i> Cardio Pacing</strong>
            <span class="muted">Remind HIIT participants to sustain heart rates within 75–85% of their maximum for optimal VO2 gains.</span>
          </div>
          <div style="background: var(--bg-canvas); padding: 14px 18px; border-radius: 12px; border: 1px solid var(--hairline);">
            <strong style="display: block; margin-bottom: 4px; color: var(--text-main);"><i class="fa-solid fa-bed text-primary"></i> Active Recovery</strong>
            <span class="muted">Recommend at least 1 weekly Yoga session to reduce cortisol levels and preserve connective tissue health.</span>
          </div>
        </div>
        <div style="margin-top: 18px;">
          <a href="${ctx}/app/workouts" class="btn blue-btn full-width">
            <i class="fa-solid fa-plus"></i> Publish a New Workout Program
          </a>
        </div>
      </div>
    </div>
  </c:when>

  <%-- =========================================================================
       3. MEMBER PERSONAL FITNESS DASHBOARD
       ========================================================================= --%>
  <c:otherwise>
    <section class="hero-banner">
      <div class="hero-banner-content">
        <span class="eyebrow"><i class="fa-solid fa-sparkles"></i> Personal Training Hub</span>
        <h1><c:out value="${sessionScope.user.welcomeMessage}"/></h1>
        <p>
          Logged in as <strong><c:out value="${sessionScope.user.fullName}"/></strong> (<c:out value="${sessionScope.user.email}"/>).
          Review your enrolled routines, track daily caloric expenditure, and log completed workouts.
        </p>
        <div class="hero-banner-actions">
          <a href="${ctx}/app/progress" class="btn blue-btn">
            <i class="fa-solid fa-plus-circle"></i> Log Training Session
          </a>
          <a href="${ctx}/app/workouts" class="btn outline-btn">
            <i class="fa-solid fa-compass"></i> Discover Workouts
          </a>
          <a href="${ctx}/logout" class="btn outline-btn" title="Sign out of FitPro">
            <i class="fa-solid fa-arrow-right-from-bracket"></i> Sign Out
          </a>
        </div>
      </div>
      <div style="display: flex; flex-direction: column; align-items: flex-end; gap: 8px;">
        <span class="badge YOGA" style="font-size: 0.85rem; padding: 6px 14px;">
          <i class="fa-solid fa-circle-check"></i> Member Active
        </span>
        <span class="badge neutral" style="font-size: 0.82rem; padding: 4px 12px;">
          <i class="fa-regular fa-calendar"></i> Today
        </span>
      </div>
    </section>

    <!-- Member Key Metrics -->
    <div class="grid four" style="margin-bottom: 28px;">
      <div class="stat-card">
        <div class="stat-header">
          <span class="stat-label">Sessions Completed</span>
          <div class="stat-icon"><i class="fa-solid fa-fire" style="color: var(--cardio-text);"></i></div>
        </div>
        <div class="stat-num"><c:out value="${summary.sessions}"/></div>
      </div>
      <div class="stat-card">
        <div class="stat-header">
          <span class="stat-label">Minutes Trained</span>
          <div class="stat-icon"><i class="fa-solid fa-clock" style="color: var(--strength-text);"></i></div>
        </div>
        <div class="stat-num"><c:out value="${summary.totalMinutes}"/></div>
      </div>
      <div class="stat-card">
        <div class="stat-header">
          <span class="stat-label">Total Calories Burned</span>
          <div class="stat-icon"><i class="fa-solid fa-bolt" style="color: #ea580c;"></i></div>
        </div>
        <div class="stat-num"><c:out value="${summary.totalCalories}"/> <span style="font-size: 1rem; font-weight: 500; color: var(--text-secondary);">kcal</span></div>
      </div>
      <div class="stat-card">
        <div class="stat-header">
          <span class="stat-label">Enrolled Programs</span>
          <div class="stat-icon"><i class="fa-solid fa-dumbbell" style="color: var(--yoga-text);"></i></div>
        </div>
        <div class="stat-num"><c:out value="${myWorkouts.size()}"/></div>
      </div>
    </div>

    <div class="grid two" style="margin-bottom: 28px;">
      <!-- Enrolled Workout Plan Section -->
      <div class="card">
        <div class="section-header-bar">
          <div>
            <h2 class="section-title"><i class="fa-solid fa-dumbbell"></i> Current Workout Plan</h2>
            <p class="muted" style="font-size: 0.88rem; margin-top: 2px;">Programs you are actively enrolled in.</p>
          </div>
          <span class="badge neutral"><c:out value="${myWorkouts.size()}"/> Active</span>
        </div>

        <c:choose>
          <c:when test="${empty myWorkouts}">
            <div class="empty-state">
              <i class="fa-solid fa-calendar-plus empty-icon"></i>
              <h3 style="font-size: 1.15rem; font-weight: 600; margin-bottom: 6px;">No Active Program Enrolled</h3>
              <p class="muted" style="max-width: 380px; margin: 0 auto 18px; font-size: 0.92rem;">
                Browse our curated catalogue and enroll in Cardio, Strength, or Yoga programs to unlock session logging.
              </p>
              <a href="${ctx}/app/workouts" class="btn blue-btn">
                <i class="fa-solid fa-compass"></i> Browse Workout Programs
              </a>
            </div>
          </c:when>
          <c:otherwise>
            <div style="display: flex; flex-direction: column; gap: 14px;">
              <c:forEach var="w" items="${myWorkouts}">
                <div style="background: var(--bg-subtle); border: 1px solid var(--hairline); border-radius: 14px; padding: 18px; display: flex; flex-direction: column; justify-content: space-between; gap: 10px;">
                  <div style="display: flex; justify-content: space-between; align-items: center;">
                    <span class="badge <c:out value='${w.type}'/>"><c:out value="${w.type}"/></span>
                    <span class="muted" style="font-size: 0.82rem;"><i class="fa-regular fa-clock"></i> <c:out value="${w.durationMin}"/> min</span>
                  </div>
                  <div>
                    <h3 style="font-size: 1.1rem; font-weight: 600; color: var(--text-main); margin-bottom: 4px;"><c:out value="${w.title}"/></h3>
                    <p class="muted" style="font-size: 0.88rem; line-height: 1.4;"><c:out value="${w.description}"/></p>
                  </div>
                  <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 6px;">
                    <span style="font-size: 0.82rem; color: var(--text-secondary); font-weight: 500;">
                      <i class="fa-solid fa-user-ninja"></i> Coach: <c:out value="${w.trainerName}" default="FitPro Coach"/>
                    </span>
                    <a href="${ctx}/app/progress?workoutId=${w.id}" class="btn small-btn primary-btn">
                      <i class="fa-solid fa-plus"></i> Log Session
                    </a>
                  </div>
                </div>
              </c:forEach>
            </div>
          </c:otherwise>
        </c:choose>
      </div>

      <!-- Calorie Breakdown & Performance Chart -->
      <div class="card">
        <div class="section-header-bar">
          <div>
            <h2 class="section-title"><i class="fa-solid fa-chart-column"></i> Calories Burned Breakdown</h2>
            <p class="muted" style="font-size: 0.88rem; margin-top: 2px;">Proportion of energy burned across training disciplines.</p>
          </div>
          <c:if test="${not empty summary.favouriteWorkout}">
            <span class="badge CARDIO"><i class="fa-solid fa-crown"></i> Top: <c:out value="${summary.favouriteWorkout}"/></span>
          </c:if>
        </div>

        <c:choose>
          <c:when test="${empty summary.caloriesByWorkout}">
            <div class="empty-state">
              <i class="fa-solid fa-chart-pie empty-icon"></i>
              <h3 style="font-size: 1.15rem; font-weight: 600; margin-bottom: 6px;">No Performance Data</h3>
              <p class="muted" style="font-size: 0.92rem; max-width: 360px; margin: 0 auto 16px;">
                Complete and log your first training session to view your caloric distribution chart.
              </p>
              <a href="${ctx}/app/progress" class="btn outline-btn small-btn">
                <i class="fa-solid fa-plus"></i> Log a Workout Now
              </a>
            </div>
          </c:when>
          <c:otherwise>
            <div class="chart-container">
              <c:forEach var="e" items="${summary.caloriesByWorkout}">
                <div class="chart-bar-item">
                  <div class="chart-bar-label">
                    <span><c:out value="${e.key}"/></span>
                    <strong style="color: var(--text-main);"><c:out value="${e.value}"/> kcal</strong>
                  </div>
                  <div class="chart-bar-track">
                    <div class="chart-bar-fill" data-width="${summary.totalCalories > 0 ? (e.value * 100 / summary.totalCalories) : 0}%"></div>
                  </div>
                </div>
              </c:forEach>
            </div>

            <div style="background: var(--bg-canvas); border-radius: 12px; padding: 14px 18px; display: flex; align-items: center; justify-content: space-between; margin-top: 20px;">
              <div>
                <span style="font-size: 0.78rem; text-transform: uppercase; font-weight: 600; color: var(--text-secondary); display: block;">Most Consistent Activity</span>
                <strong style="font-size: 1.05rem; color: var(--text-main);"><c:out value="${summary.favouriteWorkout}"/></strong>
              </div>
              <div class="stat-icon" style="background: #ffffff;"><i class="fa-solid fa-medal" style="color: #f59e0b;"></i></div>
            </div>
          </c:otherwise>
        </c:choose>
      </div>
    </div>

    <!-- Interactive BMI & Nutrition Guidance Row -->
    <div class="grid two" style="margin-bottom: 28px;">
      <!-- BMI Calculator Card -->
      <div class="card">
        <div class="section-header-bar">
          <div>
            <span class="eyebrow"><i class="fa-solid fa-calculator"></i> Quick Metric</span>
            <h2 class="section-title">BMI &amp; Active Target Tool</h2>
          </div>
        </div>
        <p class="muted" style="font-size: 0.9rem; margin-bottom: 16px;">
          Calculate your Body Mass Index and discover your optimal daily active caloric target.
        </p>

        <div class="grid two" style="gap: 12px; margin-bottom: 16px;">
          <div class="form-group" style="margin-bottom: 0;">
            <label for="calcHeight">Height (cm)</label>
            <input type="number" id="calcHeight" placeholder="175" min="100" max="250" value="175">
          </div>
          <div class="form-group" style="margin-bottom: 0;">
            <label for="calcWeight">Weight (kg)</label>
            <input type="number" id="calcWeight" placeholder="70" min="30" max="250" step="0.5" value="${summary.latestWeight > 0 ? summary.latestWeight : '70'}">
          </div>
        </div>
        <button type="button" id="btnCalculateBMI" class="btn blue-btn full-width" style="height: 44px;">
          <i class="fa-solid fa-bolt"></i> Compute BMI &amp; Targets
        </button>

        <div id="bmiResultBox" class="calc-result-box" style="margin-top: 16px; padding: 16px;">
          <div class="calc-result-grid">
            <div>
              <span style="font-size: 0.76rem; color: var(--text-secondary); font-weight: 600; text-transform: uppercase;">BMI</span>
              <div id="bmiValueText" style="font-size: 1.6rem; font-weight: 700;">-</div>
            </div>
            <div>
              <span style="font-size: 0.76rem; color: var(--text-secondary); font-weight: 600; text-transform: uppercase;">Category</span>
              <div><span id="bmiCategoryBadge" class="badge CARDIO">-</span></div>
            </div>
            <div>
              <span style="font-size: 0.76rem; color: var(--text-secondary); font-weight: 600; text-transform: uppercase;">Target Burn</span>
              <div id="bmiTargetCalText" style="font-size: 0.95rem; font-weight: 600; color: var(--accent-blue);">-</div>
            </div>
          </div>
        </div>
      </div>

      <!-- Trainer Recommendations & Nutrition Guide -->
      <div class="card">
        <div class="section-header-bar">
          <div>
            <span class="eyebrow"><i class="fa-solid fa-apple-whole"></i> Holistic Health</span>
            <h2 class="section-title">Trainer Recommendations &amp; Diet Guide</h2>
          </div>
        </div>
        <div style="display: flex; flex-direction: column; gap: 12px; margin-top: 8px;">
          <div style="background: var(--bg-canvas); padding: 12px 16px; border-radius: 12px; border: 1px solid var(--hairline);">
            <strong style="font-size: 0.9rem; color: var(--text-main); display: block; margin-bottom: 2px;">
              <i class="fa-solid fa-bottle-water" style="color: var(--accent-blue);"></i> Daily Hydration Target
            </strong>
            <span class="muted" style="font-size: 0.84rem;">Consume at least 35 ml of water per kg of body weight daily (approx. 2.5–3.0 liters).</span>
          </div>
          <div style="background: var(--bg-canvas); padding: 12px 16px; border-radius: 12px; border: 1px solid var(--hairline);">
            <strong style="font-size: 0.9rem; color: var(--text-main); display: block; margin-bottom: 2px;">
              <i class="fa-solid fa-egg" style="color: #d97706;"></i> Protein Synthesis &amp; Nutrition
            </strong>
            <span class="muted" style="font-size: 0.84rem;">Prioritize 1.6–2.2g of protein per kg of body weight for lean muscle repair and satiety.</span>
          </div>
          <div style="background: var(--bg-canvas); padding: 12px 16px; border-radius: 12px; border: 1px solid var(--hairline);">
            <strong style="font-size: 0.9rem; color: var(--text-main); display: block; margin-bottom: 2px;">
              <i class="fa-solid fa-moon" style="color: #6366f1;"></i> Circadian Recovery
            </strong>
            <span class="muted" style="font-size: 0.84rem;">Target 7.5 to 8.5 hours of uninterrupted sleep to maximize growth hormone release and nervous system reset.</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Recent Session Activity Feed -->
    <div class="card">
      <div class="section-header-bar">
        <div>
          <h2 class="section-title"><i class="fa-solid fa-clock-rotate-left"></i> Recent Activity &amp; Training History</h2>
          <p class="muted" style="font-size: 0.88rem; margin-top: 2px;">Your latest recorded workout sessions.</p>
        </div>
        <a href="${ctx}/app/progress" class="btn outline-btn small-btn">
          <i class="fa-solid fa-list"></i> View All Sessions
        </a>
      </div>

      <c:choose>
        <c:when test="${empty recentLogs}">
          <div class="empty-state">
            <i class="fa-solid fa-notes-medical empty-icon"></i>
            <p class="muted" style="margin-bottom: 12px;">No sessions logged yet. Record your first workout to start building your streak!</p>
            <a href="${ctx}/app/progress" class="btn blue-btn small-btn"><i class="fa-solid fa-plus"></i> Log a Workout</a>
          </div>
        </c:when>
        <c:otherwise>
          <div class="table-wrap">
            <table class="styled-table">
              <thead>
                <tr>
                  <th>Date Logged</th>
                  <th>Workout Routine</th>
                  <th>Duration</th>
                  <th>Recorded Weight</th>
                  <th>Estimated Burn</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="l" items="${recentLogs}" begin="0" end="4">
                  <tr>
                    <td><c:out value="${l.loggedOn}"/></td>
                    <td><strong><c:out value="${l.workoutTitle}"/></strong></td>
                    <td><c:out value="${l.minutes}"/> min</td>
                    <td><c:out value="${l.weightKg}"/> kg</td>
                    <td><span style="font-weight: 600; color: var(--cardio-text);"><c:out value="${l.calories}"/> kcal</span></td>
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
