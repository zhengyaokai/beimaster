;(function(win,$,pubFuc){
	var urls=pubFuc.urls();
	
	var soApi={
		"getAddress":urls+"/receive_address/default/",
		"addressUrl":urls+"/page/addressList.html",
		"invoiceList":urls+"/page/invoiceList.html",
		"getOneInvoice":urls+"/invoice/default/"
	};
	
	var soDom={
		"s_address":".J_address",
		"s_allAddress":".J_allAddress",
		"s_invoice":".J_invoice",
		"s_oneInvoice":".J_oneInvoice"
	};
	
	var soFunc=(function(){
		return{
			init:function(){
				this.buyerId=pubFuc.getStr("buyerId");
				this.getAddress();
				this.getAddressUrl();
				this.getOneInvoice();
				this.setDefault();				
			},
			
			setDefault:function(){
				$(soDom.s_invoice).prop("href",soApi.invoiceList+"?buyerId="+this.buyerId);
			},
			
			getAddress:function(){
				pubFuc.loads(soApi.getAddress+this.buyerId,null,"GET",function(results){
					var htmls;
					if(results.consignee){
						htmls='<span>收货人:'+results.consignee+' </span><span>'+results.mobile+'</span>'+
				        '<label>'+results.address+'</label>';
					}else{
						htmls='<span>您还未填写收获地址！</span><label>点击填写收获地址！</label>';
					}						
					$(soDom.s_address).html(htmls);
				},function(){
					alert("程序出错！");
				});
			},
			
			getOneInvoice:function(){
				pubFuc.loads(soApi.getOneInvoice+this.buyerId,null,"GET",function(results){
					var htmls;
					if(results.invTitle){
						htmls=results.invTitle+'<font></font>';
					}else{
						htmls='不开票<font></font>';
					}
					$(soDom.s_oneInvoice).html(htmls);
				},function(){
					alert("程序出错！");
				});
			},
			
			getAddressUrl:function(){
				var m=this;
				$(soDom.s_allAddress).bind("click",function(){
					location.href=soApi.addressUrl+"?buyerId="+m.buyerId;
				});
			}
		};
	})();
	
	$(function(){
		soFunc.init();
	});
})(this,jQuery,pubFuc||{});