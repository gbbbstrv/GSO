package mapreduce;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.commons.logging.LogFactory;  
import org.apache.commons.logging.Log;  
//import org.apache.commons.logging.LogFactory;  

import Utils.Constants;
import Utils.FileUtils;
import Utils.InputQOS;
import mapreduce.Mapjob.Map;
import mapreduce.Reducejob.Reduce;


public class Mainjob extends Configured implements Tool {
	enum Counter {
	LINESKIP, // 出错的行
	}
	
//	public static String input="hdfs://localhost:8020/Users/hpw/hadoop-1.2.1/tmp/hadoop-hpw/data/input";
	public static String input="/Users/hpw/Documents/data/input";
//	public static String output1="/Users/hpw/Documents/data/output/result";
//	public static String output2="/Users/hpw/Documents/data/output/result2";
//	public static String output3="/Users/hpw/Documents/data/output/result3";
	public static String temp_path="/Users/hpw/Documents/data/output/temp";

//	public static String temp_path="hdfs://localhost:8020/Users/hpw/hadoop-1.2.1/tmp/hadoop-hpw/data/output/temp";
	
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
	public static double averge=100000;
	public static  HashMap<Integer, Double> bestList = new HashMap<Integer, Double>();
	
	public static  HashMap<Integer, Double> avergeList = new HashMap<Integer, Double>();
	
	public static double bestt;
	public static double bestc;
	public static double worstc;
	public static double worstt;
   public static String T="";
   public static String C="";   
	    private static Log log=LogFactory.getLog(Mainjob.class); 

