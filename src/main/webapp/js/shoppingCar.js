;(function(win,$,pubFuc){
	var urls=pubFuc.urls();
	var shopCarUrl={
		"getGoodsUrl":urls+"/cart/buyer/",
		"submitOrderApi":urls+"/page/submitOrder.html"
	};
	
	var domClass={
		"goods_div":".J_all",
		"top_div":".J_donums",
		"all_div":".J_selectAll",
		"one_div":".J_selects",
		"num_div":".J_nums",
		"evePr_div":".J_evePrice",
		"all_price":".J_allPr",
		"s_pay":".J_pay",
		"s_checkbox":".J_checkbox"
	};
	
	var doAll=(function(){
		return{
			init:function(){
				this.buyerId=pubFuc.getStr("buyerId");
				this.getAllGoods();	
				this.submitOrder();
			},
			
			getAllGoods:function(){
				var getThis=this;
				
				pubFuc.loads(shopCarUrl.getGoodsUrl+this.buyerId,null,"GET",function(results){
					results=results.data;
					getThis.fillGoods(results);
					opraCarNum.init($(domClass.top_div),$(domClass.num_div),$(domClass.evePr_div),$(domClass.all_price),$(domClass.s_checkbox));
					selectGoods.init($(domClass.all_div),$(domClass.one_div),$(domClass.num_div),$(domClass.evePr_div),$(domClass.all_price));
				});
			},
			
			fillGoods:function(values){
				var allPrice=0,
				    allHtmls="";
				if(values==""){
					    allHtmls+=
					    	'<div class="shopping-car-empty fir-mar">'+
					    	'<div class="shopping-car-null"><img src="../image/nocar.png"></div>'+
					    	'<div class="shopping-car-text">购物车还是空的，赶紧去逛逛吧!</div>'+
					    	'<div class="shopping-car-te"><a href="" data-role="none" class="te-b">促销特卖场</a></div>'+
					    	'</div>';
				}else{
					    allHtmls=
					    	'<div class="shopping-car-top fir-mar J_selects">'+
			                '<dl class="J_selectOneAll">'+
	                        '<dt class="width-96 left">'+
	                        '<input type="checkbox" id="selectOneAll-0" data-role="none" class="display-none" value="1">'+
	                        '<label for="selectOneAll-0" class="globle-18 left"></label>'+	
		                    '<span>工电宝信息技术有限公司</span>'+
		                    '</dt>';
					$.each(values,function(index,vals){
		            	allPrice+=vals.num*vals.price;
		            	allHtmls+='<dd class="width-96 left">'+
		                '<div class="shopping-car-box left mar-top-40">'+
		                '<input type="checkbox" class="display-none J_checkbox" data-role="none" id="carCheck-'+index+'" value="'+vals.goodsId+'">'+
		                '<label class="globle-18 left" for="carCheck-'+index+'"></label>'+
		                '</div>'+
		                '<img src="'+vals.image+'">'+
		                '<div class="shopping-car-title">'+
		                '<a href="" data-role="none" class="shopping-car-title-a">'+vals.goodsName+'</a>'+
		                '<a href="" data-role="none" class="shopping-car-title-b">'+vals.model+'</a>'+
		                '<a href="" data-role="none" class="shopping-car-title-c">'+vals.buyNo+'</a>'+
		                '<div class="do-nums J_donums">'+
		                '<label>－</label>'+
		                '<input type="text" data-role="none" value="'+vals.num+'">'+
		                '<label>+</label>'+
		                '</div>'+                
		                '</div>'+
		                '<div class="shopping-car-price">'+
		                '<span>¥<font class="J_evePrice">'+vals.price+'</font></span>'+
		                '<span class="m">x <font class="J_nums">'+vals.num+'</font></span>'+
		                '</div>'+
		                '</dd>';
		            });					
				}
				allHtmls+=
	                '</dl>'+             
	                '<div class="clear"></div>'+
	                '</div>';
				$(domClass.goods_div).html(allHtmls);
				$(domClass.all_price).html(pubFuc.toFixed(allPrice));
			},
			
			submitOrder:function(){
				var t=this;
				var goodsId=new Array();
				$(domClass.s_pay).bind("click",function(){
					$.each($(domClass.s_checkbox),function(index,vals){
						if($(this).prop("checked")==true){
							goodsId.push($(this).val());
						}
					});
				var goodsIds=goodsId.join(",");
				location.href=shopCarUrl.submitOrderApi+"?goodsId="+goodsIds+"&buyerId="+t.buyerId;
				});
			}
			
		};
	})();
	
	$(function(){
		doAll.init();
	});
})(this,jQuery,pubFuc || {});