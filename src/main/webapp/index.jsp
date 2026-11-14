<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Home" />
<%@ include file="WEB-INF/views/header.jspf" %>

<!-- Hero Section -->
<section class="landing-hero">
  <span class="eyebrow"><i class="fa-solid fa-sparkles"></i> Minimalist Digital Training</span>
  <h1>Your fitness. Your pace. Your progress.</h1>
  <p>
    An elegant, distraction-free fitness platform. Explore curated training programs,
    track sessions with scientific MET accuracy, and reach your peak performance with certified coaching.
  </p>
  <div class="landing-cta-group">
    <c:choose>
      <c:when test="${not empty sessionScope.user}">
        <a class="btn primary-btn" href="${ctx}/app/dashboard">
          <i class="fa-solid fa-gauge-high"></i> Go to Dashboard
        </a>
        <a class="btn outline-btn" href="${ctx}/app/workouts">
          <i class="fa-solid fa-compass"></i> Explore Workouts
        </a>
      </c:when>
      <c:otherwise>
        <a class="btn primary-btn" href="${ctx}/register">
          <i class="fa-solid fa-arrow-right"></i> Get Started Free
        </a>
        <a class="btn outline-btn" href="${ctx}/app/workouts">
          <i class="fa-solid fa-compass"></i> Explore Workouts
        </a>
      </c:otherwise>
    </c:choose>
  </div>
</section>

<!-- Visual Showcase Ribbon -->
<div class="landing-visual-wrap">
  <div class="landing-device-screen">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; border-bottom: 1px solid var(--hairline); padding-bottom: 16px;">
      <div style="display: flex; align-items: center; gap: 10px;">
        <span style="width: 12px; height: 12px; border-radius: 50%; background: #ff5f56; display: inline-block;"></span>
        <span style="width: 12px; height: 12px; border-radius: 50%; background: #ffbd2e; display: inline-block;"></span>
        <span style="width: 12px; height: 12px; border-radius: 50%; background: #27c93f; display: inline-block;"></span>
        <span style="font-size: 0.85rem; font-weight: 600; color: var(--text-secondary); margin-left: 8px;">FitPro Studio Overview</span>
      </div>
      <span class="badge CARDIO"><i class="fa-solid fa-heart-pulse"></i> Live Metrics</span>
    </div>

    <div class="grid three">
      <div style="background: #ffffff; padding: 20px; border-radius: 16px; border: 1px solid var(--hairline);">
        <span style="font-size: 0.8rem; color: var(--text-secondary); font-weight: 600; text-transform: uppercase;">Active Energy</span>
        <div style="font-size: 1.8rem; font-weight: 700; margin: 4px 0; color: var(--text-main);">580 <span style="font-size: 1rem; font-weight: 500; color: var(--cardio-text);">kcal</span></div>
        <div class="chart-bar-track" style="margin-top: 8px;"><div class="chart-bar-fill" style="width: 78%; background: var(--cardio-text);"></div></div>
      </div>
      <div style="background: #ffffff; padding: 20px; border-radius: 16px; border: 1px solid var(--hairline);">
        <span style="font-size: 0.8rem; color: var(--text-secondary); font-weight: 600; text-transform: uppercase;">Exercise Time</span>
        <div style="font-size: 1.8rem; font-weight: 700; margin: 4px 0; color: var(--text-main);">45 <span style="font-size: 1rem; font-weight: 500; color: var(--strength-text);">min</span></div>
        <div class="chart-bar-track" style="margin-top: 8px;"><div class="chart-bar-fill" style="width: 85%; background: var(--strength-text);"></div></div>
      </div>
      <div style="background: #ffffff; padding: 20px; border-radius: 16px; border: 1px solid var(--hairline);">
        <span style="font-size: 0.8rem; color: var(--text-secondary); font-weight: 600; text-transform: uppercase;">Recovery &amp; Vitals</span>
        <div style="font-size: 1.8rem; font-weight: 700; margin: 4px 0; color: var(--text-main);">Optimal <span style="font-size: 1rem; font-weight: 500; color: var(--yoga-text);">98%</span></div>
        <div class="chart-bar-track" style="margin-top: 8px;"><div class="chart-bar-fill" style="width: 98%; background: var(--yoga-text);"></div></div>
      </div>
    </div>
  </div>
</div>

