package netology.Id;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileNameCSV = ("C:" + File.separator + "Users" + File.separator + "i.grachev" +
                File.separator + "IdeaProjects" + File.separator + "CSV-JSONparse" + File.separator + "data.csv");
        List<Employee> listFromCsv = parseCSV(columnMapping, fileNameCSV);
        String json1 = listToJson(listFromCsv);
        writeString(json1, "data.json");
        String fileNameXML = ("C:" + File.separator + "Users" + File.separator + "i.grachev" +
                File.separator + "IdeaProjects" + File.separator + "CSV-JSONparse" + File.separator + "data.xml");
        List<Employee> listFromXml = parseXML(fileNameXML);
        String json2 = listToJson(listFromXml);
        writeString(json2, "data2.json");


    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> staff = null;
        try (CSVReader scvReader = new CSVReader(new FileReader(fileName))) {

            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(scvReader)
                    .withMappingStrategy(strategy)
                    .build();

            staff = csv.parse();
            staff.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return staff;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        return json;

    }

    public static void writeString(String json, String name) {
        try (FileWriter file = new FileWriter("C:" + File.separator + "Users" + File.separator + "i.grachev" +
                File.separator + "IdeaProjects" + File.separator + "CSV-JSONparse" + File.separator + name)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> parseXML(String fileName) {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));

            List<Employee> employees = new ArrayList<>();

            Node root = doc.getDocumentElement();

            NodeList nodeList = root.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    NodeList nodelist1 = node.getChildNodes();
                    long id = 0;
                    String fistName = "";
                    String lastName = "";
                    String country = "";
                    int age = 0;

                    for (int j = 0; j < nodelist1.getLength(); j++) {
                        Node node1 = nodelist1.item(j);
                        if (Node.ELEMENT_NODE == node1.getNodeType()) {
                            Element employee = (Element) node1;
                            switch (employee.getNodeName()) {
                                case "id":
                                    id = Long.parseLong(employee.getTextContent());
                                    break;
                                case "firstName":
                                    fistName = employee.getTextContent();
                                    break;
                                case "lastName":
                                    lastName = employee.getTextContent();
                                    break;
                                case "country":
                                    country = employee.getTextContent();
                                    break;
                                case "age":
                                    age = Integer.parseInt(employee.getTextContent());
                                    break;
                            }
                        }
                    }


                    Employee emp = new Employee(id, fistName, lastName, country, age);
                    employees.add(emp);


                }
            }
            return employees;
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return null


    }
}