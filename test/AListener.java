import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AListener implements ActionListener
{
	
	private testFieldEdit dialogInterface;
	
	public AListener(testFieldEdit theInterface)
	{
		
		dialogInterface = theInterface;
	}

	@Override

	public void actionPerformed(ActionEvent event)
	{
		dialogInterface.appendToStatusArea("Button2 pressed!\n");
	}

}
