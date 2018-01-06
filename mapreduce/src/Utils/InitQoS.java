package Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class InitQoS {
	public static void main(String[] args) {
		double[][] arr = new double[5][60000];
		int index=0;
        int pingjun = 0;
        DecimalFormat df = new DecimalFormat("#.0000");  
        for(int i = 0; i < arr[0].length; i++){
        	 for(int j = 0; j < arr.length; j++){
        	
        	    if(j==0){
        	    	index=10;
        	    }else if(j==1){
        	    	index=100;
        	    }else if(j==2){
        	    	index=5;
        	    }else if(j==3){
        	    	index=1;
        	    }else{
        	    	index=3;
        	    }
	            arr[j][i] =Double.parseDouble(df.format(Math.random()*index));
	            pingjun += arr[j][i];
        	 }
        }
        java.util.Arrays.sort(arr[0]);
        System.out.println("这五个数是：" + java.util.Arrays.toString(arr[0]));
        
        
        File file = new File("/Users/hpw/Documents/data/WSQoS/QWS");
        FileWriter fw = null;
        BufferedWriter writer = null;
        try {
            fw = new FileWriter(file);
            writer = new BufferedWriter(fw);
             Set set = new HashSet<String>();
            for(int i = 0; i < arr[0].length; i++){
            	   String str="";
			       	 for(int j = 0; j < arr.length; j++){
			       	
			       		str=str+arr[j][i]+"\t";
			       	 }
			       	 set.add(str);
            	
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    
      
	}

}
