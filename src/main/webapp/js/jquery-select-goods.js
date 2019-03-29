/*孙-8-28*/
;(function($){
	var selectGoods=function(posterm,poster,posterNum,evePrice,putAddr){
		var _goodthis_=this;
		_goodthis_.poster=poster;/*最外层divdom*/
		_goodthis_.posterm=posterm;/*最底部选择所有dom*/
		
		_goodthis_.posterNum=posterNum;/*数量的dom*/
		_goodthis_.evePrice=evePrice;/*单价的dom*/	
		_goodthis_.putAll=putAddr;/*存放总价dom*/
		
		/*全选dom*/
		_goodthis_.posterdl=_goodthis_.poster.children().not("div");/*所有dl*/
		_goodthis_.posterAllCheckbox=_goodthis_.posterdl.children();/*所有dt，dd*/
		_goodthis_.selectCheckbox=$("#"+_goodthis_.posterm.prop("for"));/*底部选择所有选择框id*/
		_goodthis_.selectAll(_goodthis_.posterAllCheckbox);	
		_goodthis_.doSelect();
		
		/*部分全选dom*/
		_goodthis_.postAllOneSelectFirstBox=_goodthis_.posterdl.children().not("dd").find("input[type='checkbox']");/*单选部分全部*/
		_goodthis_.postAllOneSelectFirst=_goodthis_.postAllOneSelectFirstBox.next();/*单选部分全部*/
		
		/*单选dom*/
		_goodthis_.postAllOneSelectOtherBox=_goodthis_.posterdl.children().not("dt").find("input[type='checkbox']");/*单选个别*/
		
		_goodthis_.posterm.on("tap",function(){	
			_goodthis_.checkAllSelect(_goodthis_.posterAllCheckbox,_goodthis_.selectCheckbox,false);
			_goodthis_.getAllPrice();
		});/*点击选择所有*/
		
		_goodthis_.postAllOneSelectFirst.on("tap",function(){
			_goodthis_.checkAllSelect($(this).parent().nextAll(),$(this).prev(),true);
			_goodthis_.getAllPrice();
		});/*点击选择一类所有*/
		
		_goodthis_.postAllOneSelectOtherBox.change(function(){			
			_goodthis_.checkAllButton();
			_goodthis_.checkOneAllButton($(this).parent().parent().siblings("dt").siblings("dd").find("input[type='checkbox']"),
					$(this).parent().parent().siblings("dt").find("input[type='checkbox']"));
			_goodthis_.getAllPrice();
		});/*点击选择个别*/
		
	};
	
	selectGoods.prototype={
		checkAllSelect:function(checkAllSelectBoxs,checkAllSelectTh,types){
			this.allCheckboxValue=checkAllSelectTh.prop("checked");			
			if(!this.allCheckboxValue){	
				this.selectAll(checkAllSelectBoxs);
				if(types){
				this.checkAllButton();
				}
			}else{	
				this.deleteAll(checkAllSelectBoxs);
				if(types){
				this.checkAllButton();
				}
			}
		},
		
		doSelect:function(){
			this.selectCheckbox.prop("checked","checked");
		},		
		
		
		selectAll:function(checkAllSelectBoxs){
			checkAllSelectBoxs.each(function(){
				$(this).find("input[type='checkbox']").prop("checked","checked");
			});			
		},
		
		deleteAll:function(checkAllSelectBoxs){
			checkAllSelectBoxs.each(function(){
				$(this).find("input[type='checkbox']").prop("checked","");
			});
		},
		
		checkAllButton:function(){
			var _checkAllButton_=this;
			var _checkAllButtonType_=true;
			_checkAllButton_.postAllOneSelectOtherBox.each(function(){
				if(!$(this).prop("checked")){
					_checkAllButton_.selectCheckbox.prop("checked","");
					_checkAllButtonType_=false;
					return false;
				}
			});
			if(_checkAllButtonType_){
			_checkAllButton_.selectCheckbox.prop("checked","checked");
			}
		},
		
		checkOneAllButton:function(valueOneCheck,valueOneCheckButton){
			var valueOneCheckType=true;
			valueOneCheck.each(function(){
				if(!$(this).prop("checked")){
					valueOneCheckType=false;
					return false;
				}
			});
			if(valueOneCheckType){
				valueOneCheckButton.prop("checked","checked");
			}else{
				valueOneCheckButton.prop("checked","");
			}			
		},
		
		getAllPrice:function(){
			var _this_=this;
			var truePrice=0;
			$.each(_this_.postAllOneSelectOtherBox,function(index,vals){
				if($(vals).prop("checked")){
					truePrice+=parseFloat($(_this_.evePrice[index]).html())*parseFloat($(_this_.posterNum[index]).html());
				}else{
					truePrice+=0;
				}				
				
			});
			_this_.putAll.html(truePrice.toFixed(2));
		}
	    
	};
	
	selectGoods.init=function(posterm,posters,goodsNums,evePrice,putAddr){
		var _this_=this;
			new _this_(posterm,posters,goodsNums,evePrice,putAddr);
	};
	
	window["selectGoods"]=selectGoods;
	
})(jQuery);