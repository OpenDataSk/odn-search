package sk.opendatanode.utils;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AppProperties extends Properties {

    private static final long serialVersionUID = 8897387475702036320L;
    private static AppProperties instance = null;
    
    private static Logger logger = LoggerFactory.getLogger(AppProperties.class);
    
    private AppProperties(String fileName) throws IOException {
        super();
        this.load(getClass().getResourceAsStream("/"+fileName));
        logger.info("loaded properties file: "+fileName);
    }

    public static AppProperties getInstance(String fileName) {
        if(instance == null) {
            try {
                instance = new AppProperties(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
}
