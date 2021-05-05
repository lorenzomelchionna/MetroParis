 package it.polito.tdp.metroparis.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {

	Graph<Fermata, DefaultEdge> Grafo;
	
	public void CreaGrafo() {
		
		this.Grafo = new SimpleGraph<>(DefaultEdge.class);
		
		MetroDAO dao = new MetroDAO();
		List<Fermata> Fermate = dao.getAllFermate();
		
		//for(Fermata f : Fermate)
			//Grafo.addVertex(f);
		
		Graphs.addAllVertices(this.Grafo, Fermate);
		
		/*for(Fermata f1 : this.Grafo.vertexSet()) 
			for(Fermata f2 : this.Grafo.vertexSet()) 
				if(!f1.equals(f2) && dao.fermateCollegate(f1, f2)) 
					this.Grafo.addEdge(f1, f2);*/
				
		List<Connessione> Connessioni = dao.getAllConnection(Fermate);
		
		for(Connessione c : Connessioni) 
			this.Grafo.addEdge(c.getStazP(), c.getStazA());
		
		System.out.println(this.Grafo);
		
	}
	
}
