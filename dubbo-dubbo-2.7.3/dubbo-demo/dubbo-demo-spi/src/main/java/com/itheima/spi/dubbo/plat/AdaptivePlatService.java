package com.itheima.spi.dubbo.plat;

import com.itheima.spi.dubbo.PlatService;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.ExtensionLoader;

@Adaptive
public class AdaptivePlatService implements PlatService {
	
	@Override
	public void printPlat() {
		ExtensionLoader<PlatService> loader = ExtensionLoader.getExtensionLoader(PlatService.class);
		PlatService defaultExtension = loader.getDefaultExtension();
		defaultExtension.printPlat();
	}
	
	/**
	 * 由自己编写Adaptive扩展点时,知道如何根据参数选择不同的扩展点进行调用
	 * 通过字节码技术生成时,在生成某接口实现类的方法时,根据方法上的注解 @Adaptive 来决断
	 *
	 * @param plat
	 */
	@Override
	public void doSelect(String plat) {
		ExtensionLoader<PlatService> loader = ExtensionLoader.getExtensionLoader(PlatService.class);
		loader.getExtension(plat).doSelect(plat);
	}
	
}
