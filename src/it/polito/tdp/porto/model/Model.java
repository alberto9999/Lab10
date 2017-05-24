package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;

import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	private List<Author> authors;
	private List<Paper> papers;
	private SimpleGraph<Author,DefaultEdge> grafoAutori;
	private AuthorIdMap authorIdMap ;
	private PaperIdMap paperIdMap ;
	private List<Author>listaNonCoautoriTemp;
	
	
	
	public Model(){
		this.authorIdMap = new  AuthorIdMap() ;
		this.paperIdMap = new  PaperIdMap() ;
		
	}
	
	public Set<Author> getCoauthors(Author a) {
		caricaDati();
		generaGrafo();
		Set<Author>listaCoautori = new HashSet<Author>();
		for(Paper p : a.getListaPapers()){
			for(Author a2: p.getAuthors()){
				if(!a2.equals(a))
				listaCoautori.add(a2);
			}
		}
		listaNonCoautoriTemp= new ArrayList<Author>(getAuthors());
		for(Author aut : listaCoautori){
			listaNonCoautoriTemp.remove(aut);	
		}
		listaNonCoautoriTemp.remove(a);
		
		return listaCoautori;
	}

	
	
	private  void generaGrafo() {	
		grafoAutori= new SimpleGraph<Author,DefaultEdge>(DefaultEdge.class);
		Graphs.addAllVertices(grafoAutori, getAuthors());
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
		for(Author a :getAuthors()){
			pDAO.setPapersDaAuthor(a,paperIdMap);
		}	
		  for(Paper p: getPapers()){
		    pDAO.setAuthorsDaPaper(p,authorIdMap);
		       }
			}
		
	
	
public List<Author> getNonCoauthors() {	
		return listaNonCoautoriTemp;
	}



	private List<Paper> getPapers() {
		if(papers==null){
			PortoDAO pDAO= new PortoDAO();
			papers=pDAO.getAllPapers(paperIdMap);
			}
			return papers;
	}

	
	
	public List<Author> getAuthors(){
		if(authors==null){
		PortoDAO pDAO= new PortoDAO();
		authors=pDAO.getAllAuthors(authorIdMap);
		}
		return  authors;
	}

	public List<Paper> getSequenza(Author a, Author b) {
		List<Paper>sequenzaPubblicazioni=new ArrayList<Paper>();
		List <DefaultEdge> percorsoArchi=DijkstraShortestPath.findPathBetween(grafoAutori, a, b);
		for(DefaultEdge de : percorsoArchi){
			for(Paper p1: grafoAutori.getEdgeSource(de).getListaPapers()){
				for(Paper p2: grafoAutori.getEdgeTarget(de).getListaPapers()){
					if(p1.equals(p2)){
						sequenzaPubblicazioni.add(p1);
					}
				}
			}
		}
		return sequenzaPubblicazioni;
	}

	

	
}
