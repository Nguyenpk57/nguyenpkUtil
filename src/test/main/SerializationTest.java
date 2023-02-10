package test.main;


import com.util.bean.test.Customer;

import java.io.*;
//Serialization trong Java là cơ chế chuyển đổi trạng thái của một đối tượng (giá trị các thuộc tính trong object)
// thành một chuỗi byte sao cho chuỗi byte này có thể chuyển đổi ngược lại thành một đối tượng.

public class SerializationTest  {
    public static void main(String[] args) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("src/test/main/customer.dat")));
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("kai");
        customer.setAddress("ha noi"); // transient String address; -> tức là nó sẽ không được serialization.
        System.out.println("Customer before serialization:");
        System.out.println(customer); //Customer [id=1, name=kai, address=ha noi]
        oos.writeObject(customer);
        oos.close();
    }
}