	@Override
	public int run(String[] args) throws Exception {
		
       
         list =InputQOS.getQOS();
//		 System.out.println(list);
         T=InputQOS.BestTime();
         C=InputQOS.BestCost();
//         for(int i=0;i<C.split(";")[0].split("#").length;i++){
//        	 Constants.bestc.add(Double.parseDouble(C.split(";")[0].split("#")[i]));
//        	 Constants.worstc.add(Double.parseDouble(C.split(";")[1].split("#")[i]));
//        	 Constants.bestt.add(Double.parseDouble(T.split(";")[0].split("#")[i]));
//        	 Constants. worstt.add(Double.parseDouble(T.split(";")[1].split("#")[i]));
//         }
//         for(int i=0;i<C.split(";")[1].split("#").length;i++){
//        	 worstc.add(Double.parseDouble(C.split(";")[1].split("#")[i]));
//         }
//         for(int i=0;i<C.split(";")[1].split("#").length;i++){
//        	 bestt.add(Double.parseDouble(T.split(";")[0].split("#")[i]));
//         }
//         for(int i=0;i<C.split(";")[1].split("#").length;i++){
//        	 worstt.add(Double.parseDouble(T.split(";")[1].split("#")[i]));
//         }
         bestc=Double.parseDouble(C.split(";")[0].toString());
         worstc=Double.parseDouble(C.split(";")[1].toString());
         bestt=Double.parseDouble(T.split(";")[0].toString());
         worstt=Double.parseDouble(T.split(";")[1].toString());
         System.out.println(bestc);
         System.out.println(worstc);
         System.out.println(bestt);
         System.out.println(worstt);
		List<Configuration>	 conflist = null;
		List<Job>	 Joblist = null;
		
		start = new Date();
		 Configuration conf = new Configuration();
//		DefaultStringifier.store(conf, list ,"List");
	
		Job job = new Job(conf, "Main"); // 任务名
		job.setJarByClass(Mainjob.class); // 指定Class
	
		job.setNumReduceTasks(10);
		String cacheFilePath=input+"/GSODATA";
		DistributedCache.addCacheFile(new Path(cacheFilePath).toUri(), job.getConfiguration());
		
		FileInputFormat.addInputPath(job, new Path(input)); // 输入路径
		FileOutputFormat.setOutputPath(job,  new Path(temp_path+0+"/")); // 输出路径
		
		job.setMapperClass(Mapjob.Map.class); // 调用上面Map类作为Map任务代码
		job.setReducerClass(Reducejob.Reduce.class); // 调用上面Reduce类作为Reduce任务代码
		
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setOutputKeyClass(Text.class); // 指定输出的KEY的格式
		job.setOutputValueClass(Text.class); // 指定输出的VALUE的格式
		 tempDate = new Date();
         hashMap.put(Constants.iterNum, tempDate);
         bestList.put(Constants.iterNum, bestF);
         avergeList.put(Constants.iterNum, averge);
		 if(!job.waitForCompletion(true)){  
	            System.exit(1); // run error then exit  
	        }  
//		job.waitForCompletion(true);
//	
		
		 //  do iteration  
        boolean flag=true;  
        while(flag&&Constants.iterNum<Constants.iterTime){  
            Configuration conf1=new Configuration();  
              
            // set the centers data file  
            Path centersFile1=new Path(temp_path+(Constants.iterNum-1)+"/part-r-00000");  //  the new centers file  
//           System.out.println(centersFile1);
            
            DistributedCache.addCacheFile(centersFile1.toUri(), conf1);  
            boolean iterflag=doIteration(conf1,Constants.iterNum);  
            if(!iterflag){  
                log.error("job fails");  
                System.exit(1);  
            }  
            //  set the flag based on the old centers and the new centers  
              
            Path oldCentersFile=new Path(temp_path+(Constants.iterNum-1)+"/part-r-00000");  
            Path newCentersFile=new Path(temp_path+Constants.iterNum+"/part-r-00000");  
            FileSystem fs1=FileSystem.get(oldCentersFile.toUri(),conf1);  
            FileSystem fs2=FileSystem.get(oldCentersFile.toUri(),conf1);  
            if(!(fs1.exists(oldCentersFile)&&fs2.exists(newCentersFile))){  
                log.info("the old centers and new centers should exist at the same time");  
                System.exit(1);  
            }  
            String line1,line2;  
            FSDataInputStream in1=fs1.open(oldCentersFile);  
            FSDataInputStream in2=fs2.open(newCentersFile);  
            InputStreamReader istr1=new InputStreamReader(in1);  
            InputStreamReader istr2=new InputStreamReader(in2);  
            BufferedReader br1=new BufferedReader(istr1);  
            BufferedReader br2=new BufferedReader(istr2);  
            double error=0.0;  
            while((line1=br1.readLine())!=null&&((line2=br2.readLine())!=null)){  
                String[] str1=line1.split(",")[1].split("\t");  
                String[] str2=line2.split(",")[1].split("\t");  
                for(int i=0;i<Constants.dimension;i++){  
                    error+=(Double.parseDouble(str1[i])-Double.parseDouble(str2[i]))*(Double.parseDouble(str1[i])-Double.parseDouble(str2[i]));  
                }  
           
            } 
            
            if(error<Constants.threadHold){  
                flag=false;  
            }  
            tempDate = new Date();
            System.out.println(String.valueOf(Constants.iterNum+";"+((tempDate.getTime() - start.getTime()) )) + " 秒");
            hashMap.put(Constants.iterNum, tempDate);
            bestList.put(Constants.iterNum, bestF);
            avergeList.put(Constants.iterNum, averge);
            Constants.iterNum++;  
              
        } 
		

//	
		
		   Configuration conf2=new Configuration();  
	        // set the centers data file  
	        Path centersFile2=new Path(temp_path+(Constants.iterNum-1)+"/part-r-00000");  //  the new centers file  
	        DistributedCache.addCacheFile(centersFile2.toUri(), conf2);  
	        midJob(conf2,Constants.iterNum);  
	        tempDate = new Date();
            hashMap.put(Constants.iterNum, tempDate);
//	        System.out.println(iterNum);   
	        
	        boolean flag1=true;  
	        while(flag1&&Constants.merge_iterNum<Constants.merge_iterTime){  
	            Configuration conf3=new Configuration();  
	              
	            // set the centers data file  
	            Path centersFile1=new Path(temp_path+(Constants.iterNum-1+Constants.merge_iterNum)+"/part-r-00000");  //  the new centers file  
//	           System.out.println(centersFile1);
	            
	            DistributedCache.addCacheFile(centersFile1.toUri(), conf3);  
	            boolean iterflag=doIterationmerge(conf3,Constants.iterNum+Constants.merge_iterNum);  
	            if(!iterflag){  
	                log.error("job fails");  
	                System.exit(1);  
	            }  
	            //  set the flag based on the old centers and the new centers  
	              
	            Path oldCentersFile=new Path(temp_path+(Constants.iterNum-1+Constants.merge_iterNum)+"/part-r-00000");  
	            Path newCentersFile=new Path(temp_path+(Constants.iterNum+Constants.merge_iterNum)+"/part-r-00000");  
	            FileSystem fs1=FileSystem.get(oldCentersFile.toUri(),conf3);  
	            FileSystem fs2=FileSystem.get(oldCentersFile.toUri(),conf3);  
	            if(!(fs1.exists(oldCentersFile)&&fs2.exists(newCentersFile))){  
	                log.info("the old centers and new centers should exist at the same time");  
	                System.exit(1);  
	            }  
	            String line1,line2;  
	            FSDataInputStream in1=fs1.open(oldCentersFile);  
	            FSDataInputStream in2=fs2.open(newCentersFile);  
	            InputStreamReader istr1=new InputStreamReader(in1);  
	            InputStreamReader istr2=new InputStreamReader(in2);  
	            BufferedReader br1=new BufferedReader(istr1);  
	            BufferedReader br2=new BufferedReader(istr2);  
	            double error=0.0;  
	            if(Constants.merge_iterNum>1){
		            while((line1=br1.readLine())!=null&&((line2=br2.readLine())!=null)){  
		                String[] str1=line1.split(",")[1].split("\t");  
		                String[] str2=line2.split(",")[1].split("\t");  
		                for(int i=0;i<Constants.dimension;i++){  
		                    error+=(Double.parseDouble(str1[i])-Double.parseDouble(str2[i]))*(Double.parseDouble(str1[i])-Double.parseDouble(str2[i]));  
		                }  
		                
		         
		            }  
		            if(error<Constants.threadHold){  
		                flag1=false;  
		            }  
	            }
	            tempDate = new Date();
	            hashMap.put(Constants.iterNum+Constants.merge_iterNum, tempDate);
	            bestList.put(Constants.iterNum+Constants.merge_iterNum, bestF);
	            avergeList.put(Constants.iterNum+Constants.merge_iterNum, averge);
	            Constants.merge_iterNum++;  
	              
	        } 
	        
	    	// 输出任务完成情况
//			System.out.println("任务名称：" + job.getJobName());
//			System.out.println("任务成功：" + (job.isSuccessful() ? "是" : "否"));
//			System.out.println("输入行数："
//			+ job.getCounters()
//			.findCounter("org.apache.hadoop.mapred.Task$Counter",
//			"MAP_INPUT_RECORDS").getValue());
//			System.out.println("输出行数："
//			+ job.getCounters()
//			.findCounter("org.apache.hadoop.mapred.Task$Counter",
//			"MAP_OUTPUT_RECORDS").getValue());
//			System.out.println("跳过的行："
//			+ job.getCounters().findCounter(Counter.LINESKIP).getValue());  
	        
		return job.isSuccessful() ? 0 : 1;
		}
		
		
		public static void main(String[] args) throws Exception {
		// 判断参数个数是否正确
		// 如果无参数运行则显示以作程序说明
//		if (args.length != 2) {
//		System.err.println("");
//		System.err.println("Usage: ReverseIndex < input path > < output path > ");
//		System.err
//		.println("Example: hadoop jar ~/ReverseIndex.jar hdfs://localhost:9000/in/telephone.txt hdfs://localhost:9000/out");
//		
//		
//		System.exit(-1);
//		}
		
		String[] path={"",""};
		path[0]="/Users/hpw/Documents/data/input";
		path[1]="/Users/hpw/Documents/data/output/result";
		String p="/Users/hpw/Documents/data/output";
		File file=new File(p);
		FileUtils.deleteDir(file);
	
		// 记录开始时间
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start0 = new Date();
		// 运行任务
		int res = ToolRunner.run(new Configuration(), new Mainjob(), path);
		
		
		// 输出任务耗时
		Date end = new Date();
		float time = (float) ((end.getTime() - start.getTime()) / 1000);
		System.out.println("任务开始：" + formatter.format(start));
		System.out.println("任务结束：" + formatter.format(end));
		System.out.println("任务耗时：" + String.valueOf(time) + " 秒");
		
		System.out.println("ALL任务耗时：" + String.valueOf(((end.getTime() - start0.getTime()) / 1000)) + " 秒");
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
		
		System.exit(res);
		}
		
