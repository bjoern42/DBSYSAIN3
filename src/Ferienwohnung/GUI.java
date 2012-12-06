package Ferienwohnung;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import FillDatabase.FillDatabase.JOptionPaneMultiInput;

public class GUI extends JFrame implements ActionListener{
private static final long serialVersionUID = 1L;
private JTextField tfLand = new JTextField(), tfZimmer = new JTextField(),tfDatumAn = new JTextField(),tfDatumAb = new JTextField(),tfAusstattung = new JTextField();
private JLabel lbLand = new JLabel("Land:"), lbZimmer = new JLabel("Anzahl Min. Zimmer:"),lbDatumAn = new JLabel("Datum Ankunft:"),lbDatumAb = new JLabel("Datum Abfahrt:"),lbAusstattung = new JLabel("Ausstattung(optional):");
private JButton btSuchen = new JButton("Suchen"),btBuchen = new JButton("Buchen");
private DefaultListModel<Ferienwohnung> model = new DefaultListModel<Ferienwohnung>();
private JList<Ferienwohnung> list = new JList<Ferienwohnung>(model);
private JPanel pNorth = new JPanel(),pSouth = new JPanel();;
private Database database;

	public static void main(String[] args) {
		new GUI();
	}
	
	public GUI(){
		super("Ferienwohnung Suchen");
		try {
			database = new Database(this, "dbsys22", "admin22");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		setSize(500,500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pNorth.setLayout(new GridLayout(5,2));
		pNorth.add(lbLand);
		pNorth.add(tfLand);
		pNorth.add(lbZimmer);
		pNorth.add(tfZimmer);
		pNorth.add(lbDatumAn);
		pNorth.add(tfDatumAn);
		pNorth.add(lbDatumAb);
		pNorth.add(tfDatumAb);
		pNorth.add(lbAusstattung);
		pNorth.add(tfAusstattung);
		pSouth.add(btSuchen);
		pSouth.add(btBuchen);
		btSuchen.addActionListener(this);
		btBuchen.addActionListener(this);
		add(BorderLayout.NORTH,pNorth);
		add(BorderLayout.CENTER,new JScrollPane(list));
		add(BorderLayout.SOUTH,pSouth);
		setVisible(true);
	}

	public void addFerienwohnung(String pId, String pName){
		model.addElement(new Ferienwohnung(pId, pName));
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == btSuchen){
			model.clear();
			try {
				database.getFerienwohnungen(tfLand.getText(), tfZimmer.getText(), tfDatumAn.getText(), tfDatumAb.getText(), tfAusstattung.getText());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			try {
				String data[] = JOptionPaneMultiInput.showInputDialog(this, "Buchungsdaten eingeben", JOptionPane.OK_OPTION, 3, "KundenID","Datum Anreise","Datum Abreise");
				database.ferienwohnungBuchen(list.getSelectedValue().getID(), data[0], data[1], data[2]);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class Ferienwohnung{
		private String id,name;
		
		public Ferienwohnung(String pId,String pName){
			id = pId;
			name = pName;
		}
		
		public String getID(){
			return id;
		}
		
		@Override
		public String toString(){
			return name;
		}
	}
}
