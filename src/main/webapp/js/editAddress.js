;(function(win,$,pubFuc){
	var urls=pubFuc.urls();
	
	var eaApi={
        "detailAddress":urls+"/receive_address/id/",
        "editAddress":urls+"/receive_address/"
	};
	
	var eaDom={
		"s_save":".J_save",
	    "s_consignee" : ".J_consignee",
		"s_mobile" : ".J_mobile",
		"s_address" : ".J_address",
		"s_postCode" : ".J_postCode",
		"s_name" : "s_d",
		"s_province" : ".J_province",
		"s_city" : ".J_city",
		"s_area" : ".J_area"
	};
	
	var eaFunc=(function(){
		return{
			init:function(){
				this.buyerId=pubFuc.getStr("buyerId");
				this.id=pubFuc.getValueCatch("id");
				
				this.getProvince();
				
                this.getDetailAddress();
                
                this.saveAddress();
			},
			
			getDetailAddress:function(){
				var m=this;
				pubFuc.loads(eaApi.detailAddress+m.id,null,"GET",function(results){
					var htmls="";
                    $(eaDom.s_consignee).val(results.consignee);
                    $(eaDom.s_mobile).val(results.mobile);
                    $(eaDom.s_address).val(results.address);
                    $(eaDom.s_postCode).val(results.postCode);
                    
                    $(eaDom.s_province).val(results.provinceId);
                    m.getCity(results.provinceId);
                    $(eaDom.s_city).val(results.cityId);
                    m.getArea(results.cityId);
                    $(eaDom.s_area).val(results.areaId);
                    
                    if(results.isDefault==1){
                    	$('input[name='+eaDom.s_name+']').first().prop("checked","checked");                   
                    }else{
                    	$('input[name='+eaDom.s_name+']').last().prop("checked","checked");
                    }
                    
                    
				},function(){
					alert("程序出错！");
				});
			},
			
			getProvince:function(){
				var htmls='<option value="">请选择省份</option>';
				$.each(areas,function(index,vals){
					if(vals.pId==0){
						htmls+='<option value="'+vals.id+'">'+vals.name+'</option>';	
					}
				});
				$(eaDom.s_province).html(htmls);
			},
			
			getCity:function(proId){
				var htmls='<option value="0">请选择市</option>';
				$.each(areas,function(index,vals){
					if(vals.pId==proId){
						htmls+='<option value="'+vals.id+'">'+vals.name+'</option>';	
					}
				});
				$(eaDom.s_city).html(htmls);
			},
			
			getArea:function(cityId){
				var htmls='<option value="0">请选择地区</option>';
				$.each(areas,function(index,vals){
					if(vals.pId==cityId){
						htmls+='<option value="'+vals.id+'">'+vals.name+'</option>';	
					}
				});
				$(eaDom.s_area).html(htmls);
			},
			
			saveAddress:function(){				
				var m=this;
				$(eaDom.s_save).bind("click",function(){
				var n_buyerId=m.buyerId,
				    n_id=m.id,
				    n_countryId=100,
				    n_provinceId=$(eaDom.s_province).val(),
				    n_cityId=$(eaDom.s_city).val(),
				    n_areaId=$(eaDom.s_area).val(),
				    n_address=$(eaDom.s_address).val(),
				    n_postCode=$(eaDom.s_postCode).val(),
				    n_consignee=$(eaDom.s_consignee).val(),
				    n_mobile=$(eaDom.s_mobile).val(),
				    n_isDefault=$('input[name='+eaDom.s_name+']:checked').val();
					pubFuc.loads(eaApi.editAddress,{
						                          "id":n_id,
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
						                           },"PUT",function(results){
					if(results==1){
						alert("修改收获地址成功！");
					};
					},function(){
							alert("程序出错！");
					});
				});
			}

		};
	})();
	
	
	$(function(){
		eaFunc.init();
	});
})(this,jQuery,pubFuc||{});