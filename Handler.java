/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.*;

/**
 *
 * @author Reza Nourbakhsh
 */
public class Contact {

 public void ServerProcess()
 {
            try
                {
                    String STUNAME="";
                    String ADDRESS="";
                    String POSTCODE="";
                    String EMAIL="";


                    DatagramSocket serverSocket = new DatagramSocket(9875);
                    byte[] receiveData = new byte[1024];
                    byte[] sendData = new byte[1024];
                    while(true)
                    {
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        serverSocket.receive(receivePacket);
                        String sentence = new String( receivePacket.getData());
                        System.out.println("RECEIVED:" + sentence);
                        InetAddress IPAddress = receivePacket.getAddress();
                        int port = receivePacket.getPort();
                        //Code to retreive data from database
						boolean FindFlag = false;
                        try
                        {
                            String filename = "STUCONTACT.mdb";
                            String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
                            database+= filename.trim() ;

                            Connection con = DriverManager.getConnection(database,"","");

                            Statement stmt = con.createStatement();

                            ResultSet rs = stmt.executeQuery("SELECT STUNAME,ADDRESS,POSTCODE,EMAIL FROM Contacts where STUID='"+sentence.trim()+"'");

                            while (rs.next()) {

                                STUNAME = rs.getString("STUNAME");
                                ADDRESS = rs.getString("ADDRESS");
                                POSTCODE = rs.getString("POSTCODE");
                                EMAIL = rs.getString("EMAIL");
							    FindFlag = true;
                            }

                        }
                        catch(Exception e)
                        {
                            System.out.println("Error Ocurrs");
                        }
                        if(FindFlag == true)
                        {
                    	    sentence =  STUNAME + ADDRESS + POSTCODE + EMAIL ;
					    }
					    else
					    {
							sentence =  "There is no student with this code";
						}
                        String capitalizedSentence = sentence;

                        //************************************************
                        sendData = capitalizedSentence.getBytes();
                        DatagramPacket sendPacket =
                        new DatagramPacket(sendData, sendData.length, IPAddress, port);
                        serverSocket.send(sendPacket);
                        //************************************************
                    }
                }
                catch(Exception e)
                {

                }

            }

}
