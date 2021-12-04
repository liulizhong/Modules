package hbaseanalyze.kv;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CommDimension implements WritableComparable<CommDimension> {

    //联系人属性
    private ContactDimension contactDimension = new ContactDimension();

    //时间属性
    private DateDimension dateDimension = new DateDimension();

    public CommDimension() {
    }

    public ContactDimension getContactDimension() {
        return contactDimension;
    }

    public void setContactDimension(ContactDimension contactDimension) {
        this.contactDimension = contactDimension;
    }

    public DateDimension getDateDimension() {
        return dateDimension;
    }

    public void setDateDimension(DateDimension dateDimension) {
        this.dateDimension = dateDimension;
    }

    @Override
    public String toString() {
        return contactDimension + "\t" + dateDimension;
    }

    @Override
    public int compareTo(CommDimension o) {

        int result;

        //先比较联系人是否相同
        result = this.contactDimension.compareTo(o.contactDimension);

        if (result == 0) {

            //比较时间维度
            result = this.dateDimension.compareTo(o.dateDimension);
        }
        return result;
    }

    //序列化
    @Override
    public void write(DataOutput out) throws IOException {
        this.contactDimension.write(out);
        this.dateDimension.write(out);
    }

    //反序列化
    @Override
    public void readFields(DataInput in) throws IOException {
        this.contactDimension.readFields(in);
        this.dateDimension.readFields(in);
    }
}
