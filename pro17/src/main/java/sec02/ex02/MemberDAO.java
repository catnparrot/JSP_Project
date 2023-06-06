package sec02.ex02;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return membersList;
	}
	
	public void addMember(MemberVO m) {
		try {
			conn=dFact.getConnection();
			String id= m.getId();
			String pwd= m.getPwd();
			String name= m.getName();
			String email= m.getEmail();
			
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public MemberVO findMember(String _id) {
		MemberVO memInfo = null;
		try {
			conn = dFact.getConnection();
			String sql = "select * from t_member where id=?";
			pst=conn.prepareStatement(sql);
			pst.setString(1, _id);
			System.out.println(sql);
			ResultSet rs = pst.executeQuery();
			rs.next();
			String id= rs.getString("id");
			String pwd= rs.getString("pwd");
			String name= rs.getString("name");
			String email= rs.getString("email");
			Date joinDate = rs.getDate("joinDate");
			memInfo = new MemberVO(id, pwd, name, email, joinDate);
			MemberVO memberVO = new MemberVO(id, pwd, name, email, joinDate);
			pst.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return memInfo;
	}
	
	public void modMember(MemberVO memberVO) {
		String id = memberVO.getId();
		String pwd = memberVO.getPwd();
		String name = memberVO.getName();
		String email = memberVO.getEmail();
		try {
			conn = dFact.getConnection();
			String sql = "UPDATE t_member set pwd=?, name=?, email=? where id=?";
			System.out.println(sql);
			pst = conn.prepareStatement(sql);
			pst.setString(1, pwd);
			pst.setString(2, name);
			pst.setString(3, email);
			pst.setString(4, id);
			pst.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void delMember(String id) {
		try {
			conn=dFact.getConnection();
			String sql = "DELETE FROM t_member where id=?";
			System.out.println(sql);
			pst=conn.prepareStatement(sql);
			pst.setString(1, id);
			pst.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

