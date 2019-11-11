package zientek.lukasz.learnwords.model;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

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

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        ListViewElement listViewElement = new ListViewElement(getItem(position));
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.test_item_layout, null);
            holder = new ViewHolder();
            holder.testName = (TextView) convertView.findViewById(R.id.test_title);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        holder.testName.setText(listViewElement.getTestName());

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

    public void selectView(int position, boolean value)
    {
        if (value)
            selectedItemsId.put(position, value);
        else
            selectedItemsId.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return selectedItemsId.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return selectedItemsId;
    }

}
