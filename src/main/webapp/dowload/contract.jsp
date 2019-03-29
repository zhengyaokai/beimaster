<%@ page contentType="application/msword; charset=gb2312" %>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%response.setHeader("Content-Disposition", "attachment;filename=contract.doc");%> 


<html xmlns:w="urn:schemas-microsoft-com:office:word">

<head>
<style>
p,h1{margin:0;padding:0; line-height:22px;}
body{ font-size:14px; font-weight:400;}
table td{font-size:9pt;}
</style>
</head>
<body>

<table width="750" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td width="245" rowspan="2"><img src="http://bmatser.com/companyinfo/032a35c3-2a3e-4c9d-948a-d2a7a6c55449.png" width="60.33pt" height="18.79pt" /></td>
    <td colspan="3" align="right"><h1 style="font-size:16pt; line-height:30px;">江苏工电宝信息科技有限公司</h1></td>
  </tr>
  <tr>
    <td colspan="3" align="right"><p style="font-size:8pt;">0512-65776138 FAX:0512-65771578</p><p style="font-size:8pt;">
   	ADD：苏州市吴中区吴中大道亿达软件新城B2栋306</p></td>
  </tr>
</table>
<h1 align="center" style="font-size:19pt; line-height:35px;">销售合同</h1>
<table width="750" border="0" align="center" cellpadding="0" cellspacing="4" bgcolor="#000000">
  <tr>
    <td height="126" valign="top" bgcolor="#FFFFFF">
    <table class="atable" width="100%" border="0" cellspacing="0" cellpadding="5" >
      <tr>
        <td align="center">销售单号</td>
        <td style="border-left:1px solid #333;">${map.orderInfo.id}</td>
        <td style="border-left:1px solid #333;" align="center">时&nbsp;&nbsp;间</td>
        <td style="border-left:1px solid #333;"><fmt:formatDate value="${map.orderInfo.orderTime}" pattern="yyyy-MM-dd" type="date" dateStyle="long" /></td>
      </tr>
      <tr>
        <td align="center" style="border-top:1px solid #333;">需&nbsp;&nbsp;方</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;">${map.orderInfo.buyerName }</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" align="center">供&nbsp;&nbsp;方</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;">${map.orderInfo.sellerName }</td>
      </tr>
      <tr>
        <td align="center" style="border-top:1px solid #333;">联&#127;系&#127;人</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;">${map.orderInfo.linkman}</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" align="center">联&#127;系&#127;人</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;">${map.orderInfo.staffName}</td>
      </tr>
      <tr>
        <td style="border-top:1px solid #333;" align="center">电&nbsp;&nbsp;话</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;">${map.orderInfo.buyerTel}</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" align="center">电&nbsp;&nbsp;话</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;"> ${map.orderInfo.sellerTel}</td>
      </tr>
      <tr>
        <td style="border-top:1px solid #333;" align="center">传&nbsp;&nbsp;真</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;">${map.orderInfo.buyerFax}</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" align="center">传&nbsp;&nbsp;真</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;">${map.orderInfo.sellerFax}</td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td height="35" bgcolor="#FFFFFF" style="font-size:7.5pt;font-family:微软雅黑;">&nbsp;本合同由买卖双方协商订立，需方同意购买，供方同意出售下述产品（以下简称“产品”）</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF">
    <table class="atable" width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#000000">
      <tr>
        <td height="30" align="center" bgcolor="#FFFFFF">序号</td>
        <td style="border-left:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">订货号</td>
        <td style="border-left:1px solid #333;" width="95" height="30" align="center" bgcolor="#FFFFFF">品牌</td>
        <td style="border-left:1px solid #333;" width="180" height="30" align="center" bgcolor="#FFFFFF">型号</td>
        <td style="border-left:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">单位</td>
        <td style="border-left:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">数量</td>
        <td style="border-left:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">抵用券后单价(￥)</td>
        <td style="border-left:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">合计(￥)</td>
        <td style="border-left:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">货期(天)</td>
        <td style="border-left:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">优惠(￥)</td>
      </tr>
      
      <c:forEach items="${map.orderGoods}" var="goods" varStatus="status">
      	<c:if test="${status.index ==0}">
			<tr>
				<td style="border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">${status.index + 1}</td>
		    	<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">${goods.buyNo}</td>
				<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF"><span>${goods.brandName}</span></td>
			 	<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF"><span>${goods.model}</span></td>
			 	<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">${goods.measure}</td>
				<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">${goods.num}</td>
				<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF"><fmt:formatNumber value='${goods.unitPrice-goods.cash}' type='number' pattern='0.####' /></td>
				<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF"><fmt:formatNumber value='${goods.num*(goods.unitPrice-goods.cash)}' type='number' pattern='0.##' /></td>
				<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF"></td>
				<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF" rowspan="${goods.size()}">
						<c:if test="${map.orderInfo.groupOrderSale != '0' &&  map.orderInfo.groupOrderSale!='0.00'}">
							团购优惠:<fmt:formatNumber type="number" pattern="0.00" value="${map.orderInfo.groupOrderSale}" maxFractionDigits="2"/>
							<c:if test="${(map.orderInfo.fullCutAmount != '0' &&  map.orderInfo.fullCutAmount!='0.00')||(map.orderInfo.beanDeduction != '0' &&  map.orderInfo.beanDeduction!='0.00') || (map.orderInfo.otherAmount != '0' &&  map.orderInfo.otherAmount!='0.00')} "><br></c:if>
						</c:if>
						<c:if test="${map.orderInfo.otherAmount != '0' &&  map.orderInfo.otherAmount!='0.00' }">
							其他优惠:<fmt:formatNumber type="number" pattern="0.00" value="${map.orderInfo.otherAmount}" maxFractionDigits="2"/> 
						</c:if>
				</td>
			</tr>
		</c:if>
		<c:if test="${status.index !=0}">
			<tr>
				<td style="border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">${status.index + 1}</td>
		    	<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">${goods.buyNo}</td>
				<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF"><span>${goods.brandName}</span></td>
			 	<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF"><span>${goods.model}</span></td>
			 	<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">${goods.measure}</td>
				<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">${goods.num}</td>
				<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF"><fmt:formatNumber value='${goods.unitPrice-goods.cash}' type='number' pattern='0.####' /></td>
				<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF"><fmt:formatNumber value='${goods.num*(goods.unitPrice-goods.cash)}' type='number' pattern='0.##' /></td>
				<td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF"></td>
			</tr>
		</c:if>
	</c:forEach>
      <tr>
      	<td style="border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">运费:</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">￥${map.orderInfo.freightAmount}</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF">金额大写（元）:</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" colspan="2" align="center" bgcolor="#FFFFFF">${map.orderInfo.charAmount}</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF" colspan="2">余额抵扣:￥${map.orderInfo.balanceDeduction}</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center"  bgcolor="#FFFFFF">实付金额:</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" height="30" align="center" bgcolor="#FFFFFF" colspan="2" >￥${map.orderInfo.payAmount-map.orderInfo.balanceDeduction}</td>
      </tr>
    </table>
    </td>
  </tr>
  <tr>
    <td height="35" bgcolor="#FFFFFF" style="font-size:7.5pt;font-family:微软雅黑;">&nbsp;货款由江苏工电宝信息科技有限公司（www.bmatser.com）收取.</td>
  </tr>
  <tr>
    <td height="192" valign="top" bgcolor="#FFFFFF" style="font-size:7.5pt;font-family:微软雅黑;">
    	1.发货地点：供方所在地苏州或厂家仓库所在地；
		<br>
		2.付款方式: 全款，款到发货；
		<br>
		3.质量标准：产品以原制造厂家出厂标准，按照原制造厂家的技术文件标准验收；
		<br>
		4.发票：供方在收到货款同时向需方开具16%增值税发票。税号：91320506MA1T956B6N，开户行：中国农业银行苏州长桥支行，账号: 10539501040025253；
		<br>
		5.供方发货后，非供方质量问题不予以退换货；
		<br>
		6.异议期限：需方如发现产品质量、数量与本合同规定不符，需方应在到货后3日内提出；否则即视为对交付产品无异议；
		<br>
		7.合同生效后，若需方中途退货，应向供方偿付退货产品货款10%的违约金，并赔偿给供方造成的一切损失，包括但不限于调查取证、律师费；
		<br>
		8.争议解决：任何一方不得擅自变更或解除合同，变更或解除合同应采取书面形式，协议未达成前原合同仍然有效，直至双方履行完合同规定义务为止，协商不成交由供方所在地的法院诉讼解决；
		<br>
		9.违约责任：双方同意按上述条款履行合同。如海关抽检自然灾害等不可抗力的因素造成供方交货期有所延误的供方无需承担责任。需方未在付款期限内付清全部货款，每天则按货品总金额1‰违约金支付给供方,违约金累计总额不超过合同总额的5%；
		<br>
		10.本合同经双方签字盖章（或盖章）后生效。
		<br>
	</td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF">
    <table class="atable" width="100%" border="0" cellspacing="0" cellpadding="5" >
      <tr>
        <td width="25%" height="25">需方盖章：</td>
        <td style="border-left:1px solid #333;" width="25%" height="25" rowspan="2">&nbsp;</td>
        <td style="border-left:1px solid #333;" width="25%" height="25">供方盖章：</td>
        <td style="border-left:1px solid #333;" height="25" align="left" rowspan="2"><div style="position: absolute; height: 85px; top: 796px; left: 1080px; width: 166px;"><img src="http://www.bmatser.com/contractImg/60535f52-74ed-46ca-9b7b-9c869bac801d.png" width="180" height="165" /></div></td>
      </tr>
      <tr>
        <td style="border-top:1px solid #333;" width="25%" height="25">代理人（签字）</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" width="25%" height="25">代理人（签字）：</td>
      </tr>
      <tr>
        <td style="border-top:1px solid #333;" width="25%" height="25">日期：</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" width="25%" height="25">&nbsp;</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" width="25%" height="25">日期：</td>
        <td style="border-left:1px solid #333;border-top:1px solid #333;" height="25" align="left"><fmt:formatDate value="${map.orderInfo.orderTime}" pattern="yyyy-MM-dd" type="date" dateStyle="long" /></td>
      </tr>
    </table>
    </td>
  </tr>
</table>
</body>
</html>
