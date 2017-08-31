package com.gwcd.compile

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

public class PluginMain implements Plugin<Project> {

    @Override
    void apply(Project project) {
        // 是否进行配置
        def enable = project.rootProject.ext.compileConfig.enable
        // 配置文件名称
        def xmlName = project.rootProject.ext.compileConfig.xmlName
        if (!enable || xmlName == null) {
            println("不进行依赖构建")
            return
        }
        /**
         * 1.从xml文件中解析得到svn地址和文件名
         */
        println("配置文件解析")
        def configXml = project.rootProject.file(xmlName)
        List<ParseBean> configList = Parse.parseXml(configXml);
        if (configList == null) {
            throw GradleException("aar文件配置解析异常,请检查配置文件")
        }
        /**
         * 2.从svn中检出文件并放入libs文件夹中
         */
        println("开始进行检出")
        Files.checkoutFile(project, configList)

        println("开始进行依赖")
        project.fileTree(dir: 'libs', includes: ['*.aar']).each {
            file ->
                project.dependencies {
                    def fileName = file.getName()
                    def name = fileName.substring(0, fileName.indexOf("."))
                    println("依赖文件--${fileName}")
                    compile(name: name, ext: 'aar')
                }
        }
        println("依赖结束")
    }
}