package rs.ac.bg.rs.examiner.labexercise;

import rs.ac.bg.rs.examiner.errors.*;

import java.io.*;
import java.util.*;

import org.apache.logging.log4j.*;

/**
 * Represents a task student needs to implement as a part of an <tt>Assignment</tt>.
 * It is an abstraction of a class or an interface. Has a name,
 * list of mandatory <tt>Item</tt>s, and a parent <tt>Assignment</tt>.
 * 
 * @author Marko Milojevic
 * 
 */
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(Task.class.getName());
    private String name;
    private List<Item> items;
    private Assignment parent;

    {
        this.items = new ArrayList<Item>();
    }
    
    public Task(String name) {
        setName(name);
    }

    public Task(Task task) {
        if (task == null) {
            log.error(ErrorMessages.NULL_ARGUMENT);
            throw new IllegalArgumentException();
        }

        setName(task.name);
        for (Item item : task.items) {
            addItem(new Item(item));
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name == null) {
            log.error(ErrorMessages.NULL_ARGUMENT);
            throw new IllegalArgumentException();
        } else if (hasParent() && this.parent.containsTaskByName(name)) {
        	log.error(ErrorMessages.ALREADY_IN_COLLECTION);
            throw new IllegalArgumentException();
        }

        this.name = name;
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(this.items);
    }
    
    public Item getItemByName(String itemName) {
    	if (itemName == null) {
    		return null;
    	}
    	
    	for (Item item : this.items) {
    		if (item.getName().equals(itemName)) {
    			return item;
    		}
    	}
    	
    	return null;
    }

    public boolean addItem(Item item) {
        if (item == null) {
            return false;
        } else if (containsItemByName(item.getName())) {
            log.error(ErrorMessages.ALREADY_IN_COLLECTION);
            throw new IllegalArgumentException();
        }

        boolean opResult = this.items.add(item);
        if (opResult == true) {
            item.setParent(this);
        }

        return opResult;
    }

    public boolean removeItem(Item item) {
        if (item == null) {
            return false;
        }

        boolean opResult = this.items.remove(item);
        if (opResult == true) {
            item.setParent(null);
        }

        return opResult;
    }
    
    public boolean removeItemByName(String itemName) {
    	Item item = getItemByName(itemName);
    	return removeItem(item);
    }

    public boolean containsItem(Item item) {
        if (item == null) {
            return false;
        }

        return this.items.contains(item);
    }
    
    public boolean containsItemByName(String itemName) {
        if (itemName == null) {
            return false;
        }

        for (Item item : this.items) {
        	if (item.getName().equals(itemName)) {
        		return true;
        	}
        }
        
        return false;
    }

    public Assignment getParent() {
	    return this.parent;
	}

	void setParent(Assignment parent) {
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
		this.parent.removeTask(this);
	}
	
	public int getValue() {
	    int value = 0;
	    for (Item item : this.items) {
	        value += item.getValue();
	    }
	
	    return value;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
	        return false;
	    } else if (obj == this) {
	        return true;
	    } else if (!(obj instanceof Task)) {
	        return false;
	    }
	
	    Task task = (Task) obj;
	    boolean haveSameParent = hasParent() && task.hasParent() ?
	    		this.parent.equals(task.parent) : false;
	    		
	    return this.name.equals(task.name) && haveSameParent;
	}
}
