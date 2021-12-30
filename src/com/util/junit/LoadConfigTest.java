package com.util.junit;

import com.util.bean.Constants;
import com.util.func.config.FileConfigUtils;
import com.util.func.config.PropertiesUtils;
import com.util.func.config.ResourceUtils;
import com.util.func.message.MessageUtils;

public class LoadConfigTest {
    public static void main(String[] args) throws Exception {
        /**
         * FileConfigUtils
         */
        String useCacheDataFileCfg = FileConfigUtils.getInstance().getValue(Constants.USE_CACHE_DATA);
        System.out.println("LoadConfigTest useCacheData: " + useCacheDataFileCfg);

        /**
         * PropertiesUtils
         */
        String useCacheDataProp = PropertiesUtils.getInstance().getProperty(Constants.USE_CACHE_DATA);
        System.out.println("PropertiesUtils useCacheData: " + useCacheDataProp);

        /**
         * ResourceUtils
         */
        String r0000 = ResourceUtils.getInstance().getResource(Constants.Config.DEFAULT_BUNDLE, "R0000");
        System.out.println("ResourceUtils r0000: " + r0000);
    }
}
