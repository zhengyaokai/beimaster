<%@ page contentType="application/msword; charset=gb2312"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	response.setHeader("Content-Disposition",
			"attachment;filename=authorize.doc");
%>
<html xmlns:v="urn:schemas-microsoft-com:vml"xmlns:o="urn:schemas-microsoft-com:office:office"xmlns:w="urn:schemas-microsoft-com:office:word"xmlns:m="http://schemas.microsoft.com/office/2004/12/omml"xmlns="http://www.w3.org/TR/REC-html40">
<head><meta http-equiv=Content-Type  content="text/html; charset=gb2312" ><meta name=ProgId  content=Word.Document ><meta name=Generator  content="Microsoft Word 14" ><meta name=Originator  content="Microsoft Word 14" ><title></title>
<!--[if gte mso 9]><xml><w:WordDocument><w:View>Print</w:View><w:TrackMoves>false</w:TrackMoves><w:TrackFormatting/><w:ValidateAgainstSchemas/><w:SaveIfXMLInvalid>false</w:SaveIfXMLInvalid><w:IgnoreMixedContent>false</w:IgnoreMixedContent><w:AlwaysShowPlaceholderText>false</w:AlwaysShowPlaceholderText><w:DoNotPromoteQF/><w:LidThemeOther>EN-US</w:LidThemeOther><w:LidThemeAsian>ZH-CN</w:LidThemeAsian><w:LidThemeComplexScript>X-NONE</w:LidThemeComplexScript><w:Compatibility><w:BreakWrappedTables/><w:SnapToGridInCell/><w:WrapTextWithPunct/><w:UseAsianBreakRules/><w:DontGrowAutofit/><w:SplitPgBreakAndParaMark/><w:DontVertAlignCellWithSp/><w:DontBreakConstrainedForcedTables/><w:DontVertAlignInTxbx/><w:Word11KerningPairs/><w:CachedColBalance/><w:UseFELayout/></w:Compatibility><w:BrowserLevel>MicrosoftInternetExplorer4</w:BrowserLevel><m:mathPr><m:mathFont m:val="Cambria Math"/><m:brkBin m:val="before"/><m:brkBinSub m:val="--"/><m:smallFrac m:val="off"/><m:dispDef/><m:lMargin m:val="0"/> <m:rMargin m:val="0"/><m:defJc m:val="centerGroup"/><m:wrapIndent m:val="1440"/><m:intLim m:val="subSup"/><m:naryLim m:val="undOvr"/></m:mathPr></w:WordDocument></xml><![endif]-->
<style>
@font-face {
	font-family: "Times New Roman";
}

@font-face {
	font-family: "宋体";
}

@font-face {
	font-family: "Calibri";
}

@font-face {
	font-family: "Wingdings";
}

@font-face {
	font-family: "微软雅黑";
}

p.MsoNormal {
	mso-style-name: 正文;
	mso-style-parent: "";
	margin: 0pt;
	margin-bottom: .0001pt;
	mso-pagination: none;
	text-align: justify;
	text-justify: inter-ideograph;
	font-family: 'Times New Roman';
	font-size: 10.5000pt;
	mso-font-kerning: 1.0000pt;
}

span.10 {
	font-family: Calibri;
}

span.15 {
	font-family: 'Times New Roman';
	mso-fareast-font-family: 宋体;
	font-size: 9.0000pt;
}

span.16 {
	font-family: 'Times New Roman';
	mso-fareast-font-family: 宋体;
	font-size: 9.0000pt;
}

p.MsoHeader {
	mso-style-name: 页眉;
	margin: 0pt;
	margin-bottom: .0001pt;
	border-bottom: 1.0000pt solid windowtext;
	mso-border-bottom-alt: 0.7500pt solid windowtext;
	padding: 0pt 0pt 1pt 0pt;
	layout-grid-mode: char;
	mso-pagination: none;
	text-align: center;
	font-family: 'Times New Roman';
	font-size: 9.0000pt;
	mso-font-kerning: 1.0000pt;
}

p.MsoFooter {
	mso-style-name: 页脚;
	margin: 0pt;
	margin-bottom: .0001pt;
	layout-grid-mode: char;
	mso-pagination: none;
	text-align: left;
	font-family: 'Times New Roman';
	font-size: 9.0000pt;
	mso-font-kerning: 1.0000pt;
}

