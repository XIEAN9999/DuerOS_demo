package priv.xiean.DuerOS_dock_demo.bot;

import com.eclipsesource.json.ParseException;

import net.sf.json.JSONObject;

public class tester {

	public static void main(String[] args) throws net.minidev.json.parser.ParseException {
		 String str = "{" + "\"" + "latitude" + "\"" + ":" + 30.23 + "," + "\"" + "longitude"
	                + "\"" + ":" + 114.57 + "}";
	        System.out.println(str + "\n" + str.getClass());
	        try {
	            JSONObject jsonObj =JSONObject.fromObject(str);
	            System.out.println(jsonObj.toString() + "\n" + jsonObj.getClass());
	            /*float longitude = (float)jsonObj.get("longitude");
	            System.out.println(longitude);*/
	            double latitude = (double)jsonObj.get("latitude");
	            System.out.println(latitude);
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	}

}
