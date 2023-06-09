package sec03.brd02;

import java.util.List;

public class BoardService {
	BoardDAO boardDAO;
	
	public BoardService() {
		boardDAO = new BoardDAO();
		System.out.println("===>BoardService object created");
	}
	
	public List<ArticleVO> listArticles(){
		List<ArticleVO> articlesList = boardDAO.selectAllArticles();
		return articlesList;
	}
	
	public void addArticle(ArticleVO articleVO) {
		boardDAO.insertNewArticle(articleVO);
	}
}