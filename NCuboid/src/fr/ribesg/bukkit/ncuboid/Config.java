package fr.ribesg.bukkit.ncuboid;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.ribesg.bukkit.ncore.AbstractConfig;

/**
 * The config for the NCuboid node
 * 
 * @author Ribesg
 */
public class Config extends AbstractConfig {
    
    @Getter @Setter(AccessLevel.PRIVATE) private static Material selectionItemMaterial;
    
    /**
     * @see AbstractConfig#setValues(YamlConfiguration)
     */
    @Override
    protected void setValues(YamlConfiguration config) {
        
        // selectionItemMaterial. Default : Stick/280
        final Material m = Material.getMaterial(config.getInt("selectionItemMaterial", 280));
        setSelectionItemMaterial(m == null ? Material.STICK : m);
    }
    
    /**
     * @see AbstractConfig#getConfigString()
     */
    @Override
    protected String getConfigString() {
        final StringBuilder content = new StringBuilder();
        
        // Header
        content.append("################################################################################\n");
        content.append("# Config file for NCuboid plugin. If you don't understand something, please    #\n");
        content.append("# ask on dev.bukkit.org                 .                               Ribesg #\n");
        content.append("################################################################################\n\n");
        
        // selectionItemMaterial. Default : Stick/280
        content.append("# The tool used to select points and get informations about blocks protection. Default : 180 (Stick)\n");
        content.append("selectionItemMaterial: " + getSelectionItemMaterial().getId() + "\n\n");
        
        return content.toString();
    }
}
