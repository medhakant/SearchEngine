import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class checker
{
	public static void main ( String args []) throws Exception
	{
		BufferedReader br = null;
		SearchEngine google = new SearchEngine();

		try {
			String actionString;
			br = new BufferedReader(new FileReader("D:\\Documents\\Java Projects\\Search Engine\\src\\actions.txt"));

			while ((actionString = br.readLine()) != null) {
				google.performAction(actionString);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
}
