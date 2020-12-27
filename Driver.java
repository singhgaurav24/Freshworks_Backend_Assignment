import org.json.JSONObject;
import java.util.Scanner;

public class Driver {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("              **************Freshworks – Backend Assignment****************");
		Operation op = new Operation("F:\\jsonoutput/data.json");
		JSONObject jsonObject = new JSONObject();
		
		Scanner input = new Scanner(System.in);
		String key ="";
		String jsonKey="";
		String jsonValue="";
		
		boolean status = true;
		while(status)
		{
			
			System.out.println("Select the Operation\n"+
			                         "1 - for Create opertion \n"+ 
				                	 "2 - for Create operation with TTL\n"+
			                         "3 - for Read operation\n"+
				                	 "4 - for Delete operation\n"+
				                	 "5 - for exit");
			
			int n = input.nextInt();
			
			
			switch(n) {
			case 1:
				System.out.println("************Perform Create Operation***********");
				System.out.println("Enter Key(a string - capped at 32chars)");
				key = input.next();
				System.out.println("Enter JSON_Key");
				jsonKey = input.next();
				System.out.println("Enter JSON_Value");
				jsonValue = input.next();
				jsonObject.put(jsonKey, jsonValue);
				op.CreateEntry(key, jsonObject);
				System.out.println("Key - Value, Successfully Added!!!");
				continue;
			case 2:
				int ttl = 0;
				System.out.println("************Perform Create Operation***********");
				System.out.println("Enter Key(a string - capped at 32chars)");
				key = input.next();
				System.out.println("Enter Time-TO-Live(in seconds)");
				ttl = input.nextInt();
				System.out.println("Enter jsonKey");
				jsonKey = input.next();
				System.out.println("Enter jsonValue");
				jsonValue = input.next();
				jsonObject.put(jsonKey, jsonValue);
				op.Create(key, jsonObject,ttl);
				System.out.println("Key - value, Successfully Added!!!");
				continue;
			case 3:
				System.out.println("**********Perform Read Operation**************");
				System.out.println("Enter Key To Perform Read Operation");
				key = input.next();
				jsonObject = op.Read(key);
				System.out.println(jsonObject);
				continue;
			case 4:
				System.out.println("*********Perform Delete Operation**********");
				System.out.println("Enter Key To Perform Delete Operation");
				key = input.next();
			    op.Delete(key);
				System.out.println("Successfully Deleted!!");
				continue;
			case 5:
				System.out.println("******Exit from System*********");
				status = false;
				break;
			}
		}
	     
		
		
		System.out.println("Program Ended");
		
		
		

	}

}
