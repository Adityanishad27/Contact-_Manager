console.log("this is script file")

const toggleSidebar=()=>{
	
	if($(".sidebar").is(":visible")){
		$(".sidebar").css("display","none");
		$(".content").css("margin-left", "0%")
		
	}
	else{
		
		
				$(".sidebar").css("display","block");
				$(".content").css("margin-left","20%")
		
	}
	
	}
	
	// for search feature
	/*const search =() => {
	// console. Log("searching ... ");
	let query = $("#search-input").val();
	if (query == "") {
	$(".search-result").hide();
	} else {
	//search
	console. log(query);
	$(".search-result").show();
	}
	}*/
	
	
	// Razorpay paymentGateway
	
	const paymentStart = () => {
	console.log("payment started .. ");

	

	var amount = $("#payment_field").val();
	
	console.log(amount);
	if (amount == "" || amount == null) {
	alert("amount is required !! ");
	return;

	//code ...
	// we will use ajax to send request to server to create order- jquery
	$.ajax({
		url:'user/create_order',
		data:JSON.stringify({amount:amount,info:'order_request'}),
		contentType:'application/json',
		type:'POST',
		dataType:'json',
		success:function(response){
			
		// invoked when success	
		console.log(response)
			
		},
		
		error:function(error){
			
		console.log(error)
		alert('something went wrong !!')
		}
		
		
		
		
	}
)
	
	};