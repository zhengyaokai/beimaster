/*孙-12-4*/
;(function($){
var flaseSwitch=function(poster){
    var _this_=this;
        _this_.poster=poster;
        _this_.screenWidth=$(window).width();
        _this_.settingVa={
            auto:true,	
        	num:1,
        	setTime:5000,
        	goalWidth:15,
        	goalHeight:15,
        	firstBack:"#ff0000",
            background:"#000",
            margin:10,
            marginRight:20,
            poBottom:50
        };
        $.extend(_this_.settingVa,_this_.setting());
        _this_.setHtml();
        _this_.setValue();
        _this_.index=0;
        _this_.flashBox.on("swipeleft",function(){
        	_this_.leftSwipe();
        });
        _this_.flashBox.on("swiperight",function(){
        	_this_.rightSwipe();
        });
        _this_.autoPlay();
};

flaseSwitch.prototype={ 
    setting:function(){
    	var settingVa=this.poster.attr("data-config");
    	if(settingVa&&settingVa!=""){
    		settingVa=$.parseJSON(settingVa);
    	}else{
    		settingVa={};
    	}
    	return settingVa;
    },
    
    setHtml:function(){
    	var _this_=this;
    	this.poster.html('<div class="J_goal">22222</div><ul></ul>');
    	var htmls="",
	    htmls2="";
	    for(var m=0;m<this.settingVa.num;m++){
	    	if(m==0){
	    		htmls+='<span class="on"></span>';
	    	}else{
	    		htmls+='<span></span>';
	    	}	    	
		    htmls2+='<li>'+
		            '<a><img src="'+this.settingVa.imgUrl[m]+'"></a>'+
			        '</li>';
	    }
	    this.flashBox=this.poster.find("ul");
	    this.flashGoal=this.poster.find(".J_goal");
	    this.flashGoal.html(htmls);
	    this.flashBox.html(htmls2); 
    },
    
    setGoalBackground:function(){
    	this.flashGoal.find("span").removeClass('on');
    	this.flashGoal.find("span").eq(this.index).addClass('on');
    	this.flashGoal.find("span").css({
			background:this.settingVa.background
		});
    	this.flashGoal.find("span.on").css({
			background:this.settingVa.firstBack
		});
    },
    
    setValue:function(){
    	var img = new Image;
    	var imgWidth=this.screenWidth,
    	    imgHeight="";
        var _this_=this;
    	img.onload = function(){       
    		imgHeight=img.height*imgWidth/img.width;    		
    		
    		_this_.poster.css({
        		height:imgHeight+"px"
        	});
    		
    		_this_.flashBox.find("img").css({
        		width:imgWidth+"px",
        	});
        	
    		_this_.flashBox.css({
        		width:_this_.screenWidth*_this_.settingVa.num+"px",
        		height:_this_.flashBox.find("img").eq(0).height()+"px",
        		position:"absolute",
        		left:"0px",
        	    top:"0px"
        	});
    		_this_.flashBox.find("li").css({
        		width:_this_.screenWidth+"px",
        		height:_this_.flashBox.find("img").eq(0).height()+"px",
        		float:"left",
        	});    	
    		_this_.poster.find(".J_goal span").css({
        		width:_this_.settingVa.goalWidth+"px",
        		height:_this_.settingVa.goalHeight+"px",
        		background:_this_.settingVa.background,
        		marginRight:_this_.settingVa.marginRight+"px",
        		float:"left",
        		borderRadius:"100%"
        	});
    		_this_.poster.find(".J_goal span.on").css({
    			background:_this_.settingVa.firstBack
    		});
    		_this_.poster.find(".J_goal").css({
        		width:(_this_.settingVa.goalWidth+_this_.settingVa.marginRight)*_this_.settingVa.num+"px",
        		position:"absolute",
        		left:"0px",
        		zIndex:"10",
        		left:"50%",
        		marginLeft:-((_this_.settingVa.goalWidth+_this_.settingVa.marginRight)/2)+"px",
        		bottom:_this_.settingVa.poBottom+"px"
        	});
    	}; 
    	img.src = this.settingVa.imgUrl[0];
    },
    
    leftSwipe:function(){
    	clearInterval(this.autoValue);
    	var _this_=this;
    	this.index++;
    	if(this.index>=this.settingVa.num){
    		this.index=0;
    	}
    	this.setGoalBackground();
    	var leftDis=-this.screenWidth*this.index+"px";
    	this.flashBox.animate({
    		left:leftDis
    	},function(){
    		_this_.autoPlay();
    	});
    },
    
    rightSwipe:function(){
    	clearInterval(this.autoValue);
    	var _this_=this;
    	this.index--;
    	if(this.index<0){
    		this.index=this.settingVa.num-1;
    	}
    	this.setGoalBackground();
    	var leftDis=-this.screenWidth*this.index+"px";
    	this.flashBox.animate({
    		left:leftDis
    	},function(){
    		_this_.autoPlay();
    	});
    },
    
    autoPlay:function(){
    	var _this_=this;
    	if(this.settingVa.auto){
    		this.autoValue=setInterval(function(){
    			_this_.leftSwipe();
    		},this.settingVa.setTime);
    	};
    }
};

flaseSwitch.init=function(posters){
	var _this_=this;
	posters.each(function(){
		new _this_($(this));
	});	
};
window["flaseSwitch"]=flaseSwitch;
})(jQuery);
