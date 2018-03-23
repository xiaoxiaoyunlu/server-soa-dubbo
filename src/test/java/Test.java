import org.springframework.beans.factory.InitializingBean;

/**
 * spring里面最常用的接口InitializingBean
 * 初始化类之后，可以做一个扩展操作，必须重载 afterPropertiesSet方法
 * @author USER
 *
 */
public class Test implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		

	}

}
