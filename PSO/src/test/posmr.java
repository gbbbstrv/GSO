package test;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;  
import org.apache.hadoop.conf.Configuration;  
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
  
public class posmr {  
    public static int dim = Constants.dimension;  
    public   static int sizepop = 20; 
    
    
    public static double value_cc=0;
    public static double value_tc=0;
	public static double bestt;
	public static double bestc;
	public static double worstc;
	public static double worstt;
   public static String T="";
   public static String C=""; 
    public static List<List<String>> list;
   
    public static  HashMap<Integer, Double> bestList = new HashMap<Integer, Double>();
    public static  HashMap<Integer, Double> avergeList = new HashMap<Integer, Double>();
    public static double best=0;
    public static double averge=0;
    public static double count=0;
    public static  HashMap<Integer, Date> hashMap = new HashMap<Integer, Date>();
    public static class IntSumReducer extends Mapper<LongWritable, Text, Text,Text> {  
       private DoubleWritable job1map_key = new DoubleWritable();  
       private Text job1map_value = new Text();  
   
  
       private final double w = 1;  
       static double c1 = 1;  
       static double c2 = 1;  
       public double[] pop = new double[dim];  
       public double[] dpbest = new double[dim];  
       public double[] V = new double[dim];  
       public double[] fitness = new double[sizepop];  
       public double[] gbest = new double[dim];  
       public String[] S_value = new String[dim];  
       public String F_value;  
       public double bestfitness;  
       public double m_dFitness;  
       private Random random = new Random();  
       double g;  
       double sum1;  
       double sum2;  
  
       public void map(LongWritable key, Text values, Context context)  
              throws IOException,InterruptedException {  
           String itr[] = values.toString().split(" "); 
//           System.out.println(values.toString());
           int k =1;  
           F_value ="";  
           for (int j= 0; j < dim; j++) {  
              pop[j] =Double.valueOf((itr[k++]));  
              V[j] =Double.valueOf((itr[k++]));  
              dpbest[j] =Double.valueOf((itr[k++]));  
              gbest[j] =Double.valueOf((itr[k++]));  
           }  

          bestfitness = Double.valueOf((itr[k++]));  
           g =Double.valueOf((itr[k++]));  

           for (int i= 0; i < dim; i++) {  
//              V[i] = w * V[i] + c1 *random.nextDouble()  
//                     *(dpbest[i] - pop[i]) + c2 * random.nextDouble()  
//                     *(gbest[i] - pop[i]);  
             double a1=0;
             double a2=0;
             double b1=0;
             double b2=0;
             if(dpbest[i]>pop[i]){
            	 a1=0; 
            	 b1=c1*(dpbest[i] - pop[i]); 
             }else{
            	 a1=c1*(dpbest[i] - pop[i]);
            	 b1=0;
             }
             if(gbest[i]>pop[i]){
            	 a2=0;
            	 b2=c2*(gbest[i] - pop[i]);
             }else{
            	 a2=c2*(gbest[i] - pop[i]);
            	 b2=0;
             }
             if(gbest[i] ==pop[i]){
            	 pop[i]=gbest[i];
             }else{
             double Max=b1;
             double Min=a1;
             
             double Max2=b2;
             double Min2=a2;
          
             int x1 = (int)Math.round(Math.random()*(Max-Min)+Min);
             int x2 = (int)Math.round(Math.random()*(Max2-Min2)+Min2);
        	 V[i] = (int)(w * V[i]) + x1+ x2; 
              
              pop[i] = Math.abs((pop[i] + V[i])%(((Constants.SCount)/Constants.dimension)));  
              
              
              if((int)pop[i]==0){
            	  pop[i]=Constants.SCount/Constants.dimension;
              }
             }
           }  
           value_cc=0;
           value_tc=0;
           //计算Ackley 函数的值  
//          System.out.println(list);
           for (int i= 0; i < dim; i++) {  
//              sum1 += pop[i] *pop[i];  
//              sum2 += Math.cos(2 * Math.PI* pop[i]); 
//        	   System.out.println((int)pop[i]);
        	   if((int)pop[i]==0){
        		   pop[i]=Constants.SCount/Constants.dimension;
        	   }
              value_cc+=Double.parseDouble(list.get(Calculate.getGSOQOS(i,(int)pop[i])).get(Constants.Cindex));
    			value_tc+=Double.parseDouble(list.get(Calculate.getGSOQOS(i,(int)pop[i])).get(Constants.Tindex));
    		
           }  
           //m_dFitness 计算出的当前值  
//           m_dFitness= -20 * Math.exp(-0.2 * Math.sqrt((1.0 / dim) * sum1))  
//                 - Math.exp((1.0 / dim) * sum2) + 20 +2.72;  
//           System.out.println(value_cc+"c"+bestc);
//           System.out.println(value_tc+"t"+bestt);
           m_dFitness= Math.sqrt(Math.pow(value_cc-bestc,2)+Math.pow(value_tc-bestt,2));
//           System.out.println(m_dFitness+"gggg");
          if(m_dFitness < 0) {  
              System.out.println(value_cc + " "+ m_dFitness + " " + value_tc);  
           }  
           if(m_dFitness < bestfitness) {  
              bestfitness =m_dFitness;  
              for (int i = 0; i< dim; i++) {  
                 dpbest[i] = pop[i];  
              }  
           }  
           for (int j= 0; j < dim; j++) {  
              S_value[j] =Double.toString(pop[j]) + " "  
                     +Double.toString(V[j]) + " "  
                     +Double.toString(dpbest[j]) + " ";  
           }  
           for (int j= 0; j < dim; j++) {  
              F_value += S_value[j];  
           }  
          job1map_key.set(bestfitness);  
          job1map_value.set(F_value);  
          context.write(new Text(bestfitness+""), new Text(job1map_value));  
       }  
    }  
  
