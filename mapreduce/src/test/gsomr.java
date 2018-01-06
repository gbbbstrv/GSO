package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;  
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;  
import org.apache.hadoop.fs.Path;  
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;  
import org.apache.hadoop.mapreduce.Mapper;  
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import Utils.Calculate;
import Utils.Constants;
import Utils.FileUtils;
import Utils.InputQOS;
import mapreduce.Mainjob;  
  
public class gsomr {  

	public static List<List<String>> list;
	public static Date tempDate=null;
	public static Date start;
//	public static double bestc;
//	public static ArrayList<Double> bestc = new ArrayList<Double>();
//	public static ArrayList<Double> worstc = new ArrayList<Double>();
//	public static ArrayList<Double> bestt = new ArrayList<Double>();
//	public static ArrayList<Double> worstt = new ArrayList<Double>();
	
	public static  HashMap<Integer, Date> hashMap = new HashMap<Integer, Date>();
	
	public static double bestF=100000;
	public static double averge=0;
	public static  HashMap<Integer, Double> bestList = new HashMap<Integer, Double>();
	
	public static  HashMap<Integer, Double> avergeList = new HashMap<Integer, Double>();
	
	public static double bestt;
	public static double bestc;
	public static double worstc;
	public static double worstt;
   public static String T="";
   public static String C="";   
    



    public static class IntSumReducer extends Mapper<LongWritable, Text, Text,Text> {  
    	//萤火虫数据集
    	public static ArrayList<List> gsodataset = new ArrayList();
    	//map输入的每只萤火位置
    	public static List glowwormposition = new ArrayList();
    	public static List gp = new ArrayList();
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
  
       public void map(LongWritable key, Text values, Context context)  
              throws IOException,InterruptedException {  
    		//邻域集
		   ArrayList<String> neighborsGroup = new ArrayList<String>();
		   ArrayList<List> neighborsGrouplist = new ArrayList<List>();
		   
		   double rd1=0;
		   double rd2=0;
		   
		   double newJx;
			double newL;
			String id1="";
			String id2="";
			String newgso=null;
				String line = values.toString(); // 读取源数据
				int length=line.split("\t").length;
                int length1=line.split(",").length;
				if(length<2||length1<2){
					
				}else{
					
				String[] inputvalue=line.split(",");
		
				//种群Id
				id1=inputvalue[0];
				// GSO
				String gso1=inputvalue[1];
				
				//提取萤火虫的位置信息
				glowwormposition=extractPosition(gso1);
				if(length>Constants.dimension){
					//提取萤光素信息
					l1=Calculate.getluciferin(gso1);
					//提取决策半径信息
					rd1=Calculate.getRd(gso1);	
					//提取目标函数信息
					F1=Calculate.getJ(gso1);
				}else{
					//初始化
					 F1 = getObjectValue(list,glowwormposition);
					 rd1=Constants.rd;
			     	 l1=Calculate.getLuciferinValue(Constants.l0,F1);
			     	gso1=gso1+F1+"\t"+l1+"\t"+rd1+"\t";
				}
				//提取萤火虫的位置信息
				glowwormposition=Calculate.extractPosition(gso1);
				
				Path[] paths =DistributedCache.getLocalCacheFiles(context.getConfiguration()); 
	
				FileReader fr=new FileReader(paths[0].toString());
				BufferedReader br=new BufferedReader(fr);
				 String linevalue = br.readLine();
	 

				 
				//从分布式缓存中读取数据并计算萤火虫位置和萤光素
				while(linevalue!=null){
					
					int clength=linevalue.split("\t").length;
					int clength1=linevalue.split(",").length;
					if(clength1<2){
						
					}else{
					String[] allvalue=linevalue.split(",");
					//种群Id
					id2=allvalue[0];
					// GSO
					String gso2=allvalue[1];
//					System.out.println(gso1);
					//计算萤火虫的位置
					gsodata=Calculate.extractPosition(gso2);

					if(clength>Constants.dimension){
						//获得萤火虫的萤光素
						l2=Calculate.getluciferin(gso2);;
						rd2=Calculate.getRd(gso2);	
						F2=Calculate.getJ(gso2);
					}else{
						 F2 = getObjectValue(list,gsodata);
						 rd2=Constants.rd;
						 l2=Calculate.getLuciferinValue(Constants.l0,F2);
				     	
						 gso2=gso2+F2+"\t"+l2+"\t"+rd2+"\t";
					}
					//计算萤火虫的位置
					gsodata=Calculate.extractPosition(gso2);
				
					//计算两萤火虫之间的距离
//					System.out.println(glowwormposition);
//					System.out.println(gsodata);
					distance=Calculate.returnEDistance(glowwormposition,gsodata);
//					System.out.println(distance);

					if(distance<rd1&&F1>F2&&id1.equals(id2)){
						neighborsGroup.add(gso2);
						neighborsGrouplist.add(gsodata);
					}
					
					}
					linevalue = br.readLine();
				}
//				System.out.println(gsodataset.get(21).get(1));
				
//				System.out.println(neighborsGroup.size());

				
				//如果存在领域集，则计算领域集里每个个体的概率
				if(neighborsGroup.size()>0){
//					lpro=calculateProbability(l1,neighborsGroup);
//					lpro=calculateProbabilityF(F1,neighborsGroup);
////					//选择萤光素最大的萤火虫
//					newgso=selectBestNeighbor(lpro);
					double minF=10000;
					int indexF=0;
					for(int i=0;i<neighborsGroup.size();i++){
						 String[] line1=neighborsGroup.get(i).split(Constants.separators);
						if(minF<Double.parseDouble(line1[Constants.Findex].toString())){
							minF=Double.parseDouble(line1[Constants.Findex].toString());
							indexF=i;
						}
					}
					newgso=neighborsGroup.get(indexF).toString();
					
					bestv=Calculate.extractPosition(newgso);
					//萤火虫位置更新
					newv=Calculate.calculateNewX(glowwormposition,bestv);
//					System.out.println(curv);
//					System.out.println(bestv);
//					System.out.println(newv);
		
					
					//更新新的目标函数
					newJx=getObjectValue(list,newv);
//					newJx=0;
//					
//					
					//更新新的萤光素
					newL=Calculate.getLuciferinValue(l1,newJx);

					
					//更新新的决策半径

					double newrd=Calculate.getNewrd(rd1,neighborsGroup.size());

					String result="";
					
					for(int j=0;j<newv.size();j++){
						result=result+newv.get(j)+"\t";
					}
			
					context.write(new Text(id1),new Text(result+newJx+"\t"+newL+"\t"+newrd+"\t"+neighborsGroup.size()+"\t"));
					
					
					
				}else{
					newgso=gso1;
	                String result1="";
					if(F1>bestF){
						for(int j=0;j<Constants.dimension;j++){
							result1=result1+(new java.util.Random().nextInt(Constants.Height)+1)+"\t";
							
						}
						gp=Calculate.extractPosition(result1);
						 double FF = getObjectValue(list,gp);

						context.write(new Text(id1),new Text(result1+FF+"\t"+Calculate.getLuciferinValue(Constants.l0,FF)+"\t"+Constants.rd+"\t"+neighborsGroup.size()+"\t"));

					}else{
					for(int j=0;j<Constants.dimension;j++){
						result1=result1+glowwormposition.get(j)+"\t";
					}
					context.write(new Text(id1),new Text(result1+F1+"\t"+l1+"\t"+Constants.rd+"\t"+neighborsGroup.size()+"\t"));
					}
				}

				
			
//				i++;	
//				context.write(new Text(i+""), new Text(newgso+"#"+neighborsGroup.size())); //输出
//				context.write(new Text(i+""), new Text(line)); //输出
				
				
			}
		
       }  
    }  
  
