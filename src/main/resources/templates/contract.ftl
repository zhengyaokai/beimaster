<?xml version="1.0" encoding="UTF-8"?>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style>
p,h1{margin:0;padding:0; line-height:22px;}
body{ font-size:14px; font-weight:400;font-family: SimSun;}
table td{font-size:9pt;}
</style>
</head>
<body>
<div>

<div  style="font-size:20pt; line-height:35px;text-align:center;font-weight: bolder;"><strong>订单合同</strong></div>
<table width="100%" border="1" align="center" cellpadding="0" cellspacing="2" bgcolor="#000000" >
  <tr>
    <td height="100" valign="top" bgcolor="#FFFFFF">
    <table class="atable" width="100%" border="0" cellspacing="0" cellpadding="6" >
	 <tr>
	    <td style="" align="center">供方名称：</td>
        <td style="">${orderInfo.sellerName !''}</td>
		<td align="">合同编号：</td>
        <td style="">${orderInfo.id?c}</td>
      </tr>  
      <tr>
        <td align="center" style="">需方名称：</td>
        <td style="">${orderInfo.buyerName !''}</td>    
        <td style="" align="">签订地点：</td>
        <td style="">苏州</td>
      </tr> 
	   <tr>
        <td align="center" style=""></td>
        <td style=""></td>    
        <td style="" align="">签订时间：</td>
        <td style="">${orderInfo.orderTime?string("yyyy-MM-dd")}</td>
      </tr>   
    </table></td>
  </tr>
  <tr>
    <td height="35" bgcolor="#FFFFFF" style="font-size:7.5pt;">一、产品及金额          签订时间：${orderInfo.orderTime?string("yyyy-MM-dd")}</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF">
    <table class="atable" width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#000000">
      <tr>
        <td height="30" align="center" bgcolor="#FFFFFF">序号</td>
		 <td style="border-left:1px solid #333;" width="95" height="30" align="center" bgcolor="#FFFFFF">品牌</td>
        <td style="border-left:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">订货号</td>
        <td style="border-left:1px solid #333;" width="180" height="30" align="center" bgcolor="#FFFFFF">型号</td>
        <td style="border-left:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">单位</td>
        <td style="border-left:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">数量</td>
        <td style="border-left:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">合计(￥)</td>
        <td style="border-left:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">优惠</td>
      </tr>
      <#list orderGoods  as goods>
			<tr>
				<td style="border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">${goods_index+1}</td>
				<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF"><span>${goods.brandName!''}</span></td>
		    	<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">${goods.buyNo!''}</td>
			 	<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF"><span>${goods.model!''}</span></td>
			 	<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">${goods.measure!''}</td>
				<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">${goods.num}</td>
				<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">${goods.num*(goods.unitPrice-goods.cash!0)}</td>
				<#if goods_index==0>
					<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF" rowspan="${orderGoods?size}">
						${saleChar!''}
					</td>
				</#if>
			</tr>
      </#list>	
      <tr>
      	<td style="border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">运费:</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">￥${(orderInfo.freightAmount!0)?string('0.00')}</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">金额大写（元）:</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" colspan="2" align="center" bgcolor="#FFFFFF">${orderInfo.charAmount}</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center"  bgcolor="#FFFFFF">实付金额:</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF" colspan="2" >￥${(orderInfo.payAmount-orderInfo.balanceDeduction)?string('0.00')}</td>
      </tr>
    </table>
    </td>
  </tr>	
  <tr>
    <td height="160" valign="top" bgcolor="#FFFFFF" style="font-size:7.5pt;">
	    二、付款方式：全款到账提货。
		<br/>
    	三、货期：合同生效以应付款到帐开始核算，货期不含节假日，遇节假日顺延。
		<br/>
		四、交（提）货地点：需方所在地
		<br/>
		五、运输方式及到达站港和费用负担：供方公司承担。
		<br/>
		六、质量验收标准：按厂家原装产品标准验收。一年质保
		<br/>
		七、解决合同纠纷方式：供需双方友好协商解决，协商无效由当地仲裁机构解决。
		<br/>
		八、其它约定事项：
		<br/>
		1、本合同一式二份。双方各执一份，自应付款到账之日生效。
		<br/>
		2、供方在收到货款且货品出库后向需方开具16%增值税发票 。税号：91320506MA1T956B6N，开户行：中国农业银行苏州长桥支行，账号：10539501040025253。
		<br/>
		3、自通知需方前来提货之日起,一周之内不来提货或不付款提货的,我司有权处置留下的货，不予保留。
		<br/>
		4、自合同签订之日起超过三天需方未付款，合同将无效视为自动取消，价格及货期重新确认。
		<br/>
		5、本合同有效期一年，到期失效！特别说明收到货后当场验收，如有破损件拒绝签收，否则公司不予承担破损件。
		<br/>
		6、双方确认签字盖章后有效，手写修改无效，传真件有效。
		<br/>
	</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF">
    <table class="atable lm-table" width="100%" border="0" cellspacing="0" cellpadding="5" style="">
      <tr>
        <td width="25%" height="25">需方盖章：</td>
        <td style="border-left:1px solid #333;" width="25%" height="25" rowspan="2">${orderInfo.buyerName !''}</td>
        <td style="border-left:1px solid #333;" width="25%" height="25" >供方盖章：</td>
        <td style="border-left:1px solid #333;" height="25" align="left" rowspan="2">江苏工电宝信息科技有限公司</td>
      </tr>
      <tr>
        <td style="border-top:1px solid #333;" width="25%" height="25">需方:</td>

        <td style="border-left:1px solid #333;border-top:1px solid #333;position:relative;" width="25%" height="25">供方：
            <div style="position:absolute;right:-90px;bottom:-10px;z-index:200;height:165px;width:180px">
			 <img src="http://images.bmatser.com/static/images/brandindex/gdb_logo.png" width="165" height="165" />
			 </div>
        </td>
		
      </tr>
      <tr>
        <td style="border-top:1px solid #333;" width="25%" height="25">开户银行：</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" width="25%" height="25">${orderInfo.bank}</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" width="25%" height="25">开户银行：</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" height="25" align="left">中国农业银行苏州长桥支行</td>
      </tr>
	   <tr>
        <td style="border-top:1px solid #333;" width="25%" height="25">账号：</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" width="25%" height="25">${orderInfo.bank_account}</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" width="25%" height="25">账号：</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" height="25" align="left">10539501040025253</td>
      </tr>
	  <tr>
        <td style="border-top:1px solid #333;" width="25%" height="25">税号：</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" width="25%" height="25">${orderInfo.taxpaye_rno}</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" width="25%" height="25">税号：</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" height="25" align="left">91320506MA1T956B6N</td>
      </tr>
	  <tr>
        <td style="border-top:1px solid #333;" width="25%" height="25">地址：</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" width="25%" height="25">${orderInfo.recv_address}</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" width="25%" height="25">地址：</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" height="25" align="left">苏州市吴中区越溪吴中大道2288号B2幢306</td>
      </tr>
	  <tr>
        <td style="border-top:1px solid #333;" width="25%" height="25">电话：</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" width="25%" height="25">${orderInfo.reg_telphone}</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" width="25%" height="25">电话：</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" height="25" align="left">0512-65099638</td>
      </tr>
	  <tr>
        <td style="border-top:1px solid #333;" width="25%" height="25">传真：</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" width="25%" height="25"></td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" width="25%" height="25">传真：</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" height="25" align="left">0512-65771578</td>
      </tr>
	  <tr>
        <td style="border-top:1px solid #333;" width="25%" height="25">代表人：</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" width="25%" height="25"></td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" width="25%" height="25">代表人：</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" height="25" align="left">张文屏</td>
      </tr>
    </table>
    </td>
  </tr>
</table>
</div>
</body>
</html>