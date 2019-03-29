/*孙-12-7*/
;(function(win,$,undefined){	
	var waterFall=(function(){
		return{
			setWaterFall:function(dom){
				var offsetTop=dom.offset().top,
				    divHeight=dom.height(),
				    oneDivHeight=Math.floor(dom.find("li").height()/2),
				    scrollTop=$(document).scrollTop(),
				    screenHeight=$(window).height(),
				    waterFallHeight;
				waterFallHeight=offsetTop+divHeight-oneDivHeight-screenHeight;
                if(scrollTop>=waterFallHeight){
                	return true;
                }else{
                	return false;
                }
			}      
		};
	})();

	win.waterFall=waterFall;
})(this,jQuery,{});