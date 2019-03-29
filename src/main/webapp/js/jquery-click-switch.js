/*孙-9-1*/
;(function($){
var clickSwitch=function(poster){
	var self=this;
	self.poster=poster;
	self.clickOne=self.poster.find(".J_click");
	self.formBox=self.poster.find(".J_form");
	
	self.clickOne.on("tap",function(){
		//alert(self.clickOne.index(this));
		self.showHide(self.clickOne.index(this));
	});
};

clickSwitch.prototype={ 
    showHide:function(thisMs){
    	this.formBox.hide();
    	this.formBox.eq(thisMs).show();
    	this.clickOne.removeClass("on");
    	this.clickOne.eq(thisMs).addClass("on");
    }
};

clickSwitch.init=function(posters){
	var _this_=this;
	posters.each(function(){
		new _this_($(this));
	});	
};
window["clickSwitch"]=clickSwitch;
})(jQuery);
