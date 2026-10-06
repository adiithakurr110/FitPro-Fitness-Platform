<%-- Entry point: send visitors to the right page --%>
<% response.sendRedirect(request.getContextPath() + (session.getAttribute("user") != null ? "/app/dashboard" : "/login")); %>
