;(function(win,$,pubFuc){
	var urls=pubFuc.urls();
	
	var alApi={
		"getAddress":urls+"/receive_address/customer/",
		"selectedAddress":urls+"/receive_address/setdefault/",
		"addAddressApi":urls+"/page/addAddress.html",
		"editAddressApi":urls+"/page/editAddress.html"
	};
	
	var alDom={
		"s_addressAll":".J_addressAll",
		"s_checkOn":".J_checkOn",
		"s_addAddress":".J_addAddress",
		"s_saveValue":".J_saveValue"
	};
	
	var alFunc=(function(){
		return{
			init:function(){
				this.buyerId=pubFuc.getStr("buyerId");
				this.getAddress();
				this.setDefault();
			},
			
			saveValue:function(){
				$(alDom.s_saveValue).bind("click",function(){
					pubFuc.setValueCatch("id",$(this).attr("attrid"));
				});
			},
			
			setDefault:function(){
				$(alDom.s_addAddress).prop("href",alApi.addAddressApi+"?buyerId="+this.buyerId);
			},
			
			getAddress:function(){
				var m=this;
				pubFuc.loads(alApi.getAddress+this.buyerId,null,"GET",function(results){
					var htmls="<ul>";
					$.each(results,function(index,vals){
						htmls+=
							 '<li>'+
			                 '<div class="address-list-cont">'+
			                      '<span>'+vals.consignee+' '+vals.mobile+'</span>'+
			                      '<span>'+vals.address+'</span>'+
			                 '</div>'+
			                 '<a href="'+alApi.editAddressApi+'" class="J_saveValue" data-transition="slide" data-role="none" attrid="'+vals.id+'">'+
			                 '<div class="address-list-gth">'+			                 
			                 '</div>'+
			                 '</a>';
						if(vals.isDefault==1){							
							htmls+='<div class="address-list-gou on J_checkOn" attrid="'+vals.id+'"></div>';			                 	
						}else{
							htmls+='<div class="address-list-gou J_checkOn" attrid="'+vals.id+'"></div>';		
						}
						htmls+='</li>';
					});
					htmls+="</ul>";
					$(alDom.s_addressAll).html(htmls);	
					m.checkOn();
					m.saveValue();
				},function(){
					alert("程序出错！");
				});
			},
			
			checkOn:function(){
				var m=this;
				$(alDom.s_checkOn).bind("click",function(){					
					$(alDom.s_checkOn).removeClass("on");
					$(this).addClass("on");
					var adId=$(this).attr("attrid");
					pubFuc.loads(alApi.selectedAddress+adId+"/"+m.buyerId,null,"PUT",function(results){
						//console.log(results);
					},function(){
						alert("程序出错！");
					});
					
				});
			}
			

		};
	})();
	
	$(function(){
		alFunc.init();
	});
})(this,jQuery,pubFuc||{});