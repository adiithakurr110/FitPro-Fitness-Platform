/* FitPro client-side helpers (progressive enhancement; the server validates everything again). */
document.addEventListener("DOMContentLoaded", function () {

  // 1. Animate the dashboard bar chart: width = this workout's share of total calories
  document.querySelectorAll(".bar .fill").forEach(function (el) {
    var value = parseFloat(el.dataset.value) || 0;
    var total = parseFloat(el.dataset.total) || 1;
    setTimeout(function () { el.style.width = Math.round((value / total) * 100) + "%"; }, 100);
  });

  // 2. Highlight invalid fields before the form is submitted
  document.querySelectorAll("form.validate").forEach(function (form) {
    form.addEventListener("submit", function (event) {
      var ok = true;
      form.querySelectorAll("input, select").forEach(function (field) {
        var bad = !field.checkValidity();
        field.classList.toggle("invalid", bad);
        if (bad) ok = false;
      });
      if (!ok) event.preventDefault();
    });
  });

  // 3. Fade out success / error messages after a few seconds
  document.querySelectorAll(".alert").forEach(function (box) {
    setTimeout(function () { box.style.transition = "opacity .6s"; box.style.opacity = "0"; }, 5000);
  });

  // 4. Instant Live Search on Workout Catalogue
  var searchInput = document.getElementById("workoutSearch");
  if (searchInput) {
    searchInput.addEventListener("input", function () {
      var term = searchInput.value.toLowerCase().trim();
      var cards = document.querySelectorAll(".workout-card");
      var visibleCount = 0;

      cards.forEach(function (card) {
        var title = (card.querySelector("h3") ? card.querySelector("h3").textContent : "").toLowerCase();
        var desc = (card.querySelector(".workout-desc") ? card.querySelector(".workout-desc").textContent : "").toLowerCase();
        var meta = (card.querySelector(".workout-meta-bar") ? card.querySelector(".workout-meta-bar").textContent : "").toLowerCase();

        var match = title.includes(term) || desc.includes(term) || meta.includes(term);
        card.style.display = match ? "" : "none";
        if (match) visibleCount++;
      });

      var emptyNotice = document.querySelector(".empty-search-notice");
      if (!emptyNotice) {
        emptyNotice = document.createElement("div");
        emptyNotice.className = "empty-search-notice full-width empty-state";
        emptyNotice.style.display = "none";
        emptyNotice.innerHTML = '<i class="fa-solid fa-magnifying-glass empty-icon"></i><p class="muted">No matching workouts found for your query.</p>';
        var grid = document.querySelector(".workout-grid");
        if (grid) grid.appendChild(emptyNotice);
      }
      emptyNotice.style.display = (visibleCount === 0 && term !== "") ? "block" : "none";
    });
  }

  // 5. Interactive BMI & Calorie Target Calculator
  var btnCalc = document.getElementById("btnCalculateBMI");
  if (btnCalc) {
    btnCalc.addEventListener("click", function () {
      var heightCm = parseFloat(document.getElementById("calcHeight").value);
      var weightKg = parseFloat(document.getElementById("calcWeight").value);

      if (!heightCm || heightCm <= 0 || !weightKg || weightKg <= 0) {
        alert("Please enter a valid height (cm) and weight (kg).");
        return;
      }

      var heightM = heightCm / 100.0;
      var bmi = weightKg / (heightM * heightM);
      var bmiFormatted = bmi.toFixed(1);

      var category = "";
      var badgeClass = "";
      var targetBurn = "";

      if (bmi < 18.5) {
        category = "Underweight";
        badgeClass = "YOGA";
        targetBurn = "300 - 400 kcal / day (Focus on Strength)";
      } else if (bmi < 25.0) {
        category = "Normal Weight";
        badgeClass = "CARDIO";
        targetBurn = "400 - 550 kcal / day (Maintain Fitness)";
      } else if (bmi < 30.0) {
        category = "Overweight";
        badgeClass = "STRENGTH";
        targetBurn = "500 - 700 kcal / day (Cardio + Strength)";
      } else {
        category = "Obese";
        badgeClass = "STRENGTH";
        targetBurn = "600 - 850 kcal / day (Structured Program)";
      }

      var resultBox = document.getElementById("bmiResultBox");
      var valText = document.getElementById("bmiValueText");
      var catBadge = document.getElementById("bmiCategoryBadge");
      var targetText = document.getElementById("bmiTargetCalText");

      valText.textContent = bmiFormatted;
      catBadge.textContent = category;
      catBadge.className = "badge " + badgeClass;
      targetText.textContent = targetBurn;

      resultBox.style.display = "block";
    });
  }
});
