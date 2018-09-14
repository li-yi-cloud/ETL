package spout;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;


abstract class AbstractFileReader implements FileReader {

    private final Path file;

    public AbstractFileReader(FileSystem fs, Path file) {
        if (fs == null ) {
            throw new IllegalArgumentException("filesystem arg cannot be null for reader");
        }
        if (file == null ) {
            throw new IllegalArgumentException("file arg cannot be null for reader");
        }
        this.file = file;
    }

    @Override
    public Path getFilePath() {
        return file;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        AbstractFileReader that = (AbstractFileReader) o;

        return !(file != null ? !file.equals(that.file) : that.file != null);
    }

    @Override
    public int hashCode() {
        return file != null ? file.hashCode() : 0;
    }

}
