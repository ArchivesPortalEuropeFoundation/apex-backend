package eu.apenet.dashboard.tree;

import eu.apenet.dashboard.AbstractAction;

public abstract class DynatreeAction extends AbstractAction {
	
	protected static final String FOLDER_LAZY = "\"isFolder\": true, \"isLazy\": true";
	protected static final String FOLDER_NOT_LAZY = "\"isFolder\": true";
	protected static final String SELECTABLE = "\"unselectable\": false";
	protected static final String NOT_SELECTABLE = "\"unselectable\": true";
	protected static final String NOT_CHECKBOX = "\"hideCheckbox\": true";
	protected static final String NO_LINK = "\"noLink\": true";
	protected static final String LINK = "\"noLink\": false";
	protected static final String UTF8 = "UTF-8";
	protected static final String END_ARRAY = "]\n";
	protected static final String START_ARRAY = "[\n";
	protected static final String END_ITEM = "}";
	protected static final String START_ITEM = "{";
	protected static final String COMMA = ",";
	
}
