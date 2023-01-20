<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%--
  Created by IntelliJ IDEA.
  User: kimchanghee
  Date: 22. 12. 17.
  Time: 오후 9:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>조회 결과</title>
    <meta name="viewport" content="width=device-width", initial-scale="1">
    <link rel="shortcut icon" href="<c:url value='/static/image/favicons96.ico'/>" type="image/x-icon"/>
    <link rel="icon" href="<c:url value='/static/image/favicons96.ico'/>" type="image/x-icon"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
</head>
<body>
id: ${member.id}
password: ${member.passwd}
phone: ${member.phone}
name: ${member.userName}
car number: ${member.carNum}

<table class="table" id="parking-admin-page-table">
    <thead>
    <tr>
        <th scope="col">주차장 관리 번호</th>
        <th scope="col">등록 일자</th>
        <th scope="col">주차장 이름</th>
        <th scope="col">사용중인 차 대수</th>
    </tr>
    </thead>
    <tbody class="table-group-divider">
    <tr>
        <th scope="row">Mark</th>
        <td>Otto</td>
        <td>@mdo</td>
        <td>@mdo</td>
    </tr>
    <tr>
        <th scope="row">Jacob</th>
        <td>Jacob</td>
        <td>Thornton</td>
        <td>@fat</td>
    </tr>
    <tr>
        <th scope="row">Thornton</th>
        <td>Larry the Bird</td>
        <td>Larry the Bird</td>
        <td>
            <input id="" type="text" name="parkingNo" class="form-control"
                   placeholder="숫자 입력"
            style="width: 100px">
        </td>
    </tr>
    </tbody>
</table>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>
</html>
