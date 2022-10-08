import com.example.demo.ioc.BeanFactory;
import com.example.demo.ioc.ClassPathXmlApplicationContext;
import com.example.demo.pojo.Student;
import org.junit.Test;

/**
 * IocTest
 *
 * @author Thou
 * @date 2022/9/22
 */
public class IocTest {

    @Test
    public void testIoc() {
        BeanFactory ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        Student student1 = ac.getBean("student", Student.class);
        System.out.println("student1 = " + student1);
        Student student2 = ac.getBean("student", Student.class);
        System.out.println("student2 = " + student2);
        // 测试多例
        System.out.println(student1 == student2);
    }
}
