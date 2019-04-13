package login_server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class Server {
  static Connection con;// 驱动程序名
  static String driver = "com.mysql.cj.jdbc.Driver";// URL指向要访问的数据库名mydata
  static String url =
      "jdbc:mysql://localhost:3306/login_server?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false";
  static String user = "root";
  static String passwd = "256bef256";
  static ServerSocket server;

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    try {
      server = new ServerSocket(9091);
      while (true) {
        Socket tmp = server.accept();
        InputStream is = tmp.getInputStream();
        OutputStream os = tmp.getOutputStream();
        byte[] cbuf = new byte[128];
        int i,len;
        while ((len=is.read(cbuf, 0, is.available())) <= 0) {
          // System.out.println("读取输入错误");
        }
        
        byte[] real_bytes=new byte[len];
        for(i=0;i<len;i++) {
          real_bytes[i]=cbuf[i];
        }
        String recv = new String(real_bytes);
        // System.out.println(recv);
        String[] parts = recv.split("\\|");
        if (parts.length == 3) {
          String username = parts[0];
          String hash2 = parts[1];
          String random = parts[2];

          Class.forName(driver);
          // 1.getConnection()方法，连接MySQL数据库！！
          con = DriverManager.getConnection(url, user, passwd);
          // if (!con.isClosed())
          // System.out.println("Succeeded connecting to the Database!");
          Statement statement = con.createStatement();
          String sql = "select * from users where username='" + username + "'";
          ResultSet rs = statement.executeQuery(sql);

          rs.next();
          String server_hash1 = rs.getString("hash");
          String server_hash2 = "" + (server_hash1 + "|" + random).hashCode();
          if (server_hash2.equals(hash2)) {

            // String code="这是一个加密/解密测试，用于网络安全第四次实验。";
            String code = random; // 按照实验要求传回认证码
            // System.out.println(CoderUtil.encode(code,server_hash1,"DES"));
            os.write(AES.AESEncode(code, server_hash1).getBytes());
            os.flush();
            //
            // Scanner in = new Scanner(System.in);
            // System.out.println("输入要传输的文件：");
            // sendFile(in.nextLine(),tmp.getOutputStream(),server_hash1);

          } else {
            os.write("密码错误".getBytes());
          }
        } else if (parts.length == 2) { // 修改密码
          String username = parts[0];
          String hash2 = parts[1];

          Class.forName(driver);
          // 1.getConnection()方法，连接MySQL数据库！！
          con = DriverManager.getConnection(url, user, passwd);
          // if (!con.isClosed())
          // System.out.println("Succeeded connecting to the Database!");
          Statement statement = con.createStatement();
          String sql = "select * from users where username='" + username + "'";
          ResultSet rs = statement.executeQuery(sql);

          rs.next();
          String server_hash1 = rs.getString("hash");
          String new_hash = AES.AESDecode(hash2, server_hash1);

          if (new_hash != null) {
            statement.execute(
                "update users set hash='" + new_hash + "' where username='" + username + "'");

            os.write("修改成功".getBytes());
            os.flush();
          } else {
            os.write("修改失败".getBytes());
          }
        }
        os.close();
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  // private static void sendFile(String src,OutputStream os,String key) throws IOException {
  //
  // FileReader reader = new FileReader(src);
  // BufferedReader br = new BufferedReader(reader);
  // String tmp2;
  // while((tmp2=br.readLine())!=null) {
  // os.write(AES.AESEncode(tmp2,key).getBytes());
  // }
  // br.close();
  // }
}
