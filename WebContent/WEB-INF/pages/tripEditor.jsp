<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<fmt:setLocale value="${locale}"/>

<t:template>
	<script>		

		//31-08-2017 JALCARAZ (ISSUE 5)
		////////////////////////////////////////////////////
		function changeCalcTotCost(){

			var urlAjax = "generalAjaxServlet?action=calcTotCost&dateFrom="+$('#dateFrom').val()+"&dateTo="+$('#dateTo').val()+"&hotelCostDaily="+$('#hotelCostDaily').val()+"&travelCost="+$('#travelCost').val()+"&sigCost="+$('#sigCost').val();
			$.ajax({
				type: "post",
				url: urlAjax,
				success: function(d){
					$('#totCost').val(d);
				}
			});
		}
		////////////////////////////////////////////////////
		$(function() {			
			$('input:button').button();
			$('input:submit').button();
			

			$('#dateFrom').datepicker();
			$('#dateTo').datepicker();
			$('#hotelCostDaily').forceDouble();
			$('#travelCost').forceDouble();
			$('#sigCost').forceDouble();

			//31-08-2017 JALCARAZ (ISSUE 5)
			///////////////////////////////////////////////
			$('#totCost').css('color', 'red');
			changeCalcTotCost();
			///////////////////////////////////////////////
			

			$('#client').autocomplete({
							source: 'generalAjaxServlet?action=getEntityForAutocomplete&entity=client',
							minLength: 2,
							select: function(event, ui) {
										$('#clientId').val(ui.item.value);
										ui.item.value = ui.item.label;
										$('#clientId').change();
									},
							change: function(event, ui) {
										if(ui.item == null) {
											$('#clientId').val('');
											$('#client').val('');
											$('#clientId').change();
										}
									},
							focus: function(event, ui) {
										$('#client').val(ui.item.label);
										return false;
									},
							create: function(event, ui) {
										if($('#clientId').val()) {
											$.ajax({
												url: 'generalAjaxServlet?action=getValueForAutocompleteSelection&entity=client&id='+$('#clientId').val(),
												success: function(data) {
													$('#client').val(data);
												}
											});
										}
									}
			});


			$('#cancel').click(function() {
				window.location.href = 'generalSelector?entity=trip';
			});		

			//31-08-2017 JALCARAZ (ISSUE 3)
			//////////////////////////////////////////////////////////////////////////////////////////////////////////
			$('#city').autocomplete({
							source: 'generalAjaxServlet?action=getEntityForAutocomplete&entity=city',
							minLength: 2,
							select: function(event, ui) {
										$('#cityId').val(ui.item.value);
										ui.item.value = ui.item.label;
										$('#cityId').change();
									},
							change: function(event, ui) {
										if(ui.item == null) {
											$('#cityId').val('');
											$('#city').val('');
											$('#cityId').change();
										}
									},
							focus: function(event, ui) {
										$('#city').val(ui.item.label);
										return false;
									},
							create: function(event, ui) {
										if($('#cityId').val()) {
											$.ajax({
												url: 'generalAjaxServlet?action=getValueForAutocompleteSelection&entity=city&id='+$('#cityId').val(),
												success: function(data) {
													$('#city').val(data);
												}
											});
										}
									}
			});

				
			//////////////////////////////////////////////////////////////////////////////////////////////////////////
		});
	</script>
	
	<form name="formMain" id="formMain" method="post" action="generalEditor?entity=trip&action=save">
		
		<!-- 31-08-2017 JALCARAZ (ISSUE 5) -->
		<input type="hidden" name="totCostData" id="totCostData" value="0">
		<!-- 31-08-2017 JALCARAZ (ISSUE 5) -->
		
		
		<label>Client:</label>
		<input id="client" name="client">
		<input type="hidden" name="clientId" id="clientId" value="${entity.client.id}"> <br>
		
		<!-- 31-08-2017 JALCARAZ ISSUE 3: -->
		<label>City:</label>
		<input id="city" name="city">
		<input type="hidden" name="cityId" id="cityId" value="${entity.city.id}"> <br>
		<!-- 31-08-2017 JALCARAZ ISSUE 3: -->

		<label>Date from:</label>
		<fmt:formatDate pattern="dd/MM/yyyy" value="${entity.dateFrom}" var="dateFromFormatted" />
		<input type="text" name="dateFrom" id="dateFrom" value="${dateFromFormatted}" class="text" onchange="changeCalcTotCost()"><br>

		<label>Date to:</label>
		<fmt:formatDate pattern="dd/MM/yyyy" value="${entity.dateTo}" var="dateToFormatted" />
		<input type="text" name="dateTo" id="dateTo" value="${dateToFormatted}" class="text" onchange="changeCalcTotCost()"><br>

		<label>Hotel cost daily:</label>
		<fmt:formatNumber value="${entity.hotelCostDaily}" groupingUsed="true" var="hotelCostDailyFormatted" type="number"/>
		<input type="text" name="hotelCostDaily" id="hotelCostDaily" value="${hotelCostDailyFormatted}" class="text" onchange="changeCalcTotCost()" ><br>

		<label>Travel cost:</label>
		<fmt:formatNumber value="${entity.travelCost}" groupingUsed="true" var="travelCostFormatted" type="number"/>
		<input type="text" name="travelCost" id="travelCost" value="${travelCostFormatted}" class="text" onchange="changeCalcTotCost()" ><br>
		
		<!-- 31-08-2017 JALCARAZ (ISSUE 4) -->
		<label>Excursion cost:</label>
		<fmt:formatNumber value="${entity.sigCost}" groupingUsed="true" var="sigCostFormatted" type="number"/>
		<input type="text" name="sigCost" id="sigCost" value="${sigCostFormatted}" class="text" onchange="changeCalcTotCost()"><br>
		<!-- 31-08-2017 JALCARAZ (ISSUE 4) -->
		
		<!-- 31-08-2017 JALCARAZ (ISSUE 5) -->
		<label>Total cost:</label>
		<input type="text" name="totCost" id="totCost" class="text" disabled="true" value="0" ><br>
		
		<!-- 31-08-2017 JALCARAZ (ISSUE 5) -->
		
		<input type="hidden" name="id" id="id" value="${entity.id}">
		<input type="hidden" name="entity" id="entity" value="trip">
				
		<input type="submit" name="save" id="save" value="Save">
		<input type="button" name="cancel" id="cancel" value="Cancel">
		
				
	</form>	
</t:template>


