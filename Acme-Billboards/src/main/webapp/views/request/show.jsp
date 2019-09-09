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
        <b><spring:message code="request.status" /></b>: <jstl:out value="${request.status}" /> <br/>
		<b><spring:message code="request.commentsCustomer"  /></b>: <jstl:out value="${request.commentsCustomer}"  /> <br/> 
		<b><spring:message code="request.commentsManager"   /></b>: <jstl:out value="${request.commentsManager}"   /> <br/>
	    <b><spring:message code="request.customer"  /></b>: <a href="actor/show.do?actorId=${request.customer.id}"><jstl:out value="${request.customer.userAccount.username}"/></a><br/>
	    </div>
	    
	    <jstl:if test="${request.contract != null}">
	    
	    <div>
	    <h2><spring:message code="request.contract"/>:</h2>
	    <b><spring:message code="request.contract.text"   /></b>: <jstl:out value="${request.contract.text}"   /> <br/> 
	    <b><spring:message code="request.contract.hash"   /></b>: <jstl:out value="${request.contract.hash}"   /> <br/> 
	    <b><spring:message code="request.contract.momentCustomer" /></b>: <jstl:out value="${request.contract.momentCustomer}" /> <br/>
		<b><spring:message code="request.contract.momentManager"  /></b>: <jstl:out value="${request.contract.momentManager}"  /> <br/>
		</div>
		
		</jstl:if>
		
		<div>
		<h2><spring:message code="request.pakage"/>:</h2>
		<b><spring:message code="request.pakage.ticker" /></b>: <jstl:out value="${request.pakage.ticker}" /> <br/>
		<b><spring:message code="request.pakage.title"  /></b>: <jstl:out value="${request.pakage.title}"  /> <br/> 
		<b><spring:message code="request.pakage.description" /></b>: <jstl:out value="${request.pakage.description}" /> <br/>
		<b><spring:message code="request.pakage.startMoment" /></b>: <jstl:out value="${request.pakage.startMoment}" /> <br/>
	    <b><spring:message code="request.pakage.endMoment"   /></b>: <jstl:out value="${request.pakage.endMoment}"   /> <br/>
	    <b><spring:message code="request.pakage.price"   /></b>: <jstl:out value="${request.pakage.price}"   /> <br/> 
	    <b><spring:message code="request.pakage.photo"   /></b>: <jstl:out value="${request.pakage.photo}"   /> <br/> 
	    <b><spring:message code="request.pakage.manager"  /></b>: <a href="actor/show.do?actorId=${request.pakage.manager.id}"><jstl:out value="${request.pakage.manager.userAccount.username}"/></a><br/>
		</div>
		
		<security:authorize access="hasRole('CUSTOMER')">
			<acme:cancel url="/request/customer/list.do" code="request.back"/>
		</security:authorize>
		
		<security:authorize access="hasRole('MANAGER')">
			<acme:cancel url="/request/manager/list.do" code="request.back"/>
		</security:authorize>
