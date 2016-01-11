/*Name: Gary Dhillon 
 * Date: Friday, February 13, 2015 
 * Assignment 2*/

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.event.InputEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MenuEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.KeyStroke;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ClassLoader;

public class Window extends JFrame implements ActionListener {
	/*Window dimensions*/
	public static int WIDTH = 700;
    public static int HEIGHT = 800;

    private JTextArea results;
    private String javaCompiler = "javac";
    private String runEnv = "java";
    private String cOptions = null;
    private String rOptions = null;
    private String directory = ".";
    private final JLabel doc = new JLabel("Current Project: Default.config");
    private String fileName = "Default.config";
    private JMenu configMenu = new JMenu("Config");
    private JMenuItem jCompiler = new JMenuItem("Java Compiler " + "("+ javaCompiler + ")");
    private JMenuItem compilerOptions = new JMenuItem("Compile Options " + "("+ cOptions + ")");
    private JMenuItem jRunTime = new JMenuItem("Java Run-Time " + "(" + runEnv +")");
    private JMenuItem runOptions = new JMenuItem("Run-Time Options " + "(" + rOptions + ")");
    private JMenuItem workDir = new JMenuItem("Working Directory: "+ "(" + directory + ")");
    private String compiler = "lex/yacc Compiler";

	/*JNI functions prototyped*/
    private native String[] getFields(String fileName);
    private native String[] getNames(String[] manage, int size, String fileName);
    private native String[] lexParse(String fileName);
    static{System.loadLibrary("JNIpm");}


