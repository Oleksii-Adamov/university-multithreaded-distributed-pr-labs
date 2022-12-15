<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>New country</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
</head>
<body>
  <form action="<c:url value='/countries'/>" method="get" id="getCountriesForm" role="form">
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <a class="navbar-brand" href="#" onclick="document.getElementById('getCountriesForm').submit();">Home</a>
    </nav>
  </form>
  <div class="container">
    <form action="<c:url value='/countries'/>" method="post" role="form" data-toggle="validator" >
      <input type="hidden" id="action" name="action" value="add">
      <h2>Country</h2>
        <c:if test="${not empty message}">
            <div class="alert alert-danger">
                    ${message}
            </div>
        </c:if>
      <div class="form-group col-xs-4">

        <label for="code" class="control-label col-xs-4">Code:</label>
        <input type="text" name="code" id="code" class="form-control" pattern="[0-9]+" placeholder="1" required="true" />

        <label for="name" class="control-label col-xs-4">Name:</label>
        <input type="text" name="name" id="name" class="form-control" required="true" />

        <br>
        <button type="submit" class="btn btn-primary  btn-md">Create</button>
      </div>
    </form>
  </div>
</body>
</html>
