/*
* 特权等级
* @date: 2016-8-8
* @author: limengmeng
*/
;(function(win,$,pubFuc){
	var url=pubFuc.url(),
          urls=pubFuc.urls();
	
    var api={
          checkRank:url+"/myinfo/app/dealerRank"
    };
    
    var dom={
          "J_rank":".J_rank"
    };
    
    var rank=(function(){
    	return{
    		init:function(){
                this.checkRank();
    		},
    		
    		checkRank:function(){
    			var id=pubFuc.getStr("id"),
    			      data={};
    			data.dealerId=id;    	
    			var rankArray=[[0],[0,1,2,3],[0,1,2,3],[0,1,2,3,4],[0,1,2,3,4]];
    			pubFuc.loads(api.checkRank,data,"GET",function(s){
    				if(s.code==0){
    					$.each(rankArray[s.data.rank-1],function(i,v){
    						$(dom.J_rank).children().eq(v).find("img").attr("src",url+"/image/privilege/my_y_0"+(v+1)+".png");
    					});
    				}
    			},function(e){
    				console.log(e);
    			});
    		}
    	};
    })();

	$(function(){
		rank.init();
	});
	

})(this,jQuery,pubFuc||{});


