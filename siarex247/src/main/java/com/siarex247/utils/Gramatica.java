package com.siarex247.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Gramatica {

	public static String aplicarGramatica(String razonSocial){
		String razonSocialFinal = "";
		try{
			HashMap<String, String> letraFonetica = reglaGramatical();
			Set< String> llaves =  letraFonetica.keySet();
			Iterator<String> keysMapa = llaves.iterator();
			String key = ""; 
			while (keysMapa.hasNext()){
				key = keysMapa.next();
				if (razonSocial.indexOf(key) > -1){
					razonSocial = razonSocial.replace(key, letraFonetica.get(key));
				}
				
			}
			razonSocialFinal = razonSocial;
			
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return razonSocialFinal;
	}
	
	
	public static HashMap<String, String> reglaGramatical(){
		HashMap<String, String> letrasFonetica = new HashMap<String, String>();
		try{
			//letrasFonetica.put("S", "S"); // S - S
			//letrasFonetica.put("C", "S"); // C - S
			//letrasFonetica.put("Z", "S"); // Z - S
			//letrasFonetica.put("K", "S"); // k - S
			//letrasFonetica.put("X", "G"); // X - X
			//letrasFonetica.put("J", "G"); // X - X
			//letrasFonetica.put("Q", "K"); // X - X
			//letrasFonetica.put("CH", "S"); // CH - X
			letrasFonetica.put("(A)", "A"); // (A) - A
			letrasFonetica.put("(a)", "A"); // (a) - a
			letrasFonetica.put("(E)", "E"); // (E) - E
			letrasFonetica.put("(e)", "E"); // (e) - e
			letrasFonetica.put("(I)", "I"); // (I) - i
			letrasFonetica.put("(i)", "I"); // (i) - i
			letrasFonetica.put("(O)", "O"); // (O) - o
			letrasFonetica.put("(o)", "O"); // (o) - o
			letrasFonetica.put("(U)", "U"); // (U) - u
			letrasFonetica.put("(u)", "U"); // (u) - u
			letrasFonetica.put("(N)", "N"); // (n) - n
			letrasFonetica.put("(n)", "N"); // (N) - n
			//letrasFonetica.put("B", "B"); // b - b
			//letrasFonetica.put("V", "B"); // v - b
			//letrasFonetica.put("H", ""); // v - b
			//letrasFonetica.put("LL", "Y"); // v - b
			//letrasFonetica.put(" ", ""); // v - b
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return letrasFonetica;
	}
	
	public static void main(String[] args) {
		try{
			Gramatica f = new Gramatica();
			f.aplicarGramatica("HOME DEPOT MEJICO");
		}catch(Exception e){
			
		}
	}
}
