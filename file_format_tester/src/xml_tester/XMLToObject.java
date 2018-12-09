package xml_tester;

import java.io.File;  
import javax.xml.bind.JAXBContext;  
import javax.xml.bind.JAXBException;  
import javax.xml.bind.Unmarshaller;  
  
public class XMLToObject {  
public static void main(String[] args) {  
     try {    
            File file = new File("employee.xml");    
            JAXBContext jaxbContext = JAXBContext.newInstance(Employee.class);    
         
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();    
            Employee e=(Employee) jaxbUnmarshaller.unmarshal(file);    
            System.out.println(e.getId()+" "+e.getName()+" "+e.getSalary());  
              
          } catch (JAXBException e) {e.printStackTrace(); }    
         
}  
} 