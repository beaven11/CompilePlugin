package com.gwcd.compile

import groovy.io.FileType
import org.gradle.api.Project

class Files {

    static final def TEMP = "svnTemp"

    static checkoutFile(Project project, List<ParseBean> configList) {
        def temp = project.rootProject.file(TEMP)
        if (temp.exists()) temp.deleteDir()
        temp.mkdir()
        checkFromSvn(configList, temp)
        copyToLib(project, temp)
        temp.deleteDir()
    }

    private static checkFromSvn(List<ParseBean> configList, File tempDir) {
        for (int i = 0; i < configList.size(); i++) {
            ParseBean config = configList.get(i)
            def path = tempDir.getAbsolutePath() + File.separator + "" + i;
            new File(path).mkdir()
            def checkout = "svn checkout ${config.getSvn()} ${TEMP + "/" + i} --depth empty"
            println(checkout)
            exec(checkout.execute())
            config.getFile().each {
                file -> exec("svn up ${file}".execute(null, new File(path)))
            }
        }
    }

    private static copyToLib(Project project, File temDir) {
        temDir.eachFileRecurse(FileType.FILES) {
            file ->
                if (file.getName().endsWith(".aar")) {
                    project.copy {
                        from file.getAbsolutePath()
                        into 'libs'
                    }
                }
        }
    }

    // 执行svn检出命令
    private static exec(def proc) {
        def out = new StringBuilder(), err = new StringBuilder()
        proc.consumeProcessOutput(out, err)
        proc.waitFor()
        if (out.toString() != "" || err.toString() != "") {
            println "out> $out err> $err"
        }
    }
}