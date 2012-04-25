/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xeneo.plugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xeneo.core.plugin.ActivityPlugin;
import org.xeneo.core.plugin.PluginConfiguration;

/**
 *
 * @author Stefan Huber
 */
public class PluginWrapper implements ActivityPlugin {
    
    private static Logger logger = LoggerFactory.getLogger(PluginWrapper.class);

    private Map<Integer,PluginConfiguration> pcs = new HashMap<Integer,PluginConfiguration>();    
    
    private ActivityPlugin plugin;
    
    public void setID(int id) {}

    public int getID() {
        return plugin.getID();
    }

    public void setPluginConfiguration(PluginConfiguration pc) {
        if (pc.getID() < 0) {
            logger.info("The Plugin was configured not correctly!");       
            // TODO: maybe error!
        } else {
            pcs.put(pc.getID(), pc);           
        }
    }

    public PluginConfiguration getPluginConfiguration() {
        return plugin.getPluginConfiguration();
    }

    public void init() {        
    }   
    
    public PluginWrapper(ActivityPlugin ap) {
        this.plugin = ap;
    }

    Iterator<Integer> it = null;
    public void run() {
        if (it != null && it.hasNext()) {
            this.callWithConfigurationUpdate(it.next());
        } else {
            if (!pcs.isEmpty()) {
                it = pcs.keySet().iterator();
                if (it.hasNext()) {
                    this.callWithConfigurationUpdate(it.next());
                }
            } else {
                logger.info("ActivityPlugin with ID: " + plugin.getID() + " has no configurations.");
            }
        }
    }
    
    protected void callWithConfigurationUpdate(Integer i) {
        
        plugin.setPluginConfiguration(pcs.get(i));
        
        plugin.init();
        
        plugin.run();
        
    }
}
