var apiRequests = [];
var httpStatusCodes = [];
var currentApiSelected;


function removeHeader(currentContext){
   
    $(currentContext).parent().remove();
}

function addHeader(){
    
	$('#ad-hdr').click(function(){

        $("#ad-hdr").before('<div class="input-group" id="header-input"> <div class="input-group-prepend"> <span class="input-group-text">Key & Value</span> </div> <div class="col-xs-2" id="key"> <input type="text" class="form-control"> </div> <div class="col-xs-4" id="value"> <input type="text" class="form-control" style="width: 400px;"/> </div> <span onclick="removeHeader(this)"><i class="fa fa-remove" style="color: red;margin-left: 10px; margin-top: 10px; cursor: pointer;"></i></span> </div>');
    
    })
}


 function addJsonResponse(){
	 
	 let options = [];
	 
	 $.each(httpStatusCodes,function(index,httpStatusCode){
			
		 	let option 
		 	if(httpStatusCode.code == 200){
		 		option = '<option selected value='+httpStatusCode.code+'>'+httpStatusCode.name+'</option>';
		 	} else {
		 		option = '<option value='+httpStatusCode.code+'>'+httpStatusCode.name+'</option>';
		 	}
			
			options.push(option);
			
		})

     $('#jsn-rsp').append(' <div class="border"> <div class="toogle-button"> <label class="switch float-right"> <input type="checkbox"> <span class="slider round" onClick="slider(this)"></span> </label> </div> <div class="form-group" style="width: 20%;margin-top: 10px;margin-left: 10px"> <select class="form-control">'+options+'</select> </div> <br> <span class="beautify-json" onClick="beautifyJson(this)">Beautify</span><div class="input-group response-body"> <div class="input-group-prepend"> <span class="input-group-text">Body</span> </div> <textarea class="form-control" aria-label="With textarea" rows="15"></textarea> </div> </div>');
 }

 function selectRow(currentElement){
	 
	 $('[href="#response-body-tab"]').tab('show');
	 
	 currentApiSelected = currentElement;

	 $(".bg-secondary").addClass("bg-light");
	 
	$(".bg-secondary").removeClass("bg-secondary");
	 
    $(currentElement).removeClass('bg-light');
     $(currentElement).addClass('bg-secondary');
     
     
     let apiId = parseInt($(currentElement).attr('id'));
     
     let selectedApiRequest = _.find(apiRequests,{id:apiId});
     
     $('#uri-txt').val(selectedApiRequest.url);
     
     $('#httpMethodDrpDwn').val(selectedApiRequest.httpMethod);
     
     
     getApiResponse(apiId);
     
     
 }
 
 function removeHeaders(){
	 $('#headers-tab #header-input').each(function(index,header){
		 $(header).remove();
	 })
 }
 
 

 function saveAPI(){
	 
	 if(!$('#createApiForm').valid()){
		 return;
	 }
	 $('#api-save-btn').hide();
	 $('#api-save-spinner').show();
	 
    let httpMethod = $('#inputState').val();
    let url = $('#addUri').val();

    let data = {url:url,httpMethod:httpMethod};

    $.ajax({
        type:"POST",
        url: "/fastub/api",
        dataType: 'json',
            contentType: 'application/json',
            success: function (data) {
            },
            data: JSON.stringify(data)
        
    }).done(function (data) {
        
    	 $('#api-save-btn').show();
    	 $('#api-save-spinner').hide();
    	 apiRequests.unshift(data);
    	 createApiRow(data);
    	 
    	 hideApiCreateGuide();
    	 
    	 let firstApiUrl = _.first(apiRequests);
    	 selectRow($('#'+firstApiUrl.id));
    	 
    	 $('#exampleModal').modal('toggle');
    	 clearCreateApiModalValues();
    	 
    	 showAlert("success","New API has been added into the list.");
    	 
    	 if(apiRequests.length == 1){
    		 $('#header-dlt-a').show();
    	 }
    	 
    }).fail(function (jqXHR, textStatus, errorThrown) {
       
    	alert(jqXHR.responseJSON.message);
    
        console.log(errorThrown);
        
        $('#api-save-btn').show();
   	 	$('#api-save-spinner').hide();
    });
 }
 

 
 function getBadge(httpMethod){
	 
	 let badge;
	 
	 switch (httpMethod){
	 
	 case "POST":
		 badge = "warning";
		 break;
	 case "PUT":
		 badge = "primary";
		 break;
	 case "DELETE":
		 badge = "danger";
		 break;
	 case "GET":
		 badge = "success";
		 break;
	 case "PATCH":
	 	badge = "secondary";
	 	break;
	 
	 }
	 
	 return badge;
	 
 }
 
 function getAllUrls(){
	 
	  $.ajax({
	        type:"GET",
	        url: "/fastub/api"
	        
	    }).done(function (data) {
	        
	    	if(data && data.length != 0){
	    		
	    		apiRequests = data;
		    	
		    	createApiRows(apiRequests);
		    	
		    	
		    	
		    	let firstApiUrl = _.first(apiRequests);
		    	currentApiSelected = $('#'+firstApiUrl.id);
		    	 $('.create-api-guide').hide();
		    	 $('.wlcm-card').hide();
	    	} else {
	    		$('.api-body').hide();
	    		$('#header-dlt-a').hide();
	    	}
	    	getMetaData();
	    	
	    	
	    }).fail(function (jqXHR, textStatus, errorThrown) {
	       
	    	alert(jqXHR.responseJSON.message);
	    
	        console.log(errorThrown);
	        
	    });
	 
 }
 
 getAllUrls();
 
 
 function getMetaData(){
	 
	 let url = "/fastub/api/meta-data";
	 
	 $.ajax({
	        type:"GET",
	        url: url
	        
	    }).done(function (httpStatusCodesResponse) {
	    	
	    	httpStatusCodes = httpStatusCodesResponse;
	    	
	    	if(apiRequests && apiRequests.length != 0){
	    		selectRow(currentApiSelected);
	    	}
	    	
	    }).fail(function (jqXHR, textStatus, errorThrown) {
	       
	    	alert(jqXHR.responseJSON.message);
	    
	        console.log(errorThrown);
	        
	    });
	 
 }
 
 function getApiResponse(apiId){
	 
	 let url = "/fastub/api/" + apiId + "/api-response";
	 
	 $.ajax({
	        type:"GET",
	        url: url
	        
	    }).done(function (apiResponseList) {
	    	removeResponseDivs();
	    	createResponseDivs(apiResponseList);
	    	
	    }).fail(function (jqXHR, textStatus, errorThrown) {
	       
	    	alert(jqXHR.responseJSON.message);
	    
	        console.log(errorThrown);
	        
	    });
	 
 }
 
 function updateApi(){
	 
	
	
	 $('#api-update-btn').hide();
	 $('#api-update-spinner').show();
	 
    let httpMethod = $('#httpMethodDrpDwn').val();
    
    let apiId = parseInt($(currentApiSelected).attr('id'));
    
    let url = "/fastub/api/"+apiId;
    
    let httpResponseList = [];
    
    let isJsonValid = true;
    
    
    $('#jsn-rsp .border').each(function(index,ele){
    	
    	let httpResponse = {};

    	httpResponse.statusCode = $(ele).find('select').val();
    	httpResponse.active = $(ele).find('.switch ').find('input').is(":checked");
    	
    	
    	try {
    		if($(ele).find('textarea').val()){
    			
    			
    				
    			var jsonObj = JSON.parse($(ele).find('textarea').val());
    			if(Array.isArray(jsonObj)){
    				httpResponse.response = jsonObj;
    			} else {
    				httpResponse.response1 = jsonObj;
    			}
    		}
    		
    	} catch (e){
    		alert(e);
    		isJsonValid = false;
    		return;
    	}
    	
    	if($(ele).find('textarea').attr('id')){
    		httpResponse.id = $(ele).find('textarea').attr('id').replace("txt-area-","");
    	}
    	
    	httpResponseList.push(httpResponse);
    })
    
    if(!isJsonValid){
    	 $('#api-update-btn').show();
    	 $('#api-update-spinner').hide();
    	return;
    }
    

    let data = {httpMethod:httpMethod,responseDtos:httpResponseList};

    $.ajax({
        type:"PUT",
        url: url,
        dataType: 'json',
            contentType: 'application/json',
            success: function (data) {
            	showAlert("success","API has been updated.");
            },
            data: JSON.stringify(data)
        
    }).done(function (data) {
    	
    	
    	$('#'+apiId).children('[name="badge-span"]').removeClass();
    	
    	$('#'+apiId).children('[name="badge-span"]').text(httpMethod);
    	
    	$('#'+apiId).children('[name="badge-span"]').addClass("badge badge-" + getBadge(httpMethod));
    	
    	removeResponseDivs();
    	createResponseDivs(data);
    	
    	
    	 $('#api-update-btn').show();
    	 $('#api-update-spinner').hide();
    	 
    }).fail(function (jqXHR, textStatus, errorThrown) {
       
    	alert(jqXHR.responseJSON.message);
    
        console.log(errorThrown);
        
        $('#api-update-btn').show();
   	 	$('#api-update-spinner').hide();
    });
	
 }
 
 function getResponseHeaders(){
	 
	
	 
	 let apiId = parseInt($(currentApiSelected).attr('id'));
	 
	 let httpApiUrl =  "/fastub/api/"+apiId + "/response-headers"
	 

	    $.ajax({
	        type:"GET",
	        url: httpApiUrl,
	        
	    }).done(function (responseHeaders) {
	    	
	    	 removeHeaders();
	    	
	    	
	    	createHeaderDiv(responseHeaders);


	    	 
	    	
	    	
	    }).fail(function (jqXHR, textStatus, errorThrown) {
	       
	    	alert(jqXHR.responseJSON.message);
	    
	        console.log(errorThrown);
	        
	        $('#api-update-btn').show();
	   	 	$('#api-update-spinner').hide();
	    });
 }
 
 function saveHeaders(){
	 
	 let apiId = parseInt($(currentApiSelected).attr('id'));
	 let httpApiUrl =  "/fastub/api/"+apiId + "/response-headers";
	 
	 
	 let headers = [];
	 
	 $('#headers-tab .input-group').each(function(index,ele){
		
		 let header = {};
		 header.key = $(ele).find('#key input').val();
		 header.value=$(ele).find('#value input').val();
		 if($(ele).find('#key input').attr('id')){
			 header.id = $(ele).find('#key input').attr('id').replace('key-',"");
		 }
		 
		 headers.push(header);
	 })
	 
	 
	 let data = {responseHeaders:headers};
	
	 
	 $.ajax({
	        type:"PUT",
	        url: httpApiUrl,
	        dataType: 'json',
            contentType: 'application/json',
            success: function (data) {
            	
            },
            data: JSON.stringify(data)
	        
	    }).done(function (responseHeaders) {
	    	
	    	$('#headers-tab #header-input').each(function(index,headerDiv){
	    		$(headerDiv).remove();
	    	});
	    	
	    	createHeaderDiv(responseHeaders);
	    	
	    	showAlert("success","Resonse headers has been updated.");
	    	
	    }).fail(function (jqXHR, textStatus, errorThrown) {
	       
	    	alert(jqXHR.responseJSON.message);
	    
	        console.log(errorThrown);
	        
	        $('#api-update-btn').show();
	   	 	$('#api-update-spinner').hide();
	    });
	 
 }
 
 function createHeaderDiv(responseHeaders){
	 
	 $.each(responseHeaders,function(index,responseHeader){
 		
 		if($('#key-'+responseHeader.id).length == 0){
 			 $("#ad-hdr").before('<div class="input-group" id="header-input"> <div class="input-group-prepend"> <span class="input-group-text" id="">Key & Value</span> </div> <div class="col-xs-2" id="key"> <input type="text" class="form-control" id=key-'+responseHeader.id+'> </div> <div class="col-xs-4" id="value"> <input type="text" class="form-control" style="width: 400px;" id=value-'+responseHeader.id+'> </div> <span onclick="removeHeader(this)"><i class="fa fa-remove" style="color: red;margin-left: 10px; margin-top: 10px; cursor: pointer;"></i></span> </div>');
 		}
 		
 		 $('#key-'+responseHeader.id).val(responseHeader.key);
 		 $('#value-'+responseHeader.id).val(responseHeader.value);
 	})
 }
 
 function removeResponseDivs(){
	 
	 $.each($('#jsn-rsp').children(),function(index,ele){
    	 $(ele).remove()
     })
 }
 
 function createResponseDivs(apiResponseList){
	 
	 $.each(apiResponseList, function( index, apiResponse ) {
 		
 		$('#jsn-rsp').append(' <div class="border"> <div class="toogle-button"> <label class="switch float-right"> <input type="checkbox" id=slider-'+apiResponse.id+'> <span class="slider round" onClick="slider(this)"></span> </label> </div> <div class="form-group" style="width: 20%;margin-top: 10px;margin-left: 10px"> <select id=response-code-'+apiResponse.id+' class="form-control"></select> </div> <br> <span class="beautify-json" onClick="beautifyJson(this)">Beautify</span> <div class="input-group response-body"> <div class="input-group-prepend"> <span class="input-group-text">Body</span> </div> <textarea class="form-control" aria-label="With textarea" rows="15" id=txt-area-'+apiResponse.id+'></textarea> </div> </div>');
 		
 			let id = apiResponse.id;
 			
 		
 			if(apiResponse.response){
 				$('#txt-area-' + id).val(JSON.stringify(apiResponse.response,null,4));
 			}
 			
 			if(apiResponse.response1){
 				$('#txt-area-' + id).val(JSON.stringify(apiResponse.response1,null,4));
 			}
 			
 			
 			$.each(httpStatusCodes,function(index,httpStatusCode){
 				
 				let option = '<option value='+httpStatusCode.code+'>'+httpStatusCode.name+'</option>';
 				$('#response-code-'+id).append(option);
 				
 			})
 			
 			$('#response-code-'+id).val(apiResponse.statusCode)
 			
 			if(apiResponse.active){
 				$('#slider-'+id).attr("checked", "checked");
 			}
 			
 		
 		});
 }
 
 function slider(ele){
	 
	 if(!$(ele).siblings('input').is(":checked")){
		 
		 $( ".switch" ).find(":checked").each(function(index,currentSwitchEle){
				
			 $(currentSwitchEle).prop("checked", false);
			 
		 });
	 }
	 
	 
 }
 
 
 function search(){
	 
	 let keyword = $('#search').val();
	 
	 $.each($('.scrollbar').children(),function(index,ele){
		 $(ele).remove();
	 })
	 
	 $.ajax({
	        type:"GET",
	        url: "/fastub/api/search?keyword="+keyword
	        
	    }).done(function (data) {
	        
	    	apiRequests = data;

	    	$.each(data, function( index, responseJson ) {
	    		
	    		
	    		
	    		
	    		$('.scrollbar').append('<div class="nav-list-style"> <p class="nav-p-style bg-light text-dark text-truncate text-justify" onclick="selectRow(this)" style="cursor:pointer" id='+responseJson.id+'> <span name="badge-span" class="badge badge-' + getBadge(responseJson.httpMethod) + '" style="font-size: 10px;">'+ responseJson.httpMethod + '</span> ' + responseJson.url + '</p> </div>')
	    		  
	    				 
	    		});
	    	
	    	let firstApiUrl = _.first(apiRequests);
	    	currentApiSelected = $('#'+firstApiUrl.id);
	    	
	    	selectRow(currentApiSelected);
	    	
	    }).fail(function (jqXHR, textStatus, errorThrown) {
	       
	    	alert(jqXHR.responseJSON.message);
	    
	        console.log(errorThrown);
	        
	    });
	 
 }
 
 function beautifyJson(ele){
	 
	 let textArea = $(ele).siblings('.response-body').children('textarea');
	 let uglyJson = $(textArea).val();
	 
	 try {
		 let parsedJson = JSON.parse(uglyJson);
		 let beautifyJson = JSON.stringify(parsedJson,null,4);
		 $(textArea).val(beautifyJson);
		 $(ele).next('.response-body').removeClass('has-error');
		 $(ele).next('.response-body').addClass('has-success');
	 } catch (e){
		 alert(e);
		 $(ele).next('.response-body').addClass('has-error');
		 return false;
	 }
	 
	 
 }
 
 function createApiRows(apiUrls){
	 
	 $.each(apiUrls, function( index, responseJson ) {
 		
 		
 		
 		
 		$('.scrollbar').append('<div class="nav-list-style"> <p class="nav-p-style bg-light text-dark text-truncate text-justify" onclick="selectRow(this)" style="cursor:pointer" id='+responseJson.id+'> <span name="badge-span" class="badge badge-' + getBadge(responseJson.httpMethod) + '" style="font-size: 10px;">'+ responseJson.httpMethod + '</span> ' + responseJson.url + '</p> </div>')
 		  
 				 
 		});
 }
 
