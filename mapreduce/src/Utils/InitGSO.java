package Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class InitGSO {
	public static void main(String[] args) {
		int[][] arr = new int[Constants.dimension][Constants.num];
        int pingjun = 0;
        for(int i = 0; i < arr.length; i++){
        	 for(int j = 0; j < arr[0].length; j++){
	            arr[i][j] = new java.util.Random().nextInt(Constants.Height)+1;
	            pingjun += arr[i][j];
        	 }
        }
        java.util.Arrays.sort(arr[0]);
//        System.out.println("这五个数是：" + java.util.Arrays.toString(arr[0]));
        
        
        File file = new File("/Users/hpw/Documents/data/input/GSODATA");
        FileWriter fw = null;
        BufferedWriter writer = null;
        try {
            fw = new FileWriter(file);
            writer = new BufferedWriter(fw);
             Set set = new HashSet<String>();
            for(int i = 0; i < arr[0].length; i++){
            	for(int k=1;k<=Constants.gsok;k++){
			        	String str=k+",";
			       	 for(int j = 0; j < arr.length; j++){
			       	
			       		str=str+arr[j][i]+"\t";
			       	 }
			       	 set.add(str);
            	}
           }
            
            Iterator iterator = set.iterator();
            
            
            while(iterator.hasNext()){
                writer.write(iterator.next().toString());
                writer.newLine();//换行
            }
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                writer.close();
                fw.close();
                System.out.println("萤火虫初始化已完成");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    
      
	}

}
