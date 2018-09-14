package spout;

/**
 * Represents the notion of an offset in a file. Idea is accommodate representing file
 * offsets other than simple byte offset as it may be insufficient for certain formats.
 * Reader for each format implements this as appropriate for its needs.
 * Note: Derived types must :
 *       - implement equals() & hashCode() appropriately.
 *       - implement Comparable<> appropriately.
 *       - implement toString() appropriately for serialization.
 *       - constructor(string) for deserialization
 */

interface FileOffset extends Comparable<FileOffset>, Cloneable {
    /** tests if rhs == currOffset+1 */
    boolean isNextOffset(FileOffset rhs);
    FileOffset clone();
}
