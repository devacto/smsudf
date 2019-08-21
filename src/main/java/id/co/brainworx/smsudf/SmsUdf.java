package id.co.brainworx.smsudf;

import io.confluent.ksql.function.udf.Udf;
import io.confluent.ksql.function.udf.UdfDescription;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.net.URLEncoder;

@UdfDescription(name = "sms", description = "sms a number given a message")
public class SmsUdf {

    // See https://docs.confluent.io/current/ksql/docs/developer-guide/udf.html#null-handling
    // for more information how your UDF should handle `null` input.

    @Udf(description = "sms given a number and a message")
    public String sms(final String number, final String message) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", "TEST")
                .addFormDataPart("password", "TEST")
                .addFormDataPart("sender", "TEST")
                .addFormDataPart("msisdn", number)
                .addFormDataPart("message", message)
                .build();

        Request request = new Request.Builder()
                .url("https://smsblast.id/api/sendsingle.json")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();
    }
}
