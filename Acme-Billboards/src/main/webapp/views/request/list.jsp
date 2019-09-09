<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('CUSTOMER')">

	<display:table name="requests" id="row" requestURI="request/customer/list.do" pagesize="5">
		
		<display:column class ="${row.status}">
		<a href="request/customer/show.do?requestId=${row.id}"><spring:message code="request.show" /></a> <br/>
		<jstl:if test="${row.status=='PENDING'}">
		<a href="request/customer/edit.do?requestId=${row.id}">  <spring:message code="request.edit"   /></a> <br/>
		</jstl:if>
		</display:column>
		
		<display:column property="status"        titleKey="request.status"   class ="${row.status}"/>
		<display:column property="pakage.title"  titleKey="request.pakage"   class ="${row.status}"/>
		<display:column property="customer.userAccount.username" titleKey="request.customer" class ="${row.status}"/>
		
	</display:table>
	
	<security:authorize access="hasRole('CUSTOMER')">
		<div>
			<a href="request/customer/create.do"> <spring:message code="request.create" /></a>
		</div>
	</security:authorize>

</security:authorize>

<security:authorize access="hasRole('MANAGER')">

	<display:table name="requests" id="row" requestURI="request/manager/list.do" pagesize="5">
		
		<display:column class ="${row.status}">
		<a href="request/manager/show.do?requestId=${row.id}"> <spring:message code="request.show" /></a> <br/>
		<jstl:if test="${row.status=='PENDING'}">
		<a href="request/manager/edit.do?requestId=${row.id}"> <spring:message code="request.edit" /></a> <br/>
		</jstl:if>
		</display:column>
		
		<display:column property="status"        titleKey="request.status"   class ="${row.status}"/>
		<display:column property="pakage.title"  titleKey="request.pakage"   class ="${row.status}"/>
		<display:column property="customer.userAccount.username" titleKey="request.customer" class ="${row.status}"/>
		
	</display:table>

</security:authorize>