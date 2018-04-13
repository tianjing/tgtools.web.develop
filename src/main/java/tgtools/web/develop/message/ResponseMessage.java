package tgtools.web.develop.message;

import java.io.Serializable;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 9:50
 */
public class ResponseMessage  implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean mStatus;
    private Object mData;

    public boolean getStatus() {
        return mStatus;
    }

    public void setStatus(boolean pStatus) {
        mStatus = pStatus;
    }

    public Object getData() {
        return mData;
    }

    public void setData(Object pData) {
        mData = pData;
    }
}
