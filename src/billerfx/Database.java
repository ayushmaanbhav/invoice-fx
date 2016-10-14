/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package billerfx;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Ayush Jain
 */
public class Database {
    
    static String adminpass = "";
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static String DB_URL;
    static String USER;
    static String PASS;
    static Connection conn = null;
    static Statement stmt = null;
    final static Object obj = new Object();
    
    public static void connect() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.jdbc.Driver");
        conn = (Connection) DriverManager.getConnection(DB_URL,USER,PASS);
        stmt = (Statement) conn.createStatement();
    }
    
    public static void disconnect()
    {
        try{
            if(stmt!=null)
                stmt.close();
        }catch(SQLException se2){}
        try{
            if(conn!=null)
                conn.close();
         }catch(SQLException se){}
    }
    
    public static String[] getCurrentSettings()
    {
        String r[] = null;
        synchronized(obj){
            try
            {
                connect();
                String sql = "SELECT * FROM Settings";
                ResultSet rs = stmt.executeQuery(sql);
                r = new String[16];
                while(rs.next()){
                    for(int i=0;i<16;i++)
                    {
                        r[i] = rs.getString(i+1);
                    }
                }
                rs.close();
            }catch(Exception e){
                r = null;
            }finally{
                disconnect();
            }
        }
        return r;
    }
    
    public static ObservableList get(String ss)
    {
        ObservableList r = null;
        synchronized(obj){
            try
            {
                connect();
                String sql = "SELECT * FROM "+ss;
                ResultSet rs = stmt.executeQuery(sql);
                r = FXCollections.observableArrayList();
                while(rs.next()){
                    r.add(new Type(rs.getString(1)));
                }
                rs.close();
            }catch(Exception e){
                r = null;
            }finally{
                disconnect();
            }
        }
        return r;
    }
    
    public static void add(String kk, String jj)
    {
        synchronized(obj){
            try
            {
                connect();
                String sql = "insert into "+kk+" values ('"+jj+"')";
                stmt.executeUpdate(sql);
            }catch(Exception e){
            }finally{
                disconnect();
            }
        }
    }
    
    public static void remove(String kk, String jj)
    {
        synchronized(obj){
            try
            {
                connect();
                String sql = "delete from "+kk+" where "+kk+" = '"+jj+"'";
                stmt.executeUpdate(sql);
            }catch(Exception e){
            }finally{
                disconnect();
            }
        }
    }
    
    public static boolean setCurrentSettings(String s[])
    {
        boolean r = false;
        synchronized(obj){
            try
            {
                connect();
                String sql;
                if(s.length == 15)
                    sql = "update settings set company_name = '"+s[0]+"', company_address = '"+s[1]+"', company_phone = '"+s[2]+"', tax1name = '"+s[3]+"', tax2name = '"+s[4]+"', tax1rate = '"+s[5]+"', tax2rate = '"+s[6]+"', usetax1 = '"+s[7]+"', usetax2 = '"+s[8]+"', smtpserver = '"+s[9]+"', port = '"+s[10]+"', email = '"+s[11]+"', username = '"+s[12]+"', password = '"+s[13]+"', `ssl` = '"+s[14]+"' where id = '1';";
                else
                    sql = "update settings set company_name = '"+s[0]+"', company_address = '"+s[1]+"', company_phone = '"+s[2]+"', tax1name = '"+s[3]+"', tax2name = '"+s[4]+"', tax1rate = '"+s[5]+"', tax2rate = '"+s[6]+"', usetax1 = '"+s[7]+"', usetax2 = '"+s[8]+"', smtpserver = '"+s[9]+"', port = '"+s[10]+"', email = '"+s[11]+"', username = '"+s[12]+"', password = '"+s[13]+"', `ssl` = '"+s[14]+"', `adminpass` = '"+s[15]+"' where id = '1';";
                int rs = stmt.executeUpdate(sql);
                r = rs == 1;
            }catch(Exception e){
                e.printStackTrace();
                r = false;
            }finally{
                disconnect();
            }
        }
        return r;
    }
    
    public static ObservableList getCustomers(String searchBy, String searchTerm)
    {
        ObservableList r = null;
        synchronized(obj){
            try
            {
                connect();
                String sql = "SELECT customer_name as name, customer_phone as phone, customer_email as email, customer_address as address, count( customer_name ) as visited FROM (select * from invoice ORDER BY id DESC) AS x GROUP BY customer_phone";
                if(searchBy != null && searchTerm != null && !searchTerm.trim().equals(""))
                {
                    searchBy = searchBy.toLowerCase();
                    sql = "select * from ( "+sql+" ) as y where "+searchBy+" regexp '(^| )"+searchTerm+".*'";
                }
                ResultSet rs = stmt.executeQuery(sql);
                r = FXCollections.observableArrayList();
                int i=1;
                while(rs.next()){
                    r.add(new Customer(i+"",rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));
                    i++;
                }
                rs.close();
            }catch(Exception e){
                r = null;
            }finally{
                disconnect();
            }
        }
        return r;
    }
    
    public static ObservableList getInvoices(String searchBy, String searchTerm)
    {
        ObservableList r = null;
        synchronized(obj){
            try
            {
                connect();
                String sql = "select * from invoice";
                if(searchBy != null && searchTerm != null && !searchTerm.trim().equals(""))
                {
                    searchBy = searchBy.toLowerCase();
                    if(searchBy.indexOf("name") != -1)
                        searchBy = "customer_name";
                    sql = "select * from ( "+sql+" ) as y where "+searchBy+" regexp '(^| )"+searchTerm+".*'";
                }
                ResultSet rs = stmt.executeQuery(sql);
                r = FXCollections.observableArrayList();
                while(rs.next()){
                    String name = rs.getString(1);
                    File f = File.createTempFile("billerfx"+name, ".pdf");
                    f.deleteOnExit();
                    try{
                        Blob bv = rs.getBlob(9);
                        InputStream is = bv.getBinaryStream();
                        FileOutputStream fw = new FileOutputStream(f);
                        int bbb;
                        while((bbb = is.read()) != -1)
                        {
                            fw.write(bbb);
                        }
                        fw.close();
                        is.close();
                    }catch(Exception ee){ee.printStackTrace();}
                    r.add(new Invoice(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),f,rs.getString(10)));
                }
                rs.close();
            }catch(Exception e){
                e.printStackTrace();
                r = null;
            }finally{
                disconnect();
            }
        }
        return r;
    }
    
    public static ObservableList getInventory(String searchBy, String searchTerm)
    {
        ObservableList r = null;
        synchronized(obj){
            try
            {
                connect();
                String sql = "SELECT * from items";
                if(searchBy != null && searchTerm != null && !searchTerm.trim().equals(""))
                {
                    searchBy = searchBy.toLowerCase();
                    sql = "select * from ( "+sql+" ) as y where "+searchBy+" regexp '(^| )"+searchTerm+".*'";
                }
                ResultSet rs = stmt.executeQuery(sql);
                r = FXCollections.observableArrayList();
                while(rs.next()){
                    r.add(new Item(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),"NA",rs.getString(9)));
                }
                rs.close();
                int yy = 0;
                while(yy < r.size()){
                    ResultSet rs2 = null;
                    if(((Item)r.get(yy)).getType().equalsIgnoreCase("drinks")||((Item)r.get(yy)).getCategory().equalsIgnoreCase("drinks"))
                    {
                        rs2 = stmt.executeQuery("select * from drinks where id = '"+((Item)r.get(yy)).getId()+"'");
                        Item it = (Item)r.remove(yy);
                        while(rs2.next()){
                            r.add(yy, new Item(it.getId(),it.getName(),it.getType(),it.getCategory(),it.getDiscount(),rs2.getString(3),it.getSoldmonth(),it.getTotalsold(),rs2.getString(2),it.getAvailable()));
                            yy++;
                        }
                        rs2.close();
                    }
                    else
                        yy++;
                }
            }catch(Exception e){
                e.printStackTrace();
                r = null;
            }finally{
                disconnect();
            }
        }
        return r;
    }
    
    public static ObservableList getInventory1(String searchBy, String searchTerm)
    {
        ObservableList r = null;
        synchronized(obj){
            try
            {
                connect();
                String sql = "SELECT * from items";
                if(searchBy != null && searchTerm != null && !searchTerm.trim().equals(""))
                {
                    searchBy = searchBy.toLowerCase();
                    sql = "select * from ( "+sql+" ) as y where "+searchBy+" regexp '(^| )"+searchTerm+".*'";
                }
                ResultSet rs = stmt.executeQuery(sql);
                r = FXCollections.observableArrayList();
                while(rs.next()){
                    r.add(new Item(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),"NA",rs.getString(9)));
                }
                rs.close();
            }catch(Exception e){
                e.printStackTrace();
                r = null;
            }finally{
                disconnect();
            }
        }
        return r;
    }
    
    public static ObservableList getInventory2(String id)
    {
        ObservableList r = null;
        synchronized(obj){
            try
            {
                connect();
                String sql = "select * from drinks where id = '"+id+"'";
                ResultSet rs = stmt.executeQuery(sql);
                r = FXCollections.observableArrayList();
                while(rs.next()){
                    r.add(new Drink(rs.getString(2),rs.getString(3)));
                }
                rs.close();
            }catch(Exception e){
                e.printStackTrace();
                r = null;
            }finally{
                disconnect();
            }
        }
        return r;
    }
    
    public static boolean deleteItemFromInventory(String s)
    {
        boolean r = false;
        synchronized(obj){
            try
            {
                connect();
                String sql = "delete from items where id = '"+s+"'";
                int rs = stmt.executeUpdate(sql);
                r = rs >= 1;
                if(r)
                {
                    sql = "delete from drinks where id = '"+s+"'";
                    rs = stmt.executeUpdate(sql);
                }
            }catch(Exception e){
                e.printStackTrace();
                r = false;
            }finally{
                disconnect();
            }
        }
        return r;
    }
    
    public static boolean deleteItemFromInvoices(String s)
    {
        boolean r = false;
        synchronized(obj){
            try
            {
                connect();
                String sql = "delete from invoice where id = '"+s+"'";
                int rs = stmt.executeUpdate(sql);
                r = rs >= 1;
            }catch(Exception e){
                e.printStackTrace();
                r = false;
            }finally{
                disconnect();
            }
        }
        return r;
    }
    
    public static int getNextItemID()
    {
        int r = -1;
        synchronized(obj){
            try
            {
                connect();
                String sql = "SELECT max(id) from items";
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    r = 1 + Integer.parseInt(rs.getString(1).trim());
                }
                rs.close();
            }catch(Exception e){
                r = -1;
            }finally{
                disconnect();
            }
        }
        return r;
    }
    
    public static int getNextInvoiceID()
    {
        int r = -1;
        synchronized(obj){
            try
            {
                connect();
                String sql = "SELECT max(id) from invoice";
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    r = 1 + Integer.parseInt(rs.getString(1).trim());
                }
                rs.close();
            }catch(Exception e){
                r = -1;
            }finally{
                disconnect();
            }
        }
        return r;
    }
    
    public static ObservableList getTypes()
    {
        ObservableList r = null;
        synchronized(obj){
            try
            {
                connect();
                String sql = "SELECT * from types";
                ResultSet rs = stmt.executeQuery(sql);
                r = FXCollections.observableArrayList();
                while(rs.next()){
                    r.add(rs.getString(1));
                }
                rs.close();
            }catch(Exception e){
                r = null;
            }finally{
                disconnect();
            }
        }
        return r;
    }
    
    public static ObservableList getCategories()
    {
        ObservableList r = null;
        synchronized(obj){
            try
            {
                connect();
                String sql = "SELECT * from category";
                ResultSet rs = stmt.executeQuery(sql);
                r = FXCollections.observableArrayList();
                while(rs.next()){
                    r.add(rs.getString(1));
                }
                rs.close();
            }catch(Exception e){
                r = null;
            }finally{
                disconnect();
            }
        }
        return r;
    }
    
    public static boolean updateColumnInDB(String tablename, String primarykeycol, String primarykey, String seckeycol, String seckey, String updatecol, String value)
    {
        boolean r = false;
        synchronized(obj){
            try
            {
                connect();
                String sql = "update "+tablename+" set "+updatecol+" = '"+value+"' where "+primarykeycol+" = '"+primarykey+"'";
                if(seckeycol!=null && seckey!=null)
                    sql += " && "+seckeycol+" = '"+seckey+"'";
                sql += ";";
                int rs = stmt.executeUpdate(sql);
                r = rs >= 1;
            }catch(Exception e){
                e.printStackTrace();
                r = false;
            }finally{
                disconnect();
            }
        }
        return r;
    }
    
    public static boolean addItem(String id,String  name,String  type,String  cate,String  dis,String  price,String soldmon,String totsold,String avai)
    {
        boolean r = false;
        synchronized(obj){
            try
            {
                connect();
                String sql = "insert into items values ('"+id+"','"+name+"','"+type+"','"+cate+"','"+dis+"','"+price+"','"+soldmon+"','"+totsold+"','"+avai+"');";
                int rs = stmt.executeUpdate(sql);
                r = rs >= 1;
            }catch(Exception e){
                e.printStackTrace();
                r = false;
            }finally{
                disconnect();
            }
        }
        return r;
    }
    
    public static boolean addDrink(String id,ObservableList ob)
    {
        boolean r = false;
        synchronized(obj){
            try
            {
                connect();
                String sql = "insert into drinks values ";
                int i = 0;
                while(i < ob.size())
                {
                    sql += "('"+id+"','"+((Drink)ob.get(i)).getGlasssize()+"','"+((Drink)ob.get(i)).getPrice()+"'),";
                    i++;
                }
                sql = sql.substring(0, sql.length()-1) + ";";
                int rs = stmt.executeUpdate(sql);
                r = rs >= 1;
            }catch(Exception e){
                e.printStackTrace();
                r = false;
            }finally{
                disconnect();
            }
        }
        return r;    
    }

    public static boolean saveBill(Invoice in)
    {
        boolean r = false;
        synchronized(obj){
            try
            {
                /*try
                {
                    connect();
                    String sql = "delete from invoice where id = '"+in.getId()+"'";
                    stmt.executeUpdate(sql);
                }catch(Exception e){
                }finally{
                    disconnect();
                }*/
                Class.forName("com.mysql.jdbc.Driver");
                conn = (Connection) DriverManager.getConnection(DB_URL,USER,PASS);
                conn.setAutoCommit(false);
                String sql = "insert into invoice values ('"+in.getId()+"','"+in.getName()+"','"+in.getPhone()+"','"+in.getEmail()+"','"+in.getAddress()+"','billerfx_"+in.getId()+"','pdf','0',?,NOW())";
                PreparedStatement ps = conn.prepareStatement(sql);
                FileInputStream fis = new FileInputStream(in.getFile());
                ps.setBinaryStream(1, fis, (int)in.getFile().length());
                int rs = ps.executeUpdate();
                r = rs == 1;
                conn.commit();
                ps.close();
                fis.close();
                conn.setAutoCommit(true);
            }catch(Exception e){
                //e.printStackTrace();
                r = false;
            }finally{
                disconnect();
            }
        }
        return r;
    }
    
}
