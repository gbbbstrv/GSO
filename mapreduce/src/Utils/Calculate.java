package Utils;

import java.util.ArrayList;
import java.util.List;

import mapreduce.Mainjob;
import mapreduce.Mainjob;

public class Calculate {
	
	/*Map阶段的计算公式	*/
	
	//计算萤火虫的位置
	public static List extractPosition(String glowwormId){
		List gsodimension = new ArrayList();
		String[] line=glowwormId.split(Constants.separators);
		for(int j=0;j<Constants.dimension;j++){
			gsodimension.add(line[j]);
		}	
		return gsodimension;
	}
	
	//获得该位置萤火虫的萤光素
	public static double getluciferin(String glowwormId){
		String[] line=glowwormId.split(Constants.separators);
		return Double.parseDouble(line[Constants.dimension+1].toString());

	}
	
	//获得该位置萤火虫的目标函数值
	public static double getJ(String glowwormId){
		String[] line=glowwormId.split(Constants.separators);
		return Double.parseDouble(line[Constants.dimension].toString());

	}
	
	//获得该位置萤火虫的决策半径
	public static double getRd(String glowwormId){
		String[] line=glowwormId.split(Constants.separators);
		return Double.parseDouble(line[Constants.dimension+2].toString());

	}
	
	
	//计算目标值
	public static double getObjectValue(List<List<String>> list,List local){
		Long[] ser=Constants.ser_num();
	
//		double[] bestc= new double[]{1.1022, 0.2739, 0.1704, 0.1607, 0.6187, 0.0959, 0.1195, 0.7641, 0.1586, 0.0298, 0.1146, 0.07, 0.9821, 0.2861, 0.1361, 0.0299, 0.7844, 0.0311, 0.0092, 0.1189, 0.1505, 0.1787, 0.2233, 0.2129, 0.0173, 1.3711, 0.0128, 0.2788, 0.084, 0.1599};
//		double[] worstc=new double[]{99.3544, 99.9576, 99.8284, 99.9349, 99.3319, 99.2713, 99.6167, 99.1554, 99.0561, 99.4463, 99.9204, 99.8404, 99.8804, 99.6246, 99.7534, 99.6777, 99.9729, 99.4667, 99.53, 99.982, 99.9521, 99.3855, 99.6971, 99.21, 99.3035, 99.6434, 99.7283, 99.5944, 99.7652, 100.0};
//		double[] bestt=new double[]{0.0141, 0.0221, 0.0706, 0.1769, 0.1412, 0.0295, 8.0E-4, 0.0126, 0.0266, 0.0359, 0.0412, 0.1095, 0.0608, 0.0187, 0.001, 4.0E-4, 0.0023, 0.0027, 0.0094, 0.044, 0.0095, 0.0156, 0.0193, 0.0053, 0.0521, 0.1036, 0.0209, 0.0051, 0.0104, 0.0988};
//		double[] worstt=new double[]{9.9804, 9.9649, 9.977, 9.9682, 9.9862, 9.9242, 9.9916, 9.969, 9.9925, 9.9958, 9.9471, 9.9795, 9.9653, 9.9916, 9.9849, 9.9807, 9.9722, 9.8934, 9.9746, 9.9958, 9.8911, 9.9676, 9.9938, 9.8392, 9.956, 9.9543, 9.9752, 9.9812, 9.9604, 100.0};
		double cc=0;
		double tc=0;
		double value_cc=0;
		double value_tc=0;
		for(int i=0;i<Constants.dimension;i++){
			value_cc=Double.parseDouble(list.get(getGSOQOS(i,Integer.parseInt(local.get(i).toString()))).get(Constants.Cindex));
			value_tc=Double.parseDouble(list.get(getGSOQOS(i,Integer.parseInt(local.get(i).toString()))).get(Constants.Tindex));
//			cc=cc+Math.pow((value_cc-Constants.bestc.get(i)+1)/(Math.abs(Constants.bestc.get(i)-Constants.worstc.get(i))),1);
//			tc=tc+Math.pow((value_cc-Constants.bestt.get(i)+1)/(Math.abs(Constants.bestt.get(i)-Constants.worstt.get(i))),1);
//			
//			value_cc=10;
//			value_tc=10;
//			cc=cc+Math.pow((value_cc-Constants.bestc[i]+1)/(Math.abs(bestc[i]-worstc[i])),1);
//			tc=tc+Math.pow((value_cc-Constants.bestt[i]+1)/(Math.abs(bestt[i]-worstt[i])),1);
//			
//			cc=cc+value_cc-Constants.bestc.get(i);
//			tc=tc+value_tc-Constants.bestt.get(i);
			
			cc=cc+value_cc;
			tc=tc+value_tc;
//			
			
//			cc=cc+Double.parseDouble(list.get(getGSOQOS(i,Integer.parseInt(local.get(i).toString()))).get(Constants.Cindex));
//			tc=tc+(Double.parseDouble(list.get(getGSOQOS(i,Integer.parseInt(local.get(i).toString()))).get(Constants.Tindex)-Mainjob.bestt+1)/());
		}
//		return Math.sqrt(Math.pow((tc-Mainjob.bestt+1)/Math.abs(Mainjob.bestt-Mainjob.worstt+1),2)+Math.pow((cc-Mainjob.bestc)/Math.abs(Mainjob.worstc-Mainjob.bestc+1),2));
		return Math.sqrt(Math.pow(cc-Mainjob.bestc,2)+Math.pow(tc-Mainjob.bestt,2));
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
	
	
		
		//计算两个萤火虫之间的距离
				public static double returnEDistance(List glowwormId1,List glowwormId2){
					double value=1;
					double length=1;
					double le=0;
					double kk=0;
					double jj=0;
					List gsodimension = new ArrayList();
					Long[] ser=Constants.ser_num();
					List cdis = new ArrayList();
					List idis = new ArrayList();
					List va = new ArrayList();
					double cids_count=0;
					
					for(int i=0;i<Constants.dimension;i++){
						if(glowwormId1.get(i).toString().equals(glowwormId2.get(i).toString())){
							cdis.add(0);
						}else{
							cdis.add(1);
							cids_count=cids_count+1;
						}
						
					}
				
					if(cids_count==0){
						return 0;
					}else{
						value=1;
						for(int i=0;i<Constants.dimension;i++){
							value=value*(ser[i]-Math.abs(Double.parseDouble(glowwormId1.get(i).toString())-Double.parseDouble(glowwormId2.get(i).toString())))/(ser[i]);
						}
					
						return ((cids_count-1)/Constants.dimension+1/Constants.dimension*(1-value))*Constants.C;
					}
					
//					for(int i=0;i<Constants.dimension;i++){
//						value=value+Math.abs(Math.pow((Double.parseDouble(glowwormId1.get(i).toString())-Double.parseDouble(glowwormId2.get(i).toString())),1));
//						gsodimension.add(Math.abs(Math.pow((Double.parseDouble(glowwormId1.get(i).toString())-Double.parseDouble(glowwormId2.get(i).toString())),1)));
//					}
//					for(int j=0;j<Constants.dimension;j++){
//						if(((Double) gsodimension.get(j)).intValue()==0){
//							va.add(0);
//							
//						}else{
//							va.add(1);
//							le=le+1;
//							
//						}
//					}
//					double ll=le/Constants.dimension;
//					for(int j=0;j<Constants.dimension;j++){
//						kk=kk+(Double)gsodimension.get(j);
//						jj=jj+Constants.ser_num()[j];
//					}
//					
//				return Constants.C*kk/jj;
				}
		
	//计算该位置萤火虫的目标函数值
	public static double getNewJx(List local){
		List<List<String>> list=Mainjob.list;
	
		return getObjectValue(list,local);
		
	}
	
	 //初始化萤火虫萤光素
		public static double getLuciferinValue(double l,double F){			
			return (1-Constants.p)*l+Constants.gama*1/F;
		}
				
				
////计算该位置萤火虫的萤光素
//		public static double extractluciferin(List glowwormId,ArrayList<List> neighborsGrouplist,double l1){
//					
//			return (1-Constants.p)*l1+Constants.gama*F(glowwormId,neighborsGrouplist);
//		}
//		
//		//计算该位置萤火虫的目标函数值
//		public static double F(List glowwormId,ArrayList<List> neighborsGroup){
//			double value=0;
//			double result=0;
//		  for(int i=0;i<neighborsGroup.size();i++){
//			  for(int j=0;j<Constants.dimension;j++){
//				  value=value+Math.pow(Double.parseDouble(glowwormId.get(j).toString())-Double.parseDouble(neighborsGroup.get(i).get(j).toString()),2);
//			  }
//			  result=result+Math.sqrt(value);
//		  }
//		  if(result==0){
//			  return 0;
//		  }else{
//		  return 1/result;
//		}
//						
//		}
//		
		
		
		
		
	/*Redue阶段的计算公式	*/
		
		
		//计算移动后得到新的萤火虫位置
				public static List calculateNewX(List curv,List newv){
					List gsodimension = new ArrayList();
					double dis=Calculate.returnEDistance(newv, curv);
					if(dis!=0){
					
					for(int i=0;i<Constants.dimension;i++){
						if(newv.get(i)==curv.get(i)){
							gsodimension.add(Double.parseDouble((String) curv.get(i)));
						}else{
							double rand=Math.random();
						     if(rand<=Constants.P1){
						    	 gsodimension.add(curv.get(i));
						     }else if(rand>Constants.P1&&rand<=Constants.P2){
						    	 gsodimension.add(newv.get(i));
						     }else{
						    	 double S1=Double.parseDouble((String) curv.get(i));
						    	 double S2=Double.parseDouble((String) newv.get(i));
						    	gsodimension.add(new java.util.Random().nextInt(Constants.Height)+1);
		//					    	 gsodimension.add(((int)((S1+(S2-S1)*Constants.s))%Constants.Height)+1);
//							    	 gsodimension.add(((int)((S1+(S2-S1)/Math.abs(S2-S1)))%Constants.Height)+1);
//							    	 gsodimension.add(((int)((S1))%Constants.Height)+1);
//						    	 gsodimension.add((String) newv.get(i));
						     }
						}
					}

					return gsodimension;
					}else{
						return curv;
					}
					
				}
				
		//更新新的决策半径
		public static double getNewrd(double rd,double nbSize){			
			return Math.min(Constants.rs, Math.max(0, rd+Constants.beta*(Constants.nt-nbSize)));
		}
		
		//更新新的萤光素
		public static String calculateNewvalue(String value){			
			return value.substring(0,value.length()-1);
		}

}
