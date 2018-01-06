package mapreduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

import Utils.Calculate;
import Utils.Constants;

public class LastReducejob {
        
	
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
	    
		public static class Reduce extends Reducer<Text, Text, Text, Text> {
				public void reduce(Text key, Iterable<Text> values, Context context)
					throws IOException, InterruptedException {
								String valueString="";
								int i=0;
								double rd;
								double newJx;
								double newL;
								double l;
								String newvalue = null;
								int nbSize=0;
								double average=0;
								double count =0;
								
								for (Text value : values) {
								valueString = key+","+value.toString();
								F1=Calculate.getJ(valueString);
								if(F1<Mainjob.bestF){
									Mainjob.bestF=F1;
								}
								average=average+F1;
								count=count+1;
								context.write(new Text(valueString),null);

								}
								
								Mainjob.averge=average/count;

					}
		}
		
		
		
	
		

}
