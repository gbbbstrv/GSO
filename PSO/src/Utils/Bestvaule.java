package Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bestvaule {
	
	public static void main(String[] args) throws IOException {
		
		
		//输入QOS值	
		FileReader frqws=new FileReader(Constants.QWS);
		BufferedReader brqws=new BufferedReader(frqws);
		 String linevalueqws = brqws.readLine();
		 
		 List lc= new ArrayList();
		 List lt= new ArrayList();
		 
			while(linevalueqws!=null){
				System.out.println(linevalueqws);
				String cost=linevalueqws.split("\t")[Constants.Cindex];
				String time=linevalueqws.split("\t")[Constants.Tindex];
				lc.add(cost);
				lt.add(time);
				
				linevalueqws = brqws.readLine();
			}
			Double bc=bestpoint(lc);
			Double bt=bestpoint(lt);
			System.out.println(bc);
			System.out.println(bt);
			
	}
	
	
	//求理想点
		public static double bestpoint(List va){
			double min=1100;
			for(int i=0;i<va.size();i++){
				if(Double.parseDouble((String) va.get(i))<min){
					min=Double.parseDouble((String) va.get(i));
				}
			}
			return min;
		}

}