<!-- Features Section -->
<section id="features" class="features-section">
  <div class="section-headline-center">
    <span class="eyebrow">Product Architecture</span>
    <h2>Engineered for clarity and focus.</h2>
    <p>Every tool is designed to support your athletic journey without visual noise or unnecessary complexity.</p>
  </div>

  <div class="grid two">
    <div class="feature-box">
      <div class="feature-icon-bubble"><i class="fa-solid fa-dumbbell"></i></div>
      <h3>Personalized Workouts</h3>
      <p>
        Scientifically calibrated training programs spanning Cardio, Strength, and Yoga.
        Each routine utilizes dynamic Metabolic Equivalent (MET) factors to compute energy expenditure tailored to your body weight.
      </p>
    </div>

    <div class="feature-box">
      <div class="feature-icon-bubble"><i class="fa-solid fa-chart-line"></i></div>
      <h3>Intelligent Progress Tracking</h3>
      <p>
        Log workout duration, track weight transitions over time, and export verified session history
        into standard CSV analytical reports with one tap.
      </p>
    </div>

    <div class="feature-box">
      <div class="feature-icon-bubble"><i class="fa-solid fa-user-ninja"></i></div>
      <h3>Certified Trainer Guidance</h3>
      <p>
        Train under programs designed and managed by certified coaches.
        Trainers monitor member adherence, evaluate workout histories, and provide actionable recommendations.
      </p>
    </div>

    <div class="feature-box">
      <div class="feature-icon-bubble"><i class="fa-solid fa-heart-pulse"></i></div>
      <h3>Holistic Fitness Planning</h3>
      <p>
        Instant Body Mass Index (BMI) assessment coupled with dynamic daily calorie targets and
        nutrition guidelines tailored to your current training regime.
      </p>
    </div>
  </div>
</section>

<!-- Workout Showcase Banner -->
<section id="workouts" style="padding: 48px 0;">
  <div class="section-headline-center">
    <span class="eyebrow">Workout Disciplines</span>
    <h2>Three distinct paths to strength.</h2>
    <p>Tailored programs designed for every stage of your physical development.</p>
  </div>

  <div class="grid three">
    <div class="card hover-lift">
      <span class="badge CARDIO" style="margin-bottom: 12px;"><i class="fa-solid fa-heart-pulse"></i> Cardio Endurance</span>
      <h3 style="font-size: 1.25rem; font-weight: 600; margin-bottom: 8px;">High-Intensity &amp; Stamina</h3>
      <p class="muted" style="font-size: 0.92rem; line-height: 1.5; margin-bottom: 20px;">
        Elevate VO2 max and burn calories with progressive interval training, sprints, and metabolic conditioning.
      </p>
      <a class="link-arrow" href="${ctx}/app/workouts?type=CARDIO">Browse Cardio Programs <i class="fa-solid fa-chevron-right"></i></a>
    </div>

    <div class="card hover-lift">
      <span class="badge STRENGTH" style="margin-bottom: 12px;"><i class="fa-solid fa-dumbbell"></i> Hypertrophy &amp; Power</span>
      <h3 style="font-size: 1.25rem; font-weight: 600; margin-bottom: 8px;">Full Body &amp; Resistance</h3>
      <p class="muted" style="font-size: 0.92rem; line-height: 1.5; margin-bottom: 20px;">
        Build functional muscle and core stability with structured dumbbell, barbell, and calisthenic progressions.
      </p>
      <a class="link-arrow" href="${ctx}/app/workouts?type=STRENGTH">Browse Strength Programs <i class="fa-solid fa-chevron-right"></i></a>
    </div>

    <div class="card hover-lift">
      <span class="badge YOGA" style="margin-bottom: 12px;"><i class="fa-solid fa-spa"></i> Mobility &amp; Balance</span>
      <h3 style="font-size: 1.25rem; font-weight: 600; margin-bottom: 8px;">Vinyasa Flow &amp; Recovery</h3>
      <p class="muted" style="font-size: 0.92rem; line-height: 1.5; margin-bottom: 20px;">
        Enhance flexibility, joint health, and mental equilibrium through guided breathing and restorative flow.
      </p>
      <a class="link-arrow" href="${ctx}/app/workouts?type=YOGA">Browse Yoga Programs <i class="fa-solid fa-chevron-right"></i></a>
    </div>
  </div>
</section>

