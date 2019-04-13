package login_server;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import jdk.nashorn.internal.scripts.JO;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;

public class ChangePasswd extends JFrame {

  private JPanel contentPane;
  private JTextField textField;
  private JPasswordField passwordField;
  private JLabel label_2;
  private JPasswordField passwordField_1;
  private JLabel label_3;
  private JPasswordField passwordField_2;
  private JButton button;
  private JButton button_1;
  private Socket socket;

//  /**
//   * Launch the application.
//   */
//  public static void main(String[] args) {
//    EventQueue.invokeLater(new Runnable() {
//      public void run() {
//        try {
//          ChangePasswd frame = new ChangePasswd();
//        } catch (Exception e) {
//          e.printStackTrace();
//        }
//      }
//    });
//  }

  /**
   * Create the frame.
   */
  public ChangePasswd() {
    setTitle("修改密码");
    setVisible(true);
    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    setBounds(100, 100, 450, 361);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    contentPane.setLayout(null);

    JLabel label = new JLabel("\u7528\u6237\u540D\uFF1A");
    label.setBounds(104, 51, 72, 18);
    contentPane.add(label);

    textField = new JTextField();
    textField.setBounds(215, 48, 86, 24);
    contentPane.add(textField);
    textField.setColumns(10);

    JLabel label_1 = new JLabel("\u65E7\u5BC6\u7801\uFF1A");
    label_1.setBounds(104, 94, 72, 18);
    contentPane.add(label_1);

    passwordField = new JPasswordField();
    passwordField.setBounds(215, 91, 86, 24);
    contentPane.add(passwordField);

    label_2 = new JLabel("\u65B0\u5BC6\u7801\uFF1A");
    label_2.setBounds(104, 145, 72, 18);
    contentPane.add(label_2);

    passwordField_1 = new JPasswordField();
    passwordField_1.setBounds(215, 142, 86, 24);
    contentPane.add(passwordField_1);

    label_3 = new JLabel("\u786E\u8BA4\u65B0\u5BC6\u7801\uFF1A");
    label_3.setBounds(104, 192, 90, 18);
    contentPane.add(label_3);

    passwordField_2 = new JPasswordField();
    passwordField_2.setBounds(215, 189, 86, 24);
    contentPane.add(passwordField_2);

    button = new JButton("\u786E\u8BA4");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String username = textField.getText();
        String passwd1 = passwordField.getText();
        String new_passwd = passwordField_1.getText();
        String new_passwd2 = passwordField_2.getText();
        if (username.equals("") || passwd1.equals("") || new_passwd.equals("")
            || new_passwd2.equals("")) {
          JOptionPane.showMessageDialog(contentPane, "请将信息填写完整");
          return;
        }

        if (!new_passwd.equals(new_passwd2)) {
          JOptionPane.showMessageDialog(contentPane, "新密码两次输入不一致，请再次确认");
          return;
        }

        try {
          socket = new Socket("localhost", 9091);
          String hash1 = SHA256.getSHA256(username + "|" + passwd1);
          String hash2 = SHA256.getSHA256(username + "|" + new_passwd);
          OutputStream os= socket.getOutputStream();
          InputStream is=socket.getInputStream();
          os.write((username+"|"+AES.AESEncode(String.valueOf(hash2), String.valueOf(hash1))).getBytes());
          
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
          JOptionPane.showMessageDialog(contentPane, result);
          System.out.println(result);
          
          
        } catch (UnknownHostException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    });
    button.setBounds(66, 247, 113, 27);
    contentPane.add(button);

    button_1 = new JButton("\u53D6\u6D88");
    button_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        setVisible(false);
      }
    });
    button_1.setBounds(248, 247, 113, 27);
    contentPane.add(button_1);
  }

}
