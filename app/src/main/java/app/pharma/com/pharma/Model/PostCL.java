package app.pharma.com.pharma.Model;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.x;

/**
 * Created by Vi on 6/28/2017.
 */

public class PostCL extends StringRequest {

    private Map<String, String> params;

    public PostCL(String WEBURL, Map<String, String> params2, Response.Listener<String> listener) {
        super(Method.POST,ServerPath.ROOT_URL+WEBURL, listener, null);
        params = new HashMap<>();
        params = params2;
/*        params.put("accessToken", token);
        params.put("idRequest", id);*/
        this.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    @Override
    public Map<String, String> getParams() {
        return params;
    }
}