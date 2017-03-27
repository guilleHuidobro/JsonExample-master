package edu.niu.cs.z1761257.jsonexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**

 */
public class StateArrayAdapter extends ArrayAdapter<State> {

    public StateArrayAdapter(Context context, List<State> resource) {
        super(context, -1, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        State state = getItem(position);

        ViewHolder viewHolder;

        //if there is not a reusable View, create one
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item,parent,false); //false - not to automatically attach for parent
            viewHolder.stateTV = (TextView)convertView.findViewById(R.id.stateTextView);

            convertView.setTag(viewHolder);

        }else{//Re-use a View

            viewHolder = (ViewHolder)convertView.getTag();

        }

        viewHolder.stateTV.setText(state.stateAbbr);
        //viewHolder.stateNumTV.setText("Number: " + state.stateNumber);

        return convertView;
    }//end of getView

    private static class ViewHolder{
        TextView stateTV;

    }//end of ViewHolder

}//end of StateArrayAdapter
