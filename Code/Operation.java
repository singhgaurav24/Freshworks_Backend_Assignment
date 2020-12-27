import org.json.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.time.LocalTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


@SuppressWarnings({ "serial", "unused" })
class DuplicateKey extends Exception {

}
@SuppressWarnings("serial")
class InvalidKey extends Exception {

}
@SuppressWarnings("serial")
class TimeExceeded extends Exception {

}
@SuppressWarnings("serial")
class KeySizeExceeded extends Exception{
	
}
@SuppressWarnings("serial")
class ValueSizeExceeded extends Exception{
	
}


public class Operation {
	
    private final String FileLocation; // Making Immutable FileLocation 
   
    Operation(String path) throws JSONException {
    	FileLocation = path;
        JSONObject fill = new JSONObject();
        fill.put(" ", " "); 
         try (FileWriter file = new FileWriter(FileLocation,false))
        		{
        	      file.write(fill.toString());
        	      file.close();
        		}
         catch (IOException E)
         {
        	System.out.println("Caught IOException"); 
         }
        
        
    }
    /*
       Default constructor of class Operation
    */
    Operation() throws JSONException {
    	FileLocation = "F://Operation.JSON";
        JSONObject fill = new JSONObject();
        fill.put(" ", " ");      
         try (FileWriter file = new FileWriter(FileLocation,false))
        		{
        	      file.write(fill.toString());
        	      file.close();
        		}
         catch (IOException E)
         {
        	System.out.println("Caught IOException"); 
         }
        
    }
     /*
          Creates Entry to JSON file , accepting TimeToLive parameter
     */
    public void Create(String Key, JSONObject Value, int TimeToLive) throws Exception // Create method when TimeToLive is provided
    {    
         try {
        	 if (Key.length()>32) // Check if Key is more than 32 Char
        	 throw new KeySizeExceeded();
         /* else if((instrument.getObjectSize((Object)Value)/1024)>16) // Check if JSONObject is more than 16 KB
        	 throw new ValueSizeExceeded();
         */
         
         }
         catch (KeySizeExceeded e) {
          	System.out.println(" Key size exceeds maximum size, Enter Valid Key");
          }/* catch (ValueSizeExceeded e) {
          	System.out.println(" Value size exceeds maximum size ");
          } */
        
         try (FileReader reader = new FileReader(FileLocation)) {
            
        	 JSONTokener tokener = new JSONTokener(reader);
             JSONObject temp = new JSONObject(tokener);
            if (temp.has(Key)) //Check if JSONObject has the given key value pair
                throw new DuplicateKey();
            JSONArray tempArray = new JSONArray();
            tempArray.put(Value);        // Adding  JSONOBject provided by user as first element
            tempArray.put(TimeToLive);  // Adding TimeToLive value provided by user as second element

            LocalTime time = LocalTime.now();
            int TimeStamp = time.toSecondOfDay();
            tempArray.put(TimeStamp);  // Adding time of creation of Key as third element
            temp.put(Key, tempArray);
            try (FileWriter file = new FileWriter(FileLocation,false)) // Clearing the file , by setting append to false
            {

                file.write(temp.toString());
                file.close();

            } catch (IOException e) {
                System.out.println("Caught IO Exception");
            }

        } catch (DuplicateKey e) {
            System.out.println("KEY already exists, Duplicate keys not allowed");
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        } catch (IOException e) {
            System.out.println("Caught IO Exception");
        } 
    }
    /*
        Creates Entry to JSON file , when TimeToLive is not given
    */
    public void CreateEntry(String Key, JSONObject Value) throws Exception // Create method , if no TimeToLive is specified by user
    {
    	try {if (Key.length()>32) // Check if Key is more than 32 Char
       	 throw new KeySizeExceeded();
    	/* else if((instrument.getObjectSize((Object)Value)/1024)>16) // Check if JSONObject is more than 16 KB
       	 throw new ValueSizeExceeded(); */
        }
        catch (KeySizeExceeded e) {
         	System.out.println(" Key size exceeds maximum size, Enter Valid Key");
         }  /* catch (ValueSizeExceeded e) {
         	System.out.println(" Value size exceeds maximum size " );
         }*/
        try (FileReader reader = new FileReader(FileLocation)) {
            //Read JSON file
            
        	JSONTokener tokener = new JSONTokener(reader);
            JSONObject temp = new JSONObject(tokener);
            if (temp.has(Key)) //Check if JSONObject has the given key value pair
                throw new DuplicateKey();
            JSONArray tempArray = new JSONArray();
            tempArray.put(Value); // Adds JSONObject to array
            tempArray.put(Integer.MAX_VALUE); // Adding arbitrary Int Max value as TimeToLive 

            LocalTime time = LocalTime.now();
            int TimeStamp = time.toSecondOfDay();
            tempArray.put(TimeStamp); //Adding time stamp to array
            temp.put(Key, tempArray);
            try (FileWriter file = new FileWriter(FileLocation,false)) //Writes updated JSON based store back to file
            {
                file.write(temp.toString());
                file.close();
            } catch (IOException e) {
                System.out.println("Caught IO Exception");
            }

        } catch (DuplicateKey e) {
            System.out.println("KEY already exists. Duplicate keys not allowed");
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        } catch (IOException e) {
            System.out.println("Caught IO Exception");
        }
    }
    /**
     * Returns JSONObject for Key, requested by  user
     * @param String 
     * @return Returns JSONObject if exists for Key , else returns null in case of exceptions
     * @throws user defined InvalidKey exception ( No key matches the user given Key) , TimeExceeded exception (If the TimeToLive of Key is exceeded)
     */
    public JSONObject Read(String Key) throws Exception //Read method, which returns JSONObject
    {
        
        try (FileReader reader = new FileReader(FileLocation)) {
            //Read JSON file
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject temp = new JSONObject(tokener);
            if (temp.has(Key)) //Check if JSONObject has the given key value pair
            {
                JSONArray tempArray = new JSONArray();
                tempArray = temp.getJSONArray(Key);
                LocalTime time = LocalTime.now();
                int CurrentTime = time.toSecondOfDay();
                if ((CurrentTime - tempArray.getInt(2)) < tempArray.getInt(1)) //Checking time to live condition
                    return tempArray.getJSONObject(0);
                else
                    throw new TimeExceeded();

            } else
                throw new InvalidKey();

        } catch (TimeExceeded e) {
            System.out.println("Key Exceeded Time To Live");
        } catch (InvalidKey e) {
            System.out.println("Invalid Key, Enter a valid key to continue");
        } catch (FileNotFoundException e) {
            System.out.println("File Does Not Found");
        } catch (IOException e) {
            System.out.println("Caught IO Exception");
        }
		return null;
    }
     /*
         Delete JSONObject for Key, requested by  user
     */
    public void Delete(String Key) throws Exception // Delete method , for deleting a given < Key,JSONOBject > pair
    {
       
        try (FileReader reader = new FileReader(FileLocation)) //Read JSON file
        {
        	JSONTokener tokener = new JSONTokener(reader);
            JSONObject temp = new JSONObject(tokener);
            if (temp.has(Key)) //Check if JSONObject has the given key value pair
            {
                JSONArray tempArray = new JSONArray();
                tempArray = temp.getJSONArray(Key);
                LocalTime time = LocalTime.now();
                int CurrentTime = time.toSecondOfDay();
                if ((CurrentTime - tempArray.getInt(2)) < tempArray.getInt(1)) //Checking time to live condition , if satisfied removes < Key,Value > pair
                    temp.remove(Key);
                
                  
                else
                    throw new TimeExceeded();

                try (FileWriter file = new FileWriter(FileLocation,false)) 
                {

                    file.write(temp.toString());
                    file.close();
                }
            }
                else
                    throw new InvalidKey();

            } 
            catch (InvalidKey e) {
                System.out.println("Invalid Key, Enter a valid key to continue");
            } catch (IOException e) {
                System.out.println("Caught IO Exception");
            }

         catch (TimeExceeded e) {
            System.out.println("Key Exceeded Time To Live");
        }
    }
    
   
}