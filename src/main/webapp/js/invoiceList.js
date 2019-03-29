;(function(win,$,pubFuc){
	var urls=pubFuc.urls();
	
	var ilApi={
        "addInvUrl":urls+"/page/addInvoice.html",
        "getInvoiceList":urls+"/invoice/buyerid/",
        "setInvoiceType":urls+"/invoice/setdefault/",
	};
	
	var ilDom={
        "i_addInvUrl":".J_addInvUrl",
        "i_pupiao":".J_pupiao",
        "i_zpiao":".J_zpiao",
        "i_setDefault":".J_setDefault",
        "i_nopiao":".J_nopiao"
	};
	
	var ilFunc=(function(){
		return{
			init:function(){
				this.buyerId=pubFuc.getStr("buyerId");							
                this.setDefault();
                this.getInvoiceList();
                this.setDeInvoice();
			},
			
			setDefault:function(){
				$(ilDom.i_addInvUrl).prop("href",ilApi.addInvUrl+"?buyerId="+this.buyerId);
			},
			
			getInvoiceList:function(){
				var i=this;
				pubFuc.loads(ilApi.getInvoiceList+this.buyerId,null,"GET",function(results){
					var htmls_p="",
					    htmls_z="",
					    html_n=
					    	  '<li>'+            
				              '<input type="radio" name="invType" id="invType_0" data-role="none" class="display-none" value="0" checked="checked">'+
			                  '<label for="invType_0" class="pay J_setDefault">不开票<span></span></label>'+
			                  '</li>';
					$.each(results,function(index,vals){
						if(vals.invType==1){
					        htmls_p+='<li>';
					        if(vals.isDefault==1){
					            htmls_p+='<input type="radio" name="invType" id="invType_'+vals.id+'" data-role="none" class="display-none" value="'+vals.id+'" checked="checked">';
					        }else{
					        	htmls_p+='<input type="radio" name="invType" id="invType_'+vals.id+'" data-role="none" class="display-none" value="'+vals.id+'">';	
					        }
					        htmls_p+='<label for="invType_'+vals.id+'" class="pay J_setDefault">'+vals.invTitle+'<span></span></label>'+
				                     '</li>';
						}else if(vals.invType==2){
							htmls_z+='<li>';
					        if(vals.isDefault==1){
					            htmls_z+='<input type="radio" name="invType" id="invType_'+vals.id+'" data-role="none" class="display-none" value="'+vals.id+'" checked="checked">';
					        }else{
					        	htmls_z+='<input type="radio" name="invType" id="invType_'+vals.id+'" data-role="none" class="display-none" value="'+vals.id+'">';	
					        }
					        htmls_z+='<label for="invType_'+vals.id+'" class="pay J_setDefault">'+vals.invTitle+'<span></span></label>'+
				                     '</li>';	
						}
						
						if(vals.isDefault==1){
							html_n=
						    	  '<li>'+            
					              '<input type="radio" name="invType" id="invType_0" data-role="none" class="display-none" value="0">'+
				                  '<label for="invType_0" class="pay J_setDefault">不开票<span></span></label>'+
				                  '</li>';
						}
					});
					
					$(ilDom.i_pupiao).html(htmls_p);
					$(ilDom.i_zpiao).html(htmls_z);
					$(ilDom.i_nopiao).html(html_n);
					i.setDeInvoice();
				},function(){
					alert("程序出错！");
				});
			},
			
			setDeInvoice:function(){
				var m=this;
				$(ilDom.i_setDefault).bind("click",function(){
					var m_val=$(this).prev().val();
					pubFuc.loads(ilApi.setInvoiceType+m_val+"/"+m.buyerId,null,"PUT",function(results){
						console.log(results);
					},function(){
						alert("程序出错！");
					});
				});
			}
			
					
			

		};
	})();
	
	$(function(){
		ilFunc.init();
	});
})(this,jQuery,pubFuc||{});