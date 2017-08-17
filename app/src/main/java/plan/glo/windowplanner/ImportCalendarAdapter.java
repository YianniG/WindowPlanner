package plan.glo.windowplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zubairchowdhury on 08/08/2017.
 */

public class ImportCalendarAdapter extends BaseAdapter{

    private Context context;
    private Map<String, Boolean> calendarMap;
    private List<String> calendars;
    private LayoutInflater inflater;

    public ImportCalendarAdapter( Context context, List<String> calendars ){
        this.context = context;
        inflater = LayoutInflater.from( context );
        calendarMap = new HashMap<>();
        for ( String cal : calendars ){
            calendarMap.put( cal, true );
        }
        this.calendars = calendars;
    }


    @Override
    public int getCount() {
        return calendarMap.size();
    }

    @Override
    public Object getItem(int i) {
        return calendarMap.get( i );
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {

        final String currentName = calendars.get(pos);
        boolean currentSet = calendarMap.get( currentName );

        if ( convertView == null )
            convertView = inflater.inflate( R.layout.list_item_calendar_select, null );

        TextView mTitle = (TextView) convertView.findViewById( R.id.list_item_text );
        CheckBox checkBox = (CheckBox) convertView.findViewById( R.id.list_item_checkbox );

        if ( mTitle != null ){
            mTitle.setText( currentName );
        }

        if ( checkBox != null ){
            checkBox.setChecked( currentSet );
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    calendarMap.put( currentName, b );
                }
            });
        }

        return convertView;
    }
}
