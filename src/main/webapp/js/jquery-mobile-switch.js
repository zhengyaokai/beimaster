/*孙-8-25*/
;(function($){
var mobileSwitch=function(poster){
	var self=this;
	self.poster=poster;
	self.divOne=self.poster.children().eq(0);
	self.divTwo=self.poster.children().eq(1);
	self.divTwoTop=self.divTwo.children().eq(0);
	self.screenWidth=$(window).width();
	self.SwitchWidth=0;
	self.setting={
            "allNum":2
    };
	$.extend(self.setting,self.settings());
	self.defaultSwitch();
	//console.log(self.setting);
//	alert(self.divOne.html());
//    alert(poster);
	$(self.divTwo).on("swipeleft",function(){
		self.swipeLeft();
		self.switchScreen();
		self.addClassModel();
		//alert(self.SwitchWidth);
	});
	
	$(self.divTwo).on("swiperight",function(){
		self.swipeRight();
		self.switchScreen();
		self.addClassModel();
		//alert(self.SwitchWidth);
	});
	
	$(self.divOne.children()).on("tap",function(){
		self.SwitchWidth=-$(this).index();
		self.switchScreen();
		self.addClassModel();
	});
};

mobileSwitch.prototype={		
   defaultSwitch:function(){
	   //console.log(this.divTwo.children());
	  var defaultValue=this;
	  var disWidth;
	  
//	  alert(defaultValue.divTwoTop.height());
//	  defaultValue.divTwo.css({
//          "height":defaultValue.divTwoTop.children().eq(0).height()
//      });
	  defaultValue.divTwoTop.css({
                              "width":defaultValue.screenWidth*defaultValue.setting.allNum+"px"                              
         });
//	  alert(defaultValue.divTwoTop.children().eq(0).height());
	  defaultValue.divTwo.css({
	                           "height":defaultValue.divTwoTop.children().eq(0).height()
	  });
	  defaultValue.divTwoTop.children().each(function(elem,values){
		  $(values).css({
			        "width":defaultValue.screenWidth+"px"
		  });		  
		//console.log($(values).html());
	  });
	 // alert(defaultValue.divTwoTop.children().eq(0).html());
   },/*进入时默认值时使用*/
   
   switchScreen:function(){
	  this.divTwoTop.animate({
		             "left":this.SwitchWidth*this.screenWidth+"px"
	  });
	  this.divTwo.css({
		  "height":this.divTwoTop.children().eq(Math.abs(this.SwitchWidth)).height()
	  });
   },/*滑动时动画*/

   settings:function(){
	   var settingVale=this.poster.attr("data-setting");
	   if(settingVale&&settingVale!=""){
		   return $.parseJSON(settingVale);
	   }else{
		   return {};
	   }
   },/*赋值时使用*/
   
   swipeLeft:function(){
	   this.SwitchWidth-=1;
		if(this.SwitchWidth<=-this.setting.allNum){
			this.SwitchWidth=0;
		}
   },/*左滑动时使用*/
   
   swipeRight:function(){
	   this.SwitchWidth+=1;
		if(this.SwitchWidth>0){
			this.SwitchWidth=-this.setting.allNum+1;
		}
   },/*右滑动时使用*/
   
   addClassModel:function(){
	   var classNum=Math.abs(this.SwitchWidth);
	   $(this.divOne.children()).removeClass("on");
	   $(this.divOne.children()).eq(classNum).addClass("on");
   }/*on赋值*/

};

mobileSwitch.init=function(posters,postDiv){
	var _this_=this;
	posters.each(function(){
		new _this_($(this));
	});	
};
window["mobileSwitch"]=mobileSwitch;
})(jQuery);
