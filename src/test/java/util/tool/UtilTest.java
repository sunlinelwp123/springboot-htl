package util.tool;

import base.BaseSpring;
import com.open.boot.util.common.StringUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilTest extends BaseSpring {
    private static final Logger log = LoggerFactory.getLogger(UtilTest.class);

    @Test
    public void compactSizeFormatTest(){
        String number="20480000";
        double num = (double)Integer.parseInt(number);
        log.info(num+"");
        String str= StringUtil.compactSizeFormat(number);
        log.info(str);
    }
}
