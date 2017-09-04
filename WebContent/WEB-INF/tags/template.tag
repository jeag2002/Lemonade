<%@tag description="Template for vanillitravels" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<title>vanillitravels</title>
				
		<link type="text/css" href="site/css/redmond/jquery-ui-1.8.16.custom.css" rel="Stylesheet" />
		<link type="text/css" href="site/css/ui.jqgrid.css" rel="stylesheet" />
		<link type="text/css" href="site/css/colorbox.css" rel="stylesheet" />
		<link type="text/css" href="site/css/960_12_10_10.css" rel="stylesheet" />
		<link type="text/css" href="site/css/style.css" rel="stylesheet" />	
		
		<script type="text/javascript" src="site/js/jquery-1.6.2.min.js"></script>
		<script type="text/javascript" src="site/js/jquery-ui-1.8.16.custom.min.js"></script>	
		<script type="text/javascript" src="site/js/i18n/grid.locale-en.js"></script>
		<script type="text/javascript" src="site/js/jquery.jqGrid.min.js"></script> <!-- jq grid -->
		<script type="text/javascript" src="site/js/jquery.colorbox.js"></script> <!-- popup -->
		<script type="text/javascript" src="site/js/utils.js"></script>				
						
	</head>
	<body>
			
		<div class="container_12">
		
			<div class="grid_12 alpha omega headerContainer">
				<h2 class="grid_5 prefix_1 alpha">vanillitravels</h2>
				<div class="grid_7 omega">					 
				</div>
			</div>
			
			
			<div class="grid_2 alpha menuContainer">				
				<ul class="menu">
					<li class="current">		
						<ul class="submenu">
							<li>
								<a href="generalSelector?entity=client">Clients</a>
							</li>
							<li>
								<a href="generalSelector?entity=city">Cities</a>
							</li>
							<li>
								<a href="generalSelector?entity=trip">Trips</a>
							</li>
						</ul>		
					</li>
						
				</ul>				
			</div>
			
			<div class="grid_10 omega">				
				<div id="contentContainer">
					<jsp:doBody/>			
				</div>
			</div>
			
			<c:choose>			
				<c:when test="${feedback != null && feedback.show}">
					<div id="feedbackContainer" class="grid_6 alpha omega">			
						<ul>
							<c:forEach var="feedbackMessage" items="${feedback.messages}">                
                    			<li>${feedbackMessage.message}</li>
                    			<c:if test="${not empty feedbackMessage.field}">
	                  				<script>
										$(function() {												
											$('#${feedbackMessage.field}').addClass('fieldError');
											$('#${feedbackMessage.field}').focus();
										});
							        </script>
						        </c:if>	
                    			                    			                    		
                			</c:forEach>
                		</ul>			
					</div>
				</c:when>
			</c:choose>										
		</div>			
	</body>
</html>


