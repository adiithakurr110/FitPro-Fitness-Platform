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
});
