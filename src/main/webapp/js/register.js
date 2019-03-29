/*
* 注册页
* @date: 2017-12-10
* @author: limengmeng
*/
;(function(win,$,pubFuc){
    var registerUrl={
    	sendCodeUrl:pubFuc.urls()+"/sms/register/verify/",
    	registerUrl:pubFuc.urls()+"/user/"
    };
    
    var dom={
    	"J_name":".J_name",
    	"J_telephone":".J_telephone",
    	"J_code":".J_code",
    	"J_sendCode":".J_sendCode",
    	"J_pwd":".J_pwd",
    	"J_sPwd":".J_sPwd",
    	"J_company":".J_company",    	
        "J_box":".J_box",
        "J_save":".J_save"
    };
    
    var register=(function(){
    	return{
    		init:function(){
    			var _this_=this;
    			$(dom.J_save).bind("click",function(){
    				_this_.loginIng();
    			});
    			
    			$(dom.J_sendCode).bind("click",function(){
    				_this_.sendCode();
    			});
    		},
    		
    		loginIng:function(){
    			var name=$(dom.J_name).val(),
    			    telephone=$(dom.J_telephone).val(),
    			    code=$(dom.J_code).val(),
    			    pwd=$(dom.J_pwd).val(),
    			    sPwd=$(dom.J_sPwd).val(),
    			    company=$(dom.J_company).val();
                if(!$(dom.J_box).prop("checked")){
                	alert("请先阅读同意工电宝用户注册协议！");
                	return;
                }
                if(!pubFuc.checkEmpty(name)){
    				alert("用户名不能为空！");
    				return;
    			}
                if(!pubFuc.checkEmpty(telephone)){
    				alert("号码不能为空！");
    				return;
    			}
                if(!pubFuc.checkEmpty(code)){
    				alert("验证码不能为空！");
    				return;
    			}
                if(!pubFuc.checkEmpty(pwd)){
    				alert("密码不能为空！");
    				return;
    			}
                if(!pubFuc.checkEmpty(sPwd)){
    				alert("确认密码不能为空！");
    				return;
    			}
                if(!pubFuc.checkEmpty(company)){
    				alert("公司不能为空！");
    				return;
    			}
                if(!pubFuc.checkTel(telephone)){
                	alert("请输入正确格式的号码！");
                	return;
                }
                if(pwd!=sPwd){
                	alert("2次输入密码不相同！");
    				return;
                }
                var registerData={};
                    registerData.userName=name;
                    registerData.password=pwd;
                    registerData.mobile=telephone;
                    registerData.dealerName=company;
                    registerData.userName=name;
                    registerData.verifyCode=code;
                pubFuc.loads(registerUrl.registerUrl, registerData,"POST", function(t) {
                	
                },function(e){
                	console.log(e);
                });
    		},
    		
    		sendCode:function(){
    			var _this_=this;
    			var telephone=$(dom.J_telephone).val();
    			if(!pubFuc.checkEmpty(telephone)){
    				alert("号码不能为空！");
    				return;
    			}
    			if(!pubFuc.checkTel(telephone)){
                	alert("请输入正确格式的号码！");
                	return;
                }
    			pubFuc.loads(registerUrl.sendCodeUrl+telephone+"/", null,"GET", function(t) {
    				if(t.code==0){
    					_this_.sendCodeSuc();
    				}
    			},function(e){
    				console.log(e);
    			});
    			
    		},
    		
    		sendCodeSuc:function(){
    			var times=60;
    			var m=setInterval(function(){
    				times--;
    				if(times==0){
    					$(dom.J_sendCode).prop("disabled","").val("重新发送").button("refresh");
    					clearInterval(m);
    				}else{
    					$(dom.J_sendCode).prop("disabled","disabled").val(times).button("refresh");
    				}
    				
    			},1000);
    		}
    		
    		
    	};
    })();

	$(function(){
		register.init();
	});
	

})(this,jQuery,pubFuc||{});


