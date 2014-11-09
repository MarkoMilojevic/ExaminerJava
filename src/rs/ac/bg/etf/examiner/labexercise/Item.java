package rs.ac.bg.etf.examiner.labexercise;

import rs.ac.bg.etf.examiner.errors.*;

import java.io.*;

import org.apache.logging.log4j.*;

/**
 * Represents an item student needs to implement as a part of a <tt>Task</tt>.
 * It is an abstraction of a field, constructor or a method.
 * Has a name, description, value (number of points it is worth)
 * and a parent <tt>Task</tt>.
 *  
 * @author Marko Milojevic
 * 
 */
public class Item implements Serializable {
	
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(Item.class.getName());
    private String name;
    private String description;
    private int value;
    private Task parent;

    public Item(String name, String description, int value) {
        setName(name);
        setDescription(description);
        setValue(value);
    }
    
    public Item(Item item) {
        if (item == null) {
            log.error(ErrorMessages.NULL_ARGUMENT);
            throw new IllegalArgumentException();
        }
        
        setName(item.name);
        setDescription(item.description);
        setValue(item.value);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name == null) {
            log.error(ErrorMessages.NULL_ARGUMENT);
            throw new IllegalArgumentException();
        } else if (hasParent() && this.parent.containsItemByName(name)) {
        	log.error(ErrorMessages.ALREADY_IN_COLLECTION);
            throw new IllegalArgumentException();
        }
        
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        if (description == null) {
            log.error(ErrorMessages.NULL_ARGUMENT);
            throw new IllegalArgumentException();
        }
        
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        if (value <= 0) {
            log.error(ErrorMessages.NON_POSITIVE_VALUE);
            throw new IllegalArgumentException();
        }
        
        this.value = value;
    }

    public Task getParent() {
        return this.parent;
    }

    void setParent(Task parent) {
        if (this.parent == parent) {
            return;
        }

        if (hasParent()) {
            removeFromCurrentParent();
        }
        
        this.parent = parent;
    }

	private boolean hasParent() {
		return this.parent != null;
	}

	private void removeFromCurrentParent() {
		this.parent.removeItem(this);
	}
	
	public boolean equals(Object obj) {
		if (obj == null) {
	        return false;
	    } else if (obj == this) {
	        return true;
	    } else if (!(obj instanceof Item)) {
	        return false;
	    }
	
	    Item item = (Item) obj;
	    boolean haveSameParent = hasParent() && item.hasParent() ?
	    		this.parent.equals(item.parent) : false;
	    		
	    return this.name.equals(item.name) && haveSameParent;
	}
}
