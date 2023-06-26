package sec_Board.brd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	private DataSource dFact;
	Connection conn;
	PreparedStatement pst;
	
	public BoardDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dFact = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<ArticleVO> selectAllArticles(Map<String, Integer> pagingMap){
		List<ArticleVO> articlesList = new ArrayList<ArticleVO>();
		int section = (Integer) pagingMap.get("section");
		int pageNum = (Integer) pagingMap.get("pageNum");
		
		try {
			conn=dFact.getConnection();
			String sql = "SELECT * FROM (SELECT ROWNUM AS recNum,"
					+ " LVL, articleNO,"
					+ " parentNO, title,"
					+ " id, writedate"
					+ " FROM (SELECT LEVEL AS LVL, "
					+ "articleNO," + " parentNO," + " title," + " id,"
					+ " writedate" + " FROM t_board"
					+ " START WITH parentNO=0" + " CONNECT BY PRIOR articleNO=parentNO"
					+ " ORDER SIBLINGS BY articleNO DESC)" + ") "
					+ "WHERE recNum BETWEEN(?-1)*100+(?-1)*10+1 AND (?-1)*100+?*10";
			System.out.println(sql);
			pst = conn.prepareStatement(sql);
			pst.setInt(1, section);
			pst.setInt(2, pageNum);
			pst.setInt(3, section);
			pst.setInt(4, pageNum);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				int level = rs.getInt("lvl");
				int articleNO = rs.getInt("articleNO");
				int parentNO = rs.getInt("parentNO");
				String title = rs.getString("title");
				String id = rs.getString("id");
				Date writeDate = rs.getDate("writeDate");
				ArticleVO article = new ArticleVO();
				article.setLevel(level);
				article.setArticleNO(articleNO);
				article.setParentNO(parentNO);
				article.setTitle(title);
				article.setId(id);
				article.setWriteDate(writeDate);
				articlesList.add(article);
			}
			rs.close();
			pst.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articlesList;
	}
	
	public List<ArticleVO> selectAllArticles(){
		List<ArticleVO> articlesList = new ArrayList();
		try {
			conn=dFact.getConnection();
			String sql = "SELECT LEVEL, articleNo, parentNO, title, content, id, writeDate"
					+ " FROM t_board"
					+ " START WITH parentNO=0"
					+ " CONNECT BY PRIOR articleNO=parentNO"
					+ " ORDER SIBLINGS BY articleNO DESC";
			System.out.println();
			pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				int level = rs.getInt("level");
				int articleNO = rs.getInt("articleNO");
				int parentNO = rs.getInt("parentNO");
				String title = rs.getString("title");
				String content = rs.getString("content");
				String id = rs.getString("id");
				Date writeDate = rs.getDate("writeDate");
				ArticleVO article = new ArticleVO();
				article.setLevel(level);
				article.setArticleNO(articleNO);
				article.setParentNO(parentNO);
				article.setTitle(title);
				article.setContent(content);
				article.setId(id);
				article.setWriteDate(writeDate);
				articlesList.add(article);
			}
			rs.close();
			pst.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articlesList;
	}
	
	private int getNewArticleNO() {
		try {
			conn = dFact.getConnection();
			String sql = "SELECT max(articleNO) FROM t_board";
			System.out.println(sql);
			pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			if(rs.next())
				return(rs.getInt(1) + 1);
			rs.close();
			pst.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int insertNewArticle(ArticleVO article) {
		int articleNO = getNewArticleNO();
		try {
			conn=dFact.getConnection();
			int parentNO = article.getParentNO();
			String title = article.getTitle();
			String content = article.getContent();
			String id = article.getId();
			String imageFileName = article.getImageFileName();
			String sql = "INSERT INTO t_board (articleNO, parentNO, title, content, imageFileName, id)"
					+ " VALUES (?, ?, ?, ?, ?, ?)";
			System.out.println(sql);
			pst = conn.prepareStatement(sql);
			pst.setInt(1, articleNO);
			pst.setInt(2, parentNO);
			pst.setString(3, title);
			pst.setString(4, content);
			pst.setString(5, imageFileName);
			pst.setString(6, id);
			pst.executeUpdate();
			pst.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articleNO;
	}
	
	public ArticleVO selectArticle(int articleNO) {
		ArticleVO article = new ArticleVO();
		try {
			conn = dFact.getConnection();
			String sql = "SELECT articleNO, parentNO, title, content, imageFileName, id, writeDate"
					+ " FROM t_board"
					+ " WHERE articleNO=?";
			System.out.println(sql);
			pst = conn.prepareStatement(sql);
			pst.setInt(1, articleNO);
			ResultSet rs = pst.executeQuery();
			rs.next();
			int _articleNO = rs.getInt("articleNO");
			int parentNO = rs.getInt("parentNO");
			String title = rs.getString("title");
			String content = rs.getString("content");
			String imageFileName = rs.getString("imageFileName");
			String id = rs.getString("id");
			Date writeDate = rs.getDate("writeDate");
			
			article.setArticleNO(_articleNO);
			article.setParentNO(parentNO);
			article.setTitle(title);
			article.setContent(content);
			article.setImageFileName(imageFileName);
			article.setId(id);
			article.setWriteDate(writeDate);
			rs.close();
			pst.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return article;
	}
	
	public void updateArticle(ArticleVO article) {
		int articleNO = article.getArticleNO();
		String title = article.getTitle();
		String content = article.getContent();
		String imageFileName= article.getImageFileName();
		try {
			conn = dFact.getConnection();
			String sql = "UPDATE t_board SET title=?, content=?";
			System.out.println("imageFileName: " + imageFileName);
			System.out.println("imageFileName.length(): " + imageFileName.length());
			if(imageFileName != null && imageFileName.length()!=0) {
				sql += ", imageFileName=?";
				sql += " WHERE articleNO=?";
			} else {
				sql += " WHERE articleNO=?";
			}
			
			System.out.println(sql);
			pst = conn.prepareStatement(sql);
			pst.setString(1, title);
			pst.setString(2, content);
			if(imageFileName != null && imageFileName.length()!=0) {
				pst.setString(3, imageFileName);
				pst.setInt(4, articleNO);
			} else {
				pst.setInt(3, articleNO);
			}
			pst.executeUpdate();
			pst.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteArticle(int articleNO) {
		try {
			conn = dFact.getConnection();
			String sql = "DELETE FROM t_board";
			sql += " WHERE articleNO in (";
			sql += " SELECT articleNO FROM t_board";
			sql += " START WITH articleNO=?";
			sql += " CONNECT BY PRIOR articleNO = parentNO )";
			System.out.println(sql);
			pst = conn.prepareStatement(sql);
			pst.setInt(1, articleNO);
			pst.executeUpdate();
			pst.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Integer> selectRemovedArticles(int articleNO){
		List<Integer> articleNOList = new ArrayList<Integer>();
		try {
			conn = dFact.getConnection();
			String sql = "SELECT articleNO FROM t_board";
			sql += " START WITH articleNO = ?";
			sql += " CONNECT BY PRIOR articleNO = parentNO";
			System.out.println(sql);
			pst=conn.prepareStatement(sql);
			pst.setInt(1, articleNO);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				articleNO = rs.getInt("articleNO");
				articleNOList.add(articleNO);
			}
			pst.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articleNOList;
	}
	
	public int selectTotArticles() {
		try {
			conn = dFact.getConnection();
			String sql = "SELECT count(articleNO) FROM t_board";
			System.out.println(sql);
			pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			if(rs.next())
				return (rs.getInt(1));
			rs.close();
			pst.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
