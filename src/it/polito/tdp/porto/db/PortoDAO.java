package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.AuthorIdMap;
import it.polito.tdp.porto.model.Paper;
import it.polito.tdp.porto.model.PaperIdMap;

public class PortoDAO {

	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autore;
			}

			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				return paper;
			}

			return null;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	public Map<Integer, Author> getAllAuthors(AuthorIdMap authorIdMap) {
		Map<Integer, Author> authors= new HashMap<Integer,Author>();
		final String sql = "SELECT * FROM author";
       
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Author a= new Author(rs.getInt("id"),rs.getString("lastname"),rs.getString("firstname"));
				a = authorIdMap.put(a);
				authors.put(a.getId(),a);		
			}

			return authors;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}



	public void setPapersDaAuthor(Author a, PaperIdMap paperIdMap) {
		List<Paper>pubblicazioni= new ArrayList<Paper>();
		final String sql="SELECT p.eprintid,title,issn,publication,type,types "+
                         "FROM author a, creator c, paper p "+
                         "WHERE a.id=c.authorid AND  p.eprintid=c.eprintid AND a.id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, a.getId());
			ResultSet res = st.executeQuery() ;
			
			while(res.next()){
				Paper p= new Paper(res.getInt("eprintid"), res.getString("title"), res.getString("issn"), 
						res.getString("publication"), res.getString("type"), res.getString("types"));
				p=paperIdMap.put(p);
				a.addPaper(p);
				
			}
			res.close();
			conn.close();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}

	public void setAuthorsDaPaper(Paper p,AuthorIdMap authorIdMap) {
	
		final String sql="SELECT id, firstname, lastname "+
                         "FROM author a, creator c, paper p "+
                         "WHERE  a.id=c.authorid AND p.eprintid=c.eprintid AND p.eprintid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, p.getEprintid());
						
			ResultSet res = st.executeQuery() ;
			while ( res.next()){
				Author a= new Author (res.getInt("id"),res.getString("lastname"),res.getString("firstname"));
				a=authorIdMap.put(a);
				p.addAuthor(a);	
			}
			
			
			
			
			res.close();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}	
	}
	
	
	
}