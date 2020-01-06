package zientek.lukasz.learnwords.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.Nullable;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import zientek.lukasz.learnwords.R;

public class ListViewAdapter extends ArrayAdapter
{
    private Context context;
    private SparseBooleanArray selectedItemsId;
    private List selectionList;

    public ListViewAdapter(Context context, int resource, List items)
    {
        super(context,resource,items);
        this.context = context;
        this.selectedItemsId = new SparseBooleanArray();
        this.selectionList = items;
    }

    private class ViewHolder
    {
        TextView testName;
    }

    @NotNull
    public View getView(int position, View convertView, @NotNull ViewGroup parent)
    {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.test_item_layout, null);
            holder = new ViewHolder();
            holder.testName = convertView.findViewById(R.id.test_title);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        holder.testName.setText(getItem(position).toString());

        return convertView;
    }

    @Override
    public void remove(@Nullable Object object)
    {
        selectionList.remove(object);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position)
    {
        selectView(position, !selectedItemsId.get(position));
    }

    public void removeSelection()
    {
        selectedItemsId = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    private void selectView(int position, boolean value)
    {
        if (value)
            selectedItemsId.put(position, value);
        else
            selectedItemsId.delete(position);
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return selectedItemsId;
    }

}
