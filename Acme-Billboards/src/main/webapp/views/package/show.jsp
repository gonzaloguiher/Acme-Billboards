<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

		<div>
		<spring:message code="package.moment.format" var="formatMoment"/>
		<b><spring:message code="package.ticker" /></b>: <jstl:out value="${pakage.ticker}" /> <br/>
		<b><spring:message code="package.title"  /></b>: <jstl:out value="${pakage.title}"  /> <br/> 
		<b><spring:message code="package.description" /></b>: <jstl:out value="${pakage.description}" /> <br/>
		<b><spring:message code="package.startMoment" /></b>: <jstl:out value="${pakage.startMoment}" /> <br/>
	    <b><spring:message code="package.endMoment"   /></b>: <jstl:out value="${pakage.endMoment}"   /> <br/>
	    <b><spring:message code="package.price"   /></b>: <jstl:out value="${pakage.price}"   /> <br/> 
	    <b><spring:message code="package.photo"   /></b>: <jstl:out value="${pakage.photo}"   /> <br/> 
	    <b><spring:message code="package.manager"  /></b>: <a href="actor/show.do?actorId=${pakage.manager.id}"><jstl:out value="${pakage.manager.userAccount.username}"/></a><br/>
		</div>
		
		<security:authorize access="hasRole('MANAGER')">
			<acme:cancel url="/package/manager/list.do" code="package.back"/>
		</security:authorize>
