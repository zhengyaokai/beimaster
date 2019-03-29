/*
* 特卖专场页
* @date: 2017-12-7
* @author: limengmeng
*/
;(function(win,$,pubFuc,waterFall){
    var saleLitUrl={
    	"saleListApi":pubFuc.urls()+"/activity/sales/"
    };

    var dom={
    	"J_time":".J_time",
    	"J_title":".J_title",
        "J_saleList":".J_saleList",
        "J_orderby":".J_orderby",
        "J_orderDefBy":".J_orderDefBy",
        "J_orderPriceBy":".J_orderPriceBy",
        "J_orderRateBy":".J_orderRateBy",
        "J_over":".J_over"
    };
    
    var detail=(function(){
    	return{
    		init:function(){
    			var _this_=this;
    			this.isPriceOrder=false;/*false is close priceorder*/
    			this.isRateOrder=false;/*false is close rateorder*/
    			this.orderPrice=false;/*true is asc*/
    			this.orderRate=false;/*true is asc*/
    			this.page=0;
    			this.delay=true;
    			this.id=pubFuc.getStr("id");
    			this.setHtmls();
                this.getSaleList();/*取特卖专场数据*/
                this.orderBy();/*特卖排序*/
                $(window).scroll(function(){
                	if(_this_.delay){
                		window.setTimeout(function(){
                    		var b=waterFall.setWaterFall($(dom.J_saleList));
                        	if(b){
                        		_this_.page++;
                        		_this_.getSaleList();                        		
                        	}
                        	_this_.delay=true;
                    	},500);
                		_this_.delay=false;
                	}
                });               
    		},
    		
    		setHtmls:function(){
    			var htmls='<a href="javascript:;" class="J_orderDefBy"><span class="desc">默认</span></a>'+
			              '<a href="javascript:;" class="J_orderPriceBy"><span>价格</span></a>'+
			              '<a href="javascript:;" class="J_orderRateBy"><span>折扣</span></a>';
    			$(dom.J_orderby).html(htmls);
    		},
    		
    		orderBy:function(){ 
    			var _this_=this;
    			$(dom.J_orderDefBy).bind("click",function(){   
    				$(this).find("span").addClass("desc");
    				$(dom.J_orderRateBy).find("span").removeClass("desc asc");			
    				$(dom.J_orderPriceBy).find("span").removeClass("desc asc");
    				$(dom.J_saleList).html("");
    				_this_.page=0;
    				_this_.isPriceOrder=false;
    				_this_.isRateOrder=false;
    				_this_.orderPrice=false;
    				_this_.orderRate=false;
    				_this_.getSaleList();    				
    			}); /*default order*/   			
    			$(dom.J_orderPriceBy).bind("click",function(){   
    				$(dom.J_orderDefBy).find("span").removeClass("desc");
    				_this_.orderPrice=!_this_.orderPrice;
    				if(_this_.orderPrice){
    					$(this).find("span").removeClass("desc").addClass("asc");
    				}else{
    					$(this).find("span").removeClass("asc").addClass("desc");
    				}  				
    				$(dom.J_saleList).html("");
    				_this_.page=0;
    				_this_.isPriceOrder=true;
    				_this_.getSaleList();    				
    			});/*price order*/
    			
    			$(dom.J_orderRateBy).bind("click",function(){   
    				$(dom.J_orderDefBy).find("span").removeClass("desc");
    				_this_.orderRate=!_this_.orderRate;
    				if(_this_.orderRate){
    					$(this).find("span").removeClass("desc").addClass("asc");
    				}else{
    					$(this).find("span").removeClass("asc").addClass("desc");
    				}  				
    				$(dom.J_saleList).html("");
    				_this_.page=0;
    				_this_.isRateOrder=true;
    				_this_.getSaleList();    				
    			});/*rate order*/
    		},
    		
    		getSaleList:function(){
    			var _this_=this;
    			if(this.pageTotal&&this.page>this.pageTotal){
    				$(dom.J_over).show().html("已是最后一页");
    				return;
    			}
    			var urls=saleLitUrl.saleListApi+this.id+'/'+this.page+'/?pId='+Math.random(10);
    			if(this.isPriceOrder){
    				if(this.orderPrice){
    					urls+="&priceOrder=asc";
    				}else{
    					urls+="&priceOrder=desc";
    				}    				
    			}
    			if(this.isRateOrder){
    				if(this.orderRate){
    					urls+="&rateOrder=asc";
    				}else{
    					urls+="&rateOrder=desc";
    				}    				
    			}
    			pubFuc.loads(urls,null,"GET",function(c){
    				var saleListDate=c.rows,
    				    saleTitle=c.activityName,
    				    saleTime=c.overTime,
    				    htmls="";
    				_this_.pageTotal=Math.floor(c.total/10);
    			    $(dom.J_title).html(saleTitle);
    				setInterval(function(){
    					saleTime--;
    					var m="<span>剩余"+pubFuc.countDown(saleTime)+"</span>";
    					$(dom.J_time).html(m);
    				},1000);
    				$.each(saleListDate,function(i,v){
    					htmls+='<li>'+
					           '<div class="sale-list-img">'+
					           '<a href="'+pubFuc.urls()+'/page/detailStraight.jsp?sellerGoodsId='+v.sellerGoodsId+'" data-role="none" data-transition="slide" rel="external"><img src="'+v.image+'"></a>'+
					           '</div>'+
					           '<div class="sale-list-title">'+
					           '<a href="'+pubFuc.urls()+'/page/detailStraight.jsp?sellerGoodsId='+v.sellerGoodsId+'" data-role="none" data-transition="slide" class="a" rel="external">'+v.title+'</a>'+
					           '<a href="'+pubFuc.urls()+'/page/detailStraight.jsp?sellerGoodsId='+v.sellerGoodsId+'" data-role="none" data-transition="slide" class="b" rel="external">'+v.model+'</a>'+
					           '</div>'+
					           '<div class="sale-list-price">'+
					           '<label>￥'+v.salePrice+' </label>'+
					           '<span>'+v.rate+'折 </span>'+
					           '</div>'+
					           '</li>';
    				});
    				$(dom.J_saleList).append(htmls);
    			});
    		}
    	};
    })();

	$(function(){
		detail.init();
	});
	

})(this,jQuery,pubFuc||{},waterFall||{});


