package sec01.ex01;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {
	private PreparedStatement pst;
	private Connection conn;
	private DataSource dFact;
	
	public MemberDAO() {
		try {
			Context ctx = new InitialContext();
			Context envCtx = (Context) ctx.lookup("java:/comp/env");
			dFact = (DataSource) envCtx.lookup("jdbc/oracle");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public List<MemberVO> listMembers() {
		List<MemberVO> membersList = new ArrayList<MemberVO>();
		try {
			conn = dFact.getConnection();
			String sql = "select * from t_member order by joinDate desc";
			System.out.println(sql);
			pst=conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String id= rs.getString("id");
				String pwd= rs.getString("pwd");
				String name= rs.getString("name");
				String email= rs.getString("email");
				Date joinDate = rs.getDate("joinDate");
				MemberVO memberVO = new MemberVO(id, pwd, name, email, joinDate);
				membersList.add(memberVO);
			}
			rs.close();
			pst.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return membersList;
	}
	
	public void addMember(MemberVO memberVO) {
		try {
			conn=dFact.getConnection();
			String id= memberVO.getId();
			String pwd= memberVO.getPwd();
			String name= memberVO.getName();
			String email= memberVO.getEmail();
			
			String sql = "insert into t_member";
			sql += " (id, pwd, name, email)";
			sql += " values(?, ?, ?, ?)";
			System.out.println(sql);
			pst = conn.prepareStatement(sql);
			pst.setString(1, id);
			pst.setString(2, pwd);
			pst.setString(3, name);
			pst.setString(4, email);
			pst.executeUpdate();
			pst.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}

