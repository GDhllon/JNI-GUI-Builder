import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
 public class test extends JFrame implements testFieldEdit{
         public JLabel field0 = new JLabel("Name");
        public JTextField inp0 = new JTextField(15);
        public JLabel field1 = new JLabel("field");
        public JTextField inp1 = new JTextField(15);
        public JLabel field2 = new JLabel("moarfields");
        public JTextField inp2 = new JTextField(15);
        public JLabel field3 = new JLabel("evenmoarfields");
        public JTextField inp3 = new JTextField(15);
        public JTextArea results;
        public JButton button0 = new JButton("Testing");
        public JButton button1 = new JButton("Button");
        public JButton button2 = new JButton("Button2");
    public static void main(String args[]) {
        test t = new test();
        t.setVisible(true);
    }    public static int WIDTH = 500;
    public static int HEIGHT = 600;
    public test(){         super("test");
        setSize(WIDTH,HEIGHT);
        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        JPanel fieldPanel = new JPanel();
         fieldPanel.setLayout(new GridLayout(0,2));
        fieldPanel.add(field0);
        fieldPanel.add(inp0);
        fieldPanel.add(field1);
        fieldPanel.add(inp1);
        fieldPanel.add(field2);
        fieldPanel.add(inp2);
        fieldPanel.add(field3);
        fieldPanel.add(inp3);
        button0.addActionListener(new AddListener(this));
        buttonPanel.add(button0);
        button1.addActionListener(new BListener(this));
        buttonPanel.add(button1);
        button2.addActionListener(new AListener(this));
        buttonPanel.add(button2);
        add(fieldPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        results = new JTextArea(20,20);
        Font font = new Font("Verdana",Font.BOLD,16);
        results.setFont(font);
        JScrollPane scroll = new JScrollPane(results);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scroll, BorderLayout.SOUTH);
    }

    public void setDCName(String str) {
        field0.setText(str);
    }

    public String getDCName() {
        String str = field0.getText();
        return str;
    }

    public void setDCfield(String str) {
        field1.setText(str);
    }

    public String getDCfield() {
        String str = field1.getText();
        return str;
    }

    public void setDCmoarfields(String str) {
        field2.setText(str);
    }

    public String getDCmoarfields() {
        String str = field2.getText();
        return str;
    }

    public void setDCevenmoarfields(String str) {
        field3.setText(str);
    }

    public String getDCevenmoarfields() {
        String str = field3.getText();
        return str;
    }

    public void appendToStatusArea(String message){
        results.append(message);
    }
}
