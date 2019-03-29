/*孙-9-7*/
;(function($){
	var allOrder=function(poster){
	var self=this;
	self.poster=poster;
	self.delButton=self.poster.find(".J-allorder-del");/*删除时dom*/
	self.processButton=self.poster.find(".J-allorder-process");/*处理订单dom*/
	self.delButton.on("tap",function(){
		self.deleteOrder();
	});
	self.processButton.on("tap",function(){
		self.processOrder();
	});
//alert(poster);
	};
	
	allOrder.prototype={
		deleteOrder:function(){
			if(confirm("您确定要删除此订单？")){
				alert("处理删除订单");
			}
		},
		
		processOrder:function(){
			alert("付款订单");
		}
	    
	};
	
	allOrder.init=function(posters){
		var self=this;
		posters.each(function(){
			new self($(this));
		});
		//new this;		
	};
	
	window["allOrder"]=allOrder;
	
})(jQuery);