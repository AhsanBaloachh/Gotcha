package com.example.rabia.myproject;

/**
 * Created by AHS on 5/5/2015.
 */
public class Setting {
    String title;
    String desc;
    boolean checked;
    boolean isCheckAvailable;
    boolean isCategory;
    Setting(String _title,
            String _desc,
            boolean _checked,
             boolean _isCheckAvailable)
    {
        title= _title;
        desc= _desc;
        checked= _checked;
        isCheckAvailable= _isCheckAvailable;
        isCategory = false;
    }
    boolean get_isCategory()
    {	return isCategory;	}
    void set_isCategory(boolean _isCategory)
    {	isCategory= _isCategory;	}

    public String get_title()
    {	return title;	}
    public void set_title(String _title)
    {	title= _title;	}
    public String get_desc()
    {	return desc;	}
    public void set_desc( String _desc)
    {	desc= _desc;	}
    public boolean get_checked()
    {	return checked;	}
    public void set_checked( boolean _checked)
    {	checked= _checked;	}
    public boolean get_isCheckAvailable()
    {	return isCheckAvailable;	}
    public void set_isCheckAvailable( boolean _isCheckAvailable)
    {	isCheckAvailable= _isCheckAvailable;	}

}