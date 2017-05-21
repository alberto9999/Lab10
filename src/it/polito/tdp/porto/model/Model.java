package it.polito.tdp.porto.model;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	private Map<Integer,Author> authors;
	private SimpleGraph<Author,DefaultEdge> grafoAutori;
	private AuthorIdMap authorIdMap ;
	private PaperIdMap paperIdMap ;
	
	
	
	public Model(){
		this.authorIdMap = new  AuthorIdMap() ;
		this.paperIdMap = new  PaperIdMap() ;
		generaGrafo();
	}
	
	public  void generaGrafo() {
		caricaDati();
		grafoAutori= new SimpleGraph<Author,DefaultEdge>(DefaultEdge.class);
		Graphs.addAllVertices(grafoAutori, getAuthors().values());
		for(Author a :grafoAutori.vertexSet()){
			if(a.getListaPapers()!=null){
		       for(Paper p: a.getListaPapers()){
		       	for(Author a2: p.getAuthors()){
				  if(!a.equals(a2)){
			        grafoAutori.addEdge(a, a2) ;
				  }
			    }
		      }
		   }
		}
	}

	private void caricaDati() {
		PortoDAO pDAO= new PortoDAO();
		for(Author a :getAuthors().values()){
			pDAO.setPapersDaAuthor(a,paperIdMap);
			if(a.getListaPapers()!=null){
		       for(Paper p: a.getListaPapers()){
		    	   pDAO.setAuthorsDaPaper(p,authorIdMap);
		       }
			}
		}
	}

	public Map<Integer, Author> getAuthors(){
		if(authors==null){
		PortoDAO pDAO= new PortoDAO();
		authors=pDAO.getAllAuthors(authorIdMap);
		}
		return authors;
	}


	public Set<Author> getCoauthors(Author a) {
		Set<Author>listaCoautori = new HashSet<Author>();
		for(Paper p : a.getListaPapers()){
			for(Author a2: p.getAuthors()){
				listaCoautori.add(a2);
			}
		}
		
		return listaCoautori;
	}

	public SimpleGraph<Author, DefaultEdge> getGrafo() {
		return grafoAutori;
	}

	
	
	
	
	
}
