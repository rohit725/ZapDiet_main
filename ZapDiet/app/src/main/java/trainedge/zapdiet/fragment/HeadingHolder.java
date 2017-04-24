package trainedge.zapdiet.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import trainedge.zapdiet.R;


public class HeadingHolder extends RecyclerView.ViewHolder {

    TextView itmnam;
    ImageView itmimg;
    View rcview;

    public HeadingHolder(View itemView) {

        super(itemView);
        itmnam = (TextView) itemView.findViewById(R.id.item_name);
        itmimg = (ImageView) itemView.findViewById(R.id.item_image);
        rcview = itemView.findViewById(R.id.rlcontainer);

    }
}
