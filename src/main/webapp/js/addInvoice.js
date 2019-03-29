;(function(win,$,pubFuc){
	var urls=pubFuc.urls();
	
	var aiApi={
        "saveInvoice":urls+"/invoice/"
	};
	
	var aiDom={
        "a_invType":".J_invType",
        "a_zPu":".J_zPu",
        "a_pPu":".J_pPu",
        "a_save":".J_save",
        "s_invTitle":".J_invTitle",
        "s_z_taxpayeRno":".J_z_taxpayeRno",
        "s_z_regAddress":".J_z_regAddress",
        "s_z_regTelphone":".J_z_regTelphone",
        "s_z_bank":".J_z_bank",
        "s_z_bankAccount":"J_z_bankAccount",
        "s_recvName":".J_recvName",
        "s_recvMobile":".J_recvMobile",
        "s_recvProvince":".J_recvProvince",
        "s_recvCity":".J_recvCity",
        "s_recvArea":".J_recvArea",
        "s_recvAddress":".J_recvAddress",
        "s_name":"s_d",
        
        "s_p_taxpayeRno":".J_p_taxpayeRno",
        "s_p_regAddress":".J_p_regAddress",
        "s_p_regTelphone":".J_p_regTelphone",
        "s_p_bank":".J_p_bank",
        "s_p_bankAccount":"J_p_bankAccount",
        
        "s_checkBox":".J_checkBox",
        "s_error":".J_error"
	};
	
    var aiText=new Array();
        aiText[0]="发票抬头",
        aiText[1]="纳税人识别号",
        aiText[2]="注册地址",
        aiText[3]="注册电话",
        aiText[4]="开户银行",
        aiText[5]="银行帐号",
        aiText[6]="收票人姓名",
        aiText[7]="收票人手机",
        aiText[8]="收票人省份",
        aiText[9]="收票人市",
        aiText[10]="收票人地区",
        aiText[11]="详细地址";
    
	var aiFunc=(function(){
		return{
			init:function(){
				this.checkTrue=true;
				this.buyerId=pubFuc.getStr("buyerId");
				this.domZhuan=$(aiDom.s_checkBox).find("ul").not(".J_pPu").find('input[type="text"],select');
				this.domZhuanText=$(aiDom.s_checkBox).find("ul").not(".J_pPu").find('input[type="text"]');
				this.domZhuanSelect=$(aiDom.s_checkBox).find("ul").not(".J_pPu").find('select');
				this.domPu=$(aiDom.s_checkBox).find("ul").not(".J_pPu,.J_zPu").find('input[type="text"],select');
				this.typeId=2;
				this.checkType();				
				
				this.getProvince();
				this.changeProvince();
				this.changeCity();
				
				this.onChangeEmpty();
				this.saveInvoice();
			},
			
			checkType:function(){
				var m=this;
				$(aiDom.a_invType).bind("click",function(){
					var invId;
					$(aiDom.a_invType).removeClass("on");
					$(this).addClass("on");					
					m.typeId=$(this).attr("attrType");
					if(m.typeId==2){
						$(aiDom.a_zPu).show();
						$(aiDom.a_pPu).hide();
					}else if(m.typeId==1){
						$(aiDom.a_zPu).hide();
						$(aiDom.a_pPu).show();
					}
				});
			},
			
			saveInvoice:function(){
				/*<div class="error_show">发票抬头不能为空</div>*/
				var s=this;
				$(aiDom.a_save).bind("click",function(){
					if(s.typeId==2){
						s.checkTrue=true;
						s.checkEmpty(s.domZhuan);
						if(s.checkTrue){
						var z_buyerId=s.buyerId,
						    z_invType=s.typeId;
						    z_invTitle=$(aiDom.s_invTitle).val();
						    z_taxpayeRno=$(aiDom.s_z_taxpayeRno).val();
						    z_regAddress=$(aiDom.s_z_regAddress).val();
						    z_bank=$(aiDom.s_z_bank).val();
						    z_regTelphone=$(aiDom.s_z_regTelphone).val();
						    z_bankAccount=$(aiDom.s_z_bankAccount).val();
						    z_recvName=$(aiDom.s_recvName).val();
						    z_recvMobile=$(aiDom.s_recvMobile).val();
						    z_recvProvince=$(aiDom.s_recvProvince).val(); 
						    z_recvCity=$(aiDom.s_recvCity).val();
						    z_recvArea=$(aiDom.s_recvArea).val();
						    z_recvAddress=$(aiDom.s_recvAddress).val();
						    z_isDefault=$('input[name='+aiDom.s_name+']:checked').val();
						    
						    pubFuc.loads(aiApi.saveInvoice,{
		                          "buyerId":z_buyerId,
		                          "invType":z_invType,
		                          "invTitle":z_invTitle,
		                          "taxpayeRno":z_taxpayeRno,
		                          "regAddress":z_regAddress,
		                          "regTelphone":z_regTelphone,
		                          "bank":z_bank,
		                          "bankAccount":z_bankAccount,
		                          "recvName":z_recvName,
		                          "recvMobile":z_recvMobile,
		                          "recvProvince":z_recvProvince,
		                          "recvCity":z_recvCity,
		                          "recvArea":z_recvArea,
		                          "recvAddress":z_recvAddress,
		                          "isDefault":z_isDefault,
		                           },"POST",function(results){
	                         if(results==1){
		                          alert("添加收货地址成功！");
	                            };
		                     },function(){
		   							alert("程序出错！");
		   					});
						}
					}else if(s.typeId==1){
						s.checkTrue=true;
						s.checkEmpty(s.domPu);
						if(s.checkTrue){
						var z_buyerId=s.buyerId,
					    z_invType=s.typeId;
					    z_invTitle=$(aiDom.s_invTitle).val();
					    z_taxpayeRno=$(aiDom.s_p_taxpayeRno).val();
					    z_regAddress=$(aiDom.s_p_regAddress).val();
					    z_bank=$(aiDom.s_p_bank).val();
					    z_regTelphone=$(aiDom.s_p_regTelphone).val();
					    z_bankAccount=$(aiDom.s_p_bankAccount).val();
					    z_recvName=$(aiDom.s_recvName).val();
					    z_recvMobile=$(aiDom.s_recvMobile).val();
					    z_recvProvince=$(aiDom.s_recvProvince).val(); 
					    z_recvCity=$(aiDom.s_recvCity).val();
					    z_recvArea=$(aiDom.s_recvArea).val();
					    z_recvAddress=$(aiDom.s_recvAddress).val();
					    z_isDefault=$('input[name='+aiDom.s_name+']:checked').val();
					    
					    pubFuc.loads(aiApi.saveInvoice,{
	                          "buyerId":z_buyerId,
	                          "invType":z_invType,
	                          "invTitle":z_invTitle,
	                          "taxpayeRno":z_taxpayeRno,
	                          "regAddress":z_regAddress,
	                          "regTelphone":z_regTelphone,
	                          "bank":z_bank,
	                          "bankAccount":z_bankAccount,
	                          "recvName":z_recvName,
	                          "recvMobile":z_recvMobile,
	                          "recvProvince":z_recvProvince,
	                          "recvCity":z_recvCity,
	                          "recvArea":z_recvArea,
	                          "recvAddress":z_recvAddress,
	                          "isDefault":z_isDefault,
	                           },"POST",function(results){
                         if(results==1){
	                          alert("添加收货地址成功！");
                            };
	                     },function(){
	   							alert("程序出错！");
	   					});
					  }
					}
				});
			},
			
			onChangeEmpty:function(){
				var t=this;
				
				t.domZhuanText.bind("keyup",function(){					
					if($(this).val()!=""){
						$(this).parent().siblings(aiDom.s_error).html("");
					}else{
						$(this).parent().siblings(aiDom.s_error).html(aiText[$(this).index()]+'不能为空');
					}
				});
				
				t.domZhuanSelect.bind("change",function(){
					if($(this).val()!=""){
						$(this).siblings(aiDom.s_error).html("");
					}else{
						$(this).siblings(aiDom.s_error).html(aiText[$(this).index()]+'不能为空');
					}
				});
				
				
			},
			
			checkEmpty:function(values){
				var s=this;
			    $.each(values,function(index,vals){			    	
			    	if($(this).val()==""){
			    		$(aiDom.s_error).eq(index).html(aiText[index]+'不能为空');
			    		s.checkTrue=false;
			    		return false;						    		
			    	}
			    });
			},
			
			getProvince:function(){
				var htmls='<option value="">请选择省份</option>';
				$.each(areas,function(index,vals){
					if(vals.pId==0){
						htmls+='<option value="'+vals.id+'">'+vals.name+'</option>';	
					}
				});
				$(aiDom.s_recvProvince).html(htmls);
			},
			
			changeProvince:function(){
				var m=this;
				var htmlx='<option value="">请选择地区</option>';
				$(aiDom.s_recvProvince).bind("change",function(){
					var provinceId=$(this).val();
					m.getCity(provinceId);					
					$(aiDom.s_recvArea).html(htmlx);
				});
			},
			
			getCity:function(proId){
				var htmls='<option value="">请选择市</option>';
				$.each(areas,function(index,vals){
					if(vals.pId==proId){
						htmls+='<option value="'+vals.id+'">'+vals.name+'</option>';	
					}
				});
				$(aiDom.s_recvCity).html(htmls);
			},
			
			changeCity:function(){
				var m=this;
				$(aiDom.s_recvCity).bind("change",function(){
					var cityId=$(this).val();
					m.getArea(cityId);
				});
			},
			
			getArea:function(cityId){
				var htmls='<option value="">请选择地区</option>';
				$.each(areas,function(index,vals){
					if(vals.pId==cityId){
						htmls+='<option value="'+vals.id+'">'+vals.name+'</option>';	
					}
				});
				$(aiDom.s_recvArea).html(htmls);
			}

		};
	})();
	
	$(function(){
		aiFunc.init();
	});
})(this,jQuery,pubFuc||{});