package test;
import java.io.BufferedWriter;  
import java.io.File;  
import java.io.FileWriter;  
import java.io.IOException;
import java.util.List;
import java.util.Random;

import Utils.Calculate;
import Utils.Constants;
import Utils.FileUtils;
import Utils.InputQOS;
  
public class psotest {  
      
    private final int iRang= 30;  
    static int dim =Constants.dimension;  
    static int sizepop =Constants.gsok*Constants.num;  
 
    double g = 10000;  
    private Random random =new Random();  
    public int[][] pop =new int[sizepop][dim];  
    public double[][] dpbest= new double[sizepop][dim];  
    public double[][] V =new double[sizepop][dim];  
    public double[] fitness= new double[sizepop];  
    public double[] gbest =new double[dim];   
    public double[]fitnessgbest = new double[sizepop];  
    public double[]bestfitness = new double[sizepop];  
    int m_iTempPos =999;  
    public double value_cc=0;
    public double value_tc=0;
	public static double bestt;
	public static double bestc;
	public static double worstc;
	public static double worstt;
   public static String T="";
   public static String C=""; 
    public static List<List<String>> list;
    public static List<List<String>> init;
    public void c() throws IOException { 
        init=InputQOS.getInit();
        System.out.println(init.get(0).size());
    	 list =InputQOS.getQOS();
    	 T=InputQOS.BestTime();
         C=InputQOS.BestCost();
         bestc=Double.parseDouble(C.split(";")[0].toString());
         worstc=Double.parseDouble(C.split(";")[1].toString());
         bestt=Double.parseDouble(T.split(";")[0].toString());
         worstt=Double.parseDouble(T.split(";")[1].toString());
         System.out.println(sizepop+"-"+dim);
       for(int i = 0; i < sizepop;i++)  
       {  
           for(int j= 0; j < dim; j++) {  
              pop[i][j] = new java.util.Random().nextInt(Constants.Height)+1;
//        	   pop[i][j]=Integer.parseInt(init.get(i).get(j).toString());
              V[i][j] = dpbest[i][j] =pop[i][j];  
           }  
           value_cc=0;
           value_tc=0;
           for(int j= 0; j < dim; j++) {   
  			value_cc+=Double.parseDouble(list.get(Calculate.getGSOQOS(j,pop[i][j])).get(Constants.Cindex));
  			value_tc+=Double.parseDouble(list.get(Calculate.getGSOQOS(j,pop[i][j])).get(Constants.Tindex));
  		
           }  
//           fitness[i]= -20 * Math.exp(-0.2 * Math.sqrt((1.0 / dim) * sum1))  
//                 - Math.exp((1.0 / dim) * sum2) + 20 +2.72;  
           
           fitness[i]= Math.sqrt(Math.pow(value_cc-bestc,2)+Math.pow(value_tc-bestt,2));
          bestfitness[i] = 10000;  
          if(bestfitness[i] > fitness[i]) {  
              bestfitness[i] =fitness[i];  
              for(int j = 0; j< dim; j++) {  
                 dpbest[i][j] = pop[i][j];  
              }  
           }  
       }  
         
       for(int i = 0; i < sizepop;i++)  
       {  
           if(bestfitness[i] < g) {  
                 g = bestfitness[i];  
                 m_iTempPos = i;  
           }  
       }  
       if (m_iTempPos != 999) {  
           for (int j= 0; j < dim; j++) {  
              gbest[j] =dpbest[m_iTempPos][j];  
           }  
       }  
    }  
    public static void main(String[] args) throws IOException {  
        String p="/Users/hpw/Documents/data/psoinput/pso";
   		File file1=new File(p);
   		FileUtils.deleteDir(file1);
       psotest pso = new psotest();  
       pso.c();  
       File file = new File("/Users/hpw/Documents/data/psoinput/pso");  
       BufferedWriter out=new BufferedWriter(new FileWriter(file,true));  
       for(int i = 0; i < sizepop; i++){  
          out.write(i + " ");  
           for(int j= 0; j < dim; j++) {  
              out.write(pso.pop[i][j] + " "+ pso.V[i][j] + " " + pso.dpbest[i][j] + " " + pso.gbest[j] + " ");  
           }  
          out.write(pso.bestfitness[i]+" "+pso.g);  
          out.newLine();  
       }  
       out.close();  
    }  
}  