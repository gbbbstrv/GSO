package mapreduce;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

import Utils.Calculate;
import Utils.Constants;
import Utils.InputQOS;
import mapreduce.Mainjob.Counter;

public class MidMapjob {
	//萤火虫数据集
	public static ArrayList<List> gsodataset = new ArrayList();
	//map输入的每只萤火位置
	public static List glowwormposition = new ArrayList();
	//缓存中的每只萤火虫位置
	public static List gsodata = new ArrayList();
	
	//缓存中的每只萤火虫的萤光素概率
	public static List lpro = new ArrayList();
	
	public static double distance=0;
	

	public static double l1=0.5;
	public static double l2=0.5;
	public static int i=0;
	public static double F1=5;
	public static double F2=5;
	

	
	 
	   public static List bestv= new ArrayList();
	    public static List curv= new ArrayList();
	    public static List newv= new ArrayList();
	    public static double newJx=0;
	    public static double   newLx=0;
	    public static double  newrd=0;
	    
	public static class Map extends Mapper<LongWritable, Text, Text, Text> {
		
		public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
			String glowwormID="1";
			//邻域集
		   ArrayList<String> neighborsGroup = new ArrayList<String>();
		   ArrayList<List> neighborsGrouplist = new ArrayList<List>();
		   
		   double rd1=0;
		   double rd2=0;
		   
		   double newJx;
			double newL;
			String id1="";
			String id2="";
	
				String line = value.toString(); // 读取源数据
				int length=line.split("\t").length;
                int length1=line.split(",").length;
				if(length<2||length1<2){
					
				}else{
					
				String[] inputvalue=line.split(",");
		
				//种群Id
				id1=inputvalue[0];
				// GSO
				String gso1=inputvalue[1];
				  //计算输入萤火虫的位置
	
				context.write(new Text(id1),new Text(gso1));
				
				
				
			}
		}

		}
	
//    //初始化萤火虫目标函数
//	public static double initObjectValue(List local){
//		List<List<String>> list=Mainjob2.list;
////		System.out.println(list);
//		
//		return Calculate.getObjectValue(list,local);
//	}
//	
//	  //初始化萤火虫萤光素
//		public static double initLuciferinValue(double F,double l){			
//			return (1-Constants.p)*l+Constants.gama*1/F;
//		}



		
	
		
		//计算该位置萤火虫的萤光素概率
		public static List calculateProbability(double l, ArrayList<String> neighborsGroup){
			String[] line;
			double count=0;
			double value=0;
			 List lpro= new ArrayList();
			for(int i=0;i<neighborsGroup.size();i++){
			 line=neighborsGroup.get(i).split(Constants.separators);
			 count=count+ Double.parseDouble(line[Constants.Lindex].toString());
			
			}			
			for(int i=0;i<neighborsGroup.size();i++){
				 line=neighborsGroup.get(i).split(Constants.separators);

				   value=(Double.parseDouble(line[Constants.Lindex].toString())-l1)/(count-l1);
				   lpro.add(value+","+neighborsGroup.get(i).toString());
				}
			return lpro;
		}
		
		//选择萤光素最大的萤火虫位置
		public static String selectBestNeighbor(List lpro){
			String[] line=null;
			String nei=null;
			double max=0;
			int bestindex=0;
			double count=0;
			double ff=0;
			 List ll= new ArrayList();
			 List llnor= new ArrayList();
			if(lpro.size()>0){
			max=Double.parseDouble(lpro.get(0).toString().split(",")[0]);
			nei=lpro.get(0).toString().split(",")[1];
			for(int i=0;i<lpro.size();i++){
				line=lpro.get(i).toString().split(",");
				count=count+Double.parseDouble(line[0]);
				ll.add(Double.parseDouble(line[0]));
//				if(max<Double.parseDouble(line[0])){
//					max=Double.parseDouble(line[0]);
//					nei=line[1];
//				}
				
			}
			double d1 = Math.random();
			for(int i=0;i<ll.size();i++){
				ff=ff+(double)ll.get(i)/count;
				llnor.add(ff);
			}
//			System.out.println(llnor.size());
			for(int i=0;i<llnor.size();i++){
				if(i==0){
					if(d1<=(double)llnor.get(i)){
//						System.out.println(i);
						bestindex=i;
					}
				}else{
					if(d1<=(double)llnor.get(i)&&d1>(double)llnor.get(i-1)){
						bestindex=i;
					}
				}
			}
			
			
			}
			return lpro.get(bestindex).toString().split(",")[1];
//			return nei;
			
		}
		
		

}
