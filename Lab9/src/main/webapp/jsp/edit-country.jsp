<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Edit country</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
</head>
<body>
<div class="container">
    <form action="<c:url value='/country'/>" method="post" role="form" data-toggle="validator" >
        <input type="hidden" id="action" name="action" value="editCountry">
        <input type="hidden" id="countryCode" name="countryCode" value="${country.code}">
        <h2>Country</h2>
        <c:if test="${not empty message}">
            <div class="alert alert-danger">
                    ${message}
            </div>
        </c:if>
        <div class="form-group col-xs-4">

            <label for="name" class="control-label col-xs-4">Name:</label>
            <input type="text" name="name" id="name" class="form-control" required="true" value="${country.name}"/>

            <br>
            <button type="submit" class="btn btn-primary  btn-md">Update</button>
        </div>
    </form>
</div>
</body>
