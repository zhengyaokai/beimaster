/*孙-8-27*/
;(function($){
	var opraCarNum=function(poster,posterNum,evePrice,ind,putAddr,inputs){
		var _carthis_=this;
		_carthis_.poster=poster;
		_carthis_.ind=ind;/*第几个数量，价格*/
		_carthis_.posterNum=posterNum;/*数量的dom*/
		_carthis_.inputs=inputs;/*checkbox*/
		_carthis_.numDivs=_carthis_.posterNum.eq(_carthis_.ind);/*当前数量的dom*/
		_carthis_.evePrice=evePrice;/*单价的dom*/	
		_carthis_.putAll=putAddr;/*存放总价dom*/
		_carthis_.clickOne=_carthis_.poster.children().eq(0);/*减号dom*/
		_carthis_.numBox=_carthis_.poster.children().eq(1);/*中间数量的dom*/
		_carthis_.clickTwo=_carthis_.poster.children().eq(2);/*加号的dom*/
		
		
		_carthis_.numValue=_carthis_.getNums();
		
		_carthis_.clickOne.on("tap",function(){
			_carthis_.minusNum();
		});
		
		_carthis_.clickTwo.on("tap",function(){
			_carthis_.addNum();
		});
		
		_carthis_.numBox.keyup(function(){
			_carthis_.changeNums();
		});
		
	};
	
	opraCarNum.prototype={
			
		minusNum:function(){		   
		   this.numValueNew=this.numValue-1;
		   var returnNumCheck=this.checkNum(this.numValueNew);
		   if(!returnNumCheck){
			   this.numBox.val(this.numValue);
			   this.numDivs.html(this.numValue);
		   }else{			   
			   this.numBox.val(this.numValueNew);
			   this.numDivs.html(this.numValueNew);
		   }
		   this.getAllPrice();
		   this.numValue=this.getNums();
		},
		
		addNum:function(){		   
		   this.numValue+=1;
		   this.numBox.val(this.numValue);
		   this.numDivs.html(this.numValue);
		   this.getAllPrice();
		   this.numValue=this.getNums();
		},
		
		changeNums:function(){
		   this.changeNumValue=this.getNums();
		   var returnNumCheck=this.checkNum(this.changeNumValue);
		   if(returnNumCheck){
			   this.numBox.val(this.changeNumValue);
			   this.numDivs.html(this.changeNumValue);
		   }else{
			   this.numBox.val(this.numValue);
			   this.numDivs.html(this.numValue);
		   }
		   this.getAllPrice();
		   this.numValue=this.getNums();
		},
		
		checkNum:function(values){
			if(values<=0){
				alert("数量不能少于1");
				return false;
			}
			if(isNaN(values)){
				alert("只能输入数字！");
				return false;
			}
			return true;
		},
		
		getNums:function(){
			var numCheck=this.numBox.val();
			if(isNaN(numCheck)){
				return numCheck;
			}else{
				return parseInt(numCheck);
			}
		},
		
		getAllPrice:function(){
			var _this_=this;
			var truePrice=0;
			$.each(_this_.evePrice,function(index,vals){
				if($(_this_.inputs[index]).prop("checked")){
					truePrice+=parseFloat($(vals).html())*parseFloat($(_this_.posterNum[index]).html());
				}				
			});
			_this_.putAll.html(truePrice.toFixed(2));
		}
	    
	};
	
	opraCarNum.init=function(posters,goodsNums,evePrice,putAddr,J_input){
		var _this_=this;
		posters.each(function(index,vals){
			new _this_($(this),goodsNums,evePrice,index,putAddr,J_input);
		});
	};
	
	window["opraCarNum"]=opraCarNum;
	
})(jQuery);