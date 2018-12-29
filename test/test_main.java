import com.mmall.common.Const;
import com.mmall.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class test_main {
    public static void main(String[] args) {
        String str = "hello.nihao.word.cc.jpg";
        String fileExtensionName = StringUtils.substringAfterLast(str, ".");
        String or = "hello_asc";
        System.out.println(fileExtensionName);
        System.out.println("====" + Const.ProductListOrderBy.PRICE_ASC_DESC.contains("price_desc"));
        // String [] orderArray=Const.ProductListOrderBy.PRICE_ASC_DESC.
        String[] ord = or.split("_");
        System.out.println(ord[0] + "#########" + ord[1]);
        String keyword = "  d";
        System.out.println("判断"+(StringUtils.isEmpty(keyword) ? null : keyword));
        System.out.println("-------------------------------------");
        IUser target=new UserImpl();
        IUser userProxy=new UserProxy(target);
        userProxy.say();
    }
    @Test
    public void dynamic(){
        SqlSessionFactory sqlSessionFactory=null;
        String resource="applicationContext-datasource.xml";
        InputStream inputStream;
        try {
            inputStream= Resources.getResourceAsStream(resource);
            sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSession sqlSession=sqlSessionFactory.openSession();
    }
    @Test
    public void whenCreateEmptyOptional_thenNull(){
        Optional<OptionalTest> optionalTest=Optional.empty();
        optionalTest.get();
    }


}