span.msoIns {
	mso-style-type: export-only;
	mso-style-name: "";
	text-decoration: underline;
	text-underline: single;
	color: blue;
}

span.msoDel {
	mso-style-type: export-only;
	mso-style-name: "";
	text-decoration: line-through;
	color: red;
}

table.MsoNormalTable {
	mso-style-name: 普通表格;
	mso-style-parent: "";
	mso-style-noshow: yes;
	mso-tstyle-rowband-size: 0;
	mso-tstyle-colband-size: 0;
	mso-padding-alt: 0.0000pt 5.4000pt 0.0000pt 5.4000pt;
	mso-para-margin: 0pt;
	mso-para-margin-bottom: .0001pt;
	mso-pagination: widow-orphan;
	font-family: 'Times New Roman';
	font-size: 10.0000pt;
	mso-ansi-language: EN-US;
	mso-fareast-language: ZH-CN;
	mso-bidi-language: #0400;
}

@page {
	mso-page-border-surround-header: no;
	mso-page-border-surround-footer: no;
}
@page Section0{
    margin-top:49.6500pt;
    margin-bottom:56.7500pt;
    margin-left:90.0000pt;
    margin-right:90.0000pt;
    size:595.3000pt 841.9000pt;
    layout-grid:15.6000pt;
}
div.Section0{
    page:Section0;
}
</style>
</head>
<body style="tab-interval:21pt;text-justify-trim:punctuation;">
	<!--StartFragment-->
