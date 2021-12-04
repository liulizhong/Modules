package hbaseanalyze.kv;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CountDurationValue implements Writable {

    private int callCount;
    private int callDuration;

    public CountDurationValue() {
    }

    public int getCallCount() {
        return callCount;
    }

    public void setCallCount(int callCount) {
        this.callCount = callCount;
    }

    public int getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(int callDuration) {
        this.callDuration = callDuration;
    }

    @Override
    public String toString() {
        return callCount + "\t" + callDuration;
    }

    @Override
    public void write(DataOutput out) throws IOException {

        out.writeInt(callCount);
        out.writeInt(callDuration);

    }

    @Override
    public void readFields(DataInput in) throws IOException {

        this.callCount = in.readInt();
        this.callDuration = in.readInt();
    }
}
