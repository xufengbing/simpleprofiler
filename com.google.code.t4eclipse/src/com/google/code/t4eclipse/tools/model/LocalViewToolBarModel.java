package com.google.code.t4eclipse.tools.model;

import org.eclipse.swt.widgets.ToolItem;

public class LocalViewToolBarModel {

	//the toolitem itself
	public ToolItem item;

	//for a toolbar item in view, one of its  id and ToolTip is exists in most situations.
	//the contribution ID, also can be null
	public String ID;
	//can be null
	public String ToolTip;



	//PUSH, CHECK, RADIO, SEPARATOR, DROP_DOWN
	public int Style;

	public boolean Enabled;

	public boolean Selected;

 public LocalViewToolBarModel() {
	 this.ID="";
	 this.ToolTip="";
	 this.Enabled=false;
	 this.Selected=false;
}
}
