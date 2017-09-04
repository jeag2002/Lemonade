<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template>	

	<script>
		$(function(){ 
			$('input:button').button();
						
			$('#list').jqGrid({
		    	url:'generalSelector?entity=trip&action=get',
		    	datatype: 'json',
		    	colNames:['Id', 'Client', 'City', 'Date from', 'Date to', 'Hotel cost daily'],
		    	colModel :[ 
					{name:'id', index:'id', width:55, searchtype:'integer', searchoptions: { sopt: ['eq'] }, searchrules:{'number':true}},
					{name:'client', index:'client', width:200 },
					{name:'city', index:'city', width:200 },
					{name:'date_from', index:'date_from', width:100 },
					{name:'date_to', index:'date_to', width:100 },
					{name:'hotel_cost_daily', index:'hotel_cost_daily', width:100 , searchtype:'integer', searchoptions: { sopt: ['eq'] }, searchrules:{'number':true}}
					
		    	],
		    	pager: '#pager',
		    	rowNum:100,
		    	rowList:[100,150,200],
		    	sortname: 'id',
		    	sortorder: 'asc',
		    	viewrecords: true,
		    	gridview: true,
		    	caption: 'Trips',			    				    			    				    
		    	ondblClickRow: function(id){
					if(id)
						window.location.href = 'generalEditor?entity=trip&id='+id;						
				}
		  	}); 

			
			jQuery('#list').jqGrid('navGrid','#pager', 
						{	addfunc: 	function(){								
											window.location.href = 'generalEditor?entity=trip';										
										},
							editfunc: 	function(id){
											if(id)
												window.location.href = 'generalEditor?entity=trip&id='+id;										
										},
							delfunc:	function(id) {
											if(id) {
												if(confirm('delete?')) {
													jQuery.ajax({	url: 'generalEditor?entity=trip&action=delete&id='+id,
																	success: function(){
																		jQuery('#list').trigger("reloadGrid");
													  				}
															});
												}
											}		
										},
							edit:true,
							add:true,
							del:true,
							search:true}, 
						{}, 
						{}, 
						{}, 
						{multipleSearch:true, multipleGroup:false, showQuery: false, closeOnEscape:true, closeAfterSearch:true} ); //filter options

			jQuery('#list').setGridHeight(400, true); 			
			
		}); 
	</script>

	<div id="selectorContainer">
		<table id="list"><tr><td/></tr></table> 
		<div id="pager"></div>						
	</div> 	

</t:template>


