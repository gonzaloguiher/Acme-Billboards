<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('MANAGER')">

	<form:form action="contract/manager/sign.do" modelAttribute="contract">

		<form:hidden path="id" />
		<form:hidden path="version" />
		
		<form:hidden path="text" />
		<form:hidden path="hash" />
		<form:hidden path="momentCustomer" />
		<form:hidden path="momentManager"  />
		<form:hidden path="signCustomer"   />
		<form:hidden path="status" />

		<acme:textbox  code="contract.signManager" path="signManager"/>
		
		<acme:submit code="contract.save" name="save"/>
		
		<acme:cancel url="contract/manager/list.do" code="contract.cancel"/>

	</form:form>
	
</security:authorize>

<security:authorize access="hasRole('CUSTOMER')">

	<form:form action="contract/customer/sign.do" modelAttribute="contract">

		<form:hidden path="id" />
		<form:hidden path="version" />
		
		<form:hidden path="text" />
		<form:hidden path="hash" />
		<form:hidden path="momentCustomer" />
		<form:hidden path="momentManager"  />
		<form:hidden path="signManager"   />
		<form:hidden path="status" />

		<acme:textbox  code="contract.signCustomer" path="signCustomer"/>
		
		<acme:submit code="contract.save" name="save"/>
		
		<acme:cancel url="contract/customer/list.do" code="contract.cancel"/>

	</form:form>
	
</security:authorize>