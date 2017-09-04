<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template>	

	<script>
		$(function(){ 
			$('input:button').button();
						
			$('#list').jqGrid({
		    	url:'generalSelector?entity=city&action=get',
		    	datatype: 'json',
		    	colNames:['Id', 'Name'],
		    	colModel :[ 
					{name:'id', index:'id', width:55, searchtype:'integer', searchoptions: { sopt: ['eq'] }, searchrules:{'number':true}},
					{name:'name', index:'name', width:500 , searchoptions: { sopt: ['eq', 'bw', 'ew'] }}
					
		    	],
		    	pager: '#pager',
		    	rowNum:100,
		    	rowList:[100,150,200],
		    	sortname: 'id',
		    	sortorder: 'asc',
		    	viewrecords: true,
		    	gridview: true,
		    	caption: 'Cities',			    				    			    				    
		    	ondblClickRow: function(id){
					if(id)
						window.location.href = 'generalEditor?entity=city&id='+id;						
				}
		  	}); 

			
			jQuery('#list').jqGrid('navGrid','#pager', 
						{	addfunc: 	function(){								
											window.location.href = 'generalEditor?entity=city';										
										},
							editfunc: 	function(id){
											if(id)
												window.location.href = 'generalEditor?entity=city&id='+id;										
										},
							delfunc:	function(id) {
											if(id) {
												if(confirm('delete?')) {
													jQuery.ajax({	url: 'generalEditor?entity=city&action=delete&id='+id,
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


