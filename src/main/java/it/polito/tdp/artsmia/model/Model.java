package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	ArtsmiaDAO dao;
	Graph<Artist,DefaultWeightedEdge> grafo;
	Map<Integer,Artist> idMap;
	List<Artist> soluzione;
	
	public Model() {
		dao=new ArtsmiaDAO();
	}
	
	public List<String> getRuolo(){
		return dao.getRuoli();
	}
	
	public String creaGrafo(String ruolo) {
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap=new HashMap<>();
		dao.getActorPerRuolo(ruolo, idMap);
		Graphs.addAllVertices(grafo, idMap.values());
		for(Interactions i: dao.getArchi(ruolo, idMap)) {
			Graphs.addEdge(grafo, i.getA1(), i.getA2(), i.getPeso());
		}
		
		return "GRAFO CREATO!\n#VERTICI: "+grafo.vertexSet().size()+"\n#ARCHI: "+grafo.edgeSet().size();
	}
	
	public String getArtistiConnessi() {
		String result=new String();
		for(Artist a1: grafo.vertexSet()) {
			if(Graphs.neighborListOf(grafo, a1).size()==0)
				continue;
			result+="----\n";
			result+=a1.getId()+" "+a1.getNome()+"\n";
			List<ArtistiConnessi> artisti= new ArrayList<>();
			for(Artist a2: Graphs.neighborListOf(grafo, a1)) {
				artisti.add(new ArtistiConnessi(a2,(int)grafo.getEdgeWeight(grafo.getEdge(a2, a1))));
			}
			Collections.sort(artisti);
			for(ArtistiConnessi a:artisti) {
				result+=a.getA().getId()+" "+a.getA().getNome()+" - "+a.getPeso()+"\n";
			}
			result+="----\n";
		}
		return result;
	}
	
	public List<Artist> getCammino(int id){
		soluzione=new ArrayList<>();
		Artist partenza;
		if((partenza=idMap.get(id))==null)
			return null;
		List<Artist> parziale=new LinkedList<Artist>();
		parziale.add(partenza);
		for(Artist a: Graphs.neighborListOf(grafo, partenza)) {
		parziale.add(a);
		int peso=(int)grafo.getEdgeWeight(grafo.getEdge(a,partenza));
		cercaSoluzione(parziale,peso);
		parziale.remove(a);
		}
		return soluzione;
	}

	private void cercaSoluzione(List<Artist> parziale,int peso) {
		
			for(Artist a:Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1))) {
				if((int)grafo.getEdgeWeight(grafo.getEdge(a, parziale.get(parziale.size()-1)))==peso && !parziale.contains(a)) {
					parziale.add(a);
					cercaSoluzione(parziale,peso);
					parziale.remove(a);
				}
			}
			if(soluzione.size()<parziale.size()) {
				soluzione=new ArrayList<>(parziale);
			}
	}
}
