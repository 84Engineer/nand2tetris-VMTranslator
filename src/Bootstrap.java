import java.util.ArrayList;
import java.util.List;

/**
 * @author olysenko
 */
public class Bootstrap {

   private List<String> bootStrapCode = new ArrayList<>();

   {
      bootStrapCode.add("ram 0 256");
      bootStrapCode.add("call Sys.init 0");
   }

   public List<String> getBootStrapCode() {
      return bootStrapCode;
   }
}
