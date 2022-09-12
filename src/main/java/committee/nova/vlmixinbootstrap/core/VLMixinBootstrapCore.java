package committee.nova.vlmixinbootstrap.core;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import net.minecraft.launchwrapper.Launch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import sun.misc.URLClassPath;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Borrowed from SpongeMixins
 */
@IFMLLoadingPlugin.MCVersion("1.6.4")
@IFMLLoadingPlugin.SortingIndex(13468)
@IFMLLoadingPlugin.Name(VLMixinBootstrapCore.NAME)
public class VLMixinBootstrapCore implements IFMLLoadingPlugin {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String NAME = "VeryLegacyMixinBootStrap";

    static {
        LOGGER.info("Initializing " + NAME);
        fixMixinClasspathOrder();
        MixinBootstrap.init();
    }

    private static void fixMixinClasspathOrder() {
        // Borrowed from VanillaFix -- Move jar up in the classloader's URLs to make sure that the latest version of Mixin is used
        final URL url = VLMixinBootstrapCore.class.getProtectionDomain().getCodeSource().getLocation();
        givePriorityInClasspath(url, Launch.classLoader);
        givePriorityInClasspath(url, (URLClassLoader) ClassLoader.getSystemClassLoader());
    }

    private static void givePriorityInClasspath(URL url, URLClassLoader classLoader) {
        try {
            final Field ucpField = URLClassLoader.class.getDeclaredField("ucp");
            ucpField.setAccessible(true);
            final List<URL> urls = new ArrayList<URL>(Arrays.asList(classLoader.getURLs()));
            urls.remove(url);
            urls.add(0, url);
            final URLClassPath ucp = new URLClassPath(urls.toArray(new URL[0]));
            ucpField.set(classLoader, ucp);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> map) {

    }


}
