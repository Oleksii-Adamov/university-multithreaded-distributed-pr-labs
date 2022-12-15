<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Country ${countryName}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.2/font/bootstrap-icons.css" integrity="sha384-b6lVK+yci+bfDmaY1u0zE8YYJt0TZxLEAFyYSLHId4xoVvsrQu3INevFKo+Xir8e" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
</head>
<body>
    <form action="<c:url value='/countries'/>" method="get" id="getCountriesForm" role="form">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <a class="navbar-brand" href="#" onclick="document.getElementById('getCountriesForm').submit();">Home</a>
        </nav>
    </form>
    <div class="container">
        <h2>Country ${countryName}</h2>
        <h4>Code: ${countryCode}</h4>
        <c:if test="${not empty message}">
            <div class="alert alert-success">
                    ${message}
            </div>
        </c:if>
        <form action="<c:url value='/country'/>" method="post" id="citiesForm" role="form">
            <input type="hidden" id="cityCode" name="cityCode">
            <input type="hidden" id="action" name="action">
            <input type="hidden" id="countryCode" name="countryCode" value="${countryCode}">
            <a href="#" id="editCountry"
               onclick="document.getElementById('action').value = 'redirectToEditCountry';
                       document.getElementById('citiesForm').submit();">
                Edit Country
            </a>
            <h3>Cities</h3>
            <c:choose>
                <c:when test="${not empty cities}">
                    <table  class="table table-striped">
                        <thead>
                        <tr>
                            <td>Code</td>
                            <td>Name</td>
                            <td>Is capital</td>
                            <td>Population</td>
                            <td></td>
                            <td></td>
                        </tr>
                        </thead>
                        <c:forEach var="city" items="${cities}">
                            <c:set var="classSucess" value=""/>
                            <c:if test ="${cityCode == city.code}">
                                <c:set var="classSucess" value="info"/>
                            </c:if>
                            <tr class="${classSucess}">
                                <td>${city.code}</td>
                                <td>${city.name}</td>
                                <td><c:out default="None" escapeXml="true" value="${city.isCapital == 1 ? 'Yes' : 'No'}" /></td>
                                <td>${city.count}</td>
                                <td><a href="#" id="edit"
                                       onclick="document.getElementById('action').value = 'redirectToEditCity';document.getElementById('cityCode').value = '${city.code}';
                                               document.getElementById('citiesForm').submit();">
                                    Edit
                                </a>
                                </td>
                                <td><a href="#" id="delete"
                                       onclick="document.getElementById('action').value = 'deleteCity';document.getElementById('cityCode').value = '${city.code}';
                                               document.getElementById('citiesForm').submit();">
                                    <span class="bi bi-trash text-dark"></span>
                                </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:when>
                <c:otherwise>
                    <br>
                    <div class="alert alert-info">
                        No cities found
                    </div>
                </c:otherwise>
            </c:choose>
        </form>
        <form action ="jsp/new-city.jsp">
            <br>
            <button type="submit" class="btn btn-primary  btn-md">New city</button>
        </form>
    </div>
</body>
</html>
