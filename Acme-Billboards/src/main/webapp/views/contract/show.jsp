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
	<b><spring:message code="contract.text" /></b>: <jstl:out value="${contract.text}" /> <br/>
	<b><spring:message code="contract.hash"  /></b>: <jstl:out value="${contract.hash}"  /> <br/> 
	<b><spring:message code="contract.momentCustomer" /></b>: <jstl:out value="${contract.momentCustomer}" /> <br/>
	<b><spring:message code="contract.momentManager" /></b>:  <jstl:out value="${contract.momentManager}"  /> <br/>
	<b><spring:message code="contract.signCustomer"   /></b>: <jstl:out value="${contract.signCustomer}"   /> <br/>
	<b><spring:message code="contract.signManager"   /></b>:  <jstl:out value="${contract.signManager}"    /> <br/> 
	<b><spring:message code="contract.package"   /></b>: <a href="package/show.do?packageId=${contract.pakage.id}"><jstl:out value="${contract.pakage.ticker}"/></a><br/>
	</div>
		
	<h1> <spring:message code="contract.files" /> </h1>
			
	<display:table name="files" id="row" requestURI="${requestURI}" pagesize="5">
		
	<security:authorize access="hasRole('MANAGER')">
		<jstl:if test="${row.contract.signManager == null}">
		<display:column>
			<a href="file/edit.do?fileId=${row.id}">   <spring:message code="file.edit"   /> </a> <br>
			<a href="file/delete.do?fileId=${row.id}"> <spring:message code="file.delete" /> </a> <br>
		</display:column>
		</jstl:if>
	</security:authorize>
		
    <display:column titleKey="file.location" property="location" />
    <display:column titleKey="file.image"    property="image"    />
		    
	</display:table>
	
	<security:authorize access="hasRole('MANAGER')">
	<jstl:if test="${row.contract.signManager == null}"> <a href="file/create.do?contractId=${contract.id}"> <spring:message code="file.create" /> </a> <br> </jstl:if>
	</security:authorize> <br>
		
	<security:authorize access="hasRole('MANAGER')">
		<acme:cancel url="/contract/manager/list.do" code="contract.back"/>
	</security:authorize>
		
	<security:authorize access="hasRole('CUSTOMER')">
		<acme:cancel url="/contract/customer/list.do" code="contract.back"/>
	</security:authorize>
