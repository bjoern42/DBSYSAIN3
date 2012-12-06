package Ferienwohnung;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database {
private Connection conn;
private Statement stmt;
private ResultSet rs;	
	
	public Database(String user,String pass) throws ClassNotFoundException, SQLException{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		String url= "jdbc:oracle:thin:@oracle11g.in.htwg-konstanz.de:1521:ora11g";
		conn= DriverManager.getConnection(url,user,pass); 
		stmt = conn.createStatement();
		conn.setAutoCommit(false);
	}
	
	public String[] getFerienwohnungen(String pLand,String pZimmer,String pDatumAn, String pDatumAb, String pAusstattung) throws SQLException{
		String sql = 	"SELECT f.ferienwohnungs_ID,f.Name"+
						" FROM (((dbsys22.Ferienwohnung f INNER JOIN dbsys22.Land l ON f.Land_ID = l.Land_ID)"+
						" INNER JOIN dbsys22.istAusgestattetMit iau ON f.Ferienwohnungs_ID = iau.Ferienwohnungs_ID)"+
						" INNER JOIN dbsys22.Ausstattung au ON iau.Ausstattungs_Name = au.name)"+
						" WHERE zimmer = "+pZimmer+" AND l.Name = '"+pLand+"'"+
						" AND f.ferienwohnungs_ID NOT IN"+
						" (SELECT ferienwohnungs_ID"+ 
						" FROM dbsys22.Buchung"+
						" WHERE datum_bis > '"+pDatumAn+"' AND datum_von < '"+pDatumAb+"')";
		
		if(!pAusstattung.isEmpty()){
			sql+= " AND au.name = '"+pAusstattung+"'";
		}
		System.out.println(sql);
		rs = stmt.executeQuery(sql); 
		while(rs.next()){
			System.out.println(rs.getString(0)+rs.getString(1));
		}
		return null;
	}
	
	public void ferienwohnungBuchen(String pFerienwohnungsID, String pKundenID, String pDatumAn, String pDatumAb) throws SQLException{
		String sql = "INSERT INTO Buchung (Buchungs_ID,Kunden_ID,Ferienwohnungs_ID,Datum_Von,Datum_Bis) VALUES ("+getMaxID("Buchung")+","+pKundenID+","+pFerienwohnungsID+","+pDatumAn+","+pDatumAb;
		System.out.println(sql);
		stmt.executeUpdate(sql);
	}
	
	public int getMaxID(String pTableName) throws SQLException{
		String sql = "SELECT count(*) FROM "+pTableName;
		rs = stmt.executeQuery(sql); 
		rs.next();
		return rs.getInt("count(*)");
	}
}