    public static class job1Reducer extends  
          Reducer<Text, Text, Text,Text> {  
    	public static double minF=100000; 
    	   public static List bestv= new ArrayList();
   	    public static List curv= new ArrayList();
   	    public static List newv= new ArrayList();
   	    
   	    public static double l1=0.5;
   		public static double l2=0.5;
   		public static int i=0;
   		public static double F1=5;
   		public static double F2=5;
   		
   		public static double rd1=0;
   		public static double rd2=0;
       public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {  
   		String valueString="";
		int i=0;
		double rd;
		double newJx;
		double newL;
		double l;
		String newvalue = null;
		int nbSize=0;
		double average=0;
		double count1 =0;

		for (Text value : values) {
		valueString = key+","+value.toString();
		F1=Calculate.getJ(valueString);
		if(F1<bestF){
			bestF=F1;
			
		}
		average=average+F1;
		count1=count1+1;
		context.write(new Text(valueString),null);

		}
		
//		Mainjob.averge=average/count;
		
			averge=average/count1;
			minF=average/count1;
		
       }

    }
  
    public static void main(String[] args) throws Exception {  
    	
    	
          Configuration conf = new Configuration();  
//           String[] otherArgs = new GenericOptionsParser(conf, args)  
//                 .getRemainingArgs();  
//           if(otherArgs.length != 2) {  
//              System.err.println("Usage:wordcount <in><out>");  
//              System.exit(2);  
//           }  
//           String input = otherArgs[0];  
//           String output = otherArgs[1];  
           String input = "/Users/hpw/Documents/data/input"; 
           String output1 = "/Users/hpw/Documents/data/output/";
           String output="";
           String p="/Users/hpw/Documents/data/output";
   		File file=new File(p);
   		FileUtils.deleteDir(file);
           FileSystem fs;  
           
   
           
      	 list =InputQOS.getQOS();
      	 T=InputQOS.BestTime();
           C=InputQOS.BestCost();
           bestc=Double.parseDouble(C.split(";")[0].toString());
           worstc=Double.parseDouble(C.split(";")[1].toString());
           bestt=Double.parseDouble(T.split(";")[0].toString());
           worstt=Double.parseDouble(T.split(";")[1].toString());
           System.out.println(bestc);
           System.out.println(worstc);
           System.out.println(bestt);
           System.out.println(worstt);
           Date start = new Date();
  
           try{  
              fs =FileSystem.get(conf);  
              int step = Constants.iterTime;  
              for(int i = 0; i< step; i++) {  
            	  
            	  if(i==0){
                 	 Path centersFile1=new Path(input+"/GSODATA");  //  the new centers file  
        	            DistributedCache.addCacheFile(centersFile1.toUri(), conf); 
                  }else{
  	            // set the centers data file  
  	            Path centersFile1=new Path(output1+"temp"+(i-1)+"/part-r-00000");  //  the new centers file  
  	            DistributedCache.addCacheFile(centersFile1.toUri(), conf);  
                  }
            
            	  
                 System.out.println("第" + i + "次：" + i);  
                 output =output1+"temp"+i;  
                 Job job = new Job(conf, "word count");  
                 job.setJarByClass(gsomr.class);  
                   
               
                 
                 job.setMapperClass(IntSumReducer.class);  
                 job.setReducerClass(job1Reducer.class);  
                   
                job.setMapOutputKeyClass(Text.class);  
                 job.setMapOutputValueClass(Text.class);  
                   
                 job.setOutputKeyClass(Text.class);  
                 job.setOutputValueClass(Text.class);  
                   
                 FileInputFormat.addInputPath(job, new Path(input));  
                 FileOutputFormat.setOutputPath(job, new Path(output));  
                   
                 job.waitForCompletion(true);  
                 input = output;   
                 Date time = new Date();
                 bestList.put(i, bestF);
                 hashMap.put(i, time);
                 avergeList.put(i, averge);
                 
             
                 
              }
      		// 记录开始时间
      		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
              Date end = new Date();
      		float time = (float) ((end.getTime() - start.getTime()) / 1000);
      		
    		System.out.println("任务开始：" + formatter.format(start));
    		System.out.println("任务结束：" + formatter.format(end));
    		System.out.println("任务耗时：" + String.valueOf(time) + " 秒");
    		
  		  Iterator it = hashMap.keySet().iterator();  
 		 while(it.hasNext()) {  
 	            Integer key = (Integer) it.next();  
 	            System.out.println(key.toString()+" "+(float) ((hashMap.get(key).getTime() - start.getTime()) / 1000));  
  
 	        }  
    		
    		  Iterator it1 = bestList.keySet().iterator();  
 			 while(it1.hasNext()) {  
 		            Integer key = (Integer) it1.next();  
 		            System.out.println(key.toString()+" "+bestList.get(key).toString());  
 	 
 		        }  
 			 
 			  Iterator it2 = avergeList.keySet().iterator();  
  			 while(it2.hasNext()) {  
  		            Integer key = (Integer) it2.next();  
  		            System.out.println(key.toString()+" "+avergeList.get(key).toString());  
  	 
  		        } 
           } catch(IOException e) {  
              e.printStackTrace();  
           } catch(InterruptedException e) {  
              e.printStackTrace();  
           } catch(ClassNotFoundException e) {  
              e.printStackTrace();  
           }  
       } 

    public static double getObjectValue(List<List<String>> list,List local){
		double cc=0;
		double tc=0;
		double value_cc=0;
		double value_tc=0;
		for(int i=0;i<Constants.dimension;i++){
			value_cc+=Double.parseDouble(list.get(getGSOQOS(i,Integer.parseInt(local.get(i).toString()))).get(Constants.Cindex));
			value_tc+=Double.parseDouble(list.get(getGSOQOS(i,Integer.parseInt(local.get(i).toString()))).get(Constants.Tindex));

		}
		return Math.sqrt(Math.pow(value_cc-bestc,2)+Math.pow(value_tc-bestt,2));
	}
    
	//获得萤火虫对应的QOS
	public static int getGSOQOS(int index,int value){
		Long[] ser=Constants.ser_num();
		int resule=value;
		for(int i=1;i<=index;i++){
			resule=resule+ser[i-1].intValue();
		}
		return resule-1;
		
	}
	//计算萤火虫的位置
	public static List extractPosition(String glowwormId){
		List gsodimension = new ArrayList();
		String[] line=glowwormId.split(Constants.separators);
		for(int j=0;j<Constants.dimension;j++){
			gsodimension.add(line[j]);
		}	
		return gsodimension;
	}
}  