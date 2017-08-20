package plan.glo.windowplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zubairchowdhury on 08/08/2017.
 */

public class ImportCalendarAdapter extends BaseAdapter{

    private Context context;
    private static boolean DEFAULT_CHECKED = true;
    private Map<Integer, String> calendars;
    private List<Integer> calendarOrder;
    private Map<Integer, Boolean> calendarMap;
    private LayoutInflater inflater;

    public ImportCalendarAdapter( Context context, Map<Integer, String> calendars ){
        this.context = context;
        inflater = LayoutInflater.from( context );
        calendarMap = new HashMap<>();
        calendarOrder = new ArrayList<>();
        for ( Integer cal : calendars.keySet() ){
            calendarMap.put(cal, DEFAULT_CHECKED);
            calendarOrder.add(cal);
        }
        this.calendars = calendars;
    }


    @Override
    public int getCount() {
        return calendarMap.size();
    }

    @Override
    public Object getItem(int i) {
        return calendarMap.get(calendarOrder.get(i));
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {

        final int calendarId = calendarOrder.get(pos);
        final String currentName = calendars.get(calendarId);
        boolean currentSet = calendarMap.get(calendarId);

        if ( convertView == null )
            convertView = inflater.inflate( R.layout.list_item_calendar_select, null );

        TextView mTitle = (TextView) convertView.findViewById( R.id.list_item_text );
        CheckBox checkBox = (CheckBox) convertView.findViewById( R.id.list_item_checkbox );

        if ( mTitle != null ){
            mTitle.setText(currentName);
        }

        if ( checkBox != null ){
            checkBox.setChecked( currentSet );
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    calendarMap.put(calendarId, b );
                }
            });
        }

        return convertView;
    }

    public Map<Integer, Boolean> getCheckedCalendars() {
        return calendarMap;
    }
}
