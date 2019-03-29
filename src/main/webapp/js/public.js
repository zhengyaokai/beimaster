/*
* 通用
* @date: 2017-09-16
* @author: limengmeng
*/
;(function(win,$,undefined){	
	var pub=(function(){
		return{
			url:function(){
				var domain = document.domain;
				return "http://"+domain+":8080/gdbmro_serviceApi";
			},
			
			urls:function(){
				var strFullPath = window.document.location.href;
				var strPath = window.document.location.pathname;
				var pos = strFullPath.indexOf(strPath);
				var prePath = strFullPath.substring(0, pos);
				var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
			    var rootUrl = prePath + postPath;
			    if (rootUrl.indexOf('/page') > 0) {
				    rootUrl = rootUrl.substring(0, rootUrl.indexOf("/page"));
			    }
			    return rootUrl;
			},
			
			loads:function(url, data, methods,successfn, errorfn){
				data = (data == null || data == "" || typeof (data) == "undefined") ? {} : data;
				data._cache=Math.random(15);
				methods = (methods == null || methods == "" || typeof (methods) == "undefined") ? "POST" : methods;
				var queryString=$.param(data),
				    dataPw={};
				dataPw.url=url;
				dataPw.queryString=queryString;
				$.ajax({
					type : "POST",
					data : dataPw,
					url : this.urls()+"/getAppkey/",
					dataType : "json"
				}).success(function(p){
					if(p){
						data.sign=p;
						$.ajax({
							type : methods,
							data : data,
							url : url,
							dataType : "json"
						}).success(function(d){
							successfn(d);
						}).error(function(e){
							errorfn(e);
						});	
					}
				}).error(function(q){
					console.log(q);
				});
			},
			
			getStr:function(name){
			     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
			     var r = window.location.search.substr(1).match(reg);
			     if(r!=null){
			    	 return  unescape(r[2]);
			     }else{
			    	 return null;
			     }
			},
			
			setValueCatch:function(key,value){
				window.localStorage.setItem(key,value); 
			},
			
			getValueCatch:function(key){
				return window.localStorage.getItem(key);
				window.localStorage.clear();
			},
			
			toFixed:function(t){
				return t.toFixed(2);
			},
			
			countDown:function(t){
				var oneDay=86400,
				    oneHour=3600,
				    oneMinute=60,
				    days,
				    hours,
				    minutes,
				    seconds,
				    goInDate="";
				days=Math.floor(t/oneDay);
				goInDate+=days+"天";
				hours=Math.floor((t-oneDay*days)/oneHour);
				goInDate+=hours+"小时";
				minutes=Math.floor((t-days*oneDay-hours*oneHour)/oneMinute);
				goInDate+=minutes+"分";
				seconds=t-days*oneDay-hours*oneHour-minutes*oneMinute;
				goInDate+=seconds+"秒";
				return goInDate;
			},
			
			checkEmpty:function(t){
    			if(t==""){
    				return false;
    			}else{
    				return true;
    			}
    		},
			
			checkTel:function(t){
				var reg=/(^(0[0-9]{2,3}\s)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$)|(^(0[0-9]{2,3})?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$)|(^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$)|(^((\(\d{3}\))|(\d{3}\-))?(1[358]\d{9})$)/;
				if(reg.test(t)){
					return true;
				}else{
					return false;
				}
			}
			
		};
	})();
	
	win.pubFuc=pub;
})(this,jQuery,{});

