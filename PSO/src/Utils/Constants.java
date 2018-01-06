package Utils;

import java.util.ArrayList;

public class Constants {
	

	
	
	//MRDGSO相关参数
	//分群迭代次数
	public static final int iterTime=400;  
	public static int iterNum=1; 
	//合并迭代次数迭代次数   
	public static final int merge_iterTime=1;  
	public static int merge_iterNum=1; 
	//QoS－HDFS路径
//	public static String QWS="hdfs://localhost:8020/Users/hpw/hadoop-1.2.1/tmp/hadoop-hpw/data/WSQoS/QWS";
	//QoS－本地路径
	public static String QWS="/Users/hpw/Documents/data/WSQoS/QWS";
	//服务总数
	public static int SCount=2000;
	//维度－抽象服务数
	public static int dimension=40;
	//萤火虫数量
	public static int num=100;
	//萤火虫种群数
	public static int gsok=1;
	//每个抽象服务的具体服务数
	public static int Height=SCount/dimension;
	//距离参数
	public static int C=20;
	//收敛误差  
	public static final double threadHold=num*5;
	//分隔符
	public static String separators="\t";

	
	
	
	//萤火虫相关参数
	//􏰩􏵵􏵶􏷈􏷉􏳩􏰩􏵵􏵶􏷈􏷉􏳩􏰩􏵵􏵶􏷈􏷉􏳩萤光素消失率
	public static double p=0.4;
	//萤光素更新率
	public static double gama=0.6;
	//萤光素更新率
	public static double  beta=0.08;	
	//领域集集合数量控制
	public static double  nt=5;
	//初始萤光素
	public static double  l0=5;
	//初始决策半径
	public static double  rd=5;
	//感知半径
	public static double  rs=10;
	//移动步长
	public static double s=0.5;
	//初始目标函数
	public static double  f=5;
	//P1参数
	public static double  P1=0.1;
	//P2参数
	public static double  P2=0.9;
	
	
	
	
	
	
	//time的索引
	public static int Tindex=0;
	//cost的索引
	public static int Cindex=1;
	//目标函数索引
	public static int Findex=dimension;
	//萤光素索引
	public static int Lindex=dimension+1;
	//决策半径索引
	public static int Rindex=dimension+2;
		
	
	//迭代次数
	public static double t=2;
	
	//TOPSIS－正理想点和负理想点
	public static ArrayList<Double> bestc = new ArrayList<Double>();
	public static ArrayList<Double> worstc = new ArrayList<Double>();
	public static ArrayList<Double> bestt = new ArrayList<Double>();
	public static ArrayList<Double> worstt = new ArrayList<Double>();
	
	public static Long[] ser_num(){
	
	
	Long[] ser_num =new Long[dimension];
	for(int i=0;i<dimension;i++){
		ser_num[i]=(long) Height;
	}
	return ser_num;
	}
	
	
	
}
