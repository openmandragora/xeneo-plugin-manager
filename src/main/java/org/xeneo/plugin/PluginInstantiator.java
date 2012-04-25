/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xeneo.plugin;

import java.util.logging.Level;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.xeneo.core.plugin.*;

/**
 *
 * @author Stefan Huber
 */
public class PluginInstantiator {
    
    Logger logger = LoggerFactory.getLogger(PluginInstantiator.class);
        
    private BundleContext bc;
    
    private PluginInstanceManager pim;

    public void setBundleContext(BundleContext bundleContext) {
        bc = bundleContext;
    }   
    
    public Plugin createPluginInstance(PluginDescriptor pd, String ownerURI) {
        
        try {
            Bundle b = bc.getBundle(pd.getID());                    
            Class pluginClass = b.loadClass(pd.getPluginClass());
            
            if (pd.getPluginType().equals(PluginDescriptor.ACTIVITY_PLUGIN_TYPE)) {
                Object object = pluginClass.newInstance();
                
                if (object instanceof ActivityPlugin) {
                    int id = pim.createPluginInstance(pd.getPluginURI(),ownerURI);
                    Plugin plugin = (Plugin) object;
                    plugin.setID(id);
                    
                    return plugin;
                }              
            }
            
        } catch (InstantiationException ex) {
            logger.error("Instanziation failed: " + ex.getMessage());
        } catch (IllegalAccessException ex) {
            logger.error("Instanziation failed: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            logger.error("The Plugin Instance could not be created for the reason of: " + ex.getMessage());
        }        
        
        // TODO: maybe an FailedInstanceException fits better here!
        return null;
    }    
}
