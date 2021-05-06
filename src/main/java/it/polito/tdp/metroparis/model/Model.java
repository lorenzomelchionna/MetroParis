 package it.polito.tdp.metroparis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
//import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {

	Graph<Fermata,DefaultEdge> Grafo;
	
	Map<Fermata,Fermata> predecessore;
	
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
		
		System.out.println("Grafo creato con "+this.Grafo.vertexSet().size()+" vertici e "+this.Grafo.edgeSet().size()+" archi\n");
		
	}
	
	public List<Fermata> fermateRaggiungibili(Fermata partenza) {
		
		//AMPIEZZA
		BreadthFirstIterator<Fermata,DefaultEdge> bfv = new BreadthFirstIterator<>(this.Grafo, partenza);
		
		//PROFONDITA'
		//DepthFirstIterator<Fermata,DefaultEdge> dfv = new DepthFirstIterator<>(this.Grafo, partenza);
		
		this.predecessore = new HashMap<>();
		this.predecessore.put(partenza, null);
		
		bfv.addTraversalListener(new TraversalListener<Fermata,DefaultEdge>(){

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {
				
				DefaultEdge arco = e.getEdge();
				Fermata a =Grafo.getEdgeSource(arco);
				Fermata b = Grafo.getEdgeTarget(arco);
				
				if(predecessore.containsKey(b) && !predecessore.containsKey(a)) {
					
					predecessore.put(a,b);
					//System.out.println(a+" scoperto da "+b);
					
				} else if(predecessore.containsKey(a) && !predecessore.containsKey(b)){
					
					predecessore.put(b,a);
					//System.out.println(b+" scoperto da "+a);
					
				}
				
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Fermata> e) {
				
				//System.out.println(e.getVertex());
				//Fermata nuova = e.getVertex();
				//Fermata precedente = null;
				//predecessore.put(nuova, precedente);
				
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Fermata> e) {
			}
			
		});
		
		List<Fermata> result = new ArrayList<>();
		
		while(bfv.hasNext()) {
			
			Fermata f = bfv.next();
			result.add(f);
			
		}
		
		return result;
		
	}
	
	public Fermata trovaFermata(String nome) {
		
		for(Fermata f : this.Grafo.vertexSet())
			if(f.getNome().equals(nome))
				return f;
		
		return null;
		
	}
	
	public List<Fermata> trovaCammino(Fermata partenza, Fermata arrivo) {
		
		fermateRaggiungibili(partenza);
		
		List<Fermata> result = new LinkedList<>();
		
		result.add(arrivo);
		Fermata f = arrivo;
		
		while(predecessore.get(f) != null){
			
			f = predecessore.get(f);
			result.add(0,f);
			
		}
		
		return result;
		
	}
	
}
