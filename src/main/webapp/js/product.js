/*
 * limengmeng
 * 2016-8-31
 */
//;define(["public","plugin/jquery-moveimg-1.0","plugin/jquery-cookie-1.0"],function(pub){
;(function(win,$,pubFuc){
	var url=pubFuc.url(),
          urls=pubFuc.urls();
	
	var dom={
		  "J_attr":".J_attr",
		  "J_body":".J_body"
	};
	
	var api={
		  "productApi":url+"/goods/mall/detail/"
	};
	
	var product=(function(){
		return{
			init:function(){
				this.getProduct();
			},
			
			getProduct:function(){
				var id=pubFuc.getStr("sellerGoodsId"),
				      _this_=this;
				pubFuc.loads(api.productApi+id,null,"GET",function(s){
					if(s.code==0){
						/*attrs*/
						var p=s.data;
						if(p.attrs.length>0){
							var aHtml="";
							$.each(p.attrs,function(i,v){
								aHtml+=
									'<li class="ft14 color666">'+
						            '<span class="a">'+v.attrName+'</span><span class="b">'+v.attrValue+'</span>'+
						            '</li>';
							});
						}
						$(dom.J_attr).html(aHtml);
						/*body*/
						//p.detail='<p>1.wxt</p><p>2.wmt</p><img src=\"http://www.bmatser.com/goodsImg/0.jpg\"/>';
						if(p.detail&&p.detail!=""){
							p.detail=p.detail.replace(".jpg","!.jpg").replace(".png","!.png");								
						}else{
							p.detail='<img src="'+p.image+'">';
						}
						if(p.proxyCertificate){
							p.detail+='<img src="'+p.proxyCertificate+'">';
						}
						$(dom.J_body).html(p.detail);
						$.each($(dom.J_body).find("img"),function(){
							if($(this).attr("src").indexOf("/0!")>0){
								$(this).attr("src",p.image);
							}
						});
					}
				},function(e){
					console.log(e);
				});
			}
		};
	})();
	
	$(function(){
		product.init();
	});

})(this,jQuery,pubFuc||{});
