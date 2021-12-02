package oracle.druid工具类;

/**
 * @author lizhong.liu
 * @version TODO
 * @class Bean的表类，和数据库的字段对应
 * @CalssName Department
 * @create 2020-03-25 15:32
 * @Des TODO
 */
public class TB_EMP_ATTENDTIMERULE {
    private int id;
    private String name;
    private int WORKTIMELONG;
    private int WORKCOUNT;
    private int LINKWORKTIMELONG;
    private int LINKWORKCOUNT;

    @Override
    public String toString() {
        return "TB_EMP_ATTENDTIMERULE{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", WORKTIMELONG=" + WORKTIMELONG +
                ", WORKCOUNT=" + WORKCOUNT +
                ", LINKWORKTIMELONG=" + LINKWORKTIMELONG +
                ", LINKWORKCOUNT=" + LINKWORKCOUNT +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWORKTIMELONG() {
        return WORKTIMELONG;
    }

    public void setWORKTIMELONG(int WORKTIMELONG) {
        this.WORKTIMELONG = WORKTIMELONG;
    }

    public int getWORKCOUNT() {
        return WORKCOUNT;
    }

    public void setWORKCOUNT(int WORKCOUNT) {
        this.WORKCOUNT = WORKCOUNT;
    }

    public int getLINKWORKTIMELONG() {
        return LINKWORKTIMELONG;
    }

    public void setLINKWORKTIMELONG(int LINKWORKTIMELONG) {
        this.LINKWORKTIMELONG = LINKWORKTIMELONG;
    }

    public int getLINKWORKCOUNT() {
        return LINKWORKCOUNT;
    }

    public void setLINKWORKCOUNT(int LINKWORKCOUNT) {
        this.LINKWORKCOUNT = LINKWORKCOUNT;
    }

    public TB_EMP_ATTENDTIMERULE(int id, String name, int WORKTIMELONG, int WORKCOUNT, int LINKWORKTIMELONG, int LINKWORKCOUNT) {
        this.id = id;
        this.name = name;
        this.WORKTIMELONG = WORKTIMELONG;
        this.WORKCOUNT = WORKCOUNT;
        this.LINKWORKTIMELONG = LINKWORKTIMELONG;
        this.LINKWORKCOUNT = LINKWORKCOUNT;
    }

    public TB_EMP_ATTENDTIMERULE() {
    }
}