<!-- How It Works Section -->
<section id="how-it-works" style="padding: 48px 0;">
  <div class="section-headline-center">
    <span class="eyebrow">Streamlined Workflow</span>
    <h2>How FitPro works.</h2>
    <p>Get started in minutes with our three-step fitness journey.</p>
  </div>

  <div class="grid three">
    <div class="step-card">
      <span class="step-num">STEP 01</span>
      <h3 style="font-size: 1.2rem; font-weight: 600; margin-bottom: 8px;">Select Your Program</h3>
      <p class="muted" style="font-size: 0.92rem; line-height: 1.5;">
        Browse the catalogue, filter by discipline or difficulty, and enroll into programs that match your schedule.
      </p>
    </div>

    <div class="step-card">
      <span class="step-num">STEP 02</span>
      <h3 style="font-size: 1.2rem; font-weight: 600; margin-bottom: 8px;">Train &amp; Log Sessions</h3>
      <p class="muted" style="font-size: 0.92rem; line-height: 1.5;">
        Complete your workout and log your minutes and current weight. FitPro calculates exact polymorphic caloric burn.
      </p>
    </div>

    <div class="step-card">
      <span class="step-num">STEP 03</span>
      <h3 style="font-size: 1.2rem; font-weight: 600; margin-bottom: 8px;">Track &amp; Progress</h3>
      <p class="muted" style="font-size: 0.92rem; line-height: 1.5;">
        Monitor personal records, analyze historical charts, and download CSV reports to verify athletic improvements.
      </p>
    </div>
  </div>
</section>

<!-- Interactive Quick Tool on Landing Page -->
<section style="padding: 32px 0;">
  <div class="calculator-card">
    <div class="section-header-bar">
      <div>
        <span class="eyebrow"><i class="fa-solid fa-wand-magic-sparkles"></i> Instant Tool</span>
        <h2 class="section-title">Body Mass Index (BMI) &amp; Caloric Target Calculator</h2>
        <p class="muted" style="font-size: 0.92rem; margin-top: 4px;">Calculate your health metrics instantly to calibrate your daily active calorie burn goals.</p>
      </div>
    </div>

    <div class="grid three" style="align-items: flex-end; margin-top: 20px;">
      <div class="form-group" style="margin-bottom: 0;">
        <label for="calcHeight">Height (cm)</label>
        <input type="number" id="calcHeight" placeholder="e.g. 175" min="100" max="250" value="175">
      </div>
      <div class="form-group" style="margin-bottom: 0;">
        <label for="calcWeight">Body Weight (kg)</label>
        <input type="number" id="calcWeight" placeholder="e.g. 70" min="30" max="250" step="0.5" value="70">
      </div>
      <div>
        <button type="button" id="btnCalculateBMI" class="btn primary-btn full-width" style="height: 48px;">
          <i class="fa-solid fa-bolt"></i> Compute BMI
        </button>
      </div>
    </div>

    <div id="bmiResultBox" class="calc-result-box">
      <div class="calc-result-grid">
        <div>
          <span style="font-size: 0.82rem; color: var(--text-secondary); text-transform: uppercase; font-weight: 600;">Calculated BMI</span>
          <div id="bmiValueText" style="font-size: 2rem; font-weight: 700; color: var(--text-main);">-</div>
        </div>
        <div>
          <span style="font-size: 0.82rem; color: var(--text-secondary); text-transform: uppercase; font-weight: 600;">Health Status</span>
          <div style="margin-top: 4px;"><span id="bmiCategoryBadge" class="badge CARDIO">-</span></div>
        </div>
        <div>
          <span style="font-size: 0.82rem; color: var(--text-secondary); text-transform: uppercase; font-weight: 600;">Recommended Active Burn</span>
          <div id="bmiTargetCalText" style="font-size: 1.1rem; font-weight: 600; color: var(--accent-blue); margin-top: 4px;">-</div>
        </div>
      </div>
    </div>
  </div>
</section>

<!-- Final Call to Action -->
<section id="about" style="text-align: center; padding: 72px 20px; background: #ffffff; border-radius: 28px; border: 1px solid var(--hairline); margin: 32px 0;">
  <span class="eyebrow">Get Started Today</span>
  <h2 style="font-size: clamp(2rem, 4vw, 2.8rem); font-weight: 700; letter-spacing: -0.025em; margin-bottom: 12px;">Start your fitness journey.</h2>
  <p class="muted" style="max-width: 580px; margin: 0 auto 28px; font-size: 1.05rem;">
    Join thousands of athletes tracking their workouts with precision, clarity, and certified coach support.
  </p>
  <div style="display: flex; gap: 14px; justify-content: center; flex-wrap: wrap;">
    <a href="${ctx}/register" class="btn primary-btn" style="padding: 12px 28px; font-size: 1rem;">
      <i class="fa-solid fa-user-plus"></i> Create Free Account
    </a>
    <a href="${ctx}/login" class="btn outline-btn" style="padding: 12px 28px; font-size: 1rem;">
      <i class="fa-solid fa-arrow-right-to-bracket"></i> Sign In to FitPro
    </a>
  </div>
</section>

<%@ include file="WEB-INF/views/footer.jspf" %>
