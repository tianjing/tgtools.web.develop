package tgtools.web.develop.message;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 10:11
 */
public class GridMessage extends ResponseMessage {
    private int mTotal=0;

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int pTotal) {
        mTotal = pTotal;
    }
}
