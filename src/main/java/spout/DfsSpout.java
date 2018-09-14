package spout;

import com.google.common.base.Strings;

import org.apache.storm.Config;
import org.apache.storm.metric.api.IMetric;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DfsSpout extends BaseRichSpout {

    private static final Logger logger = LoggerFactory.getLogger(DfsSpout.class);

    private SpoutConfig _spoutConfig;
    private SpoutOutputCollector _collector;

    private Configuration _dfsConfig;
    private String scandir;
    private String username;
    private URI uri;
    private boolean deletefile;
    private FileSystem dfs;

    private ConcurrentHashMap<UUID,Values> _pending;

    public DfsSpout(SpoutConfig spoutConf) {
        _spoutConfig = spoutConf;
    }

    @Override
    public void open(Map conf, final TopologyContext context, final SpoutOutputCollector collector) {
        _collector = collector;
        _pending = new ConcurrentHashMap<UUID,Values>();

        this._dfsConfig = new Configuration();
        this.scandir = _spoutConfig.scandir;
        this.username = _spoutConfig.username;
        this.deletefile = _spoutConfig.deletefile;
        this.uri = _spoutConfig.uri;

        try {
            this.dfs = FileSystem.get(uri,_dfsConfig,username);
        }catch (IOException e){
            logger.error(e.toString());
            System.exit(1);
        }catch (InterruptedException e){
            logger.info("Interrupted: ",e);
            System.exit(1);
        }
    }

    @Override
    public void close() {
        try {
            dfs.close();
        }catch (IOException e){
            logger.warn("close hdfs client error.",e);
        }
    }

    @Override
    public void nextTuple() {
        System.out.println("==========================0");
        InputStream in = null;
        RemoteIterator<LocatedFileStatus> file_iter = null;
        try {
            file_iter = dfs.listFiles(new Path(scandir), true);
            System.out.println("==========================1");
            while (file_iter.hasNext()) {
                LocatedFileStatus file = file_iter.next();
                Path filepath = file.getPath();

                System.out.println("==========================");
                System.out.println(filepath.toString());
                logger.info(filepath.toString());
                System.out.println("==========================");

                System.out.println(filepath.toString());
                in = dfs.open(filepath);
                byte[] output_obj = null;
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                IOUtils.copyBytes(in, out, 4096, false);

                UUID msgId = UUID.randomUUID();
                this._pending.put(msgId,new Values(out.toByteArray()));
                _collector.emit(new Values(out.toByteArray()),msgId);
                out.close();
                IOUtils.closeStream(in);
                if (this.deletefile) {
                    dfs.delete(filepath, true);
                }
            }
        } catch (IOException e) {
            logger.error("IOException error: ", e);
        }
    }

    @Override
    public void ack(Object msgId) {
        this._pending.remove(msgId);
    }

    @Override
    public void fail(Object msgId) {
        this._collector.emit(this._pending.get(msgId),msgId);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("dfs-spout"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration () {
        Map<String, Object> configuration = super.getComponentConfiguration();
        if (configuration == null) {
            configuration = new HashMap<>();
        }
        String configKeyPrefix = "config.";

        return configuration;
    }
}