function createApiRow(responseJson){
	 
	 $('.scrollbar').prepend('<div class="nav-list-style"> <p class="nav-p-style bg-light text-dark text-truncate text-justify" onclick="selectRow(this)" style="cursor:pointer" id='+responseJson.id+'> <span name="badge-span" class="badge badge-' + getBadge(responseJson.httpMethod) + '" style="font-size: 10px;">'+ responseJson.httpMethod + '</span> ' + responseJson.url + '</p> </div>')
	 
 }

function clearCreateApiModalValues(){
	$('#inputState').val('GET');
	$('#addUri').val("");
}

function deleteApi(){
	
	 $('#api-delete-btn').hide();
	 $('#api-delete-spinner').show();
	
	let apiId = parseInt($(currentApiSelected).attr('id'));
	
	$.ajax({
        type:"DELETE",
        url: "/fastub/api/" + apiId
        
    }).done(function (data) {
        
    	$('#api-delete-btn').show();
	 	$('#api-delete-spinner').hide();
    	
    	deleteDiv(apiId);
    	
    	
    	 $('#deletePathModal').modal('toggle');
    	 clearCreateApiModalValues();
    	 
    	 showAlert("danger","API has been deleted.");
    	 
    	 if(apiRequests.length == 0){
    		 $('#header-dlt-a').hide();
    	 }
    	 
    	 
    }).fail(function (jqXHR, textStatus, errorThrown) {
       
    	alert(jqXHR.responseJSON.message);
    
        console.log(errorThrown);
   	 	$('#api-delete-btn').show();
	 	$('#api-delete-spinner').hide();
    });
	
}

