package trainedge.zapdiet;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;


class CheckConnection {
    private View v;
    private ImageView img;
    private Button retry;

    public CheckConnection(View v, ImageView img, Button retry){
        this.v = v;
        this.img = img;
        this.retry = retry;
    }

    public boolean checkconn() {
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkconn();
            }
        });
        v.setVisibility(View.GONE);
        img.setVisibility(View.GONE);
        retry.setVisibility(View.GONE);
        if (isOnline()) {
            v.setVisibility(View.VISIBLE);
            return true;
        } else {
            img.setVisibility(View.VISIBLE);
            retry.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }
}

