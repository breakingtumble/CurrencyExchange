<%@ page import="java.util.Date" %><%--
  Created by IntelliJ IDEA.
  User: yaroslav
  Date: 23.09.2023
  Time: 21:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>Testing JSP</h1>
    <%
        java.util.Date now = new Date();
        String someString = "Current date: " + now;
    %>

    <p>
        <%=someString%>
    </p>

</body>
</html>
