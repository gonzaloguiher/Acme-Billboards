<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('ADMIN')">

	<spring:message code="administrator.datatable"/>
	<table style="width:'100%' border='0' align='center' ">
			<tr>
				<th><spring:message code="administrator.type"/></th>
				<th><spring:message code="administrator.requestsPerManager" /></th>
				<th><spring:message code="administrator.requestsPerCustomer"/></th>
				<th><spring:message code="administrator.requestsPerManagerStatusPending" /></th>
				<th><spring:message code="administrator.requestsPerCustomerStatusPending"/></th>
			</tr>
			<tr>
				<td><spring:message code="administrator.average"/></td>
				<td><jstl:out value="${avgRequestsPerManager}  "/></td>
				<td><jstl:out value="${avgRequestsPerCustomer} "/></td>	
				<td><jstl:out value="${avgRequestsPerManagerStatusPending}  "/></td>		
				<td><jstl:out value="${avgRequestsPerCustomerStatusPending} "/></td>
			</tr>
			<tr>
				<td><spring:message code="administrator.minimum"/></td>
				<td><jstl:out value="${minRequestsPerManager}  "/></td>
				<td><jstl:out value="${minRequestsPerCustomer} "/></td>
				<td><jstl:out value="${minRequestsPerManagerStatusPending}  "/></td>
				<td><jstl:out value="${minRequestsPerCustomerStatusPending} "/></td>
			</tr>	
			<tr>
				<td><spring:message code="administrator.maximum"/></td>
				<td><jstl:out value="${maxRequestsPerManager}  "/></td>
				<td><jstl:out value="${maxRequestsPerCustomer} "/></td>
				<td><jstl:out value="${maxRequestsPerManagerStatusPending}  "/></td>
				<td><jstl:out value="${maxRequestsPerCustomerStatusPending} "/></td>
			</tr>
			<tr>
				<td><spring:message code="administrator.stdv"/></td>
				<td><jstl:out value="${stdevRequestsPerCustomer}"/></td>
				<td><jstl:out value="${stdevRequestsPerCustomer}"/></td>
			</tr>
	</table>
	
	<b><spring:message code="administrator.top10ManagersRequestsStatusPending"/></b>
	<table style="width:'100%' border='0' align='center' ">
		<jstl:forEach var="i" items="${top10ManagersRequestsStatusPending}">
		<tr>
			<td><jstl:out value="${i.name}"/> <jstl:out value="${i.surname}"/></td>
		</tr>			
		</jstl:forEach>
	</table>
	
</security:authorize>