    public static class job1Reducer extends  
          Reducer<Text, Text, Text,Text> {  
       private DoubleWritable job1reduce_key = new DoubleWritable();  
       private Text job1reduce_value = new Text();  
      
  
  
       public double[] pop1 = new double[dim];  
       public double[] dpbest1 = new double[dim];  
       public double[] V1 = new double[dim];  
       public double[] gbest1 = new double[dim];  
       public double[] gbest_temp = new double[dim];  
       public String[] S_value1 = new String[dim];  
       public String F_value1;  
       public double m_dFitness =Double.MAX_VALUE;  
  
       public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {  
           Double bestfitness = Double.valueOf(key.toString());  
//           System.out.println(bestfitness);
           averge+=bestfitness;
           count+=1;
           int k;  
           if(bestfitness < m_dFitness) {  
              m_dFitness =bestfitness;  
              for (Text val : values){  
                 String itr[] = val.toString().split(" ");  
                 k = 0;  
                 for (int j = 0; j < dim; j++){  
                     pop1[j] =Double.valueOf((itr[k++]));  
                     V1[j] =Double.valueOf((itr[k++]));  
                     dpbest1[j]= Double.valueOf((itr[k++]));  
                 }  
                 for (int j = 0; j < dim; j++){  
                     gbest1[j] =dpbest1[j];  
                    gbest_temp[j] = dpbest1[j];  
                 }  
                 F_value1 ="1"+ " "; 
                 for (int j = 0; j < dim; j++){  
                     S_value1[j]= Integer.toString((int)pop1[j]) + " "  
                           + Double.toString(V1[j]) + " "  
                           + Double.toString(dpbest1[j]) + " "  
                           + Double.toString(gbest1[j]) + " ";  
                 }  
                 for (int j = 0; j < dim; j++){  
                     F_value1 +=S_value1[j];  
                 }  
                 F_value1 += (Double.toString(bestfitness)) + " "  
                        +(Double.toString(m_dFitness));  
                 job1reduce_key.set(1);  
                 job1reduce_value.set(F_value1);  
//                 context.write(job1reduce_key,job1reduce_value); 
                 best=m_dFitness;
                 context.write(new Text(F_value1),null);
              }  
           } else{  
              for (Text val : values){  
                 String itr[] = val.toString().split(" ");  
                 k = 0;  
                 for (int j = 0; j < dim; j++){  
                     pop1[j] =Double.valueOf((itr[k++]));  
                     V1[j] =Double.valueOf((itr[k++]));  
                     dpbest1[j]= Double.valueOf((itr[k++]));  
                 }  
                 for (int j = 0; j < dim; j++){  
                     gbest1[j] =gbest_temp[j];  
                 }  
                 F_value1 ="1"+ " ";  
                 for (int j = 0; j < dim; j++){  
                	  S_value1[j]= Integer.toString((int)pop1[j]) + " "  
                           + Double.toString(V1[j]) + " "  
                           + Double.toString(dpbest1[j]) + " "  
                           + Double.toString(gbest1[j]) + " ";  
                 }  
                 for (int j = 0; j < dim; j++){  
                     F_value1 +=S_value1[j];  
                 }  
                 F_value1 += (Double.toString(bestfitness)) + " "  
                        +(Double.toString(m_dFitness));  
                 job1reduce_key.set(1);  
                 job1reduce_value.set(F_value1);  
//                 context.write(job1reduce_key,job1reduce_value); 
                 best=m_dFitness;
                 context.write(new Text(F_value1),null);  
              }  
           }  
       }  
    }
  
    public static void main(String[] args) throws Exception {  
    	
    	
      
//           String[] otherArgs = new GenericOptionsParser(conf, args)  
//                 .getRemainingArgs();  
//           if(otherArgs.length != 2) {  
//              System.err.println("Usage:wordcount <in><out>");  
//              System.exit(2);  
//           }  
//           String input = otherArgs[0];  
//           String output = otherArgs[1];  
           String input = "/Users/hpw/Documents/data/psoinput"; 
//           String input ="hdfs://localhost:8020/Users/hpw/hadoop-1.2.1/tmp/hadoop-hpw/data/psoinput";

           String output1 = "/Users/hpw/Documents/data/psooutput/";
//           String output1="hdfs://localhost:8020/Users/hpw/hadoop-1.2.1/tmp/hadoop-hpw/data/psooutput/";
           String output="";
           String p="/Users/hpw/Documents/data/psooutput";
   		File file=new File(p);
   		FileUtils.deleteDir(file);
           FileSystem fs;  
           
   
           
      	 list =InputQOS.getQOS();
//      	 System.out.println(list);
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
           Configuration conf = new Configuration();  
           try{  
              fs =FileSystem.get(conf);  
              int step = Constants.iterTime;  
              for(int i = 0; i< step; i++) {  
            	  Date start0 = new Date();
                 System.out.println("第" + i + "次：" + i);  
                 output =output1+"temp"+i;  
                 Job job = new Job(conf, "word count");  
                 job.setJarByClass(posmr.class);  
                   
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
                 bestList.put(i, best);
                 hashMap.put(i, time);
                 avergeList.put(i, averge/count);
                 averge=0;
                 count=0;
             
                 
              }
      		// 记录开始时间
      		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
              Date end = new Date();
      		float time = (float) ((end.getTime() - start.getTime()) );
      		
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
}  