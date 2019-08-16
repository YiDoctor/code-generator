package com.yim.generator;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import static org.springframework.util.ResourceUtils.*;

/**
 * mybatis-plus 代码生成类
 * 需注意tinyint--boolean
 * Create By @author Yim On 2019-5-31 17:36
 **/
public class CodeGenerator {

    /**
     * 获取CodeGenerator包所在路径，默认为生成代码包路径
     */
    private static final String PAGE_PATH = CodeGenerator.class.getPackage().getName();

    /** 获取项目路径，此处针对的IDEA中的项目
     * @params []
     * @return java.lang.String
     */
    private String  projectPath(){

        String projectPath = null;
        try {
            projectPath = getFile("classpath:").getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        projectPath = projectPath.replace("\\target", "").replace("\\classes","");
        return projectPath;
    }

    /**
     * 读取application。properties获取数据源连接信息
     * @params []
     * @return com.baomidou.mybatisplus.generator.config.DataSourceConfig
     */
    private DataSourceConfig dataSource(){

        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        Properties properties = null;
        try {
            properties = PropertiesLoaderUtils.loadAllProperties("application.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataSourceConfig.setUrl(properties.getProperty("spring.datasource.url"));
        dataSourceConfig.setDriverName(properties.getProperty("spring.datasource.driver-class-name"));
        dataSourceConfig.setUsername(properties.getProperty("spring.datasource.username"));
        dataSourceConfig.setPassword(properties.getProperty("spring.datasource.password"));
        return dataSourceConfig;
    }
    /**
     * 全局配置信息
     * @params: []
     * @return: com.baomidou.mybatisplus.generator.config.GlobalConfig
     */
    private GlobalConfig globalConfig(){

        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(projectPath()+"/src/main/java");
        globalConfig.setAuthor("yim");
        globalConfig.setOpen(false);
        return globalConfig;
    }
    /**
     * 策略配置信息（策略模式）
     * @params: []
     * @return: com.baomidou.mybatisplus.generator.config.StrategyConfig
     */
    private StrategyConfig strategyConfig(){

        StrategyConfig strategyConfig = new StrategyConfig();
        // entity的父类，有需要时候可定义
        String superEntity = "com。yim.generatory.BaseEntity";
        // Controller的父类，有需要时候可定义
        String superController = "com.yim.generatory.BaseController";

        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        //strategyConfig.setSuperEntityClass(superEntity);
        // 此处设置生成Lombok注解的实体类，默认false
        strategyConfig.setEntityLombokModel(true);
        // 生成接口风格
        strategyConfig.setRestControllerStyle(true);
        //strategyConfig.setSuperControllerClass(superController);
        strategyConfig.setSuperEntityColumns("id");
        strategyConfig.setControllerMappingHyphenStyle(true);
        strategyConfig.setTablePrefix(packageConfig().getModuleName() + "_");
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        return strategyConfig;
    }
    /**
     * 包路径信息
     * @params: []
     * @return: com.baomidou.mybatisplus.generator.config.PackageConfig
     */
    private PackageConfig packageConfig(){

        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent(PAGE_PATH);
        return packageConfig;
    }
    /**
     * 注入配置
     * @params: []
     * @return: com.baomidou.mybatisplus.generator.InjectionConfig
     */
    private InjectionConfig injectionConfig(){

        InjectionConfig injectionConfig = new InjectionConfig(){
            @Override
            public void initMap() {
            }
        };

        List<FileOutConfig> list = new ArrayList<FileOutConfig>();
        //模板信息
        list.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                try {
                    //XML文件路径配置
                    return projectPath() + "/src/main/resources/mapper/" + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                } catch (Exception e) {
                    return null;
                }
            }
        });

        injectionConfig.setFileOutConfigList(list);
        return injectionConfig;
    }
    public static void main(String[] args){

        CodeGenerator generator = new CodeGenerator();
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setGlobalConfig(generator.globalConfig());
        autoGenerator.setDataSource(generator.dataSource());
        autoGenerator.setPackageInfo(generator.packageConfig());
        autoGenerator.setCfg(generator.injectionConfig());
        autoGenerator.setTemplate(new TemplateConfig().setXml(null));

        StrategyConfig strategyConfig = generator.strategyConfig();
        // 此处不添加默认生成数据库中所有的表的代码
        //strategyConfig.setInclude(generator.scanner("请输入表名,多表以英文,隔开"));
        autoGenerator.setStrategy(strategyConfig);

        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());
        autoGenerator.execute();
    }

    private String[] scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(tip);
        String[] split = scanner.nextLine().split(",");
        return split;
    }
}