<div class="Section0" style="layout-grid:15.6000pt;">
		<p class=MsoNormal align=center
			style="mso-pagination:widow-orphan;text-align:center;line-height:120%;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:18.0000pt;mso-font-kerning:0.0000pt;"><font
				face="微软雅黑">授权委托书</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:18.0000pt;mso-font-kerning:0.0000pt;"><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal
			style="text-indent:21.0000pt;mso-char-indent-count:2.0000;mso-pagination:widow-orphan;text-align:left;line-height:170%;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">中华人民共和国增值税暂行条例第二十一条规定：向消费者个人、小规模纳税人销售货物或者应税劳务的，不得开具增值税专用发票。因此，如果您需要开具增值税专用发票，请提供公司委托您个人或代理公司采购货物并付款的授权委托书。</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal
			style="mso-pagination:widow-orphan;text-align:left;line-height:170%;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(255,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">（必填）委托公司</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">：</font>
			</span>
		</p>
		<p class=MsoNormal
			style="mso-pagination:widow-orphan;text-align:left;line-height:170%;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(255,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">（必填）法定代表人</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">：</font>
			</span>
		</p>
		<p class=MsoNormal
			style="mso-pagination:widow-orphan;text-align:left;line-height:170%;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(255,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">（必填）联系电话</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">：</font>
			</span>
		</p>
		<p class=MsoNormal
			style="mso-pagination:widow-orphan;text-align:left;line-height:170%;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(255,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">（必填）代理公司名称或代理人姓名</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">：</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(255,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">（必填）联系电话</font></span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">：</font></span><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal
			style="text-indent:2em;mso-char-indent-count:2.4500;mso-pagination:widow-orphan;text-align:left;line-height:170%;">
			<span style="font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;">
				委托公司委托上述代理公司（代理人）通过工电宝网站(<a href="">www.bmatser.com</a>)以下单方式采购商品，并为本公司验货、收货及支付货款。

			</span><u>
			</u><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal
			style="text-indent:25.7000pt;mso-char-indent-count:2.4500;mso-pagination:widow-orphan;text-align:left;line-height:170%;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">付款方式为：委托公司以</font>&nbsp;</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(255,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">（</font>
			</span><u><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(255,0,0);text-decoration:underline;text-underline:single;font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
					face="微软雅黑">必填）</font>
			</span>
			</u><u><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);text-decoration:underline;text-underline:single;font-size:10.5000pt;mso-font-kerning:1.0000pt;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
			</u><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">（现金、转账）方式将货款转至上述代理公司或代理人的银行卡账户，由上述代理公司（代理人）将此款项通过转账或网上支付方式向工电宝网站（</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(255,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">工电宝账号</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">：</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(255,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">（</font>
			</span><u><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(255,0,0);text-decoration:underline;text-underline:single;font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
					face="微软雅黑">请勿修改）</font>${map.username}</span>
			</u><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">）支付相应货款。代理公司或代理人的付款行为视为委托公司的行为，委托公司愿承担由此产生的一切经济与法律责任纠纷。</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal
			style="text-indent:25.5000pt;mso-char-indent-count:2.4300;mso-pagination:widow-orphan;text-align:left;line-height:170%;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">特此授权。</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal align=right
			style="margin-right:9.0000pt;mso-pagination:widow-orphan;text-align:right;line-height:170%;">
			<span style="font-size:10.5000pt;font-family:微软雅黑;">委托公司（<font style="color:rgb(255,0,0);">盖章</font>）：
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font style="color:rgb(255,0,0);text-decoration:underline">（必填）</font>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日</span>
		</p>
		<p class=MsoNormal align=right
			style="margin-right:9.0000pt;word-break:break-all;mso-pagination:widow-orphan;text-align:right;line-height:170%;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:0.0000pt;"><o:p>&nbsp;</o:p>
			</span>
		</p>
		<p class=MsoNormal align=right
			style="margin-right:9.0000pt;mso-pagination:widow-orphan;text-align:right;line-height:170%;">
			<span style="font-size:10.5000pt;font-family:微软雅黑;">代理公司或代理人（<font style="color:rgb(255,0,0);">盖章</font>）：
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font style="color:rgb(255,0,0);text-decoration:underline">（必填）</font>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日</span>
		</p>
		<p class=MsoNormal align=right
			style="margin-right:9.0000pt;mso-pagination:widow-orphan;text-align:right;line-height:170%;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:0.0000pt;"><o:p>&nbsp;</o:p>
			</span>
		</p>
		<p class=MsoNormal
			style="mso-pagination:widow-orphan;text-align:left;">
			<b><span
				style="mso-spacerun:'yes';font-family:宋体;font-weight:bold;font-size:9.0000pt;mso-font-kerning:1.0000pt;"><font
					face="宋体">提示：</font></span>
			</b><span
				style="mso-spacerun:'yes';font-family:微软雅黑;font-size:9.0000pt;mso-font-kerning:1.0000pt;">&nbsp;1、委托公司盖章处须加盖公司公章。委托公司名称、法人代表名字、公司联系电话，公司名称与</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;font-size:9.0000pt;mso-font-kerning:1.0000pt;"><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal
			style="text-indent:45.0000pt;mso-char-indent-count:5.0000;mso-pagination:widow-orphan;text-align:left;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;font-size:9.0000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">公司公章（专用章、个人章、合同章、部门章等均无效）需要一致</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:9.0000pt;mso-font-kerning:0.0000pt;"><font
				face="微软雅黑">。</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:9.0000pt;mso-font-kerning:0.0000pt;"><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal
			style="mso-pagination:widow-orphan;text-align:left;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:9.0000pt;mso-font-kerning:0.0000pt;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;font-size:9.0000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">工电宝账号即您下</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:9.0000pt;mso-font-kerning:0.0000pt;"><font
				face="微软雅黑">单使用的客户账号。（此处请勿随意改动，如有涂抹修改，请在修改处补加</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:9.0000pt;mso-font-kerning:0.0000pt;"><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal
			style="mso-pagination:widow-orphan;text-align:left;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:9.0000pt;mso-font-kerning:0.0000pt;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font
				face="微软雅黑">公章，否则视为无效）。</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:9.0000pt;mso-font-kerning:0.0000pt;"><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal
			style="text-indent:31.5000pt;mso-char-indent-count:3.5000;mso-pagination:widow-orphan;text-align:left;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;font-size:9.0000pt;mso-font-kerning:1.0000pt;">3、建议提供彩色版本的PDF扫描件或者照片图片JPG档</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:9.0000pt;mso-font-kerning:0.0000pt;"><font
				face="微软雅黑">。</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:9.0000pt;mso-font-kerning:0.0000pt;"><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal
			style="text-indent:31.5000pt;mso-char-indent-count:3.5000;mso-pagination:widow-orphan;text-align:left;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;font-size:9.0000pt;mso-font-kerning:1.0000pt;">4、代理公司（代理人）信息填写完整，代理公司（代理人）需要盖章。</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:9.0000pt;mso-font-kerning:0.0000pt;"><o:p></o:p>
			</span>
		</p>
	</div>
	<!--EndFragment-->
</body>
</html>