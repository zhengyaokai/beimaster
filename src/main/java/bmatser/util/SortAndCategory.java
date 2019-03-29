package bmatser.util;

import java.text.Collator;
import java.util.*;
import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * 〈将List中数据分类排序〉
 *
 * @author YuAfonso
 * @create 2018-10-15
 * @since 1.0.0
 */

public class SortAndCategory {



    public static String toPinyin(String str){
        String convert = "";
        for (int j = 0;j<str.length();j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }
    public static Map<String,Object> toSortAndCategory(List<Map<String,Object>> list){
        List<Map<String,Object>> resultList = new ArrayList<>();
        List<Map<String,Object>> AList = new ArrayList<>();
        List<Map<String,Object>> BList = new ArrayList<>();
        List<Map<String,Object>> CList = new ArrayList<>();
        List<Map<String,Object>> DList = new ArrayList<>();
        List<Map<String,Object>> EList = new ArrayList<>();
        List<Map<String,Object>> FList = new ArrayList<>();
        List<Map<String,Object>> GList = new ArrayList<>();
        List<Map<String,Object>> HList = new ArrayList<>();
        List<Map<String,Object>> IList = new ArrayList<>();
        List<Map<String,Object>> JList = new ArrayList<>();
        List<Map<String,Object>> KList = new ArrayList<>();
        List<Map<String,Object>> LList = new ArrayList<>();
        List<Map<String,Object>> MList = new ArrayList<>();
        List<Map<String,Object>> NList = new ArrayList<>();
        List<Map<String,Object>> OList = new ArrayList<>();
        List<Map<String,Object>> PList = new ArrayList<>();
        List<Map<String,Object>> QList = new ArrayList<>();
        List<Map<String,Object>> RList = new ArrayList<>();
        List<Map<String,Object>> SList = new ArrayList<>();
        List<Map<String,Object>> TList = new ArrayList<>();
        List<Map<String,Object>> UList = new ArrayList<>();
        List<Map<String,Object>> VList = new ArrayList<>();
        List<Map<String,Object>> WList = new ArrayList<>();
        List<Map<String,Object>> XList = new ArrayList<>();
        List<Map<String,Object>> YList = new ArrayList<>();
        List<Map<String,Object>> ZList = new ArrayList<>();
        List<Map<String,Object>> otherList = new ArrayList<>();
        Map<String,Object> resultMap = new HashMap<>();
        for (Map<String,Object> map:list) {
            Object name = map.get("name");
            if(name != null){
               String nameToPY=  toPinyin(name.toString()).toUpperCase().substring(0,1);
               if(nameToPY.equals("A")){
                    AList.add(map);
               }if(nameToPY.equals("B")){
                    BList.add(map);
                }if(nameToPY.equals("C")){
                    CList.add(map);
                }if(nameToPY.equals("D")){
                    DList.add(map);
                }if(nameToPY.equals("E")){
                    EList.add(map);
                }if(nameToPY.equals("F")){
                    FList.add(map);
                }if(nameToPY.equals("G")){
                    GList.add(map);
                }if(nameToPY.equals("H")){
                    HList.add(map);
                }if(nameToPY.equals("I")){
                    IList.add(map);
                }if(nameToPY.equals("J")){
                    JList.add(map);
                }if(nameToPY.equals("K")){
                    KList.add(map);
                }if(nameToPY.equals("L")){
                    LList.add(map);
                }if(nameToPY.equals("M")){
                    MList.add(map);
                }if(nameToPY.equals("N")){
                    NList.add(map);
                }if(nameToPY.equals("O")){
                    OList.add(map);
                }if(nameToPY.equals("P")){
                    PList.add(map);
                }if(nameToPY.equals("Q")){
                    QList.add(map);
                }if(nameToPY.equals("R")){
                    RList.add(map);
                }if(nameToPY.equals("S")){
                    SList.add(map);
                }if(nameToPY.equals("T")){
                    TList.add(map);
                }if(nameToPY.equals("U")){
                    UList.add(map);
                }if(nameToPY.equals("V")){
                    VList.add(map);
                }if(nameToPY.equals("W")){
                    WList.add(map);
                }if(nameToPY.equals("X")){
                    XList.add(map);
                }if(nameToPY.equals("Y")){
                    YList.add(map);
                }if(nameToPY.equals("Z")){
                    ZList.add(map);
                }if(nameToPY.substring(0,1).matches("^[0-9]*$")){
                    otherList.add(map);
                }

            }
        }
        if(AList.size()>0){
            resultMap.put("A",AList);
        }if(BList.size()>0){
            resultMap.put("B",BList);
        }if(CList.size()>0){
            resultMap.put("C",CList);
        }if(DList.size()>0){
            resultMap.put("D",DList);
        }if(EList.size()>0){
            resultMap.put("E",EList);
        }if(FList.size()>0){
            resultMap.put("F",FList);
        }if(GList.size()>0){
            resultMap.put("G",GList);
        }if(HList.size()>0){
            resultMap.put("H",HList);
        }if(IList.size()>0){
            resultMap.put("I",IList);
        }if(JList.size()>0){
            resultMap.put("J",JList);
        }if(KList.size()>0){
            resultMap.put("K",KList);
        }if(LList.size()>0){
            resultMap.put("L",LList);
        }if(MList.size()>0){
            resultMap.put("M",MList);
        }if(NList.size()>0){
            resultMap.put("N",NList);
        }if(OList.size()>0){
            resultMap.put("O",OList);
        }if(PList.size()>0){
            resultMap.put("P",PList);
        }if(QList.size()>0){
            resultMap.put("Q",QList);
        }if(RList.size()>0){
            resultMap.put("R",RList);
        }if(SList.size()>0){
            resultMap.put("S",SList);
        }if(TList.size()>0){
            resultMap.put("T",TList);
        }if(UList.size()>0){
            resultMap.put("U",UList);
        }if(VList.size()>0){
            resultMap.put("V",VList);
        }if(WList.size()>0){
            resultMap.put("W",WList);
        }if(XList.size()>0){
            resultMap.put("X",XList);
        }if(YList.size()>0){
            resultMap.put("Y",YList);
        }if(ZList.size()>0){
            resultMap.put("Z",ZList);
        }if(otherList.size()>0){
            resultMap.put("#",otherList);
        }
        return resultMap;
    }


}
