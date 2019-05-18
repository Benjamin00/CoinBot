import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.jibble.pircbot.*;
import java.util.HashMap;
import java.util.Map;


public class MyBot extends PircBot{
	public String [] validCurrency;
	public ArrayList<String> list = new ArrayList<String>();
	
	public static String coin;
	public static String curr;
	
	public static void main(String[] args) {
	}
	public MyBot() {
		this.setName("CoinBot23");
		Gson gson = new Gson();
	}
	//This is the function that responds to prompts from the user
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		String url1 = "https://min-api.cryptocompare.com/data/price?fsym=";
		String url2 = "&tsyms=";
		//Create Lists to hold the acronym and the counterpart
		String [] validCoins = {"BTC","ETH","XRP","BCH","EOS","LTC","XLM","ADA","USDT",
								"MIOTA","TRX","NEO","XMR","DASH","ETC","XEM","BNB","XTZ","VEN","OMG",
								"ZEC","QTUM","BCN","LSK","ONT","ICX","ZIL","ZRX","BTG","DCR"};
		String [] validCoinsNames = {"Bitcoin","Ethereum","XRP","Bitcoin Cash","EOS","Litecoin","Stellar","Cardano","Tether",
									"IOTA","TRON","NEO","Monero","DASH","Ethereum Classic","XEM","Binance Coin","Tezos","VeChain","OmiseGO",
									"Zcash","Qtum","Bytecoin","Lisk","Onotology","Icon","Zilliqa","0x","Bitcoin Gold","Decred"};
		
		Map<String,String> coinDict = new HashMap<String,String>();
		for(int i=0; i<validCoins.length; i++) {
			coinDict.put(validCoins[i],validCoinsNames[i]);
		}
		//Reverse dict used to match the value to the key
		Map<String,String> rcoinDict = new HashMap<String,String>();
		for(int i=0; i<validCoins.length; i++) {
			rcoinDict.put(validCoinsNames[i],validCoins[i]);
		}
		//Split the message into parts
		String[] messageArray = message.split(" ");
	    //The command should be the very first 
		String command = messageArray[0];
		command = command.toUpperCase();
	    switch(command) {
			default: break;
			
			case "PRICE":
				
				coin = messageArray[1];
				curr = messageArray[2];
				curr = curr.toUpperCase();
				
				String coin2 = rcoinDict.get(coin);	//Coin2 turns "Bitcoin" to "BTC"
				//sendMessage(channel, sender+":Reverse Coin Input " + coin2 + " Actual input " + coin);
				
			if(coin.length() ==3) {
				//If an acronym
				coin = coin.toUpperCase();
				String jResp = getJSON(url1,url2,coin,curr);
				String price = parseJSON(jResp,curr);
					if(coinDict.containsKey(coin)) {
						sendMessage(channel, sender + " : The price of " + coinDict.get(coin) +" (" + coin + ") is " + price +" "+curr);
					}
					else {
						sendMessage(channel, sender + " : The price of " + coin + " is " + price +" "+curr);
					}
				}
			else if(rcoinDict.containsKey(coin)){
				//If a name in the map
				String jResp = getJSON(url1,url2,coin2,curr);
				String price = parseJSON(jResp,curr);	
				sendMessage(channel, sender+" : The price of "+coin+ " is " + price +" "+curr);
				}
			else {
				sendMessage(channel, sender+": Invalid coin name");
			}
			return;
		}
	    
	    if (message.equalsIgnoreCase("time")) {
			String time = new java.util.Date().toString();
			sendMessage(channel, sender + ": The time is now " + time);
		}
		
	}
	public static String getJSON(String url1,String url2, String[] coins, String[] currencies) {
		//System.out.print("Inside get PJSON");
		String returnedStr = "";
		String coinSTR = parseArr(coins);
		String currSTR = parseArr(currencies);
		String finalURL = url1 + coinSTR + url2 + currSTR;
		try {
			URL url = new URL(finalURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			if (con.getResponseCode() != 200) {//200 Means a bad response from the HTTP
				throw new RuntimeException("Failed : HTTP error code : " + con.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
			
			String output;
			System.out.println("Output ...");
			while((output = br.readLine())!=null) {
				System.out.println(output);
				returnedStr += output;
			}
		}
		catch (MalformedURLException e){
			e.printStackTrace();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		return returnedStr;
	}
	public static String getJSON(String url1,String url2, String coinSTR, String currSTR) {
		//System.out.print("Inside get PJSON");
		String returnedStr = "";

		String finalURL = url1 + coinSTR + url2 + currSTR;
		try {
			URL url = new URL(finalURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			if (con.getResponseCode() != 200) {//200 Means a bad response from the HTTP
				throw new RuntimeException("Failed : HTTP error code : " + con.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
			
			String output;
			System.out.println("Output ...");
			while((output = br.readLine())!=null) {
				System.out.println(output);
				returnedStr += output;
			}
		}
		catch (MalformedURLException e){
			e.printStackTrace();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		return returnedStr;
	}
	public static String parseJSON(String jsonString, String get) {
		JsonObject jobj = new Gson().fromJson(jsonString, JsonObject.class);
		String result = jobj.get(get).getAsString();
		return result;
	}
	public static String parseArr(String[] arr) {//used to pass arrays to the url
		String ret = "";
		if(arr.length ==1) {//if the only element
			ret = arr[0];
		}
		else {
		for (int i = 0; i<arr.length;i++) {
			if (i!=arr.length-1) {//if not the last element...
			ret.concat(arr[i]);
			ret.concat(",");
			}
			else {
			ret.concat(arr[i]);//if the last element
			}
		}
		}
		return ret;
	}

}