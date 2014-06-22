package info.androidhive.slidingmenu.adapter;

import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.model.NavDrawerItem;
import info.androidhive.slidingmenu.model.NavMenuItem;
import info.androidhive.slidingmenu.model.NavMenuSection;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerListAdapter {
    public static class CompositeListFromLists<E> extends AbstractList<E> {

        private final List<E> list1;
        private final List<E> list2;

        public CompositeListFromLists(List<E> list1, List<E> list2) {
            this.list1 = list1;
            this.list2 = list2;
        }

        @Override
        public E get(int index) {
            if (index < list1.size()) {
                return list1.get(index);
            }
            return list2.get(index-list1.size());
        }

        @Override
        public int size() {
            return list1.size() + list2.size();
        }
    }

    final ArrayAdapter<NavDrawerItem> arrayAdapter;
    final ArrayList<NavDrawerItem> sysMenuObjects;
    final ArrayList<NavDrawerItem> pageObjects;
    final CompositeListFromLists<NavDrawerItem> lists;
    private LayoutInflater inflater;

    public ArrayAdapter<NavDrawerItem> getArrayAdpater() {
    	return arrayAdapter;
    }
    
    public void resetPageMenuItems(String menuSectionText) {
	clearPageMenuItems();
	addPageMenuItem(NavMenuSection.create(100000,menuSectionText));
    }

    public void clearPageMenuItems() {
    	pageObjects.clear();
    	arrayAdapter.notifyDataSetChanged();
    }

    public void addSysMenu(NavDrawerItem item) {
    	sysMenuObjects.add(item);
    }
    public void addPageMenuItem(NavDrawerItem item) {
    	pageObjects.add(item);
    	arrayAdapter.notifyDataSetChanged();
    }

    public NavDrawerItem getNthItem(int n) {
	   int menuSize = sysMenuObjects.size();
	    
	if (n < menuSize) {
	    return sysMenuObjects.get(n);
	}

	return pageObjects.get(n-menuSize);
    }
    
    public NavDrawerListAdapter(Context context, int textViewResourceId) {
        sysMenuObjects = new ArrayList<NavDrawerItem>();
        pageObjects = new ArrayList<NavDrawerItem>();
        lists = new CompositeListFromLists<NavDrawerItem>(sysMenuObjects, pageObjects);
        arrayAdapter = new ArrayAdapter<NavDrawerItem>(context, textViewResourceId,
                lists) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = null ;
                NavDrawerItem menuItem = this.getItem(position);
                if ( menuItem.getType() == NavMenuItem.ITEM_TYPE ) {
                    view = getItemView(convertView, parent, menuItem );
                }
                else {
                    view = getSectionView(convertView, parent, menuItem);
                }
                return view ;
            }
            
            @Override
            public int getViewTypeCount() {
                return 2;
            }
            
            @Override
            public int getItemViewType(int position) {
                return this.getItem(position).getType();
            }
            
            @Override
            public boolean isEnabled(int position) {
                return getItem(position).isEnabled();
            }
            
            private View getItemView( View convertView, ViewGroup parentView, NavDrawerItem navDrawerItem ) {
                
                NavMenuItem menuItem = (NavMenuItem) navDrawerItem ;
                NavMenuItemHolder navMenuItemHolder = null;
                
                if (convertView == null) {
                    convertView = inflater.inflate( R.layout.drawer_list_item, parentView, false);
                    TextView labelView = (TextView) convertView
                            .findViewById( R.id.navmenuitem_label );
                    //ImageView iconView = (ImageView) convertView
                     //       .findViewById( R.id.navmenuitem_icon );

                    navMenuItemHolder = new NavMenuItemHolder();
                    navMenuItemHolder.labelView = labelView ;
                    //navMenuItemHolder.iconView = iconView ;

                    convertView.setTag(navMenuItemHolder);
                }

                if ( navMenuItemHolder == null ) {
                    navMenuItemHolder = (NavMenuItemHolder) convertView.getTag();
                }
                            
                navMenuItemHolder.labelView.setText(menuItem.getLabel());
               // navMenuItemHolder.iconView.setImageResource(menuItem.getIcon());
                
                return convertView ;
            }

            private View getSectionView(View convertView, ViewGroup parentView,
                    NavDrawerItem navDrawerItem) {
                
                NavMenuSection menuSection = (NavMenuSection) navDrawerItem ;
                NavMenuSectionHolder navMenuItemHolder = null;
                
                if (convertView == null) {
                    convertView = inflater.inflate( R.layout.navdrawer_section, parentView, false);
                    TextView labelView = (TextView) convertView
                            .findViewById( R.id.navmenusection_label );

                    navMenuItemHolder = new NavMenuSectionHolder();
                    navMenuItemHolder.labelView = labelView ;
                    convertView.setTag(navMenuItemHolder);
                }

                if ( navMenuItemHolder == null ) {
                    navMenuItemHolder = (NavMenuSectionHolder) convertView.getTag();
                }
                            
                navMenuItemHolder.labelView.setText(menuSection.getLabel());
                
                return convertView ;
            }
        };

        this.inflater = LayoutInflater.from(context);
    }    

    private static class NavMenuItemHolder {
        private TextView labelView;
    }
    
    private class NavMenuSectionHolder {
        private TextView labelView;
    }
}
