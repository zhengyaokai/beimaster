/*
* 登录页
* @date: 2017-12-9
* @author: limengmeng
*/
;(function(win,$,pubFuc){
    var loginUrl={
        loginUrl:pubFuc.urls()+"/user/login/"
    };
    
    var dom={
        "J_name":".J_name",
        "J_pwd":".J_pwd",
        "J_button":".J_button"
    };
    
    var login=(function(){
    	return{
    		init:function(){
    			var _this_=this;
    			$(dom.J_button).bind("click",function(){
    				_this_.loginIng();
    			});                
    		},
    		
    		loginIng:function(){
    			var name=$(dom.J_name).val(),
    			    pwd=$(dom.J_pwd).val();
    			if(!pubFuc.checkEmpty(name)){
    				alert("用户名/手机/邮箱不能为空！");
    				return;
    			}
    			if(!pubFuc.checkEmpty(pwd)){
    				alert("密码不能为空！");
    				return;
    			}
    			pubFuc.loads(loginUrl.loginUrl+name+"/"+pwd,null,"GET", function(c){
    				if(c.code==0){
    					location.href=pubFuc.urls()+"/page/index.jsp";
    				}
    			},function(e){
    				console.log(e);
    			});
    		}
    	};
    })();

	$(function(){
		login.init();
	});
	

})(this,jQuery,pubFuc||{});


