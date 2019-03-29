package bmatser.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

public class Analyzer {

	public static void main(String[] args) throws IOException {  

		String content = "IK Analyzer是一个开源的，基于java语言" +
						"开发的轻量级的中文分词工具包。从2006年12月推出1.0版开始， " +
						"IKAnalyzer已经推出了4个大版本。最初，它是以开源项目Luence为" +
						"应用主体的，结合词典分词和文法分析算法的中文分词组件。从3.0版" +
						"本开始，IK发展为面向Java的公用分词组件，独立于Lucene项目，同时" +
						"提供了对Lucene的默认优化实现。";
		System.out.println(IKAnalyzer(content));
    }
	
	public static List<String> IKAnalyzer(String content){
		if(StringUtils.isBlank(content)){
			return null;
		}
		List<String> list = new ArrayList<String>();
		IKSegmentation ikSeg = new IKSegmentation(new StringReader(content), true);
		Lexeme l = null;
		try {
			while ((l = ikSeg.next()) != null) {
			    String word = l.getLexemeText();
			    list.add(word.toUpperCase());
			}
		} catch (IOException e) {
			return null;
		}
		return list;
	}
}
