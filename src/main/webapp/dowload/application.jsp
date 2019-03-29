<%@ page contentType="application/msword; charset=gb2312"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	response.setHeader("Content-Disposition",
			"attachment;filename=application.doc");
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

@page Section0 {
	margin-top: 49.6500pt;
	margin-bottom: 56.7500pt;
	margin-left: 90.0000pt;
	margin-right: 90.0000pt;
	size: 595.3000pt 841.9000pt;
	layout-grid: 15.6000pt;
}

div.Section0 {
	page: Section0;
}
</style>
</head>
<body style="tab-interval:21pt;text-justify-trim:punctuation;">
	<!--StartFragment-->
	<div class="Section0" style="layout-grid:15.6000pt;">
		<p class=MsoNormal align=center
			style="mso-pagination:widow-orphan;text-align:center;line-height:170%;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:18.0000pt;mso-font-kerning:0.0000pt;"><font
				face="微软雅黑">开票申请书</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:18.0000pt;mso-font-kerning:0.0000pt;"><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal
			style="text-indent:2em;mso-char-indent-count:2.0000;mso-pagination:widow-orphan;text-align:left;line-height:170%;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">中华人民共和国增值税暂行条例第二十一条规定：向消费者个人、小规模纳税人销售货物或者应税劳务的，不得开具增值税专用发票。因此，如果您需要开具增值税专用发票，请完整填写以下内容。</font></span>
		</p>
		<p class=MsoNormal
			style="mso-pagination:widow-orphan;text-align:left;line-height:170%;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(255,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">（必填）公司名称（以下简称我司）：</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(255,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><o:p></o:p>
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
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><o:p></o:p>
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
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal
			style="mso-pagination:widow-orphan;text-align:left;line-height:170%;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(255,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">（必填）指定付款人</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">：</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal
			style="mso-pagination:widow-orphan;text-align:left;line-height:170%;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;">&nbsp;</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(255,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">（必填）联系电话：</font>&nbsp;&nbsp;&nbsp;&nbsp;</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;">&nbsp;&nbsp;&nbsp;&nbsp;</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal
			style="text-indent:25.7000pt;mso-char-indent-count:2.4500;mso-pagination:widow-orphan;text-align:left;line-height:170%;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">付款方式为：指定付款人账户（</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:18.0000pt;mso-font-kerning:1.0000pt;">&#9633;&nbsp;</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">银行卡、</font></span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:18.0000pt;mso-font-kerning:1.0000pt;">&#9633;&nbsp;</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">对公账户、</font></span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:18.0000pt;mso-font-kerning:1.0000pt;">&#9633;&nbsp;</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">支付宝账户、</font></span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:18.0000pt;mso-font-kerning:1.0000pt;">&#9633;&nbsp;</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">微信账户），</font></span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">我司指定上述付款方式将此款项通过转账</font></span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">或网上支付方式向工电宝网站（</font></span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(255,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">工电宝注册账号</font></span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">：</font></span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(255,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">（</font></span><u><span style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(255,0,0);text-decoration:underline;text-underline:single;font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
					face="微软雅黑">请勿修改）</font>${map.username}</span></u><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">）支付相应货款。我司或者指定付款人的付款行为视为我司的对公行为，</font></span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">我司愿承担由此产生的一切经济与法律责任纠纷。</font></span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><o:p></o:p></span>
		</p>
		<p class=MsoNormal
			style="text-indent:25.5000pt;mso-char-indent-count:2.4300;mso-pagination:widow-orphan;text-align:left;line-height:170%;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">特此证明。</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:1.0000pt;"><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal align=right
			style="margin-right:9.0000pt;mso-pagination:widow-orphan;text-align:right;line-height:170%;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:0.0000pt;"><font
				face="微软雅黑">公司名称（</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(255,0,0);font-size:10.5000pt;mso-font-kerning:0.0000pt;"><font
				face="微软雅黑">盖章</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:0.0000pt;"><font
				face="微软雅黑">）：</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(255,0,0);font-size:10.5000pt;mso-font-kerning:0.0000pt;">&nbsp;&nbsp;(</span><u><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(255,0,0);text-decoration:underline;text-underline:single;font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
					face="微软雅黑">必填）</font>
			</span>
			</u><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(255,0,0);font-size:10.5000pt;mso-font-kerning:0.0000pt;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:0.0000pt;"><font
				face="微软雅黑">年</font>&nbsp;&nbsp;<font face="微软雅黑">月</font>&nbsp;&nbsp;<font
				face="微软雅黑">日</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:0.0000pt;"><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal align=right
			style="margin-right:9.0000pt;word-break:break-all;mso-pagination:widow-orphan;text-align:right;line-height:170%;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:0.0000pt;"><o:p>&nbsp;</o:p>
			</span>
		</p>
		<p class=MsoNormal align=right
			style="margin-right:9.0000pt;word-break:break-all;mso-pagination:widow-orphan;text-align:right;line-height:170%;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:0.0000pt;"><font
				face="微软雅黑">指定付款人（</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(255,0,0);font-size:10.5000pt;mso-font-kerning:0.0000pt;"><font
				face="微软雅黑">签字</font>/盖章</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:0.0000pt;"><font
				face="微软雅黑">）：</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(255,0,0);font-size:10.5000pt;mso-font-kerning:0.0000pt;">&nbsp;&nbsp;(</span><u><span
				style="mso-spacerun:'yes';font-family:微软雅黑;color:rgb(255,0,0);text-decoration:underline;text-underline:single;font-size:10.5000pt;mso-font-kerning:1.0000pt;"><font
					face="微软雅黑">必填</font>
			</span>
			</u><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(255,0,0);font-size:10.5000pt;mso-font-kerning:0.0000pt;">&nbsp;)&nbsp;&nbsp;</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:0.0000pt;">&nbsp;&nbsp;&nbsp;<font
				face="微软雅黑">年</font>&nbsp;&nbsp;<font face="微软雅黑">月</font>&nbsp;&nbsp;<font
				face="微软雅黑">日</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:10.5000pt;mso-font-kerning:0.0000pt;"><o:p></o:p>
			</span>
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
					face="宋体">提示：</font>&nbsp;</span>
			</b><span
				style="mso-spacerun:'yes';font-family:微软雅黑;font-size:9.0000pt;mso-font-kerning:1.0000pt;">1、公司名称盖章处须加盖公司公章。公司名称、法人代表名字、公司联系电话，公司名称与</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;font-size:9.0000pt;mso-font-kerning:1.0000pt;"><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal
			style="text-indent:45.0000pt;mso-char-indent-count:5.0000;mso-pagination:widow-orphan;text-align:left;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;font-size:9.0000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">&nbsp;公司公章（专用章、个人章、合同章、部门章等均无效）需要一致</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:9.0000pt;mso-font-kerning:0.0000pt;"><font
				face="微软雅黑">。</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:9.0000pt;mso-font-kerning:0.0000pt;"><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal style="mso-pagination:widow-orphan;">
			<span style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:9.0000pt;mso-font-kerning:0.0000pt;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、</span><span style="mso-spacerun:'yes';font-family:微软雅黑;font-size:9.0000pt;mso-font-kerning:1.0000pt;"><font face="微软雅黑">工电宝注册账号即您下单使用的客户账号。（此处请勿随意改动，如有涂抹修改，请在修改处</font>
			</span>
		</p>
		<p class=MsoNormal
			style="text-indent:45.0000pt;mso-char-indent-count:5.0000;mso-pagination:widow-orphan;text-align:left;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;font-size:9.0000pt;mso-font-kerning:1.0000pt;"><font
				face="微软雅黑">&nbsp;补加公章，否则视为无效）。</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:9.0000pt;mso-font-kerning:0.0000pt;"><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal
			style="mso-char-indent-count:3.5000;mso-pagination:widow-orphan;text-align:left;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;font-size:9.0000pt;mso-font-kerning:1.0000pt;">&nbsp;3、建议提供彩色版本的PDF扫描件或者照片图片JPG档</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:9.0000pt;mso-font-kerning:0.0000pt;"><font
				face="微软雅黑">。</font>
			</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:9.0000pt;mso-font-kerning:0.0000pt;"><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal
			style="text-indent:31.5000pt;mso-char-indent-count:3.5000;mso-pagination:widow-orphan;text-align:left;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;font-size:9.0000pt;mso-font-kerning:1.0000pt;">&nbsp;4、指定付款人信息填写完整，指定付款人需要签字或者盖章。</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;font-size:9.0000pt;mso-font-kerning:1.0000pt;"><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal
			style="text-indent:31.5000pt;mso-char-indent-count:3.5000;mso-pagination:widow-orphan;text-align:left;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;font-size:9.0000pt;mso-font-kerning:1.0000pt;">&nbsp;5、指定付款人可填写多个，需全部签字确认。</span><span
				style="mso-spacerun:'yes';font-family:微软雅黑;font-size:9.0000pt;mso-font-kerning:1.0000pt;"><o:p></o:p>
			</span>
		</p>
		<p class=MsoNormal
			style="text-indent:31.5000pt;mso-char-indent-count:3.5000;mso-pagination:widow-orphan;text-align:left;">
			<span
				style="mso-spacerun:'yes';font-family:微软雅黑;mso-bidi-font-family:宋体;color:rgb(0,0,0);font-size:9.0000pt;mso-font-kerning:0.0000pt;"><o:p>&nbsp;</o:p>
			</span>
		</p>
	</div>
	<!--EndFragment-->
</body>
</html>