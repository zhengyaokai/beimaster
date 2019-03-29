;(function(win,$,pubFuc){
	var urls=pubFuc.urls();
	
	var aaApi={
		"saveAddress":urls+"/receive_address/"
	};
	
	var aaDom={
		"s_save":".J_save",
        "s_consignee":".J_consignee",
        "s_mobile":".J_mobile",
        "s_address":".J_address",
        "s_postCode":".J_postCode",
        "s_name":"s_d",
        "s_province":".J_province",
        "s_city":".J_city",
        "s_area":".J_area"
	};
	
	var aaFunc=(function(){
		return{
			init:function(){
				this.buyerId=pubFuc.getStr("buyerId");							
				this.getProvince();
				this.changeProvince();
				this.changeCity();
				this.saveAddress();	
			},
			
			saveAddress:function(){
				var m=this;
				$(aaDom.s_save).bind("click",function(){					
				var n_buyerId=m.buyerId;
				    n_countryId=100;
				    n_provinceId=$(aaDom.s_province).val();
				    n_cityId=$(aaDom.s_city).val();
				    n_areaId=$(aaDom.s_area).val();
				    n_address=$(aaDom.s_address).val();
				    n_postCode=$(aaDom.s_postCode).val();
				    n_consignee=$(aaDom.s_consignee).val();
				    n_mobile=$(aaDom.s_mobile).val();
				    n_isDefault=$('input[name='+aaDom.s_name+']:checked').val();
					pubFuc.loads(aaApi.saveAddress,{
						                          "buyerId":n_buyerId,
						                          "countryId":n_countryId,
						                          "provinceId":n_provinceId,
						                          "cityId":n_cityId,
						                          "areaId":n_areaId,
						                          "address":n_address,
						                          "postCode":n_postCode,
						                          "consignee":n_consignee,
						                          "mobile":n_mobile,
						                          "isDefault":n_isDefault
						                           },"POST",function(results){
					if(results==1){
						alert("添加收获地址成功！");
					};
					},function(){
							alert("程序出错！");
					});
				});
			},
			
			getProvince:function(){
				var htmls='<option value="0">请选择省份</option>';
				$.each(areas,function(index,vals){
					if(vals.pId==0){
						htmls+='<option value="'+vals.id+'">'+vals.name+'</option>';	
					}
				});
				$(aaDom.s_province).html(htmls);
			},
			
			changeProvince:function(){
				var m=this;
				var htmlx='<option value="0">请选择地区</option>';
				$(aaDom.s_province).bind("change",function(){
					var provinceId=$(this).val();
					m.getCity(provinceId);					
					$(aaDom.s_area).html(htmlx);
				});
			},
			
			getCity:function(proId){
				var htmls='<option value="0">请选择市</option>';
				$.each(areas,function(index,vals){
					if(vals.pId==proId){
						htmls+='<option value="'+vals.id+'">'+vals.name+'</option>';	
					}
				});
				$(aaDom.s_city).html(htmls);
			},
			
			changeCity:function(){
				var m=this;
				$(aaDom.s_city).bind("change",function(){
					var cityId=$(this).val();
					m.getArea(cityId);
				});
			},
			
			getArea:function(cityId){
				var htmls='<option value="0">请选择地区</option>';
				$.each(areas,function(index,vals){
					if(vals.pId==cityId){
						htmls+='<option value="'+vals.id+'">'+vals.name+'</option>';	
					}
				});
				$(aaDom.s_area).html(htmls);
			}
			

		};
	})();
	
	$(function(){
		aaFunc.init();
	});
})(this,jQuery,pubFuc||{});