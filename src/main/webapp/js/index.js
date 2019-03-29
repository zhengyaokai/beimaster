/*
* 首页
* @date: 2017-12-7
* @author: limengmeng
*/
;(function(win,$,pubFuc){
    var indexUrl={
    	"nextSaleApi":pubFuc.urls()+"/activity/notice/",
    	"todaySaleApi":pubFuc.urls()+"/activity/",
    };
    
    var dom={
        "J_date":".J_date",
        "J_saleList":".J_saleList",
        "J_todaySale":".J_todaySale"
    };
    
    var detail=(function(){
    	return{
    		init:function(){
    			this.getNextSale();/*取下期预告*/
    			this.getTodaySale();/*今日特卖*/
    		},
    		
    		getTodaySale:function(){
    			pubFuc.loads(indexUrl.todaySaleApi,null,"GET", function(c){
    				var htmls="";
    				$.each(c,function(i,v){
    					htmls+='<li><a href="'+pubFuc.urls()+'/page/saleList.jsp?id='+v.id+'" data-role="none" rel="external">'+
    						   '<img src="'+v.bannerImage+'">'+
						       '</a>'+
							   '<div class="today-seller-cont">'+
							   '<div class="sale-l">'+
							   '<span>'+v.rate+'</span>折起'+
							   '</div>'+
							   '<div class="sale-c">'+v.brief+'</div>'+
							   '<div class="sale-r">剩余10天</div>'+
							   '</div></li>';
    				});
    				$(dom.J_todaySale).html(htmls);
    			});
    		},
    		
    		getNextSale:function(){
    			pubFuc.loads(indexUrl.nextSaleApi,null,"GET", function(c){
    				var l=c.length-1,
    				    saleCont=c[l],
    				    htmls="";
    				$(dom.J_date).html("/ "+saleCont.startDate+" 至 "+saleCont.endDate);
    				    $.each(saleCont.brands,function(i,v){
    				    	htmls+='<li>'+
						           '<a href="" data-role="none"><img src="'+v.brandImage+'"></a>'+
						           '</li>';
    				    });
    				$(dom.J_saleList).html(htmls);
    			});
    		}
    	};
    })();

	$(function(){
		detail.init();
	});
	

})(this,jQuery,pubFuc||{});


