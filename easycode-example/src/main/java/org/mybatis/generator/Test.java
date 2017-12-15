package org.mybatis.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.redistest.JRedisPoolConfig;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import static org.apache.hadoop.yarn.webapp.hamlet.HamletSpec.InputType.file;

/**
 * 修改 IntrospectedTable.java 下的 calculateJavaClientAttributes 方法下的 【sb.append(“Mapper”);】，把他改为【sb.append(“Dao”);】
 */
public class Test {
//    private static final String PATH_STRING=Test.class.getClass().getResource("/").getPath()+"generatorConfig.xml";
//    private static final String PATH_STRING="src/main/java/org/mybatis/generator/generatorConfig.xml";
    //        InputStream propFile = Test.class.getResourceAsStream("generatorConfig.xml");
    private static final String PATH_STRING="F:\\project\\ideapro\\mypro\\easycode\\easycode-parent\\easycode-example\\src\\main\\java\\org\\mybatis\\generator\\generatorConfig.xml";

    public static void main(String[] args) throws Exception {

        System.out.println(new File("").getAbsolutePath());

//		makeRun();
        String[] tables = {
//				"dsp_t_mcc_device_count",
				"admin_user"
        };
        for (String string : tables) {
            if ("".equals(string)) {
                continue;
            }
            String[] split = string.split("_");
            String model = "";
            for (int i = 2; i < split.length; i++) {
                model += split[i].substring(0, 1).toUpperCase() + split[i].substring(1);
            }
            model += "Model";
            changeFileName(string, model);
            makeRun();
        }

    }


    public static void makeRun() throws Exception {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        File configFile = new File(PATH_STRING);
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }

    public static void changeFileName(String tableName, String beanName) throws Exception {
        //读取XML文件，获得document对象
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(PATH_STRING));

        //获得root节点的属性对象
        Element rootElem = document.getRootElement();
        Element element = rootElem.element("context").element("table");

        Attribute tableNameA = element.attribute("tableName");
        tableNameA.setValue(tableName);

        Attribute domainObjectNameA = element.attribute("domainObjectName");
        domainObjectNameA.setValue(beanName);
        //设置文件编码
        OutputFormat xmlFormat = new OutputFormat();
        xmlFormat.setEncoding("UTF-8");
        // 设置换行
        xmlFormat.setNewlines(true);
        // 生成缩进
        xmlFormat.setIndent(true);
        // 使用4个空格进行缩进, 可以兼容文本编辑器
        xmlFormat.setIndent("    ");
        //将某节点的属性和值写入xml文档中
        XMLWriter writer = new XMLWriter(new FileWriter(PATH_STRING), xmlFormat);
        writer.write(document);
        writer.close();
    }


    public void dom4j(String path) throws Exception {
        //读取XML文件，获得document对象
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(path));

        //获得某个节点的属性对象
        Element rootElem = document.getRootElement();
        //获取根节点属性对象
        Attribute rootAttr = rootElem.attribute("id");

        //获取指定节点属性对象
        Element contactElem = rootElem.element("contact");
        Attribute contactAttr = contactElem.attribute("id");

        //遍历某个节点的所有属性
        for (Iterator it = contactElem.attributeIterator(); it.hasNext(); ) {
            Attribute conAttr = (Attribute) it.next();
            String conTxt = conAttr.getValue();
            String conAttrName = conAttr.getName();
            System.out.println(conAttrName + " = " + conTxt);
        }
        //设置某节点的属性和值
        contactElem.addAttribute("name", "zhangsan");

        //设置(更改)某属性的值
        Attribute nameAttr = contactElem.attribute("name");
        nameAttr.setValue("lisi");

        //删除某节点的指定属性
//		  contactElem.remove(nameAttr);
        //将某节点的属性和值写入xml文档中
        XMLWriter writer = new XMLWriter(new FileWriter(path));
        writer.write(document);
        writer.close();

        /**
         * 如果文档中有中文需要设置字符编码
         * 用如下语句:
         * OutputFormat format = OutputFormat.createPrettyPrint();
         * format.setEncoding("GBK");
         * XMLWriter writer = new XMLWriter(new FileWriter("./src/contact.xml"),format);
         */
        //获取指定对象的属性名
//		  System.out.println(rootAttr.getName());
//		  System.out.println(contactAttr.getName());
//		  //获取指定对象的属性值
//		  System.out.println(contactAttr.getValue());
//		  System.out.println(rootAttr.getValue());
    }
}
