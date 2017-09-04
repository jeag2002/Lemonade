$.datepicker.setDefaults({
	dateFormat: 'dd/mm/yy'
});

jQuery.fn.forceDouble = function () {

	return this.each(function () {
		$(this).keydown(function (e) {
			var key = e.which || e.keyCode;


			if (!e.shiftKey && !e.altKey && !e.ctrlKey &&
					// numbers   
					(key >= 48 && key <= 57) ||
					// Numeric keypad
					(key >= 96 && key <= 105) ||
					// period 
					(key == 190 || key == 110) ||
					// Backspace and Tab and Enter
					key == 8 || key == 9 || key == 13 ||
					// Home and End
					key == 35 || key == 36 ||
					// left and right arrows
					key == 37 || key == 39 ||
					// Del and Ins
					key == 46 || key == 45) {		                     
				return true;
			}
			else if(e.shiftKey){
				return (key >= 48 && key <= 57);
			}

			return false;
		});
	});
}



// in case they exist, show the feedback messages (used to show ajax feedback)
function showFeedbackMessages(data, feedbackContainerId) {
	
	$('#feedbackList').remove();
	
	if(containsFeedbackMessage(data)) {
		$('#'+feedbackContainerId).append('<ul id="feedbackList"></ul>');
		for(var key in data){																				
			$('#feedbackList').append('<li>'+data[key]+'</li>');
		}
		
		$('#'+feedbackContainerId).show();
	}
	else {
		$('#'+feedbackContainerId).hide();
	}
}

// check if the json data object ontains any feedback message
function containsFeedbackMessage(data) {
	
	for(var key in data){
		if(key.indexOf("feedback")==0)
			return true;
	}
	
	return false;
}

function dateToStr(date) {
	var result = '';
	
	var arr = date.split(',');
	var month = arr[0].split(' ')[0];
	var day = arr[0].split(' ')[1];
	var year = arr[1].split(' ')[1];
	
	
	return day + '/' + monthNameToNumber(month) + '/' + year;
}

function monthNameToNumber(month) {
	var result = 0;
	
	if(month == 'Jan')
		result = 1;
	else if(month == 'Feb')
		result = 2;
	else if(month == 'Mar')
		result = 3;
	else if(month == 'Apr')
		result = 4;
	else if(month == 'May')
		result = 5;
	else if(month == 'Jun')
		result = 6;
	else if(month == 'Jul')
		result = 7;
	else if(month == 'Aug')
		result = 8;
	else if(month == 'Sep')
		result = 9;
	else if(month == 'Oct')
		result = 10;
	else if(month == 'Nov')
		result = 11;
	else if(month =='Dic')
		result = 12;
	
	return result;
}