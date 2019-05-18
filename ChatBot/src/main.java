import org.jibble.pircbot.*;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class main {
	
	public static void main(String[] args) throws Exception{
		MyBot bot = new MyBot();
		
		bot.setVerbose(true);
		
		bot.connect("irc.freenode.net");
		bot.joinChannel("#pircbot");
				
	}

	
}
