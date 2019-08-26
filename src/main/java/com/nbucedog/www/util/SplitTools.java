package com.nbucedog.www.util;

import java.util.ArrayList;
import java.util.List;

public class SplitTools {

    private static final String imgType = "jpg"+"jpeg"+"png"+"bmp"+"gif";
    private static final String videoType = "mp4"+"avi"+"wmv"+"mkv"+"mov"+"mpeg";

    /**
     * 文件名分割
     * @param fileName 文件名
     * @return result
     */
    public static String[] fileNameSplit(String fileName){
        String[] result =new String[]{null,null,null};
        try {
            String[] firstSplit = fileName.split("\\.");
            String[] secondSplit = firstSplit[0].split("_");
            if(isLetterOrDigits(secondSplit[0])){
                result[0] = secondSplit[0];
                result[1] = secondSplit[1];
                result[2] = getTypeFolder(firstSplit[firstSplit.length-1]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static String insertNum2Str(String str,Integer value){
        String valueStr = String.valueOf(value);
        if(str==null){
            return valueStr;
        }
        String[] split_ = str.split("_");
        String[] splitAll = str.split("_|-");
        int distance = value-Integer.valueOf(splitAll[0]);
        int index=0;
        if(distance<0){
            index=-1;
        }
        for(int i=0;i<split_.length;i++){
            if(isDigit(split_[i])){
                Integer itemInt = Integer.valueOf(split_[i]);
                if(split_[i].equals(valueStr)){
                    return str;
                }
                int nowDistance = value-itemInt;
                if(nowDistance>0 && nowDistance<distance){
                    distance=nowDistance;
                    index=i;
                }
            }
            else if(split_[i].contains("-")){
                String[] split = split_[i].split("-");
                if(value>=Integer.valueOf(split[0]) && value<=Integer.valueOf(split[1])){
                    return str;
                }
                int nowDistance=value-Integer.valueOf(split[1]);
                if(nowDistance>0 && nowDistance<distance){
                    distance=nowDistance;
                    index=i;
                }
            }
        }
        String result;
        if(index==-1){
            result=insetPreNumStr(value,split_[0]);
            for (int i=1;i<split_.length;i++){
                result=result + "_"+split_[i];
            }
        }
        else if(index==split_.length-1){
            String resultItem=insertNextNumStr(split_[index],value);
            if(index==0){
                result = resultItem;
            }else {
                result=split_[0];
                for (int i=1;i<split_.length-1;i++){
                    result=result + "_"+split_[i];
                }
                result=result+"_"+resultItem;
            }
        }else {
            String resultItem=insertMidNumStr(split_[index],value,split_[index+1]);
            if(index==0){
                result=resultItem;
                for(int i=index+2;i<split_.length;i++){
                    result=result + "_"+split_[i];
                }
            }else {
                result=split_[0];
                for (int i=1;i<index;i++){
                    result=result + "_"+split_[i];
                }
                result=result+"_"+resultItem;
                for(int i=index+2;i<split_.length;i++){
                    result=result + "_"+split_[i];
                }
            }
        }
        return result;
    }

    private static String insetPreNumStr(Integer pre,String mid){
        String result;
        String preStr=String.valueOf(pre);
        int distance=Integer.valueOf(mid.split("-")[0])-pre;
        String midTail;
        if(mid.contains("-")){
            String[] split = mid.split("-");
            midTail=split[1];
        }else {
            midTail=mid;
        }
        if(distance==1){
            result=preStr+"-"+midTail;
        }else {
            result=preStr+"_"+mid;
        }
        return result;
    }

    private static String insertMidNumStr(String pre,Integer mid,String next){
        String result;
        String midStr=String.valueOf(mid);
        int preDistance;
        int nextDistance;
        String preHead;
        String nextTail;
        if(pre.contains("-")){
            String[] split = pre.split("-");
            preDistance=mid-Integer.valueOf(split[1]);
            preHead=split[0];
        }else {
            preDistance=mid-Integer.valueOf(pre);
            preHead=pre;
        }
        if(next.contains("-")){
            String[] split = next.split("-");
            nextDistance=Integer.valueOf(split[0])-mid;
            nextTail=split[1];
        }else {
            nextDistance=Integer.valueOf(next)-mid;
            nextTail=next;
        }
        if(preDistance==1&&nextDistance==1){
            result=preHead+"-"+nextTail;
        }else if(preDistance==1){
            result=preHead+"-"+midStr+"_"+next;
        }else if(nextDistance==1){
            result=pre+"_"+midStr+"-"+nextTail;
        }else {
            result=pre+"_"+midStr+"_"+next;
        }
        return result;
    }

    private static String insertNextNumStr(String mid,Integer next){
        String result;
        String nextStr=String.valueOf(next);
        int distance;
        String midHead=mid.split("-")[0];
        if(mid.contains("-")){
            distance=next-Integer.valueOf(mid.split("-")[1]);
        }else {
            distance=next-Integer.valueOf(mid);
        }
        if(distance==1){
            result=midHead+"-"+next;
        }else {
            result=mid+"_"+nextStr;
        }
        return result;
    }

    public static List<Integer> splitString2IntList(String value){
        List<Integer> result = new ArrayList<>();
        String[] split_ = value.split("_");
        for (String item:split_){
            if(isDigit(item)){
                result.add(Integer.valueOf(item));
            }
            else if(item.contains("-")){
                result.addAll(splitNumber2number(item));
            }
        }
        return result;
    }

    public static List<Integer> splitNumber2number(String value){
        List<Integer> result = new ArrayList<>();
        String[] split = value.split("-");
        Integer start = Integer.valueOf(split[0]);
        Integer end = Integer.valueOf(split[1]);
        for(Integer i=start;i<=end;i++){
            result.add(i);
        }
        return result;
    }

    public static String combineIntList2Str(List<Integer> integerList){
        List<String> stringList = new ArrayList<>();
        String result=null;
        int end=integerList.size();
        for(int i=0;i<end;i++){
            if(i!=0&&i!=end-1){
                Integer pre = integerList.get(i-1);
                Integer now = integerList.get(i);
                Integer next = integerList.get(i+1);
                if(now-pre==1&&next-now==1){
                    stringList.add("-");
                }else{
                    stringList.add(String.valueOf(integerList.get(i)));
                }
            }else if(i==0){
                stringList.add(String.valueOf(integerList.get(i)));
            }
            else {
                stringList.add(String.valueOf(integerList.get(i)));
            }
        }
        for(int i=0;i<stringList.size();i++){
            if(i==0){
                result=stringList.get(i);
            }
            else if(stringList.get(i).equals("-") && !stringList.get(i-1).equals("-")){
                result=result + "-";
            }else if(!stringList.get(i).equals("-") && !stringList.get(i-1).equals("-")){
                result=result + "_" +stringList.get(i);
            }else if(!stringList.get(i).equals("-") && stringList.get(i-1).equals("-")){
                result = result + stringList.get(i);
            }
        }
        return result;
    }

    private static String getTypeFolder(String fileType){
        String type = fileType.toLowerCase();
        if(type.equals("pdf")){
            return "pdf";
        }else if(imgType.contains(type)){
            return "img";
        }else if(videoType.contains(type)){
            return "video";
        }else {
            return "unknown";
        }

    }

    /**
     * 判断型号是否只有数字或字母
     * @param code 型号
     */
    private static boolean isLetterOrDigits(String code){
        for(int i=0;i<code.length();i++){
            if (!(Character.isLowerCase(code.charAt(i)) || Character.isUpperCase(code.charAt(i)) || Character.isDigit(code.charAt(i)))) {
                return false;
            }
        }
        return true;
    }
    private static boolean isDigit(String code){
        for(int i=0;i<code.length();i++){
            if (!Character.isDigit(code.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