	    public static boolean doIteration(Configuration conf,int iterNum) throws IOException, ClassNotFoundException, InterruptedException{  
	    	System.out.println(iterNum);  
	    	boolean flag=false;  
	        Job job = new Job(conf, "GSO job"+" "+iterNum);  
	        job.setJarByClass(Mainjob.class);  
			job.setMapperClass(Mapjob.Map.class); // 调用上面Map类作为Map任务代码
			job.setReducerClass(Reducejob.Reduce.class); // 调用上面Reduce类作为Reduce任务代码
			
			job.setOutputFormatClass(TextOutputFormat.class);
			job.setOutputKeyClass(Text.class); // 指定输出的KEY的格式
			job.setOutputValueClass(Text.class); // 指定输出的VALUE的格式
			
	        FileInputFormat.addInputPath(job, new Path(temp_path+(iterNum-1)+"/part-r-00000"));  
	        FileOutputFormat.setOutputPath(job, new Path(temp_path+iterNum+"/"));    
	        flag=job.waitForCompletion(true);  
	        return flag;  
	    }  
	    
	    public static void midJob(Configuration conf,int iterNum) throws IOException, ClassNotFoundException, InterruptedException{  
	    	System.out.println(iterNum);  
	    	Job job = new Job(conf, "kmeans job"+" "+iterNum);  
	        job.setJarByClass(Mainjob.class);  
			job.setMapperClass(MidMapjob.Map.class); // 调用上面Map类作为Map任务代码
			job.setReducerClass(MidReducejob.Reduce.class); // 调用上面Reduce类作为Reduce任务代码
			
			job.setOutputFormatClass(TextOutputFormat.class);
			job.setOutputKeyClass(Text.class); // 指定输出的KEY的格式
			job.setOutputValueClass(Text.class); // 指定输出的VALUE的格式
		    FileInputFormat.addInputPath(job, new Path(temp_path+(iterNum-1)+"/part-r-00000"));  
	        FileOutputFormat.setOutputPath(job, new Path(temp_path+iterNum+"/"));   
	        job.waitForCompletion(true);  
	    }  
	    
