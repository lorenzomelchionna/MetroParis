package it.polito.tdp.metroparis.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		 
		Model m = new Model();
		
		m.CreaGrafo();
		
		Fermata p = m.trovaFermata("La Fourche");
		
		if(p==null) 
			System.out.println("Fermata non trovata");
		else {
			
			List<Fermata> Raggiungibili = m.fermateRaggiungibili(p);
			System.out.println(Raggiungibili);
			
		}
		
		Fermata a = m.trovaFermata("Temple");
		
		List<Fermata> percorso = m.trovaCammino(p,a);
		
		System.out.println(percorso);
			
	}

}
