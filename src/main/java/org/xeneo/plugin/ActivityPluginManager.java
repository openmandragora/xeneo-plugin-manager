package org.xeneo.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.xeneo.core.plugin.*;

/**
 *
 * @author Stefan Huber
 */
public class ActivityPluginManager {
    
    Map<String,ActivityPlugin> plugins = new HashMap<String,ActivityPlugin>();   
    Map<String,ScheduledFuture> tasks = new HashMap<String,ScheduledFuture>();
    
    
    @Autowired
    private PluginInstantiator pi;
    
    @Autowired
    private PluginInstanceManager pim;
    
    @Autowired
    private PluginManager pm;
        
    public void startActivityPluginInstance(String pluginURI, String ownerURI, PluginConfiguration pc) {
        String instanceid = pluginURI + ownerURI;          
        
        if (pc.getID() < 0 && !plugins.containsKey(instanceid)) {
            
            PluginDescriptor pd = pm.getPluginDescriptor(pluginURI);            
            ActivityPlugin ap = (ActivityPlugin) pi.createPluginInstance(pd, ownerURI);      
            ap.setPluginConfiguration(pc);
            ap.init();
            // ScheduledFuture sf = scheduler.scheduleWithFixedDelay(ap,1000L);
            
            plugins.put(instanceid, ap);
            // tasks.put(instanceid, sf);
            
        } else if (pc.getID() < 0 && plugins.containsKey(instanceid)) {
            
        } else if (pc.getID() > 0 && !plugins.containsKey(instanceid)) {
            
        } else if (pc.getID() > 0 && plugins.containsKey(instanceid)) {
            
        }        
    }
    
    public void stopActivityPluginInstance(int id) {
        
    }
        
    
}
