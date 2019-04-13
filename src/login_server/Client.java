package login_server;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class Client extends JFrame {
  private JPanel contentPane;
  private JTextField textField;
  private JPasswordField passwordField;
  private Socket socket;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          Client frame = new Client();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the frame.
   */
  public Client() {
    setVisible(true);
    setTitle("登录");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 450, 380);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    contentPane.setLayout(null);
    
    JLabel label = new JLabel("\u7528\u6237\u540D\uFF1A");
    label.setBounds(113, 92, 72, 18);
    contentPane.add(label);
    
    textField = new JTextField();
    textField.setBounds(199, 89, 122, 24);
    contentPane.add(textField);
    textField.setColumns(10);
    
    JLabel label_1 = new JLabel("\u5BC6\u7801\uFF1A");
    label_1.setBounds(113, 154, 72, 18);
    contentPane.add(label_1);
    
    passwordField = new JPasswordField();
    passwordField.setBounds(199, 151, 122, 24);
    contentPane.add(passwordField);
    
    JButton button = new JButton("\u767B\u5F55");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        try {
          String username=textField.getText();
          String passwd=passwordField.getText();
          if(username.equals("")||passwd.equals("")) {
            JOptionPane.showMessageDialog(contentPane, "请将信息填写完成");
            return;
          }
          socket=new Socket("localhost",9091);
          
          if(username.contains("|")) {
            JOptionPane.showMessageDialog(contentPane, "非法用户名");
            return;
          }
          String hash1=SHA256.getSHA256(username+"|"+passwd);
//          System.out.println(hash1);
          String random=""+(int)(Math.random()*100000);
          String hash2=SHA256.getSHA256(hash1+"|"+random);
          OutputStream os= socket.getOutputStream();
          InputStream is=socket.getInputStream();
          os.write((username+"|"+hash2+"|"+random).getBytes());
          
          byte[] cbuf=new byte[256];
          int i,len;
          while((len=is.read(cbuf,0,is.available()))<=0) {
//            System.out.println("读取输入错误");
          }
          byte[] real_bytes=new byte[len];
          for(i=0;i<len;i++) {
            real_bytes[i]=cbuf[i];
          }
          String result=new String(real_bytes);
          System.out.println(result);
          if(!result.equals("密码错误")) {
            JOptionPane.showMessageDialog(contentPane, "登录成功");
//            System.out.println(hash1);
            String real_content=AES.AESDecode(result, String.valueOf(hash1));
            FileOutputStream fos=new FileOutputStream(new File("lab4_decode.txt"));
            fos.write(real_content.getBytes());
//            fos.write("\n".getBytes());
//            while((len=is.read(cbuf,0,is.available()))!=-1) {
////              System.out.println("读取输入错误");
//              real_bytes=new byte[len];
//              for(i=0;i<len;i++) {
//                real_bytes[i]=cbuf[i];
//              }
//              fos.write(AES.AESDecode(result, real_content).getBytes());
//            }
            fos.close();
            
//            FileWriter fw=new FileWriter("recv_file.txt");
//            cbuf=new byte[1024];
//            while((len=is.read(cbuf))!=-1) {
//              real_bytes=new byte[len];
//              for(i=0;i<len;i++) {
//                real_bytes[i]=cbuf[i];
//              }
//              fw.write(AES.AESDecode(new String(real_bytes),String.valueOf(hash1)));
//            }
//            fw.close();
            
          }else {
            JOptionPane.showMessageDialog(contentPane, "密码错误");
          }
          os.close();
          is.close();
          socket.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    });
    button.setBounds(72, 241, 113, 27);
    contentPane.add(button);
    
    JButton button_1 = new JButton("\u53D6\u6D88");
    button_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });
    button_1.setBounds(246, 241, 113, 27);
    contentPane.add(button_1);
    
    JButton btnNewButton = new JButton("<html><u>修改密码</u></html>");
    btnNewButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        ChangePasswd frame=new ChangePasswd();
      }
    });
    btnNewButton.setBounds(159, 306, 113, 27);
    btnNewButton.setIconTextGap(0);//将标签中显示的文本和图标之间的间隔量设置为0  
    btnNewButton.setBorderPainted(false);//不打印边框  
    btnNewButton.setBorder(null);//除去边框  
//    button.setText(null);//除去按钮的默认名称  
    btnNewButton.setFocusPainted(false);//除去焦点的框  
    btnNewButton.setContentAreaFilled(false);//除去默认的背景填充 
    contentPane.add(btnNewButton);
  }
}
