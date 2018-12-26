import org.apache.commons.lang3.StringUtils;

public class test_main {
    public static void main(String[] args) {
        String str="hello.nihao.word.cc.jpg";
        String fileExtensionName=StringUtils.substringAfterLast(str,".");

        System.out.println(fileExtensionName);
    }
}
