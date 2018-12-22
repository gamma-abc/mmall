<html>
<body>
<h2>Hello World!</h2>
<%--enctype="multipart/form-data"--%>
<form action="${pageContext.request.contextPath}/update/he.do" method="post" >
    <input type = "text"  name = "username" /><br>
    file<input type="file"     name = "password" /><br>
    <input type = "submit" value = "submit">
</form>
</body>
</html>
