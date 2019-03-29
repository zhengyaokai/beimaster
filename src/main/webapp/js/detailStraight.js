/*
* 详细页
* @date: 2017-12-8
* @author: limengmeng
*/
;(function(win,$,pubFuc){
    var detailUrl={
    	"contentApi":pubFuc.urls()+"/goods/detail/",
    	"addCarApi":pubFuc.urls()+"/cart/"
    };
    
    var dom={
    	"d_img":".J_img",
    	"d_title":".J_title",
    	"d_model":".J_model",
    	"d_brandName":".J_brandName",
    	"d_stockm":".J_stockm",
    	"d_measurem":".J_measurem",
    	"d_attrs":".J_attrs",
    	"d_bigImage":".J_bigImage",
    	"d_proxyCertificate":".J_proxyCertificate",
    	"d_foot":".J_footer",
    	"d_stock":".J_stock",
    	"d_donum":".J_donum",
    	"d_submit":".J_submit"
    };
    
    var detail=(function(){
    	return{
    		init:function(){
    			var selfThis=this;
    			selfThis.minus=$(dom.d_donum).children().eq(0),
    			selfThis.change=$(dom.d_donum).children().eq(1),
    			selfThis.add=$(dom.d_donum).children().eq(2);
    			selfThis.buyNum=parseInt(this.change.val());
    			selfThis.sellerGoodsId="";
    			//selfThis.buyerId=pubFuc.getStr("buyerId");
    			selfThis.buyerId=940139;
    			selfThis.addCars($(dom.d_foot).find("a").eq(1));
    			selfThis.sellerGoodsId=pubFuc.getStr("sellerGoodsId");
    			
    			pubFuc.loads(detailUrl.contentApi+selfThis.sellerGoodsId, null,"GET", function(results) {
    				results=results.data;
    				selfThis.addImage(results.image,$(dom.d_img));
    				selfThis.addPub(results.title,$(dom.d_title));
    				selfThis.addModel(results.model,$(dom.d_model));
    				selfThis.addBrandName(results.brandName,$(dom.d_brandName));
    				selfThis.addStock(results.stock,$(dom.d_stockm));
    				selfThis.addMeasure(results.measure,$(dom.d_measurem));
    				selfThis.addAttrs(results.attrs,$(dom.d_attrs));
    				selfThis.addImage(results.image,$(dom.d_bigImage));
    				selfThis.addImage(results.proxyCertificate,$(dom.d_proxyCertificate));
    				selfThis.addTel(results.telephone,$(dom.d_foot).find("a").first());
    				selfThis.addSom(results.stock,$(dom.d_stock),results.measure); 
    				//selfThis.sellerGoodsId=results.sellerGoodsId;
        		});
    			
    			selfThis.minus.bind("click",function(){
    				selfThis.minusNum();    				
    			});
    			
    			selfThis.change.bind("keyup",function(){
    				selfThis.changeNum();    				
    			});
    			
    			selfThis.add.bind("click",function(){
    				selfThis.addNum();    				
    			});
    			
    			$(dom.d_submit).bind("click",function(){
    				selfThis.saveNum(selfThis.sellerGoodsId); 
    			});
    			
    		},
    		
    		minusNum:function(){    			  
    			  if(this.buyNum==1){
    				 alert("最小购买一件！");    					
    			  }else{
    				  this.buyNum-=1;
    				  this.change.val(this.buyNum); 
    			  }    			    			
    		},
    		
    		addNum:function(){
  				this.buyNum+=1;
  			    this.change.val(this.buyNum);    
    		},
    		
    		changeNum:function(){
    			var changeNum=this.change.val();
    			if(isNaN(changeNum)){
    				//alert("只能输入数字！");
    				this.change.val(this.buyNum);
    			}    			
    		},
    		
    		addCars:function(boxDom){   
    			boxDom.prop("href",pubFuc.urls()+"/page/shoppingCar.jsp?buyerId="+this.buyerId)
    			      .prop("rel","external");
    		},
    		
    		addImage:function(values,boxDom){   
    			boxDom.html('<img src="'+values+'">');
    		},
    		
    		addPub:function(values,boxDom){
    			boxDom.html(values);
    		},
    		
    		addModel:function(values,boxDom){
    			boxDom.html("型号："+values);
    		},
    		
    		addBrandName:function(values,boxDom){
    			boxDom.html("品牌："+values);
    		},
    		
    		addStock:function(values,boxDom){
    			boxDom.html("库存："+values);
    		},
    		
    		addTel:function(values,boxDom){
    			boxDom.prop("href","tel:"+values);
    		},
    		
    		addMeasure:function(values,boxDom){
    			if(values==0){
    				values="";
    			}
    			boxDom.html(values);
    		},
    		
    		addSom:function(values,boxDom,valuem){
    			if(values==0){
    				values="";
    			}
    			boxDom.html("库存 "+values+" "+valuem);
    		},
    		
    		addAttrs:function(values,boxDom){
    			var htmls="";
    			$.each(values,function(index,vals){
    				htmls+='<p>'+vals.attrName+'：'+vals.attrValue+'</p>';
    			});
    			boxDom.html(htmls);
    		},
    		
    		saveNum:function(values){
    			pubFuc.loads(detailUrl.addCarApi, {"buyerId":this.buyerId,"num":this.buyNum,"sellerGoodsId":this.sellerGoodsId},"POST", function(results) {
    				if(results==1){
    					alert("添加成功！");
    				};
    			},function(results){
    				alert("保存出错！");
    			});
    		}
    		
    	};
    })();

	$(function(){
		detail.init();
	});
	

})(this,jQuery,pubFuc||{});


