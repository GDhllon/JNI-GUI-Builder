import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddListener implements ActionListener
{
	
	private testFieldEdit dialogInterface;
	
	public AddListener(testFieldEdit theInterface)
	{
		
		dialogInterface = theInterface;
	}

	@Override

	public void actionPerformed(ActionEvent event)
	{
		dialogInterface.appendToStatusArea("Testing button pressed!\n");
	}

}
