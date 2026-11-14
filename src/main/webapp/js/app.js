/* ==========================================================================
   FitPro Client-Side Enhancements (Vanilla JS)
   Accessible, lightweight, responsive, and robust.
   ========================================================================== */

document.addEventListener("DOMContentLoaded", function () {

  // 1. Mobile Menu Drawer Toggle
  var mobileToggle = document.querySelector(".mobile-toggle");
  var mobileDrawer = document.querySelector(".mobile-drawer");
  if (mobileToggle && mobileDrawer) {
    mobileToggle.addEventListener("click", function () {
      var isOpen = mobileDrawer.classList.toggle("open");
      mobileToggle.setAttribute("aria-expanded", isOpen ? "true" : "false");
      var icon = mobileToggle.querySelector("i");
      if (icon) {
        icon.className = isOpen ? "fa-solid fa-xmark" : "fa-solid fa-bars";
      }
    });
  }

  // 2. Password Visibility Toggle
  document.querySelectorAll(".password-toggle-btn").forEach(function (btn) {
    btn.addEventListener("click", function () {
      var targetId = btn.getAttribute("data-target");
      var input = targetId ? document.getElementById(targetId) : btn.parentElement.querySelector("input");
      if (!input) return;

      var isPassword = input.type === "password";
      input.type = isPassword ? "text" : "password";

      var icon = btn.querySelector("i");
      if (icon) {
        icon.className = isPassword ? "fa-regular fa-eye-slash" : "fa-regular fa-eye";
      }
    });
  });

  // 3. Form Validation Feedback
  document.querySelectorAll("form.validate").forEach(function (form) {
    form.addEventListener("submit", function (event) {
      var isValid = true;
      form.querySelectorAll("input, select, textarea").forEach(function (field) {
        if (!field.checkValidity()) {
          field.classList.add("invalid");
          isValid = false;
        } else {
          field.classList.remove("invalid");
        }
      });
      if (!isValid) {
        event.preventDefault();
      }
    });
  });

  // 4. Dismissible Alerts with smooth fade
  document.querySelectorAll(".alert").forEach(function (alertBox) {
    setTimeout(function () {
      alertBox.style.transition = "opacity 0.6s ease, transform 0.6s ease";
      alertBox.style.opacity = "0";
      alertBox.style.transform = "translateY(-6px)";
      setTimeout(function () {
        if (alertBox.parentNode) alertBox.parentNode.removeChild(alertBox);
      }, 650);
    }, 6000);
  });

  // 5. Chart Bar Fill Animation
  document.querySelectorAll(".chart-bar-fill").forEach(function (fillBar) {
    var width = fillBar.getAttribute("data-width");
    if (width) {
      fillBar.style.width = "0%";
      setTimeout(function () {
        fillBar.style.width = width;
      }, 150);
    }
  });

  // 6. Live Instant Search for Workout Catalogue
  var searchInput = document.getElementById("workoutSearch");
  if (searchInput) {
    searchInput.addEventListener("input", function () {
      var query = searchInput.value.toLowerCase().trim();
      var cards = document.querySelectorAll(".workout-card");
      var visibleCount = 0;

      cards.forEach(function (card) {
        var title = (card.querySelector(".workout-card-title") ? card.querySelector(".workout-card-title").textContent : "").toLowerCase();
        var desc = (card.querySelector(".workout-card-desc") ? card.querySelector(".workout-card-desc").textContent : "").toLowerCase();
        var meta = (card.querySelector(".workout-meta-ribbon") ? card.querySelector(".workout-meta-ribbon").textContent : "").toLowerCase();

        var match = title.includes(query) || desc.includes(query) || meta.includes(query);
        card.style.display = match ? "" : "none";
        if (match) visibleCount++;
      });

      var emptyNotice = document.querySelector(".empty-search-notice");
      if (!emptyNotice) {
        emptyNotice = document.createElement("div");
        emptyNotice.className = "empty-search-notice empty-state";
        emptyNotice.innerHTML = '<i class="fa-solid fa-magnifying-glass empty-icon"></i><p class="muted">No matching workouts found.</p>';
        var grid = document.querySelector(".workout-grid");
        if (grid) grid.appendChild(emptyNotice);
      }
      if (emptyNotice) {
        emptyNotice.style.display = (visibleCount === 0 && query !== "") ? "block" : "none";
      }
    });
  }

  // 7. Interactive BMI & Calorie Target Calculator
  var calcBtn = document.getElementById("btnCalculateBMI");
  if (calcBtn) {
    calcBtn.addEventListener("click", function () {
      var heightInput = document.getElementById("calcHeight");
      var weightInput = document.getElementById("calcWeight");
      if (!heightInput || !weightInput) return;

      var heightCm = parseFloat(heightInput.value);
      var weightKg = parseFloat(weightInput.value);

      if (!heightCm || heightCm <= 50 || heightCm > 260 || !weightKg || weightKg <= 20 || weightKg > 350) {
        alert("Please enter a valid height (cm) and weight (kg).");
        return;
      }

      var heightM = heightCm / 100.0;
      var bmi = weightKg / (heightM * heightM);
      var bmiFormatted = bmi.toFixed(1);

      var category = "";
      var badgeClass = "";
      var targetText = "";

      if (bmi < 18.5) {
        category = "Underweight";
        badgeClass = "YOGA";
        targetText = "300 – 450 kcal / day (Focus on Strength & Nutrition)";
      } else if (bmi < 25.0) {
        category = "Normal Weight";
        badgeClass = "CARDIO";
        targetText = "400 – 550 kcal / day (Optimal Maintenance & Stamina)";
      } else if (bmi < 30.0) {
        category = "Overweight";
        badgeClass = "STRENGTH";
        targetText = "500 – 700 kcal / day (Structured Cardio + Hypertrophy)";
      } else {
        category = "Obese";
        badgeClass = "STRENGTH";
        targetText = "600 – 850 kcal / day (Supervised Progressive Regimen)";
      }

      var resultBox = document.getElementById("bmiResultBox");
      var valText = document.getElementById("bmiValueText");
      var catBadge = document.getElementById("bmiCategoryBadge");
      var calTarget = document.getElementById("bmiTargetCalText");

      if (valText) valText.textContent = bmiFormatted;
      if (catBadge) {
        catBadge.textContent = category;
        catBadge.className = "badge " + badgeClass;
      }
      if (calTarget) calTarget.textContent = targetText;
      if (resultBox) resultBox.style.display = "block";
    });
  }

});
