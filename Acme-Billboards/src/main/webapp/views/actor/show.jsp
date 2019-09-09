<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

	<div>		
		<b><spring:message code="actor.username"/></b>:   <jstl:out value="${actor.userAccount.username}"/> <br/>			
		<b><spring:message code="actor.name"/></b>:       <jstl:out value="${actor.name}"/> <br/>
		<b><spring:message code="actor.middleName"/></b>: <jstl:out value="${actor.middleName}"/> <br/>
		<b><spring:message code="actor.surname"/></b>:    <jstl:out value="${actor.surname}"/> <br/>
		<b><spring:message code="actor.address"/></b>:    <jstl:out value="${actor.address}"/> <br/>
		<b><spring:message code="actor.phone"/></b>:      <jstl:out value="${actor.phone}"/> <br/>
		<b><spring:message code="actor.photo"/></b>:      <jstl:out value="${actor.photo}"/> <br/>				 				 
		<b><spring:message code="actor.email"/></b>:      <jstl:out value="${actor.email}"/> <br/>			 				 
		<jstl:if test="${isCustomer}">
		<b><spring:message code="actor.vatNumber"/></b>: <jstl:out value="${actor.vatNumber}"/> <br/>
		</jstl:if>
	</div>
	<br/>
	
	<jstl:if test="${logged}">
	
	<jstl:if test="${isCustomer}">
		<b><spring:message code="actor.creditCard.holder"/></b>: <jstl:out value="${actor.creditCard.holder}"/> <br/>
		<b><spring:message code="actor.creditCard.make"/></b>:   <jstl:out value="${actor.creditCard.make}"/> <br/>
		<b><spring:message code="actor.creditCard.number"/></b>: <jstl:out value="${actor.creditCard.number}"/> <br/>
		<b><spring:message code="actor.creditCard.CVV"/></b>:    <jstl:out value="${actor.creditCard.CVV}"/> <br/>
		<b><spring:message code="actor.creditCard.expiration"/></b>: <jstl:out value="${actor.creditCard.expirationDate.getMonth()}"/> / <jstl:out value="${actor.creditCard.expirationDate.getYear()+1900}"/> <br/>
		<br>
	</jstl:if>
	
	</jstl:if>
		
	<jstl:if test="${logged}">
		
	<jstl:if test="${isManager}">
		<b><a href="actor/manager/editPersonal.do">   <spring:message code="actor.edit" /></a> Personal Data</b> <br/>
		<b><a href="actor/manager/editUserAccount.do"><spring:message code="actor.edit" /></a> User Account </b> <br/>
	</jstl:if> 
		
	<jstl:if test="${isCustomer}">
		<b><a href="actor/customer/editPersonal.do">   <spring:message code="actor.edit" /></a> Personal Data</b> <br/>
		<b><a href="actor/customer/editUserAccount.do"><spring:message code="actor.edit" /></a> User Account </b> <br/>
		<b><a href="actor/customer/editCreditCard.do"> <spring:message code="actor.edit" /></a> Credit Card  </b> <br/>
	</jstl:if> 
		
	<jstl:if test="${isAdmin}">
		<b><a href="actor/administrator/editPersonal.do">   <spring:message code="actor.edit" /></a> Personal Data</b> <br/>
		<b><a href="actor/administrator/editUserAccount.do"><spring:message code="actor.edit" /></a> User Account </b> <br/>
	</jstl:if> <br>
		
	</jstl:if>
		
	<jstl:if test="${logged}">
		<a href="actor/delete.do" onclick="confirmLeave();">Delete ALL data</a> <br/>
		<a href="actor/generateData.do"> <spring:message code="actor.generate" /> </a> <br/>
	</jstl:if>
		
	<input type="button" name="back" value="<spring:message code="actor.show.back" />" onclick="javascript: window.location.replace('')" /> <br/>
	
<script>
	function confirmLeave() {
	if(!confirm("Are you sure about this? this operation is not reversible and will delete all your data...")) return;
	}
</script>