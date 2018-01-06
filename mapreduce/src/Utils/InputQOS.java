package Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class InputQOS {
   
	public static List<List<String>> getQOS() throws IOException{
		
		 List<List<String>> list = new ArrayList();
		//输入QOS值	
		FileReader frqws;
		try {
//			frqws = new FileReader(Constants.QWS);
//	
//		BufferedReader brqws=new BufferedReader(frqws);
//		
		
		 Path file=new Path(Constants.QWS); 
		 Configuration conf=new Configuration();  
		 FileSystem fs=FileSystem.get(file.toUri(),conf); 
		 FSDataInputStream in=fs.open(file);   
            InputStreamReader istr=new InputStreamReader(in);  
            BufferedReader brqws=new BufferedReader(istr);  
		
		
		 String linevalueqws = brqws.readLine();
		 
		
		int k=0;
		 
			while(linevalueqws!=null&&k<Constants.SCount){
//				System.out.println(linevalueqws);
				 List str= new ArrayList();
				String[] value=linevalueqws.split("\t");
				int le=value.length;
				for(int i=0;i<le;i++){
					str.add(value[i]);
				}
				list.add(str);
				k++;
				linevalueqws = brqws.readLine();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	public static List<List<String>> getGSO(int index) throws IOException{
		
		 List<List<String>> list = new ArrayList();
		//输入QOS值	
		FileReader frqws;
		try {
//			frqws = new FileReader(Constants.QWS);
//	
//		BufferedReader brqws=new BufferedReader(frqws);
		Path file;
		if(index==0){
			  file=new Path("/Users/hpw/Documents/data/input/GSODATA"); 
		}else{
		  file=new Path("/Users/hpw/Documents/data/output/temp"+index+"/part-r-00000"); 
		}
		 Configuration conf=new Configuration();  
		 FileSystem fs=FileSystem.get(file.toUri(),conf); 
		 FSDataInputStream in=fs.open(file);   
           InputStreamReader istr=new InputStreamReader(in);  
           BufferedReader brqws=new BufferedReader(istr);  
		
		
		 String linevalueqws = brqws.readLine();
		 
		
		int k=0;
		 
			while(linevalueqws!=null&&k<Constants.SCount){
//				System.out.println(linevalueqws);
				 List str= new ArrayList();
				String[] value=linevalueqws.split("\t");
				int le=value.length;
				for(int i=0;i<le;i++){
					str.add(value[i]);
				}
				list.add(str);
				k++;
				linevalueqws = brqws.readLine();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public static String BestCost() {
		//输入QOS值	
		FileReader frqws;
		 
		 List lc= new ArrayList();
		 List lt= new ArrayList();
		 
		try {
//			frqws = new FileReader(Constants.QWS);
//		
//		BufferedReader brqws=new BufferedReader(frqws);
		
		 Path file=new Path(Constants.QWS); 
		 Configuration conf=new Configuration();  
		 FileSystem fs=FileSystem.get(file.toUri(),conf); 
		 FSDataInputStream in=fs.open(file);   
            InputStreamReader istr=new InputStreamReader(in);  
            BufferedReader brqws=new BufferedReader(istr);  
		 String linevalueqws = brqws.readLine();
		 int k=0;
			while(linevalueqws!=null&&k<Constants.SCount){
//				System.out.println(linevalueqws);
				String cost=linevalueqws.split("\t")[Constants.Cindex];
		
				lc.add(cost);
				k++;
				linevalueqws = brqws.readLine();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return bestpoint(lc)+";"+worstpoint(lc);
	}
	
	public static String BestTime() {
		//输入QOS值	
		FileReader frqws;
		 
		 List lc= new ArrayList();
		 List lt= new ArrayList();
		try {
//			frqws = new FileReader(Constants.QWS);
//		
//		BufferedReader brqws=new BufferedReader(frqws);
			
			 Path file=new Path(Constants.QWS); 
			 Configuration conf=new Configuration();  
			 FileSystem fs=FileSystem.get(file.toUri(),conf); 
			 FSDataInputStream in=fs.open(file);   
	            InputStreamReader istr=new InputStreamReader(in);  
	            BufferedReader brqws=new BufferedReader(istr);  
			
		 String linevalueqws = brqws.readLine();
		 int k=0;
		
			while(linevalueqws!=null&&k<Constants.SCount){
//				System.out.println(linevalueqws);
			
				String time=linevalueqws.split("\t")[Constants.Tindex];

				lt.add(time);
				k++;
				linevalueqws = brqws.readLine();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return bestpoint(lt)+";"+worstpoint(lt);
	}
	//求正理想点
			public static String bestpoint(List va){
				
				double minstr=0;
				 Long[] ser=Constants.ser_num();
				 double min=100;
				 int index=0;
				 int lastindex=ser[0].intValue();
				 List bestlist = new ArrayList();
				 StringBuffer sb = new StringBuffer();  
				 for(int j=0;j<ser.length-1;j++){
					  min=100;
					for(int i=index;i<lastindex;i++){
						if(Double.parseDouble((String) va.get(i))<min){
							min=Double.parseDouble((String) va.get(i));
						}
					}
					index=index+ser[j].intValue();
					lastindex=index+ser[j+1].intValue();
					minstr=minstr+min;
//					sb.append(min);
//					sb.append("#");
//					bestlist.add(min);
					
				 }
				 
				  min=100;
					for(int i=index;i<lastindex;i++){
						if(Double.parseDouble((String) va.get(i))<min){
							min=Double.parseDouble((String) va.get(i));
						}
					}

					minstr=minstr+min;
//					bestlist.add(min);
//					sb.append(min);
//				return sb.toString();
					return minstr+"";
			}
			
			//负求理想点
			public static String worstpoint(List va){
				
				double maxstr=0;
				 Long[] ser=Constants.ser_num();
				 double max=100;
				 int index=0;
				 int lastindex=ser[0].intValue();
				 StringBuffer sb = new StringBuffer();  
				 for(int j=0;j<ser.length-1;j++){
					  max=0;
					for(int i=index;i<lastindex;i++){
						if(Double.parseDouble((String) va.get(i))>max){
							max=Double.parseDouble((String) va.get(i));
						}
					}
					index=index+ser[j].intValue();
					lastindex=index+ser[j+1].intValue();
					maxstr=maxstr+max;
//					sb.append(max);
//					sb.append("#");
				 }
				 
				 max=100;
					for(int i=index;i<lastindex;i++){
						if(Double.parseDouble((String) va.get(i))>max){
							max=Double.parseDouble((String) va.get(i));
						}
					}

					maxstr=maxstr+max;
//					sb.append(max);
//				return sb.toString();
					return maxstr+"";
			}
}
