
// Adding packages
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class WeatherAPI {
	// converting json data into Map format
	public static Map<String, Object> jsonToMap(String str) {
		Map<String, Object> map = new Gson().fromJson(str, new TypeToken<HashMap<String, Object>>() {
		}.getType());
		return map;
	}

	// Main funtion starts here
	public static void main(String[] args) throws IOException {
		System.out.println("Welecome to weather finder");
		String API_KEY = "ccb1ca0fcfe40eb661bfe21e7282ab2d14"; // declaration of Weather API key
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the latitude and longitude of location");
		// taking latitute and longitude as input
		String latitude = br.readLine();
		String longitude = br.readLine();
		// Showcasing the menu option for input from user
		System.out.println("Please provide an input based on the following options");
		System.out.println("1.Weather Information for current time");
		System.out.println("2. Weather Information for 5 days");
		System.out.println("3. Air Pollution Data");
		System.out.println("4. Exit");
		int choice = (Integer.parseInt(br.readLine()));
		// Loop for accepting and processing inputs from the user
		do {
			switch (choice) {
				case 1: {
					StringBuilder Current_Weather_data = new StringBuilder();
					try { // communicating with current weather API
						URL Weather_URL = new URL("https://api.openweathermap.org/data/2.5/weather?latitude=" + latitude
								+ "&longitude=" + longitude + "&units=metric" + "&appid=" + API_KEY);
						HttpsURLConnection connection = (HttpsURLConnection) Weather_URL.openConnection();
						connection.setRequestMethod("GET");
						BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						String checkline;
						while ((checkline = rd.readLine()) != null) {
							Current_Weather_data.append(checkline);
						}
						rd.close();
						System.out.println(
								"CURRENT WEATHER FOR LOCATION " + latitude + "LATITUDE " + longitude + "LONGITUDE");
						Map<String, Object> respMap = jsonToMap(Current_Weather_data.toString());
						Map<String, Object> mainMap = jsonToMap(respMap.get("main").toString());
						Map<String, Object> windMap = jsonToMap(respMap.get("wind").toString());
						System.out.println("Temperature_FEEL: " + mainMap.get("temp") + " Celsius");
						System.out.println("Temperature_MINIMUM: " + mainMap.get("temp_min") + " Celsius");
						System.out.println("Temperature_MAXIMUM: " + mainMap.get("temp_max") + " Celsius");
						System.out.println("Humidity: " + mainMap.get("humidity") + "%");
						System.out.println("Pressure: " + mainMap.get("pressure") + "hpa");
						System.out.println("Current windspeed: " + windMap.get("speed") + "meter/sec");
						JSONObject newObj = new JSONObject(Current_Weather_data.toString());
						JSONArray items = newObj.getJSONArray("weather");
						for (int i = 0; i < items.length(); i++) {
							JSONObject contactItem = items.getJSONObject(i);
							System.out.println("Current Weather is: " + contactItem.getString("main"));
						}

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				case 2: {
					StringBuilder result = new StringBuilder();
					try {
						URL url = new URL("https://api.openweathermap.org/data/2.5/forecast?latitude=" + latitude
								+ "&longitude=" + longitude + "&cnt=5" + "&units=metric" + "&appid=" + API_KEY);
						HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
						connection.setRequestMethod("GET");
						// URLConnection connection=url.openConnection();
						BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						String checkline;
						while ((checkline = rd.readLine()) != null) {
							result.append(checkline);
						}
						rd.close();
						Map<String, Object> respMap = jsonToMap(result.toString());
						String daywise = respMap.get("list").toString();
						System.out.println("Five days forecast is: " + daywise);
						JSONObject newObj = new JSONObject(result.toString());
						JSONArray items = newObj.getJSONArray("list");
						for (int it = 0; it < items.length(); it++) {
							JSONObject contactItem = items.getJSONObject(it);
							System.out.println("DATE is: " + contactItem.getString("dt_txt"));
							System.out.println(
									"Temperature_FEEL: " + contactItem.getJSONObject("main").getBigDecimal("temp")
											+ " Celsius");
							System.out.println("Temperature_MINIMUM: "
									+ contactItem.getJSONObject("main").getBigDecimal("temp_min") + " Celsius");
							System.out.println("Temperature_MAXIMUM: "
									+ contactItem.getJSONObject("main").getBigDecimal("temp_max") + " Celsius");
							System.out
									.println("Humidity: " + contactItem.getJSONObject("main").getInt("humidity") + "%");
							System.out.println(
									"Pressure: " + contactItem.getJSONObject("main").getInt("pressure") + "hpa");
							System.out.println(
									"Current windspeed: " + contactItem.getJSONObject("wind").getBigDecimal("speed")
											+ "meter/sec");
							JSONArray itemsweather = contactItem.getJSONArray("weather");
							for (int itw = 0; itw < itemsweather.length(); itw++) {
								JSONObject contactItemWeather = itemsweather.getJSONObject(itw);
								System.out.println("Weather is: " + contactItemWeather.getString("main"));
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case 3: {
					StringBuilder result_Current_Pollution = new StringBuilder();
					try {
						URL Current_Pollution_Url = new URL(
								"http://api.openweathermap.org/data/2.5/air_pollution?latitude="
										+ latitude + "&longitude="
										+ longitude + "&appid=" + API_KEY);
						HttpURLConnection connection = (HttpURLConnection) Current_Pollution_Url.openConnection();
						connection.setRequestMethod("GET");
						BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						String checkline;
						while ((checkline = rd.readLine()) != null) {
							result_Current_Pollution.append(checkline);
						}
						rd.close();
						System.out.println(
								"QUALITY OF AIR FOR LOCATION " + latitude + "LATITUDE " + longitude + "LONGITUDE");
						JSONObject newObj = new JSONObject(result_Current_Pollution.toString());
						JSONArray items = newObj.getJSONArray("list");
						for (int it = 0; it < items.length(); it++) {
							JSONObject contactItem = items.getJSONObject(it);
							System.out.println("Current Air Quality " + contactItem.getJSONObject("main"));
							System.out.println(
									"Current concentration of gases " + contactItem.getJSONObject("components"));
							System.out.println(
									"Possible values for Air Quality index are: 1, 2, 3, 4, 5. Where 1 = Good, 2 = Fair, 3 = Moderate, 4 = Poor, 5 = Very Poor.");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case 4:
					System.out.println("Thanks for using Weather API");
					return;
				default: {
					System.out.print("Invalid Choice! Please try again.");
					break;
				}

			}
		} while (choice != 4);
	}
}
