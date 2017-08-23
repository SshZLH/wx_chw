package org.wss.utils;

import java.util.Arrays;

public class ArrayCopy{
	public static byte[] copyOfRange(byte[] bytes,int start,int end){
		byte[] b = new byte[end-start];
		for(int m=0;m<end-start;m++){
			b[m]=bytes[m+start];
		}
		return b;
	}
	public static void main(String[] args) {
		ArrayCopy shuzu = new ArrayCopy();
		byte[] bytes = new byte[15];
		bytes[0]=1;
		bytes[1]=2;
		bytes[2]=3;
		bytes[3]=4;
		bytes[4]=5;
		bytes[5]=6;
		bytes[6]=7;
		bytes[7]=8;
		bytes[8]=9;
		bytes[9]=10;
		bytes[10]=11;
		bytes[11]=12;
		bytes[12]=13;
		bytes[13]=14;
		bytes[14]=15;
		
		
		byte[] b = shuzu.copyOfRange(bytes,0,10);
		for(int i=0;i<b.length;i++){
			System.out.println("B:"+b[i]);
		}
		byte[] c = Arrays.copyOfRange(bytes,0,10);
		for(int i=0;i<c.length;i++){
			System.out.println("C:"+c[i]);
		}
	}
}
