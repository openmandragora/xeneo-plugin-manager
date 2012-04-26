/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xeneo.plugin;

import java.util.Dictionary;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.osgi.context.BundleContextAware;
import org.xeneo.core.plugin.PluginDescriptor;
import org.xeneo.core.plugin.PluginManager;

/**
 *
 * @author Stefan Huber
 */
public class PluginListener implements BundleContextAware, BundleListener {

    private static Logger logger = LoggerFactory.getLogger(PluginListener.class);

    @Autowired
    private PluginManager pm;
    
    public void init() {
        bc.addBundleListener(this);
        logger.info("Plugin Listener initialized and listening for new plugins.");
    }
    private BundleContext bc;

    public void setBundleContext(BundleContext bundleContext) {
        bc = bundleContext;
    }

    public void bundleChanged(BundleEvent be) {

        if (be.getType() == BundleEvent.INSTALLED) {
            Bundle b = be.getBundle();

            Dictionary<String, String> d = b.getHeaders();
            String pluginURI = d.get(PluginDescriptor.PLUGIN_URI);

            if (pluginURI != null) {
                PluginDescriptor pd = new PluginDescriptor();
                pd.setPluginURI(pluginURI);
                pd.setTitle(d.get("Bundle-Name"));
                pd.setDescription(d.get("Bundle-Description"));
                pd.setID(b.getBundleId());
                
                String pluginType = d.get(PluginDescriptor.PLUGIN_TYPE);
                logger.info("A new Plugin with URI: " + pluginURI + "and type: " + pluginType + ", was registered.");
                pd.setPluginType(pluginType);
                
                if (pluginType.equals(PluginDescriptor.ACTIVITY_PLUGIN_TYPE)) {
                    String pluginClassname = d.get(PluginDescriptor.PLUGIN_CLASSNAME);
                    pd.setPluginClass(pluginClassname);                   
                }

                pm.addPlugin(pd);
            }
        } else if (be.getType() == BundleEvent.UNINSTALLED) {
            Bundle b = be.getBundle();
            Dictionary<String, String> d = b.getHeaders();
            String pluginURI = d.get(PluginDescriptor.PLUGIN_URI);           
            
            if (pluginURI != null && !pluginURI.isEmpty()) {
                pm.deactivatePlugin(pluginURI);            
                logger.info("Plugin with URI: " + pluginURI + " was unistalled and therefore deactivated.");
            }
        }
    }
}
