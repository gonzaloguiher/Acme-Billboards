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

	<form:form action="request/customer/edit.do" modelAttribute="request">

		<form:hidden path="id" />
		<form:hidden path="customer" />
		<form:hidden path="status" />
		
		<jstl:if test="${request.id != 0}">
		<form:hidden path="pakage" />
		</jstl:if>

		<acme:textarea code="request.commentsCustomer" path="commentsCustomer" /><br />

		<jstl:if test="${request.id == 0}">
		<acme:select items="${packages}" itemLabel="title" code="request.pakages" path="pakage" /><br />
		</jstl:if>

		<acme:submit name="save" code="request.save" />
		<acme:cancel url="request/customer/list.do" code="request.cancel" />
		<br />

	</form:form>

</security:authorize>

<security:authorize access="hasRole('MANAGER')">

	<form:form action="request/manager/edit.do" modelAttribute="request">

		<form:hidden path="id" />
		<form:hidden path="customer" />

		<form:label path="status">
			<spring:message code="request.status" />
		</form:label>	
		<form:select path="status">
			<form:option value="0" label="----" />		
			<form:options items="${statuses}" />
		</form:select>
		<form:errors path="status" cssClass="error" />
		<br/>
		
		<acme:textarea code="request.commentsManager" path="commentsManager" /><br />

		<div id="some-div">
  		<img src="http://www.brokenarrowwear.com/embroidery/img/infodot.png" class="infodot" />
  
  		<span id="some-element">
		If the request is rejected, it is recommended to fill in the comment field.
  		</span> </div>
		
		<acme:submit name="save" code="request.save" /> 
		<acme:cancel url="request/manager/list.do" code="request.cancel" />
		<br />

	</form:form>

</security:authorize>
