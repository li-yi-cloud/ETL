package spout;

import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

public class SpoutConfig implements Serializable {
    public String scandir = null;
    public String username = "hdfs";
//    public Configuration dfsconf = null;
    public URI uri = null;
    public boolean deletefile = false;

    private static final Logger logger = LoggerFactory.getLogger(SpoutConfig.class);
/*
    public void SpoutConfig(Configuration hdfsconfig, String namenode, String scandir, String username){
        this.dfsconf = hdfsconfig;
        this.scandir = scandir;
        this.username = username;
        try {
            this.uri = new URI("hdfs://" + namenode + ":8020");
        } catch (URISyntaxException e){
            logger.error("URI Syntax error: ",e);
            System.exit(1);
        }
    }

    public void SpoutConfig(Configuration hdfsconfig, String namenode, String scandir){
        this.dfsconf = hdfsconfig;
        this.scandir = scandir;
        try {
            this.uri = new URI("hdfs://" + namenode + ":8020");
        } catch (URISyntaxException e){
            logger.error("URI Syntax error: ",e);
            System.exit(1);
        }
    }
    */
    public SpoutConfig(String namenode,String scandir,String username,boolean deletefile){
//        this.dfsconf = hdfsconfig;

        this.username = username;
        this.deletefile = deletefile;
        try {
            this.uri = new URI("hdfs://" + namenode + ":8020");
        } catch (URISyntaxException e){
            logger.error("URI Syntax error: ",e);
            System.exit(1);
        }
        this.scandir = this.uri+scandir;
    }
    /*
    public void SpoutConfig(Configuration hdfsconfig,String namenode,String scandir,boolean deletefile){
        this.dfsconf = hdfsconfig;
        this.scandir = scandir;
        this.deletefile = deletefile;
        try {
            this.uri = new URI("hdfs://" + namenode + ":8020");
        } catch (URISyntaxException e){
            logger.error("URI Syntax error: ",e);
            System.exit(1);
        }
    }*/
}
