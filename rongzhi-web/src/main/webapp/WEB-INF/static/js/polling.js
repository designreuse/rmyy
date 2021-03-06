function Poll(){
	var pollingUrl = ctx + '/polling';
	var longPolling = function(url, callback) {
	    $.ajax({
	    	url: url,
	        async: true,
	        cache: false,
	        global: false,
	        timeout: 30 * 1000,
	        dataType : "json",
	        success: function (data, status, request) {
	            callback(data);
	            data = null;
	            status = null;
	            request = null;
	            setTimeout(
	                function () {
	                    longPolling(url, callback);
	                },
	                10 * 1000
	            );
	        },
	        error: function (xmlHR, textStatus, errorThrown) {
	            xmlHR = null;
	            textStatus = null;
	            errorThrown = null;
	
	            setTimeout(
	                function () {
	                    longPolling(url, callback);
	                },
	                30 * 1000
	            );
	        }
	    });
	};
	longPolling(pollingUrl, function(data) {
	    if(data) {
	        if (data.isFlush){
	         	$.post(ctx + "/fcf/column3d", {}, function(result) {
	        	    myChart.setJSONData(result);      
	        		myChart.render("Column3D1");
	        	}); 
	        }
	    }
	});
}	