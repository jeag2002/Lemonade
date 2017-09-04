<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<fmt:setLocale value="${locale}"/>

<t:template>
	<script>			
		$(function() {			
			$('input:button').button();
			$('input:submit').button();
			

			$('#cancel').click(function() {
				window.location.href = 'generalSelector?entity=client';
			});			

		});
	</script>
	
	<form name="formMain" id="formMain" method="post" action="generalEditor?entity=client&action=save">
		<label>First name:</label>
		<input type="text" name="firstName" id="firstName" value="${entity.firstName}" class="text"  maxlength="128"><br>

		<label>Last name:</label>
		<input type="text" name="lastName" id="lastName" value="${entity.lastName}" class="text"  maxlength="128"><br>


		<input type="hidden" name="id" id="id" value="${entity.id}">
		<input type="hidden" name="entity" id="entity" value="client">

		<input type="submit" name="save" id="save" value="Save">
		<input type="button" name="cancel" id="cancel" value="Cancel">
		
				
	</form>	
</t:template>


