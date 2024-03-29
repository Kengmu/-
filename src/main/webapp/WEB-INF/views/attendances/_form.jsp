<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="action" value="${ForwardConst.ACT_ATT.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commCrt" value="${ForwardConst.CMD_CREATE.getValue()}" />
<c:set var="commUpt" value="${ForwardConst.CMD_UPDATE.getValue()}" />


<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>

    </div>
</c:if>



<label for="name">氏名</label><br />
<c:out value="${sessionScope.login_employee.name}" />
<br /><br />


<button type="${AttributeConst.ATT_ATTENDANCE_AT_WORK.getValue()}">出勤</button><br />
<br /><br />

<button type="${AttributeConst.BREAK_START.getValue()}">休憩開始</button><br />
<br /><br />

<button type="${AttributeConst.END_OF_BREAK.getValue()}">休憩終了</button><br />
<br /><br />

<button type="${AttributeConst.LEAVING_WORK.getValue()}">退勤</button><br />
<br /><br />



<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
<input type="hidden" name="${AttributeConst.ATT_ID.getValue()}" value="${attendance.id}" />


