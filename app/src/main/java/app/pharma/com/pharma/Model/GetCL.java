package app.pharma.com.pharma.Model;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import static android.R.attr.x;

/**
 * Created by Vi on 6/29/2017.
 */

public class GetCL  extends StringRequest {

    public GetCL(String WEBURL, Response.Listener<String> listener) {
        super(Method.GET,ServerPath.ROOT_URL+WEBURL, listener, null);

        this.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }



}