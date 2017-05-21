package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
	
	//	TEST PER COPIARE ELEMENTI DI LISTA E RIMUOVERLI	
		List <String>stringhe= new ArrayList<String>();
		List <String>strings= new ArrayList<String>();
		stringhe .add("k");
		stringhe .add("l");
		stringhe .add("s");
		strings=new ArrayList<String>(stringhe);
		strings.remove("k");
		System.out.println(stringhe);
		System.out.println(strings);
		
		
	}

}
