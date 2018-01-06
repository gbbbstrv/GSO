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

public class LastMapjob {
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
				glowwormposition=Calculate.extractPosition(gso1);
				
				String newgso=null;
				if(length>Constants.dimension){
					l1=Calculate.getluciferin(gso1);
					rd1=Calculate.getRd(gso1);	
					F1=Calculate.getJ(gso1);
				}else{
					 F1 = Calculate.getNewJx(glowwormposition);
					 rd1=Constants.rd;
			     	 l1=Calculate.getLuciferinValue(F1,Constants.l0);
			     	gso1=gso1+F1+"\t"+l1+"\t"+rd1+"\t";
				}
				  //计算输入萤火虫的位置
				glowwormposition=Calculate.extractPosition(gso1);
				
				Path[] paths =DistributedCache.getLocalCacheFiles(context.getConfiguration()); 
				FileReader fr=new FileReader(paths[0].toString());
//				for(int i=0;i<paths.length;i++){
//				System.out.println(paths[i].toString());
//				}
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
						 F2 = Calculate.getNewJx(gsodata);
						 rd2=Constants.rd;
						 l2=Calculate.getLuciferinValue(F2,Constants.l0);
				     	
						 gso2=gso2+F2+"\t"+l2+"\t"+rd2+"\t";
					}
					//计算萤火虫的位置
					gsodata=Calculate.extractPosition(gso2);
			
					//计算两萤火虫之间的距离
					distance=Calculate.returnEDistance(glowwormposition,gsodata);
//					System.out.println(distance);

					if(distance<rd1&&F1>F2){
						neighborsGroup.add(gso2);
						neighborsGrouplist.add(gsodata);
					}
					
					}
					linevalue = br.readLine();
				}
//				System.out.println(gsodataset.get(21).get(1));
				
//				System.out.println(neighborsGroup+"mmmm");

				
				//如果存在领域集，则计算领域集里每个个体的概率
				if(neighborsGroup.size()>0){
//					lpro=calculateProbability(l1,neighborsGroup);
//					//选择萤光素最大的萤火虫
//					newgso=selectBestNeighbor(lpro);
					
					double minF=10000;
					int indexF=0;
					for(int i=0;i<neighborsGroup.size();i++){
						 String[] line1=neighborsGroup.get(i).split(Constants.separators);
						if(minF<Double.parseDouble(line1[Constants.Lindex].toString())){
							minF=Double.parseDouble(line1[Constants.Lindex].toString());
							indexF=i;
						}
					}
					newgso=neighborsGroup.get(indexF).toString();
				}else{
					newgso=gso1;
				}

				
				bestv=Calculate.extractPosition(newgso);
				//萤火虫位置更新
				newv=Calculate.calculateNewX(glowwormposition,bestv);
//				System.out.println(curv);
//				System.out.println(bestv);
//				System.out.println(newv);
			
				
				//更新新的目标函数
				newJx=Calculate.getNewJx(newv);
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
		
				context.write(new Text(id1),new Text(result+newJx+"\t"+newL+"\t"+newrd+"\t"));
				
//				i++;	
//				context.write(new Text(i+""), new Text(newgso+"#"+neighborsGroup.size())); //输出
//				context.write(new Text(i+""), new Text(line)); //输出
				
				
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
