package test.main;

import com.util.bean.test.Customer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
//Một số lưu ý về Serialization trong Java
//Nếu class cha implement Serializable thì các class con không cần thực hiện implement Serializablenữa
//Các thuộc tính static và transient sẽ không được serialization
//Hàm khởi tạo (constructor) sẽ không được gọi khi một đối tượng được deserialization

//Khi thực hiện serialization một đối tượng thì tất cả các thuộc tính bên trong nó đều phải là serializable
// (áp dụng với các thuộc tính có kiểu đối tượng, ví dụ object Person có thuộc tính Address
// thì thuộc tính Address đó cũng phải implement Serializable nếu không sẽ bị lỗi java.io.NotSerializableException
// khi thực hiện serialization đối tượng person).
public class DeserializationTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = null;
        Customer customer = null;

        ois = new ObjectInputStream(new FileInputStream(new File("src/test/main/customer.dat")));
        customer = (Customer) ois.readObject();
        ois.close();
        System.out.println("Customer after deserialization:");
        System.out.println(customer); //Customer [id=1, name=kai, address=null] because address is transient

    }
}