	    public static boolean doIterationmerge(Configuration conf,int iterNum) throws IOException, ClassNotFoundException, InterruptedException{  
	    	System.out.println(iterNum);  
	    	boolean flag=false;  
	        Job job = new Job(conf, "GSO Merge job"+" "+iterNum);  
	        job.setJarByClass(Mainjob.class);  
			job.setMapperClass(LastMapjob.Map.class); // 调用上面Map类作为Map任务代码
			job.setReducerClass(LastReducejob.Reduce.class); // 调用上面Reduce类作为Reduce任务代码
			
			job.setOutputFormatClass(TextOutputFormat.class);
			job.setOutputKeyClass(Text.class); // 指定输出的KEY的格式
			job.setOutputValueClass(Text.class); // 指定输出的VALUE的格式
			
	        FileInputFormat.addInputPath(job, new Path(temp_path+(iterNum-1)+"/part-r-00000"));  
	        FileOutputFormat.setOutputPath(job, new Path(temp_path+iterNum+"/"));    
	        flag=job.waitForCompletion(true);  
	        return flag;  
	    }  
	    
	    public static void lastJob(Configuration conf,int iterNum) throws IOException, ClassNotFoundException, InterruptedException{  
	    	System.out.println(iterNum);  
	    	Job job = new Job(conf, "last job"+" "+iterNum);  
	        job.setJarByClass(Mainjob.class);  
			job.setMapperClass(LastMapjob.Map.class); // 调用上面Map类作为Map任务代码
			job.setReducerClass(LastReducejob.Reduce.class); // 调用上面Reduce类作为Reduce任务代码
			
			job.setOutputFormatClass(TextOutputFormat.class);
			job.setOutputKeyClass(Text.class); // 指定输出的KEY的格式
			job.setOutputValueClass(Text.class); // 指定输出的VALUE的格式
		    FileInputFormat.addInputPath(job, new Path(temp_path+(iterNum-1)+"/part-r-00000"));  
	        FileOutputFormat.setOutputPath(job, new Path(temp_path+iterNum+"/"));   
	        job.waitForCompletion(true);  
	    }  
		
		
		
}
