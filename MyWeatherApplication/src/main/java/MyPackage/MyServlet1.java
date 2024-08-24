 package MyPackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Scanner;
import java.util.Date;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MyServlet1
 */
public class MyServlet1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet1() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String inputData = request.getParameter("city");
		
		String apiKey ="56b2f979f8ca71e415aa62889f35aeed";
		String city = request.getParameter("city");
		String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid="+apiKey+"";
		
		URL url = new URL (apiUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		//reading the data from the network
		InputStream inputStream = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(inputStream);
		
		//Data store in the string
		StringBuilder responseContent = new StringBuilder();
		
		//input lene ke liye from the reader , will create scanner object 
		Scanner scanner = new Scanner(reader);
		
		while (scanner.hasNext()) {
			responseContent.append(scanner.nextLine());
			} 
		scanner.close();
		//parse the  JSON responses to extract temperature, date and humidity etc...
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(responseContent.toString(),JsonObject.class);
		
		//Date & time 
		long dateTimeStamp = jsonObject.get("dt").getAsLong()*1000;
		String date = new Date(dateTimeStamp).toString();
		
		//Temperature 
		double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int temperatureCelsius =(int)(temperatureKelvin -273.15);
		
		//Humidity
		int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		
		//wind Speed 
		double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		
		//Weather Conditions
		String weathercondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		
		
		//set the data attribute as request attribute (for sending to the jsp page as response )
		
		request.setAttribute("date",date);
		request.setAttribute("city",city);
		request.setAttribute("temperature",temperatureCelsius);
		request.setAttribute("weatherCondition",weathercondition);
		request.setAttribute("humidity",humidity);
		request.setAttribute("windSpeed",windSpeed);

        request.setAttribute("weatherData", responseContent.toString());
		
		connection.disconnect();
		
		// Forward the request to the weather.jsp page for rendering 
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
		
	}

}