function deleteDiv(id){
	
	$('#'+id).parent().remove();
	apiRequests = _.without(apiRequests,_.findWhere(apiRequests,{
		id:id
	}))
	
	let firstApiUrl = _.first(apiRequests);
	if(firstApiUrl){
		currentApiSelected = $('#'+firstApiUrl.id);
		selectRow(currentApiSelected);
	} else {
		showApiCreateGuide();
	}
	
}
 
 
function showAlert(alertType,message){
	
	if("success" == alertType){
		$('.alert-corner p').text(message);
		$('#alert').removeClass('alert-danger');
		$('#alert').addClass('alert-success');
		
	} else if ("danger" == alertType){
		$('.alert-corner p').text(message);
		$('#alert').removeClass('alert-success');
		$('#alert').addClass('alert-danger');	
	}
	$('.alert-corner').show();
	
	setTimeout(function(){
	    $(".alert-corner").hide(); 
	  }, 2000);
}

function showApiCreateGuide(){
	
	$('.create-api-guide').show();
	$('.api-body').hide();
	$('.wlcm-card').show();
}

function hideApiCreateGuide(){
	$('.create-api-guide').hide();
	$('.wlcm-card').hide();
	 $('.api-body').show();
}
 
 



$(document).ready(function(){
    removeHeader();
    addHeader();
    
    //addJsonResponse();
    $('#api-save-spinner').hide();
    $('#api-delete-spinner').hide();
    $('#api-update-spinner').hide();
    $('.alert-corner').hide();
    
    $( "#createApiForm" ).validate( {
		rules: {
			URI: "required",
		},
		messages: {
			firstname: "Please enter URI.",
		},
		errorElement: "div",
		errorPlacement: function ( error, element ) {
			// Add the `help-block` class to the error element
			error.addClass( "invalid-feedback" );

			if ( element.prop( "type" ) === "checkbox" ) {
				error.insertAfter( element.parent( "label" ) );
			} else {
				error.insertAfter( element );
			}
		},
		highlight: function ( element, errorClass, validClass ) {
			$( element ).parents( ".mb-3" ).addClass( "has-error" ).removeClass( "has-success" );
		},
		unhighlight: function (element, errorClass, validClass) {
			$( element ).parents( ".mb-3" ).addClass( "has-success" ).removeClass( "has-error" );
		}
	} );
    
})
