package priv.xiean.DuerOS_dock_demo.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.assertj.core.util.Arrays;

public class tester {
 
	public static void main(String[] args) throws net.minidev.json.parser.ParseException {
		Scanner scanner = new Scanner(System.in);
		List<ArrayList<Object>> array = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> subArray = new ArrayList<>();
		String str;
		while (scanner.hasNextLine() && !(str = scanner.nextLine()).isEmpty()) {
			subArray = (ArrayList<Object>) Arrays.asList(str.split(" "));
			array.add(subArray);
		}
		scanner.close();
		array.stream().forEach(sub -> System.out.println(sub.toString()));
	}

}
