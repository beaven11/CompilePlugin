package com.gwcd.compile

class Parse {
    static List<ParseBean> parseXml(File file) {
        if (!file.exists()) return null
        List<ParseBean> configList = new ArrayList<>();
        def parse = new XmlParser().parse(file)
        parse.compile.each {
            compile ->
                ParseBean config = new ParseBean()
                config.setSvn(compile.svn.text())
                List<String> files = new ArrayList<>()
                compile.file.each {
                    fileName -> files.add(fileName.text())
                }
                config.setFile(files)
                configList.add(config)
        }
        return configList
    }
}