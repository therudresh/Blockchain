package com.rudramanish.dumpster.daoObjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rudramanish.dumpster.dao.UserInfoDao;

@Repository
public class AccountRelatedDao {
	
	private DatabaseDaoQuery daoMain;


	@Autowired
	public void setDatabaseDaoQuery(DatabaseDaoQuery dao){
		this.daoMain = dao;
	}
	
	public String addUser(UserInfoDao userRecord){
		String state = "failed";
		String sql = "insert into userrecord values(?,?,?)";
		Connection con = null;
		PreparedStatement stmt = null;
		try{
			con= this.daoMain.getConnection();
			stmt =  con.prepareStatement(sql);
			stmt.setString(1,userRecord.getClue());
			stmt.setString(2, userRecord.getAddress());
			stmt.setInt(3, userRecord.getPinCode());
			stmt.executeUpdate(sql);
			daoMain.commitConnection(con);
			state = "Success";
		}catch(SQLException e){
			System.out.println("*******user record not updated sql error:\n"+ e);
		}finally{
			this.daoMain.stmtClose(stmt);
			this.daoMain.releaseConnection(con);
		}
		return state;
	}
	
	public String authenticate(UserInfoDao userRecord){
		String state = "failed";
		String sql = "select count(*) from userrecord where clue=?";
		PreparedStatement stmt = null;
		ResultSet set = null;
		Connection con = null;
		try{
			con = this.daoMain.getConnection();
			stmt = con.prepareStatement(sql);
			stmt.setString(1,userRecord.getClue());
			set = stmt.executeQuery();
			while(set.next()){
				if(set.getInt(1)== 1){
					state = "success";
				}
			}
		}catch(SQLException e){
			System.out.println("*******error from authenticate:\n"+ e);
		}finally{
			this.daoMain.stmtClose(stmt);
			this.daoMain.resultSetClose(set);
			this.daoMain.releaseConnection(con);
		}
		return state;
	}
	
	
	
}

