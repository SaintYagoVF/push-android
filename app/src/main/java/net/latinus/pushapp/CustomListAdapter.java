package net.latinus.pushapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter {

    public CustomListAdapter(Activity context,String[] dateArrayParam, String[] nameArrayParam, String[] infoArrayParam, Integer[] imageIDArrayParam){

        super(context,R.layout.listview_row , nameArrayParam);



        this.context=context;
        this.imageIDarray = imageIDArrayParam;
        this.dateArray = dateArrayParam;
        this.nameArray = nameArrayParam;
        this.infoArray = infoArrayParam;

    }

    //Activity
    private final Activity context;

    //Imagen
    private final Integer[] imageIDarray;

    //Fecha
    private final String[] dateArray;

    //Título
    private final String[] nameArray;

    //Info
    private final String[] infoArray;





    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView dateTextField = (TextView) rowView.findViewById(R.id.dateTextViewID);
        TextView nameTextField = (TextView) rowView.findViewById(R.id.nameTextViewID);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.infoTextViewID);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1ID);

        //this code sets the values of the objects to values from the arrays
        dateTextField.setText(dateArray[position]);
        nameTextField.setText(nameArray[position]);
        infoTextField.setText(infoArray[position]);
        imageView.setImageResource(imageIDarray[position]);

        return rowView;

    };
}
