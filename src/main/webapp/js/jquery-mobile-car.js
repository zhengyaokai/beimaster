/*孙-8-26*/
;(function($){
var carCounts=function(poster,posterm,posterx){
	var self=this;
	self.poster=poster;
	self.posterm=posterm;
	self.posterx=posterx;
	self.screenWidths=$(window).width();
	self.screenHeights=$(window).height();
	self.defaultValue();
	
	self.posterm.on("tap",function(){
		self.showTable();
	});
	
	self.posterx.on("tap",function(){
		self.closeTable();
	});
	
};

carCounts.prototype={		
   defaultValue:function(){
	 this.poster.css({
		 "top":"-100%"
	 }); 
   },
   
   showTable:function(){
	  this.poster.show();
	  this.poster.animate({
		 "top":"0px" 
	  });
   },
   
   closeTable:function(){
	   var seld=this;
	   seld.poster.animate({
		   "top":"-100%"
		  },function(){
			  seld.poster.hide();  
	   }); 
   }

};

carCounts.init=function(posters,posterms,posterxs){
	var _this_=this;
	//posters.each(function(){
		new _this_(posters,posterms,posterxs);
	//});	
};
window["carCounts"]=carCounts;
})(jQuery);