	public Window(String command){
        
        super(command);
        /*Window menus, buttons and shortcuts initialized*/        
        JMenu fileMenu = new JMenu("File");
        JMenuItem newP = new JMenuItem("New");
        JMenuItem openP = new JMenuItem("Open");
        JMenuItem saveP = new JMenuItem("Save");
        JMenuItem saveAsP = new JMenuItem("Save as...");
        JMenuItem quit = new JMenuItem("Quit");
        fileMenu.setMnemonic('F');
        newP.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
        openP.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
        saveP.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
        saveAsP.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_DOWN_MASK));
        quit.setAccelerator(KeyStroke.getKeyStroke('Q', InputEvent.CTRL_DOWN_MASK));

        newP.addActionListener(new MenuActionListener());
        openP.addActionListener(new MenuActionListener());
        saveP.addActionListener(new MenuActionListener());
        saveAsP.addActionListener(new MenuActionListener());
        quit.addActionListener(new MenuActionListener());
        JPanel statusBar = new JPanel();
        JPanel document = new JPanel();
        statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusBar.setPreferredSize(new Dimension(this.getWidth(),22));
        
        setLayout(new BorderLayout());

        statusBar.add(doc);
        fileMenu.add(newP);
        fileMenu.add(openP);
        fileMenu.add(saveP);
        fileMenu.add(saveAsP);
        fileMenu.add(quit);
        
        JMenu compileMenu = new JMenu("Compile");
        JMenuItem compileP = new JMenuItem("Compile");
        JMenuItem compileRun = new JMenuItem("Compile and run");
        JMenuItem compilerSelect = new JMenuItem("Compile Mode");
        compileP.addActionListener(new MenuActionListener());
        compileRun.addActionListener(new MenuActionListener());
        compilerSelect.addActionListener(new MenuActionListener());
        compileMenu.setMnemonic('C');
        compileP.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_DOWN_MASK));
        compileRun.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_DOWN_MASK));
        compilerSelect.setAccelerator(KeyStroke.getKeyStroke('P', InputEvent.CTRL_DOWN_MASK));
        compileMenu.add(compileP);
        compileMenu.add(compileRun);
        
        jCompiler.addActionListener(new MenuActionListener());
        compilerOptions.addActionListener(new MenuActionListener());
        jRunTime.addActionListener(new MenuActionListener());
        runOptions.addActionListener(new MenuActionListener());
        workDir.addActionListener(new MenuActionListener());
        configMenu.setMnemonic('O');
        jCompiler.setAccelerator(KeyStroke.getKeyStroke('J', InputEvent.CTRL_DOWN_MASK));
        compilerOptions.setAccelerator(KeyStroke.getKeyStroke('K', InputEvent.CTRL_DOWN_MASK));
        jRunTime.setAccelerator(KeyStroke.getKeyStroke('T', InputEvent.CTRL_DOWN_MASK));
        runOptions.setAccelerator(KeyStroke.getKeyStroke('U', InputEvent.CTRL_DOWN_MASK));
        runOptions.setAccelerator(KeyStroke.getKeyStroke('W', InputEvent.CTRL_DOWN_MASK));
        
        configMenu.add(jCompiler);
        configMenu.add(compilerOptions);
        configMenu.add(jRunTime);
        configMenu.add(runOptions);
        configMenu.add(workDir);
        configMenu.add(compilerSelect);
        
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        JMenuItem help = new JMenuItem("Help");
        help.addActionListener(new MenuActionListener());
        help.setAccelerator(KeyStroke.getKeyStroke('H', InputEvent.CTRL_DOWN_MASK));
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(new MenuActionListener());
        about.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_DOWN_MASK));
        
        helpMenu.add(help);
        helpMenu.add(about);
        
        JMenuBar mainBar = new JMenuBar();
        mainBar.add(fileMenu);
        mainBar.add(compileMenu);
        mainBar.add(configMenu);
        mainBar.add(helpMenu);
        
        setJMenuBar(mainBar);
        
        JToolBar buttonBar = new JToolBar("Buttons");
        JButton newDoc = new JButton();
        newDoc.setIcon(new ImageIcon("icon/NewPage.png"));
        newDoc.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
				if(doc.toString().equals("Current Project: " + fileName))
				{
					String n = JOptionPane.showInputDialog(new JFrame(), "Please enter a new file name:");
					results.setText("");
					fileName = n;
					if(!fileName.contains(".config"))
					{
						fileName = fileName + ".config";
					}
					File f = new File(fileName);
					try {
						f.createNewFile();
					} catch (IOException g) {
						// TODO Auto-generated catch block
						g.printStackTrace();
					}
					doc.setText("Current Project: " + fileName);
				}
				else
				{
					saveFile(0);
					doc.setText("Current Project: " + fileName);
					results.setText("");
					String n = JOptionPane.showInputDialog(new JFrame(), "Please enter a new file name:");
					fileName = n;
					if(!fileName.contains(".config"))
					{
						fileName = fileName + ".config";
					}
					File f = new File(fileName);
					try {
						f.createNewFile();
					} catch (IOException g) {
						// TODO Auto-generated catch block
						g.printStackTrace();
					}
					doc.setText("Current Project: " + fileName);
				}
        	} 
        });
        JButton open = new JButton();
        open.setIcon(new ImageIcon("icon/Folder.png"));
        open.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		openFile();
        	}
        });
        JButton save = new JButton();
        save.setIcon(new ImageIcon("icon/Floppy.png"));
        save.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
				if(fileName.equals("Default.config"))
				{
					saveFile(0);
				}
				else
				{
					saveFile(1);
				}
        	}
        });
        JButton compile = new JButton();
        compile.setIcon(new ImageIcon("icon/Compiler.png"));
        compile.addActionListener(new ActionListener()
        {
			public void actionPerformed(ActionEvent e)
			{
				if(compiler.equals("lex/yacc Compiler"))
				{
					lexCompile(0);
				}
				else if(compiler.equals("IDE Compiler"))
				{
					compile(0);
				}
			}
		});
		
        JButton run = new JButton();
        run.setIcon(new ImageIcon("icon/run.gif"));
        run.addActionListener(new ActionListener()
        {
			public void actionPerformed(ActionEvent e)
			{
				if(compiler.equals("lex/yacc Compiler"))
				{
					lexCompile(1);
				}
				else if(compiler.equals("IDE Compiler"))
				{
					compile(1);
				}
					
			}
		});
        
        buttonBar.add(newDoc);
        buttonBar.add(open);
        buttonBar.add(save);
        buttonBar.add(compile);
        buttonBar.add(run);
        buttonBar.setFloatable(false);
                
        setSize(WIDTH,HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
        
        add(buttonBar, BorderLayout.PAGE_START);        
        
        results = new JTextArea(47,30);
        results.getDocument().addDocumentListener(new DocumentModify());
        Font font = new Font("Verdana",Font.BOLD,16);
        results.setFont(font);
        JScrollPane scroll = new JScrollPane(results);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);        
        
        document.setLayout(new BorderLayout());
        document.add(scroll, BorderLayout.CENTER);
        document.add(statusBar,BorderLayout.PAGE_END);
        add(document, BorderLayout.CENTER);
        
        
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	/*Menu Bar listener*/
	class MenuActionListener implements ActionListener{
		
		JFrame frame = new JFrame();
		
		public void actionPerformed(ActionEvent e)
		{
			String command = e.getActionCommand();
    		
    		if(command.equals("New"))
    		{
    			results.setText("");
        		doc.setText("Default.config");		
    		}
    		else if(command.equals("Open"))
    		{
    			openFile();
    		}
    		else if(command.equals("Save"))
    		{
    			saveFile(1);
    		}
    		else if(command.equals("Save as..."))
    		{
    			saveFile(0);
    		}
    		else if(command.equals("Compile"))
    		{
				if(compiler.equals("lex/yacc Compiler"))
				{
					lexCompile(0);
				}
				else if(compiler.equals("IDE Compiler"))
				{
					compile(0);
				}
			}
			else if(command.equals("Compile and run"))
			{
				if(compiler.equals("lex/yacc Compiler"))
				{
					lexCompile(1);
				}
				else if(compiler.equals("IDE Compiler"))
				{
					compile(1);
				}
			}
			else if(command.equals("Java Compiler " + "("+ javaCompiler + ")"))
			{
				JFileChooser pathSelect = new JFileChooser();
				int result = pathSelect.showOpenDialog(Window.this);
				if(result == JFileChooser.APPROVE_OPTION)
				{
					javaCompiler = pathSelect.getSelectedFile().getAbsolutePath();
					JOptionPane.showMessageDialog(frame, "File Path: " + javaCompiler);
					jCompiler.setText("Java Compiler " + "("+ javaCompiler + ")");
				}
			}
			else if(command.equals("Java Run-Time " + "(" + runEnv +")"))
			{
				JFileChooser pathSelect = new JFileChooser();
				int result = pathSelect.showOpenDialog(Window.this);
				if(result == JFileChooser.APPROVE_OPTION)
				{
					javaCompiler = pathSelect.getSelectedFile().getAbsolutePath();
					JOptionPane.showMessageDialog(frame, "File Path: " + javaCompiler);
					jRunTime.setText("Java Run-Time " + "(" + runEnv +")");
				}
			}
			else if(command.equals("Compile Options " + "("+ cOptions + ")"))
			{
				frame = new JFrame("Input");
				
				String n = JOptionPane.showInputDialog(frame, "Please enter any valid javac compiler options:");
				if(n!=null)
				{
					cOptions = n;
				}
				compilerOptions.setText("Compile Options " + "("+ cOptions + ")");
				
			}
			else if(command.equals("Run-Time Options " + "("+ rOptions + ")"))
			{
				frame = new JFrame("Input");
				
				String n = JOptionPane.showInputDialog(frame, "Please enter any valid java run-time options:");
				if(n!=null)
				{
					rOptions = n;
				}
				runOptions.setText("Run-Time Options " + "("+ rOptions + ")");
				
			}
			else if(command.equals("Working Directory: "+ "(" + directory + ")"))
			{
				frame = new JFrame("Working Directory: ");
				
				String n = JOptionPane.showInputDialog(frame, "Please enter any valid java run-time options:");
				if(n!=null)
				{
					directory = n;
				}
				workDir.setText("Working Directory: " + "("+ directory + ")");
			}
			else if(command.equals("Help"))
			{
				JFrame help = new JFrame("Help");
				JLabel h = new JLabel("NOTE your compiled program will not run if the required ActionListeners are not present, see readme for more details");
				help.add(h);
				help.setSize(300,300);
				help.setVisible(true);
			}
			
			else if(command.equals("About"))
			{
				JFrame about = new JFrame("Help");
				JLabel h = new JLabel("Name: Gurpreet (Gary) Dhillon \n Student ID: 0802321");
				about.setSize(300,300);
				about.add(h);
				about.setVisible(true);
			}
			else if(command.equals("Compile Mode"));
			{
				JFrame comp = new JFrame("Compiler Select");
				comp.setLayout(new BorderLayout());
				final JRadioButton lexButton = new JRadioButton("lex/yacc Compiler");
				final JRadioButton ideButton = new JRadioButton("IDE Compiler");
				lexButton.setActionCommand("lex/yacc Compiler");
				lexButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						lexButton.setSelected(true);
						ideButton.setSelected(false);
						compiler = "lex/yacc Compiler";
					}
				});
				ideButton.setActionCommand("IDE Compiler");
				ideButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						ideButton.setSelected(true);
						lexButton.setSelected(false);
						compiler = "IDE Compiler";
					}
				});
				
				comp.add(lexButton, BorderLayout.NORTH);
				comp.add(ideButton, BorderLayout.SOUTH);
				
				if(compiler.equals("lex/yacc Compiler"))
				{
					lexButton.setSelected(true);
					ideButton.setSelected(false);
				}
				else if(compiler.equals("IDE Compiler"))
				{
					ideButton.setSelected(true);
					lexButton.setSelected(false);
				}
				
				comp.setSize(400,400);
				comp.setVisible(true);
			}	
			if(command.equals("Quit"))
			{
				System.exit(0);
			}
		}
	}
	
	/*Opens file system dialog and loads data from selected file to text area*/
	public void openFile()
	{
		
		JFileChooser fileOpen = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CONFIG FILES", "config");
        fileOpen.setFileFilter(filter);
        String dir = System.getProperty("user.home");
        ClassLoader classLoader = Dialogc.class.getClassLoader();
        File file = new File(classLoader.getResource("").getPath());
        fileOpen.setCurrentDirectory(file);
        int result = fileOpen.showOpenDialog(Window.this);
        if(result == JFileChooser.APPROVE_OPTION)
        {
        	FileReader reader = null;
        	try {
				reader = new FileReader(fileOpen.getSelectedFile());
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
        	fileName = fileOpen.getSelectedFile().getName();
        	
        	BufferedReader input = new BufferedReader(reader);
        	
        	String line = null;
        	try {
				line = input.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
        	String text = null;
        	text = line;
        	
        	while(line!=null)
        	{
        		try {
					line = input.readLine();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		if(line!=null)
        		{
        			text = text + "\n" + line;
        		}
        		
        	}
        	results.setText(text);
        	doc.setText("Current Project: " +fileName);
        }
	}
	
	/*Saves data in text area to .config file, takes int argument to decide whether performing 'save' or 'save as.."*/
	public void saveFile(int saveAs)
	{
		JFileChooser fileSave = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CONFIG FILES", "config");
        ClassLoader classLoader = Dialogc.class.getClassLoader();
        File dir = new File(classLoader.getResource("").getPath());
        fileSave.setCurrentDirectory(dir);
        File file;
        fileSave.setFileFilter(filter);
        if(saveAs == 0)
        {
	        int result = fileSave.showSaveDialog(Window.this);
	        if(result == JFileChooser.APPROVE_OPTION)
	        {
	            file = fileSave.getSelectedFile();
	            if(file.toString().contains(".config"))
	            {
	            }
	            else
	            {
	            	file = new File(file.toString() + ".config");
	            }
	        	FileWriter writer = null;
	        	fileSave.setSelectedFile(file);
	        	try {
					writer = new FileWriter(fileSave.getSelectedFile());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	
	        	fileName = file.getName();
	        	
	        	BufferedWriter input = new BufferedWriter(writer);
	        	
	        	String line[] = null;
				line = results.getText().split("\n");
				
				try {
					input.write("");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        	for(int i =0; i<line.length; i++)
	        	{
	        		try {
						input.write(line[i] +"\n");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	        	}
	        	try {
					input.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	doc.setText("Current Project: " +fileName);
	        }
        }
        
        else
        {
        	BufferedWriter input = null;
        	try {
				FileWriter write = new FileWriter(fileName);
				input = new BufferedWriter(write);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	String line[] = null;
			line = results.getText().split("\n");
        	
			try {
				input.write("");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
        	for(int i =0; i<line.length; i++)
        	{
        		try {
					input.append(line[i] +"\n");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        	try {
				input.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	doc.setText("Current Project: " +fileName);
        	
        	
        }
		
	}
	
	/*Document title panel listener*/
	class DocumentModify implements DocumentListener {

		@Override
		public void changedUpdate(DocumentEvent arg0) {
			doc.setText("Current Project: " + fileName + " [modified]");

		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			doc.setText("Current Project: "+ fileName + " [modified]");

		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			doc.setText("Current Project: " + fileName + " [modified]");

		}

	}
	
	/*Parses the arguments using JNI, then prints to .java file containing the class and another .java file which contains the interface.
	 * It then compiles these files using exec, if int run = 1 it will also run the compiled program*/
	public void compile(int run)
	{
		String[] fields = null;
		String[] names = null;
		String[] butTypes = new String[100];
		String[] fieldTypes = new String[100];
		JFrame err = new JFrame("err");
		int i, numFields, numButtons, m;
		numFields = 0;
		numButtons = 0;
		saveFile(1);
		JTextArea results = new JTextArea(30,30);
		JScrollPane scroll = new JScrollPane(results);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		err.setSize(600,600);
		err.add(scroll);
		
		fields = this.getFields(fileName);
		if(fields==null)
		{
			
			FileReader reader = null;
			try {
				reader = new FileReader("error.txt");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BufferedReader input = new BufferedReader(reader);
        	
        	String line = null;
        	try {
				line = input.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
        	String text = null;
        	text = line;
        	
        	while(line!=null)
        	{
        		try {
					line = input.readLine();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		if(line!=null)
        		{
        			text = text + "\n" + line;
        		}
        		
        	}
        	err.setVisible(true);
        	results.setText(text);
			
			
			PrintWriter writer = null;
			try {
				writer = new PrintWriter("error.txt");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writer.print("");
			writer.close();
			return;
		}
		for(i=1; (fields[i].equals("FFF"))==false;i++)
		{
			numFields++;
		}
		for(i=numFields+2; (fields[i].equals("BBB"))==false;i++)
		{
			numButtons++;
		}
		
		names = this.getNames(fields, numButtons + numFields,fileName);
		
		if(names==null)
		{
			FileReader reader = null;
			try {
				reader = new FileReader("error.txt");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BufferedReader input = new BufferedReader(reader);
        	
        	String line = null;
        	try {
				line = input.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
        	String text = null;
        	text = line;
        	
        	while(line!=null)
        	{
        		try {
					line = input.readLine();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		if(line!=null)
        		{
        			text = text + "\n" + line;
        		}
        		
        	}
        	err.setVisible(true);
        	results.setText(text);
			
			
			PrintWriter writer = null;
			try {
				writer = new PrintWriter("error.txt");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writer.print("");
			writer.close();
			return;
		}
		
		m=0;
		for(i=1; (fields[i].equals("FFF"))==false;i++)
		{
			fieldTypes[m] = names[i-1];
			m++;
		}
		m=0;
		for(i=numFields+2; (fields[i].equals("BBB"))==false;i++)
		{
			butTypes[m] = names[i-1];
			m++;
		}
		
		File dir = new File(directory + "/" + fields[0]);
		if(!dir.exists())
		{
			dir.mkdirs();
		}
		
		File f = new File(directory + "/" + fields[0] +"/" + fields[0] + ".java");
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.println("import javax.swing.JFrame;");
		writer.println("import javax.swing.JButton;");
		writer.println("import javax.swing.JTextField;");
		writer.println("import javax.swing.JPanel;");
		writer.println("import javax.swing.JLabel;");
		writer.println("import java.awt.BorderLayout;");
		writer.println("import java.awt.GridLayout;");
		writer.println("import javax.swing.JTextArea;");
		writer.println("import javax.swing.JScrollPane;");
		writer.println("import java.awt.Font;");
		writer.println("import javax.swing.event.DocumentEvent;");
		writer.println("import javax.swing.event.DocumentListener;");
		writer.println(" ");
		writer.println("public class " + fields[0] + " extends JFrame implements " +fields[0] + "FieldEdit{");
		writer.println(" ");
		for(int k =1; k<=numFields; k++)
		{
			writer.println("        public JLabel field" + k + " = new JLabel(" + "\"" + fields[k] + "\"" + ");");
			writer.println("        public JTextField inp" + k + " = new JTextField(15);");
		}
		writer.println("        public JTextArea results;");
		
		for(int k = numFields +2; k<=(numButtons+numFields)+1; k++)
		{
			writer.println("        public JButton button" + k + " = new JButton("+ "\"" + fields[k] + "\"" + ");");
		}
		writer.println("    public static void main(String args[]) {");
		writer.println("        " + fields[0] + " t = new " +fields[0] +"();");
		writer.println("        t.setVisible(true);");
		writer.println("    }");
		writer.println("    public static int WIDTH = 500;");
		writer.println("    public static int HEIGHT = 600;");
		writer.println("    public " + fields[0] + "(){");
		writer.println(" ");
		writer.println("        super("+"\"" +fields[0]+ "\"" + ");");
		writer.println("        setSize(WIDTH,HEIGHT);");
		writer.println("        setLayout(new BorderLayout());");
		writer.println("        JPanel buttonPanel = new JPanel();");
		writer.println("        JPanel fieldPanel = new JPanel();");
		
		writer.println(" ");
		
		writer.println("        fieldPanel.setLayout(new GridLayout(0,2));");
		
		
		for(int k =1; k<=numFields; k++)
		{
			writer.println("        fieldPanel.add(field" + k + ");");
			writer.println("        fieldPanel.add(inp" + k + ");");
		}
		
		int g =0;
		for(int k = numFields +2; k<=(numButtons+numFields)+1; k++)
		{
			writer.println("        button"+ k+ ".addActionListener(new " + butTypes[g] + "(this));");
			writer.println("        buttonPanel.add(button" + k + ");");
			g++;
		}
		
		writer.println("        add(fieldPanel, BorderLayout.NORTH);");
		writer.println("        add(buttonPanel, BorderLayout.CENTER);");
		writer.println("        results = new JTextArea(20,20);");
        writer.println("        Font font = new Font("+ "\""+ "Verdana" + "\"" + ",Font.BOLD,16);");
        writer.println("        results.setFont(font);");
        writer.println("        JScrollPane scroll = new JScrollPane(results);");
        writer.println("        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);");
        writer.println("        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);");
		writer.println("        add(scroll, BorderLayout.SOUTH);");
		writer.println("    }");
		
		for(int j=1; (fields[j].equals("FFF"))==false;j++)
		{
			writer.println("\n\n");
			writer.println("    public void setDC" + fields[j] + "(String str) {");
			writer.println("        field" + j + ".setText(str);");
			writer.println("    }");
			
			writer.println("\n\n");
			writer.println("    public String getDC" + fields[j] + "() {");
			writer.println("        String str = field" + j + ".getText();");
			writer.println("        return str;");
			writer.println("    }");
		}
		
		writer.println("\n\n");
		writer.println("    public void appendToStatusArea(String message){");
		writer.println("        results.append(message);");
		writer.println("    }");
		writer.println("}");
		

		writer.close();
		
		f = new File(directory + "/" + fields[0] +"/"+ fields[0] + "FieldEdit.java");
		
		try {
			writer = new PrintWriter(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writer.println("public interface " + fields[0] + "FieldEdit{");
		for(int j=1; (fields[j].equals("FFF"))==false;j++)
		{
			writer.println("\n\n");
			writer.println("    public void setDC" + fields[j] + "(String str);");
			
			writer.println("\n\n");
			writer.println("    public String getDC" + fields[j] + "();");
		}
		
		writer.println("\n\n");
		writer.println("    public void appendToStatusArea(String message);");
		writer.println("}");
		writer.close();
				
		/*Compile both the class .java and interface .java*/
		try{
			Runtime r = Runtime.getRuntime();
			if(cOptions!=null)
			{
				String com = javaCompiler + " " + directory + "/" + fields[0] +"/" + fields[0]  + ".java" + " " + directory + "/" + fields[0] +"/" + fields[0] + "FieldEdit.java";
				
				for(i=0;i<numButtons;i++)
				{
					com = com + " " + directory + "/" + fields[0] +"/" + butTypes[i] + ".java ";
				}
				com = com + cOptions;
				Process p = r.exec(com);
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				String line = null;
				/*Direct stream from the process to stdout and stderr*/
				while((line=input.readLine())!=null)
				{
					System.out.println(line);
				}
				BufferedReader input2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
				line = null;
				while((line=input2.readLine())!=null)
				{
					System.out.println(line);
				}
			}
			else
			{
				String com = javaCompiler + " " + directory + "/" + fields[0] +"/" + fields[0]  + ".java" + " " + directory + "/" + fields[0] +"/" + fields[0] + "FieldEdit.java";
				
				for(i=0;i<numButtons;i++)
				{
					com = com + " " + directory + "/" + fields[0] +"/" + butTypes[i] + ".java ";
				}
				Process p = r.exec(com);
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				String line = null;
				/*Direct stream from the process to stdout and stderr*/
				while((line=input.readLine())!=null)
				{
					System.out.println(line);
				}
				BufferedReader input2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
				line = null;
				while((line=input2.readLine())!=null)
				{
					System.out.println(line);
				}
			}
		}catch(Throwable t)
		{
			System.out.println("RUNTIME ERROR");
		}
		
		/*If run =1 then run the compiled program*/
		if(run == 1)
		{
			try{
				Runtime r = Runtime.getRuntime();
				if(rOptions!=null)
				{
					Process p = r.exec(runEnv + " -cp " + directory + "/" + fields[0]  +" " + fields[0] + " " + rOptions);
					BufferedReader input = new BufferedReader(new InputStreamReader(p.getErrorStream()));
					String line = null;
					/*Direct stream from the process to stdout and stderr*/
					while((line=input.readLine())!=null)
					{
						System.out.println(line);
					}
					input = new BufferedReader(new InputStreamReader(p.getInputStream()));
					line = null;
					while((line=input.readLine())!=null)
					{
						System.out.println(line);
					}
				}
				else
				{
					Process p = r.exec(runEnv + " -cp " + directory + "/" + fields[0]  +" " + fields[0]);
				}
			}catch(Throwable t)
			{
				System.out.println("RUNTIME ERROR");
			}
		}
		
		
	}
	
	public void lexCompile(int options)
	{
		int numButtons;
		String[] butTypes = new String[100];
		String cmd = "./example7";
		String title = null;
		try{
			Process p = Runtime.getRuntime().exec(cmd);
			OutputStream outStream = p.getOutputStream();
			PrintWriter pWriter = new PrintWriter(outStream);
			FileReader reader = null;
        	try {
				reader = new FileReader(fileName);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			BufferedReader input = new BufferedReader(reader);
        	
        	String line = null;
        	do
        	{
				try {
					line = input.readLine();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				pWriter.println(line);
			}while(line!=null);
			
			pWriter.flush();
			pWriter.close();
			
		}catch(Throwable t)
		{
			System.out.println("RUNTIME ERROR");
		}
		
		
		String titleName = null;
		FileReader reader = null;
        try {
			reader = new FileReader("title");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader input3 = new BufferedReader(reader);
		
		try {
			titleName = input3.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String str = null;
		int i=0;
		do
		{
			try {
				str = input3.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			butTypes[i] = str;
			i++;
		}while(str!=null);
		
		numButtons = i;
		
		try{
			Runtime r = Runtime.getRuntime();
			if(cOptions!=null)
			{
				String com = javaCompiler + " " + directory + "/" + titleName +"/" + titleName  + ".java" + " " + directory + "/" + titleName +"/" + titleName + "FieldEdit.java";
				for(i=0;i<numButtons-1;i++)
				{
					com = com + " " + directory + "/" + titleName +"/" + butTypes[i] + ".java ";
				}
				
				com = com + cOptions;
				Process p = r.exec(com);
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				String line = null;
				/*Direct stream from the process to stdout and stderr*/
				while((line=input.readLine())!=null)
				{
					System.out.println(line);
				}
				BufferedReader input2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
				line = null;
				while((line=input2.readLine())!=null)
				{
					System.out.println(line);
				}
			}
			else
			{
				String com = javaCompiler + " " + directory + "/" + titleName +"/" + titleName  + ".java" + " " + directory + "/" + titleName +"/" + titleName + "FieldEdit.java";
				for(i=0;i<numButtons-1;i++)
				{
					com = com + " " + directory + "/" + titleName +"/" + butTypes[i] + ".java ";
				}
				System.out.println(com);
				Process p = r.exec(com);
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				String line = null;
				/*Direct stream from the process to stdout and stderr*/
				while((line=input.readLine())!=null)
				{
					System.out.println(line);
				}
				BufferedReader input2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
				line = null;
				while((line=input2.readLine())!=null)
				{
					System.out.println(line);
				}
			}
		}catch(Throwable t)
		{
			System.out.println("RUNTIME ERROR");
		}
		
		if(options == 1)
		{
			try{
				Runtime r = Runtime.getRuntime();
				if(rOptions!=null)
				{
					Process p = r.exec(runEnv + " -cp " + directory + "/" + titleName  +" " + titleName + " " + rOptions);
					BufferedReader input = new BufferedReader(new InputStreamReader(p.getErrorStream()));
					String line = null;
					/*Direct stream from the process to stdout and stderr*/
					while((line=input.readLine())!=null)
					{
						System.out.println(line);
					}
					input = new BufferedReader(new InputStreamReader(p.getInputStream()));
					line = null;
					while((line=input.readLine())!=null)
					{
						System.out.println(line);
					}
				}
				else
				{
					Process p = r.exec(runEnv + " -cp " + directory + "/" + titleName  +" " + titleName);
				}
			}catch(Throwable t)
			{
				System.out.println("RUNTIME ERROR");
			}
		}
		
			
	}
		

}
