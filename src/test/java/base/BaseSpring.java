/**
 * @author: xuechenming-ghq
 * @date: 2014-11-10-上午11:41:24
 */
package base;

import com.open.boot.ActorApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * spring junit测试基类，加载spring配置文件
 * 
 * @author zhangxingmei-ghq
 * @since 2017-2-10
 */

@RunWith(SpringJUnit4ClassRunner.class)
//表示继承了SpringJUnit4ClassRunner类
@SpringBootTest(classes= ActorApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("dev")
public class BaseSpring {

}
